# AGENTS.md - Netmirror Plugin Development

## Goal
- Make Netflix/PrimeVideo play via play.php→playlist.php flow (clean URLs without watermark/stop)

## Key Findings

### Hash format from play.php
- play.php returns `h=in=TOKEN1::TOKEN2::TIMESTAMP::ep::i::` (6 parts)
- The `::ep::i::` suffix is **internal/watermarked** — do NOT send to playlist.php
- Send only first 3 parts (`TOKEN1::TOKEN2::TIMESTAMP`) as `h` param to playlist.php

### playlist.php response behavior
- When sent the **3-part clean hash**, playlist.php returns the **complete working hash** directly in the source file URL
- Working URL format (curl): `/hls/ID.m3u8?in=TOKEN1::TOKEN2::TIMESTAMP::ep::p::TOKEN3` (6 parts with `::ep::p::`)
- **net11.cc returns different format**: `/hls/ID.m3u8?in=TOKEN1::TOKEN2::TIMESTAMP::ep::99` (5 parts, `::ep::99`)
  - This format partially works — episode 1 plays but episode 9 gets `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED`
  - `99` might be degraded/limited access (missing proper TOKEN3)
  - net11.cc playlist.php works **without** postMessage cookies (unlike net52.cc)
- **net52.cc playlist.php returns `unknown::ep`** — requires postMessage cookies (user_token, t_hash_p, ott)
- **Both TOKEN1 and TOKEN2** change between play.php hash and M3U8 URL (not just TOKEN2)
- **Timestamp also changes** between play.php and M3U8 URL (~65s added)
- **No cookies needed** for the M3U8 URL itself — the `in` hash carries all auth
- If playlist.php returns `unknown::ep`, the hash exchange failed — falls through to player.php

### Header/Cookie fix
- `getVideoInterceptor` was **overwriting** Cookie to just `hd=on`, stripping `t_hash_t`
- Fix: preserve existing cookies, only add `hd=on` if missing
- Simplified `androidHeaders` to remove unneeded sec-* headers that might confuse CDN
- Changed Referer from `$mainUrl/mobile/home?app=1` to `$mainUrl/`

### postMessage cookies (user_token, t_hash_p, ott)
- These are set via JavaScript `postMessage`, **not** HTTP `Set-Cookie` headers
- net22.cc play.php page loads net52.cc/play.php in an iframe; the iframe sends `parent.postMessage("key=value", "*")`
- `Utils.kt:getPlaylistUrl()` extraction approaches:
  1. GET `play.php?in=...` with `Accept: text/html` → find iframe src (HTTP, fails: err:1003/null)
  2. Fetch iframe URL → regex for `parent.postMessage(...)` (HTTP, fails: same)
  3. **WebView** fallback: load parent page first, let it render iframe naturally (auto-Referer)
- **net11.cc does NOT need postMessage cookies** — its playlist.php returns valid M3U8 even without them (but with degraded `::ep::99` format)
- Domain priority changed: `mainUrl` (net52.cc) tried first in play.php POST API loop
- **KEY INSIGHT**: `bypass()` successfully POSTs to `net52.cc/verify.php` with `Referer: net22.cc/verify2`. net52.cc accepts requests with the right Referer.
- **UPDATE (Jul 5, 2026)**: net22.cc is **DEAD** (`Unable to resolve host`). Can't use parent page approach.
- **NEW TRY (Jul 5, 2026)**: Add `::ep::p::TOKEN2` format variants to play.php and playlist.php calls. The curl example shows play.php accepts `in=...::ep::p::TOKEN3` format (not `::ep::i::`). Maybe net52.cc blocks `::ep::i::` hashes.
- **KEY INSIGHT**: `bypass()` successfully POSTs to `net52.cc/verify.php` with `Referer: net22.cc/verify2` and `Origin: net22.cc`. net52.cc rejects play.php with err:1003 when called with `Referer: net52.cc/` and `Origin: net52.cc`. **The Referer/Origin matters** — net52.cc expects cross-origin requests from net22.cc.
- **NEW TRY (Jul 5, 2026 v2)**: When calling `net52.cc/play.php`, set `Referer: https://net22.cc/play.php?id=X` and `Origin: https://net22.cc` (same as the real iframe flow). net22.cc DNS is dead but we only need the header value as a string.

## DEBUG MODE
- In `Utils.kt`, `Log.e("PLAYURL", url)` prints the final M3U8 URL at ERROR level for easy copy-paste

## Implementation Details
- `Utils.kt:getPlaylistUrl()` — clean 3-part hash + PM cookie extraction (HTTP + WebView fallback) + playlist.php call
- `Utils.kt:capturePmCookiesViaWebView()` — hidden WebView with `@JavascriptInterface`, intercepts `window.postMessage` override and `message` event listener, bails on `err:1003`, polls for cookies (10s max)
- `NetflixmirrorPlugin.kt` — sets `pluginContext` for WebView
- `NetflixProvider.kt:loadLinks()` — play.php flow with fallback to player.php
- `NetflixProvider.kt:getVideoInterceptor()` — returns null (no interceptor)
- `PrimevideoProvider.kt:loadLinks()` — same structure as Netflix
- `PrimevideoProvider.kt:getVideoInterceptor()` — returns null (no interceptor)

## Current Status (Jul 5, 2026)

### Summary
- play.php→playlist.php flow WORKS on net11.cc: returns valid M3U8 with `::ep::99` format
- **CDN domains `s*.nm-cdn*.top` are PRIVATE/INTERNAL** — give NXDOMAIN (no public DNS)
- No publicly accessible CDN serves the segment paths (`/files/...` or `/hls/...`) → all 404
- The HLS streaming approach via netmirror API is FUNDAMENTALLY BLOCKED by private CDN infrastructure

### What Works
- net52.cc bypass (verify.php) → `t_hash_t` cookie ✓
- net11.cc play.php → hash ✓
- net11.cc playlist.php → M3U8 URL with `::ep::99` ✓
- Main M3U8 is valid (#EXTM3U, proper structure) ✓
- player.php fallback → M3U8 on `tv.imgcdn.kim` with watermark/stop ✓

### What Does NOT Work
- CDN segment URLs → NXDOMAIN (`s*.nm-cdn*.top`) or 404 (any public CDN)
- No path substitution works (`/files/` → `/hls/`, different CDN hosts)
- `check.php` — doesn't exist on net52.cc or net11.cc
- `userver.net52.cc/?jjoii=X` — returns "User ID Expired" (needs auth token)
- Interceptor CDN rewrites — reset to null (all strategies exhausted)

### Root Cause
- The video segments are hosted on private CDN servers (`sXX.nm-cdnYY.top`) that are only accessible from within the netmirror network
- Without internal network access or proper Cloudflare session, segments are unreachable
- No amount of header/path tweaking can make inaccessible CDNs work

### API Endpoints Probed
| Endpoint | Result |
|---|---|
| `/check.php` (all domains) | 404 / "File not found." |
| `/newtv/check.php` (all domains) | 404 (Apache) |
| `userver.net52.cc/?jjoii=ID` | "User ID Expired." |
| `net52.cc/hls/ID.m3u8?in=unknown::ep` | 404 |
| `net11.cc/hls/ID.m3u8?in=::ep::99` | **Valid M3U8** ✓ (but CDN segments NXDOMAIN) |

### Prime Video Status
- Same as Netflix — play.php→playlist.php returns M3U8 but CDN segments not accessible
- player.php fallback — watermarked/stopped video

### Next Steps (if any)
- **Decompile current netmirror app** to find the `userver` jjoii token algorithm
- **Implement local HTTP proxy** that serves modified M3U8 with all segment requests proxied through the same endpoint as the M3U8 (requires server-side proxying)
- **Accept player.php fallback** (watermarked) as the only working playback option
