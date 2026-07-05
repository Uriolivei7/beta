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

### Key insight: All endpoints return preview-only without Cloudflare session
- **net52.cc**: ALL hash variants on direct M3U8 (`cleanHash`, `playHash`, `hashWithP`, `playHashP`) return **same 3463 bytes** — preview M3U8. Hash format does NOT affect auth level.
- **net52.cc play.php**: `err:1002/1003` for all formats including `::ep::p::TOKEN3` with real t_hash_t
- **net52.cc playlist.php**: `unknown::ep` for all hash variants — hash exchange fails server-side
- **net52.cc home**: Cloudflare JS challenge (5595 bytes "Just a moment...")
- **net11.cc**: play.php returns hash ✓ → playlist.php returns `::ep::99` (degraded/preview)
- **net11.cc direct M3U8**: Returns 3160-2908 bytes with **broken CDN URLs** (`https:///files/...` triple slash)
- **API player.php** (`tv.imgcdn.kim`): Returns M3U8 but only 10-min preview regardless of hash

### Root Cause
- net52.cc requires Cloudflare session + proper auth cookies for full episode access
- Without solving Cloudflare JS challenge, all endpoints serve preview-only
- No hash format workaround (`::ep::p::TOKEN3`, `::ep::99`, `::ep::i::`) bypasses server-side auth

### Bug Fixes Applied (Jul 5, 2026 v4)
1. **`playlist.php` cross-origin headers**: net52.cc now sends `Origin: net22.cc`, `Referer: net22.cc/play.php?id=X` (same as play.php pattern) — may change `unknown::ep` response
2. **Direct M3U8 cross-origin**: Added `Origin` header + changed `Referer` to cross-origin for net52.cc
3. **Body comparison bug**: `foundSource` was comparing `body.length > URL_string.length` (always true for valid M3U8), making LAST variant/domain always win. Fixed to track `bestBodyLen` — now largest body wins (net52.cc 3463 bytes preferred over net11.cc 3160 bytes)
4. **Play.php POST Referer**: Changed from `net22.cc/verify2` to `net22.cc/play.php?id=$id` for net52.cc (matching expected iframe flow)

### What Works
- net52.cc bypass (verify.php) successfully returns `t_hash_t`: `verify.php status=200 cookies={hd=on, t_hash_t=...}`
- net11.cc play.php returns hash with `::ep::i::` format ✓
- net11.cc playlist.php returns M3U8 URL with `::ep::99` format ✓
- Direct M3U8 on net52.cc returns 3463 bytes (10-min preview) with proper CDN `s23.nm-cdn9.top`

### What Does NOT Work
- Full episode playback — all paths give 10-min preview or broken URLs
- net52.cc play.php — err:1002/1003 regardless of hash format or headers
- net52.cc playlist.php — `unknown::ep` regardless of hash format or headers (even after cross-origin fix)
- net11.cc direct M3U8 — broken CDN URLs (`https:///files/...`)
- `::ep::99` long episodes — fail with `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED`

### Remaining unknowns
- Is there a different API endpoint (not player.php, playlist.php) that returns full episodes?
- Does net52.cc playlist.php change behavior with cross-origin headers (needs testing)?
- Is there a way to solve Cloudflare from within the app (Headless Chrome, etc.)?

## Prime Video Status
- playlist.php returns "Video ID not found!" for ott=pv (non-Netflix titles) — expected
- Falls back to player.php which returns watermarked/stopped video
- No fix yet for Prime Video clean URLs
