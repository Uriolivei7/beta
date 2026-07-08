# AGENTS.md - Netmirror Plugin Development

## Goal
- Proveer streams completos desde cncverse para Netflix/PrimeVideo/JioHotstar providers en CloudStream.

## ADCIONAL DECOMPILATION (08 Jul 2026) — NEWEST VERSION
El folder `adicional/` contiene una versión MÁS NUEVA de cncverseMobile que el folder `aplicación/`.
Diferencias clave:

### bypass() — COMPLETAMENTE DECOMPILADO
```kotlin
// Cache: 15 horas (54000000ms)
// Headers: Chrome 147 Windows, Origin/Referer: net22.cc
// POST https://net52.cc/verify.php
// Body: g-recaptcha-response=${UUID.randomUUID()}
// OkHttp followRedirects(false) + followSslRedirects(false)
// Parse Set-Cookie → t_hash_t=
```

### NUEVO: getNewTvUserToken(apiBase, ott) — OTP-based auth
Variables: savedToken, otpHeaders, savedTimestamp
Data class: NewTvOtpResponse { otp, status, usertoken, pub_msg, ... }
Cache: nf_cookie_full + nf_cookie_full_timestamp (15h)

### loadLinks — FLUJO CAMBIADO (vs version vieja playlist.php)
| Versión | Auth | Endpoint | Variables clave |
|---------|------|----------|-----------------|
| VIEJA (`aplicación`) | Solo t_hash_t cookie | playlist.php | playlist, item, source, track |
| NUEVA (`adicional`) | t_hash_t + userToken (OTP) | **mobile/hls** | apiBase, userToken, response |

### Per-Provider Headers (cada provider tiene su propio `headers` field)
| Provider | X-Requested-With | User-Agent |
|----------|-----------------|------------|
| Netflix | `XMLHttpRequest` | Chrome 144 WebView |
| PrimeVideo | `XMLHttpRequest` | Chrome 144 WebView |
| HotStar | `XMLHttpRequest` | Chrome 144 WebView |
| DisneyPlus | `app.netmirror.netmirrornew` | Chrome 144 WebView |

### getVideoInterceptor — SIMPLE (solo Cookie: hd=on)
```java
// Solo agrega Cookie: hd=on a .m3u8 requests
// NO rewrite de CDN, NO in= parameter manipulation
```

## NEW FLOW (implemented 08 Jul 2026)
```kotlin
// loadLinks:
// 1. resolveApiUrl() → apiBase
// 2. bypass(mainUrl) → t_hash_t cookie (15h cache)
// 3. getNewTvUserToken(apiBase, ott) → userToken (OTP, 15h cache)
// 4. GET {apiBase}/mobile/hls/{id}.m3u8?q=720p&in={userToken}&hd=on&lang=eng
//    Headers: per-provider headers + Cookie: t_hash_t=...;hd=on;ott=nf
// 5. Parse master M3U8:
//    - Audio: keep as-is (s23 CDN)
//    - Video: rewrite freecdn → s23.nm-cdn9.top, remove in= param
//    - Subtitles: parse URI from EXT-X-MEDIA
// 6. Fallback: player.php
// 7. Fallback: playlist.php
```

## Endpoints
| Endpoint | USAGE | Status |
|---|---|---|
| `net52.cc/newtv/main.php` | Main page | ✅ |
| `net52.cc/newtv/search.php` | Búsqueda | ✅ |
| `net52.cc/newtv/post.php` | Detalles | ✅ |
| `net52.cc/newtv/episodes.php` | Episodios | ✅ |
| `net52.cc/mobile/hls/{id}.m3u8` | **Playback master** | ✅ |
| `s23.nm-cdn9.top/files/{id}/{quality}/` | **Full CDN** | ✅ |
| `net52.cc/verify.php` | Bypass (POST g-recaptcha) | ✅ |
| `net52.cc/newtv/otp.php?ott=nf` | **OTP user token** | ⏸️ Sin probar |
| `net52.cc/newtv/player.php` | Fallback | ✅ |
| `s21.freecdn4.top/files/...` | Preview (60 JPG) | ❌ Solo preview |

## Implementation (NetflixProvider.kt / PrimevideoProvider.kt)

### `loadLinks` — Primary Flow (s23 Cookie auth, no `in=` param)
```kotlin
// 1. Get bypass token (verify.php → t_hash_t cookie)
val cookieRaw = currentBypassToken  // h1::h2::ts::ep::99 (decoded)

// 2. Fetch mobile/hls master to get CDN structure
val inParam = cookieRaw  // used in mobile/hls request, but ignored by s23
val mobileResp = app.get("$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng",
    headers = mobileHeaders)  // Chrome/149 WebView

// 3. Build custom master:
//    - Audio lines (from mobile/hls response) → keep as-is (already on s23, no in=)
//    - Video variants: rewrite CDN freecdn → s23, STRIP in= param
//    - s23 accepts Cookie (t_hash_t + hd=on) without in= parameter
```

### Key Change (07 Jul 2026)
- **REMOVED** dependency on server-rewritten `in=` token (server no longer rewrites)
- **s23.nm-cdn9.top** accepts requests with ONLY Cookie auth (t_hash_t + hd=on)
- Audio already worked without `in=`; video now uses the same approach
- Player.php demoted to fallback (returns preview content, wrong episode)

### Fallbacks (in order)
1. ❌ ~~Scraping HTML~~ — reemplazado por mobile/hls
2. `playlist.php` — preview only, pero mantiene compatibilidad
3. `player.php` — preview only

### `getVideoInterceptor`
- Adds `Cookie: hd=on` to all `.m3u8` requests (preserves existing cookie from ExtractorLink)
- Replaces `in=unknown::ep` watermark with bypass token
- Fixes relative JPG segment URLs by appending `in=<token>` param
- Does NOT strip audio/subtitle groups (s23 has everything on same CDN)

## Current State (08 Jul 2026)
- ✅ `newTvBaseHeaders` actualizado: Chrome/149 WebView + `app.netmirror.netmirrornew`
- ✅ `loadLinks` primario: mobile/hls → s23 (cookie auth, no `in=` param)
- ✅ `bypass()` actualizado: Chrome 147 Windows + `g-recaptcha-response=${UUID}` + 15h cache
- ✅ `getNewTvUserToken()` NUEVO: OTP-based auth (NewTvOtpResponse.usertoken)
- ✅ `loadLinks` simplificado: apiBase → bypass → userToken → mobile/hls → parse M3U8
- ✅ `loadLinks` actualizado en NetflixProvider, PrimevideoProvider, JioHotstarProvider
- ✅ `NetflixMirrorStorage` extendido con saveFullCookie/getFullCookie/clearFullCookie
- ✅ Fallbacks: player.php → playlist.php
- ✅ Bypass: OkHttp no-redirect POST verify.php con Chrome 147 headers
- ✅ Interceptor: m3u8CdnFixInterceptor() (parches)
- ⏸️ `getNewTvUserToken` endpoint sin probar → probar en dispositivo
- ⏸️ `otp.php` endpoint depende del server — puede cambiar

## Files
- `NetflixProvider.kt` — `loadLinks()` mobile/hls → s23 primary + OTP auth
- `PrimevideoProvider.kt` — idem (ott="pv")
- `JioHotstarProvider.kt` — idem (ott="hs")
- `Utils.kt` — `bypass()`, `getNewTvUserToken()`, `resolveApiUrl()`, `newTvBaseHeaders`, `m3u8CdnFixInterceptor()`
- `PlutotvProvider/PlutotvProvider.kt` — PlutoTV provider (separado)

## Next Steps
1. ✅ Instalar APK compilado en dispositivo y probar reproducción real
2. ✅ Verificar que los JPG segments de s23 se reproducen en CloudStream
3. ✅ Eliminar dependencia de `in=` param — s23 acepta Cookie sola
4. ⏸️ **PROBAR el nuevo bypass() + getNewTvUserToken() en dispositivo real**
5. ⏸️ Si falla getNewTvUserToken, ajustar endpoint de otp.php
6. ⏸️ Si funciona, replicar en DisneyStudioProvider.kt
