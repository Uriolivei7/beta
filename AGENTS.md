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
  3. **WebView** fallback: loads net52.cc/play.php in hidden WebView, injects JS to intercept postMessage (also fails: err:1003 in WebView too)
- **net11.cc does NOT need postMessage cookies** — its playlist.php returns valid M3U8 even without them (but with degraded `::ep::99` format)
- Domain priority changed: `mainUrl` (net52.cc) tried first in play.php POST API loop

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

## Verified Working URL (curl example)
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
