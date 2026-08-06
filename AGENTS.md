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
