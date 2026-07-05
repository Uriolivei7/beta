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

### Key insight: Mobile app API vs Web domains
- **Web domains** (net52.cc, net11.cc): play.php POST works → returns hash. playlist.php converts hash to URL.
  - net11.cc playlist.php returns `::ep::99` (degraded, fails for long episodes)
  - net52.cc playlist.php returns `unknown::ep` (needs PM cookies)
- **API domain** (from `checknewtv.php` → `resolveApiUrl()`): `post.php`, `player.php`, etc.
  - `player.php?id=X` returns video URL (watermarked in fallback)
  - **Maybe app calls `player.php?id=X&h=HASH` with the play hash → clean URL?**

### New try: API player.php with hash (Jul 5, 2026 v3)
- When playlist.php fails on ALL web domains, fall back to `$apiBase/newtv/player.php?id=$id&h=$hash`
- Passes `h=` and `in=` variants with the 3-part clean hash
- Mobile app may use this endpoint instead of playlist.php
- `::ep::p::TOKEN3` = clean format (from curl example, works with proper auth)
- `::ep::99` = degraded format (missing TOKEN3, preview/limited)
- `::ep::i::` = internal/watermarked format (from play.php POST, **don't** send to playlist.php)

net52.cc playlist.php returns `unknown::ep` → fails completely (needs PM cookies)
net11.cc playlist.php returns `::ep::99` → works for **short** episodes (1-2, ~26min), **fails** for long episode 9 (59min) with `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED`

Theory: `::ep::99` segments exist on net11.cc only for short episodes. net52.cc CDN swap served episode 9's first ~10min (watermarked) but net11.cc has no segments at all for episode 9 with `::ep::99` format.

### CDN Swap fails
- Replacing net11.cc→net52.cc in the M3U8 URL breaks ALL episodes
- net52.cc CDN only serves `::ep::p::TOKEN3` segments, not `::ep::99` segments
- Episodes 1-2 work on net11.cc CDN because its `::ep::99` segments exist for shorter episodes

### BREAKTHROUGH: Fake Referer/Origin changed err:1003 → err:1002 (Jul 5, 2026)
- **When sending `Origin: net22.cc` + `Referer: net22.cc/play.php?id=X` to net52.cc play.php GET:**
  - Error CHANGED from `err:1003` (body len=98) to `err:1002` (body len=34)
  - This proves net52.cc processes the request differently with cross-origin headers
  - Theory: `err:1003` = origin/referer validation failed; `err:1002` = hash format validation failed
  - **Progress**: we passed origin check, now failing at a different check
- **New bypass POST change**: `Utils.kt:688-697` now also sends cross-origin `Referer: net22.cc/verify2` + `Origin: net22.cc` when POSTing to net52.cc/play.php
  - May let us get a net52.cc-style hash (with `::ep::p::` format instead of `::ep::i::`)

### ::ep::99 weak fallback + API player.php (Jul 5, 2026)
- **Problem**: API player.php fallback was NEVER triggered because `::ep::99` didn't contain "unknown" — code accepted it as valid
- **Fix**: `Utils.kt:940-947` — when playlist.php returns M3U8 with `::ep::99`, store it as `weakFallback`, then try API player.php with hash
- **New flow**:
  1. Try playlist.php on all domains (net52.cc → unknown, net11.cc → ::ep::99)
  2. If `::ep::99` found → save as weak fallback → try API player.php with hash
  3. If API player.php succeeds → use its clean URL
  4. If API player.php fails → use `::ep::99` fallback
- API player.php now tries 3 hash formats (clean 3-part, full 5-part, ::ep::p::) with both `h=` and `in=` params
- API player.php headers aligned with original fallback: `Referer: https://net52.cc`

### Current Code Changes (Jul 5, 2026)
1. `bypass()` POST to net52.cc now uses `Origin: net22.cc`, `Referer: net22.cc/verify2` (cross-origin)
2. `::ep::99` treated as weak fallback — API player.php tried even when ::ep::99 succeeds
3. API player.php tries 3 hash formats × 2 param names = 6 combinations
4. API player.php uses `Referer: https://net52.cc` (matching original fallback)
5. Default PM cookies: `user_token=id`, `t_hash_p=t_hash_t` (from bypass) instead of empty
6. Added direct M3U8 URL fallback: `/hls/ID.m3u8?in=HASH` (curl example pattern)
7. `bypass()` now logs cached cookie status and Home page body (first 500 chars)

### Confirmed: net52.cc/home has Cloudflare challenge
- `home page len=5595 first 500=<!DOCTYPE html>...Just a moment...` — Cloudflare JS challenge
- Cannot extract play hash from home page

### Confirmed: API player.php with hash returns M3U8 but only 10-min preview
- URL: `https://tv.imgcdn.kim/newtv/hls/nf/81936153.m3u8` — plays but limited to ~10 min
- Probably watermarked/preview version regardless of hash parameter
- NOT a full replacement for playlist.php flow

### Remaining unknowns
- Does faking `user_token=id` + `t_hash_p=t_hash_t` change net52.cc playlist.php response?
- Does direct M3U8 URL (`/hls/ID.m3u8?in=HASH`) work on net52.cc?
- Is net52.cc play.php err:1002 a permanent block, or is there a header/param we haven't tried?

### Verified Working URL (curl example)
```
curl 'https://net52.cc/hls/81936153.m3u8?in=TOKEN1::TOKEN2b::TIMESTAMP::ep::p::TOKEN3' \
  -H 'referer: https://net52.cc/play.php?id=81936153&in=TOKEN1::TOKEN2a::TIMESTAMP::ep::p::TOKEN3'
```
- TOKEN2 changes between referer and M3U8 URL (playlist.php re-issues it)
- TOKEN3 stays the same
- Domain: net52.cc (not net22.cc / mainUrl)

## Prime Video Status
- playlist.php returns "Video ID not found!" for ott=pv (non-Netflix titles) — expected
- Falls back to player.php which returns watermarked/stopped video
- No fix yet for Prime Video clean URLs
