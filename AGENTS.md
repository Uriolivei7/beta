# AGENTS.md - Netmirror Plugin Development

## Reglas de trabajo (OBLIGATORIAS)
- **NUNCA ejecutar Gradle por cuenta propia.** Ni `compileReleaseKotlin`, ni `make`, ni `installDebug`, ni tests. Aunque el código "debería" compilar y aunque los errores sean obvios: **primero termina todos los cambios del proveedor, y solo compila cuando el usuario lo pida expresamente.** Si crees que hay un error de compilación, descríbelo y sigue editando; no lanc gradle para confirmarlo.
- **NUNCA hacer `git commit`, `git push`, `git tag` ni `git rebase` sin que el usuario lo pida de forma explícita.** Al terminar un proveedor: enumera los archivos tocados y el comando de compilación exacto que debe correr el usuario, y qué queda pendiente de probar en dispositivo.
- Al cerrar cada proveedor o fix importante, **añadir su sección a este `AGENTS.md`** con: arquitectura verificada, reglas del sitio, causa raíz, fix aplicado, estado de compilación y qué queda pendiente de probar en dispositivo. Es el único sitio donde se registra el progreso.


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

## PlushdProvider — CRITICAL Fix interceptor + hls3 3003 (04 Sep 2026 v4.1)

### 🐛 Causa raíz del 3003 y del congelamiento (verificado)
- **El interceptor NUNCA se llamaba**: CloudStream resuelve el interceptor por **`getApiFromNameNull(link.source)`** (CS3IPlayer.kt:1940-1941) — solo si `link.source` == nombre exacto del provider (`"PlusHD"`). Plushd emitía `newExtractorLink("VidHide", ...)` en `tryVidHideExtraction` (2 call-sites) → `getApiFromNameNull` retorna null → **sin interceptor** → los segmentos `.ts`/`.woff2` iban sin UA/Referer → CDN cortaba.
- **hls3 = 3003**: los segmentos son `seg-N-f1-v1-a1.woff2` (rutas relativas en el variant) y el master/variante son `.txt` (no `.m3u8`). El gate viejo `if (!url.contains(".m3u8") && !url.contains(".ts"))` los excluía → sin headers → 403 (`text/plain`) / 404 (`text/html`) → ExoPlayer `PARSING_CONTAINER_UNSUPPORTED (3003)`.
- **Verificado con Python (PC)** desde el embed `zhc8nmz6aurw`:
  | Request hls3 | Resultado |
  |---|---|
  | segmento `.woff2` sin headers | **403** `text/plain` |
  | solo UA | **404** `text/html` |
  | UA + Referer `vidhideplus.com` | **200** `video/MP2T`, sync `0x47` OK, 701240 B |
  | master `.txt` | 200 `application/vnd.apple.mpegurl`, 11 líneas, 3 variantes |
  | variant `index-f1-v1-a1.txt` | 200, 136 segmentos |
- **hls2 (acek-cdn)**: master/variante/segmentos `.ts` con token en URL — 200 ok, pero CDN **lento** (veredicto previo: < bitrate → freeze cada ~3s). El interceptor ahora inyecta headers también aquí.

### ✅ Fix implementado (PlushdProvider.kt)
1. **`source = "PlusHD"`** en los 2 `newExtractorLink` de vidhide (`VidHidePro - hlsX` y fallback `VidHide`) — `getApiFromNameNull` ahora encuentra el provider y llama `getVideoInterceptor`.
2. **`wrappedCallback` preserva el referer**: `this.referer = link.referer ?: data` (antes forzaba `data` = tioplus → los CDN vidhide necesitan referer `vidhideplus.com`).
3. **Interceptor ampliado**: `isPlaylist = .m3u8 || .txt || .urlset`; inyecta UA/Referer/Origin a **todo** lo que pase (`.txt`, `.woff2`, `.ts`, `.m3u8`). El peek Cloudflare + filtro 1080p solo se hace en playlists.
- Compilación OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:PlushdProvider:make` → `PlushdProvider/build/PlushdProvider.cs3` (44.7 KB)
- `plugins.json`: fileSize 44619 → 44712 (version 3)

### ⏸️ Pendiente
- Probar en dispositivo: (1) hls3 ya no debe dar 3003 (segmentos `.woff2` con headers); (2) hls2 puede mejorar el freeze (interceptor activo); (3) verificar en logs `[intercept]` o ausencia de 403 en `.woff2`.

---

## PlushdProvider — Veredicto final: throttle por IP del CDN vidhide (04 Sep 2026 v4.2)

### ✅ Interceptor arreglado (confirmado en logs del dispositivo)
- El fix v4.1 funcionó: `getVideoInterceptor` SÍ corre (`M3U8 filtered: 1739 -> 1344`, `Filtering out 1080p`), `tryVidHideExtraction` emite 2 variantes y el probe es correcto (`probe hls3 -> 200 (749B)`, `probe hls2 -> 200 (1739B)`).
- `preferOrder` cambiado a **`hls3 > hls2 > hls4`** (PlushdProvider.kt:536) — hls3 (businessgrowthhacks) auto-primero.
- `plugins.json`: fileSize 44712 (version 3) — sin cambios de bytes (solo orden de const).

### 🐛 El 3003/hls3 y el freeze/hls2 tienen la MISMA causa raíz: **throttle por IP del CDN**
- Probado en PC (script `plus_hls3_seq.py`) sobre el embed real `zhc8nmz6aurw`:
  | Request | Resultado |
  |---|---|
  | hls3 master `.txt` / variante `.txt` | 200 `application/vnd.apple.mpegurl` |
  | hls3 **seg-1** `.woff2` (con UA+Referer, ±Origin, ±cookies) | **200 `video/MP2T` sync `0x47` en 0.6s** (701240 B) |
  | hls3 **seg-2 en adelante** | `IncompleteRead` (62-191 KB de ~700 KB), **HTTP 520**, read-timeout |
  | embed page `vidhideplus.com/v/...` | no setea cookies (jar vacío) → las cookies no son la solución |
- **Interpretación**: el CDN permite ~700-900 KB de burst por IP y luego corta/trunca TODAS las conexiones durante un cooldown (decenas de segundos). Coincide con el patrón en el dispositivo:
  - **hls3 → 3003**: seg2 llega truncado (TS incompleto) → ExoPlayer `PARSING_CONTAINER_UNSUPPORTED`.
  - **hls2 → freeze ~4-5s + tarda ~60s en volver**: `SocketException: Socket closed` en `chain.proceed` (PlushdProvider.kt:250) cada ~54s + master re-fetch.
- Ambos CDNs (`acek-cdn` y `businessgrowthhacks`) pertenecen a la misma cuenta `m5QqjwpATPzb.*` → mismo throttle.
- **Veredicto**: no es arreglable desde el provider (ni headers, ni cookies, ni preferOrder, ni cambio de variante). Los 4 servidores SPA (`strp2p/upns/4meplayer/rpmstream`) dan `error 2001` (requieren JS/WebView) y `turbovid` da 3003. El cuello de botella es el servidor/CDN de vidhide.

### 📌 Decisión (usuario, 04 Sep 2026)
- **Dejarlo como está** — no más cambios de código para este síntoma. Si se quiere probar el camino WebView para los SPA (por si usan otro CDN), es esfuerzo alto y resultado incierto.

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
- ⏱️ **`timeout` de `app.get` está en SEGUNDOS (03 Oct 2026)** — verificado por bytecode en NiceHttp 0.4.13 (`custom$suspendImpl` pasa el `long timeout` directo a `call/connect/write/readTimeout(J, SECONDS)`, sin conversión). `timeout = 20000L` = 20000 segundos (sin timeout efectivo) → el request se cuelga hasta el killer de 120s (`Timed out waiting for 120000 ms`). Causó el bug "2do episodio sin links vidhide, solo se arregla reiniciando la app" (conexión envenenada al CDN throttled + sin timeout real). **Regla**: usar valores en segundos (`timeout = 20L`, no `20000L`). `withTimeoutOrNull/delay/postDelayed/waitMs` SÍ son ms (kotlinx/Android, no tocar). Corregido en SoloLatino(+Extractors) y SerieskaoExtractors (03 Oct 2026). Pendiente el mismo barrido en Mhdflix/PoseidonHD/Gnula/Danimados/Plushd/Monoschinos.

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

### 🔧 Fix Phase 4 shard parallel download (05 Sep 2026 v72)
- **Síntoma (v70/v71 en dispositivo)**: ep5 `44NxgQha` (msd:1, 6 CDN shards) nunca completa la descarga → al cambiar de episodio o re-abrir da 2001. Logs MegaExtractor (22:05-22:09, build v70):
  - `Chunk 16 incomplete: 2772555/4194304` reintentado **hasta attempt 29 sin éxito** (chunk que cruza el boundary del shard).
  - `HEARTBEAT port=32945 chunks=75/84 bytes=439192934/349407051` → **writtenBytes 439MB > fileSize 349MB** (doble conteo).
  - `Partial download deleted for Zt8m3LYQ` (v70 borraba parciales; v71 ya no).
- **Causa raíz (3 bugs en Phase 4)**:
  1. **`shardEnd = stream.fileSize` en TODOS los shard threads** (línea ~275) → cada thread iteraba TODOS los chunks del rango phase4 (solo skip si `chunkEndByte < shardOffset`), así que varios threads descargaban los MISMO chunks → `writtenBytes` se disparaba por encima de fileSize (doble conteo) + sobrescrituras concurrentes.
  2. **`downloadChunkFromShard` SIN cross-shard split**: pedía el chunk completo como rango absoluto a UNA shard URL → para un chunk que cruza el boundary de shard, el CDN solo sirve su porción → `incomplete` infinito (chunk 16).
  3. **`raf.setLength(start)` en incomplete** truncaba el ARCHIVO entero mientras otros threads escribían otros offsets → corrupción.
- **Fix (v72)**:
  - Phase 4: cada shard thread ahora tiene su rango `[shardStart, shardEndExclusive)` (siguiente offset o fileSize). Solo el thread dueño de cada chunk lo descarga (`chunkStartByte < shardStart → continue`, `>= shardEndExclusive → break`). Sin duplicados.
  - `downloadChunkFromShard` reescrito con **sub-chunks cross-shard** (misma lógica `SubChunk` que `downloadChunkWithFreshUrl`): cada sub-rango que cae en otra shard se pide a la URL de ESA shard con `relStart-relEnd` relativo. Reintento de chunk completo `if (totalWritten < expectedSize)` tras revertir `writtenBytes`.
  - Crucially: `raf.setLength` eliminado — no se trunca el archivo en descargas paralelas.
- Compilación OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (**98.4 KB**)
- `plugins.json`: version 72, fileSize 95762 → 98441
- ⏸️ **Pendiente**: instalar cs3 v72 y probar ep5 `44NxgQha` (aislado): esperar `chunks=84/84`, sin doble conteo en HEARTBEAT, y que al volver a ep1 se sirva desde cache/parcial.

### 🔧 Fix shard CDN hang → UFA fallback en Phase 4 (05 Sep 2026 v73)
- **Verificado en dispositivo (logs 22:53-22:55, build v72)**: el fix v72 FUNCIONÓ:
  - ep1 `Zt8m3LYQ` (1 CDN URL -> Phase 4 secuencial): descarga fluida + al cambiar de episodio: `Partial download kept for resume: mega_Zt8m3LYQ.mp4 (363MB, 35 chunks)` — resume en disco funciona.
  - ep5 `44NxgQha`: `CROSS-SHARD split` de chunks 16 y 82 OK (ambos se completan), shard threads con sus rangos exactos terminan (16/16/16/17 chunks), `HEARTBEAT` ya no excede fileSize (`bytes=286492491/349407051`).
  - ✅ **pero NUEVO bug**: shard thread `gfs270n459` (132MB-198MB) se quedó colgado en **chunk 35** (22:55:41 «downloading» sin completar) → HEARTBEAT congelado en `chunks=69/84` durante 15+s → ExoPlayer 2001.
- **Causa raíz**: cada shard thread usa SOLO su URL de shard y nunca cambia de fuente. Si un nodo CDN se cuelga (read timeout 60s repetido, p.ej. `gfs270n459` throttleado por IP), ese thread no avanza y el archivo nunca llega a 84/84 → el serve loop corta la conexión a los 30s → 2001.
- **Fix (v73)** en `downloadChunkFromShard` (`MegaExtractor.kt`): **UFA URL como fallback** en TODOS los fallos del shard:
  - **509**: probar UFA inmediatamente (igual que `downloadChunkWithFreshUrl`).
  - **HTTP != 200/206**: probar UFA desde el attempt 2.
  - **Catch (read timeout / socket)**: probar UFA desde el primer intento fallido.
  - **Incomplete / 0 bytes**: probar UFA desde el attempt 2.
  - UFA (`stream.ufaUrl`) es un mirror del archivo completo con bucket de rate-limit DISTINTO → sirve el rango absoluto `start-end` aun si el nodo shard está muerto. Ya venía del flujo v66 (`performUfaUnlock`) y se pasa a `DiskStream` en `startStreamProxy` (también en la rama resume).
  - **Lock fix**: `tryDownloadFromUfa` usaba `synchronized(raf)` mientras los shard threads usan `synchronized(stream.fileLock)` → riesgo de seek/write race en paralelo. Ahora usa `synchronized(stream.fileLock)` + `raf.seek(ufaWritePos)` incrementado por buffer.
  - `start`/`end` movidos fuera del `try` para que el catch pueda llamar a `tryDownloadFromUfa`.
- Compilación OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (**99.1 KB = 99071 B**)
- `plugins.json`: version 73, fileSize 98441 → 99071 (JSON válido, 66 entradas)
- ⏸️ **Pendiente**: instalar cs3 v73 y probar ep5 `44NxgQha` de nuevo — si `gfs270n459` se cuelga, el log debe mostrar `UFA fallback: requesting bytes ...` y el chunk 35+ debe completarse; expectativa `chunks=84/84`.

### 🔧 Fix seek-ahead 2001 → serve on-demand via UFA (05 Sep 2026 v74)
- **Verificado en dispositivo (logs 23:31-23:33, build v73)**: el test NO llegó a ejercitar el fallback UFA del shard:
  - ep1 `Zt8m3LYQ` (91 chunks, 363MB): resume a 36/91, descarga secuencial fluida chunks 35→60 (~2s cada uno, ~8MB/s sostenido). **PERO** el usuario hizo seek a ~306MB y el `handleStreamRange` esperó 30s por el chunk 73 (`Timeout waiting for chunk 73 pos=306430588`) → sirvió **0 bytes** → **2001**.
  - ep5 `44NxgQha` (84 chunks, 333MB): resume a 69/84; **el probe de 6 CDNs seguía corriendo al final del log** (`Shard #5: probing` 25s+), así que Phase 4 y el UFA fallback de v73 **jamás se ejecutaron**; los chunks 35-49 (shard gfs270n459) quedaron sin descargar durante ese tiempo.
- **Causa raíz**: el serve loop (`handleStreamRange`) solo espera a que el background downloader alcance el chunk pedido (máx 30s) y nunca lo descarga él mismo. Si el usuario hace seek AVANTE del progreso (ep1), o un shard está caído/throttlado y todavía no se ha mapeado (ep5), el chunk pedido por ExoPlayer no llega a tiempo → 0 bytes → 2001.
- **Fix (v74)** en `MegaExtractor.kt`:
  - `DiskStream.fetchChunkOnDemand(ci)` + `fetchChunkOnDemandUnlocked(ci)`: **descarga UN chunk concreto vía la UFA URL (mirror del archivo completo, rango absoluto `bytes=start-end`)** cuando ExoPlayer lo necesita. Guard `ondemandFetching` (synchronizedSet) para que varios threads de serve no descarguen el mismo chunk en paralelo; si otro ya lo está descargando, espera hasta 20s.
  - `serveAesKey`/`serveBaseIv` en `DiskStream`, seteado en `startStreamProxy` (mismos keys que usa el downloader). `fetchChunkOnDemand` usa `resolvedAesKey ?: serveAesKey` (igual que `downloadChunkWithFreshUrl` L.438/`tryDownloadFromUfa`).
  - `handleStreamRange` reescrito: espera 5s al background (`waitForChunk(5000)`), luego loop hasta 30s intentando `fetchChunkOnDemand(ci)` cada 5s. Si aún no hay chunk → timeout → break (como antes).
- **Resultado esperado**: seek al minuto 15 de ep1 → sirve el chunk 73 vía UFA en 1-3s en vez de 30s de timeout; ep5 con shard caído → el serve obtiene el chunk 35 vía UFA aunque el probe no haya terminado.
- Compilación OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` → `RetrotveProvider/build/RetrotveProvider.cs3` (101190 B)
- `plugins.json`: version 74, fileSize 99071 → 101190 (JSON válido, 66 entradas)
- ⏸️ **Pendiente**: instalar cs3 v74 y probar: (1) ep1 con seek AVANTE — log `On-demand UFA fetch chunk N` y reproducción sin 2001; (2) ep5 — el probe puede tardar pero el play debe continuar con `On-demand UFA fetch chunk 35+` según llegue el playhead.

---

## TelelibreProvider - Migracion a tele-libre.live + ClearKey sensa (15 Sep 2026)

### Dominio nuevo
- `mainUrl` cambiado `tele-libre.buzz` -> `https://tele-libre.live` (v2). Mismo motor (cards `a.channel-link`, `embed2.php`).

### Flujo sensa ClearKey (verificado en universaltv/axn/golden/studio-universal)
- Embed trae `var config = {"url":"...mpd","k1":"kid-hex","k2":"key-hex"}` + `var HEADERS='b64({"origin","referer"})'`.
- MPD en `cdn.sensa.com.ar` (302 a `cdn5x`/`smt-usr-edgeXX`), `cenc:default_KID` == k1. Requiere UA Chrome completo (UA corto -> 403).
- Segmentos son byte-ranges sobre la URL del MPD; desde IP 148.222.x.x dan 401 con cualquier header (posible bloqueo por IP o webtoken JWT de la extension).
- `handleSensaConfig()` emite `newDrmExtractorLink` DASH + `hexToB64Url()` (ClearKey exige kid/key base64url, el sitio da hex).
- `handleTokHtml` (?r=/cvattv) tambien emite keys ahora (antes las descartaba).
- `getVideoInterceptor` diagnostico: loguea `[cdn] METODO host/archivo -> codigo tipo` para sensa/cvattv.
- `load()`: `cleanTitle()` quita "Ver ... en VIVO Online Por internet".

### BLOQUEADOR: typo en CLEARKEY_DRM_UUID de CloudStream (app, no plugin)
- `ExtractorApi.kt:468`: `CLEARKEY_DRM_UUID = Uuid.fromLongs(-0x1d8e62a7567a4c37L, 0x781AB030AF78D30EL)`.
- LSB correcto (W3C EME / Android CDM / ExoPlayer C.CLEARKEY_UUID): `0x781A059057B03BAC`.
- LSB en CloudStream: `0x781AB030AF78D30E` (digitos traspuestos; coincide con el ID del registro DASH-IF, que NO es el UUID del CDM).
- Efecto: `CS3IPlayer when (drm.uuid)` no matchea el UUID correcto -> log `DRM Metadata class is not supported: DrmMetadata` -> DRM descartado -> contenido cenc en negro/silencio (MPD 200 en loop, 0 segmentos pedidos).
- Ningun provider puede reproducir ClearKey hasta que upstream corrija la constante a `0x781A059057B03BAC`.
- Verificado por bytecode: default `kty="oct"`, default `uuid=CLEARKEY_UUID`; licencia `{"keys":[{"kty","k","kid"}],"type":"temporary"}`.

---

## DonghualifeProvider - Migracion a Next.js 15 (05 Oct 2026)

El sitio fue reescrito de Drupal a **Next.js 15 (App Router)**. Todo el provider anterior quedo obsoleto; se reescribio completo con `org.json`.

### Rutas verificadas
| Ruta | Uso |
|---|---|
| `/series?page=N` | Catalogo series |
| `/peliculas?page=N` | Catalogo peliculas |
| `/series?page=N&sort=latest` | Series recientes |
| `/series/{slug}` | Detalle serie |
| `/peliculas/{slug}` | Detalle pelicula |
| `/watch/{seasonSlug}-{ep}` | Reproductor |
| `/api/series/{slug}/seasons/{seasonSlug}/episodes` | Episodios de temporada |
| `/api/player/source` + `/api/player/refresh` | Token de reproduccion |
| `/api/subtitles`, `/api/subtitles/{id}` | Lista / contenido de subtitulos |

### Reglas del sitio (IMPORTANTE)
- **NO existe `/search`** (404). La busqueda se hace en **dos** peticiones: `/series?q={q}` **y** `/peliculas?q={q}`. `/peliculas?search=` se ignora (siempre pagina 1).
- Cards: `a.poster-card[href]`, titulo desde `img[alt]` (NO `.title`, que sale vacio), poster desde `img[src]`. Admite `/_next/image?url=...`.
- Metadatos en `script[type=application/ld+json]` (`name`, `description`, `genre`, `image`, `datePublished`).
- **ID de watch = `{seasonSlug}-{episodeNumber}`**, NO `{seriesSlug}-temporada-N-M`. Ejemplos: `swallowed-star-1-3`, `doupo-cangqiong-especial-1`, `record-mortals-journey-immortality-season-6-5`.
- Los UUID `...-temporada-N-M` que aparecian en la portada devuelven **cero fuentes** -> no usarlos.
- Temporadas especiales: slug con sufijo numerico (`season-6`) o texto (`-especial`); se renumeran **despues** de la ultima temporada normal para que CS3 no las ordene al principio (mismo bug que en Uniquestream).
- Peliculas: `data = "pelis:{slug}"`.
- `GET /api/sources?episodeId=` es una DEMO que devuelve `example.com` -> no usar.
- En NiceHttp el JSON va en `requestBody = json.toRequestBody(...)`, **nunca** en `data = String`.

### Fuentes (muestreo de 17 episodios)
| Proveedor | Tratamiento |
|---|---|
| `ok.ru` | **El mas frecuente (12/17 solo ok.ru)** -> extractor HLS propio |
| `rumble` / `dailymotion` / `odysee` | `loadExtractor` |
| `R2` | M3U8 directo (master/init/segmentos 200) |
| `Mg` (mega.nz) | Dejado en `loadExtractor` (no se integro `MegaExtractor`) |

- **ok.ru**: GET `https://ok.ru/videoembed/{id}` con Referer -> parsear `[data-options]` -> `flashvars.metadata.hlsManifestUrl`. Master verificado con 6 variantes (144p-1080p) y segmentos 200. Fallback: URLs progresivas de `metadata.videos[]`.
- R2: los 404 iniciales eran transitorios del CDN; `index.m3u8`, `init.mp4` y segmentos responden 200.

### Subtítulos (ASS -> VTT)
- `GET /api/subtitles/{id}` da **403 sin Referer** y 200 con el Referer de la pagina `/watch/...`.
- El body es JSON `{content: <ASS>}`. Se convierte a VTT (respeta `\\anN`), se cachea en memoria y se sirve en URLs falsas `$mainUrl/__sub/{id}.vtt` desde `getVideoInterceptor` con `Content-Type: text/vtt`.
- CS3 detecta el `.vtt` por extension y lo procesa nativamente.

### Fixes de compilacion (05 Oct 2026)
- **`parseJson<T>()` es INUTILIZABLE en plugins**: el CloudStream jar es JVM 11 y `build.gradle.kts` fuerza `JvmTarget.JVM_1_8` -> `Cannot inline bytecode built with JVM target 11 into bytecode that is being built with JVM target 1.8`. Por eso este provider usa **solo `org.json`** (mismo criterio que la regla de Jackson, ver SyncPlugin).
- **`loadExtractor` NO acepta lambda final**: `loadExtractor(url, referer, subCb) { link -> ... }` -> `Suspension functions can only be called within coroutine body`. Usar el patron de callback capturado (igual que `SoloLatinoProvider.kt:547`): declarar `val collector: (ExtractorLink) -> Unit = { ... }` y pasarlo como 4to argumento.
- `search()` debe devolver `List<SearchResponse>?`; `newSearchResponse` y `SpacerCard` no existen en esta API del plugin.

### Estado
- Compilacion OK: `.\gradlew.bat :DonghualifeProvider:make --console=plain -q`
- Plugin: `DonghualifeProvider/build/DonghualifeProvider.cs3` (**49063 B**)
- `build.gradle.kts`: `version = 4`; `plugins.json`: version 4, fileSize 49063 (67 entradas)
- `load()` detecta URLs `/watch/...` y devuelve `MovieLoadResponse` con `movieData = watchId`, para que los cards de "Ultimos episodios" se reproduzcan en una pulsacion.
- Archivos tocados: `DonghualifeProvider/src/main/kotlin/com/example/DonghualifeProvider.kt`, `DonghualifeProvider/build.gradle.kts`, `plugins.json`, `AGENTS.md`.
- ⏸️ **Pendiente**: probar en dispositivo ok.ru (extractor + interceptor), rumble/dailymotion/odysee via `loadExtractor`, y los subtitulos ASS->VTT con Referer.

### 🐛 Fix "sin episodios o temporadas" + search vacio (05 Oct 2026 v5)
**Sintoma (logcat del usuario)**: `sin temporadas en great-ruler-2-41`, `el-cazador-de-demonios-3-28`, `combat-continent-2-la-inigualable-secta-tang-1-173` y `GET https://donghualife.com/{uuid}-temporada-1-21 fallo: StandaloneCoroutine was cancelled`. Ademas el search no mostraba ninguna tarjeta.

**Causa raiz 1 (ids sueltos)**: `parseLatestEpisodes` y la lista de episodios de `load()` usaban el **watchId desnudo** como `SearchResponse.url` / `EpisodeItem.id` (`great-ruler-2-41`). `load()` solo detectaba `/watch/` en la URL, asi que un id suelto caia en la rama de serie -> `GET /{id}` -> pagina vacia -> "sin temporadas". CS3 ademas prepende `mainUrl` a las urls relativas (por eso los GET salian a `https://donghualife.com/{id}`).

**Causa raiz 2 (temporadas truncadas)**: `initialEpisodes` del RSC viene **incompleto** en temporadas != 1. Verificado: `great-ruler-1` inline=52 de 52, `great-ruler-2` inline=**10 de 41** (la API `/api/series/great-ruler/seasons/great-ruler-2/episodes` devuelve los 41). `seasonEpisodes` devolvia los inline sin comprobar nada -> la T2 aparecia con 10 episodios.

**Causa raiz 3 (peliculas)**: `load()` hacia `MOVIE_PREFIX + slug` con `slug = "pelis:{slug}"` (el id ya traia prefijo) -> `pelis:pelis:{slug}` -> `/peliculas/pelis:{slug}` -> 0 fuentes. Afectaba a peliculas de portada y de search.

**Fixes**:
- `parseCards` y `parseLatestEpisodes` emiten **URLs absolutas** (`$mainUrl/series/{slug}`, `$mainUrl/peliculas/{slug}`, `$mainUrl/watch/{id}`). Elimina la ambigüedad con el prepending de `mainUrl` que hace CS3.
- Nueva `WATCH_PREFIX = "watch:"` para los ids de episodio. `load()` reconoce episodio por (a) prefijo `watch:`, (b) `/watch/` en la URL, o (c) id suelto con forma `{seasonSlug}-{ep}` (`EPISODE_SHAPE`) -> `episodeResponse()` devuelve `MovieLoadResponse` con `movieData = "watch:{id}"`. `loadLinks()` quita el prefijo al construir la URL.
- `seasonEpisodes(seriesSlug, slug, initial, expected)`: usa la API cuando `inline.size < episodeCount` (nuevo data class `SeasonRaw`).
- `movieData = MOVIE_PREFIX + (movieSlug ?: slug)` — evita el doble prefijo.
- `search()`: log de entrada/salida (`search 'q' -> N series, M peliculas`), `CancellationException` re-lanzada, excepcion general -> `null`, y `null` si la lista queda vacia.
- `plugins.json`: `tvTypes` ahora `["Anime","OVA","AnimeMovie"]` (solo declaraba `["Anime"]`, aunque `build.gradle.kts` ya tenia los tres y el provider sobrescribe `supportedTypes` en codigo).

**Verificado por bytecode por que el search deberia funcionar**: `SearchViewModel.search` -> `APIRepository.search(query,page)` -> `withTimeout(getTimeout(searchTimeoutMs))` (**120000ms** por defecto, `coerceIn(5000,480000)`) -> `MainAPI.search(query,page)` -> **`invokevirtual search(String,Continuation)`** (offset 131 del `search$suspendImpl`), o sea que **si llama al override de 1 argumento** del provider. El endpoint del provider coincide con el del buscador del sitio: el JS de la home hace `router.push('/series?q=' + encodeURIComponent(q))`.

**Descartado**: los UUID legacy `{uuid}-temporada-N-M` **NO** son enlaces muertos. Verificado: `/watch/1c684f19-...-temporada-1-21` tiene `sources` (rumble, dailymotion) y el token resuelve 200 a `rumble.com/embed/...`. Se habiaadded un filtro `DEAD_WATCH` que los descartaba -> **eliminado**.

**Otros verificados**: `/peliculas/*` tiene `sources` (rumble/vk, dailymotion/ok.ru) y tokens que resuelven 200; los labels vienen en variantes de caja (`Ok.ru`, `Dailymotion`, `rumble`, `vk`) pero el dispatch se hace por URL resuelta, no por label; las peliculas **no** traen `episodeId` (sin subtitulos); la busqueda del sitio es server-side y correcta (0 resultados solo para titulos ausentes del catalogo: "one piece", "re:zero", "solo leveling").

### Estado v5
- `build.gradle.kts`: `version = 5`; `plugins.json`: version 5, tvTypes `[Anime, OVA, AnimeMovie]`, `fileSize` **pendiente de actualizar** tras compilar.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar. Para diagnosticar el search: `adb logcat -s DonghuaLife:V` y buscar `search() llamado:` / `search '...' -> N series`.
- ⏸️ Verificar tambien ok.ru, rumble/dailymotion/odysee/vk y los subtitulos ASS->VTT.

### 📊 Logging de diagnostico + seccion Action en home (05 Oct 2026 v5.1)
- **Motivo**: el search no mostraba nada en dispositivo y hacia falta ver donde falla exactamente cada etapa.
- **Seccion nueva en `getMainPage`**: `Acción` desde `/genres/Acci%C3%B3n?page=N` (verificado: 50 poster-cards en la pagina 1, paginado `?page=N`). Se usa `encGenre()` porque el sitio exige **percent-encoding UTF-8** en el path (`URLEncoder.encode` + `+`→`%20`).
- **Logging anadido** (todo con `TAG = DonghuaLife`, ver `adb logcat -s DonghuaLife:V`):
  | Etapa | Logs |
  |---|---|
  | `fetchDoc` | `GET ok {ms}ms len={n} url` / `GET fallo {ms}ms url -> Excepcion: msg` |
  | `getMainPage` | `getMainPage page=N` + resumen `popular/recientes/pelis/accion/ultimos` + `sin listas` |
  | `search` | `search() llamado: 'q'`, `search 'q' -> N series, M peliculas, N total`, `cancelada`, `fallo` |
  | `load` | `load(url) -> Episodio <id>` / `-> pagina <url>`, `pagina vacia`, `jsonld=<bool> titulo=`, `temporadas=N (T1:slug=52, ...) episodios=N` |
  | `seasonEpisodes` | `temporada <slug>: inline N/M (sin API)` / `-> consultando API` / `API devolvio N/M en Xms` / fallo con fallback a inline |
  | `loadLinks` | `loadLinks data='..' -> pagina <url>`, `pagina no disponible`, `fuentes=N [label(provider)]`, por fuente `sin links`, y final `OK/SIN LINKS (N fuentes) Xms` |
  | `resolveSource` | `source ok Xms -> url`, `source fallo y refresh no devolvio token`, `source ok tras refresh` |
  | `emitOkru` | `ok.ru <id> -> HLS`, `sin manifest`, `sin URLs` |
  | subtitulos | `subtitulos: N`, `subtitulo <id> fallo` |
- **Mejora de robustez en `seasonEpisodes`**: si la API falla o devuelve 0, ahora **devuelve los inline** en vez de lista vacia (antes la temporada se quedaba vacia).
- ⚠️ **TRAMPA CRITICA (no revertir)**: `fetchDoc` hace `Jsoup.parse(text, url)` — el **baseUri es obligatorio**, porque `parseCards` lee `a.attr("abs:href")`. Con `Jsoup.parse(text)` (sin baseUri) `abs:href` devuelve la ruta RELATIVA (`/series/xyz`) y se rompen los links de todas las tarjetas. Se cambiò de `.document` a `.text` + `Jsoup.parse` solo para poder loguear el tamaño real del body.

### 🎬 Generos del home: como añadir mas (05 Oct 2026 v5.2)
- **Mecanismo**: todo esta en la constante `GENRE_SECTIONS` (`DonghualifeProvider.kt:38`). **Añadir un genero = añadir una linea**. `getMainPage` hace un `async` (fetch paralelo) por genero a `/genres/{nombre}?page=N` y crea un `HomePageList` por cada uno. Los generos vacios se omiten solos (`takeIf { it.isNotEmpty() }`).
- **El nombre debe coincidir EXACTAMENTE con el de `/genres/{nombre}`** (ver tabla). El path se codifica en UTF-8 con `encGenre()` = `URLEncoder.encode` + `+`→`%20` (`Artes marciales` → `Artes%20marciales`, `Acción` → `Acci%C3%B3n`).
- **Lista canonica** (sacada de `https://donghualife.com/genres`, que es un indice con `a.surface[href^=/genres/]` → `h2` = etiqueta, `p` = nº de series). Total 352 series.:
  | # | Genero | # | Genero | # | Genero | # | Genero |
  |---|---|---|---|---|---|---|---|
  | 272 | Acción | 74 | Artes marciales | 14 | Demonios | 6 | Militar |
  | 150 | Aventura | 41 | Comedia | 13 | Isekai | 5 | Bélico |
  | 149 | Cultivo | 40 | Reencarnación | 11 | Magia | 5 | Alquimia |
  | 102 | Animación | 40 | Venganza | 10 | Seinen | 5 | Escolar |
  | 98 | Fantasía | 33 | Ciencia Ficción | 9 | Sistema de cultivo | 5 | Historia |
  | 96 | Romance | 30 | Misterio | 4 | Estrategia | 4 | Harem |
  | 87 | Drama | 29 | Superpoder | 3 | Guerra y política | 4 | Conspiración |
- **Ojo**: `/genres/{X}` responde **200 con 0 cards** para nombres inexistentes o vacios (p.ej. `Sci-Fi`, `Wuxia`, `Mecha`, `Cultura`, `Psicológico`) → la seccion simplemente no aparece, no rompe.
- **Ojo 2**: el sitio duplica enlaces en minusculas (`/genres/acci%C3%B3n`, `/genres/acci%C3%B3n.`) y esos **NO** traen series. Usar siempre la variante con mayusculas de la tabla.
- **Coste**: cada seccion de genero son ~180-260 KB. Con 8 generos son 12 fetches en paralelo por pagina del home (4 base+ 8 generos) → ~2 MB por paginacion. Si molesta en datos moviles, recortar `GENRE_SECTIONS`.
- El log de `getMainPage` ahora incluye el desglose: `generos=Acción=50, Aventura=44, Cultivo=50, ...`

### 🐛🐛 CRITICO fixUrl(): "enlaces no encontrados" en TODOS los episodios (05 Oct 2026 v6)
**Sintoma (logcat del usuario, v5)**: todos los episodes dan "enlaces no encontrados":
```
loadLinks data='https://donghualife.com/watch:blades-guardians-1-1' -> pagina https://donghualife.com/watch:blades-guardians-1-1
GET ok 226ms len=37019 https://donghualife.com/watch:blades-guardians-1-1
loadLinks fuentes=0 []
sin fuentes en ..., delegando a loadExtractor
```
Ademas `load(https://donghualife.com/supreme-god-emperor-2-580)` -> pagina vacia (37019 B) -> `sin temporadas`.

**CAUSA RAIZ (confirmada por bytecode de `MainAPIKt.fixUrl`)**: mi prefijo `WATCH_PREFIX = "watch:"` **NO es una URL**, asi que CS3 lo trato como relativa y le prependio `mainUrl`:
```kotlin
// MainAPIKt.fixUrl(api, url) — decompilado
if (url.startsWith("http") || url.startsWith("{\"") || url.startsWith("[")) return url  // intacta
if (url.isEmpty()) return ""
if (url.startsWith("//")) return "https:$url"
if (url.startsWith("/")) return api.mainUrl + url
return api.mainUrl + "/" + url        // <-- "watch:x" cae aqui
```
Y **`newEpisode()` SIEMPRE la aplica**: `newEpisode(api, url, init, fixUrl=true)` (el default de `newEpisode$default` pone `iconst_1` en el flag, offset 20-21) -> `fixUrl(api, url)` en el offset 29. Log: `watch:blades-guardians-1-1` -> `https://donghualife.com/watch:blades-guardians-1-1` -> el sitio responde **200 con la pagina de 404 (37019 B)**, sin `sources` → 0 enlaces.
Tambien lo aplican `newMovieSearchResponse` y `newAnimeSearchResponse` (offset 45 en ambos). `newMovieLoadResponse` y `newTvSeriesLoadResponse` **NO** pasan por fixUrl.

**REGLA (definitiva)**: **emitir siempre URLs ABSOLUTAS `https://donghualife.com/...`** en `newEpisode`, `newMovieSearchResponse`, `newAnimeSearchResponse`, `newMovieLoadResponse` y `newTvSeriesLoadResponse`. Nunca `watch:x`, nunca `pelis:x`, nunca rutas relativas.
Helpers anadidos: `watchUrl(id)`, `seriesUrl(slug)`, `movieUrl(slug)`.

**Fixes**:
- `newEpisode(watchUrl("$seasonSlug-$ep"))` (antes `WATCH_PREFIX + ...`).
- `episodeResponse`: `movieData = watchUrl(watchId)`.
- `load()` de peliculas: `movieData = pageUrl` (la URL absoluta ya resuelta).
- `newTvSeriesLoadResponse(title, seriesUrl(slug), ...)`.
- `loadLinks(data)`: si `data` es absoluta se usa tal cual; si trae `/watch/` o `/peliculas/` se reconstruye; si es un id suelto -> `watchUrl(id)`.
- `load()`: detecta episodio por `/watch/` en la url (o id suelto legado), sin depender de prefijos.
- **Eliminados** `MOVIE_PREFIX`, `WATCH_PREFIX`, `EPISODE_SHAPE` y `parseLatestEpisodes`.

### 🗑️ Quitada la fila "Ultimos episodios" + fix numeracion de temporadas (05 Oct 2026 v6)
- **Peticion del usuario**: la fila "Ultimos episodios" del home estorba (generaba cards de episodio con MovieLoadResponse y ruido en el log). Eliminada de `getMainPage` junto con el fetch de `$mainUrl/` y la funcion `parseLatestEpisodes`. El home queda: Popular, Recientes, Peliculas y las 8 secciones de genero.
- **Bug numeracion de temporadas**: `westward-5-0` salia como **T9** (era la T5, 64 eps). Causa: el numero se sacaba de `slug.substringAfterLast('-')` -> "0" -> no >0 -> caia en `base+1+idx` = 4+1+4 = 9. Ahora `seasonNumber(slug)` recorre los segmentos **de derecha a izquierda** y devuelve el primer entero > 0: `x-1`->1, `x-season-2`->2, **`x-5-0`->5**. Los `isSpecial` se numeran al final (base+1...).

### 🐛 Fix dailymotion: loadExtractor devuelve 0 links (05 Oct 2026 v7)
**Sintoma (logcat del usuario, v6)**: el fixUrl quedo **resuelto** (`loadLinks data='https://donghualife.com/watch/blades-guardians-1-1'` -> pagina 91401 B -> `fuentes=1 [dailymotion]` -> `source ok 141ms -> https://geo.dailymotion.com/player/xhojl.html?video=k5DBqQlgteD31hzaRfI`), pero:
```
sin links para dailymotion (https://geo.dailymotion.com/player/xhojl.html?video=k5DBqQlgteD31hzaRfI)
loadLinks ... -> SIN LINKS (1 fuentes) 1717ms
```

**Investigacion (bytecode de `Dailymotion.getVideoId`, LA CAUSA RAIZ)**:
```kotlin
// Dailymotion.getVideoId(url) — decompilado
Url(url).encodedPath                      // geo: "/player/xhojl.html"
    .decodeURLPart()
    .substringAfter("/video/")            // "/video/" NO aparece -> devuelve el path ENTERO
    .takeIf { Regex("^[kx][a-zA-Z0-9]+$").matches(it) }   // no cumple -> null
```
- `getVideoId` saca el id **solo del PATH** (`/video/{id}`) y **NUNCA** del query `?video=`. Con `https://geo.dailymotion.com/player/xhojl.html?video=k5DB...` devuelve **null antes de hacer ninguna peticion** -> `loadExtractor` no emite nada -> "sin links". El manejo de `geo.dailymotion.com`/`video=` existe, pero **solo en `getEmbedUrl`**, nunca en `getVideoId`.
- `getVideoId` NO es el problema en si mismo con urls `www.dailymotion.com/video/{id}` (esa forma si la resuelve). Lo que rompe es la forma `geo...player...?...video=` que da el sitio.
- El endpoint que usa es `mainUrl + "/player/metadata/video/" + id` (literal en el BootstrapMethods via `StringConcatFactory.makeConcatWithConstants`) -> el mismo que usamos nosotros.
- Desde PC ese endpoint responde **200 con cualquier UA** (sin UA, `okhttp/4.12.0`, `ExoPlayerLib`, Chrome) -> no es bloqueo por UA. El modelo de CS3 (`Map<String, List<Quality>>`, `SubtitleData(label, urls: List<String>)`) coincide **exactamente** con el JSON real.
- El HTML de `geo.dailymotion.com/player/xhojl.html?video=ID` (200, 30 KB) es solo el shell JS del player: **no** contiene m3u8/manifestUrl (0 coincidencias) -> no sirve como fallback.
- `https://www.dailymotion.com/cdn/H264-1920x1080?video=ID` (progresivo) -> **403** en todas las resoluciones. `stream_formats` del metadata es solo un mapa de disponibilidad (`{"1080":"mpegts",...}`), sin URLs.

**Por que la version antigua no tenia este problema** (`git show d2c35036`): la version mas antigua (sitio Drupal) leia `.embed-links li a[data-video]` e `iframe[src]` de la pagina y hacia `loadExtractor(videoUrl, data, ...)` para TODO, asignando `found = true` **sin verificar** si el extractor devolvio algo. Esos iframes no eran urls `geo.dailymotion.com/player/...?video=`, asi que nuncaLocked. Desde `b415a68b` (sitio Next.js) el flujo es `resolveSource(token)` -> `geo.dailymotion.com/player/...` -> ahi se rompe. No era una regresion de `loadExtractor`: es que **la forma de la url cambio**.

**Fix implementado**: `emitDailymotion()` propio en el provider, que no depende del extractor de CS3:
- `dailymotionId(url)` saca el id de `?video=<id>`, `/video/<id>` o `/embed/video/<id>`.
- `canonicalDailymotion(url)` -> `https://www.dailymotion.com/video/{id}` (forma que CS3 si lee) para el fallback.
- `GET https://www.dailymotion.com/player/metadata/video/{id}` con `browserHeaders` + `Referer: https://www.dailymotion.com/embed/video/{id}`; **si falla, reintenta SIN headers** (por si el header extra provoca el rechazo).
- **Forma real del JSON (verificada)**: `qualities` es `{"auto":[{"type":"application/x-mpegURL","url":"https://cdndirector.dailymotion.com/cdn/manifest/video/x8lg96q.m3u8?sec=..."}]}` -> la calidad es una **LISTA** de objetos, no un objeto. Se soporta tambien el formato antiguo (objeto suelto).
- Emite cada URL como `newExtractorLink(name, "Dailymotion AUTO", url, M3U8)` con referer + headers del embed.
- Subtitles de DM: `subtitles.data` es un mapa `etiqueta -> {label, urls:[...]}` (urls como `List<String>`; se acepta tambien el formato objeto) -> `subtitleCallback(SubtitleFile(label, url))`.
- `loadLinks` hace dispatch por `url.contains("dailymotion.com")` **antes** del `loadExtractor` generico; si `emitDailymotion` no emite nada, reintenta con `loadExtractor(canonicalDailymotion(url))`.
- Logs: `dailymotion <id> metadata fallo (...)`, `... no es JSON: <160 chars del body>`, `... sin qualities`, `... -> OK` / `0 URLs`, `dailymotion <id> -> sub <label>`.

**Nota de compilacion**: `newExtractorLink(...)` es `suspend`, asi que las funciones que lo llaman deben ser `suspend` (`emitDmLink` y `emitDailymotion` lo son). Error tipico: *"Suspend function ... can only be called from a coroutine or another suspend function"*.

**Nota de calidad**: el HLS que expone DM para `k5DBqQlgteD31hzaRfI` (blades-guardians EP1) solo tiene 2 variantes, 848x360 y 512x216 -> **360p es lo maximo que ofrece el uploader**, no un fallo del provider (el progressivo 1080p da 403).

### 🐛 Fix rumble: CS3 no trae extractor de Rumble (05 Oct 2026 v8)
**Sintoma (usuario)**: dailymotion ya reproduce, pero los episodios con fuente rumble siguen sin reproducir.

**Investigacion**:
- La lista de clases en `com/lagradost/cloudstream3/extractors/` del jar (832 clases) **NO contiene ninguna clase Rumble** (tampoco Odysee). `loadExtractor(url-rumble)` no encuentra extractor -> **0 links siempre**.
- El sitio resuelve el token rumble a `https://rumble.com/embed/vXXXX/` (verificado: `record-mortals-journey-immortality-season-6-5` -> `https://rumble.com/embed/v7a23rm/`).
- `GET https://rumble.com/embed/v7a23rm/` desde PC: `requests` -> **403 challenge Cloudflare** ("Just a moment..."); **curl -> 200 (166 KB)**. Es fingerprint de TLS, no bloqueo de IP: OkHttp (`app.get`) debe pasar igual que curl.
- El HTML del embed trae el HLS directo: `https://rumble.com/hls-vod/{id}/playlist.m3u8` (verificado 200, master con variantes **hasta 2560x1440**). Solo hay 1 mp4 progresivo directo (baja calidad), asi que el HLS es la via.
- Los segmentos/variantes van a `hugh.cdn.rumble.cloud` (URLs con `?r_file=chunklist.m3u8&...`).

**Fix implementado**: `emitRumble()` propio + dispatch `url.contains("rumble.com")` **antes** del `loadExtractor` generico:
1. `app.get(embed)` con `browserHeaders` + `Referer: https://rumble.com/`; si falla, **reintento sin headers**. Unescape `\/` -> `/`.
2. Regex `https://rumble\.com/hls-vod/...playlist.m3u8` -> `newExtractorLink(name, "Rumble HLS", url, M3U8)` con referer + headers (ExoPlayer adapta calidades solo).
3. Si la url no era embed, busca `https://rumble.com/embed/...` dentro del HTML y reintenta una vez.
4. Respaldo: mp4 directos de `*.rumble.cloud` como `VIDEO`.
5. Si `emitRumble` falla, fallback a `loadExtractor` (que usa el `RumbleExtractor` del plugin, tambien mejorado).
- Logs: `rumble embed falló (...)`, `rumble: reintentando con embed ...`, `rumble -> HLS OK` / `MP4 OK` / `0 URLs`.
- El `getVideoInterceptor` no necesita cambios (el link ya lleva headers+referer; el interceptor es pass-through salvo `__sub` y ok.ru).

### 🔍 Comparativa RumbleExtractor (archivo del plugin) vs emitRumble (05 Oct 2026 v8)
El usuario apunto que existe `DonghualifeProvider/src/main/kotlin/com/example/RumbleExtractor.kt` (registrado en `DonghualifePlugin.kt` con `registerExtractorAPI`). Comparativa:
| Aspecto | RumbleExtractor (original) | emitRumble (provider) |
|---|---|---|
| Fetch | `app.get` sin timeout, 1 intento | `timeout = 30L`, reintento sin headers |
| Unescape `\/` | Si | Si |
| Regex HLS | `[^"']+` greedy | `[^"'\s\\]+?` lazy + `distinct()` |
| Link emitido | `source="Rumble"`, referer=embed, **sin headers** | `source=name`, referer+headers rumble |
| Fallbacks | Ninguno (silencio si no hay match) | embed-search + mp4s + `loadExtractor` |
| Logs | Ninguno | 4 mensajes de diagnostico |
- **Mejoras aplicadas al archivo** `RumbleExtractor.kt`: reintento sin headers, `timeout = 30L`, headers UA+Referer en los links emitidos, fallback a mp4 directos, logs con `TAG = RumbleExt`. Se mantiene registrado como red de seguridad (el dispatch del provider lo usa de fallback).
- **Verificado (PC)**: variante HLS mas baja y sus segmentos dan **200 sin headers**, TS valido (`sync 0x47`) -> si se emiten links, **reproducen**. El problema es solo emision (app.get 403 por Cloudflare o regex sin match), no playback.
- **Matching de `loadExtractor` (bytecode)**: itera `extractorApis` de atras hacia adelante y matchea por `strippedUrl.startsWith(strippedMainUrl)` (`schemaStripRegex = ^(https:|)//(www\.|)`). Los extractores del plugin van al final -> se prueban primero. `RumbleExtractor.mainUrl = https://rumble.com` si matchea `rumble.com/embed/...`.
- **Plan B si `app.get` da 403 tambien en el movil**: `com.lagradost.cloudstream3.network.WebViewResolver` existe en el jar (`resolveUsingWebView` devuelve `Pair<Request, List<Request>>` interceptados por regex). Solo implementarlo si el logcat muestra `rumble embed falló (HTTP 403 ...)` en el dispositivo.

### 🔧 FIX rumble WebView + log de código HTTP (05 Oct 2026 v9)
**Log del dispositivo (v8) que lo aclara todo**:
```
source ok 161ms -> https://rumble.com/embed/v78yvtc/
rumble -> 0 URLs              <- SIN mensaje "embed falló": app.get NO lanzó excepción
```
- **NiceHttp NO lanza excepción con 403** (verificado por bytecode: `Requests` no chequea `isSuccessful`, solo envuelve la respuesta; `NiceResponse` expone `code`/`isSuccessful` sin validar). Un challenge de Cloudflare llega como HTML silencioso -> regex sin match -> `0 URLs`.
- El HTML de `https://rumble.com/embed/v78yvtc/` desde PC (curl) **SÍ trae** `https://rumble.com/hls-vod/3x3_LqIIOso/playlist.m3u8`. Conclusión: el dispositivo recibió el challenge (403) en vez de la página real.
- **Odysee aparcado**: el embed `$/embed/` es un shell JS (15 KB, sin stream); el API `resolve` da 404; `player.odysee.com` está muerto; el JSON-LD trae `contentUrl` (`player.odycdn.com/api/v3/streams/free/...mp4`) pero responde **401 "edge credentials missing"** (CDN77 con token por sesión: ni Referer, ni firma del embed como query/body, ni cookies lo abren). Requiere el player JS vivo o WebView con autoplay+intercept.

**Fix v9 (`emitRumble` + `renderViaWebView`, patrón TvenvivoProvider)**:
1. `emitRumble` ahora loguea `rumble embed -> code=${resp.code} len=...` (diagnóstico definitivo: 403 = challenge, 200 + len~165K = página real).
2. Extracción movida a `extractRumbleLinks(clean, callback)` reutilizable (HLS + mp4s).
3. Si OkHttp no trae playlist -> **fallback WebView**: `WebView(appCtx)` en `Dispatchers.Main` con JS activado, `addJavascriptInterface` + sondeo de `outerHTML` cada 2s (hasta 10) buscando `hls-vod`; `withTimeout(26s)` con devolución del último HTML capturado.
4. `DonghualifePlugin.load` ahora guarda `DonghualifeProvider.pluginContext = context` (igual que Tvenvivo).
5. Imports nuevos: `android.webkit.WebView/WebViewClient`, `Dispatchers/withContext/withTimeout/CompletableDeferred/TimeoutCancellationException`.

### 🐛 Rumble irresoluble por red + Odysee vía WebView (05 Oct 2026 v10)
**Log del dispositivo (v9)**:
```
rumble embed -> code=403 len=5720 https://rumble.com/embed/v78yvtc/
WebView HTTP 403 -> https://rumble.com/embed/v78yvtc/   (x2)
WebView HTTP 401 -> https://challenges.cloudflare.com/.../pat/.../
WebView timeout, devolviendo último HTML (len=28660)
```

**Veredicto Rumble (PC)**:
- El master `rumble.com/hls-vod/.../playlist.m3u8` con `requests` -> **403** (5799 B). Cloudflare desafía TODO `rumble.com` para fingerprints bloqueados: aunque se descubriera la URL por otro lado, **ExoPlayer también recibiría 403**. Evidencia PC (misma IP): curl -> 200 en todo, `requests` -> 403 en todo = 100% fingerprint, no IP.
- Proxy `r.jina.ai` -> devuelve el challenge (403). Muerto.
- `Odysseusa` del jar es un mirror de Vidara, NO Odysee. **CS3 no trae extractor Odysee.**
- **Rumble queda como está**: el código es correcto y funciona donde CF no desafía. En esta red es imposible por código.

**Cambios Rumble (v10)**:
- Si `resp.code == 403 || == 401` -> **fail rápido**: se omite WebView y `loadExtractor` (probado fútil: Challenge-Platform 401 + ~8s quemados). Log: `rumble bloqueado por Cloudflare (403): sin WebView ni loadExtractor`. El episodio pasa de 28-39s a ~2s en este caso.
- Timeout del WebView 26s -> 15s (el auto-solve de CF ocurre pronto o nunca).

**`emitOdysee` nuevo** (el dominio `odysee.com` NO tiene challenge: todo dio 200 desde PC):
1. **Vía rápida**: canónica `https://odysee.com/@canal:cid/stream:sid` (decodificada del path `/$/embed/`), JSON-LD `contentUrl` (`player.odycdn.com/api/v3/streams/free/...mp4`), probe con `Range: bytes=0-0`. Si 200/206 -> se emite directo.
2. **WebView con autoplay**: `interceptMediaViaWebView(url, Regex("(player\.odycdn\.com|\.mp4(\?|$)|\.m3u8)"))` — `shouldInterceptRequest` captura el stream que pide el player (el player consigue sus edge-credentials solo). `&autoplay=true` si falta. Timeout 25s.
- Dispatch `url.contains("odysee.com")` **antes** del `loadExtractor` genérico.
- Logs: `odysee contentUrl -> code=...`, `odysee -> directo OK`, `WebView media capturado: ...`, `odysee -> WebView OK` / `0 URLs`.

**Nota de verificación**: el checker `ghd_check.py` da falsos positivos con `""""` (raw string que empieza/termina en comilla, ej. línea `contentUrl` original). `ghd_bal.py` maneja mejor los strings pero TAMPOCO soporta `""""`. Se reescribió esa línea con `substringAfter/substringBefore` (sin regex) -> balance 0/0/0 real.

### ⛔ Veredicto Rumble: irresoluble por red en dispositivos desafiados (05 Oct 2026, sin cambio de versión)
**El usuario confirmó que falla igual en celular físico y emulador** (misma WiFi). Barrido completo de vías (todas desde PC salvo indicación):
| Vía | Resultado |
|---|---|
| `app.get` / WebView / ExoPlayer (huella móvil) | **403** en `/embed/`, master `hls-vod`, `oembed`, `embedJS` |
| Master `hls-vod/.../playlist.m3u8` con fingerprint bloqueado | **403** (5799 B) -> aunque se descubra la URL, ExoPlayer también fallaría |
| Variantes `.tar?r_file=chunklist.m3u8` y segmentos en `hugh.cdn.rumble.cloud` | **200 sin headers**, TS válido -> el CDN está abierto, solo `rumble.com` desafía |
| Proxy `r.jina.ai` | 403 (devuelve el challenge) |
| Google Translate (`rumble-com.translate.goog/embed/...`) | **200 HTML con playlist** (Google sí pasa CF en páginas) PERO el master por el proxy -> **403** (Google también recibe challenge en `.m3u8`) |
| CORS proxies (allorigins raw/get, corsproxy.io, codetabs) | 522 / 408 / 403 "domain blocked" / 522 (sus servidores tampoco pasan CF) |
| Cobalt API nueva (`POST api.cobalt.tools/`) | `error.api.auth.jwt.missing` (requiere API key); v7 apagada desde nov-2024 |
| `Odysseusa` del jar | Mirror de Vidara, NO Odysee |
- **Conclusión**: con la red del usuario (CF desafía todo `rumble.com` a huellas móviles), Rumble no se puede resolver ni reproducir por código. El fail-rápido de v10 (`bloqueado por Cloudflare (403)`, episodio en ~2s en vez de ~35s) es el comportamiento correcto.
- **Workaround real**: otra red (datos vs WiFi) o VPN con IP limpia — si CF no desafía, el código v8+ funciona directo (`rumble -> HLS OK`).
- **Último recurso (no implementado)**: dependencia `cronet-embedded` (TLS real de Chromium en la app). Pesado (~MBs nativos, empaquetado cs3 incierto) y resultado incierto. Solo con visto bueno del usuario.

### Estado v10
- `build.gradle.kts`: `version = 10`; `plugins.json`: version 10, `fileSize` **pendiente**.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar `blades-guardians-season-2-1`. Esperado: rumble falla rápido con `bloqueado por Cloudflare (403)` y **odysee emite link vía WebView** (`WebView media capturado` -> `odysee -> WebView OK`).
- ⏸️ Si el autoplay no dispara el stream en el WebView, el siguiente paso es click JS al botón play tras `onPageFinished`.

---

## CinehdplusProvider — Migración a la nueva estructura (Tailwind) (06 Oct 2026 v3)

### Estructura nueva (verificada)
El sitio se rediseñó por completo (Tailwind/daisyUI). Nada del markup viejo existe (`card__cover`, `details__title`, `episodios-todos`, `li.clili`, `data-tplayernv`, `#OptYt` → 0 matches).
| Antes | Ahora |
|---|---|
| Cards `div.card__cover` | `<a class="group ..." href="/series-tv-{id}/{slug}/">` y `/pelicula-{id}/{slug}/` (img + `<p>` título) |
| Detalle serie/peli | JSON-LD `TVSeries`/`Movie` (`name`, `datePublished`, `description`, `image`, `genre[]`) + `og:title/description/image` |
| Episodios DOM (`div.episodios-todos`) | 1 link por episodio: `/episodio-{id}/{slug}-{S}x{E}/` dentro de `div#season-content-{N}` (857/857 server-rendered, sin AJAX) |
| Servidores `li.clili` + `div#id iframe` | `<button data-url="//api.cinehdplus.org/ir/player.php?h=..." data-domain="voe" data-lang="Español Latino">` (youtube con `data-lang="Oficial"` se salta) |
| Trailer `#OptYt iframe` | `button[data-url*=youtube.com]` → `watch?v=` |

### Lo que NO cambió
- **Cadena `ir/` intacta** (verificada end-to-end con hash real): `goto.php?h=` → form `input#url` → POST `rd.php` → `input#url` → POST `redir_ddh.php` (`url`, `dl=0`) → form `action` + `#vid`/`#hash` → POST → `link = 'b64'` → `voe.sx/...`.
- Paginación `/series/page/N`, `/peliculas/page/N` (page/1 redirige a canónica pero trae contenido).
- `/?s=` redirige a `/search/{q}/` con el mismo markup de tarjetas.
- Episodios: langs `Español Latino`/`Oficial`, domains voe/cdnwish/streamtape/hqq/upstream.

### Rewrite (`CinehdplusProvider.kt`)
- `getMainPage`: tarjetas `a[href*=/series-tv-], a[href*=/pelicula-]` (título = `img[alt]` o primer `p`, poster `img[src]`); `hasNext` por link `/page/{N+1}/`.
- `search()`: mismo parser sobre `/search/{q}/`.
- `load()`: JSON-LD primero (`name`, `datePublished`, `description`, `image`, `genre`), fallback a `og:*`; `/episodio-` → `MovieLoadResponse` con `movieData = url` (usa `TVEpisode`: serie, `episodeNumber`, regex `-{S}x{E}`); `/pelicula-` → película; resto → serie con episodios de `div[id^=season-content-]` (temporada del id, `{S}x{E}` del slug, nombre de `h3`/`img[alt]`, URLs absolutas por `fixUrl`).
- Recomendaciones: links de detalle excluyendo self (capped 12). Trailer → `addTrailer`.
- `loadLinks()`: `button[data-url]` (salta youtube, `//` → `https:`, `.m3u8` directo se emite, resto `player.php?h=` → `resolveIrChain()` con la cadena `ir/` original). Retorna `found` (antes `true` siempre).
- `loadSourceNameExtractor` y `fixHostsLinks` sin cambios. `waaw` (StreamSB) sin cambios.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 20000).
- Compilación OK: pendiente de compilar por el usuario (`.\gradlew.bat :CinehdplusProvider:make --console=plain -q`) — **no compilado por regla del repo**.
- ⏸️ **Pendiente**: compilar, instalar y probar home/search/detalle serie+película/episodios/servidores (voe, streamtape, etc.).

### 🐛 Fix search (tarjetas overlay) + logging (06 Oct 2026 v3)
**Síntoma**: el search no devolvía nada (ej. `one punch` → `/search/one+punch/` con 2 resultados reales en el HTML).
**Causa raíz**: la página de búsqueda usa tarjetas DISTINTAS a los listados: `<a href="..." class="absolute inset-0" aria-label="One Punch Man"></a>` **vacío** (overlay), con el `<img>` fuera en el contenedor `div.group`. `toCard()` exigía `img` o `p` **dentro** del `<a>` → retornaba null para todo → 0 resultados.
**Fixes**:
- `toCard()`: fallbacks `closest("div.group")?.selectFirst("img")`, `a[aria-label]`, `h2/h3` del contenedor. Sirve para ambos markups (listados + búsqueda + recomendaciones).
- `search()`: `URLEncoder.encode(query, "UTF-8")` (espacio → `+`, igual que el `/search/one+punch/` del sitio).
- **Logs** con `TAG = Cinehdplus` (`adb logcat -s Cinehdplus:V`): `getMainPage` (sección, items, hasNext, ms), `search` (query, resultados, ms), `load` (episodio/película/serie con temporadas+episodios, ms), `loadLinks` (nº botones + `domain:lang`, OK/FALLO por servidor, final), `resolveIrChain` (paso exacto que falla: goto/rd/redir/action/vid/link).
- Sin cambio de versión (v3 aún no compilada/publicada).

---

## SeriesdonghuaProvider — Plugin nuevo (06 Oct 2026 v1)

Sitio: `seriesdonghua.com` (PHP custom, server-rendered, **sin Cloudflare**: todo 200 con UA plano).

### Estructura verificada
| Parte | Ruta / markup |
|---|---|
| Home | `/` (En emisión, Nuevos Episodios) |
| Listados | `/donghuas-en-emision`, `/donghuas-finalizados`, `/episodios`, `/genero/` + `?page=N` |
| Search | `/buscar.php?q=` (mismo markup de cards) |
| Serie | `/{slug}/` (h1 título, og:description/image, `div.genre-pill-list a.genre-pill`, 263/263 eps server-rendered) |
| Episodio | `/{slug}-episodio-{n}/` (`article.episode-card-item[data-ep]`, href, img alt/poster) |
| Secuelas = entradas separadas | (`doupo-cangqiong-7`, `i-will-eternal-4`) — sin multi-temporada por página |
| Servidores | `button.server-tab-btn` (nombre en `span` sin clase, `data-video-id`, `data-server-index`, badges Sub ES/1080p) |
| Subs | No hay softsubs (embeds con Sub ES quemado) |

### Player API (sin auth, verificada)
`POST /api/player/get-server` con `Content-Type: application/json` + `X-CSRF-TOKEN` (de `meta[name=csrf-token]`) + `X-Requested-With: XMLHttpRequest` + Referer/Origin, body `{"video_id":15512,"server_index":0}` → `{"success":true,"embed_url":"https://..."}`. En NiceHttp el JSON va en `requestBody` (nunca `data = String`). Fuentes vistas: dailymotion `geo...player...?video=`, ok.ru `videoembed`, rumble `embed/...?pub=`.

### Implementación (v1, sin compilar por regla del repo)
- `getMainPage`: home (2 secciones pedidas) + 5 géneros en paralelo (`En emisión`, `Finalizados`, `Acción`, `Aventura`, `Cultivo`, `Fantasía`, `Romance` — todos verificados 200 con 24 cards); `hasNext` por `a[href$="page=N+1"]`.
- `search()`: `/buscar.php?q=` + `URLEncoder`.
- `load()`: serie → `newTvSeriesLoadResponse(TvType.Anime, season=1)` (rama TvSeries determinista, lección Uniquestream); episodio → `MovieLoadResponse` mínimo con `movieData=url`; URLs **relativas** → siempre `fixUrl()` (lección inversa a DonghuaLife, que eran absolutas).
- `loadLinks()`: botones → `postPlayerServer()` → dispatch ok.ru/dailymotion/rumble/`.m3u8`/`loadExtractor` (mismo código probado de Donghualife, con `CancellationException` re-lanzada).
- `RumbleExtractor.kt` incluido y registrado (copia mejorada de Donghualife) como red de seguridad.
- `getVideoInterceptor`: solo rama ok.ru (`__sub` no aplica: sin subtítulos en el sitio).
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `SeriesdonghuaPlugin.kt` (+`pluginContext`), `SeriesdonghuaProvider.kt`, `RumbleExtractor.kt`; `plugins.json` → entrada `SeriesDonghua` v1 (68 entradas, `fileSize: 0` pendiente).
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar home/search/load/links. Logs: `adb logcat -s SeriesDonghua:V` y `RumbleExt:V`.

---

## VeranimeProvider — Plugin nuevo (08 Oct 2026 v1)

Sitio: `veranime.ninja` (WordPress + tema DooPlay, **sin Cloudflare** pero servidor MUY lento: detalle 19-40s por página).

### Cadena verificada (end-to-end en PC)
Episodio `/ver/{slug}-episodio-{n}/` → `li.dooplay_player_option` (`data-post`, `data-nume`, `data-type`) → `POST wp-admin/admin-ajax.php` (`action=doo_player_ajax`, form-data, **sin nonce**) → `{"embed_url":"https://saidochesto.top/embed.php?id=..."}` → hub con `go_to_player('URL')` por idioma (`OD_SUB`/`OD_LAT`/`OD_ES`: FileLions, StreamWish, StreamTape, LuluStream, HexLoad, FileMoon, Mp4Upload, Uqload).

### Playback verificado (mirror por mirror)
| Mirror | Estado |
|---|---|
| StreamWish / Uqload | 200 con m3u8+packer → extractor CS3 OK |
| StreamTape | 200 player real → extractor CS3 OK |
| LuluStream / Mp4Upload | Muertos ("deleted/expired") → se saltan solos |

### Implementación (v1, sin compilar por regla del repo)
- `getMainPage`: `article.item` (título `div.data h3 a`, poster `img[data-src]` — lazy, `src` es placeholder SVG); secciones Catálogo + 6 géneros (`/genero/accion/page/2/` verificado); `hasNext` por `a[href$="page=N+1"]`.
- `search()`: `/?s=` + `URLEncoder` + filtro `matchesQuery()` (WP es laxo).
- `load()`: serie → temporadas reales `#seasons .se-c` (`.se-t` + `ul.episodios li`, `div.numerando` "T - E", stills TMDB) con `TvSeriesLoadResponse(TvType.Anime)`; episodio `/ver/...-episodio-N/` → `MovieLoadResponse` mínimo con `movieData=url`; poster/info vía `div.poster`, `#info .wp-content`, `nav.genres`, `div.custom_fields`.
- `loadLinks()`: AJAX → hub → mirrors por idioma (**LAT primero**, repo latino) → `fixMirrorHost()` (filemooon→filemoon.sx, uqload.io→.com) → `loadExtractor` con `withTimeout(25s)` por mirror.
- **Timeouts largos**: detalle/episodio `120L`, listados `60L` (en NiceHttp el timeout va en SEGUNDOS; con 30L las páginas de 40s morirían).
- Sin WebView/subs/interceptor (embeds con subs quemados; extractores CS3 manejan sus headers).
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `VeranimePlugin.kt`, `VeranimeProvider.kt`; `plugins.json` → entrada `VerAnime` v1 (69 entradas, `fileSize: 0` pendiente).
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :VeranimeProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar home/search/load/links. Logs: `adb logcat -s VerAnime:V`.

### 🐛 Fix search result-item + hubs alternos (08 Oct 2026 v2)
**Log del dispositivo (v1)**: search `one punch` → `0 total`; `loadLinks` EP5 → `opciones=1` y `SIN LINKS` sin ningún log intermedio (ni embed ni hub).

**Causa search**: la página de resultados usa `div.result-item > article` (`div.title a` + `.image img`), NO `article.item` (ese es solo el widget lateral "ANIMES RECIENTES"). Mi conteo con regex `class="[^"]*item` me engañó (matcheaba `w_item_a`).
**Fix**: nuevo `toSearchCard()` (`div.title a`, poster `.image img`); `search()` usa `div.result-item` y solo cae a `article.item` si no hay ninguno (para no devolver widgets). Log si hay result-item sin parsear.

**Causa loadLinks**: el EP5 resuelve a OTRO hub (`darkanimes.com/...`, no saidochesto). Su página no tiene bloques OD ni `go_to_player` → los 3 `?: continue` saltaban en silencio → 0 links.
**Hallazgo**: darkanimes trae JSON limpio `{"cyberlocker","link","language","quality"}` (14 mirrors: streamtape/netu/filemooon/uqload/filelions/streamwish/savefiles/mxdrop/hexload/mp4upload; `Japones`=SUB, `Español`=LAT).
**Fixes**:
- Branch por tipo de hub: OD_* → `emitSaidochesto()` (extraído tal cual); si no → `emitCyberlockerJson()` (orden Español-latino primero); si no hay items → fallbacks genéricos (`.m3u8` directos, `iframe[src]` → loadExtractor).
- `fixMirrorHost()` += `uqload.cx/is → uqload.com`.
- Cobertura CS3 verificada por bytecode: filelions→VidHidePro1-3, mxdrop→MxDropTo, hexload→Hexload, mp4upload→Mp4Upload, luluvdo→LuluStream. Sin cobertura: savefiles, netuplayer (se saltan solos).

### Estado v2
- `build.gradle.kts`: `version = 2`; `plugins.json`: version 2, `fileSize` **pendiente** (sigue 0).
- ⏸️ **Pendiente**: compilar, instalar y probar search `one punch` (→ One Punch Man) y EP5 de Kage (→ links ES/LAT via cyberlocker JSON).

### 🔧 Fix zilla-direct + Byse + mapeos (08 Oct 2026 v3)
**Log del dispositivo (v2)**: OPM emite OK (varios mirrors CS3 funcionan en silencio), pero Link Click EP1 solo trae 5 mirrors SUB y fallan 3: `zilla-networks` (`player.zilla-networks.com/m3u8/{hash}`), `uns` (animeav1), `mega`. Además `filemooon`/`uqload` dan 0 links en varios episodios. El usuario apunta que el Latino con "byse" (igual que Monoschinos) debería funcionar.

**Investigación (PC + bytecode)**:
- `player.zilla-networks.com/m3u8/{hash}` → **200 `application/x-mpegURL`** (media playlist VOD con segmentos `.html`). Mi check `contains(".m3u8")` lo pasaba por alto (es `/m3u8/`, sin punto). Mismo patrón que Animeav1 (sus segmentos exigen headers completos o dan 403).
- `filemooon.link/e/...` → `<title>Byse Frontend</title>`: **FileMoon migró al stack Byse** (challenge ECDSA + PoW + AES-GCM) que los extractores CS3 no resuelven. De ahí los `sin links [filemooon]`.
- `uqload.vc` es XUpload clásico (**NO** Byse); CS3 cubre uqload.com/.co/.bz/.cx/.xyz pero no `.vc`/`.io`/`.is`.
- `dood.sh` → 301 a `playmogo.com` (con extractor `Playmogo`); sin mapeo, `loadExtractor` no matchea host y da 0 sin intentarlo.
- `MEGA` (`mega.nz/embed/...`) requiere el flujo proxy+AES de Retrotve: pesado, aparcado (otros mirrors cubren).
- `uns` (`animeav1.uns.bio/#codigo`): host propio de AnimeAv1, aparcado.

**Fixes (v3)**:
- `emitCyberlockerJson`: `isPlaylist = contains(".m3u8") || contains("/m3u8/")`; links zilla con `zillaHeaders` (port de `Animeav1Provider.kt:63`, log `hub playlist directa`).
- `getVideoInterceptor` nuevo: inyecta `zillaHeaders` a todo `zilla-networks.com` (master + segmentos).
- `ByseExtractor.kt` portado de Monoschinos (retagueado a `VerAnime`, autocontenido: Jackson + OkHttp propio + JCA).
- `emitByse()` nuevo: fetch del embed, si trae "Byse Frontend" → `ByseHttpExtractor().extract(url, hubUrl, hubHost)` → emite sources + subtítulos; si no, `false` (cae a `loadExtractor`).
- Ruta filemoon/byse en `emitCyberlockerJson`: Byse primero, `loadExtractor` después.
- `fixMirrorHost()` += `filemoon0.top→filemoon.sx`, `dood.sh→playmogo.com`, `uqload.(io|is|vc|com)→uqload.cx`.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 0).
- ⏸️ **Pendiente**: compilar, instalar y probar (1) Link Click EP1 → `hub playlist directa [zilla-networks]` + zilla reproduce; (2) episodio con filemoon → `byse sources=N`; (3) `adb logcat -s VerAnime:V`.

### 🔧 Fix compilación: `takeIf { it.isNotEmpty() }` en receivers nulables (08 Oct 2026)
**Error**: `Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'` (línea 286).
**Causa**: en cadenas como `selectFirst(...)?.text()?.trim().takeIf { it.isNotEmpty() }`, el `it` dentro de `takeIf` es `String?` (el `?.` propaga nulabilidad) e `isNotEmpty()/isNotBlank()` exigen receptor no-nulo.
**Fix global** (`replaceAll`): `takeIf { it.isNotEmpty() }` → `takeIf { !it.isNullOrEmpty() }`, `takeIf { it.isNotBlank() }` → `takeIf { !it.isNullOrBlank() }` (semántica idéntica, `isNullOr*` acepta `CharSequence?`).
**Regla para futuros providers**: después de cualquier `?.`, dentro de `takeIf`/`let` usar siempre `isNullOrEmpty()/isNullOrBlank()`, nunca `isNotEmpty()/isNotBlank()` directo.

### 🐛 Fix get-server sin embed + filtro de search (08 Oct 2026 v2)
**Log del dispositivo (v1)**: home/search/load OK (7 secciones × 24, `search 'blades' -> 24`, series con episodios), pero `player/get-server v=14922 s=0..3 -> sin embed_url` en los 4 servidores (DM/OK.ru/Rumble/VOE) → `SIN LINKS`. Además el search devolvía 24 resultados irrelevantes.

**Investigación (PC)**:
- El MISMO video (14922) por API responde `200 {"success":true,"embed_url":...}` en los 4 servidores (incluido VOE `voe.sx/e/...`). La API está bien; el dispositivo recibe otra cosa.
- El log v1 no mostraba QUÉ recibía (solo "sin embed_url") → imposible distinguir 419/500 HTML de `success:false` JSON.
- Search `/buscar.php?q=blades` devuelve 24 cards, casi todas irrelevantes (`Xi Xing Ji`, `Gu An`...), pero `Blade of The Guardians 2` SÍ está (#19). El buscador del sitio es laxo por diseño.

**Fixes**:
- `postPlayerServer()`: captura `resp.code` + body; si no hay `embed_url` loguea `code=... sin embed_url: <200 chars del body>` (distingue HTML de error de JSON). Añadido header `Accept: application/json`.
- `search()`: filtro de relevancia `matchesQuery()` — TODOS los tokens del query (y su singular si termina en `s`) deben aparecer en el título. `blades` → `blade` matchea "Blade of The Guardians 2", descarta las 23 restantes. Log: `N total, M filtrados`.

### Estado v2
- `build.gradle.kts`: `version = 2`; `plugins.json`: version 2, `fileSize` **pendiente** (sigue 0).
- ⏸️ **Pendiente**: compilar, instalar y (1) ver el log `player/get-server ... code=... sin embed_url: ...` para diagnosticar la respuesta real del dispositivo; (2) probar search `blades` → debe devolver 1 resultado.

### 🐛 Fix 419 CSRF token mismatch (08 Oct 2026 v3)
**Log del dispositivo (v2)** — el diagnóstico funcionó a la primera:
```
player/get-server v=14922 s=0 -> code=419 sin embed_url: {"message": "CSRF token mismatch.",
"exception": "Symfony\Component\HttpKernel\Exception\HttpException", ...}
```
- El token `X-CSRF-TOKEN` SÍ se envía (viene del meta fresco), pero Laravel lo ata a la **sesión**: sin la cookie de sesión del GET, crea sesión nueva en el POST → token inválido → 419. Desde PC funciona porque `requests.Session` persiste cookies; el jar de CS3 (`NiceResponse` expone `code` pero no valida nada, y su jar no siempre persiste el `CustomCookieJar` entre `app.get`/`app.post`).
- **Fix**: round-trip manual de cookies (precedente: `bypass()` de Netmirror parsea `Set-Cookie` a mano). `loadLinks` guarda `pageResp.cookies` (`Map<String,String>` de NiceHttp) como header `Cookie: k=v; ...` y lo pasa a `postPlayerServer()`. Si el jar ya enviaba sus cookies, los valores son idénticos (mismo GET) → duplicado inofensivo. Log nuevo: `loadLinks cookies=[nombres] code=...` (solo nombres, sin valores).
- **Nota search**: el log prueba que SeriesDonghua devuelve exactamente 1 resultado para `blade` y `blades`. Si la UI muestra 3, los otros 2 vienen de OTROS providers instalados (CS3 busca en todos a la vez: DonghuaLife, etc.). Verificar por etiqueta de provider en cada card.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 0).
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), instalar y probar el episodio de Blade — esperado: `player/get-server v=14922 s=0 -> ok` + links DM/OK.ru/Rumble/VOE.

### 🐛 Fix DM sin qualities + mirrors muertos + cap loadExtractor (08 Oct 2026 v4)
**Log del dispositivo (v3)**: EP1 perfecto (DM OK + subs, ok.ru HLS, cookies OK, search 3/3). EP8 (`blades-of-the-guardians-8`): DM `sin qualities` para `k2dpzipls098HizodoS` (pero ok.ru HLS salva el episodio → OK global).

**Investigación (PC)**:
- El metadata de `k2dpzipls098HizodoS` responde **200 CON `qualities.auto`** ("BOTG 008"). El dispositivo recibió JSON sin qualities → varianza por IP/throttle de DM, no video muerto.
- **VOE muerto**: `voe.sx/e/atrym6yf0gvl` → **404 real** del sitio. Correcto ignorarlo (en la web se reproduce por DM/ok.ru).
- **Embedwish muerto**: `embedwish.com/e/...` → 200 de 426 B con "File is no longer available as it expired or has been deleted". También correcto ignorarlo. Pero su `loadExtractor` **colgó ~17s** antes de fallar.
- El usuario confirma que en la web SÍ reproduce Dailymotion (coherente: el video está vivo).

**Fixes**:
- `emitDailymotion`: extraído `fetchDmMeta(id, referer, noHeaders)` (fetch + parse + log); **hasta 2 intentos** cuando falta `qualities` (el 2º sin headers). Log con `keys=` + `err=` del JSON para diagnosticar la varianza.
- `loadExtractorCollect`: `withTimeout(25_000L)` — un mirror muerto ya no atasca el episodio (solo el fallback genérico; los extractores propios no se tocan).
- Sin cambio en Rumble/VOE/Embedwish (comportamiento correcto actual: fail rápido / 0 links).

### Estado v4
- `build.gradle.kts`: `version = 4`; `plugins.json`: version 4, `fileSize` **pendiente** (sigue 0).
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), instalar y probar EP8 de Blades — esperado: `dailymotion ... -> OK` (al 1º o 2º intento) + `ok.ru HLS`; Embedwish/VOE siguen en 0 links (muertos) pero sin colgar.

### 🐛 Fix search: el parámetro correcto es `?s=`, no `?q=` (08 Oct 2026 v3, reportado por el usuario)
**El usuario indicó**: en `https://seriesdonghua.com/buscar.php?s=blade` salen 3 resultados (`Blade of The Guardians 2`, `Blades of the Guardians`, `Blade of Vengers`).
**Verificado**: el `<input>` del form es `name="s"`. Con `?s=blade` → 3 relevantes; con `?q=blade` el sitio **ignora** el parámetro y devuelve 24 sin filtrar (de ahí venía el ruido que el filtro `matchesQuery()` tenía que limpiar).
**Fix**: `buscar.php?q=` → `buscar.php?s=` en `search()`. Se mantiene `matchesQuery()` como red de seguridad. Sin cambio de versión (v3 aún no compilada).

### Estado v9
- `build.gradle.kts`: `version = 9`; `plugins.json`: version 9, `fileSize` **pendiente**.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar `blades-guardians-season-2-1` (fuentes Rumble+odysee). Buscar `rumble embed -> code=` (confirma 403) y `rumble -> HLS OK` vía WebView.
- ⏸️ Odysee sigue sin extractor (será `emitOdysee` con WebView+autoplay en v10 si hace falta).

### Estado v8
- `build.gradle.kts`: `version = 8`; `plugins.json`: version 8, `fileSize` **pendiente**.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar un episodio con rumble (ej. `record-mortals-journey-immortality-season-6 EP5`). Buscar en logcat `rumble -> HLS OK`.
- ⏸️ Si rumble da 403 en el dispositivo (Cloudflare tambien bloquea a OkHttp movil), la alternativa es WebView (`WebViewResolver`) — avisar.

### Estado v7
- `build.gradle.kts`: `version = 7`; `plugins.json`: version 7, `fileSize` **pendiente**.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar. Para diagnosticar: `adb logcat -s DonghuaLife:V` y buscar `dailymotion` — los 4 mensajes de log distinguishes exactamente el fallo (red / no-JSON / sin qualities / 0 URLs).
- ⏸️ Pendiente de verificar en dispositivo: rumble / odysee / vk (siguen yendo por `loadExtractor`; si alguno falla tambien, se Vera en el log `sin links para <label>`).

### Estado v6
- `build.gradle.kts`: `version = 6`; `plugins.json`: version 6, `fileSize` **pendiente** tras compilar.
- ⏸️ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y comprobar que `loadLinks data='https://donghualife.com/watch/...'` (con `/watch/` y **sin** `watch:`) trae `fuentes=N` y emite enlaces.
