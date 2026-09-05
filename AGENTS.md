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

### `getVideoInterceptor` (26 Jul 2026) — __cm=1 + Domain-aware + freecdn in= injection
- **__cm=1 priority**: intercepta requests a `net52.cc/mobile/hls/{id}.m3u8?__cm=1`, sirve custom master desde `customMasters[id]` (alamacenado en loadLinks)
- **net52.cc/net22.cc/net11.cc** requests: Cookie `t_hash_t=...; hd=on; ott=nf/pv/hs` + Connection: close
- **CDN domains** (nm-cdn, freecdn, imgcdn): Cookie `t_hash_t=...; hd=on; ott=nf/pv/hs` + Connection: close
- **freecdn in= injection**: Guarda `in=` de la variant M3U8 request, lo inyecta en segment requests (.ts) via `addEncodedQueryParameter`
- All requests: `Cache-Control: no-cache`, `Connection: close`
- `hp=yes` stripped from M3U8 URL

## Current State (26 Jul 2026)
- ✅ **__cm=1 restored** — loadLinks descarga M3U8 con conexión FRESCA, almacena en customMasters, sirve inline a ExoPlayer. Previene que el CDN devuelva preview basado en sesión compartida.
- ✅ `clearCookie()` en cambio de episodio — t_hash_t fresco por episodio
- ✅ Interceptor mejorado: t_hash_t en CDN + freecdn in= injection + Connection: close
- ✅ `hp=yes` stripped, `_t=` cache-busting, anti-cache headers
- ✅ M3U8 body logging (len, video lines, props como hasIFrames/hasThumbnails)
- ✅ **PrimeVideo: episode transition WORKS** (nm-cdn, nunca tuvo el bug)
- ⏸️ **BUG: Netflix "next episode → 10-min preview"** — pendiente de probar con __cm=1 restaurado

## Hipótesis (26 Jul 2026)
- El código **antiguo** (con `__cm=1` + `setCustomMaster`) **NO tenía** preview en video. Solo thumbnails incorrectos (ExoPlayer genera thumbnails de I-frames, no hay thumbnail track separado).
- El código **actual** (URL directa sin __cm=1) **introdujo** el preview bug — ExoPlayer comparte conexión HTTP con el CDN entre episodios, CDN sirve preview al detectar sesión existente.
- **Fix**: Restaurar __cm=1 (fetch M3U8 en loadLinks con conexión fresca) + mantener interceptor mejorado (t_hash_t a CDN + in= injection como safety net).
- Si aún falla: el servidor CDN (`s24.freecdn3.top`) podría requerir `in=` en segment requests de forma obligatoria (no opcional).

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
| `PrimevideoProvider` | 05 Ago | Posters lentos → proxy `wsrv.nl?w=500` |

## 🔧 Fix posters lentos PrimeVideo (05 Ago 2026)
- **Síntoma**: en el apartado PrimeVideo los posters tardan mucho en cargar y algunos parecen no tener poster; Netflix carga bien.
- **Causa raíz (medido con curl/python)**:
  - Netflix `imgcdn.kim/poster/v/{id}.jpg` → **22-62 KB**, carga en 0.6-0.8s.
  - PrimeVideo `imgcdn.kim/pv/v/{id}.jpg` → **700KB-3.8MB** (imágenes full-size), carga en 1.7-5.4s → CloudStream se queda sin tiempo o tarda muchísimo.
  - No existe ruta de thumbnail propia en imgcdn para pv (`/pv/t/`, `/pv/300/`, etc. → 404).
- **Fix** en `PrimevideoProvider.kt:28`: `pvPoster(id)` ahora devuelve `buildVerticalPosterUrlWithProxy(id, "pv")` → `https://wsrv.nl/?url=...&w=500` → **37-80 KB**, carga en 0.6-1.4s. Aplica a main page, search, detalle y recomendaciones (todos usan `pvPoster`). `pvBg`/`pvEpPoster` sin cambios.
- Compilación OK: `.\gradlew.bat :NetflixmirrorProvider:compileReleaseKotlin --console=plain -q`
- ⏸️ Pendiente: probar en dispositivo que los posters pv cargan rápido.

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

---

## TokianimeProvider — Estado (24 Jul 2026)

### ✅ Funcionando
- `getMainPage()`: home/últimos, tendencia, géneros (Acción, Comedia, Fantasía, Drama, Romance, Sci-Fi) — carga HTML, parsea links a[href^=/anime/] + posters de img[src].
- `load()`: detalle del anime via DOM (og:title, description, year, tags, poster), episodios via API `api/anime/{slug}/episodes`. Soporta multi-season: parsea "Ver orden sugerido" accordion, llama API por cada entry, ordena Temporadas → OVA → Especial → Película.
- `loadLinks()`: extrae SID del RSC payload (`self.__next_f.push`), llama `api/player/source?a=MAL_ID&ep=N&sid=SID&mode=play`, retorna M3U8 con q=720p/1080p/480p. Regex normaliza `\"` → `"` y `\u0026` → `&`.
- `search()`: usa API `GET /api/catalog?adult=0&q={query}&page=0&pageSize=20` — extrae slugs/titles/posters via regex, limite 50 resultados, detiene en page vacía.

### 🔧 Última fix (24 Jul)
- **Search reescrito**: reemplazó fallback por género (lento) con API `GET /api/catalog?q={query}` directa.
- **Bug corregido**: regex `\[(.*?)\]` se detenía en primer `]` (tags array). Ahora extrae slugs/titles/posters con regex independientes (no confía en aislar items array).
- **Loop infinito corregido**: el paginado se detiene cuando `"items":[]` o `results.size >= total`.
- **Episodios con posters y descripción**: ahora extrae `thumbs.{ep} → posterUrl` y `meta.{ep}.overview → description` del API `api/anime/{slug}/episodes`.
- **Bug search corregido**: `sources[].slug` se contaba como slug adicional, desalineando el pairing por índice. Ahora usa regex `"slug":"xxx"[^}]*?"title":"yyy"` que solo empareja slug+title del mismo item, ignorando sources.
- **Bug #ultimos corregido**: algunos links tenían texto "Ver ahora" (botón) en vez del nombre del anime. Ahora usa `img.alt` primero (título real), filtra "Ver ahora"/"Watch now".

### ✅ Confirmado
- Search API ya busca en múltiples campos: `title`, `titleEnglish`, `titleNative`, `synonyms`. Buscar "One Punch Man" retorna resultado con `matchedBy:"title"`.
- Episodios API devuelve `thumbs.{n}` (poster 640.webp) y `meta.{n}.{title,overview}` por cada episodio.

### 🔬 Próximo
- Probar en dispositivo: search API, load multi-season, episodios con posters/descripción, reproducción de video.

---

## TMDBProvider — Estado (24 Jul 2026)

### ✅ Estructura creada
Plugin completo que usa:
- **TMDB API v3** para catálogo/búsqueda/detalles (necesita API key gratuita)
- **TMDB Embed API** autohosteada para streams (Docker: `inside4ndroid/tmdb-embed-api`)

### Soporte
- Películas y Series
- Episodios con posters (`still_path`) y descripción (`overview`)
- Recomendaciones desde TMDB
- Multi-temporada (carga episodio por episodio desde TMDB)
- Score desde TMDB (escala 0-10 convertida a Score CS3)

### ⏸️ Pendiente
- Usuario debe registrar API key en https://www.themoviedb.org/settings/api
- Usuario debe deployar TMDB Embed API vía Docker
- Probar en dispositivo

### Archivos
- `TMDBProvider/build.gradle.kts`
- `TMDBProvider/src/main/kotlin/com/example/TMDBPlugin.kt`
- `TMDBProvider/src/main/kotlin/com/example/TMDBProvider.kt`

---

## UniqueStreamProvider (AnimeStream) — Estado (02 Ago 2026)

Sitio: `anime.uniquestream.net` (Nuxt). Provider en `UniquestreamProvider/src/main/kotlin/com/example/UniquestreamProvider.kt`.

### ✅ Causa raíz del error 3001 RESUELTA (02 Ago 2026)
- **Síntoma**: `ERROR_CODE_PARSING_CONTAINER_MALFORMED (3001)` en TODOS los links de algunos episodios (ej. serie `HWH21Ge0`, episodios `hDGx3QZd` fallan, `vEPPCFXi`/`HAx1NgR2` funcionan).
- **Causa**: el interceptor devolvía `hex.decode(media_id)` como key AES. Eso funciona SOLO cuando la key real coincide con el media_id (casualidad para ciertos episodios). El key.bin del servidor es un **señuelo cifrado** cuya key real se deriva distinta por episodio.
- **Derivación REAL de la key (encontrada en el JS del player, función `Za`)**:
  1. Fetch de `key.bin` **con header obligatorio `x-am-media-id: {media_id}`** (sin el header el servidor devuelve señuelo que no descifra).
  2. El body es **base64** del dato cifrado.
  3. Descifrar AES-CBC: `key = SHA256("key"+media_id)[:16]`, `iv = SHA256("iv"+media_id)[:16]`.
  4. El resultado (16 bytes) es la key real de los segmentos.
- **IV de segmentos**: media sequence number (`seg-N.png` → IV = N en 16 bytes big-endian), igual que el EXT-X-KEY (sin atributo IV).
- **Verificado con Python (pycryptodome)**:
  - `hDGx3QZd`: derived=`f127680318f3aeaf6c2f050108fb7372` → 5/5 seg TS-OK (con media_id fallaba).
  - `vEPPCFXi`: derived=`a18520f3ab9af4169f0d36f4f085eccc` == hex(media_id) → 5/5 TS-OK.
- **Fix implementado** en `getVideoInterceptor`: hace el fetch real del key.bin con header `x-am-media-id`, descifra con SHA-256/AES-CBC, y devuelve la key derivada. Fallback a `hex.decode(media_id)` si el fetch/decrypt falla.
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`

### 🔧 Fix error 2000 tras cambio de CDN (05 Ago 2026) — get3.mediacache.cc
- **Síntoma**: tras migrar el sitio de `api.uniquestream.net` a **`get3.mediacache.cc`**, los episodios daban `ERROR_CODE_IO_UNSPECIFIED (2000)` / "Enlaces no encontrados Error de fuente".
- **Nuevo flujo del API de video** (`/episode/{id}/media/dash/{locale}` → 200):
  - `hls.playlist` = `https://get3.mediacache.cc/episode/{serie}/{season}/{ep}/.../{media_id}_{locale}/master.m3u8?sign=...&expires=...` — **media_id CORTO** (ej. `534740`, 6 dígitos), NO 32-hex.
  - Master (1 sola variante `v_1920x1080/playlist.m3u8?...` con su propio sign) → variante AES-128 con `URI="../keys/key.bin?expires=...&sign=..."` y segmentos `seg-N.png` **sin sign**.
- **Verificado en PC (curl/python, sign firmado para mi IP)**:
  - Master, variante y segmentos → **200 con solo User-Agent** (incluso `ExoPlayerLib/2.19.1`); sin headers → 403.
  - `key.bin` **con** `x-am-media-id: 534740` → body base64 real → AES-CBC `key=SHA256("key534740")[:16]`, `iv=SHA256("iv534740")[:16]` → `740c6909a4c4d4792fc9ed7bfd183baa` (padding PKCS7 válido) → seg-0 descifra a TS válido (0x47 en 20133/20133 paquetes, IV=media sequence).
  - `key.bin` **sin** `x-am-media-id` → 200 pero **señuelo distinto** (descifra a garbage `05dc9366...`) → ExoPlayer usaría key incorrecta.
- **Causa raíz**: `keyRegex = /([0-9a-f]{32})_.../` exigía media_id de 32 hex → no matcheaba los IDs cortos (`534740`) → `mediaId = null` → el interceptor era no-op → ExoPlayer pedía key.bin sin `x-am-media-id` → key señuelo → fallo.
- **Fix** en `getVideoInterceptor` (`UniquestreamProvider.kt:91`):
  - `keyRegex` cambiado a `/([A-Za-z0-9]+)_[^/]+/master\.m3u8` (captura `534740`).
  - Fallback adicional: extraer media_id de la propia URL de `key.bin` (`/([A-Za-z0-9]+)_[^/]+/keys/key\.bin`), primero del link y luego del request actual.
  - Mantiene fallback legacy 32-hex y `hex.decode(media_id)`.
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- ⏸️ **Pendiente**: probar en dispositivo `dv_1011` (The Hated Classmate) — ver log `Interceptando key.bin -> 740c6909... (derived=true)`.

### ✅ Funcional (antes del fix)
- `getMainPage`: 9 secciones (Nuevos, Populares, Películas, Acción, Aventura, Comedia, Drama, Fantasía, Sci-Fi).
- Posters `480x720`; películas `TvType.AnimeMovie`; descripción con `Audio:`/`Subtítulos:`.
- Temporadas ordenadas por `season_seq_number`, indexadas 1..N; `distinctBy { content_id }`.
- Subtítulos VTT: `subtitleCallback(SubtitleFile(lang, url))` con `language` (NO `locale`).

### 🔧 Fix desorden de temporadas al re-entrar (03 Ago 2026)
- **Síntoma**: 1ª carga orden correcto (1,2,3,4,5); 2ª/3ª carga la UI muestra temporadas desordenadas (1,2,5,3,4 / 1,4,5,7,9,10,2,3,6,8).
- **Diagnóstico**: logs en `load()` probaron que el provider SIEMPRE entregaba orden correcto (API estable, sort por `season_seq_number`, `episodesList` ordenado). El desorden lo producía CloudStream al **restaurar** la agrupación en la rama Anime de `postEpisodes()` (NO reordena: confía en el orden de inserción, que al restaurar del caché/ViewModel se pierde).
- **Causa raíz estructural**: la rama `AnimeLoadResponse` de CloudStream no ordena y su agrupación se desordena al restaurar; la rama `TvSeriesLoadResponse` **sí** reordena por `season*10000 + episode` (determinista y estable ante restauración). Providers sin `this.season` (Hianime, AnimeOnsen) no sufren el bug por tener una sola temporada.
- **Fix**: cambiar `newAnimeLoadResponse(TvType.Anime)` → `newTvSeriesLoadResponse(TvType.TvSeries, episodesList)` pasando la lista directamente (mismo patrón que `PrimevideoProvider.kt:162`). El reproductor/lector de episodios no cambia.
- Logs de diagnóstico en `load()` agregados y luego **removidos** tras confirmar la causa.
- ✅ **Confirmado en dispositivo (03 Ago 2026)**: el bug era de la rama `Anime` de CloudStream (no soporta bien muchas temporadas). Con `TvSeries` las temporadas se mantienen ordenadas 1..N en re-entries y las series con muchas temporadas cargan **más rápido**.

### 🔧 Fix especiales al inicio de la temporada (04 Ago 2026)
- **Síntoma (Attack on Titan `AG689vQw`, temporada 4 `heIY0Kaq`)**: los Especiales (THE FINAL CHAPTERS Special 1/2, `episode_number=1.0/2.0`) aparecían como "episodio 1 y 2" al INICIO de la temporada 4 en la UI.
- **Diagnóstico por logs**: el provider SÍ los entregaba al final (`orden FINAL`: 60-87, luego Special 1, Special 2). PERO la rama `TvSeries` de CloudStream **reordena** por `season*10000 + episode`, y como los especiales tienen `episode_number=1/2`, quedaban siempre primeros numéricamente.
- **Causa raíz**: imposible separar la clave de orden de CloudStream del número de episodio mostrado; si los especiales tienen número bajo, CloudStream los fuerza al inicio.
- **Fix** en `loadSeasonEpisodes` (`UniquestreamProvider.kt:~353`): separar `regulars`/`specials` (special = título contiene "Special" O `episode` empieza con "SP"), ordenar normales ascendente, y **renumerar los especiales con `maxRegular + 1, +2, ...`** (en AoT pasan a ser 88, 89). Así CloudStream los ordena al final de la temporada y ya no son "1 y 2". No afecta playback (el ID usado en loadLinks es `content_id`, no el número).
- Logs de diagnóstico removidos tras implementar el fix.
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`

### ⏸️ Pendiente
- Compilar APK completo y probar en dispositivo (los episodios que daban 3001).
- **Probar el fix de temporadas**: entrar 2ª vez en `5spNP0fT` (Re:ZERO) y `oC7sJj1J` — verificar que el orden 1..N se mantiene. ✅ **VERIFICADO**
- Scripts de verificación en `%TEMP%\opencode\`: `check_final.py` (derivación key real), `check_xam.py` (header x-am-media-id), `check_derive3.py`, `check_hdg.py`, `check_seasons.py` (orden API).

### Archivos
- `UniquestreamProvider/src/main/kotlin/com/example/UniquestreamProvider.kt` (~570 líneas)

### 🔧 Fix timeout 120s en series grandes (One Piece) (09 Ago 2026)
- **Síntoma**: One Piece (1216 eps, 68 páginas de API) daba `Timed out waiting for 120000 ms` en TODAS las peticiones de episodios; el log mostraba `getWithRetry error ... (intento 1): Timed out waiting for 120000 ms` y `StandaloneCoroutine was cancelled` en main page.
- **Diagnóstico (decompilado)**: `APIRepository$load$2` envuelve el `load()` del provider en `withTimeout(getLoadTimeoutMs() ?: 120000L)`. El `getTimeout(null)` de APIRepository devuelve 120000ms con `coerceIn(5000, 480000)`. One Piece superaba los 120s → CloudStream cancelaba TODO el `load()` y mataba las peticiones en vuelo (ese mensaje es `TimeoutCancellationException` de kotlinx, NO el timeout de OkHttp).
- **Fix**:
  1. `override val loadTimeoutMs: Long? = 480_000L` (máximo permitido por coerceIn; el getter `getLoadTimeoutMs()` es ACC_PUBLIC sin ACC_FINAL → open val overrideable).
  2. **Rethrow `CancellationException`** en `getWithRetry`, secciones de `getMainPage`, y loop de `load()` — ya no se loguea como error ni se reintenta tras cancelación.
  3. Semáforo API 6 → **12** (en PC 20 concurrentes funcionaron sin Cloudflare).
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` → `UniquestreamProvider/build/UniquestreamProvider.cs3` (75 KB)
- ⏸️ **Pendiente**: instalar cs3 en dispositivo y abrir One Piece — la carga de episodios ya no debe morir a los 120s.
- ✅ **VERIFICADO en dispositivo (09 Ago 2026)**: One Piece carga **1216 episodios completos** en ~5.5 min (12:59:19 → 13:05:12). Ya no hay `Timed out waiting for 120000 ms` ni cancelación del `load()`. Quedan timeouts OkHttp puntuales por petición (vISRWU6Y p1, QLBXwRlT p1) que `getWithRetry` reintenta y completa. Sin Cloudflare. El tiempo es normal: 68 páginas API × 5-30s / 12 concurrentes. `episodeCache` en memoria (solo por sesión).

### 🔧 Caché en disco de episodios y serie (09 Ago 2026)
- **Motivo**: One Piece tarda ~5.5 min en cargar los 1216 episodios (68 páginas API). Con solo caché en memoria, re-abrir la serie dentro de la misma sesión es rápido pero tras reiniciar la app se re-descarga todo.
- **Implementado** en `UniquestreamProvider.kt`:
  - Directorio `context.filesDir/uniquestream_cache/` con archivos `season_{id}.json` (lista procesada de `EpisodeItem`) y `series_{id}.json` (raw JSON de `/series/{id}`).
  - `readSeasonCache`/`writeSeasonCache` y `readSeriesCache`/`writeSeriesCache` — todo en `Dispatchers.IO`, TTL **24h** (`CACHE_TTL_MS`).
  - `loadSeasonEpisodes()`: primero caché en memoria → disco → API; al completar escribe en disco.
  - `load()`: serie leída de memoria → disco → API; al completar escribe en disco.
  - `AppUtils.toJson`/`parseJson` (Jackson, mismas data classes que usa el provider). Nota: `toJson` es extensión `Any.toJson()` — requiere `import com.lagradost.cloudstream3.utils.AppUtils.toJson`.
- **No afecta**: reproducción (`loadLinks`), interceptor de video ni key derivation — solo el catálogo de episodios.
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` → `UniquestreamProvider/build/UniquestreamProvider.cs3` (79 KB)
- ⏸️ **Pendiente**: instalar cs3 y verificar que re-abrir One Piece tras reiniciar la app carga los 1216 eps desde disco (rápido, sin 68 peticiones).

---

## AnimeAV1 — Fix error HLS 2004 (05 Ago 2026) — player.zilla-networks.com

### ✅ Diagnóstico (verificado con Python desde PC)
- El embed HLS de animeav1.com entrega `https://player.zilla-networks.com/play/{32-hex}` — es una **página HTML** (Vite+JWPlayer, 998 B), NO un m3u8. El JS del player (`/assets/index-b0y5A--O.js`) construye `file: "https://player.zilla-networks.com/m3u8/" + hash` y lo pasa a JWPlayer.
- **Mapeo de rutas**:
  - `/play/{hash}` → 403 sin Referer; 200 con Referer pero HTML (SPA) → alimentar a ExoPlayer daba `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)` o parse error.
  - `/m3u8/{hash}` → **200 sin headers**, m3u8 VOD fMP4: `#EXT-X-MAP:URI=".../segs/{hash}/init.html"`, segmentos `000.html`, `001.html`, `#EXT-X-PLAYLIST-TYPE:VOD`.
  - `/segs/{hash}/{n}.html` → **403 challenge de Cloudflare** ("Attention Required!") con UA solitario o incluso con Referer.
- **Clave**: los segmentos pasan a **200** con set completo de headers de navegador (Accept `*/*`, Accept-Language, Origin+Referer `player.zilla-networks.com`, Sec-Fetch-Dest/Mode/Site, Priority). Funciona incluso con UA `ExoPlayerLib/2.19.1` y sin sec-ch-ua. No se guardan cookies en el flujo play→m3u8 (challenge es de headers, no de sesión).

### Fix implementado en `Animeav1Provider.kt`
1. **`loadCustomExtractor`** (`Animeav1Provider.kt:376`): si la URL es `player.zilla-networks.com/play/{hash}`, transforma a `/m3u8/{hash}` y emite `newExtractorLink` con `type=M3U8`, `quality=P1080`, `referer=https://player.zilla-networks.com/` y `headers=zillaHeaders`. Evita que ExoPlayer reciba la página HTML.
2. **`getVideoInterceptor`** (`Animeav1Provider.kt:74`): interceptor OkHttp que inyecta `zillaHeaders` a TODA petición a `player.zilla-networks.com` (master + segmentos + init.html). Es el mecanismo probado en el repo (mismo patrón que UniqueStream).
3. `zillaHeaders` (`Animeav1Provider.kt:63`): Accept, Accept-Language, Origin, Referer, Sec-Fetch-*, Priority.

### Estado
- Compilación OK: `.\gradlew.bat :Animeav1Provider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:Animeav1Provider:make` → `Animeav1Provider/build/Animeav1Provider.cs3` (27 KB)
- ⏸️ **Pendiente**: instalar cs3 en dispositivo y probar Fullmetal Alchemist: Brotherhood EP1 (link "Subtitulado: HLS") — debería cargar master + segmentos sin 403. Si Cloudflare aun bloquea desde IP móvil, revisar si requiere sec-ch-ua/UA de navegador real en vez de ExoPlayerLib.

### Scripts de verificación (`%TEMP%\opencode\`)
- `check_av1.py`, `check_zilla.py`, `check_zilla2.py`, `check_js.py`, `check_js2.py`, `check_m3u8.py` (mapeo /play→/m3u8→/segs)
- `check_segs.py`, `check_segs2.py`, `check_403.py` (403 Cloudflare con headers parciales)
- `check_fullhdr.py` (**headers completos → 200**), `check_cookieflow.py` (no hay cookies), `check_exo.py` (UA ExoPlayer + full headers → 200)

## PlushdProvider (PlusHD) — Fix películas congeladas (07 Ago 2026)

### ✅ Resuelto: películas ya no se congelan
- **Síntoma**: las series reproducían fluido, pero las **películas** (vidhideplus.com) se congelaban cada ~5s.
- **Causa raíz (2 bugs)**:
  1. **Desempaquetador roto** en `tryVidHideExtraction` (`PlushdProvider.kt:~521`): buscaba el primer `eval(` de la página (que es el de **publicidad**, con `}\('` escapado) y luego `html.indexOf("}('")`. Como el eval real del player tiene `}('` pero aparece DESPUÉS del de ads, `callStart` daba -1 → desempaquetado fallaba siempre → el link lo emitía `loadExtractor` (extractor core de CloudStream).
  2. **Extractor core devuelve hls3** (`master.txt` → segmentos `.woff2` en `breezewoodcreativeworks.cfd`), y el `getVideoInterceptor` solo inyectaba headers a `.m3u8`/`.ts` → los `.woff2` iban sin Referer → **403 Cloudflare → freeze cada ~5s**.
- **Fix implementado**:
  - `tryVidHideExtraction` reescrito con regex robusta que encuentra el eval correcto: `}[(]'(.*?)',(\d+),(\d+),'(.*?)'[.]split` (DOT_MATCHES_ALL), itera todos los matches y desempaqueta hasta hallar un `.m3u8`. El m3u8 extraído es **hls2** (`dramiyos-cdn.com`, segmentos `.ts`) que funciona **sin headers** (verificado 200 en master/variante/segmentos).
  - `getVideoInterceptor` gate ampliado: ahora también añade UA/Referer/Origin a URLs `.woff2` y `.txt` (cubre hls3 por si se usa).
- **Verificado en PC (Python)**:
  - hls2 (`dramiyos-cdn.com`) → master/variante/850 segmentos `.ts` → **200 sin headers**, Content-Type `video/MP2T`, syncTS OK.
  - hls3 (`breezewoodcreativeworks.cfd`) → segmentos `.woff2` → **403 sin Referer**, 404 con UA ExoPlayer, **200 solo con UA + Referer vidhideplus**.
  - El player JS real usa `links.hls4||links.hls3||links.hls2` → cae en hls3, pero nuestro extractor prefiere hls2 (más simple, no necesita headers).

### 🔧 Fix regex eval (07 Ago v2) — `\(`/`\.` rechazados por ICU de Android
- **Síntoma**: las **series** reproducían fluido pero las **películas** seguían congelándose pese al fix anterior.
- **Diagnóstico (logcat `PlushdProvider-VidHide`)**: `Error: Syntax error in regexp pattern near index 1` apuntando a `\(` en el `evalRegex`. El motor regex de Android (libcore/ICU) **rechaza** `\(`/`\.` en el patrón `}\('(.*?)'...` aunque JVM (Java estándar) lo compile OK — por eso el test en PC no lo detectó.
- **Flujo real por tipo**:
  - **Series**: el m3u8 directo aparece en el HTML → `m3u8Regex` (línea 521) lo encuentra ANTES de llegar al eval → `tryVidHideExtraction` emite hls2 → fluido. Nunca se tocaba el evalRegex.
  - **Películas**: NO hay m3u8 directo → se llega al `evalRegex` (línea 526) → excepción de compilación → `tryVidHideExtraction` retorna `false` → el link lo emitía `loadExtractor` (extractor core) → **hls3 `.woff2`** → freeze.
- **Fix**: `}\('(.*?)'` → `}[(]'(.*?)'` y `\.split` → `[.]split` (clases de caracteres, sin backslash-escapes ambiguos). Semántica idéntica.
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (35.3 KB)

### 🔧 Fix regex eval (07 Ago v3) — el `}` inicial es inválido en ICU
- **Síntoma**: tras el fix v2 (con `[(]`), las películas seguían congeladas. El logcat seguía mostrando `Error: Syntax error in regexp pattern near index 1` apuntando al `[` (posición 1), es decir al carácter INMEDIATO después del `}` inicial.
- **Causa raíz real**: el motor regex de Android (libcore/ICU) trata el `}` suelto como cierre de cuantificador `{n,m}`; un `}` sin `{` previo es **error de sintaxis siempre**, sin importar qué escape/clase venga después. El problema no era `\(` ni `[(]`, era el **ancla `}` del inicio** del patrón. Por eso el error siempre apunta a index 1 (el char tras el `}`).
- **Fix**: quitar el ancla `}` del patrón: `"""[(]'(.*?)',(\d+),(\d+),'(.*?)'[.]split"""`. Sin el `}` no hay error de compilación y el regex igual encuentra el eval correcto (verificado en PC: 1 match, a=36 c=602 → m3u8 hls2 `dramiyos-cdn.com`). El bucle ya itera todos los matches y solo acepta el que desempaqueta a un `.m3u8` (los evals de publicidad no producen m3u8 y se descartan).
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (35.3 KB)

### 🔧 Fix referer segmentos (07 Ago v4) — el CDN puede rechazar referer tioplus
- **Síntoma**: el plugin ya extrae el m3u8 hls2 correcto (`acek-cdn.com`) tras el fix v3, pero la película seguía congelándose.
- **Diagnóstico (PC)**: el CDN `{sub}.acek-cdn.com` responde **200** a master/variante/segmentos `.ts` (Content-Type `video/MP2T`, sync byte `0x47` ok) pero con **latencia alta e inestable** (3-12s por request, rate-limiting por IP en tandas). Además, con `Referer=https://tioplus.app` los segmentos daban ERR, mientras `Referer=https://vidhideplus.com` → 200.
- **Fix**: en `tryVidHideExtraction` el `ExtractorLink` ahora usa `referer = vidReferer` (el URL vidhideplus) en lugar de `mainUrl`. El `getVideoInterceptor` ahora usa `Origin = extractorLink.referer` (en vez de `mainUrl`), igual que un navegador.
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (35.4 KB)
- ⏸️ **Sospecha residual**: el CDN parece lento/inestable por sí mismo (timeouts desde PC incluso sin headers). Si persiste el freeze, es problema del servidor/CDN, no del provider.

### 🔧 Fix referer condicional (07 Ago v5) — revertir cambio global que ralentizó series
- **Síntoma**: tras v4 (referer/Origin = extractorLink.referer global), las **series también** empezaron a tardar en reproducir.
- **Causa**: el v4 cambió `Origin` del interceptor a `extractorLink.referer` para TODOS los links. Las series (dramiyos-cdn) funcionaban con `Origin=mainUrl` (tioplus) y el cambio global les afectó.
- **Fix**: 
  - Interceptor vuelve a `Origin = mainUrl` (global, como v3).
  - El `ExtractorLink` solo usa `referer = vidReferer` si el host del m3u8 es `acek-cdn` (películas vidhide); si no (dramiyos-cdn, series), usa `mainUrl`.
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (35.5 KB)
- ⏸️ **Pendiente**: probar en dispositivo que las series vuelven a reproducir fluido y ver si la película mejora algo.

### 🔍 Veredicto final (07 Ago v6) — el congelamiento es del CDN, no del provider
- **Síntoma**: con v5 instalado, el usuario reporta congelamiento **tanto en películas como en series**.
- **Logs del dispositivo (v5)** confirman que la **extracción funciona perfectamente**: eval desempaquetado OK (a=36 c=602), m3u8 hls2 `{sub}.acek-cdn.com`, `linksFound=1`. El interceptor entrega master/variante con headers correctos.
- **Medido en PC (mismo master/variante/segmentos que ve el dispositivo)**:
  | Request | Status | Tiempo |
  |---------|--------|--------|
  | Master m3u8 | 200 | 3.6s |
  | Variante | 200 | 4.7s |
  | Segmento `.ts` | 200 (sync `0x47` OK) | 6-11s+ |
  | 2ª tanda de segmentos | ERR/timeout | 20-60s |
- **Causa raíz**: cada segmento contiene ~10s de video pero tarda **más** en descargar que su duración → ExoPlayer agota el buffer → freeze cada ~5s. Además el CDN hace **rate-limiting por IP** (2ª tandas fallaban incluso sin headers).
- **Conclusión**: el plugin ya hace todo lo posible (extrae el m3u8 correcto, headers correctos, segmentos devuelven 200 con TS válido). El cuello de botella es el CDN `{sub}.acek-cdn.com` del servidor (lento/saturado) — **fuera de nuestro alcance**.
- **Sugerencias al usuario**: probar otra película/serie (el rate-limit es por IP y momento), probar en otro horario, o comparar si el sitio web en navegador reproduce fluido en la misma red.
- Estado final del plugin: **v5 (35.5 KB)** es la versión estable correcta — no hacer más cambios de código para este síntoma.

### Scripts de verificación (`%TEMP%\opencode\`)
- `plus_unpack4.py` (desempaquetado completo del eval vidhide), `plus_regex_test.py` (validación del regex Kotlin → encuentra eval correcto a=36 c=602), `plus_player_flow.py` (mapeo película→player page→vidhideplus), `plus_cdn_check*.py` (master→variante→segmentos), `plus_seg_hdr.py` (headers segmentos hls2 vs hls3), `plus_hls3*.py` (master.txt hls3)

---

## PlushdProvider — Variantes múltiples CDN (04 Sep 2026 v4)

### ✅ Implementado
- **Motivo**: al igual que SoloLatino, PlusHD ahora emite las variantes `hls2/hls3/hls4` del script `var links={...}` de vidhide como links separados (`VidHidePro - hls2/hls3/hls4`), para poder elegir un CDN alternativo si uno se congela (acek-cdn es lento/saturado).
- **Probe con validación real (NO solo HTTP 200)**: cada variante se probó leyendo el body — solo se emiten las que responden **200 y cuyo body empieza con `#EXTM3U`** (playlist HLS real). Esto descarta CDNs que devuelven 200 con contenido no-HLS (causa del `ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED (3003)` en hls3 `master.txt` / `ecommercesolution.sbs`).
- **Fallback**: si ninguna variante pasa el probe, se emiten todas (como antes).
- **Refactor `processServer`**: el bucle de servidores player-path (tioplus `/player/`) se extrajo a una función local reutilizable. La 2ª pasada de reintento (cuando `foundLinks==0`) ya NO solo repite servidores DIRECTOS — ahora llama `processServer` para **TODOS** los servidores con referer `$mainUrl/`, arreglando el bug de "primer play → Enlaces no encontrados, segundo play → carga" (la 1ª pasada a veces devuelve videoUrl en blanco en tioplus).
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (44.6 KB)
- `plugins.json`: PlusHD v3 → sin cambio de version (sigue 3), fileSize 41424 → 44619

### ⏸️ Pendiente
- Probar en dispositivo: (1) que hls3 ya no se emite si falla el probe `#EXTM3U`; (2) que el primer play ya no da "Enlaces no encontrados" (reintento 2ª pasada con todos los servidores).

---

## UniqueStreamProvider — Fix películas standalone (12 Ago 2026)

### 🐛 Bug corregido
- **Síntoma**: las películas standalone (no dentro de una serie) no cargaban información — salía error.
- **Causa raíz (verificado con curl)**:
  - `load()` solo llamaba `GET /api/v1/series/{id}` → **404 para TODAS las películas** (ej. `dv_575811` The Garden of Words, `stWMCKHO` Shinobi Girl: The Movie).
  - `loadLinks()` solo llamaba `GET /api/v1/episode/{id}/media/dash/{locale}` → 404 para películas.
  - El search API devuelve `movies[]` (con `type:"movie"`) pero el plugin solo parseaba `series[]`.

### Endpoints correctos (verificados)
| Endpoint | Uso | Status |
|---|---|---|
| `GET /api/v1/content/{id}` → `content_type:"movie"` | Detectar tipo | ✅ 200 movies, 404 series |
| `GET /api/v1/movie/{id}` | Detalle película (title, images, duration_ms, year, studio, rating) | ✅ 200 |
| `GET /api/v1/movie/{id}/media/dash/{locale}` | Playback master de película | ✅ 200 |
| `GET /api/v1/series/{id}` | Detalle serie | ✅ 200 series, 404 movies |
| `GET /api/v1/episode/{id}/media/dash/{locale}` | Playback de episodio | ✅ 200 |
| `GET /api/v1/search?query=` → `movies[]` + `series[]` | Búsqueda | ✅ 200 |

### Fix implementado en `UniquestreamProvider.kt`
1. **Detección de películas**: set `movieIds` poblado en `toSearchResponse()` (main page + search) y `probeContentType()` que hace `GET /content/{id}` como respaldo (si `content_type` presente → movie).
2. **`load()`**: si es movie → `loadMovie()` usa `GET /movie/{id}`, devuelve `newMovieLoadResponse(TvType.AnimeMovie)` con poster, plot (desc + año/estudio), duration, year, score. Sin episodios (es película).
3. **`loadLinks()`**: si es movie → `GET /movie/{id}/media/dash/{locale}`; si no → `/episode/` (flujo anterior intacto).
4. **`search()`**: ahora parsea `series[]` + `movies[]` (antes solo `series[]`).
5. Master de movie: `get3.mediacache.cc/movie/{id}/{media_id}_{locale}/master.m3u8` — mismo `keyRegex`/interceptor de key (media_id corto tipo `575811` ya soportado).

### Estado
- Compilación OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` → `UniquestreamProvider/build/UniquestreamProvider.cs3` (89 KB)
- ⏸️ **Pendiente**: instalar cs3 en dispositivo y probar The Garden of Words (`dv_575811`) — detalle + reproducción ja-JP/es-419.

## UniqueStreamProvider — Diagnóstico freeze CDN (12 Ago 2026)
- **Síntoma**: EP8 de Fate/strange Fake (ja-JP, content_id `kmRlqThD`) congelaba cada ~5s; pausa+espera solo fluido por unos minutos.
- **Medido desde PC (master/variante/segmentos reales de `get3.mediacache.cc`)**:
  - Solo **1 variante 1080p** (avg 5.5 Mbps). No hay 720p/480p (paths `v_1280x720` etc → 403, sign ligado a resolución; API ignora `?quality=`).
  - Segmentos ~3.8s de duración tardan 1.8→10.4s en descargar. Sostenido **4.5 Mbps**, y con headers completos/keep-alive **3.3-4.4 Mbps**; paralelo 2 conexiones 2.6 Mbps.
  - **Conclusión**: CDN throttleado por IP (igual veredicto que acek-cdn en Plushd). No hay fix de código — solo variante 1080p y CDN < bitrate. No confundir con cambios de catálogo (0.1.0-0.1.2) que NO tocan reproducción.
- AnimeOnsen emite el mismo patrón (1 sola calidad) PERO su CDN entrega > bitrate → pausa llena buffer y se mantiene fluido. No hay feature que copiar; es ancho de banda.
- Scripts: `fsf_speed.py`, `fsf_speed2.py`, `fsf_parallel.py`, `fsf_headers.py` en `%TEMP%\opencode\`.

## SyncPlugin (CloudStream Sync) — Sincronización entre dispositivos (13 Ago 2026)

### ¿Qué hace?
- Sincroniza datos de CloudStream entre dispositivos usando un **Proyecto de GitHub (ProjectV2)** sin servidor propio.
- Cada dispositivo se guarda como un **DraftIssue** del proyecto; su body es un JSON comprimido (GZIP+Base64) con el backup.
- 5 categorías: `bookmarks` (favoritos), `resume` (progreso de reproducción), `search_history`, `extensions_repos`, `settings`.

### Archivos (SyncPlugin/src/main/kotlin/com/example/)
| Archivo | Rol |
|---------|-----|
| `SyncPlugin.kt` | Orquestador: listeners de prefs + `bookmarksUpdatedEvent`, polling 30s, `runSync` (restore desde el dispositivo con `updatedAt` más reciente + push si hash cambió), debounce 2s por cambio |
| `SyncBackup.kt` | Construye el backup (datastore + settings SharedPreferences), clasifica claves en categorías, MD5 hash, merge por timestamps, filtrado por categoría |
| `SyncNetwork.kt` | GraphQL de GitHub (fetchProjectId, fetchDevices, registerDevice, updateDevice), GZIP+Base64 compress, `getDeviceId` = MD5(packageName+ANDROID_ID) |
| `SyncStorage.kt` | Persistencia de token/números/proyecto/deviceId vía `AcraApplication.getKey/setKey` |
| `SyncSettings.kt` | UI programática (AlertDialog): token, número de proyecto, checkboxes backup/restore por categoría, botón "Guardar y sincronizar" |
| `SyncData.kt` | Modelos serializables (BackupFile, BackupVars, SyncDevice, GitHubGraphQLResponse...) |

### Pontos clave del diseño
- `isBackupEnabled`/`isRestoreEnabled` por categoría (flags "true"/"false" en AcraApplication keys).
- Restore: coge el DraftIssue de OTRO dispositivo (`deviceId != propio`) con `updatedAt` más alto, hace merge por categoría comparando `categoryTimestamp` local vs `updatedAt` en epoch. `isRestoring=true` silencia los listeners durante el restore.
- Push: solo si `computeHash(data) != lastPushedHash`.
- `nonTransferableKeys` excluye cuentas/logins de terceros, rutas de descarga, keys propias del plugin, etc.
- Resume watching vía `HomeViewModel.getResumeWatching()` (nullable → `?.also`) con cache.

### 🔧 Fixes de compilación (13 Ago 2026)
- **`DataStore.getSharedPrefs`/`getDefaultSharedPrefs` NO resuelven** en el compilador del plugin (el jar `jetified-cloudstream.jar` SÍ tiene los métodos JVM pero el metadata Kotlin `mv=[2,2,0]`/`xi=48` no los expone). **Fix**: acceso directo a los ficheros (verificado por bytecode):
  - datastore → `context.getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE)` (el host usa exactamente ese nombre, confirmado con `javap -c`)
  - settings → `context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)` (= `PreferenceManager.getDefaultSharedPreferences`)
- `HomeViewModel.getResumeWatching()` devuelve `List<ResumeWatchingResult>?` → handle con `?.also` + caché.
- `forceSync` no es suspend (lanza `scope.launch` internamente) → se puede llamar desde `setOnClickListener`.

### 🔧 CRITICAL: NO usar kotlinx-serialization en plugins (13 Ago 2026)
- **Síntoma**: al instalar el plugin, CloudStream entra en "Modo seguro ACTIVADO" y el plugin crashea con:
  `java.lang.AbstractMethodError: abstract method ... GeneratedSerializer.typeParametersSerializers()`
  en `com.example.GitHubGraphQLError$$serializer` durante `decodeFromString`.
- **Causa**: el plugin se compila contra kotlinx-serialization **1.11** (transitiva de cloudstream3-pre-release) pero el **APK runtime** del usuario usa una versión **más antigua** (sin `typeParametersSerializers()` en `GeneratedSerializer`). El metadata del plugin API jar NO empaqueta kotlinx-serialization; el mismatch de versiones es fatal en runtime (modo seguro).
- **Fix definitivo**: migrar TODA la serialización a **Jackson vía `AppUtils`** (el patrón que ya usa `UniquestreamProvider` y funciona en dispositivo):
  - `AppUtils.parseJson<T>(string)` → deserializa (TypeReference reified)
  - `toPush.toJson()` → serializa (extensión `Any.toJson()`, importar `com.lagradost.cloudstream3.utils.AppUtils.toJson`)
  - Data classes **sin `@Serializable`**; usar `com.fasterxml.jackson.annotation.JsonProperty` para nombres como `__typename`, `addProjectV2DraftIssue`, `updateProjectV2DraftIssue`.
  - El mapper host (`MainAPIKt.getMapper()`) ya registra el módulo Kotlin (`kotlinModule`), confirmado por bytecode.
- **Regla para futuros plugins en este repo**: compilar JSON con Jackson + `AppUtils`, NUNCA kotlinx-serialization (mismatch de versiones garantiza crash).

### 🔧 UI SyncSettings (13 Ago 2026)
- **Bug 1**: forzar `setTextColor` con `resolveColor(context, textColorPrimary)` daba negro sobre fondo oscuro (attributos de tema resueltos como resource no son Int color directo). **Fix**: crear vistas con `activity` (NO `applicationContext`) y **no** forzar colores de texto — heredan el tema del diálogo.
- **Bug 2**: checkbox "Mostrar token" → `tokenInput.setSelection(tokenInput.text?.length ?: 0)` (`Editable?` nullable).
- Botón: `AppCompatButton` + `ViewCompat.setBackgroundTintList(ColorStateList.valueOf(holo_blue_dark))` + texto blanco.
- Campos: `GradientDrawable` redondeado semitransparente (blanco ~10% en dark / negro ~5% en light).

### 🔧 Diseño por dispositivo excluido del backup (13 Ago 2026)
- CloudStream guarda el modo de diseño (celular/TV/emulador/Automático) en **`app_layout_key`** (Int, `getInt/putInt` en settings, confirmado en `SetupFragmentLayout` con `R.string.app_layout_key`).
- Añadida a `nonTransferableKeys` en `SyncBackup.kt` → **nunca** se sincroniza; cada dispositivo conserva su diseño aunque el backup venga de otro.

### Estado
- ✅ Compilación OK: `.\gradlew.bat :SyncPlugin:compileReleaseKotlin --console=plain`
- ✅ Plugin empaquetado: `:SyncPlugin:make` → `SyncPlugin/build/SyncPlugin.cs3` (**48 KB**, 13 Ago 2026, con Jackson)
- ⏸️ **Pendiente**: reinstalar el cs3 (fix Jackson) y probar en dispositivo — crear el project (ProjectV2), token classic con scope, registrar un dispositivo como DraftIssue y verificar sync bidireccional.
- ⏸️ Pendiente: verificar que `updatedAt` entre los DraftIssues de GitHub se actualiza correctamente tras `updateDevice`.

## AnizoneProvider — Migración a items JSON en x-data (19 Ago 2026)

### 🔧 Problema original
- `search()` y `getMainPage()` devolvían **vacío**: el sitio anizone.to (Livewire 3 + Alpine) migró y los items ya NO están en `div[wire:key]` de la página. Ahora vienen como JSON en el atributo `x-data` de un `[x-data]` del componente `pages.anime-index`.

### Nueva estructura (verificada en PC)
- Main page/search: `<div x-data="{ items: JSON.parse('...'), nextCursor: '...', hasMore: true, ..., loadMore() { $wire.loadPage(this.nextCursor) } }">` dentro del componente con `wire:snapshot` de `pages.anime-index`.
- **Decode**: el atributo HTML tiene doble escape JS+JSON (p.ej. `\\u0022`, `\\\/`). El replace naive `replace("\\u0022","\"")` **rompe títulos con comillas internas**. Fix: `unescapeJsString()` char-por-char (JS unescape primero: `\\`→`\`, `\/`→`/`, `\uXXXX`→char; luego `JSONArray` termina con los escapes restantes).
- Campos del item JSON: `slug`, `url`, `cover`, `main_title` (con `"` literales), `title_list` (mapa `"1"`=EN, `"5"`=principal, `"8"`=JA), `type` ("TV Series"/"Movie"/"OVA"/"Web"), `start_year`, `episode_count`.
- Search por URL filtra server-side: `$mainUrl/anime?search=query` → x-data con items (sin Livewire).

### Paginación
- **Main page**: método Livewire `loadPage` con `params=[nextCursor]` → los items de la página siguiente vienen en **`effects.dispatches[0].params`** (dispatch `name="items-loaded"`, keys `items`/`nextCursor`/`hasMore`), NO en `effects.html`.
- **Episodios en `load()`**: el detail usa paginator Livewire `paginators.page` (36/página, Aikatsu 178 eps = 5 páginas). Update `{"paginators.page": "N"}` → los `li[x-data]` de esa página vienen en `effects.html`. El viejo `.h-12[x-intersect="$wire.loadMore()"]` ya no existe.

### 🔧 Player (loadLinks) — cambiado a vidstackPlayer
- El `media-player` + `<track>` + `span.truncate` ya no existen. El reproductor es `<div x-data="vidstackPlayer(JSON.parse('...'))">`.
- El JSON contiene: `src` (master.m3u8), `subtitles[]` (title/format/language/default/forced/file), `storage`, `snapshot`, `storyboard`, `chapter`, `fonts`.
- Fix: regex `vidstackPlayer\(JSON\.parse\('(.*?)'\)\)` (DOT_MATCHES_ALL) + `unescapeJsString` + `JSONObject` → `src` para el link, `subtitles[]` para `subtitleCallback`.
- Fuente: `div:containsOwn(Source:)` → siguiente sibling (ej. "Web").
- Master CDN: `https://suzaku.xin-cdn.xyz/{uuid}/master.m3u8` — responde 200, variantes 360/720/1080 + audio ja.

### Estado
- ✅ Compilación OK: `.\gradlew.bat :AnizoneProvider:compileReleaseKotlin --console=plain -q`
- ✅ Helpers nuevos: `unescapeJsString`, `parseItemsJson`, `findItemsXData`, `extractNextCursor`, `extractHasMore`, `getItemsLoadedParams`, `toResult(JSONObject)`.
- ⏸️ **Pendiente**: instalar cs3 en dispositivo y probar search, main page (paginación multi-página), load con muchos episodios (Aikatsu) y reproducción + subtítulos.

---

## ReanimeProvider — Estado (23 Ago 2026)

### Arquitectura del sitio (reanime.to)
- SvelteKit SPA, catálogo basado en AniList (covers de s4.anilist.co)
- APIs JSON limpias:
  - `GET /api/v1/search?q=` → `{results:[{anime_id, anilist_id, title{english}, cover_image{large}}]}`
  - `GET /api/v1/anime/{slug}` → metadata completa (anilist_id, description, genres, average_score 0-100, status)
  - `GET /api/v1/anime/{slug}/episodes?limit=2000` → `{data:[{episode_number, title, subbed, dubbed}]}`
- **Streams**: `GET /api/flix/{anilist_id}/{episode}` → `{success, servers:[{serverName:"HD-1"/"HD-2", dataLink:"https://flixcloud.cc/e/{hash}?v=N"}]}`
  - Preferencia del sitio: HD-2 → HD-1 → primero. v=1=sub v=2=dub aprox
  - Variante TMDB: `/api/flix/0/{ep}?tmdb={id}&season={n}`

### Cadena de descifrado flixcloud (REVERSED y validado end-to-end)
1. GET embed `flixcloud.cc/e/{hash}?v=N` con UA+Referer → HTML contiene estado SvelteKit serializado
2. Extraer: `obfuscation_seed`, `w_payload` (WASM b64), bloque `obfuscated_crypto_data` (kf_/ivf_), tokenField, keyFrag2Field
3. **Field mapping** (nombres de campos ofuscados): `e=seed; x3: e=SHA256(e+"0|1|2")`; luego segunda cadena idéntica desde e final:
   - tokenField = `${e[48:64]}_${e[56:64]}`, keyFrag2Field = `${s[0:16]}_${s[16:24]}`, kf/ivf = prefijos de la primera cadena
   - Los campos aparecen a veces CON comillas y a veces SIN (probar ambos regex)
4. `GET flixcloud.cc/api/m3u8/{token}` → JSON; claves: `sha256(token+"vid")[:10]` = enc_url b64, `sha256(token+"key")[:10]` = keymat b64
5. **WASM** (~350B, constantes/ops RANDOMIZADAS por carga): `_s(seedInt)` setea global; `_r(A,B,C,out,k)` computa por byte mezcla de A^B^C con adds/xors/shs y `(global + i*M)&255`; out=P (32B). `_c()` devuelve PK=mem[2064..2096]
   - SOLUCIÓN: mini-intérprete WASM embebido en el provider (clase MiniWasm) — parsea secciones export/code/DATA y ejecuta el subset de opcodes. Validado contra wasmtime con inputs aleatorios en múltiples payloads
   - IMPORTANTE: escribir A/B/C en mem[1000/1032/1064] ANTES de _r (offsets como el JS: C=1e3,I=C+k,q=I+k,et=q+k). La data-section inicializa mem[2000..2064] con 64 bytes = clave PK (PK=data[0:32]^data[32:64])
6. `PBKDF2-HMAC-SHA256(P, salt=seed_utf8, 1000 iter, 32B)` → XOR byte a byte con seed → SHA-256 = **clave AES-256-CBC**
7. `AES-CBC-decrypt(clave, iv=ivf_b64, ct=enc_url)` → **master.m3u8 URL** (JWT con client_ip binding)
8. GET master con header Referer `https://flixcloud.cc/` → body es BASE64; decodificar → si no empieza con #EXTM3U → XOR con PK → playlist real

### Detalles críticos descubiertos
- **El body de los playlists viene cifrado (b64+XOR con PK)**: el hls.js parcheado de flixcloud (`/artplayer-new/hls.js?v=103`) lo descifra en onSuccess leyendo `window.__pk` (generado por `_c()`). Implementado en `getVideoInterceptor`: probar cada PK activo hasta que el decode empiece con #EXTM3U
- El cifrado del playlist es OPCIONAL por archivo (One Piece E1 llegó plano una vez, Frieren cifrado siempre) → el interceptor debe manejar ambos casos
- Audio dual INTEGRADO en el HLS: `#EXT-X-MEDIA TYPE=AUDIO LANGUAGE="jpn"/"eng"` — ExoPlayer lo maneja nativo
- Subtítulos EXTERNOS `.ass`/`.srt` listados en el embed (`subtitles:[{url,language,format,default}]`) → subtitleCallback
- Páginas degradadas intermitentes (falta frag2/token): reintentar con carga fresca (resolveFlix reintenta 3x)
- Headers Sec-Fetch completos reducen páginas degradadas
- VOE/HgLink en otros sitios usan gates ALTCHA-PBKDF2/JS challenge → NO resolubles sin WebView

### Archivos
- `ReanimeProvider/src/main/kotlin/com/example/ReanimeProvider.kt` — provider completo + MiniWasm interpreter
- Compilación OK: `.\gradlew.bat :ReanimeProvider:make --console=plain -q`
- ⏸️ Pendiente: probar en dispositivo (home/search/load/playback + subtítulos ASS)
### FIX segmentos cifrados (23 Ago 2026 v2)
- Los segmentos `seg-N-f1-v1-a0.png`/`.webp` (CDNs atomic4cdn.top/stronghole.site) traen: firma PNG falsa (8B) o RIFF-WEBP (12B) + contenido **XOR-cifrado**
- Clave XOR FIJA de 16B encontrada en el fetch-loader del hls.js parcheado (`flix_hls.js`): `[9d,2a,f1,47,b3,8e,5c,70,a6,19,e4,3b,d8,62,0f,c5]`
- Lógica exacta del JS: si tras saltar la firma el byte NO es 0x47 (sync TS) → XOR cíclico key[i&15]
- Implementado en getVideoInterceptor (aplica a TODAS las responses del link con esas firmas)
- Verificado en PC: 100/100 sync bytes TS tras descifrar
- stronghole.site (HD-1) da 403 a todo sin sesión de navegador → HD-2 (vault-96.atomic4cdn.top) es el servidor que funciona
### FIX subtítulos con posición (24 Ago 2026 v7)
- Subtítulos ahora se convierten **ASS→VTT** (no SRT) conservando POSICIONES:
  - [V4+ Styles] → map styleName→Alignment (numpad 1-9)
  - Override `\anN` en el texto tiene prioridad sobre el estilo
  - Mapeo a WebVTT cue settings: bottom=default, middle=`line:50%`, top=`line:0`; odd/even → `align:left/right`
- Emitidos como URLs falsas `flixcloud.cc/__sub/{n}.vtt` servidas por el interceptor (sin red)
- Extensión .vtt → ExoPlayer infiere TEXT_VTT nativo
- Validado: One Piece E1 → 26 cues arriba (canciones/signos), resto abajo
### FIX definitivo posiciones (24 Ago 2026 v9) — marcador nativo {anN}
- **CS3 tiene fixSubtitleAlignment nativo** (CustomDecoder.Companion): `locationRegex = "\{\an(\d+)\}"` busca el marcador `{anN}` EN EL TEXTO del cue → setLineAnchor/setLine/setPosition según numpad SSA y ELIMINA el tag al renderizar
- Por eso AnimeParadise funcionaba sin hacer nada: sus VTT traen `{anN}` residual en el texto
- Mi conversor ahora emite el marcador: si alignment!=2 → prefija texto con `{anN}` (derivado de \an override > \pos proporcional > estilo)
- Los settings line:/align: de VTT se QUITARON (CS3 los ignoraba/sobrescribía con su default)
- Prioridad conversión: \anN > \pos(X,Y)→numpad por proporción Y/PlayResY,X/1280 > estilo > 2
### FIX posters PV rotos (24 Ago 2026)
- **Síntoma**: en el home de PrimeVideo muchos posters no cargaban
- **Causa**: wsrv.nl (proxy de resize) devuelve 404 en ~50% de las imágenes — el fetch saliente del proxy hacia imgcdn.kim falla intermitentemente para muchas imágenes. NO es caché negativa ni rate-limit: images.weserv.nl igual (404), nonce no ayuda, statically.io bloqueado por imgcdn (403 total)
- **Origen directo imgcdn.kim: 20/20 OK** (~1.1MB promedio por poster, hasta 2.1MB)
- **Fix**: `buildVerticalPosterUrlWithProxy` vuelve a devolver la URL directa sin proxy
- Tradeoff: posters pesados otra vez, pero un poster lento es mejor que uno que no carga
- `?w=500` en imgcdn se ignora (devuelve tamaño original) — no hay resize nativo

## SoloLatinoProvider — Estado (30 Ago 2026)

### Arquitectura del sitio (sololatino.net)
- Películas y series latinas (español/VOSE)
- Cadena: `sololatino.net` → `embed69.org` (PoW + AES decryption) → links directos (vidhidepro, streamwish, voe)
- Hosts alternativos: `morencius.com` (→ vidhidepro), `xupalace.org` (go_to_playerVast)

### Funcional
- `getMainPage()` — 5 secciones (Últimas, Películas, Series, Recientes, etc.)
- `search()` — búsqueda por query string
- `load()` — detalle: título, descripción, poster, tipo (movie/tv), episodios (DOM parsing)
- `loadLinks()` — resuelve embed69 → PoW → AES decrypt → `loadSourceNameExtractor` → custom extractors
- `tryVidHideProExtraction()` — eval/packed JS unpack → `hls2`/`hls3` URLs from `var links={...}`
- `tryVoeExtraction()` — redirect chain → m3u8/mp4 extraction (CAPTCHA limitation)
- `getVideoInterceptor()` — inyecta User-Agent + Referer + Origin a CDN (dramiyos, phtilzjvfok, vidhidepro, vidhide)
- `unpackPackedJS()` — robust regex: finds `eval(function(p,a,c,k,e,d){...}('`, base-N dictionary replacement

### 🔧 FIX getVideoInterceptor not called (30 Ago 2026) — CRITICAL
- **Síntoma**: `getVideoInterceptor()` definido correctamente pero CloudStream NUNCA lo llamaba. Sin logs `[intercept] CDN request`. Error `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)` en todos los links.
- **Causa raíz**: CloudStream busca el provider con `getApiFromNameNull(link.source)` — necesita que `link.source == provider.name`. SoloLatinoProvider usaba `"LATINO[VidHide]"`, `"VidHidePro"`, `"Voe"` como source en `newExtractorLink` → `getApiFromNameNull` retorna null → interceptor nunca llamado.
- **Fix**: los 3 `newExtractorLink` ahora usan `source = "SoloLatino"` (= `provider.name`):
  - Fallback `loadExtractor` wrapper (línea 652): `"SoloLatino"`, label `"$source[${link.source}]"`
  - `tryVidHideProExtraction` (línea 728): `"SoloLatino"`, label `"VidHidePro - $chosen"`
  - `tryVoeExtraction` (línea 831): `"SoloLatino"`, label `"Voe"`

### 🔧 FIX unpackPackedJS broken URLs (30 Ago 2026) — CRITICAL
- **Síntoma**: interceptor ahora SÍ llamado pero CDN retorna 403. URL desempaquetada tenía nombres de parámetros faltantes: `?=TOKEN&=1788145211&=129600` (roto) vs `?t=TOKEN&s=1788145211&e=129600` (correcto).
- **Causa raíz**: `unpackPackedJS` usaba `HashMap` para el diccionario → iteración en orden aleatorio. El JS packer reemplaza de ÍNDICE ALTO→BAJO para evitar que palabras cortas del diccionario (como `t`, `s`, `e`, `f`) corrompan reemplazos más largos. `HashMap` procesa en orden random → las palabras cortas se reemplazan primero y corruptan los valores.
- **Fix**: eliminar `HashMap`, usar loop directo `for (idx in count - 1 downTo 0)` que procesa de alto→bajo, igual que el JS packer `while(c--)`.
- **Verificado con Python (PC)**: CDN retorna 200 OK con token fresco, incluso sin headers — el problema era 100% la URL rota del unpacker.

### 🔧 FIX preferOrder y relative URLs (30 Ago 2026)
- **PreferOrder cambiado**: `hls2 > hls3 > hls4` (antes `hls4 > hls2 > hls3`). `hls2` es el CDN conocido que funciona (`dramiyos-cdn.com`/`acek-cdn.com`). `hls4` tiene URLs relativas (`/stream/...`) que pueden no funcionar.
- **Relative URL fix**: si el m3u8 URL empieza con `/`, se prepende `https://vidhidepro.com`.

### ✅ VERIFICADO en dispositivo (30 Ago 2026)
- `source = "SoloLatino"` → interceptor llamado (`[intercept] CDN request`)
- Unpacker produce URLs correctas → CDN responde 200 (`application/vnd.apple.mpegurl`)
- Segmentos `.ts` descargados seg-1 a seg-8 → 200 `video/MP2T`
- Reproducción funcional en ambos servidores (dramiyos-cdn y acek-cdn)

### Patrón general para plugins
- `newExtractorLink(source=..., ...)` — el parámetro `source` DEBE ser `this.name` (el nombre del provider) para que `getApiFromNameNull()` lo encuentre y llame a `getVideoInterceptor()`.
- Providers que lo hacen bien: Uniquestream (`this.name`), Netflix (`name`), Primevideo (`name`), JioHotstar (`name`), Reanime (`name`), SoloLatino (`"SoloLatino"`)
- Providers con el mismo bug: Plushd (usa `"VidHide"` en vez de `"PlusHD"`)

### 🔧 FIX streamwish + voe sin links → fallback WebView (03 Sep 2026)
- **Síntoma**: solo se emitía el link vidhide hls2. Streamwish daba 0 links (página challenge "Loading... please wait", len=811, sin m3u8). Voe daba 0 links (redirect a mirror `johnbeyondnation.com` con CAPTCHA Altcha).
- **Causa raíz**: ambos requieren ejecución JS — StreamWish para pasar su challenge, Voe/Altcha que es PoW y se auto-resuelve en navegador real. `app.get` plano nunca lo pasa.
- **Fix**: `renderViaWebView()` (WebView + `outerHTML` tras 12s, patrón Tvenvivo) + refactor de parseos reutilizables:
  - `SoloStreamWish.parseHtml()` (miembro, antes inline en `getUrl`) — rama `streamwish` en `loadSourceNameExtractor`: primero `loadExtractor`, si 0 links → WebView + parse, emite con `source="SoloLatino"`.
  - `VoeExtractor.parseHtml()` (miembro, antes inline en `getUrl`) — `tryVoeExtraction`: si CAPTCHA o sin m3u8/mp4 → WebView + parse con `source="SoloLatino"`.
- `SoloLatinoPlugin.load()` guarda `SoloLatinoProvider.pluginContext` para el WebView.
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar en dispositivo — buscar `[SW] WebView` / `[Voe] WebView fallback` en logs y verificar que streamwish/voe emiten links.

### 🔧 FIX subtítulos eliminados en rewrite vidhide (03 Sep 2026)
- **Síntoma**: antes del cambio de vidhide salían subtítulos, después ya no.
- **Causa raíz**: el commit `078d4457` ("original sin subs") eliminó TODA la emisión de subtítulos (`scanPageForSubs`, `tryExtractSubsFromM3u8`, tracks `.srt/.vtt` del DOM).
- **Fix**: restauradas las 2 funciones adaptadas al flujo actual:
  - `scanHtmlForSubs()` + `scanPageForSubs()` — busca `.vtt/.srt` en HTML (con dedupe por `seen` set). Se llama en `loadSourceNameExtractor` (paralelo, por servidor) y en `tryVidHideProExtraction` (página + JS desempaquetado).
  - `tryExtractSubsFromM3u8()` — parsea `#EXT-X-MEDIA:TYPE=SUBTITLES` del master m3u8. Se llama en `tryVidHideProExtraction` tras emitir el link.
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar en dispositivo — buscar `[PageSubs]` / `[M3u8Subs]` en logs y verificar subtítulos en el player.

### 🔧 FIX streamwish post-challenge sin parse + voe mirrors (03 Sep 2026)
- **Hallazgo en logs**: WebView SÍ pasa el challenge SW (`pageHasJW=true hasSources=true`) pero el parser no hallaba nada — el video viene en packer Dean Edwards real, `file:` con URL relativa/protocol-relative, o iframe.
- **Fix SW** (`SoloStreamWish.parseHtml`): regex `file|src` con URLs `//` y `/` (resueltas vs `pageUrl`), desempaquetador Dean Edwards alto→bajo (`unpackDeanEdwards`, igual que el fix vidhide), un nivel de iframes, y log de contexto `file|sources` si falla.
- **Fix Voe**: si WebView sigue en CAPTCHA o no hay fuentes → probar mirrors alternos (`yip.su`, `donaldlineelse.com`, `tubelessceliolymph.com`) con el mismo hash `/e/` (portable en la red voe).
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar — buscar `[SW] M3U8 (eval)` / `[SW] file` / `[Voe] probando mirror` en logs.

### ⚡ Speedup loadLinks: paralelizar servidores + mirrors (03 Sep 2026)
- **Síntoma**: el video tarda ~45-60s en iniciar. vidhide listo a los ~5s pero `loadLinks` no termina hasta pasar SW-WebView (~20s) + Voe-WebView (~13s) + mirrors (~60s) **en serie** (`encryptedLinks.forEach`).
- **Fix**: `forEach` → `amap` en servidores embed69 (vidhide/SW/voe en paralelo ≈ 25s total); mirrors voe en paralelo con `AtomicBoolean` + timeout 15s→10s (donaldlineelse se colgaba 66s).
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar tiempo total hasta `loadLinks FIN`.

### ⚡ Speedup v2: WebView con salida temprana + mirrors primero + PoW rápido (03 Sep 2026)
- **Síntoma**: aún tarda en cargar (~30s). Los WebView esperan 12s fijos aunque el challenge se resuelva antes.
- **Fix**:
  - `renderViaWebView` ahora acepta `readyJs` y hace polling cada 2s: dumpea el HTML en cuanto aparecen marcadores (`SW_READY_JS` = jwplayer/m3u8, `VOE_READY_JS` = sin altcha + con blob/m3u8) en vez de esperar los 12s siempre.
  - Voe: mirrors (baratos, paralelos) ANTES que WebView en ambas ramas.
  - PoW: hex con tabla lookup (`toHexFast`) en vez de `"%02x".format` por byte (~5x más rápido).
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar — buscar `[WebView] listo antes de tiempo` en logs.

### 🔧 FIX freeze: emitir todas las variantes hls + cubrir nuevos CDNs (03 Sep 2026)
- **Síntoma**: Looney Tunes reproduce 5s y congela 40-60s. Log: segmentos `acek-cdn` tardan 10-20s (primer intento cuelga, el reintento OK). Mismo veredicto que Plushd v6: CDN lento, no código.
- **Hallazgo**: el unpack trae `hls2/hls3/hls4` (3 CDNs distintos) pero solo se emitía `hls2`. Además el interceptor NO cubría `premilkyway.com` (host del link SW) ni `honeycombbrandatelier.cyou` (hls3) → esos segmentos iban sin headers.
- **Fix**: emitir las 3 variantes como links separados (`VidHidePro - hls2/hls3/hls4`) para elegir CDN rápido; `cdnDomains` += `premilkyway`, `honeycombbrandatelier`.
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar eligiendo otra variante cuando acek-cdn se congele.

### 🔧 FIX crash WebView thread + probe de variantes + mirrors acotados (03 Sep 2026)
- **Bug 1 (crítico)**: `onPoll` corre en thread JavaBridge y llamaba `evaluateJavascript` directo → `WebViewMethodCalledOnWrongThreadViolation`, el dump nunca ocurría (SW WebView daba 0 links). **Fix**: postear el dump al Main via `mainHandler`.
- **Bug 2**: `acek-cdn` devuelve **502** (caído) → 2004 al reproducir hls2. **Fix**: probe paralelo de masters (timeout 10s) y solo se emiten variantes con 200; si ninguna responde se emiten todas (fallback).
- **Bug 3**: `donaldlineelse.com` se cuelga a nivel DNS 60-115s ignorando timeouts → CloudStream mata `loadLinks` a los 120s. **Fix**: fuera de la lista de mirrors + `withTimeoutOrNull(20s)` global.
- **Extra**: `cdnDomains` += `cyou` genérico (cubre `honeycombbrandatelier`, `autumnmeadowcollective`, `publicshowcase` y futuros hls3).
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar — buscar `[VH-Pro] probe hls2 -> 502` y elegir `hls4` (vidhidepro da 200).

### 🔧 FIX interceptor por path + voe multi-candidato (03 Sep 2026)
- **hls3 rota dominios** (`.shop`, `.space`, `.store`, `.sbs` además de `.cyou`) → el match por marca quedaba obsoleto cada episodio. **Fix**: matchear por path (`/hls2/`, `/hls3/`, `.urlset/`) + hosts fijos. Cubre marcas presentes y futuras.
- **Mirrors voe inútiles**: el primer base64 largo de la página suele ser señuelo (hash integrity) y el decrypt fallaba. **Fix**: probar TODOS los candidatos base64 con `decryptVoeF7(quiet=true)` hasta hallar uno con `source`/`direct_access_url`.
- Compilación OK: `:SoloLatinoProvider:compileReleaseKotlin`
- ⏸️ **Pendiente**: probar mirrors voe — buscar `[Voe] Found M3U8` tras `probando mirror`.

---

## TorrentioProvider — Plugin Stremio Torrentio (04 Sep 2026)

### Arquitectura (igual que Stremio real, sin servidor propio)
- **Catálogo/búsqueda/detalle**: Cinemeta `https://v3-cinemeta.strem.io` (sin API key):
  - `GET /catalog/{movie|series}/top.json` → main page (Películas/Series Top)
  - `GET /catalog/{movie|series}/top/search={q}.json` → search (ambos tipos en paralelo)
  - `GET /meta/{movie|series}/{ttId}.json` → detalle + episodios (`videos[]` con season/episode)
- **Streams**: Torrentio público `https://torrentio.strem.fun/stream/{movie|series}/{ttId[:s:e]}.json` → `{streams:[{infoHash, fileIdx, name, title, behaviorHints{filename}, sources[tracker:...]}]}` (verificado con tt0111161: ~50 resultados con seeders/tamaño/proveedor).
- **Magnets**: `magnet:?xt=urn:btih:{hash}&dn={filename}&tr={trackers}` — trackers del propio stream (`sources[]`) + lista ngosang (caché 1h, tope 15). Se emiten como `ExtractorLink` y los reproduce el **torrent player interno de CloudStream** (igual que TotalTorrent).
- Label: `Torrentio 1080p 👤100 💾6.9GB (+[fileIdx] si ≠0)`; quality vía `getQualityFromName`; tope 50 links.

### Reglas aplicadas
- JSON con Jackson vía `AppUtils.parseJson`/`toJson` (NUNCA kotlinx-serialization en plugins).
- Sin TMDB API key (Cinemeta no la pide). Sin Real-Debrid en v1 (los links son magnets, no HTTP).

### Limitaciones conocidas
- `fileIdx ≠ 0` (packs multi-archivo): el reproductor torrent puede elegir mal el archivo — se muestra `[N]` en el label como aviso.
- Sin seeders = no reproduce (normal en torrents). Elegir links con 👤 alto.
- Solo IDs `tt...` (IMDb). Lo demás se omite.

### Archivos
- `TorrentioProvider/build.gradle.kts` (v1, mx, Movie+TvSeries, logo torrentio)
- `TorrentioProvider/src/main/AndroidManifest.xml`
- `TorrentioProvider/src/main/kotlin/com/example/TorrentioPlugin.kt`
- `TorrentioProvider/src/main/kotlin/com/example/TorrentioProvider.kt`
- `plugins.json` → entrada `Torrentio` v1 (29517 bytes)

### Estado
- Compilación OK: `.\gradlew.bat :TorrentioProvider:compileReleaseKotlin --console=plain`
- Plugin empaquetado: `:TorrentioProvider:make` → `TorrentioProvider/build/TorrentioProvider.cs3` (29631 bytes)
- ⏸️ **Pendiente**: push a `master` (el bot lo publica en `builds`), instalar y probar búsqueda → detalle → link magnet → reproducción.

### Enfoques adoptados de yuzono/anime-extensions (04 Sep 2026)
- **Config inline en la URL**: `qualityfilter=cam,scr|sort=seeders/stream/...` — filtrado server-side (verificado: ordena por 👤 desc, sin CAM/SCR). Evita settings UI en v1.
- **`&index={fileIdx}` en el magnet**: selecciona archivo en packs multi-archivo (inofensivo si el reproductor lo ignora).
- **No adoptado**: trackers anime extra (ngosang basta), filtros por provider/idioma (requiere settings UI → v2 con Real-Debrid), TMDB API key (Cinemeta no la pide), `torrentioanime`/kitsu (solo anime; club de fans aparte).

---

## RetrotveProvider — MEGA.nz extraction (30 Ago 2026)

### Problem
- RetrotveProvider's `processPlayerPage` had a switch case for MEGA links that just logged "MEGA links require app installation, skipping" and did nothing
- The site uses MEGA as one of 6 servers (Iframe/blenditall, Opt2/mega.nz, Opt3/1fichier, Opt4/mega.nz embed, Opt5/yourupload, Opt6) for episode playback
- **Paso a paso 1x1** (trid=10532) has MEGA as the ONLY working server

### Solution: Local HTTP Proxy + AES-CTR Decryption
MEGA files are **AES-128-CTR encrypted** — ExoPlayer cannot play them directly. Solution is a local HTTP proxy:

1. **Parse URL**: Extract `file_id` and `key` from `mega.nz/embed/{id}#{key}`
2. **MEGA API**: POST to `https://g.api.mega.co.nz/cs` → get temporary download URL + file size
3. **Local proxy server**: Random port, ExoPlayer connects to `http://127.0.0.1:PORT/video`
4. **On-the-fly decryption**: Proxy fetches encrypted data from MEGA, decrypts AES-CTR, serves plaintext
5. **Range requests**: For seeking support, compute AES-CTR counter offset from byte position

### Key Implementation Details
- **Key derivation**: `aes_key[i] = key_bytes[i] XOR key_bytes[i+16]` for i=0..15; `iv[0..7] = key_bytes[16..23]`, rest zeros
- **Attribute decryption**: AES-CBC (not CTR) with zero IV for filename extraction; plaintext starts with "MEGA" prefix
- **Block alignment**: When Range start isn't block-aligned (16 bytes), decrypt dummy prefix bytes to advance CTR counter
- **ServerSocket timeout**: 60s; server thread stops when socket closed or process ends

### Files
- `RetrotveProvider/src/main/kotlin/com/example/MegaExtractor.kt` — MEGA API client + local HTTP proxy + AES decryption
- `RetrotveProvider/src/main/kotlin/com/example/RetrotveProvider.kt` — integration in `processPlayerPage` (lines 409-421)

### Build
- Plugin: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (51 KB)
- Compilation: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`

### ⏸️ Pending
- Test on device: Paso a paso 1x1 (trid=10532, mega.nz embed) — verify local proxy starts, ExoPlayer connects, video plays
- Test seeking (Range requests) — pause + seek to different position
- Test file naming — decrypt filename from MEGA attributes
- Test edge cases: large files (>1GB), slow connections, MEGA download quotas

### 🔧 Fix msd:1 multi-CDN shard download (01 Sep 2026 v54)
- **Sintoma**: episodios con `msd:1` (multi-server download) y 6 URLs CDN fallan — chunk 0 funciona (ftyp OK en CDN #1), pero chunks mas alla de ~55-70MB obtienen 0 bytes de TODAS las URLs CDN
- **Causa raiz (3 bugs)**:
  1. **CDN #0 skip**: despues de obtener URLs frescas (`retries % 5 == 0`), `cdnUrlIndex` se pone en 0 pero se incrementa inmediatamente a 1 — CDN #0 NUNCA se reintenta
  2. **Descarga parcial aceptada**: cuando CDN retorna 2.7MB de 4MB, `totalWritten > 0` se acepta como completo — deja gaps en el archivo
  3. **Sin routing aware de shards**: `msd:1` retorna 6 URLs que sirven diferentes rangos de bytes, pero se usan rangos absolutos en TODAS — CDN retorna 0 bytes cuando el rango esta fuera de su shard
- **Fix: Shard probing**: descarga 4MB de cada URL CDN, intenta descifrar en cada posicion de CHUNK_SIZE para encontrar MP4 valido → mapea cada URL a su offset de shard
- **Fix: Rangos relativos al shard**: para cada chunk, encuentra la URL cuyo shard contiene la posicion del chunk, calcula rango relativo (`start - shardOffset`), usa `$url/$relStart-$relEnd`
- **Fix: Deteccion de descarga parcial**: si `totalWritten < expectedSize`, revierte y reintenta
- **Fix: CDN #0 fresh URLs**: usa CDN #0 directamente sin incrementar despues de URLs frescas
- Compilacion OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (72 KB)
- Pendiente: instalar cs3 en dispositivo y probar episodio 5 (44NxgQha, 333MB, 6 URLs CDN, `msd:1`) — verificar que el shard probing encuentra los offsets correctos y que los chunks se descargan de las URLs correctas

### 🔧 Fix UFA URL fallback + exponential 509 backoff (02 Sep 2026 v66)
- **Síntoma (v65)**: episodio 5 (44NxgQha) obtuvo HTTP 509 en TODOS los CDNs para chunk 0. Intentó CDN#1→#5, URLs frescas, no-range test — todos 509 con Content-Length=0. Es throttle de bandwidth por IP, no bug de código.
- **Causa raíz**: el throttle 509 es por IP, NO por CDN. Probar diferentes CDNs no ayuda. El backoff anterior (15s×retries, cap 60s) era insuficiente — el cooldown de MEGA puede durar 5-10 minutos.
- **Fix 1: UFA URL como fallback** — `performUfaUnlock()` ahora retorna la URL UFA (antes solo retornaba boolean). Se guarda en `DiskStream.ufaUrl`. Cuando TODOS los CDNs fallan con 509 después del retry loop, se intenta la UFA URL como último recurso (bucket de rate-limit diferente).
- **Fix 2: Exponential backoff mejorado** — delays: 15s, 15s, 20s, 25s, 30s, 40s, 50s, 60s, 90s, 120s (antes: 15s×retries, cap 60s). Da tiempo al cooldown de MEGA para expirar.
- **No se puso UFA URL en cdnUrls** — porque el probe de shards la detecta como "mirrors" (sirve todo el archivo), rompiendo el mapeo de shards.
- Compilación OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (89 KB)
- ⏸️ **Pendiente**: probar en dispositivo — episode 5 aislado (no después de episode 1) para verificar UFA fallback + backoff.
