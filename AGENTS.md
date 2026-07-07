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
- `NetflixProvider.kt` / `PrimevideoProvider.kt` — try bypass() first, getPlayHash() as fallback; original player.php test (confirmed "File not found") replaced with direct play.php→M3U8 flow as primary

### Relevant Files
- `Utils.kt` — `bypass()`, `resolveApiUrl()`, `newTvBaseHeaders`, data classes, interceptor
- `NetflixProvider.kt` / `PrimevideoProvider.kt` — `loadLinks()` with direct M3U8 primary (play.php hash), then playlist.php fallback, then /newtv/player.php fallback
- `CNC Verse Mobile/classes.dex_Decompiler.com/sources/com/horis/cncverse/` — decompiled reference

## Status Update (07 Jul 2026)
### What WAS tried (all failed for full content):
| Attempt | Result |
|---|---|
| `POST net52.cc/play.php` | **Dead** — returns `Page Not Found! err: 1002` |
| `GET net52.cc/player.php` | **Dead** — returns `File not found.` |
| `GET/POST net52.cc/newtv/player.php` | Returns `status:ok` + 10-min preview (same CDN, same 1961 bytes) |
| `tv.imgcdn.kim/hls/` base | **404 error 2004** — removed from hlsBases (fixed) |
| Cookie suffix `::ep::99` → `::ep::m` | **No change** — CDN still returns 60 segments |
| URL `in=` token format changes | **No change** — CDN returns 1961 bytes regardless |
| Both content IDs (81936153, 81403113) | **Both** return 10-min preview |
| `hd=on` cookie in ExtractorLink headers | **No change** — CDN returns 1961 bytes |
| Interceptor overwrites Cookie → `hd=on` on M3U8 (matching cncverse) | **No change** — CDN returns 1961 bytes |

### Root cause
The CDN **`s21.freecdn4.top`** enforces a **10-minute preview** (60 segments, ~1961 bytes per variant playlist) on ALL tokens/parameters we've tried. The cncverse API (`playlist.php` / `player.php`) only returns preview-quality HLS URLs pointing to this CDN.

### Decompiled code replication status (ALL matched):
| Aspect | cncverse (decompiled) | Our plugin | Match? |
|---|---|---|---|
| `newTvBaseHeaders` (6 headers) | Firefox UA + NetmirrorNewTV v1.0 | Same | ✅ |
| `buildNewTvHeaders(ott, extra)` | Base + Ott + extra | Same | ✅ |
| Bypass flow | Cache → tv/p.php → WebView → POST verify.php | Same | ✅ |
| Cookie storage | `nf_cookie` + `nf_cookie_full` via SharedPrefs | Same (bypass saves) | ✅ |
| `loadLinks` flow | cookies → playlistHeaders → playlist.php → Source[] → ExtractorLinks | Same | ✅ |
| ExtractorLink setup | `setHeaders(playlistHeaders)`, referer=`mainUrl/mobile/home?app=1` | Same | ✅ |
| `getVideoInterceptor` | Overwrite Cookie → `hd=on` on M3U8 requests | Same | ✅ |
| Token in URL | `in=substringBefore("::ep") + "::ep"` | Same | ✅ |
| Subscription model | Ads mode vs ₹20/mo subscription (ads removal only) | N/A | N/A |

### Conclusion
The **cncverse API is fundamentally limited to 10-minute previews** for the free tier. The subscription (₹20/month) only removes ads — it does NOT unlock full content. The decompiled code shows no premium API path or different CDN for full content. Either:
1. Full content requires a separate paid API tier not present in the free decompiled APK
2. The cncverse app uses a different mechanism (server-side proxy, different CDN contract) not visible in the code

### Relevant Files
- `Utils.kt` — `bypass()`, `resolveApiUrl()`, `newTvBaseHeaders`, data classes, interceptor
- `NetflixProvider.kt` / `PrimevideoProvider.kt` — `loadLinks()` with playlist.php primary, player.php fallback
- `Mobile/classes.dex_Decompiler.com/sources/com/horis/cncverse/` — decompiled reference

### Architectural Change (07 Jul 2026)
| Change | Description |
|---|---|
| `getMainPage`/`search`/`load`/`getEpisodes` | Now use `apiBase` (tv.imgcdn.kim → `resolveApiUrl()`) with `mainUrl` (net52.cc) as fallback for `/newtv/` endpoints |
| `loadLinks` | Already had this pattern — no change needed |
| Scraping directo | Implementado como primary method en `loadLinks` — parsea HTML de `net52.cc/{netflix,movie,tv,watch}/{id}` buscando M3U8 |
| `Descompilado NET/` | Verificado: es la app NetflixMirror standalone original (React Native, bytecode Hermes). No útil para extraer hash |

### Current Status
- **net52.cc y tv.imgcdn.kim están caídos** (07 Jul 2026) — no se puede probar nada
- Cuando el sitio regrese, la prioridad es investigar cómo cncverse reproduce los videos completos (sniffear tráfico, comparar con el código decompilado)

## Next Steps
1. ✅ **Fix `newTvBaseHeaders`** — done
2. ✅ **Remove `usertoken` from `NewTvPlayerResponse`** — done
3. ✅ **Fix bypass (POST verify.php with no-redirect)** — done
4. ✅ **Fix tv.imgcdn.kim 404 error** — removed from hlsBases
5. ✅ **Test `hd=on` cookie + interceptor Cookie overwrite** — done, no change
6. ⏸️ **Website down** — net52.cc y tv.imgcdn.kim no responden. Pausado hasta que vuelvan.
7. 🔲 **Cuando el sitio regrese:**
   - Sniffear tráfico de cncverse para ver cómo obtiene contenido completo (no preview)
   - Descifrar cómo el CDN `nm-cdn9.top` (full content) se diferencia de `s21.freecdn4.top` (preview 10 min)
   - Probar scraping directo de net52.cc (ya implementado)
   - Si nada funciona, evaluar WebView embebido (capturar M3U8 del tráfico WebView)
