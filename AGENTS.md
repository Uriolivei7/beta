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

### Major Breakthrough (07 Jul 2026): Bypass fixed — `POST verify.php` returns t_hash_t
**`POST https://net52.cc/verify.php` with ANY `g-recaptcha-response` value returns `Set-Cookie: t_hash_t=...` in a 301!**

No captcha needed — server accepts any random token and issues a valid t_hash_t (3-day expiry).

**Root cause of previous bypass failure:**
- CloudStream's `Requests.post()` follows HTTP redirects
- The 301 from net52.cc/verify.php redirects to `/home` (sandbox) or net11.cc/verify.php (user device)
- net11.cc has NXDOMAIN on user device → exception thrown, discarding the Set-Cookie
- **Fix**: OkHttp with `followRedirects(false)` + `followSslRedirects(false)` captures the cookie before any redirect

**Token format:**
```
t_hash_t=a3398a4472afd0d309c3e64f5b011898%3A%3Accd8cadf32a6b2247d930975c2e47431%3A%3A1783429065%3A%3Aep%3A%3A99
```
URL-encoded (`%3A%3A` = `::`) → URL-decode → `hash1::hash2::timestamp::ep::99`

### Token Usage
| Source | Raw value | For Cookie header | For URL `in=` replace |
|---|---|---|---|
| POST verify.php | `h1::h2::ts::ep::99` | Full value | `substringBefore("::ep")` → `h1::h2::ts` |
| POST net11.cc/play.php | `h1::h2::ts` | Same | Same (no ::ep suffix) |

### Updated bypass flow
1. ✅ Cached cookie check (<10 min)
2. ✅ GET `tv/p.php` → 403 (Cloudflare)
3. ✅ WebView bypass (5s timeout → fallback)
4. **✅ HTTP POST `verify.php` via OkHttp no-redirect → extracts t_hash_t**

### Files Changed
- `Utils.kt` — bypass(): replaced `app.post()` with OkHttp `followRedirects=false`; added URLDecoder; WebView timeout 30→5s; interceptor uses `substringBefore("::ep")`
- `NetflixProvider.kt` / `PrimevideoProvider.kt` — try bypass() first, getPlayHash() as fallback

### Relevant Files
- `Utils.kt` — `bypass()`, `resolveApiUrl()`, `newTvBaseHeaders`, data classes, interceptor
- `NetflixProvider.kt` / `PrimevideoProvider.kt` — `loadLinks()` with original player.php test first, then playlist.php primary, then /newtv/player.php fallback
- `CNC Verse Mobile/classes.dex_Decompiler.com/sources/com/horis/cncverse/` — decompiled reference

## Next Steps
1. ✅ **Fix `newTvBaseHeaders`** — done
2. ✅ **Remove `usertoken` from `NewTvPlayerResponse`** — done
3. ✅ **Fix bypass (POST verify.php with no-redirect)** — done
4. 🔲 **Test on user device** — verify which endpoint returns full-length content (not 10-min preview):
   - Primary: `$mainUrl/player.php?id=ID` (original, no `/newtv/` prefix) with t_hash_t cookie
   - Fallback 1: `playlist.php` (existing flow, returns 10-min preview)
   - Fallback 2: `tv.imgcdn.kim/newtv/player.php` (existing flow, returns 10-min preview)
5. 🔲 Once tested: if original player.php works, simplify by removing playlist.php and /newtv/player.php fallback entirely
