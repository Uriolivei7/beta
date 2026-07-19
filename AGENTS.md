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

## Current State (11 Jul 2026)
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
- `PandramaProvider/` — Provider para pandrama.tv (Inertia.js SPA)

## Providers actualizados recientemente
| Provider | Fecha | Cambio |
|----------|-------|--------|
| `YoutubeProvider/Youtube.kt` | 12 Jul | Channel y playlist: lockupViewModel |
| `MundodonghuaProvider` | 12 Jul | Rewrite v4.0.4 + search URL fix |
| `DonghualifeProvider` | 12 Jul | Search fix + RumbleExtractor |
| `PandramaProvider` | 12 Jul | Rewrite completo: channel model, episode URLs, loadLinks via page data |

## PandramaProvider — Estructura
- **Arquitectura**: Laravel + Inertia.js (Vue SPA). Datos embedidos en `window.bootstrapData = {...}` dentro de `<script>`
- **Main page**: `/dramas`, `/peliculas` → parsea `loaders.channelPage.channels[].content.data[]`
- **Search**: `/search/{query}` → `loaders.searchPage.results[]`
- **Load (detalle)**: `/titulo/{id}/{slug}` → `loaders.titlePage.episodes.data[]` + `loaders.titlePage.title`
- **loadLinks (video)**: `/titulo/{id}/{slug}/temporada/{season}/episodio/{epNum}` → `loaders.episodePage.current_video.src`
- **Tipos de video**: `embed` (OK.ru, VK, YouTube, Dailymotion), `video/stream` (HLS/DASH directo), `shaka` (DRM)
- **Subtítulos**: `current_video.captions[]` con url, name, language
- **No usa API** (`/api/*` retorna 401) — todo se obtiene del JSON embedido en HTML

## GloboViewProvider — Estado (19 Jul 2026)
### ✅ Implementado
- `getMainPage`: 16 países (España, México, Argentina, Colombia, EEUU, Venezuela, Perú, Chile, Ecuador, Rep. Dominicana, Puerto Rico, Brasil, Alemania, Reino Unido, Francia, Italia) en vez de 8 categorías que timeouteaban. Las páginas de país cargan más rápido (~8-15s) y tienen todos los canales disponibles.
- `search`: escanea los mismos 16 países (5 antes) = ~384 canales vs 120 antes
- Todos los `app.get()` pasan `timeout = 60L`

### ⏸️ Pendiente
- Cada país puede tener paginación. Solo se ve página 1 (~24 canales). Para ver más canales por país, necesitaríamos detectar paginación.

## Next Steps (Netmirror)
1. ✅ Instalar APK compilado en dispositivo y probar reproducción real
2. ⏸️ **PROBAR cambios del 10 Jul v2** (customMasters + __cm=1 + M3U8 body logging)
3. ⏸️ Revisar los logs del M3U8 body — comparar contenido de EP1 vs EP2
4. ⏸️ Si el M3U8 es IDÉNTICO pero preview persiste: el problema es en los segmentos CDN, no en el M3U8
5. ⏸️ Si el M3U8 es DIFERENTE (EP2 tiene segmentos preview): el servidor limita EP2 cuando EP1 sigue activo
6. ⏸️ Próximo paso si es CDN: probar `Connection: close` en segment requests (ya implementado) o crear OkHttpClient propio para segmentos

## TudoramaProvider — Estado (17 Jul 2026)
### ✅ Implementado
- `getMainPage()` — 7 secciones (recientes, tendencias, géneros, películas)
- `search()` — búsqueda por query string
- `load()` — detalle con episodios DOM + AJAX (`corvus_get_episodes`)
- `loadLinks()` — download table → `/d/` URLs → `resolveServerUrl()` → `extractFromEmbed()`
- Posters en episodios (DOM y AJAX con `episode_image` del API)

### ✅ Arreglado (17 Jul)
- `/d/` → `/e/` path conversion en `extractFromEmbed` (VidStack espera `/e/` embed, no `/d/` download)
- Fallback AJAX `corvus_get_servers` + stream URL → iframe src (pero requiere login WP)
- Manual HTTP extraction con regex (m3u8/mp4 en HTML)
- **NEW: Direct API extraction** — `tryApiExtraction()` para hgcloud.to, bysesukior.com, 4meplayer.pro
- `/f/` → `/e/` **removido** (preserva `/f/` para hgcloud.to)
- `Uri.parse()` import para parsing de URLs

### ⏸️ Pendiente (BUG)
- `loadExtractor()` retorna 0 links para TODOS los servidores
- Causa raíz: Extractors registrados vía plugin (VidStack subclases) NO son auto-descubiertos por CS3
- `tryApiExtraction()` intenta llamadas API directas como fallback (requiere probar)
- `corvus_get_servers` requiere autenticación WordPress (no usable)

### Next Steps (Tudorama)
1. ✅ Build APK exitoso con API extraction directa
2. ⏸️ **Probar APK en dispositivo** — verificar si API extraction encuentra links:
   - hgcloud.to: `/api/source/{code}` POST
   - bysesukior.com: `/api/source` POST
   - 4meplayer.pro: `master.php` POST
3. ⏸️ Si API extraction falla: implementar extractor VidStack manual (WebView JS injection)
4. ⏸️ Si funciona: probar con múltiples episodios y servers
