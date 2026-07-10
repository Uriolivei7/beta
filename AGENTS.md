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
1. `playlist.php` — primary flow
2. `player.php` — fallback (JioHotstar primary)
3. Direct M3U8 with clean hash (via `getPlaylistUrl` in Utils.kt)

### `getVideoInterceptor` (10 Jul 2026) — Domain-aware
- **net52.cc/net22.cc/net11.cc** requests: Cookie `t_hash_t=...; hd=on; ott=nf/pv/hs` + User-Agent + Referer
- **CDN domains** (nm-cdn, freecdn, imgcdn): ONLY `Cookie: hd=on` (no `t_hash_t` — CDN uses `in=` URL param)
- All requests: `Cache-Control: no-cache`, `Pragma: no-cache`, `Connection: close`
- `hp=yes` stripped from M3U8 URL (suspected "homepage preview" flag)

## Current State (10 Jul 2026)
- ✅ `bypass()` + `clearCookie()` on episode change (fresh t_hash_t per episode)
- ✅ `_t=` cache-busting on M3U8 URL
- ✅ Anti-cache headers in interceptor (Cache-Control, Pragma, Connection: close)
- ✅ Domain-aware interceptor (t_hash_t only to main domain, hd=on only to CDN)
- ✅ `hp=yes` stripped from M3U8 URL
- ✅ MonoschinosProvider search fixed
- ✅ **NEW: customMasters + __cm=1** — M3U8 descargado en loadLinks (fuera del interceptor compartido), servido inline a ExoPlayer
- ✅ **NEW: M3U8 body logging** — log de primeras 1000 chars del body para comparar EP1 vs EP2
- ⏸️ **BUG: "next episode → 10-min preview" still occurs** — audio/subs correct, video preview only
- ⏸️ Trabajando en: identificar por qué el video se muestra como preview en EP2

## Hipótesis actual (10 Jul 2026)
- El problema NO es de cookie/bypass (ya comprobado con clearCookie + fresh bypass)
- El problema NO es de cache HTTP (ya comprobado con `_t=` + Cache-Control headers)
- El problema NO es de `t_hash_t` en CDN (ya domain-aware)
- El problema NO es `hp=yes`
- **NUEVA hipótesis**: CloudStream/ExoPlayer REUSA el mismo reproductor/HTTP client entre episodios. Cuando EP1 sigue activo (faltan 40s), las conexiones HTTP de EP1 se reusan para EP2, y el servidor/CDN sirve preview al ver el `in=` hash cambiado en la misma conexión.
- **customMasters + __cm=1**: descarga el M3U8 en loadLinks (conexión FRESCA, no compartida), lo sirve inline. Los segmentos aún van por el interceptor compartido, pero el M3U8 en sí es correcto.

## Files
- `NetflixProvider.kt` — `loadLinks()` playlist.php → mobile/hls primary + __cm=1
- `PrimevideoProvider.kt` — idem (ott="pv")
- `JioHotstarProvider.kt` — player.php primary, playlist.php fallback
- `Utils.kt` — `bypass()`, `getNewTvUserToken()`, `resolveApiUrl()`, `newTvBaseHeaders`, `m3u8CdnFixInterceptor()`, `NetflixMirrorStorage`
- `PlutotvProvider/PlutotvProvider.kt` — PlutoTV provider (separado)

## Next Steps
1. ✅ Instalar APK compilado en dispositivo y probar reproducción real
2. ⏸️ **PROBAR cambios del 10 Jul v2** (customMasters + __cm=1 + M3U8 body logging)
3. ⏸️ Revisar los logs del M3U8 body — comparar contenido de EP1 vs EP2
4. ⏸️ Si el M3U8 es IDÉNTICO pero preview persiste: el problema es en los segmentos CDN, no en el M3U8
5. ⏸️ Si el M3U8 es DIFERENTE (EP2 tiene segmentos preview): el servidor limita EP2 cuando EP1 sigue activo
6. ⏸️ Próximo paso si es CDN: probar `Connection: close` en segment requests (ya implementado) o crear OkHttpClient propio para segmentos
