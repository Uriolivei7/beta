# AGENTS.md - Netmirror Plugin Development

## Goal
- Migrate Netflix/PrimeVideo providers from broken netXX.cc API to cncverse NewTv API (mobiledetects domains) to get working, non-watermarked streams

## Constraints & Preferences
- Must work as a CloudStream plugin (not standalone APK)
- Must mirror verified working decompiled cncverse APK code

## Progress
### Done
- Fully decompiled cncverse APK (`com.horis.cncverse`) from classes.dex via JADX + Decompiler.com — identified 24 base64-decoded mobiledetect domains, NewTv entity models, auth headers, cookie storage, provider structure
- All decompiled Java sources preserved at `CNC Verse Mobile/classes.dex_Decompiler.com/sources/com/horis/cncverse/`

### Key Discoveries from Decompiled Code

#### `NewTvPlayerResponse` (player.php fallback)
- **Has NO `usertoken` field** — only `status`, `video_link`, `referer`
- Our added `usertoken` field is purely diagnostic; cncverse doesn't use it
- `player.php` is the **fallback** path, not the primary flow

#### Primary flow: `playlist.php` → `Source[]`
- The primary endpoint returns a **list of `Source` objects**: `file` (M3U8 URL), `label` (quality name), `type` (mime type)
- cncverse maps each Source to an `ExtractorLink` with:
  - `headers = buildNewTvHeaders(ott, extra)` (newTvBaseHeaders + Ott header)
  - `referer = "$mainUrl/mobile/home?app=1"`
  - `quality = parsed from source file's "q=" parameter`
- The response structure is a JSON array of items, each containing sources and tracks

#### `loadLinks` flow (from continuation metadata)
1. Parse `data` JSON → extract `title`/`id`
2. Load `cookies` from `NetflixMirrorStorage.getCookie()`
3. Build `cookieStr` = headers map entries joined as `"key=value"`
4. Build `playlistHeaders` via `buildNewTvHeaders(ott, extra)`
5. Call playlist endpoint → parse `playlist` response
6. Map through items → sources → create ExtractorLinks
7. Process tracks (subtitles) with `Referer: $mainUrl/`

#### Correct headers (from decompiled `UtilsKt.newTvBaseHeaders`)
```
Cache-Control: no-cache, no-store, must-revalidate
Pragma: no-cache
Expires: 0
X-Requested-With: NetmirrorNewTV v1.0    ← OUR VALUE WAS WRONG (app.netmirror.netmirrornew)
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0 /OS.GatuNewTV v1.0
           ← OUR VALUE WAS WRONG (Chrome/149.0.7827.91 /OS.Gatu v3.0 with sec-ch-* headers)
Accept: application/json, text/plain, */*
```
Note: Original has NO `sec-ch-*`, NO `Accept-Language`, uses Firefox UA with `/OS.GatuNewTV v1.0`

#### Provider-level `headers` (browser scraping on net52.cc)
```
User-Agent: Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/144.0.7559.132 Safari/537.36 /OS.Gatu v3.0
X-Requested-With: XMLHttpRequest
sec-ch-ua: "Not(A:Brand";v="8", "Chromium";v="144", "Android WebView";v="144"
Sec-Fetch-Dest: document, Sec-Fetch-Mode: navigate, Sec-Fetch-Site: same-origin, Sec-Fetch-User: ?1
```

#### `getVideoInterceptor`
- Identical to ours — sets `Cookie: hd=on` on `.m3u8` requests

#### `NetflixMirrorStorage` (SharedPreferences)
- **`nf_cookie`** + `nf_cookie_timestamp` — the bypass token
- **`nf_cookie_full`** + `nf_cookie_full_timestamp` — the full cookie string (separate storage)
- App name: `NetflixMirrorPrefsMobile`

#### `bypass()` signature
- `suspend fun bypass(mainUrl: String): String`
- Only called with `mainUrl` (net52.cc) — NOT with newTv API URL
- Uses `app` object (Chrome UA Requests instance) for HTTP calls
- Has retry counter (`count` variable)
- Posts to verify.php with form body containing `g-recaptcha-response`
- Falls back if verify.php fails

#### `resolveApiUrl()` sequence
- Tries each mobiledetect domain → `checknewtv.php` → base64-decode → API URL
- Falls back to pinging domain roots
- Falls back to `mainUrl`

### Endpoint Behavior
| Endpoint | Domain | Behavior |
|---|---|---|
| `checknewtv.php` | mobiledetects.* | Returns base64-encoded API URL |
| `verify.php` | net52.cc | GET → captcha page; POST → Cloudflare 403 |
| `verify.php` | tv.imgcdn.kim | 404 (not found) |
| `player.php` | tv.imgcdn.kim | Returns `{status: "otp"/"ok", video_link: "...", referer: "..."}` |
| `playlist.php` | netXX.cc | Returns `Source[]` with M3U8 URLs (primary flow) |

### Current Blockers
1. **`bypass()`** fails — all mobiledetect.* domains unreachable (connection timeout after 10s, ALL 24 domains); no token_hash retrieved anywhere
2. **`playlist.php` returns relative paths (`/hls/ID.m3u8?in=unknown::ep`)** — 404 on `tv.imgcdn.kim`; correct path from player.php is `/newtv/hls/nf/ID.m3u8`
3. **`$apiBase/newtv/playlist.php` = "File not found."** — playlist endpoint only works on `mainUrl` (net52.cc), not on tv.imgcdn.kim

### Key Open Questions
- What is the correct HLS base URL for the playlist.php relative paths? (net52.cc? net11.cc?)
- Why do all 24 mobiledetect.* domains timeout from this network (172.23.x.x private IP)?
- Could `in=unknown::ep` (invalid hash) change the M3U8 path? Would a valid hash produce different paths?
- How does cncverse bypass work if mobiledetect domains are unreachable? Does it use a hardcoded API URL?

### Relevant Files
- `Utils.kt` — `bypass()`, `resolveApiUrl()`, `newTvBaseHeaders` (fixed), `NewTvPlayerResponse` (no usertoken), data classes (`Source`, `PlaylistItem`, `PlaylistTrack`), `getPlaylistUrl` (kept for JioHotstar)
- `NetflixProvider.kt` — `loadLinks()` with playlist.php→Source[] primary flow + player.php fallback
- `PrimevideoProvider.kt` — same structure
- `CNC Verse Mobile/classes.dex_Decompiler.com/sources/com/horis/cncverse/` — decompiled reference

## Next Steps
1. ✅ **Fix `newTvBaseHeaders`** — done, matches decompiled values
2. ✅ **Remove `usertoken` from `NewTvPlayerResponse`** — done
3. ✅ **Implement `playlist.php` → `Source[]` primary flow** — done in both providers
4. 🔲 **Find correct HLS base URL** — determine whether `/hls/ID.m3u8` resolves to net52.cc, net11.cc, or requires a valid auth hash to even exist
5. 🔲 **Fix bypass** — mobiledetect.* unreachable; consider hardcoding `apiBase = https://tv.imgcdn.kim` as fallback or investigating why network blocks these domains
6. 🔲 **if bypass stays broken** — investigate whether cncverse uses hardcoded API URL or different auth mechanism that doesn't depend on mobiledetect domains
