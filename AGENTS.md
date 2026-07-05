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
- Working URL format: `/hls/ID.m3u8?in=TOKEN1::TOKEN2::TIMESTAMP::ep::p::TOKEN3` (6 parts with `::ep::p::`)
- **TOKEN2 differs** between play.php response (`TOKEN2a`) and playlist.php response (`TOKEN2b`) — server regenerates it
- **No cookies needed** for the M3U8 URL itself — the `in` hash carries all auth
- If playlist.php returns `unknown::ep`, the hash exchange failed — code now falls through to player.php (the old `replace("unknown::ep", playHash)` was broken: it produced 9-part URL with wrong `::ep::i::` suffix)

### Header/Cookie fix
- `getVideoInterceptor` was **overwriting** Cookie to just `hd=on`, stripping `t_hash_t`
- Fix: preserve existing cookies, only add `hd=on` if missing
- Simplified `androidHeaders` to remove unneeded sec-* headers that might confuse CDN
- Changed Referer from `$mainUrl/mobile/home?app=1` to `$mainUrl/`

### postMessage cookies (user_token, t_hash_p, ott)
- These are set via JavaScript `postMessage`, **not** HTTP `Set-Cookie` headers
- net22.cc play.php page loads net52.cc/play.php in an iframe; the iframe sends `parent.postMessage("key=value", "*")`
- `Utils.kt:getPlaylistUrl()` now extracts these by:
  1. GET `play.php?in=...` with `Accept: text/html` → find iframe src
  2. Fetch iframe URL → regex for `parent.postMessage("user_token=...", "*")` etc.
  3. Merge `user_token`, `t_hash_p`, `ott` into cookie before playlist.php call
  4. Fallback: also try `play.php?h=...` parameter format

## DEBUG MODE
- In `Utils.kt`, `Log.e("PLAYURL", url)` prints the final M3U8 URL at ERROR level for easy copy-paste

## Implementation Details
- `Utils.kt:getPlaylistUrl()` — line 563-690: clean 3-part hash + postMessage cookie extraction + playlist.php call
- `NetflixProvider.kt:loadLinks()` — play.php flow with fallback to player.php
- `NetflixProvider.kt:getVideoInterceptor()` — fixed cookie preservation
- `PrimevideoProvider.kt:loadLinks()` — same structure as Netflix
- `PrimevideoProvider.kt:getVideoInterceptor()` — same fix

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
