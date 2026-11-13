# AGENTS.md - Netmirror Plugin Development

## LacartoonsProvider â€” sincronizar versiÃ³n del catÃ¡logo (5 Nov 2026 v21)
- La web pÃºblica muestra la ficha de El Chavo del 8 y sus episodios (incluido el capÃ­tulo 1 de temporada 1); el catÃ¡logo del repo estaba desincronizado: `LacartoonsProvider/build.gradle.kts` declaraba versiÃ³n 21, mientras `plugins.json` ofrecÃ­a versiÃ³n 1. El usuario podÃ­a seguir recibiendo el plugin viejo y ver â€œenlaces no encontradosâ€ aunque el cÃ³digo reciente estuviera en el repo.
- **Fix**: sincronizar `plugins.json` a versiÃ³n 21. El `fileSize` sigue siendo 15000 y debe actualizarse al publicar el `.cs3` generado.
- **CompilaciÃ³n**: no ejecutada por regla del repo. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Pendiente en dispositivo**: actualizar/reinstalar Lacartoons v21 y probar un episodio de El Chavo del Ocho. Si aÃºn da â€œenlaces no encontradosâ€, compartir logs desde `loadLinks called` hasta `loadLinks returning`, porque no hay logs de esa ejecuciÃ³n en el reporte actual para atribuir un fallo restante al selector de iframe o al extractor.

## LacartoonsProvider â€” contar enlaces emitidos y diagnosticar Cubeembed (5 Nov 2026 v22)
- **Log del usuario, El Chavo del 8 T4E4**: iframe `https://cubeembed.rpmvid.com/#hsd9i`; `loadExtractor` devuelve `true`, pero CS3 muestra â€œenlaces no encontradosâ€. `loadExtractor` indica que reconociÃ³ el extractor, no que su callback entregÃ³ links; el proveedor confundÃ­a ambos estados.
- **Fix**: envolver el callback de `loadExtractor`, contar solo enlaces realmente emitidos y devolver `true` Ãºnicamente si el contador es mayor que cero. Cubeembed registra ahora HTTP/content-type/tamaÃ±o, presencia de cada campo de stream y si emitiÃ³ links; se deja de imprimir el payload descifrado completo.
- Versiones a **22** en `LacartoonsProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente de actualizar al publicar el artefacto. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Pendiente en dispositivo**: instalar v22 y volver a probar ese capÃ­tulo. Revisar logs `Cubeembed` y `emitted=`. Si la API descifra pero ambos campos salen falsos, el formato del endpoint cambiÃ³ y esos logs permitirÃ¡n ajustar el parser con evidencia.

## LacartoonsProvider â€” fallback TurboEmbed para Cubeembed (5 Nov 2026 v23)
- **Evidencia del usuario**: `cubeembed.rpmvid.com/#hsd9i` termina en `https://turboembed.com/alb2`; el player usa JWPlayer y el video DOM aparece como `blob:`. El bundle actual consulta `/api/stream?s=alb2` (metadata solamente) y luego obtiene la config de reproducciÃ³n por `/api/view` cifrado. El endpoint heredado `/api/v1/video?id=...` no corresponde al flujo actual.
- **Fix**: Cubeembed abre el iframe original en WebView, pulsa el overlay del reproductor y consulta `jwplayer('media-player').getPlaylistItem().file` hasta hallar la URL HTTP fuente; si no la obtiene, conserva el extractor API legado como fallback. El contexto del plugin se guarda al inicializarse. `loadLinks` ahora cuenta links entregados al callback, no el booleano de reconocimiento del extractor.
- Versiones a **23** en `LacartoonsProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente de actualizar al publicar. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Pendiente en dispositivo**: instalar v23 y probar el episodio `https://www.lacartoons.com/serie/capitulo/30004?t=4`. Buscar `TurboEmbed loaded`, `TurboEmbed JW source encontrado`, `emitted=` y `loadLinks returning`. Si falla, esos logs mostrarÃ¡n si la navegaciÃ³n llegÃ³ a `/alb2` y si JWPlayer publicÃ³ su playlist.

## LacartoonsProvider â€” diagnÃ³stico genÃ©rico por iframe/extractor (5 Nov 2026 v24)
- **Pedido**: disponer de logs reutilizables para diagnosticar futuros fallos con cualquier servidor/extractor.
- **Cambios**: registrar estado/tamaÃ±o de la pÃ¡gina; URL iframe reducida a host+path (sin query ni fragmento); por iframe, host, si hubo extractor, nÃºmero de callbacks, duraciÃ³n y advertencias distintas para â€œsin extractorâ€ y â€œextractor sin linksâ€. Las excepciones incluyen tipo y mensaje. Cubeembed registra etapas de WebView y API sin imprimir URL de stream firmadas.
- Versiones a **24** en `LacartoonsProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Pendiente en dispositivo**: instalar v24 y probar un episodio de El Chavo; revisar `extractor result`, `no extractor matched`, `extractor matched but emitted no links` y, para Cubeembed, `TurboEmbed page loaded`/`JW source found`.

## LacartoonsProvider â€” registrar TurboEmbed directo (5 Nov 2026 v25)
- **Dato nuevo del usuario**: otro video usa la ruta directa `https://turboembed.com/3t08`, mismo player que el iframe de Cubeembed que redirige a `/alb2`.
- **Causa de cobertura**: `loadExtractor` solo tenÃ­a registrado `cubeembed.rpmvid.com`; si la pÃ¡gina de episodio entrega directamente un enlace `turboembed.com`, ningÃºn extractor lo reconocÃ­a y no llegaba al fallback WebView.
- **Fix**: registrar `Turboembed` como segunda entrada del mismo extractor compartido, con `mainUrl=https://turboembed.com`. El resolver usa la URL recibida completa y no tiene IDs/rutas hardcodeadas, por lo que sirve para `/3t08`, `/alb2` y otras rutas del player.
- Versiones a **25** en `LacartoonsProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Logs posteriores del usuario**: Cubeembed `#hsd9i` y `#qlwif` emiten 1 link cada uno; Liga de la Justicia T1E1 `#w5cm8` lo reconoce (`loadExtractor matched=true`) pero emite 0. Esto localiza el fallo dentro del recorrido Cubeembed/TurboEmbed de ese video, no en el parseo de iframes de LaCartoons. No hay log de `Cubeembed` en el paste para saber si la redirecciÃ³n llegÃ³ al player o si JWPlayer no expuso la playlist.
- El resolver es dinÃ¡mico y no depende de esos IDs; rutas como `/alb2`, `/3t08` o `/4d07` se abren con la misma lÃ³gica. El caso `#w5cm8` demuestra que una ruta individual puede no entregar una fuente aunque otras sÃ­ funcionen.
- **Pendiente**: reproducir Liga de la Justicia con v25 y compartir logs `Cubeembed` + `LACartoonsProvider` sin filtrar tags. Confirmar tanto `TurboEmbed page loaded` y `TurboEmbed JW source found` como `extractor result`/`emitted`.

## TvguatemalaProvider â€” NormalizaciÃ³n de tÃ­tulos (31 Oct 2026 v2)
- El detalle mostraba tÃ­tulos SEO largos, como â€œCanal 3 Guatemala En Vivo Gratis 24/7 | Ver TV Online | TV Guatemala En Vivoâ€; el tÃ­tulo SEO de El Chavo tambiÃ©n incluÃ­a promociÃ³n y marca del sitio.
- AÃ±adida `cleanChannelTitle()` para cortar el texto tras `|`, quitar sufijos promocionales y eliminar â€œTV Guatemalaâ€. Se aplica en home, bÃºsqueda, detalle y nombre del enlace reproducido.
- Actualizados `TvguatemalaProvider/build.gradle.kts` y la entrada de `plugins.json` de v1 a v2; `fileSize` queda pendiente hasta generar el artefacto.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :TvguatemalaProvider:make --console=plain -q`.
- Pendiente probar en dispositivo Canal 3 (â€œCanal 3 Guatemalaâ€) y El Chavo (â€œEl Chavo del 8â€) en bÃºsqueda, detalle y reproductor.

## HDFullProvider â€” RevisiÃ³n y primer arreglo (31 Oct 2026)
- **Arquitectura**: el mÃ³dulo aÃ±adido implementa bÃºsqueda/detalle por DOM, temporadas por POST a `/a/episodes` y `loadLinks` decodifica `var ad` (Base64 + ofuscaciÃ³n) en servidor/cÃ³digo/idioma/calidad. El plugin registra HDFull y solo VidMoly/VidMolyCom.
- **HTML real Doctor Who T1E8**: cuatro entradas: Powvideo/Powwideo Ã—2 y Streamplay Ã—2 (subtitulado ES y audio original); audio ES/latino muestra cero opciones.
- **Causa probable de cero enlaces**: `loadSourceNameExtractor` lanzaba una coroutine IO independiente por link; `loadLinks` podÃ­a finalizar antes de ejecutar `callback`. Corregido recolectando primero el resultado de `loadExtractor` y luego invocando callback de forma secuencial. Se agrega log por proveedor/idioma con cantidad de enlaces.
- **Seguridad**: el fuente agregado incluÃ­a credenciales de cuenta y cookies de sesiÃ³n incrustadas. Se retiraron; `ensureLoggedIn` ahora conserva cookie de idioma y actualiza cookies recibidas del sitio sin hacer login automÃ¡tico. Si se requiere autenticaciÃ³n, debe implementarse como sesiÃ³n del usuario, nunca con secretos en el repo. Recomendar cambio de las credenciales expuestas.
- **Extractores faltantes**: no existen Powvideo/Powwideo ni Streamplay localmente; HDFull solo registra VidMoly. El map transforma Powvideo a Powwideo pero el iframe real usa `/embed-...html`, por lo que la URL construida no estÃ¡ verificada. Los hosts externos no fueron accesibles para observar el HTML/JS del player, asÃ­ que no se aÃ±adiÃ³ extractor especulativo.
- **Pendiente**: inspeccionar `provider/code` decodificado y HTML/logs de ambos iframes para implementar extractores, registrar APIs, comprobar si Streamplay funciona con los extractores del app, y confirmar tipo de autenticaciÃ³n.
- **Manifest**: mÃ³dulo ya aparece en `plugins.json` como `HdfullProvider` v1; confirmar que el artefacto de publicaciÃ³n coincide con nombre/version.
- **CompilaciÃ³n**: no ejecutada por instrucciÃ³n del repo. Comando: ` .\gradlew.bat :HDFullProvider:make --console=plain -q`.
- **Dispositivo**: pendiente instalar y probar T1E8, idiomas, streams, headers/referer y subtÃ­tulos.

## Reglas de trabajo (OBLIGATORIAS)
- **Las respuestas siempre en espaÃ±ol**
- **NUNCA ejecutar Gradle por cuenta propia.** Ni `compileReleaseKotlin`, ni `make`, ni `installDebug`, ni tests. Aunque el cÃ³digo "deberÃ­a" compilar y aunque los errores sean obvios: **primero termina todos los cambios del proveedor, y solo compila cuando el usuario lo pida expresamente.** Si crees que hay un error de compilaciÃ³n, descrÃ­belo y sigue editando; no lanc gradle para confirmarlo.
- **NUNCA hacer `git commit`, `git push`, `git tag` ni `git rebase` sin que el usuario lo pida de forma explÃ­cita.** Al terminar un proveedor: enumera los archivos tocados y el comando de compilaciÃ³n exacto que debe correr el usuario, y quÃ© queda pendiente de probar en dispositivo.
- Al cerrar cada proveedor o fix importante, **aÃ±adir su secciÃ³n a este `AGENTS.md`** con: arquitectura verificada, reglas del sitio, causa raÃ­z, fix aplicado, estado de compilaciÃ³n y quÃ© queda pendiente de probar en dispositivo. Es el Ãºnico sitio donde se registra el progreso.


## Goal
- Proveer streams completos desde cncverse para Netflix/PrimeVideo/JioHotstar providers en CloudStream.

## ADCIONAL DECOMPILATION (08 Jul 2026) â€” NEWEST VERSION
El folder `adicional/` contiene una versiÃ³n MÃS NUEVA de cncverseMobile que el folder `aplicaciÃ³n/`.
Diferencias clave:

### bypass() â€” COMPLETAMENTE DECOMPILADO
```kotlin
// Cache: 15 horas (54000000ms)
// Headers: Chrome 147 Windows, Origin/Referer: net22.cc
// POST https://net52.cc/verify.php
// Body: g-recaptcha-response=${UUID.randomUUID()}
// OkHttp followRedirects(false) + followSslRedirects(false)
// Parse Set-Cookie â†’ t_hash_t=
```

### NUEVO: getNewTvUserToken(apiBase, ott) â€” OTP-based auth
Variables: savedToken, otpHeaders, savedTimestamp
Data class: NewTvOtpResponse { otp, status, usertoken, pub_msg, ... }
Cache: nf_cookie_full + nf_cookie_full_timestamp (15h)

### loadLinks â€” FLUJO CAMBIADO (vs version vieja playlist.php)
| VersiÃ³n | Auth | Endpoint | Variables clave |
|---------|------|----------|-----------------|
| VIEJA (`aplicaciÃ³n`) | Solo t_hash_t cookie | playlist.php | playlist, item, source, track |
| NUEVA (`adicional`) | t_hash_t + userToken (OTP) | **mobile/hls** | apiBase, userToken, response |

### Per-Provider Headers (cada provider tiene su propio `headers` field)
| Provider | X-Requested-With | User-Agent |
|----------|-----------------|------------|
| Netflix | `XMLHttpRequest` | Chrome 144 WebView |
| PrimeVideo | `XMLHttpRequest` | Chrome 144 WebView |
| HotStar | `XMLHttpRequest` | Chrome 144 WebView |
| DisneyPlus | `app.netmirror.netmirrornew` | Chrome 144 WebView |

### getVideoInterceptor â€” SIMPLE (solo Cookie: hd=on)
```java
// Solo agrega Cookie: hd=on a .m3u8 requests
// NO rewrite de CDN, NO in= parameter manipulation
```

## NEW FLOW (implemented 08 Jul 2026)
```kotlin
// loadLinks:
// 1. resolveApiUrl() â†’ apiBase
// 2. bypass(mainUrl) â†’ t_hash_t cookie (15h cache)
// 3. getNewTvUserToken(apiBase, ott) â†’ userToken (OTP, 15h cache)
// 4. GET {apiBase}/mobile/hls/{id}.m3u8?q=720p&in={userToken}&hd=on&lang=eng
//    Headers: per-provider headers + Cookie: t_hash_t=...;hd=on;ott=nf
// 5. Parse master M3U8:
//    - Audio: keep as-is (s23 CDN)
//    - Video: rewrite freecdn â†’ s23.nm-cdn9.top, remove in= param
//    - Subtitles: parse URI from EXT-X-MEDIA
// 6. Fallback: player.php
// 7. Fallback: playlist.php
```

## Endpoints
| Endpoint | USAGE | Status |
|---|---|---|
| `net52.cc/newtv/main.php` | Main page | âœ… |
| `net52.cc/newtv/search.php` | BÃºsqueda | âœ… |
| `net52.cc/newtv/post.php` | Detalles | âœ… |
| `net52.cc/newtv/episodes.php` | Episodios | âœ… |
| `net52.cc/mobile/hls/{id}.m3u8` | **Playback master** | âœ… |
| `s23.nm-cdn9.top/files/{id}/{quality}/` | **Full CDN** | âœ… |
| `net52.cc/verify.php` | Bypass (POST g-recaptcha) | âœ… |
| `net52.cc/newtv/otp.php?ott=nf` | **OTP user token** | â¸ï¸ Sin probar |
| `net52.cc/newtv/player.php` | Fallback | âœ… |
| `s21.freecdn4.top/files/...` | Preview (60 JPG) | âŒ Solo preview |

## Implementation (NetflixProvider.kt / PrimevideoProvider.kt)

### `loadLinks` â€” Primary Flow (s23 Cookie auth, no `in=` param)
```kotlin
// 1. Get bypass token (verify.php â†’ t_hash_t cookie)
val cookieRaw = currentBypassToken  // h1::h2::ts::ep::99 (decoded)

// 2. Fetch mobile/hls master to get CDN structure
val inParam = cookieRaw  // used in mobile/hls request, but ignored by s23
val mobileResp = app.get("$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng",
    headers = mobileHeaders)  // Chrome/149 WebView

// 3. Build custom master:
//    - Audio lines (from mobile/hls response) â†’ keep as-is (already on s23, no in=)
//    - Video variants: rewrite CDN freecdn â†’ s23, STRIP in= param
//    - s23 accepts Cookie (t_hash_t + hd=on) without in= parameter
```

### Key Change (07 Jul 2026)
- **REMOVED** dependency on server-rewritten `in=` token (server no longer rewrites)
- **s23.nm-cdn9.top** accepts requests with ONLY Cookie auth (t_hash_t + hd=on)
- Audio already worked without `in=`; video now uses the same approach
- Player.php demoted to fallback (returns preview content, wrong episode)

### `getVideoInterceptor` (26 Jul 2026) â€” __cm=1 + Domain-aware + freecdn in= injection
- **__cm=1 priority**: intercepta requests a `net52.cc/mobile/hls/{id}.m3u8?__cm=1`, sirve custom master desde `customMasters[id]` (alamacenado en loadLinks)
- **net52.cc/net22.cc/net11.cc** requests: Cookie `t_hash_t=...; hd=on; ott=nf/pv/hs` + Connection: close
- **CDN domains** (nm-cdn, freecdn, imgcdn): Cookie `t_hash_t=...; hd=on; ott=nf/pv/hs` + Connection: close
- **freecdn in= injection**: Guarda `in=` de la variant M3U8 request, lo inyecta en segment requests (.ts) via `addEncodedQueryParameter`
- All requests: `Cache-Control: no-cache`, `Connection: close`
- `hp=yes` stripped from M3U8 URL

## Current State (26 Jul 2026)
- âœ… **__cm=1 restored** â€” loadLinks descarga M3U8 con conexiÃ³n FRESCA, almacena en customMasters, sirve inline a ExoPlayer. Previene que el CDN devuelva preview basado en sesiÃ³n compartida.
- âœ… `clearCookie()` en cambio de episodio â€” t_hash_t fresco por episodio
- âœ… Interceptor mejorado: t_hash_t en CDN + freecdn in= injection + Connection: close
- âœ… `hp=yes` stripped, `_t=` cache-busting, anti-cache headers
- âœ… M3U8 body logging (len, video lines, props como hasIFrames/hasThumbnails)
- âœ… **PrimeVideo: episode transition WORKS** (nm-cdn, nunca tuvo el bug)
- â¸ï¸ **BUG: Netflix "next episode â†’ 10-min preview"** â€” pendiente de probar con __cm=1 restaurado

## HipÃ³tesis (26 Jul 2026)
- El cÃ³digo **antiguo** (con `__cm=1` + `setCustomMaster`) **NO tenÃ­a** preview en video. Solo thumbnails incorrectos (ExoPlayer genera thumbnails de I-frames, no hay thumbnail track separado).
- El cÃ³digo **actual** (URL directa sin __cm=1) **introdujo** el preview bug â€” ExoPlayer comparte conexiÃ³n HTTP con el CDN entre episodios, CDN sirve preview al detectar sesiÃ³n existente.
- **Fix**: Restaurar __cm=1 (fetch M3U8 en loadLinks con conexiÃ³n fresca) + mantener interceptor mejorado (t_hash_t a CDN + in= injection como safety net).
- Si aÃºn falla: el servidor CDN (`s24.freecdn3.top`) podrÃ­a requerir `in=` en segment requests de forma obligatoria (no opcional).

## Files
- `NetflixProvider.kt` â€” `loadLinks()` playlist.php â†’ mobile/hls primary + __cm=1
- `PrimevideoProvider.kt` â€” idem (ott="pv")
- `JioHotstarProvider.kt` â€” player.php primary, playlist.php fallback
- `Utils.kt` â€” `bypass()`, `getNewTvUserToken()`, `resolveApiUrl()`, `newTvBaseHeaders`, `m3u8CdnFixInterceptor()`, `NetflixMirrorStorage`
- `PlutotvProvider/PlutotvProvider.kt` â€” PlutoTV provider (separado)
- `PandramaProvider/` â€” Provider para pandrama.tv (Inertia.js SPA)

## Providers actualizados recientemente
| Provider | Fecha | Cambio |
|----------|-------|--------|
| `YoutubeProvider/Youtube.kt` | 12 Jul | Channel y playlist: lockupViewModel |
| `MundodonghuaProvider` | 12 Jul | Rewrite v4.0.4 + search URL fix |
| `DonghualifeProvider` | 12 Jul | Search fix + RumbleExtractor |
| `PandramaProvider` | 12 Jul | Rewrite completo: channel model, episode URLs, loadLinks via page data |
| `PrimevideoProvider` | 05 Ago | Posters lentos â†’ proxy `wsrv.nl?w=500` |

## ðŸ”§ Fix posters lentos PrimeVideo (05 Ago 2026)
- **SÃ­ntoma**: en el apartado PrimeVideo los posters tardan mucho en cargar y algunos parecen no tener poster; Netflix carga bien.
- **Causa raÃ­z (medido con curl/python)**:
  - Netflix `imgcdn.kim/poster/v/{id}.jpg` â†’ **22-62 KB**, carga en 0.6-0.8s.
  - PrimeVideo `imgcdn.kim/pv/v/{id}.jpg` â†’ **700KB-3.8MB** (imÃ¡genes full-size), carga en 1.7-5.4s â†’ CloudStream se queda sin tiempo o tarda muchÃ­simo.
  - No existe ruta de thumbnail propia en imgcdn para pv (`/pv/t/`, `/pv/300/`, etc. â†’ 404).
- **Fix** en `PrimevideoProvider.kt:28`: `pvPoster(id)` ahora devuelve `buildVerticalPosterUrlWithProxy(id, "pv")` â†’ `https://wsrv.nl/?url=...&w=500` â†’ **37-80 KB**, carga en 0.6-1.4s. Aplica a main page, search, detalle y recomendaciones (todos usan `pvPoster`). `pvBg`/`pvEpPoster` sin cambios.
- CompilaciÃ³n OK: `.\gradlew.bat :NetflixmirrorProvider:compileReleaseKotlin --console=plain -q`
- â¸ï¸ Pendiente: probar en dispositivo que los posters pv cargan rÃ¡pido.

## PandramaProvider â€” Estructura
- **Arquitectura**: Laravel + Inertia.js (Vue SPA). Datos embedidos en `window.bootstrapData = {...}` dentro de `<script>`
- **Main page**: `/dramas`, `/peliculas` â†’ parsea `loaders.channelPage.channels[].content.data[]`
- **Search**: `/search/{query}` â†’ `loaders.searchPage.results[]`
- **Load (detalle)**: `/titulo/{id}/{slug}` â†’ `loaders.titlePage.episodes.data[]` + `loaders.titlePage.title`
- **loadLinks (video)**: `/titulo/{id}/{slug}/temporada/{season}/episodio/{epNum}` â†’ `loaders.episodePage.current_video.src`
- **Tipos de video**: `embed` (OK.ru, VK, YouTube, Dailymotion), `video/stream` (HLS/DASH directo), `shaka` (DRM)
- **SubtÃ­tulos**: `current_video.captions[]` con url, name, language
- **No usa API** (`/api/*` retorna 401) â€” todo se obtiene del JSON embedido en HTML

### ðŸ› Fix VK + desync de versiones (10 Oct 2026 v6)
**SÃ­ntoma (usuario)**: "enlaces no encontrados" en Goblin T1E1 (`/titulo/1/goblin-el-solitario-ser-inmortal/temporada/1/episodio/1`). El log mostraba 4 sources y 0 links: `e.img-webp.com/video/...` (Ã—2), `tokyvideo.com/es/embed/375753`, `vk.com/video_ext.php?oid=779636710&id=456239102&hd=2`.
**InvestigaciÃ³n (PC + bytecode)**:
- `e.img-webp.com/video/...` â†’ **404 real**. Mirror muerto, correcto ignorarlo.
- `tokyvideo.com/es/embed/375753` â†’ pÃ¡gina de "El video solicitado no existe o ha sido borrado" (`global_tokyvideo_video_not_found = 1`). Mirror muerto, correcto ignorarlo (no existe extractor Tokyvideo en CS3 de todos modos).
- `vk.com/video_ext.php?...` â†’ **host mismatch**: `VkExtractor.mainUrl = "https://vkvideo.ru"` (verificado por bytecode) y `loadExtractor` matchea por `strippedUrl.startsWith(strippedMainUrl)` â†’ `vk.com/...` NUNCA matchea â†’ 0 links garantizados aunque el video estÃ© vivo. (El fetch directo a `vkvideo.ru/video_ext.php` da timeout desde PC â€” VK bloquea datacenters; el extractor usa headers de Firefox y lo maneja en el dispositivo.)
**Fix**: `fixEmbedHost()` (patrÃ³n VerAnime) mapea `vk.com`/`m.vk.com`/`www.vk.com` (http/https) â†’ `vkvideo.ru`, aplicado al construir `cleanSrc`; check `hasExtractor` += `vkvideo.ru`.
**Desync encontrado**: `build.gradle.kts` iba en 5 pero `plugins.json` seguÃ­a en **1** (igual que el desync SoloLatino/SeriesKao) â†’ el usuario tenÃ­a v1 instalada y nunca recibiÃ³ v2-v5. Ambos a **6**.
- CompilaciÃ³n: pendiente por el usuario (`.\gradlew.bat :PandramaProvider:make --console=plain -q`) â€” **no compilado por regla del repo**. `plugins.json`: version 6, `fileSize` **pendiente** (sigue 65000).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 â€” si el video VK estÃ¡ vivo sale link vÃ­a `VkExtractor`; si tambiÃ©n estÃ¡ borrado, el "enlaces no encontrados" es correcto (3 de 4 mirrors muertos en origen).

### ðŸ”§ Fallback VK directo (10 Oct 2026 v7)
**Log v6 en dispositivo**: el mapeo a `vkvideo.ru` funciona (el log ya muestra el host nuevo) pero `VkExtractor` sigue dando 0 links en EP1 (`...102`) y EP2 (`...103`).
**Bytecode de `VkExtractor.getUrl`**: doble GET (segundo con cookies) + regex `"url([0-9]+)":"([^"]*)"` (IGNORE_CASE) para progresivos y claves `dash_sep`/`hls` para DASH/HLS. Si la pÃ¡gina no trae esos patrones (video borrado/privado o muro de login), 0 links es lo correcto â€” pero el extractor no loguea nada y no se puede distinguir.
**Fix**: `tryVkDirect()` â€” si `loadExtractor` falla en un embed `vkvideo.ru`, reintenta con los MISMOS patrones del extractor y emite directo (`"$linkName ${q}p"`, quality vÃ­a `getQualityFromName`, type M3U8/VIDEO por extensiÃ³n, referer = pÃ¡gina VK). Si tampoco hay patrones â†’ log `VK sin patrones url (len=...)` = diagnÃ³stico definitivo de video muerto/muro. Solo corre en la rama de fallo (el Ã©xito no hace requests extra); `timeout = 15L` (segundos, regla NiceHttp).
- Versiones a **7** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 buscando `VK directo q=` (video vivo â†’ links) o `VK sin patrones url` (video muerto â†’ "enlaces no encontrados" correcto).

### ðŸ”§ Fallback VK estilo Retrotve (10 Oct 2026 v8)
**Propuesta del usuario**: portar el extractor VK de Retrotve (`extractVKVideo`), que sÃ­ reproduce esos enlaces.
**Diferencia clave encontrada**: Retrotve hace lo CONTRARIO al v6 â€” reescribe `vkvideo.ru` â†’ **`vk.com`** y parsea OTRA familia de claves (`"mp4_360"`, `"hls"`, `"dash"`, `"video_url"`, m3u8/mp4 directos, `data-video-url`), mientras `VkExtractor` usa `vkvideo.ru` + `"url720"`. Son dos formatos de pÃ¡gina distintos del mismo backend.
**Fix**: `tryVkDirect()` ahora prueba **ambas formas** (`vkvideo.ru` y `vk.com`) y une ambas familias de patrones (`"urlN"` + `"mp4_N"` + `"hls(_x)"` + `"video_url"` + m3u8/mp4 directos como Ãºltimo recurso). Emite al primer formato que dÃ© URLs (`break`); log por forma (`VK sin patrones (<url> len=...)`).
- Versiones a **8** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 â€” buscar `VK directo q=` y luego reproducir (los mp4 de VK suelen pedir Referer = pÃ¡gina VK, ya puesto).

### ðŸ“Š Veredicto v8 en dispositivo + Viki DRM (10 Oct 2026 v9)
**Log v8 (Goblin EP1/EP2)**: VK consume ~30s (extractor + ambas formas del fallback) y emite 0 â€” VK tambiÃ©n pone muro/bloquea desde el dispositivo. Los `img-webp` y `tokyvideo` ya estaban muertos en origen.
**Caso nuevo (usuario)**: Twinkling Watermelon EP1 trae `Video type=shaka, src=https://vikiplatform.akamaized.net/...mpd` (Ã—2) + 1 img-webp.
- MPD descargado y verificado: **`ContentProtection` Widevine (`edef8ba9-...` + pssh) + PlayReady en audio Y video** â†’ cifrado real, imposible sin licencia de Viki. Correcto ignorarlo (sin licencia no hay playback posible).
- Su img-webp (`...6e289439...`) tambiÃ©n da **404**.
- ConclusiÃ³n Twinkling: 0 links es lo correcto (2Ã—DRM + 1Ã—muerto).
**Fix v9 (diagnÃ³stico + perf, no cambia resultados)**:
1. `allVideos distinctBy { src }` â€” pandrama duplica sources idÃ©nticos (img-webp Ã—2, shaka Ã—2); ahorra fetches repetidos.
2. El fallback de embeds genÃ©ricos ahora loguea `embed fetch code=... len=...` (antes fetch silencioso) â†’ el prÃ³ximo tÃ­tulo se diagnostica de una mirada (404 vs pÃ¡gina sin m3u8).
- Versiones a **9** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar un tÃ­tulo con fuentes VIVAS (ok.ru/dailymotion/youtube) para confirmar que el plugin emite â€” Goblin y Twinkling no tienen nada reproducible en origen.

### ðŸ”§ Fallback VK vÃ­a WebView (10 Oct 2026 v10, Ãºltimo cartucho)
**Pregunta del usuario**: "Â¿entonces Goblin no se puede reproducir?" â€” con fuentes actuales no (muertos + DRM + muro VK a HTTP plano).
**HipÃ³tesis restante**: si el muro de VK es por **fingerprint TLS** (OkHttp vs Chromium real), el WebView sÃ­ pasarÃ­a â€” mismo mecanismo que resolviÃ³ StreamWish en SoloLatino/VerAnime (si el video estÃ¡ borrado, ni el WebView lo revive).
**Fix**: `renderViaWebView()` + `webViewMutex` + `catch (t: Throwable)` portados (patrÃ³n SoloLatino; `PandramaPlugin.load` guarda `pluginContext`); `tryVkDirect()` ahora termina con WebView (15s, sin readyJs â€” dumpea tras `onPageFinished`, la pÃ¡gina VK mete datos en JS impredecible) + `parseVkPage()` sobre el HTML renderizado. Incluye lo de v9 (nunca compilada: `distinctBy { src }` + `embed fetch code/len`).
- Versiones a **10** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 â€” buscar `[VK] WebView fallback` â†’ `[VK] WebView OK` (video vivo tras el muro â†’ links) o `VK sin patrones url` final (video muerto â†’ respuesta definitiva, ver Goblin en Netflix/Viki oficial).
- **Nota checker**: `bal_check.py` daba falso MISMATCH en regexes `""""` (raw string que empieza/termina en comilla, ej. `""""url...`). Fix: si tras el `"""` candidato hay otra `"`, es la comilla-de-contenido â†’ saltar 4 (`j+4`), no 3. Validado contra `Extractors.kt` (compilado seguro) â†’ 0/0/0.

### ðŸš« Fix enlace fantasma MPD-DRM (10 Oct 2026 v11)
**SÃ­ntoma (usuario, log v10)**: Twinkling EP1 muestra UN enlace que "parece imÃ¡genes, no se mueve y no hay audio".
**Causa raÃ­z**: la rama `isDirectPlay` emitÃ­a TODO `.mpd` sin verificar. El MPD de Viki lleva un AdaptationSet de **thumbnails (image/jpeg, SIN cifrar)** + audio/video cifrados. ExoPlayer reproduce el track libre (fotos fijas) y falla lo cifrado â†’ imagen congelada sin audio. Era un falso positivo de link.
**Fix**: `isDrmProtected(url)` â€” si el `.mpd` contiene `ContentProtection`, se omite (`MPD con DRM..., omitido`); si el probe falla por red, se emite igual (fail-open, comportamiento anterior). `.m3u8` sin cambios.
- Versiones a **11** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y verificar que Twinkling EP1 ya NO muestra el enlace fantasma (debe dar "enlaces no encontrados", que es lo correcto: DRM + img-webp 404).
- â¸ï¸ **Goblin VK aÃºn sin diagnÃ³stico completo**: el log v10 no trae las lÃ­neas `VK sin patrones` / `[VK] WebView fallback` (posible recorte del paste; ademÃ¡s EP1 y EP2 corren en hilos paralelos y el VK de EP1 pudo loguear fuera de la ventana pegada). Pedido al usuario: pegar TODAS las lÃ­neas con "VK" sin recortar para ver si el WebView corriÃ³ y quÃ© devolviÃ³ cada forma.

### ðŸ› Fix VK colgado: timeout al extractor (10 Oct 2026 v12)
**Reporte del usuario (v10)**: en VK no sale NI UNA lÃ­nea de diagnÃ³stico (ni `sin patrones`, ni `WebView fallback`) â€” solo el `src=` y luego "enlaces no encontrados".
**Causa raÃ­z**: el build v10 SÃ trae el fallback (las lÃ­neas `embed fetch code=` de v9 lo prueban, mismo build). Si no sale ni una lÃ­nea VK, el cÃ³digo posterior nunca corre â†’ `loadExtractor` **se cuelga**: VK no rechaza la conexiÃ³n, la agujerea (IP bloqueada), y los 2 GETs del `VkExtractor` esperan hasta el timeout por defecto. Todo lo de detrÃ¡s (directos + WebView) jamÃ¡s ejecuta.
**Fix**: `withTimeoutOrNull(30_000L)` SOLO alrededor del `loadExtractor` de VK (ok.ru/youtube intactos); al expirar loguea `VK loadExtractor colgado (30s), pasando a fallback` y corre `tryVkDirect` igual. `CancellationException` externa se re-lanza (no rompe el kill de 120s de CS3). Precedente: `loadExtractorCollect` con `withTimeout(25s)` en Veranime/Seriesdonghua.
- Versiones a **12** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente; v11 incluye el skip MPD-DRM, compilar una vez trae todo).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 â€” esperado `VK loadExtractor colgado (30s)` â†’ `VK sin patrones (https://vkvideo.ru... len=...)` â†’ `[VK] WebView fallback` â†’ veredicto final.
- **Nota timing v12 (log 10:37)**: el paste del usuario termina en el `src=` de VK sin lÃ­neas posteriores â€” copiado antes de tiempo o recorte. La secuencia VK completa tarda hasta ~90s en el peor caso (30 cap + 15 + 15 directos + ~30 WebView). **InstrucciÃ³n: entrar al episodio, NO tocar nada ~2 min, filtrar logcat por `VK` y pegar TODO.**
- **Builds frescos VERIFICADO (10 Oct 2026)**: no hay workflow en este repo (sin `.github/workflows`), pero la rama `builds` se actualiza sola ("Build: Actualizacion automatica..."). Tras `git fetch`: `PandramaProvider.cs3` reconstruido **10:32 Lima** (5 min despuÃ©s del push `d4b8cc0a` con todo v6-v12), 56719 bytes. El `.cs3` instalado por el usuario (10:51, version=12 + lÃ­neas `embed fetch`) **SÃ trae todo el cÃ³digo v12** â€” teorÃ­a de build-stale DESCARTADA. Si faltan lÃ­neas VK en un paste con mensaje rÃ¡pido, es filtro/recorte del usuario o cancelaciÃ³n, no el build. Protocolo de captura: papelera â†’ SIN filtros de texto â†’ tocar EP1 una vez â†’ anotar hora inicio/fin del mensaje â†’ pegar TODO el intervalo.

### ðŸ›¡ï¸ Blindaje de logs VK + resumen loadLinks (10 Oct 2026 v13)
**Pedido del usuario**: logs mÃ¡s directos para ver quÃ© pasa y por quÃ© sigue el "enlaces no encontrados".
**Cambios** (solo logging, cero cambio de comportamiento):
- Rama VK: `[VK] inicio extractor+fallback` (inmediata, prueba que la rama se ejecuta) + `[VK] extractor -> true/false (Xms)` + `[VK] fin video: ok=... total=...ms`.
- `tryVkDirect`: `[VK] directo inicio` + `[VK] fetch directo (1/2)` + `[VK] fetch code/len` por forma.
- WebView: `[VK] WebView HTML len=...` (distingue muro vacÃ­o de pÃ¡gina con contenido).
- `loadLinks`: resumen final `loadLinks <data> -> OK/SIN LINKS` (antes no existÃ­a â€” ahora el veredicto queda en log).
- Versiones a **13** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 **una vez, sin tocar nada ~2 min**, con logcat SIN filtros de texto â€” el log ahora cuenta la historia completa solo.

### ðŸŽ¯ Causa raÃ­z: links VK con URL vacÃ­a (10 Oct 2026 v14)
**Log v13 (el blindaje funcionÃ³ a la primera)**:
```
[VK] inicio extractor+fallback
[VK] extractor -> true (2465ms)   <- rÃ¡pido y "true"
[VK] fin video: ok=true
loadLinks -> OK                   <- Â¡OK!
```
â€¦pero igual "enlaces no encontrados".
**Causa raÃ­z**: el regex de `VkExtractor` (`"url([0-9]+)":"([^"]*)"`) acepta `""` â€” VK sirve la pÃ¡gina SIN urls (video borrado) y el extractor emite links con **URL vacÃ­a** igual. Nuestro `ok=true`, CS3 descarta los vacÃ­os â†’ 0 vÃ¡lidos â†’ mensaje. Falso positivo de `loadExtractor`.
**Fix**: wrapper del callback en la rama VK que descarta `link.url.isBlank()` (`[VK] link vacÃ­o descartado: <name>`) y `ok` se calcula de lo EMITIDO-vÃ¡lido (no del return del extractor). En `parseVkPage`: contador `empty` + log `VK urls vacÃ­as=N (video borrado en origen?)` cuando hay matches vacÃ­os pero nada Ãºtil.
- Versiones a **14** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 â€” esperado `[VK] link vacÃ­o descartado` Ã—N â†’ `VK urls vacÃ­as=N` â†’ `loadLinks -> SIN LINKS` (veredicto correcto y visible: video borrado en origen).

### ðŸ·ï¸ Nombres con idioma estilo web (10 Oct 2026 v15)
**Log v14 (La Vengadora EP1)**: VK **vivo y reproduciendo** (`extractor -> true`, links reales) + ok.ru Ã—2 â†’ `loadLinks -> OK`. Pero los links salÃ­an con el nombre pelado del extractor (`Vk`, `Okru`) sin idioma.
**Datos reales del episodio** (HTML): los videos traen `name` de uploader (`Vaca`, `Rosado`, `Okru-ext1`, `Iframe` = etiquetas de server de la web) + `language` (`es-419`, `ko`).
**Fix**: `loadExtractorNamed()` â€” envuelve lo emitido por `loadExtractor` como `"$linkName [${link.name}]"` (ej. `Vaca (Latino) [Vk 720p]`), copiando quality/type/referer/headers/extractorData (patrÃ³n VerAnime `loadExtractorCollect`); descarta URLs vacÃ­as (absorbe el wrapper de v14). Aplicado en las 4 llamadas de `loadLinks` (hasExtractor, fallback genÃ©rico Ã—2, else). Etiquetas: `ko` â†’ `(Coreano)`, `es-ES` â†’ `(Castellano)` (nuevo), resto igual.
**Hallazgo v14 Goblin (redirect loop)**: el fetch vkvideo.ru falla con `Too many follow-up requests: 21` (VK rebota en loop â†’ login); el fetch vk.com da `200 len=21903` sin patrones; el WebView da `len=65993` sin patrones. Muro confirmado por 3 vÃ­as.
- Versiones a **15** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar La Vengadora EP1 â€” los links deben salir como `Vaca (Latino) [Vk ...]`, `Okru-ext1 ... [Okru ...]`.

## GloboViewProvider â€” Estado (19 Jul 2026)
### âœ… Implementado
- `getMainPage`: 16 paÃ­ses (EspaÃ±a, MÃ©xico, Argentina, Colombia, EEUU, Venezuela, PerÃº, Chile, Ecuador, Rep. Dominicana, Puerto Rico, Brasil, Alemania, Reino Unido, Francia, Italia) en vez de 8 categorÃ­as que timeouteaban. Las pÃ¡ginas de paÃ­s cargan mÃ¡s rÃ¡pido (~8-15s) y tienen todos los canales disponibles.
- `search`: escanea los mismos 16 paÃ­ses (5 antes) = ~384 canales vs 120 antes
- Todos los `app.get()` pasan `timeout = 60L`

### â¸ï¸ Pendiente
- Cada paÃ­s puede tener paginaciÃ³n. Solo se ve pÃ¡gina 1 (~24 canales). Para ver mÃ¡s canales por paÃ­s, necesitarÃ­amos detectar paginaciÃ³n.

## Next Steps (Netmirror)
1. âœ… Instalar APK compilado en dispositivo y probar reproducciÃ³n real
2. â¸ï¸ **PROBAR cambios del 10 Jul v2** (customMasters + __cm=1 + M3U8 body logging)
3. â¸ï¸ Revisar los logs del M3U8 body â€” comparar contenido de EP1 vs EP2
4. â¸ï¸ Si el M3U8 es IDÃ‰NTICO pero preview persiste: el problema es en los segmentos CDN, no en el M3U8
5. â¸ï¸ Si el M3U8 es DIFERENTE (EP2 tiene segmentos preview): el servidor limita EP2 cuando EP1 sigue activo
6. â¸ï¸ PrÃ³ximo paso si es CDN: probar `Connection: close` en segment requests (ya implementado) o crear OkHttpClient propio para segmentos

## TudoramaProvider â€” Estado (17 Jul 2026)
### âœ… Implementado
- `getMainPage()` â€” 7 secciones (recientes, tendencias, gÃ©neros, pelÃ­culas)
- `search()` â€” bÃºsqueda por query string
- `load()` â€” detalle con episodios DOM + AJAX (`corvus_get_episodes`)
- `loadLinks()` â€” download table â†’ `/d/` URLs â†’ `resolveServerUrl()` â†’ `extractFromEmbed()`
- Posters en episodios (DOM y AJAX con `episode_image` del API)

### âœ… Arreglado (17 Jul)
- `/d/` â†’ `/e/` path conversion en `extractFromEmbed` (VidStack espera `/e/` embed, no `/d/` download)
- Fallback AJAX `corvus_get_servers` + stream URL â†’ iframe src (pero requiere login WP)
- Manual HTTP extraction con regex (m3u8/mp4 en HTML)
- **NEW: Direct API extraction** â€” `tryApiExtraction()` para hgcloud.to, bysesukior.com, 4meplayer.pro
- `/f/` â†’ `/e/` **removido** (preserva `/f/` para hgcloud.to)
- `Uri.parse()` import para parsing de URLs

### â¸ï¸ Pendiente (BUG)
- `loadExtractor()` retorna 0 links para TODOS los servidores
- Causa raÃ­z: Extractors registrados vÃ­a plugin (VidStack subclases) NO son auto-descubiertos por CS3
- `tryApiExtraction()` intenta llamadas API directas como fallback (requiere probar)
- `corvus_get_servers` requiere autenticaciÃ³n WordPress (no usable)

### Next Steps (Tudorama)
1. âœ… Build APK exitoso con API extraction directa
2. â¸ï¸ **Probar APK en dispositivo** â€” verificar si API extraction encuentra links:
   - hgcloud.to: `/api/source/{code}` POST
   - bysesukior.com: `/api/source` POST
   - 4meplayer.pro: `master.php` POST
3. â¸ï¸ Si API extraction falla: implementar extractor VidStack manual (WebView JS injection)
4. â¸ï¸ Si funciona: probar con mÃºltiples episodios y servers

### ðŸ› Fix tÃ­tulo + Byse compartido + PixelDrain directo (10 Oct 2026 v2)
**SÃ­ntoma (usuario, log v1)**: `load: title=null` â†’ `title not found` (Ã—3) en `/serie/goblin-el-solitario-ser-inmortal/` â€” el detalle no abrÃ­a.
**Causa raÃ­z (HTML real)**: el theme Wstream-4 usa `<header class="hero__header"><h1>TÃTULO</h1>` (h1 SIN clase). El provider buscaba `h1.hero__title` (pelis) y `section#hero .hero--serie .hero__header h2` (series) â€” ambos inexistentes â†’ null siempre.
**Fix `load()`**: `section#hero .hero__header h1` + fallbacks `og:title` / `<title>` (cortando `Â» TuDorama`). Regla `takeIf { !it.isNullOrEmpty() }` (nunca `isNotEmpty()` tras `?.`). Resto del parse verificado vigente (poster `.hero__poster img`, backdrop, `.hero__overview`, tags `.hero__genres a`, episodios `li.lep` con `data-episode`/`data-season`/`a[href]`/`.lep__title`).
**Extractores de otros plugins (hipÃ³tesis del usuario CONFIRMADA)** â€” episodio real `/ver/...-s1x1/` trae: EarnVids `dingtezuni.com/file/...`, Filemoon `bysesukior.com/d/...`, StreamHG `hglink.to/...`, PixelDrain `pixeldrain.com/u/...`:
- `bysesukior.com` sirve **`Byse Frontend`** â†’ el `TudoramaBysesukior : VidStack()` era el stack equivocado (0 links garantizados). **Portado `ByseExtractor.kt` Ã­ntegro desde VerAnime** (autocontenido: Jackson + OkHttp propio + JCA; solo retag de logs a `Tudorama`); `extractFromEmbed` lo usa primero cuando el host contiene `byse` (probe `Byse Frontend` â†’ `ByseHttpExtractor().extract()` â†’ sources + subs). Los 4 `VidStack` registrados quedan como fallback.
- `pixeldrain.com/u/{id}` â†’ **directo sin extractor**: `https://pixeldrain.com/api/file/{id}` streamea el archivo (verificado: la API devolviÃ³ el binario, >5MB). Rama en el loop de download-table que emite VIDEO directo con referer a la pÃ¡gina (antes `resolveServerUrl` la descartaba por falta de `a.download-button`).
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 20000).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :TudoramaProvider:make --console=plain -q`), instalar y probar Goblin (`adb logcat -s Tudorama:V`): detalle con tÃ­tulo + `byse sources=N` en Filemoon + link PixelDrain directo.

### ðŸ”§ Fix Filemoon: resolveServerUrl descartaba la pÃ¡gina Byse (10 Oct 2026 v3)
**Log v2 (usuario)**: tÃ­tulo OK en todo (pelis + series + 16 eps Goblin), PixelDrain directo **reproduce** (`PixelDrain directo: ooP8Nmds` â†’ `FIN: true`), pero Filemoon nunca llega al extractor: `resolveServerUrl: no download-button found` â†’ skip.
**Causa raÃ­z**: `resolveServerUrl` exige `a.download-button` (?s=) en TODAS las URLs de la tabla. Pero `bysesukior.com/d/...` **ES** la pÃ¡gina del reproductor (Byse Frontend, verificado), no una landing â€” no tiene botÃ³n y se descartaba antes de llegar a `emitByse`.
**Fix**: si no hay botÃ³n y la URL contiene `byse`, se devuelve la URL tal cual (ya es el embed). EarnVids/StreamHG sin cambios (sus landings sÃ­ necesitan resoluciÃ³n; pendiente de diagnosticar si se reporta).
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 buscando `byse sources=N` + links `Filemoon - Byse ...`.

### ðŸ”§ Fix pelÃ­culas (AJAX 404) + etiqueta de idioma (10 Oct 2026 v4)
**Log v3 (usuario)** â€” Filemoon **reproduce** vÃ­a Byse (`sources=1` â†’ `FIN: true`), PixelDrain directo OK. Dos pendientes nuevos:
1. **PelÃ­cula** (`/pelicula/hear-me-our-summer/`): `0 servidores (download)` â†’ fallback AJAX â†’ POST a `https://tudorama.com/admin-ajax.php` â†’ **404** (`<title>PÃ¡gina no encontrada`) â†’ `FIN: false`.
   **Causa**: en `loadLinks` el `ajaxUrl` cae a `"$mainUrl/"` porque las pelis no tienen `div.eps`; el endpoint WP real es `/wp-admin/admin-ajax.php` (confirmado por `corvutils.ajaxurl` del propio HTML + convenciÃ³n WP). **Fix**: default `"$mainUrl/wp-admin/"`.
2. **Idioma en los links** (pedido): en Goblin salen "servidores en espaÃ±ol latino", en la peli "servidor subtitulado". La tabla download no trae idioma por fila, pero la pÃ¡gina sÃ­ (`og:title`: "Episodio 1 EspaÃ±ol Latino"). **Fix**: `detectContentLanguage(doc)` (og:title + `<title>`: latino â†’ `Latino`, subtitul â†’ `Subtitulado`, castellano â†’ `Castellano`) y el re-wrap final antepone `[$langTag]` â†’ `[Latino] Filemoon - Byse 1080p`.
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 20000).
- â¸ï¸ **Pendiente**: compilar, instalar y (1) probar la peli Hear Me buscando en `fetchStreamServers` JSON vÃ¡lido en vez de 404; (2) comprobar `[Latino]`/`[Subtitulado]` en los nombres de link.

### ðŸ”§ Fix ?s= encoded + AJAX array directo (10 Oct 2026 v5)
**Log v4 (usuario)** â€” dos causas raÃ­z nuevas, ambas verificadas en el propio log:
1. **`?s=` URL-encoded**: `resolveServerUrl: resolved=https%3A%2F%2Fmorencius.com%2F...` (sin decodificar) â†’ todo lo de abajo muere (`host=null`, `Expected URL scheme ... no scheme was found`). Afectaba a TODOS los `cdn.tudorama.com/download-*.php` (earnvids/streamhg/filemoon EspLat+SubEsp). **Fix**: `URLDecoder.decode(..., "UTF-8")` con fallback al crudo (`runCatching`, regla fail-open).
2. **AJAX devuelve array directo**: con la URL ya corregida, el POST trae `[{url,name,lang,type,server}]` (NO el `["{...}"]` doblemente envuelto que esperaba el cÃ³digo) â†’ `outer parse failed ... START_OBJECT` â†’ 0 links en pelis (y en series cuando la tabla falla). **Fix**: `StreamServerItem` + parse directo primero, legacy despuÃ©s. El `lang` por server (`en`â†’`Subtitulado`, `es`â†’`Latino`, restoâ†’idioma de pÃ¡gina) se hornea en el nombre (`[Subtitulado] sub:abyss`); el re-wrap final no duplica si ya empieza con `[`.
- Versiones a **5** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar (1) Hear Me (debe resolver iframes y emitir); (2) My Royal Nemesis T1E1 (los 6 `download-*.php` deben decodificar y fluir a VidStack/Byse/manual).
- âš ï¸ **Nota ediciÃ³n**: el `edit` reportÃ³ un falso "not found" en el re-wrap pero el contenido SÃ quedÃ³ aplicado (verificado por `git diff`); el call-site de `fetchStreamServers` (param `langTag`) sÃ­ requiriÃ³ re-aplicaciÃ³n â€” sin Ã©l no compilaba (6 params vs 5 args). Verificar siempre con `git diff`, no con el mensaje de la herramienta.

### ðŸ”§ EarnVids=vidhide + iframe por JS (10 Oct 2026 v6)
**Log v5 (usuario)** â€” Hear Me parsea bien (`[Subtitulado] sub:*` Ã—4) pero `resolveStreamUrl` da null en los 4 (el `<iframe id="myIframe">` viene con `src` VACÃO; el src real lo pone el JS al pulsar: `iframe.src = "https://abyssplayer.com/..."`). My Royal Nemesis decodifica bien pero EarnVids/StreamHG dan 0 (Filemoon-Byse Ã—2 OK â†’ `FIN: true`).
**Hallazgo EarnVids (hipÃ³tesis del usuario CONFIRMADA otra vez)**: SoloLatino (`SoloLatinoProvider.kt:1250`) y SeriesKao (`SerieskaoExtractors.kt:96`) mapean `morencius.com` â†’ `vidhidepro.com`. Verificado: `/download/` dice "Downloads disabled" y `/e/` da 404, pero **`/embed/{code}` existe** (pÃ¡gina "Embed" del player). Es el mismo backend vidhide.
**Fixes**:
1. `resolveStreamUrl`: fallback a regex `iframe.src = "..."` (doble y simple comilla) sobre el HTML cuando `#myIframe[src]` viene vacÃ­o â†’ la peli Hear Me debe resolver sus 4 iframes.
2. Portados `unpackPackedJS()` + `tryVidHideProExtraction()` de Serieskao (probe paralelo de masters con `amap`, `preferOrder hls2>hls3>hls4`, base relativa = origin propio, etiqueta interna `VidHidePro - key` sin idioma porque el re-wrap externo ya pone `[Latino]`+server). Hook en `extractFromEmbed` para hosts morencius/vidhidepro construyendo `/embed/{code}` ANTES del path VidStack.
- Versiones a **6** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar (1) Hear Me (los 4 `stream/*.php` deben dar `iframe por JS` + emitir); (2) My Royal Nemesis â€” EarnVids debe dar `[VH-Pro] emit hlsX` (hgcloud sigue en 522, nada que hacer ahÃ­).
- âš ï¸ **Nota ediciÃ³n (otra)**: el `edit` fallÃ³ un anclaje por una lÃ­nea en blanco intermedia no vista â€” releer la regiÃ³n antes de reintentar.

### ðŸ”§ EarnVids emite + minochinos es vidhide (10 Oct 2026 v7)
**Log v6 (usuario)** â€” EarnVids por VidHidePro **FUNCIONA** (`/embed/` â†’ unpack â†’ probe hls2/hls3/hls4 en 200 â†’ 3 variantes Ã—2 servidores). Byse Ã—2 OK. hgcloud sigue en **522** (caÃ­do). PelÃ­cula: parsea `[Subtitulado] sub:*` Ã—4 + `iframe por JS` OK en los 4, filemoon-Byse emite (FIN: true), pero **earnvids NO** (su iframe va a `minochinos.com/embed/...`, no a morencius).
**Hallazgo**: `minochinos.com/embed/{code}` trae player "Embed" con assets `/vidhide/` + meta `PELI ... SubEsp [TuDorama com]` â†’ **otro clon vidhide** (el `tryMinochinosApi` da 404 porque esa API no existe; el `TudoramaMinochinos : VidStack()` tampoco sirve).
**Fix**: hook VH += host `minochinos` (misma construcciÃ³n `/embed/{code}`, que aquÃ­ ya viene asÃ­). Si no hay packer, falla graceful al path normal.
- Versiones a **7** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Hear Me buscando `[VH-Pro] emit hlsX` en `sub:earnvids` (abyssplayer.com queda fuera: host desconocido sin extractor).

### ðŸ”§ EarnVids directo (packer) + hglink challenge (10 Oct 2026 v8)
**Log v7 (usuario)** â€” peli Hear Me: `sub:earnvids` â†’ minochinos VH **emite 3 variantes** (`probe hls2/hls3/hls4 â†’ 200`), filemoon-Byse OK â†’ `FIN: true`. Goblin EP1/13/10/16: Byse + PixelDrain OK en todos.
**Hallazgo dingtezuni (EarnVids directo)**: `dingtezuni.com/file/{code}` trae el player jwplayer con **eval Dean Edwards empaquetado** â€” las URLs m3u8 solo existen tras desempaquetar (dict con `m3u8`, cuenta acek-cdn `m5QqjwpATPzb`). Por eso los regexes planos fallaban. En cambio `morencius.com/download/` dice "Downloads disabled" y `/e/` da 404 (la form viva es `/embed/`, ya cubierta en v6).
**Hallazgo hglink.to**: sirve pÃ¡gina challenge "Loading... please wait..." (JS). Como apunta al backend hgcloud (522 caÃ­do), no vale la pena WebView para Ã©l.
**Fix**: Pattern 4 en la rama manual de `extractFromEmbed` â€” `unpackPackedJS(page)` + regex m3u8/mp4 sobre el desempaquetado (emite todos los distintos, precedente SoloLatino `[SW] M3U8 (eval)` sin probe por frescura del token). Solo corre si los patterns 1-3 fallan.
- Versiones a **8** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Goblin T1E1 buscando `extractFromEmbed: unpacked url found` en EarnVids (dingtezuni).

---

## TokianimeProvider â€” Estado (24 Jul 2026)

### âœ… Funcionando
- `getMainPage()`: home/Ãºltimos, tendencia, gÃ©neros (AcciÃ³n, Comedia, FantasÃ­a, Drama, Romance, Sci-Fi) â€” carga HTML, parsea links a[href^=/anime/] + posters de img[src].
- `load()`: detalle del anime via DOM (og:title, description, year, tags, poster), episodios via API `api/anime/{slug}/episodes`. Soporta multi-season: parsea "Ver orden sugerido" accordion, llama API por cada entry, ordena Temporadas â†’ OVA â†’ Especial â†’ PelÃ­cula.
- `loadLinks()`: extrae SID del RSC payload (`self.__next_f.push`), llama `api/player/source?a=MAL_ID&ep=N&sid=SID&mode=play`, retorna M3U8 con q=720p/1080p/480p. Regex normaliza `\"` â†’ `"` y `\u0026` â†’ `&`.
- `search()`: usa API `GET /api/catalog?adult=0&q={query}&page=0&pageSize=20` â€” extrae slugs/titles/posters via regex, limite 50 resultados, detiene en page vacÃ­a.

### ðŸ”§ Ãšltima fix (24 Jul)
- **Search reescrito**: reemplazÃ³ fallback por gÃ©nero (lento) con API `GET /api/catalog?q={query}` directa.
- **Bug corregido**: regex `\[(.*?)\]` se detenÃ­a en primer `]` (tags array). Ahora extrae slugs/titles/posters con regex independientes (no confÃ­a en aislar items array).
- **Loop infinito corregido**: el paginado se detiene cuando `"items":[]` o `results.size >= total`.
- **Episodios con posters y descripciÃ³n**: ahora extrae `thumbs.{ep} â†’ posterUrl` y `meta.{ep}.overview â†’ description` del API `api/anime/{slug}/episodes`.
- **Bug search corregido**: `sources[].slug` se contaba como slug adicional, desalineando el pairing por Ã­ndice. Ahora usa regex `"slug":"xxx"[^}]*?"title":"yyy"` que solo empareja slug+title del mismo item, ignorando sources.
- **Bug #ultimos corregido**: algunos links tenÃ­an texto "Ver ahora" (botÃ³n) en vez del nombre del anime. Ahora usa `img.alt` primero (tÃ­tulo real), filtra "Ver ahora"/"Watch now".

### âœ… Confirmado
- Search API ya busca en mÃºltiples campos: `title`, `titleEnglish`, `titleNative`, `synonyms`. Buscar "One Punch Man" retorna resultado con `matchedBy:"title"`.
- Episodios API devuelve `thumbs.{n}` (poster 640.webp) y `meta.{n}.{title,overview}` por cada episodio.

### ðŸ”¬ PrÃ³ximo
- Probar en dispositivo: search API, load multi-season, episodios con posters/descripciÃ³n, reproducciÃ³n de video.

---

## TMDBProvider â€” Estado (24 Jul 2026)

### âœ… Estructura creada
Plugin completo que usa:
- **TMDB API v3** para catÃ¡logo/bÃºsqueda/detalles (necesita API key gratuita)
- **TMDB Embed API** autohosteada para streams (Docker: `inside4ndroid/tmdb-embed-api`)

### Soporte
- PelÃ­culas y Series
- Episodios con posters (`still_path`) y descripciÃ³n (`overview`)
- Recomendaciones desde TMDB
- Multi-temporada (carga episodio por episodio desde TMDB)
- Score desde TMDB (escala 0-10 convertida a Score CS3)

### â¸ï¸ Pendiente
- Usuario debe registrar API key en https://www.themoviedb.org/settings/api
- Usuario debe deployar TMDB Embed API vÃ­a Docker
- Probar en dispositivo

### Archivos
- `TMDBProvider/build.gradle.kts`
- `TMDBProvider/src/main/kotlin/com/example/TMDBPlugin.kt`
- `TMDBProvider/src/main/kotlin/com/example/TMDBProvider.kt`

---

## UniqueStreamProvider (AnimeStream) â€” Estado (02 Ago 2026)

Sitio: `anime.uniquestream.net` (Nuxt). Provider en `UniquestreamProvider/src/main/kotlin/com/example/UniquestreamProvider.kt`.

### âœ… Causa raÃ­z del error 3001 RESUELTA (02 Ago 2026)
- **SÃ­ntoma**: `ERROR_CODE_PARSING_CONTAINER_MALFORMED (3001)` en TODOS los links de algunos episodios (ej. serie `HWH21Ge0`, episodios `hDGx3QZd` fallan, `vEPPCFXi`/`HAx1NgR2` funcionan).
- **Causa**: el interceptor devolvÃ­a `hex.decode(media_id)` como key AES. Eso funciona SOLO cuando la key real coincide con el media_id (casualidad para ciertos episodios). El key.bin del servidor es un **seÃ±uelo cifrado** cuya key real se deriva distinta por episodio.
- **DerivaciÃ³n REAL de la key (encontrada en el JS del player, funciÃ³n `Za`)**:
  1. Fetch de `key.bin` **con header obligatorio `x-am-media-id: {media_id}`** (sin el header el servidor devuelve seÃ±uelo que no descifra).
  2. El body es **base64** del dato cifrado.
  3. Descifrar AES-CBC: `key = SHA256("key"+media_id)[:16]`, `iv = SHA256("iv"+media_id)[:16]`.
  4. El resultado (16 bytes) es la key real de los segmentos.
- **IV de segmentos**: media sequence number (`seg-N.png` â†’ IV = N en 16 bytes big-endian), igual que el EXT-X-KEY (sin atributo IV).
- **Verificado con Python (pycryptodome)**:
  - `hDGx3QZd`: derived=`f127680318f3aeaf6c2f050108fb7372` â†’ 5/5 seg TS-OK (con media_id fallaba).
  - `vEPPCFXi`: derived=`a18520f3ab9af4169f0d36f4f085eccc` == hex(media_id) â†’ 5/5 TS-OK.
- **Fix implementado** en `getVideoInterceptor`: hace el fetch real del key.bin con header `x-am-media-id`, descifra con SHA-256/AES-CBC, y devuelve la key derivada. Fallback a `hex.decode(media_id)` si el fetch/decrypt falla.
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`

### ðŸ”§ Fix error 2000 tras cambio de CDN (05 Ago 2026) â€” get3.mediacache.cc
- **SÃ­ntoma**: tras migrar el sitio de `api.uniquestream.net` a **`get3.mediacache.cc`**, los episodios daban `ERROR_CODE_IO_UNSPECIFIED (2000)` / "Enlaces no encontrados Error de fuente".
- **Nuevo flujo del API de video** (`/episode/{id}/media/dash/{locale}` â†’ 200):
  - `hls.playlist` = `https://get3.mediacache.cc/episode/{serie}/{season}/{ep}/.../{media_id}_{locale}/master.m3u8?sign=...&expires=...` â€” **media_id CORTO** (ej. `534740`, 6 dÃ­gitos), NO 32-hex.
  - Master (1 sola variante `v_1920x1080/playlist.m3u8?...` con su propio sign) â†’ variante AES-128 con `URI="../keys/key.bin?expires=...&sign=..."` y segmentos `seg-N.png` **sin sign**.
- **Verificado en PC (curl/python, sign firmado para mi IP)**:
  - Master, variante y segmentos â†’ **200 con solo User-Agent** (incluso `ExoPlayerLib/2.19.1`); sin headers â†’ 403.
  - `key.bin` **con** `x-am-media-id: 534740` â†’ body base64 real â†’ AES-CBC `key=SHA256("key534740")[:16]`, `iv=SHA256("iv534740")[:16]` â†’ `740c6909a4c4d4792fc9ed7bfd183baa` (padding PKCS7 vÃ¡lido) â†’ seg-0 descifra a TS vÃ¡lido (0x47 en 20133/20133 paquetes, IV=media sequence).
  - `key.bin` **sin** `x-am-media-id` â†’ 200 pero **seÃ±uelo distinto** (descifra a garbage `05dc9366...`) â†’ ExoPlayer usarÃ­a key incorrecta.
- **Causa raÃ­z**: `keyRegex = /([0-9a-f]{32})_.../` exigÃ­a media_id de 32 hex â†’ no matcheaba los IDs cortos (`534740`) â†’ `mediaId = null` â†’ el interceptor era no-op â†’ ExoPlayer pedÃ­a key.bin sin `x-am-media-id` â†’ key seÃ±uelo â†’ fallo.
- **Fix** en `getVideoInterceptor` (`UniquestreamProvider.kt:91`):
  - `keyRegex` cambiado a `/([A-Za-z0-9]+)_[^/]+/master\.m3u8` (captura `534740`).
  - Fallback adicional: extraer media_id de la propia URL de `key.bin` (`/([A-Za-z0-9]+)_[^/]+/keys/key\.bin`), primero del link y luego del request actual.
  - Mantiene fallback legacy 32-hex y `hex.decode(media_id)`.
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- â¸ï¸ **Pendiente**: probar en dispositivo `dv_1011` (The Hated Classmate) â€” ver log `Interceptando key.bin -> 740c6909... (derived=true)`.

### âœ… Funcional (antes del fix)
- `getMainPage`: 9 secciones (Nuevos, Populares, PelÃ­culas, AcciÃ³n, Aventura, Comedia, Drama, FantasÃ­a, Sci-Fi).
- Posters `480x720`; pelÃ­culas `TvType.AnimeMovie`; descripciÃ³n con `Audio:`/`SubtÃ­tulos:`.
- Temporadas ordenadas por `season_seq_number`, indexadas 1..N; `distinctBy { content_id }`.
- SubtÃ­tulos VTT: `subtitleCallback(SubtitleFile(lang, url))` con `language` (NO `locale`).

### ðŸ”§ Fix desorden de temporadas al re-entrar (03 Ago 2026)
- **SÃ­ntoma**: 1Âª carga orden correcto (1,2,3,4,5); 2Âª/3Âª carga la UI muestra temporadas desordenadas (1,2,5,3,4 / 1,4,5,7,9,10,2,3,6,8).
- **DiagnÃ³stico**: logs en `load()` probaron que el provider SIEMPRE entregaba orden correcto (API estable, sort por `season_seq_number`, `episodesList` ordenado). El desorden lo producÃ­a CloudStream al **restaurar** la agrupaciÃ³n en la rama Anime de `postEpisodes()` (NO reordena: confÃ­a en el orden de inserciÃ³n, que al restaurar del cachÃ©/ViewModel se pierde).
- **Causa raÃ­z estructural**: la rama `AnimeLoadResponse` de CloudStream no ordena y su agrupaciÃ³n se desordena al restaurar; la rama `TvSeriesLoadResponse` **sÃ­** reordena por `season*10000 + episode` (determinista y estable ante restauraciÃ³n). Providers sin `this.season` (Hianime, AnimeOnsen) no sufren el bug por tener una sola temporada.
- **Fix**: cambiar `newAnimeLoadResponse(TvType.Anime)` â†’ `newTvSeriesLoadResponse(TvType.TvSeries, episodesList)` pasando la lista directamente (mismo patrÃ³n que `PrimevideoProvider.kt:162`). El reproductor/lector de episodios no cambia.
- Logs de diagnÃ³stico en `load()` agregados y luego **removidos** tras confirmar la causa.
- âœ… **Confirmado en dispositivo (03 Ago 2026)**: el bug era de la rama `Anime` de CloudStream (no soporta bien muchas temporadas). Con `TvSeries` las temporadas se mantienen ordenadas 1..N en re-entries y las series con muchas temporadas cargan **mÃ¡s rÃ¡pido**.

### ðŸ”§ Fix especiales al inicio de la temporada (04 Ago 2026)
- **SÃ­ntoma (Attack on Titan `AG689vQw`, temporada 4 `heIY0Kaq`)**: los Especiales (THE FINAL CHAPTERS Special 1/2, `episode_number=1.0/2.0`) aparecÃ­an como "episodio 1 y 2" al INICIO de la temporada 4 en la UI.
- **DiagnÃ³stico por logs**: el provider SÃ los entregaba al final (`orden FINAL`: 60-87, luego Special 1, Special 2). PERO la rama `TvSeries` de CloudStream **reordena** por `season*10000 + episode`, y como los especiales tienen `episode_number=1/2`, quedaban siempre primeros numÃ©ricamente.
- **Causa raÃ­z**: imposible separar la clave de orden de CloudStream del nÃºmero de episodio mostrado; si los especiales tienen nÃºmero bajo, CloudStream los fuerza al inicio.
- **Fix** en `loadSeasonEpisodes` (`UniquestreamProvider.kt:~353`): separar `regulars`/`specials` (special = tÃ­tulo contiene "Special" O `episode` empieza con "SP"), ordenar normales ascendente, y **renumerar los especiales con `maxRegular + 1, +2, ...`** (en AoT pasan a ser 88, 89). AsÃ­ CloudStream los ordena al final de la temporada y ya no son "1 y 2". No afecta playback (el ID usado en loadLinks es `content_id`, no el nÃºmero).
- Logs de diagnÃ³stico removidos tras implementar el fix.
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`

### â¸ï¸ Pendiente
- Compilar APK completo y probar en dispositivo (los episodios que daban 3001).
- **Probar el fix de temporadas**: entrar 2Âª vez en `5spNP0fT` (Re:ZERO) y `oC7sJj1J` â€” verificar que el orden 1..N se mantiene. âœ… **VERIFICADO**
- Scripts de verificaciÃ³n en `%TEMP%\opencode\`: `check_final.py` (derivaciÃ³n key real), `check_xam.py` (header x-am-media-id), `check_derive3.py`, `check_hdg.py`, `check_seasons.py` (orden API).

### Archivos
- `UniquestreamProvider/src/main/kotlin/com/example/UniquestreamProvider.kt` (~570 lÃ­neas)

### ðŸ”§ Fix timeout 120s en series grandes (One Piece) (09 Ago 2026)
- **SÃ­ntoma**: One Piece (1216 eps, 68 pÃ¡ginas de API) daba `Timed out waiting for 120000 ms` en TODAS las peticiones de episodios; el log mostraba `getWithRetry error ... (intento 1): Timed out waiting for 120000 ms` y `StandaloneCoroutine was cancelled` en main page.
- **DiagnÃ³stico (decompilado)**: `APIRepository$load$2` envuelve el `load()` del provider en `withTimeout(getLoadTimeoutMs() ?: 120000L)`. El `getTimeout(null)` de APIRepository devuelve 120000ms con `coerceIn(5000, 480000)`. One Piece superaba los 120s â†’ CloudStream cancelaba TODO el `load()` y mataba las peticiones en vuelo (ese mensaje es `TimeoutCancellationException` de kotlinx, NO el timeout de OkHttp).
- **Fix**:
  1. `override val loadTimeoutMs: Long? = 480_000L` (mÃ¡ximo permitido por coerceIn; el getter `getLoadTimeoutMs()` es ACC_PUBLIC sin ACC_FINAL â†’ open val overrideable).
  2. **Rethrow `CancellationException`** en `getWithRetry`, secciones de `getMainPage`, y loop de `load()` â€” ya no se loguea como error ni se reintenta tras cancelaciÃ³n.
  3. SemÃ¡foro API 6 â†’ **12** (en PC 20 concurrentes funcionaron sin Cloudflare).
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` â†’ `UniquestreamProvider/build/UniquestreamProvider.cs3` (75 KB)
- â¸ï¸ **Pendiente**: instalar cs3 en dispositivo y abrir One Piece â€” la carga de episodios ya no debe morir a los 120s.
- âœ… **VERIFICADO en dispositivo (09 Ago 2026)**: One Piece carga **1216 episodios completos** en ~5.5 min (12:59:19 â†’ 13:05:12). Ya no hay `Timed out waiting for 120000 ms` ni cancelaciÃ³n del `load()`. Quedan timeouts OkHttp puntuales por peticiÃ³n (vISRWU6Y p1, QLBXwRlT p1) que `getWithRetry` reintenta y completa. Sin Cloudflare. El tiempo es normal: 68 pÃ¡ginas API Ã— 5-30s / 12 concurrentes. `episodeCache` en memoria (solo por sesiÃ³n).

### ðŸ”§ CachÃ© en disco de episodios y serie (09 Ago 2026)
- **Motivo**: One Piece tarda ~5.5 min en cargar los 1216 episodios (68 pÃ¡ginas API). Con solo cachÃ© en memoria, re-abrir la serie dentro de la misma sesiÃ³n es rÃ¡pido pero tras reiniciar la app se re-descarga todo.
- **Implementado** en `UniquestreamProvider.kt`:
  - Directorio `context.filesDir/uniquestream_cache/` con archivos `season_{id}.json` (lista procesada de `EpisodeItem`) y `series_{id}.json` (raw JSON de `/series/{id}`).
  - `readSeasonCache`/`writeSeasonCache` y `readSeriesCache`/`writeSeriesCache` â€” todo en `Dispatchers.IO`, TTL **24h** (`CACHE_TTL_MS`).
  - `loadSeasonEpisodes()`: primero cachÃ© en memoria â†’ disco â†’ API; al completar escribe en disco.
  - `load()`: serie leÃ­da de memoria â†’ disco â†’ API; al completar escribe en disco.
  - `AppUtils.toJson`/`parseJson` (Jackson, mismas data classes que usa el provider). Nota: `toJson` es extensiÃ³n `Any.toJson()` â€” requiere `import com.lagradost.cloudstream3.utils.AppUtils.toJson`.
- **No afecta**: reproducciÃ³n (`loadLinks`), interceptor de video ni key derivation â€” solo el catÃ¡logo de episodios.
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` â†’ `UniquestreamProvider/build/UniquestreamProvider.cs3` (79 KB)
- â¸ï¸ **Pendiente**: instalar cs3 y verificar que re-abrir One Piece tras reiniciar la app carga los 1216 eps desde disco (rÃ¡pido, sin 68 peticiones).

---

## AnimeAV1 â€” Fix error HLS 2004 (05 Ago 2026) â€” player.zilla-networks.com

### âœ… DiagnÃ³stico (verificado con Python desde PC)
- El embed HLS de animeav1.com entrega `https://player.zilla-networks.com/play/{32-hex}` â€” es una **pÃ¡gina HTML** (Vite+JWPlayer, 998 B), NO un m3u8. El JS del player (`/assets/index-b0y5A--O.js`) construye `file: "https://player.zilla-networks.com/m3u8/" + hash` y lo pasa a JWPlayer.
- **Mapeo de rutas**:
  - `/play/{hash}` â†’ 403 sin Referer; 200 con Referer pero HTML (SPA) â†’ alimentar a ExoPlayer daba `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)` o parse error.
  - `/m3u8/{hash}` â†’ **200 sin headers**, m3u8 VOD fMP4: `#EXT-X-MAP:URI=".../segs/{hash}/init.html"`, segmentos `000.html`, `001.html`, `#EXT-X-PLAYLIST-TYPE:VOD`.
  - `/segs/{hash}/{n}.html` â†’ **403 challenge de Cloudflare** ("Attention Required!") con UA solitario o incluso con Referer.
- **Clave**: los segmentos pasan a **200** con set completo de headers de navegador (Accept `*/*`, Accept-Language, Origin+Referer `player.zilla-networks.com`, Sec-Fetch-Dest/Mode/Site, Priority). Funciona incluso con UA `ExoPlayerLib/2.19.1` y sin sec-ch-ua. No se guardan cookies en el flujo playâ†’m3u8 (challenge es de headers, no de sesiÃ³n).

### Fix implementado en `Animeav1Provider.kt`
1. **`loadCustomExtractor`** (`Animeav1Provider.kt:376`): si la URL es `player.zilla-networks.com/play/{hash}`, transforma a `/m3u8/{hash}` y emite `newExtractorLink` con `type=M3U8`, `quality=P1080`, `referer=https://player.zilla-networks.com/` y `headers=zillaHeaders`. Evita que ExoPlayer reciba la pÃ¡gina HTML.
2. **`getVideoInterceptor`** (`Animeav1Provider.kt:74`): interceptor OkHttp que inyecta `zillaHeaders` a TODA peticiÃ³n a `player.zilla-networks.com` (master + segmentos + init.html). Es el mecanismo probado en el repo (mismo patrÃ³n que UniqueStream).
3. `zillaHeaders` (`Animeav1Provider.kt:63`): Accept, Accept-Language, Origin, Referer, Sec-Fetch-*, Priority.

### Estado
- CompilaciÃ³n OK: `.\gradlew.bat :Animeav1Provider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:Animeav1Provider:make` â†’ `Animeav1Provider/build/Animeav1Provider.cs3` (27 KB)
- â¸ï¸ **Pendiente**: instalar cs3 en dispositivo y probar Fullmetal Alchemist: Brotherhood EP1 (link "Subtitulado: HLS") â€” deberÃ­a cargar master + segmentos sin 403. Si Cloudflare aun bloquea desde IP mÃ³vil, revisar si requiere sec-ch-ua/UA de navegador real en vez de ExoPlayerLib.

### Scripts de verificaciÃ³n (`%TEMP%\opencode\`)
- `check_av1.py`, `check_zilla.py`, `check_zilla2.py`, `check_js.py`, `check_js2.py`, `check_m3u8.py` (mapeo /playâ†’/m3u8â†’/segs)
- `check_segs.py`, `check_segs2.py`, `check_403.py` (403 Cloudflare con headers parciales)
- `check_fullhdr.py` (**headers completos â†’ 200**), `check_cookieflow.py` (no hay cookies), `check_exo.py` (UA ExoPlayer + full headers â†’ 200)

## PlushdProvider (PlusHD) â€” Fix pelÃ­culas congeladas (07 Ago 2026)

### âœ… Resuelto: pelÃ­culas ya no se congelan
- **SÃ­ntoma**: las series reproducÃ­an fluido, pero las **pelÃ­culas** (vidhideplus.com) se congelaban cada ~5s.
- **Causa raÃ­z (2 bugs)**:
  1. **Desempaquetador roto** en `tryVidHideExtraction` (`PlushdProvider.kt:~521`): buscaba el primer `eval(` de la pÃ¡gina (que es el de **publicidad**, con `}\('` escapado) y luego `html.indexOf("}('")`. Como el eval real del player tiene `}('` pero aparece DESPUÃ‰S del de ads, `callStart` daba -1 â†’ desempaquetado fallaba siempre â†’ el link lo emitÃ­a `loadExtractor` (extractor core de CloudStream).
  2. **Extractor core devuelve hls3** (`master.txt` â†’ segmentos `.woff2` en `breezewoodcreativeworks.cfd`), y el `getVideoInterceptor` solo inyectaba headers a `.m3u8`/`.ts` â†’ los `.woff2` iban sin Referer â†’ **403 Cloudflare â†’ freeze cada ~5s**.
- **Fix implementado**:
  - `tryVidHideExtraction` reescrito con regex robusta que encuentra el eval correcto: `}[(]'(.*?)',(\d+),(\d+),'(.*?)'[.]split` (DOT_MATCHES_ALL), itera todos los matches y desempaqueta hasta hallar un `.m3u8`. El m3u8 extraÃ­do es **hls2** (`dramiyos-cdn.com`, segmentos `.ts`) que funciona **sin headers** (verificado 200 en master/variante/segmentos).
  - `getVideoInterceptor` gate ampliado: ahora tambiÃ©n aÃ±ade UA/Referer/Origin a URLs `.woff2` y `.txt` (cubre hls3 por si se usa).
- **Verificado en PC (Python)**:
  - hls2 (`dramiyos-cdn.com`) â†’ master/variante/850 segmentos `.ts` â†’ **200 sin headers**, Content-Type `video/MP2T`, syncTS OK.
  - hls3 (`breezewoodcreativeworks.cfd`) â†’ segmentos `.woff2` â†’ **403 sin Referer**, 404 con UA ExoPlayer, **200 solo con UA + Referer vidhideplus**.
  - El player JS real usa `links.hls4||links.hls3||links.hls2` â†’ cae en hls3, pero nuestro extractor prefiere hls2 (mÃ¡s simple, no necesita headers).

### ðŸ”§ Fix regex eval (07 Ago v2) â€” `\(`/`\.` rechazados por ICU de Android
- **SÃ­ntoma**: las **series** reproducÃ­an fluido pero las **pelÃ­culas** seguÃ­an congelÃ¡ndose pese al fix anterior.
- **DiagnÃ³stico (logcat `PlushdProvider-VidHide`)**: `Error: Syntax error in regexp pattern near index 1` apuntando a `\(` en el `evalRegex`. El motor regex de Android (libcore/ICU) **rechaza** `\(`/`\.` en el patrÃ³n `}\('(.*?)'...` aunque JVM (Java estÃ¡ndar) lo compile OK â€” por eso el test en PC no lo detectÃ³.
- **Flujo real por tipo**:
  - **Series**: el m3u8 directo aparece en el HTML â†’ `m3u8Regex` (lÃ­nea 521) lo encuentra ANTES de llegar al eval â†’ `tryVidHideExtraction` emite hls2 â†’ fluido. Nunca se tocaba el evalRegex.
  - **PelÃ­culas**: NO hay m3u8 directo â†’ se llega al `evalRegex` (lÃ­nea 526) â†’ excepciÃ³n de compilaciÃ³n â†’ `tryVidHideExtraction` retorna `false` â†’ el link lo emitÃ­a `loadExtractor` (extractor core) â†’ **hls3 `.woff2`** â†’ freeze.
- **Fix**: `}\('(.*?)'` â†’ `}[(]'(.*?)'` y `\.split` â†’ `[.]split` (clases de caracteres, sin backslash-escapes ambiguos). SemÃ¡ntica idÃ©ntica.
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (35.3 KB)

### ðŸ”§ Fix regex eval (07 Ago v3) â€” el `}` inicial es invÃ¡lido en ICU
- **SÃ­ntoma**: tras el fix v2 (con `[(]`), las pelÃ­culas seguÃ­an congeladas. El logcat seguÃ­a mostrando `Error: Syntax error in regexp pattern near index 1` apuntando al `[` (posiciÃ³n 1), es decir al carÃ¡cter INMEDIATO despuÃ©s del `}` inicial.
- **Causa raÃ­z real**: el motor regex de Android (libcore/ICU) trata el `}` suelto como cierre de cuantificador `{n,m}`; un `}` sin `{` previo es **error de sintaxis siempre**, sin importar quÃ© escape/clase venga despuÃ©s. El problema no era `\(` ni `[(]`, era el **ancla `}` del inicio** del patrÃ³n. Por eso el error siempre apunta a index 1 (el char tras el `}`).
- **Fix**: quitar el ancla `}` del patrÃ³n: `"""[(]'(.*?)',(\d+),(\d+),'(.*?)'[.]split"""`. Sin el `}` no hay error de compilaciÃ³n y el regex igual encuentra el eval correcto (verificado en PC: 1 match, a=36 c=602 â†’ m3u8 hls2 `dramiyos-cdn.com`). El bucle ya itera todos los matches y solo acepta el que desempaqueta a un `.m3u8` (los evals de publicidad no producen m3u8 y se descartan).
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (35.3 KB)

### ðŸ”§ Fix referer segmentos (07 Ago v4) â€” el CDN puede rechazar referer tioplus
- **SÃ­ntoma**: el plugin ya extrae el m3u8 hls2 correcto (`acek-cdn.com`) tras el fix v3, pero la pelÃ­cula seguÃ­a congelÃ¡ndose.
- **DiagnÃ³stico (PC)**: el CDN `{sub}.acek-cdn.com` responde **200** a master/variante/segmentos `.ts` (Content-Type `video/MP2T`, sync byte `0x47` ok) pero con **latencia alta e inestable** (3-12s por request, rate-limiting por IP en tandas). AdemÃ¡s, con `Referer=https://tioplus.app` los segmentos daban ERR, mientras `Referer=https://vidhideplus.com` â†’ 200.
- **Fix**: en `tryVidHideExtraction` el `ExtractorLink` ahora usa `referer = vidReferer` (el URL vidhideplus) en lugar de `mainUrl`. El `getVideoInterceptor` ahora usa `Origin = extractorLink.referer` (en vez de `mainUrl`), igual que un navegador.
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (35.4 KB)
- â¸ï¸ **Sospecha residual**: el CDN parece lento/inestable por sÃ­ mismo (timeouts desde PC incluso sin headers). Si persiste el freeze, es problema del servidor/CDN, no del provider.

### ðŸ”§ Fix referer condicional (07 Ago v5) â€” revertir cambio global que ralentizÃ³ series
- **SÃ­ntoma**: tras v4 (referer/Origin = extractorLink.referer global), las **series tambiÃ©n** empezaron a tardar en reproducir.
- **Causa**: el v4 cambiÃ³ `Origin` del interceptor a `extractorLink.referer` para TODOS los links. Las series (dramiyos-cdn) funcionaban con `Origin=mainUrl` (tioplus) y el cambio global les afectÃ³.
- **Fix**: 
  - Interceptor vuelve a `Origin = mainUrl` (global, como v3).
  - El `ExtractorLink` solo usa `referer = vidReferer` si el host del m3u8 es `acek-cdn` (pelÃ­culas vidhide); si no (dramiyos-cdn, series), usa `mainUrl`.
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (35.5 KB)
- â¸ï¸ **Pendiente**: probar en dispositivo que las series vuelven a reproducir fluido y ver si la pelÃ­cula mejora algo.

### ðŸ” Veredicto final (07 Ago v6) â€” el congelamiento es del CDN, no del provider
- **SÃ­ntoma**: con v5 instalado, el usuario reporta congelamiento **tanto en pelÃ­culas como en series**.
- **Logs del dispositivo (v5)** confirman que la **extracciÃ³n funciona perfectamente**: eval desempaquetado OK (a=36 c=602), m3u8 hls2 `{sub}.acek-cdn.com`, `linksFound=1`. El interceptor entrega master/variante con headers correctos.
- **Medido en PC (mismo master/variante/segmentos que ve el dispositivo)**:
  | Request | Status | Tiempo |
  |---------|--------|--------|
  | Master m3u8 | 200 | 3.6s |
  | Variante | 200 | 4.7s |
  | Segmento `.ts` | 200 (sync `0x47` OK) | 6-11s+ |
  | 2Âª tanda de segmentos | ERR/timeout | 20-60s |
- **Causa raÃ­z**: cada segmento contiene ~10s de video pero tarda **mÃ¡s** en descargar que su duraciÃ³n â†’ ExoPlayer agota el buffer â†’ freeze cada ~5s. AdemÃ¡s el CDN hace **rate-limiting por IP** (2Âª tandas fallaban incluso sin headers).
- **ConclusiÃ³n**: el plugin ya hace todo lo posible (extrae el m3u8 correcto, headers correctos, segmentos devuelven 200 con TS vÃ¡lido). El cuello de botella es el CDN `{sub}.acek-cdn.com` del servidor (lento/saturado) â€” **fuera de nuestro alcance**.
- **Sugerencias al usuario**: probar otra pelÃ­cula/serie (el rate-limit es por IP y momento), probar en otro horario, o comparar si el sitio web en navegador reproduce fluido en la misma red.
- Estado final del plugin: **v5 (35.5 KB)** es la versiÃ³n estable correcta â€” no hacer mÃ¡s cambios de cÃ³digo para este sÃ­ntoma.

### Scripts de verificaciÃ³n (`%TEMP%\opencode\`)
- `plus_unpack4.py` (desempaquetado completo del eval vidhide), `plus_regex_test.py` (validaciÃ³n del regex Kotlin â†’ encuentra eval correcto a=36 c=602), `plus_player_flow.py` (mapeo pelÃ­culaâ†’player pageâ†’vidhideplus), `plus_cdn_check*.py` (masterâ†’varianteâ†’segmentos), `plus_seg_hdr.py` (headers segmentos hls2 vs hls3), `plus_hls3*.py` (master.txt hls3)

---

## PlushdProvider â€” Variantes mÃºltiples CDN (04 Sep 2026 v4)

### âœ… Implementado
- **Motivo**: al igual que SoloLatino, PlusHD ahora emite las variantes `hls2/hls3/hls4` del script `var links={...}` de vidhide como links separados (`VidHidePro - hls2/hls3/hls4`), para poder elegir un CDN alternativo si uno se congela (acek-cdn es lento/saturado).
- **Probe con validaciÃ³n real (NO solo HTTP 200)**: cada variante se probÃ³ leyendo el body â€” solo se emiten las que responden **200 y cuyo body empieza con `#EXTM3U`** (playlist HLS real). Esto descarta CDNs que devuelven 200 con contenido no-HLS (causa del `ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED (3003)` en hls3 `master.txt` / `ecommercesolution.sbs`).
- **Fallback**: si ninguna variante pasa el probe, se emiten todas (como antes).
- **Refactor `processServer`**: el bucle de servidores player-path (tioplus `/player/`) se extrajo a una funciÃ³n local reutilizable. La 2Âª pasada de reintento (cuando `foundLinks==0`) ya NO solo repite servidores DIRECTOS â€” ahora llama `processServer` para **TODOS** los servidores con referer `$mainUrl/`, arreglando el bug de "primer play â†’ Enlaces no encontrados, segundo play â†’ carga" (la 1Âª pasada a veces devuelve videoUrl en blanco en tioplus).
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (44.6 KB)
- `plugins.json`: PlusHD v3 â†’ sin cambio de version (sigue 3), fileSize 41424 â†’ 44619

### â¸ï¸ Pendiente
- Probar en dispositivo: (1) que hls3 ya no se emite si falla el probe `#EXTM3U`; (2) que el primer play ya no da "Enlaces no encontrados" (reintento 2Âª pasada con todos los servidores).

---

## PlushdProvider â€” CRITICAL Fix interceptor + hls3 3003 (04 Sep 2026 v4.1)

### ðŸ› Causa raÃ­z del 3003 y del congelamiento (verificado)
- **El interceptor NUNCA se llamaba**: CloudStream resuelve el interceptor por **`getApiFromNameNull(link.source)`** (CS3IPlayer.kt:1940-1941) â€” solo si `link.source` == nombre exacto del provider (`"PlusHD"`). Plushd emitÃ­a `newExtractorLink("VidHide", ...)` en `tryVidHideExtraction` (2 call-sites) â†’ `getApiFromNameNull` retorna null â†’ **sin interceptor** â†’ los segmentos `.ts`/`.woff2` iban sin UA/Referer â†’ CDN cortaba.
- **hls3 = 3003**: los segmentos son `seg-N-f1-v1-a1.woff2` (rutas relativas en el variant) y el master/variante son `.txt` (no `.m3u8`). El gate viejo `if (!url.contains(".m3u8") && !url.contains(".ts"))` los excluÃ­a â†’ sin headers â†’ 403 (`text/plain`) / 404 (`text/html`) â†’ ExoPlayer `PARSING_CONTAINER_UNSUPPORTED (3003)`.
- **Verificado con Python (PC)** desde el embed `zhc8nmz6aurw`:
  | Request hls3 | Resultado |
  |---|---|
  | segmento `.woff2` sin headers | **403** `text/plain` |
  | solo UA | **404** `text/html` |
  | UA + Referer `vidhideplus.com` | **200** `video/MP2T`, sync `0x47` OK, 701240 B |
  | master `.txt` | 200 `application/vnd.apple.mpegurl`, 11 lÃ­neas, 3 variantes |
  | variant `index-f1-v1-a1.txt` | 200, 136 segmentos |
- **hls2 (acek-cdn)**: master/variante/segmentos `.ts` con token en URL â€” 200 ok, pero CDN **lento** (veredicto previo: < bitrate â†’ freeze cada ~3s). El interceptor ahora inyecta headers tambiÃ©n aquÃ­.

### âœ… Fix implementado (PlushdProvider.kt)
1. **`source = "PlusHD"`** en los 2 `newExtractorLink` de vidhide (`VidHidePro - hlsX` y fallback `VidHide`) â€” `getApiFromNameNull` ahora encuentra el provider y llama `getVideoInterceptor`.
2. **`wrappedCallback` preserva el referer**: `this.referer = link.referer ?: data` (antes forzaba `data` = tioplus â†’ los CDN vidhide necesitan referer `vidhideplus.com`).
3. **Interceptor ampliado**: `isPlaylist = .m3u8 || .txt || .urlset`; inyecta UA/Referer/Origin a **todo** lo que pase (`.txt`, `.woff2`, `.ts`, `.m3u8`). El peek Cloudflare + filtro 1080p solo se hace en playlists.
- CompilaciÃ³n OK: `.\gradlew.bat :PlushdProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:PlushdProvider:make` â†’ `PlushdProvider/build/PlushdProvider.cs3` (44.7 KB)
- `plugins.json`: fileSize 44619 â†’ 44712 (version 3)

### â¸ï¸ Pendiente
- Probar en dispositivo: (1) hls3 ya no debe dar 3003 (segmentos `.woff2` con headers); (2) hls2 puede mejorar el freeze (interceptor activo); (3) verificar en logs `[intercept]` o ausencia de 403 en `.woff2`.

---

## PlushdProvider â€” Veredicto final: throttle por IP del CDN vidhide (04 Sep 2026 v4.2)

### âœ… Interceptor arreglado (confirmado en logs del dispositivo)
- El fix v4.1 funcionÃ³: `getVideoInterceptor` SÃ corre (`M3U8 filtered: 1739 -> 1344`, `Filtering out 1080p`), `tryVidHideExtraction` emite 2 variantes y el probe es correcto (`probe hls3 -> 200 (749B)`, `probe hls2 -> 200 (1739B)`).
- `preferOrder` cambiado a **`hls3 > hls2 > hls4`** (PlushdProvider.kt:536) â€” hls3 (businessgrowthhacks) auto-primero.
- `plugins.json`: fileSize 44712 (version 3) â€” sin cambios de bytes (solo orden de const).

### ðŸ› El 3003/hls3 y el freeze/hls2 tienen la MISMA causa raÃ­z: **throttle por IP del CDN**
- Probado en PC (script `plus_hls3_seq.py`) sobre el embed real `zhc8nmz6aurw`:
  | Request | Resultado |
  |---|---|
  | hls3 master `.txt` / variante `.txt` | 200 `application/vnd.apple.mpegurl` |
  | hls3 **seg-1** `.woff2` (con UA+Referer, Â±Origin, Â±cookies) | **200 `video/MP2T` sync `0x47` en 0.6s** (701240 B) |
  | hls3 **seg-2 en adelante** | `IncompleteRead` (62-191 KB de ~700 KB), **HTTP 520**, read-timeout |
  | embed page `vidhideplus.com/v/...` | no setea cookies (jar vacÃ­o) â†’ las cookies no son la soluciÃ³n |
- **InterpretaciÃ³n**: el CDN permite ~700-900 KB de burst por IP y luego corta/trunca TODAS las conexiones durante un cooldown (decenas de segundos). Coincide con el patrÃ³n en el dispositivo:
  - **hls3 â†’ 3003**: seg2 llega truncado (TS incompleto) â†’ ExoPlayer `PARSING_CONTAINER_UNSUPPORTED`.
  - **hls2 â†’ freeze ~4-5s + tarda ~60s en volver**: `SocketException: Socket closed` en `chain.proceed` (PlushdProvider.kt:250) cada ~54s + master re-fetch.
- Ambos CDNs (`acek-cdn` y `businessgrowthhacks`) pertenecen a la misma cuenta `m5QqjwpATPzb.*` â†’ mismo throttle.
- **Veredicto**: no es arreglable desde el provider (ni headers, ni cookies, ni preferOrder, ni cambio de variante). Los 4 servidores SPA (`strp2p/upns/4meplayer/rpmstream`) dan `error 2001` (requieren JS/WebView) y `turbovid` da 3003. El cuello de botella es el servidor/CDN de vidhide.

### ðŸ“Œ DecisiÃ³n (usuario, 04 Sep 2026)
- **Dejarlo como estÃ¡** â€” no mÃ¡s cambios de cÃ³digo para este sÃ­ntoma. Si se quiere probar el camino WebView para los SPA (por si usan otro CDN), es esfuerzo alto y resultado incierto.

---

## UniqueStreamProvider â€” Fix pelÃ­culas standalone (12 Ago 2026)

### ðŸ› Bug corregido
- **SÃ­ntoma**: las pelÃ­culas standalone (no dentro de una serie) no cargaban informaciÃ³n â€” salÃ­a error.
- **Causa raÃ­z (verificado con curl)**:
  - `load()` solo llamaba `GET /api/v1/series/{id}` â†’ **404 para TODAS las pelÃ­culas** (ej. `dv_575811` The Garden of Words, `stWMCKHO` Shinobi Girl: The Movie).
  - `loadLinks()` solo llamaba `GET /api/v1/episode/{id}/media/dash/{locale}` â†’ 404 para pelÃ­culas.
  - El search API devuelve `movies[]` (con `type:"movie"`) pero el plugin solo parseaba `series[]`.

### Endpoints correctos (verificados)
| Endpoint | Uso | Status |
|---|---|---|
| `GET /api/v1/content/{id}` â†’ `content_type:"movie"` | Detectar tipo | âœ… 200 movies, 404 series |
| `GET /api/v1/movie/{id}` | Detalle pelÃ­cula (title, images, duration_ms, year, studio, rating) | âœ… 200 |
| `GET /api/v1/movie/{id}/media/dash/{locale}` | Playback master de pelÃ­cula | âœ… 200 |
| `GET /api/v1/series/{id}` | Detalle serie | âœ… 200 series, 404 movies |
| `GET /api/v1/episode/{id}/media/dash/{locale}` | Playback de episodio | âœ… 200 |
| `GET /api/v1/search?query=` â†’ `movies[]` + `series[]` | BÃºsqueda | âœ… 200 |

### Fix implementado en `UniquestreamProvider.kt`
1. **DetecciÃ³n de pelÃ­culas**: set `movieIds` poblado en `toSearchResponse()` (main page + search) y `probeContentType()` que hace `GET /content/{id}` como respaldo (si `content_type` presente â†’ movie).
2. **`load()`**: si es movie â†’ `loadMovie()` usa `GET /movie/{id}`, devuelve `newMovieLoadResponse(TvType.AnimeMovie)` con poster, plot (desc + aÃ±o/estudio), duration, year, score. Sin episodios (es pelÃ­cula).
3. **`loadLinks()`**: si es movie â†’ `GET /movie/{id}/media/dash/{locale}`; si no â†’ `/episode/` (flujo anterior intacto).
4. **`search()`**: ahora parsea `series[]` + `movies[]` (antes solo `series[]`).
5. Master de movie: `get3.mediacache.cc/movie/{id}/{media_id}_{locale}/master.m3u8` â€” mismo `keyRegex`/interceptor de key (media_id corto tipo `575811` ya soportado).

### Estado
- CompilaciÃ³n OK: `.\gradlew.bat :UniquestreamProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:UniquestreamProvider:make` â†’ `UniquestreamProvider/build/UniquestreamProvider.cs3` (89 KB)
- â¸ï¸ **Pendiente**: instalar cs3 en dispositivo y probar The Garden of Words (`dv_575811`) â€” detalle + reproducciÃ³n ja-JP/es-419.

## UniqueStreamProvider â€” DiagnÃ³stico freeze CDN (12 Ago 2026)
- **SÃ­ntoma**: EP8 de Fate/strange Fake (ja-JP, content_id `kmRlqThD`) congelaba cada ~5s; pausa+espera solo fluido por unos minutos.
- **Medido desde PC (master/variante/segmentos reales de `get3.mediacache.cc`)**:
  - Solo **1 variante 1080p** (avg 5.5 Mbps). No hay 720p/480p (paths `v_1280x720` etc â†’ 403, sign ligado a resoluciÃ³n; API ignora `?quality=`).
  - Segmentos ~3.8s de duraciÃ³n tardan 1.8â†’10.4s en descargar. Sostenido **4.5 Mbps**, y con headers completos/keep-alive **3.3-4.4 Mbps**; paralelo 2 conexiones 2.6 Mbps.
  - **ConclusiÃ³n**: CDN throttleado por IP (igual veredicto que acek-cdn en Plushd). No hay fix de cÃ³digo â€” solo variante 1080p y CDN < bitrate. No confundir con cambios de catÃ¡logo (0.1.0-0.1.2) que NO tocan reproducciÃ³n.
- AnimeOnsen emite el mismo patrÃ³n (1 sola calidad) PERO su CDN entrega > bitrate â†’ pausa llena buffer y se mantiene fluido. No hay feature que copiar; es ancho de banda.
- Scripts: `fsf_speed.py`, `fsf_speed2.py`, `fsf_parallel.py`, `fsf_headers.py` en `%TEMP%\opencode\`.

## SyncPlugin (CloudStream Sync) â€” SincronizaciÃ³n entre dispositivos (13 Ago 2026)

### Â¿QuÃ© hace?
- Sincroniza datos de CloudStream entre dispositivos usando un **Proyecto de GitHub (ProjectV2)** sin servidor propio.
- Cada dispositivo se guarda como un **DraftIssue** del proyecto; su body es un JSON comprimido (GZIP+Base64) con el backup.
- 5 categorÃ­as: `bookmarks` (favoritos), `resume` (progreso de reproducciÃ³n), `search_history`, `extensions_repos`, `settings`.

### Archivos (SyncPlugin/src/main/kotlin/com/example/)
| Archivo | Rol |
|---------|-----|
| `SyncPlugin.kt` | Orquestador: listeners de prefs + `bookmarksUpdatedEvent`, polling 30s, `runSync` (restore desde el dispositivo con `updatedAt` mÃ¡s reciente + push si hash cambiÃ³), debounce 2s por cambio |
| `SyncBackup.kt` | Construye el backup (datastore + settings SharedPreferences), clasifica claves en categorÃ­as, MD5 hash, merge por timestamps, filtrado por categorÃ­a |
| `SyncNetwork.kt` | GraphQL de GitHub (fetchProjectId, fetchDevices, registerDevice, updateDevice), GZIP+Base64 compress, `getDeviceId` = MD5(packageName+ANDROID_ID) |
| `SyncStorage.kt` | Persistencia de token/nÃºmeros/proyecto/deviceId vÃ­a `AcraApplication.getKey/setKey` |
| `SyncSettings.kt` | UI programÃ¡tica (AlertDialog): token, nÃºmero de proyecto, checkboxes backup/restore por categorÃ­a, botÃ³n "Guardar y sincronizar" |
| `SyncData.kt` | Modelos serializables (BackupFile, BackupVars, SyncDevice, GitHubGraphQLResponse...) |

### Pontos clave del diseÃ±o
- `isBackupEnabled`/`isRestoreEnabled` por categorÃ­a (flags "true"/"false" en AcraApplication keys).
- Restore: coge el DraftIssue de OTRO dispositivo (`deviceId != propio`) con `updatedAt` mÃ¡s alto, hace merge por categorÃ­a comparando `categoryTimestamp` local vs `updatedAt` en epoch. `isRestoring=true` silencia los listeners durante el restore.
- Push: solo si `computeHash(data) != lastPushedHash`.
- `nonTransferableKeys` excluye cuentas/logins de terceros, rutas de descarga, keys propias del plugin, etc.
- Resume watching vÃ­a `HomeViewModel.getResumeWatching()` (nullable â†’ `?.also`) con cache.

### ðŸ”§ Fixes de compilaciÃ³n (13 Ago 2026)
- **`DataStore.getSharedPrefs`/`getDefaultSharedPrefs` NO resuelven** en el compilador del plugin (el jar `jetified-cloudstream.jar` SÃ tiene los mÃ©todos JVM pero el metadata Kotlin `mv=[2,2,0]`/`xi=48` no los expone). **Fix**: acceso directo a los ficheros (verificado por bytecode):
  - datastore â†’ `context.getSharedPreferences("rebuild_preference", Context.MODE_PRIVATE)` (el host usa exactamente ese nombre, confirmado con `javap -c`)
  - settings â†’ `context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)` (= `PreferenceManager.getDefaultSharedPreferences`)
- `HomeViewModel.getResumeWatching()` devuelve `List<ResumeWatchingResult>?` â†’ handle con `?.also` + cachÃ©.
- `forceSync` no es suspend (lanza `scope.launch` internamente) â†’ se puede llamar desde `setOnClickListener`.

### ðŸ”§ CRITICAL: NO usar kotlinx-serialization en plugins (13 Ago 2026)
- **SÃ­ntoma**: al instalar el plugin, CloudStream entra en "Modo seguro ACTIVADO" y el plugin crashea con:
  `java.lang.AbstractMethodError: abstract method ... GeneratedSerializer.typeParametersSerializers()`
  en `com.example.GitHubGraphQLError$$serializer` durante `decodeFromString`.
- **Causa**: el plugin se compila contra kotlinx-serialization **1.11** (transitiva de cloudstream3-pre-release) pero el **APK runtime** del usuario usa una versiÃ³n **mÃ¡s antigua** (sin `typeParametersSerializers()` en `GeneratedSerializer`). El metadata del plugin API jar NO empaqueta kotlinx-serialization; el mismatch de versiones es fatal en runtime (modo seguro).
- **Fix definitivo**: migrar TODA la serializaciÃ³n a **Jackson vÃ­a `AppUtils`** (el patrÃ³n que ya usa `UniquestreamProvider` y funciona en dispositivo):
  - `AppUtils.parseJson<T>(string)` â†’ deserializa (TypeReference reified)
  - `toPush.toJson()` â†’ serializa (extensiÃ³n `Any.toJson()`, importar `com.lagradost.cloudstream3.utils.AppUtils.toJson`)
  - Data classes **sin `@Serializable`**; usar `com.fasterxml.jackson.annotation.JsonProperty` para nombres como `__typename`, `addProjectV2DraftIssue`, `updateProjectV2DraftIssue`.
  - El mapper host (`MainAPIKt.getMapper()`) ya registra el mÃ³dulo Kotlin (`kotlinModule`), confirmado por bytecode.
- **Regla para futuros plugins en este repo**: compilar JSON con Jackson + `AppUtils`, NUNCA kotlinx-serialization (mismatch de versiones garantiza crash).

### ðŸ”§ UI SyncSettings (13 Ago 2026)
- **Bug 1**: forzar `setTextColor` con `resolveColor(context, textColorPrimary)` daba negro sobre fondo oscuro (attributos de tema resueltos como resource no son Int color directo). **Fix**: crear vistas con `activity` (NO `applicationContext`) y **no** forzar colores de texto â€” heredan el tema del diÃ¡logo.
- **Bug 2**: checkbox "Mostrar token" â†’ `tokenInput.setSelection(tokenInput.text?.length ?: 0)` (`Editable?` nullable).
- BotÃ³n: `AppCompatButton` + `ViewCompat.setBackgroundTintList(ColorStateList.valueOf(holo_blue_dark))` + texto blanco.
- Campos: `GradientDrawable` redondeado semitransparente (blanco ~10% en dark / negro ~5% en light).

### ðŸ”§ DiseÃ±o por dispositivo excluido del backup (13 Ago 2026)
- CloudStream guarda el modo de diseÃ±o (celular/TV/emulador/AutomÃ¡tico) en **`app_layout_key`** (Int, `getInt/putInt` en settings, confirmado en `SetupFragmentLayout` con `R.string.app_layout_key`).
- AÃ±adida a `nonTransferableKeys` en `SyncBackup.kt` â†’ **nunca** se sincroniza; cada dispositivo conserva su diseÃ±o aunque el backup venga de otro.

### Estado
- âœ… CompilaciÃ³n OK: `.\gradlew.bat :SyncPlugin:compileReleaseKotlin --console=plain`
- âœ… Plugin empaquetado: `:SyncPlugin:make` â†’ `SyncPlugin/build/SyncPlugin.cs3` (**48 KB**, 13 Ago 2026, con Jackson)
- â¸ï¸ **Pendiente**: reinstalar el cs3 (fix Jackson) y probar en dispositivo â€” crear el project (ProjectV2), token classic con scope, registrar un dispositivo como DraftIssue y verificar sync bidireccional.
- â¸ï¸ Pendiente: verificar que `updatedAt` entre los DraftIssues de GitHub se actualiza correctamente tras `updateDevice`.

## AnizoneProvider â€” MigraciÃ³n a items JSON en x-data (19 Ago 2026)

### ðŸ”§ Problema original
- `search()` y `getMainPage()` devolvÃ­an **vacÃ­o**: el sitio anizone.to (Livewire 3 + Alpine) migrÃ³ y los items ya NO estÃ¡n en `div[wire:key]` de la pÃ¡gina. Ahora vienen como JSON en el atributo `x-data` de un `[x-data]` del componente `pages.anime-index`.

### Nueva estructura (verificada en PC)
- Main page/search: `<div x-data="{ items: JSON.parse('...'), nextCursor: '...', hasMore: true, ..., loadMore() { $wire.loadPage(this.nextCursor) } }">` dentro del componente con `wire:snapshot` de `pages.anime-index`.
- **Decode**: el atributo HTML tiene doble escape JS+JSON (p.ej. `\\u0022`, `\\\/`). El replace naive `replace("\\u0022","\"")` **rompe tÃ­tulos con comillas internas**. Fix: `unescapeJsString()` char-por-char (JS unescape primero: `\\`â†’`\`, `\/`â†’`/`, `\uXXXX`â†’char; luego `JSONArray` termina con los escapes restantes).
- Campos del item JSON: `slug`, `url`, `cover`, `main_title` (con `"` literales), `title_list` (mapa `"1"`=EN, `"5"`=principal, `"8"`=JA), `type` ("TV Series"/"Movie"/"OVA"/"Web"), `start_year`, `episode_count`.
- Search por URL filtra server-side: `$mainUrl/anime?search=query` â†’ x-data con items (sin Livewire).

### PaginaciÃ³n
- **Main page**: mÃ©todo Livewire `loadPage` con `params=[nextCursor]` â†’ los items de la pÃ¡gina siguiente vienen en **`effects.dispatches[0].params`** (dispatch `name="items-loaded"`, keys `items`/`nextCursor`/`hasMore`), NO en `effects.html`.
- **Episodios en `load()`**: el detail usa paginator Livewire `paginators.page` (36/pÃ¡gina, Aikatsu 178 eps = 5 pÃ¡ginas). Update `{"paginators.page": "N"}` â†’ los `li[x-data]` de esa pÃ¡gina vienen en `effects.html`. El viejo `.h-12[x-intersect="$wire.loadMore()"]` ya no existe.

### ðŸ”§ Player (loadLinks) â€” cambiado a vidstackPlayer
- El `media-player` + `<track>` + `span.truncate` ya no existen. El reproductor es `<div x-data="vidstackPlayer(JSON.parse('...'))">`.
- El JSON contiene: `src` (master.m3u8), `subtitles[]` (title/format/language/default/forced/file), `storage`, `snapshot`, `storyboard`, `chapter`, `fonts`.
- Fix: regex `vidstackPlayer\(JSON\.parse\('(.*?)'\)\)` (DOT_MATCHES_ALL) + `unescapeJsString` + `JSONObject` â†’ `src` para el link, `subtitles[]` para `subtitleCallback`.
- Fuente: `div:containsOwn(Source:)` â†’ siguiente sibling (ej. "Web").
- Master CDN: `https://suzaku.xin-cdn.xyz/{uuid}/master.m3u8` â€” responde 200, variantes 360/720/1080 + audio ja.

### Estado
- âœ… CompilaciÃ³n OK: `.\gradlew.bat :AnizoneProvider:compileReleaseKotlin --console=plain -q`
- âœ… Helpers nuevos: `unescapeJsString`, `parseItemsJson`, `findItemsXData`, `extractNextCursor`, `extractHasMore`, `getItemsLoadedParams`, `toResult(JSONObject)`.
- â¸ï¸ **Pendiente**: instalar cs3 en dispositivo y probar search, main page (paginaciÃ³n multi-pÃ¡gina), load con muchos episodios (Aikatsu) y reproducciÃ³n + subtÃ­tulos.

---

## ReanimeProvider â€” Estado (23 Ago 2026)

### Arquitectura del sitio (reanime.to)
- SvelteKit SPA, catÃ¡logo basado en AniList (covers de s4.anilist.co)
- APIs JSON limpias:
  - `GET /api/v1/search?q=` â†’ `{results:[{anime_id, anilist_id, title{english}, cover_image{large}}]}`
  - `GET /api/v1/anime/{slug}` â†’ metadata completa (anilist_id, description, genres, average_score 0-100, status)
  - `GET /api/v1/anime/{slug}/episodes?limit=2000` â†’ `{data:[{episode_number, title, subbed, dubbed}]}`
- **Streams**: `GET /api/flix/{anilist_id}/{episode}` â†’ `{success, servers:[{serverName:"HD-1"/"HD-2", dataLink:"https://flixcloud.cc/e/{hash}?v=N"}]}`
  - Preferencia del sitio: HD-2 â†’ HD-1 â†’ primero. v=1=sub v=2=dub aprox
  - Variante TMDB: `/api/flix/0/{ep}?tmdb={id}&season={n}`

### Cadena de descifrado flixcloud (REVERSED y validado end-to-end)
1. GET embed `flixcloud.cc/e/{hash}?v=N` con UA+Referer â†’ HTML contiene estado SvelteKit serializado
2. Extraer: `obfuscation_seed`, `w_payload` (WASM b64), bloque `obfuscated_crypto_data` (kf_/ivf_), tokenField, keyFrag2Field
3. **Field mapping** (nombres de campos ofuscados): `e=seed; x3: e=SHA256(e+"0|1|2")`; luego segunda cadena idÃ©ntica desde e final:
   - tokenField = `${e[48:64]}_${e[56:64]}`, keyFrag2Field = `${s[0:16]}_${s[16:24]}`, kf/ivf = prefijos de la primera cadena
   - Los campos aparecen a veces CON comillas y a veces SIN (probar ambos regex)
4. `GET flixcloud.cc/api/m3u8/{token}` â†’ JSON; claves: `sha256(token+"vid")[:10]` = enc_url b64, `sha256(token+"key")[:10]` = keymat b64
5. **WASM** (~350B, constantes/ops RANDOMIZADAS por carga): `_s(seedInt)` setea global; `_r(A,B,C,out,k)` computa por byte mezcla de A^B^C con adds/xors/shs y `(global + i*M)&255`; out=P (32B). `_c()` devuelve PK=mem[2064..2096]
   - SOLUCIÃ“N: mini-intÃ©rprete WASM embebido en el provider (clase MiniWasm) â€” parsea secciones export/code/DATA y ejecuta el subset de opcodes. Validado contra wasmtime con inputs aleatorios en mÃºltiples payloads
   - IMPORTANTE: escribir A/B/C en mem[1000/1032/1064] ANTES de _r (offsets como el JS: C=1e3,I=C+k,q=I+k,et=q+k). La data-section inicializa mem[2000..2064] con 64 bytes = clave PK (PK=data[0:32]^data[32:64])
6. `PBKDF2-HMAC-SHA256(P, salt=seed_utf8, 1000 iter, 32B)` â†’ XOR byte a byte con seed â†’ SHA-256 = **clave AES-256-CBC**
7. `AES-CBC-decrypt(clave, iv=ivf_b64, ct=enc_url)` â†’ **master.m3u8 URL** (JWT con client_ip binding)
8. GET master con header Referer `https://flixcloud.cc/` â†’ body es BASE64; decodificar â†’ si no empieza con #EXTM3U â†’ XOR con PK â†’ playlist real

### Detalles crÃ­ticos descubiertos
- **El body de los playlists viene cifrado (b64+XOR con PK)**: el hls.js parcheado de flixcloud (`/artplayer-new/hls.js?v=103`) lo descifra en onSuccess leyendo `window.__pk` (generado por `_c()`). Implementado en `getVideoInterceptor`: probar cada PK activo hasta que el decode empiece con #EXTM3U
- El cifrado del playlist es OPCIONAL por archivo (One Piece E1 llegÃ³ plano una vez, Frieren cifrado siempre) â†’ el interceptor debe manejar ambos casos
- Audio dual INTEGRADO en el HLS: `#EXT-X-MEDIA TYPE=AUDIO LANGUAGE="jpn"/"eng"` â€” ExoPlayer lo maneja nativo
- SubtÃ­tulos EXTERNOS `.ass`/`.srt` listados en el embed (`subtitles:[{url,language,format,default}]`) â†’ subtitleCallback
- PÃ¡ginas degradadas intermitentes (falta frag2/token): reintentar con carga fresca (resolveFlix reintenta 3x)
- Headers Sec-Fetch completos reducen pÃ¡ginas degradadas
- VOE/HgLink en otros sitios usan gates ALTCHA-PBKDF2/JS challenge â†’ NO resolubles sin WebView

### Archivos
- `ReanimeProvider/src/main/kotlin/com/example/ReanimeProvider.kt` â€” provider completo + MiniWasm interpreter
- CompilaciÃ³n OK: `.\gradlew.bat :ReanimeProvider:make --console=plain -q`
- â¸ï¸ Pendiente: probar en dispositivo (home/search/load/playback + subtÃ­tulos ASS)
### FIX segmentos cifrados (23 Ago 2026 v2)
- Los segmentos `seg-N-f1-v1-a0.png`/`.webp` (CDNs atomic4cdn.top/stronghole.site) traen: firma PNG falsa (8B) o RIFF-WEBP (12B) + contenido **XOR-cifrado**
- Clave XOR FIJA de 16B encontrada en el fetch-loader del hls.js parcheado (`flix_hls.js`): `[9d,2a,f1,47,b3,8e,5c,70,a6,19,e4,3b,d8,62,0f,c5]`
- LÃ³gica exacta del JS: si tras saltar la firma el byte NO es 0x47 (sync TS) â†’ XOR cÃ­clico key[i&15]
- Implementado en getVideoInterceptor (aplica a TODAS las responses del link con esas firmas)
- Verificado en PC: 100/100 sync bytes TS tras descifrar
- stronghole.site (HD-1) da 403 a todo sin sesiÃ³n de navegador â†’ HD-2 (vault-96.atomic4cdn.top) es el servidor que funciona
### FIX subtÃ­tulos con posiciÃ³n (24 Ago 2026 v7)
- SubtÃ­tulos ahora se convierten **ASSâ†’VTT** (no SRT) conservando POSICIONES:
  - [V4+ Styles] â†’ map styleNameâ†’Alignment (numpad 1-9)
  - Override `\anN` en el texto tiene prioridad sobre el estilo
  - Mapeo a WebVTT cue settings: bottom=default, middle=`line:50%`, top=`line:0`; odd/even â†’ `align:left/right`
- Emitidos como URLs falsas `flixcloud.cc/__sub/{n}.vtt` servidas por el interceptor (sin red)
- ExtensiÃ³n .vtt â†’ ExoPlayer infiere TEXT_VTT nativo
- Validado: One Piece E1 â†’ 26 cues arriba (canciones/signos), resto abajo
### FIX definitivo posiciones (24 Ago 2026 v9) â€” marcador nativo {anN}
- **CS3 tiene fixSubtitleAlignment nativo** (CustomDecoder.Companion): `locationRegex = "\{\an(\d+)\}"` busca el marcador `{anN}` EN EL TEXTO del cue â†’ setLineAnchor/setLine/setPosition segÃºn numpad SSA y ELIMINA el tag al renderizar
- Por eso AnimeParadise funcionaba sin hacer nada: sus VTT traen `{anN}` residual en el texto
- Mi conversor ahora emite el marcador: si alignment!=2 â†’ prefija texto con `{anN}` (derivado de \an override > \pos proporcional > estilo)
- Los settings line:/align: de VTT se QUITARON (CS3 los ignoraba/sobrescribÃ­a con su default)
- Prioridad conversiÃ³n: \anN > \pos(X,Y)â†’numpad por proporciÃ³n Y/PlayResY,X/1280 > estilo > 2
### FIX posters PV rotos (24 Ago 2026)
- **SÃ­ntoma**: en el home de PrimeVideo muchos posters no cargaban
- **Causa**: wsrv.nl (proxy de resize) devuelve 404 en ~50% de las imÃ¡genes â€” el fetch saliente del proxy hacia imgcdn.kim falla intermitentemente para muchas imÃ¡genes. NO es cachÃ© negativa ni rate-limit: images.weserv.nl igual (404), nonce no ayuda, statically.io bloqueado por imgcdn (403 total)
- **Origen directo imgcdn.kim: 20/20 OK** (~1.1MB promedio por poster, hasta 2.1MB)
- **Fix**: `buildVerticalPosterUrlWithProxy` vuelve a devolver la URL directa sin proxy
- Tradeoff: posters pesados otra vez, pero un poster lento es mejor que uno que no carga
- `?w=500` en imgcdn se ignora (devuelve tamaÃ±o original) â€” no hay resize nativo

## SoloLatinoProvider â€” Estado (30 Ago 2026)

### ðŸ”§ Fix fallback player-url Cloudflare 403 (30 Oct 2026)
- **Log del usuario**: Sanctum y episodio cargan (HTTP 204/200), se extraen 2 tokens; los POST nativos a `/api/player-url` responden 403 con HTML `Just a moment...`. El WebView volvÃ­a a pedir el endpoint API como navegaciÃ³n de documento, esperaba clearance y repetÃ­a idÃ©nticos POST varias veces, todos 403; por eso no obtenÃ­a JSON ni URLs para extractores.
- **Fix**: el WebView ahora calienta Cloudflare cargando la raÃ­z del sitio, luego usa la pÃ¡gina de episodio para enviar fetch same-origin. Limita el reintento JS y si recibe 403 cambia una sola vez a UA Android y recarga episodio; el segundo 403 termina el fallback en vez de repetirlo hasta seis veces. Se amplÃ­a el margen inicial del challenge a 20 s.
- **Arquitectura verificada**: el flujo sigue dependiendo de `/api/player-url` y sus tokens de servidor; el log confirma la puerta API bloqueada por Cloudflare, no un fallo de extractor.
- **CompilaciÃ³n**: pendiente por el usuario (`.\\gradlew.bat :SoloLatinoProvider:compileReleaseKotlin --console=plain -q`); no ejecutada por regla del repo.
- **Pendiente en dispositivo**: buscar `[WV] fase 0` con clearance, luego `servidores resueltos`; si persiste el 403 tras UA Android, el challenge de Cloudflare no es resoluble con este WebView desde esa red y harÃ¡ falta inspeccionar la respuesta del endpoint/sesiÃ³n del sitio.

### ðŸ”§ SoloLatino v37 â€” Cloudflare debe calentarse en `/api/player-url` (30 Oct 2026)
- **Logs del usuario, instalado como v36**: los POST nativos reciben 403; WebView pasa por la portada (`challenge=false`, `cf_clearance=true`) y carga el episodio, pero ambos `fetch('/api/player-url')` siguen recibiendo 403. No se llega a resolver ningÃºn servidor.
- **Causa**: visitar la portada y observar una cookie `cf_clearance` no demuestra que el endpoint API haya superado su challenge. El fetch same-origin no ejecuta challenge de documento y repite el 403.
- **Fix**: el WebView ahora inicia navegando directamente a `GET /api/player-url`, y vuelve a cargar ese endpoint mientras el tÃ­tulo siga siendo el challenge; solo pasa al episodio cuando termina el challenge. La cookie queda como dato diagnÃ³stico, no como condiciÃ³n para saltarse un challenge todavÃ­a visible.
- **Seguridad de logs**: sustituir impresiÃ³n de valores completos de cookies/XSRF por nombres de cookie y presencia/longitud; los valores capturados previamente son secretos de sesiÃ³n y no deben volver a pegarse en logs compartidos.
- **Versiones**: `SoloLatinoProvider/build.gradle.kts` y `plugins.json` a 37; `fileSize` pendiente de actualizar tras la compilaciÃ³n.
- **CompilaciÃ³n**: pendiente por el usuario (`.\\gradlew.bat :SoloLatinoProvider:make --console=plain -q`); no ejecutada por regla del repo.
- **Pendiente en dispositivo**: instalar v37 y revisar si la navegaciÃ³n inicial a `/api/player-url` pasa challenge y luego aparece `[WV] servidores resueltos`; si vuelve a dar 403, guardar solo status/tÃ­tulo/cookieNames (nunca valores de cookies/tokens) para el siguiente diagnÃ³stico.

## GitHub Actions â€” JVM target de YoutubeProvider (30 Oct 2026)
- **Evidencia del run**: los mÃ³dulos anteriores, incluido `SoloLatinoProvider`, completan `make`; falla `:YoutubeProvider:compileDebugKotlin` con incompatibilidad JVM: `compileDebugJavaWithJavac=11` y `compileDebugKotlin=1.8`. La falla es global y no revoca los enlaces que SoloLatino ya emitiÃ³ en el dispositivo.
- **Causa raÃ­z**: el `kotlin { compilerOptions { ... } }` de `YoutubeProvider/build.gradle.kts` estaba anidado dentro de `android {}`, por lo que no alineaba el target del task Kotlin. El root fija Kotlin en 1.8 y el mÃ³dulo fija Java en 11.
- **Fix**: mover la configuraciÃ³n `kotlin.compilerOptions` al nivel superior y fijar explÃ­citamente `KotlinJvmCompile.compilerOptions.jvmTarget=JVM_11`, alineado con Java 11.
- **DiagnÃ³stico del workflow**: `Intento N fallo (exit=0)` era engaÃ±oso porque `$?` se consultaba despuÃ©s del bloque `if`; ahora se captura el exit code en `else` antes de ejecutar `sleep`.
- **Estado**: no ejecutar Gradle segÃºn regla del repo. ValidaciÃ³n pendiente en el siguiente push de Actions; esperado que pase `:YoutubeProvider:compileDebugKotlin` y `BUILD_OK=1`.
- **Archivos**: `YoutubeProvider/build.gradle.kts`, `.github/workflows/build.yml`, `AGENTS.md`.

## SeriesKao â€” resolver episodio por API del router (30 Oct 2026 v30)
- **Log del usuario**: bÃºsqueda y detalle correctos (396 episodios); `loadLinks` recibe la ficha con `#s1-e12` y no encuentra `iframe#player-iframe`.
- **Arquitectura verificada en la web**: `episode-router.js` interpreta `#sN-eM`, solicita `GET /api/episodio/{serie|anime}/{slug}/{season}/{episode}` como JSON e inserta `data.html` en `#episode-container`; ese HTML incluye `iframe#player-iframe` con ruta `/vidurl/...`. `/vidurl/...` conserva el mecanismo existente `dataLink` + `POW_CHALLENGE`/`POW_SALT`, que el provider ya resuelve.
- **Causa raÃ­z**: `loadLinks` descargaba solo la ficha base. El fragmento `#s1-e12` no se envÃ­a en HTTP y el iframe del episodio solo aparece tras la llamada al router JS.
- **Fix**: quitar el fragmento para cargar la ficha; si falta iframe y el data tiene `#sN-eM`, llamar el endpoint API, parsear el HTML JSON y extraer el iframe. Luego continÃºa el pipeline existente `/vidurl` â†’ PoW â†’ hosts de video.
- **Versiones**: `SerieskaoProvider/build.gradle.kts` y `plugins.json` a 30; `fileSize` pendiente tras compilar.
- **CompilaciÃ³n**: pendiente por el usuario (`.\\gradlew.bat :SerieskaoProvider:make --console=plain -q`); no ejecutada por regla del repo.
- **Pendiente en dispositivo**: instalar v30 y probar ChapulÃ­n T1E12; esperar `episode API HTTP 200, iframe=true`, `dataLink`, PoW resuelto y links emitidos. Fuentes web revisadas: `/themes/serieskao/ficha.js?v=3`, `/api/episodio/serie/el-chapulin-colorado-1973/1/12`, `/vidurl/tt0229888-1x12/`.

## SeriesKao â€” conservar host espejo VidHide (3 Nov 2026 v31)
- El usuario reporta â€œenlaces no encontradosâ€. El cÃ³digo local v30 ya resuelve episodios mediante `/api/episodio/...` cuando la ficha no trae iframe y tiene logs por etapa (`episode API HTTP`, `iframe`, `dataLink`, PoW, hosts y total por extractor). AÃºn no hay logs del dispositivo para confirmar si el fallo estÃ¡ en esa ruta o al extraer las fuentes.
- RevisiÃ³n de VidHide encontrÃ³ que las URLs espejo se normalizaban a `vidhidepro.com` antes de cargarse, mientras SoloLatino conserva el host original porque el host canÃ³nico presenta fallos TLS. Ahora SeriesKao preserva hosts VidHide espejo (`morencius`, `minochinos`, `mivalyo`, `dinisglows`, `dhtpre`), los procesa con su extractor, construye `/stream/...` usando el origen espejo y reintenta el canÃ³nico si el espejo no extrae. `Origin` ahora usa esquema + host, sin `/embed`.
- `SeriesKaoProvider/build.gradle.kts` y `plugins.json` pasan de v30 a v31; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- Pendiente instalar v31 y probar un episodio fallido. Si sigue â€œenlaces no encontradosâ€, compartir logs filtrados por `SeriesKao` desde `loadLinks data` hasta el total final; en especial API HTTP/iframe, `dataLink`, PoW, URLs decrypted y `loadKaoSourceExtractor ... 0 links`.

## SeriesKao â€” instrumentaciÃ³n de diagnÃ³stico loadLinks (3 Nov 2026 v32)
- AÃ±adidos logs de inicio y cierre con cantidad real de links entregados al callback de CloudStream, y por cada emisiÃ³n: source, nombre, tipo, calidad y host.
- La ficha registra HTTP/cantidad de HTML; el flujo del router registra hash/ruta, respuesta API (HTTP, JSON parseado, `success`, longitud de `html`, iframe), y el player registra HTTP/longitud. Se aÃ±aden idioma/conteo de embeds, fallos de descifrado y total de URLs descifradas. El PoW registra presencia y longitudes del challenge/salt, sin imprimir sus valores.
- Versiones v32 en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- Pendiente instalar v32 y reproducir un episodio que dÃ© â€œenlaces no encontradosâ€. Capturar las lÃ­neas `SeriesKao` desde `loadLinks START` hasta `loadLinks FIN`; ese intervalo distinguirÃ¡ ausencia de hash/iframe, fallo API, falta de `dataLink`, PoW, descifrado o cero emisiones.

## SeriesKao â€” emitir destino probado de VidHide hls4 (3 Nov 2026 v33)
- **Logs v32 del usuario**: los episodios que sÃ­ resuelven VidHide extraen `master.m3u8` de `morencius.com/stream/...`; hls4 devuelve HTTP 200 durante el probe y se emite la URL del espejo. El log no incluye aÃºn el error del reproductor, pero el probe anterior seguÃ­a redirects automÃ¡ticamente y luego conservaba/emite la URL original del espejo, sin comprobar el destino final.
- **ComparaciÃ³n**: Danimados ya inspecciona redirects sin seguirlos, prueba el `Location` y emite el destino si responde. El parser de hosts/hls de SeriesKao y SoloLatino es esencialmente igual; la diferencia Ãºtil para este fallo es el manejo del redirect en el probe.
- **Fix v33**: SeriesKao ahora prueba cada variante con redirects desactivados. Si recibe 3xx, resuelve `Location`, comprueba el destino y emite esa URL si responde 2xx. Conserva el espejo original como fallback si el destino no valida; las variantes alcanzables siguen teniendo prioridad.
- `SeriesKaoProvider/build.gradle.kts` y `plugins.json` pasan de v32 a v33; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente en dispositivo**: instalar v33 y probar el mismo episodio de ChapulÃ­n/otro episodio VidHide. Revisar `[VH-Pro] probe hls4`, posible `probe hls4 redirect -> ...`, URL de `emit hls4` y respuesta a la solicitud del player. El log de entrada no contiene el error de reproducciÃ³n posterior, asÃ­ que el fix aÃºn debe confirmarse en dispositivo.

## SeriesKao â€” interceptar VidHide hls4 en `/stream/` (3 Nov 2026 v34)
- **Logs v33 del usuario**: ChapulÃ­n obtiene VidHide hls4 como `https://morencius.com/stream/.../master.m3u8` y emite el link; el usuario reporta `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)` al reproducir. El extractor `getVideoInterceptor` solo reconocÃ­a dominios CDN y rutas `/hls2/`, `/hls3/`, `.urlset/`, por lo que la URL hls4 `/stream/` quedaba fuera y no recibÃ­a el tratamiento de headers ni los logs de respuesta del interceptor.
- **Fix v34**: aÃ±adir `/stream/` a las rutas interceptadas para que el master hls4 y sus segmentos reciban UA, Referer, Origin y Accept. Preservar el `Origin` que ya lleva el request (derivado del host espejo) y usar `https://vidhidepro.com` solo como fallback. Se conserva el probe de redirects de v33.
- **TitÃ¡n Sim-BiÃ³nico sigue siendo un caso distinto**: API y router funcionan, pero `embed69.org/video/tt1709198-1x01/` no tiene `dataLink` ni `playServerVast`; no se emite ningÃºn servidor. Este cambio no resuelve esa pÃ¡gina. Hace falta identificar su player/API dinÃ¡mico antes de aÃ±adir un extractor fiable.
- Versiones a **34** (`SeriesKaoProvider/build.gradle.kts` y `plugins.json`); `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente en dispositivo**: instalar v34 y probar el mismo episodio de ChapulÃ­n; confirmar que aparece `[intercept] CDN request` para `/stream/` y observar `[intercept] CDN response` para el master/segmentos. TitÃ¡n continÃºa pendiente de inspecciÃ³n de Embed69.

## SeriesKao â€” fallback WebView y descubrimiento de servidores Embed69 (3 Nov 2026 v35)
- **AclaraciÃ³n del usuario**: Embed69 sÃ­ muestra tres servidores reproducibles para TitÃ¡n Sim-BiÃ³nico: DoodStream, VidHide y StreamWish. El diagnÃ³stico v32/34 de â€œno `dataLink` ni `playServerVast`â€ describe solo el HTML inicial que recibe `app.get`; no demuestra que los servidores no existan en el player renderizado.
- **Fix v35**: al faltar ambos formatos, buscar URLs conocidas de DoodStream, VidHide y StreamWish en atributos/HTML estÃ¡tico; si no aparecen, abrir la pÃ¡gina en WebView y repetir la bÃºsqueda sobre el DOM renderizado. Los candidatos se pasan a `loadKaoSourceExtractor`, incluyendo el mapeo normal de hosts. Se aÃ±aden logs de conteo/lista de servidores descubiertos y scripts/iframes si no se encuentra ninguno.
- `SeriesKaoProvider/build.gradle.kts` y `plugins.json` pasan a v35; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente en dispositivo**: probar TitÃ¡n T1E1. Buscar `servidores en HTML inicial`, `WebView HTML`, `servidores tras WebView` y `loadLinks FIN`; comprobar que se emiten los tres servidores y reproducir cada uno. La disponibilidad del WebView para esta pÃ¡gina todavÃ­a no estÃ¡ confirmada.

## SeriesKao â€” importar Jsoup para fallback Embed69 (3 Nov 2026 v36)
- Corregido el error de compilaciÃ³n reportado en `SerieskaoExtractors.kt`: faltaba `import org.jsoup.Jsoup` para `findServerUrls`. La falta del sÃ­mbolo impedÃ­a inferir el tipo de `element` y resolver `attr`.
- Versiones a **36** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ Pendiente compilar por el usuario y probar TitÃ¡n T1E1 en dispositivo, verificando la detecciÃ³n de servidores en el HTML inicial/WebView.

## SeriesKao â€” fallback WebView para el player interno (3 Nov 2026 v37)
- **Logs v36 del usuario**: TitÃ¡n T1E1 ahora llega a `https://serieskao.top/vidurl/tt1709198-1x01/` (HTTP 200, HTML 15184), pero no trae `dataLink` ni PoW en el HTML estÃ¡tico; `loadExtractor` termina con 0 links. El fallback WebView v35 solo estaba en la rama de Embed69 externo y no cubrÃ­a este caso interno.
- **Fix v37**: al faltar `dataLink` o `POW_CHALLENGE`/`POW_SALT`, renderizar el player interno con WebView y volver a parsear `dataLink`; usar el HTML renderizado si se encuentra. Logs muestran longitud, presencia de dataLink/PoW y scripts/iframes cuando sigue sin resolver.
- **VidHide hls4**: el mismo log confirma HTTP 200 para master e `index-v1-a1.m3u8` vÃ­a interceptor. No contiene la solicitud/respuesta posterior que provoca 2004; el siguiente diagnÃ³stico requiere las lÃ­neas `[intercept] CDN request/response` que siguen a la respuesta del index.
- Versiones a **37** (`SeriesKaoProvider/build.gradle.kts` y `plugins.json`), `fileSize` pendiente. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente**: compilar, probar TitÃ¡n T1E1 y confirmar `player WebView ... dataLink=true`; para hls4, pegar la secuencia del log desde `master.m3u8` hasta el primer error, incluidos los status HTTP de playlists/segmentos.

## SeriesKao â€” visibilidad del renderer WebView (3 Nov 2026 v38)
- Corregido `Cannot access renderViaWebView: it is private in file`: la funciÃ³n top-level pasa de `private` a `internal` para poder llamarse desde `SerieskaoProvider.kt`, manteniÃ©ndose encapsulada al mÃ³dulo.
- Versiones a **38** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ Pendiente compilar por el usuario y probar TitÃ¡n T1E1: confirmar que el WebView del player interno encuentra `dataLink`/PoW.

## SeriesKao â€” diagnÃ³stico de forma del player TitÃ¡n (3 Nov 2026 v39)
- **Logs v38 del usuario**: `/vidurl/tt1709198-1x01/` responde HTTP 200 con 15184 caracteres. El fallback WebView se ejecuta, pero devuelve solo 2293 caracteres sin `dataLink`/PoW; el HTML estÃ¡tico no tiene scripts externos ni iframes. `loadExtractor` emite 0. La diferencia de longitudes sugiere que el contenido renderizado no es la pÃ¡gina completa del player, pero falta ver quÃ© contiene.
- **Cambio v39**: registrar forma mÃ­nima del HTML estÃ¡tico y WebView: tÃ­tulo, texto visible limitado a 450 caracteres, contadores de scripts/iframes, presencia de marcadores conocidos y hasta seis scripts inline limitados a 220 caracteres. Esto permite identificar error de acceso o formato alternativo sin volcar el HTML completo.
- Versiones a **39** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente**: compilar e instalar v39; repetir TitÃ¡n T1E1 y compartir lÃ­neas `loadLinks player WebView`, `player estÃ¡tico` e `inlineScript`. Con esos datos se podrÃ¡ implementar el parser correcto del player que entrega `/vidurl/`.

## SeriesKao â€” renderizar Embed69 en iframe y resolver servidores visibles (3 Nov 2026 v40)
- **Logs v39 del usuario**: `/vidurl/tt1709198-1x01/` entrega HTML de Embed69 con texto visible `dood Stream HD`, `streamwish Stream Multicalidad`, `vidhide Stream Multicalidad` y `1fichier Descarga Directa`. El primer script ejecuta `if(window.self===window.top){document.documentElement.innerHTML='';window.stop();}`, vaciando la pÃ¡gina cuando WebView la abre directamente. AsÃ­ se explica el WebView â€œPÃ¡gina web no disponibleâ€ y los 0 links; los servidores sÃ­ estÃ¡n en la ficha.
- **Fix v40**: el renderer WebView ahora acepta `renderInIframe`; carga `/vidurl/` en un iframe dentro de un documento padre del mismo origen para que el script de Embed69 no active el bloqueo top-level. Recoge el HTML del iframe y simula clics en controles cuyo texto/atributos indican Dood, VidHide o StreamWish, guardando snapshots tras cada selecciÃ³n. `loadLinks` extrae URLs conocidas de esos snapshots y usa los extractores existentes.
- Versiones a **40** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ **Pendiente**: instalar v40 y probar TitÃ¡n T1E1. Revisar `[WebView] renderizando ... iframe=true`, `Embed69 servidores WebView=N`, `loadLinks EMIT` y `loadLinks FIN`; comprobar que los servidores detectados abren sus extractores. VidHide hls4 queda para despuÃ©s de cerrar TitÃ¡n.

## SeriesKao â€” tolerar errores de recursos secundarios en Embed69 (3 Nov 2026 v41)
- Ajustado el renderer con iframe para no cancelar toda la captura ante `onReceivedError` de recursos secundarios (scripts/anuncios) del player; ahora registra URL/error y continÃºa sondeando el DOM del iframe. Se corrige tambiÃ©n el Ã­ndice al hacer clic en cada botÃ³n de servidor, para que un handler fallido no salte el siguiente control.
- Versiones a **41** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ Pendiente probar TitÃ¡n T1E1 en dispositivo; buscar `iframe=true`, `Embed69 servidores WebView=N` y emisiones de links. Luego retomar el error hls4 de VidHide.

## SeriesKao â€” diagnÃ³stico seguro de fallos HLS (3 Nov 2026 v42)
- Los logs comparativos muestran que HLS4 de TitÃ¡n y ChapulÃ­n llega a HTTP 200 para master e index; el error 2004 de ChapulÃ­n ocurre despuÃ©s de esas respuestas y cada episodio tiene rutas/segmentos distintos.
- El interceptor ahora registra para `.m3u8` el estado, tipo, tamaÃ±o aproximado y primeras lÃ­neas de la playlist; las URLs se reducen a host/path sin query token. TambiÃ©n captura fallos de red con clase de excepciÃ³n y host/path antes de relanzarlos.
- Versiones a **42** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- â¸ï¸ Pendiente compilar, seleccionar VidHide hls4 de ChapulÃ­n T1E3 y compartir `[intercept] HLS playlist`, cualquier `CDN network failure` y `playerError`. Esos datos identificarÃ¡n si el index anuncia segmentos, si sus rutas fallan o si ExoPlayer rechaza el contenido. TitÃ¡n ya resuelve los tres servidores en v41.

## SeriesKao â€” incluir cdnsync en interceptor HLS (3 Nov 2026 v43)
- **Logs v42 de ChapulÃ­n T1E1**: el master e index de VidHide HLS4 responden HTTP 200. La playlist anuncia como primer segmento una URL de `redirector.cdnsync.cloud`, host que no estaba en `cdnDomains`, por lo que la solicitud no pasaba por el interceptor ni quedaba registrada. StreamWish tambiÃ©n muestra una URI en `tiktokcdn.com` (posible publicidad/segmento inyectado), dato separado del link VidHide.
- **Cambio v43**: aÃ±adir `cdnsync.cloud` a `cdnDomains`, para registrar las respuestas de los segmentos del redirector y aplicar UA/Referer/Origin y los timeouts extendidos. Se preserva el `Origin` original del mirror.
- Versiones a **43** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.
- **Resultado confirmado en dispositivo (logs 3 Nov 2026)**: master e index de HLS4 responden 200, pero el index apunta a `redirector.cdnsync.cloud/`; el interceptor registra cuatro respuestas consecutivas **522 text/html** para ese segmento. Esa respuesta HTTP fallida causa `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)`. La extracciÃ³n del enlace y los headers sÃ­ funcionan hasta llegar al segmento; el contenido del redirector estÃ¡ caÃ­do o inaccesible para este episodio.
- **ConclusiÃ³n**: el hls4 de TitÃ¡n y el de ChapulÃ­n son rutas de segmentos distintas; que TitÃ¡n reproduzca no garantiza que la ruta de ChapulÃ­n estÃ© disponible. Para ChapulÃ­n T1E1, usar VOE o StreamWish, que sÃ­ entregan alternativas reproducibles. El 522 es upstream y no se corrige cambiando el parser del plugin.
- Versiones a **43** en `SeriesKaoProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. Gradle no ejecutado. Comando: ` .\gradlew.bat :SeriesKaoProvider:make --console=plain -q`.

## OkruProvider â€” normalizar source y contar callbacks (31 Oct 2026 v6)
- **Log del usuario**: la bÃºsqueda y carga del video OK; `OkRuExtractor -> isFound=true` tras ~12 s, pero el player no mostraba los enlaces.
- **Hallazgo de cÃ³digo**: `isFound=true` solo se establecÃ­a dentro del callback, asÃ­ que el extractor sÃ­ emitiÃ³ al menos un link. El extractor construye esos links con `source=OkRU`/`OkRu`, pero el MainAPI registrado se llama `OKRU`; el provider entregaba el link del extractor sin normalizar `source`. Es un desajuste real que puede impedir asociar/reproducir el enlace desde el MainAPI.
- **Fix**: reenvolver los links del extractor (y del fallback genÃ©rico) con `newExtractorLink(name, ...)`, preservando URL, tipo, calidad, referer, headers y extractorData, para que `source` coincida con `OKRU`. El log ahora imprime cada emisiÃ³n y el total real; el retorno de `loadLinks` depende del nÃºmero emitido.
- **Versiones**: `OkruProvider/build.gradle.kts` y `plugins.json` a 6; `fileSize` pendiente tras compilar.
- **CompilaciÃ³n**: pendiente por el usuario (`.\\gradlew.bat :OkruProvider:make --console=plain -q`); no ejecutada por regla del repo.
- **Pendiente en dispositivo**: instalar v6 y comprobar `loadLinks emit source=OKRU` + `emitted=N`, luego abrir el enlace. No se pudo confirmar en el log compartido cuÃ¡ntos enlaces de video se emitieron ni si ya se intentÃ³ reproducirlos.

### OKRU v7 â€” corregir suspensiÃ³n y pÃ³ster genÃ©rico (31 Oct 2026)
- **Reporte**: compilador `Suspension functions can only be called within coroutine body` en `OkruProvider.kt` lÃ­nea 263; el HTML de una pelÃ­cula tiene `og:image=/res/i/anonym/def_1200x630-r.png` (imagen genÃ©rica), por eso el favorito se guarda sin pÃ³ster real.
- **Fix de compilaciÃ³n**: el callback normal ya solo acumula `ExtractorLink`; despuÃ©s de que termina `OkRuExtractor`/`loadExtractor`, `emitProviderLinks()` los reenvÃ­a desde el cuerpo suspendido de `loadLinks`, donde se puede llamar a `newExtractorLink`.
- **Fix de pÃ³ster**: guardar en cachÃ© en memoria el thumbnail real (`thumbnail.big/small`/`imageUrl`) recibido en bÃºsqueda, indexado por video ID. Si `og:image` es el placeholder genÃ©rico, usar el thumbnail de bÃºsqueda. No guardar el placeholder como `posterUrl`.
- **Versiones**: `OkruProvider/build.gradle.kts` y `plugins.json` a 7; `fileSize` pendiente.
- **CompilaciÃ³n**: pendiente por el usuario (`.\\gradlew.bat :OkruProvider:make --console=plain -q`); no ejecutada por regla del repo.
- **Pendiente en dispositivo**: compilar e instalar v7; confirmar compilaciÃ³n de `loadLinks`, que emite `source=OKRU`, y que al abrir desde resultados/favoritos se conserva el thumbnail real tambiÃ©n tras reiniciar la app. Si se abre un OK.ru directo sin haber pasado por bÃºsqueda en la sesiÃ³n actual, no hay thumbnail en esa cachÃ© en memoria y harÃ¡ falta otra fuente persistente.

### Arquitectura del sitio (sololatino.net)
- PelÃ­culas y series latinas (espaÃ±ol/VOSE)
- Cadena: `sololatino.net` â†’ `embed69.org` (PoW + AES decryption) â†’ links directos (vidhidepro, streamwish, voe)
- Hosts alternativos: `morencius.com` (â†’ vidhidepro), `xupalace.org` (go_to_playerVast)

### Funcional
- `getMainPage()` â€” 5 secciones (Ãšltimas, PelÃ­culas, Series, Recientes, etc.)
- `search()` â€” bÃºsqueda por query string
- `load()` â€” detalle: tÃ­tulo, descripciÃ³n, poster, tipo (movie/tv), episodios (DOM parsing)
- `loadLinks()` â€” resuelve embed69 â†’ PoW â†’ AES decrypt â†’ `loadSourceNameExtractor` â†’ custom extractors
- `tryVidHideProExtraction()` â€” eval/packed JS unpack â†’ `hls2`/`hls3` URLs from `var links={...}`
- `tryVoeExtraction()` â€” redirect chain â†’ m3u8/mp4 extraction (CAPTCHA limitation)
- `getVideoInterceptor()` â€” inyecta User-Agent + Referer + Origin a CDN (dramiyos, phtilzjvfok, vidhidepro, vidhide)
- `unpackPackedJS()` â€” robust regex: finds `eval(function(p,a,c,k,e,d){...}('`, base-N dictionary replacement

### ðŸ”§ FIX getVideoInterceptor not called (30 Ago 2026) â€” CRITICAL
- **SÃ­ntoma**: `getVideoInterceptor()` definido correctamente pero CloudStream NUNCA lo llamaba. Sin logs `[intercept] CDN request`. Error `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)` en todos los links.
- **Causa raÃ­z**: CloudStream busca el provider con `getApiFromNameNull(link.source)` â€” necesita que `link.source == provider.name`. SoloLatinoProvider usaba `"LATINO[VidHide]"`, `"VidHidePro"`, `"Voe"` como source en `newExtractorLink` â†’ `getApiFromNameNull` retorna null â†’ interceptor nunca llamado.
- **Fix**: los 3 `newExtractorLink` ahora usan `source = "SoloLatino"` (= `provider.name`):
  - Fallback `loadExtractor` wrapper (lÃ­nea 652): `"SoloLatino"`, label `"$source[${link.source}]"`
  - `tryVidHideProExtraction` (lÃ­nea 728): `"SoloLatino"`, label `"VidHidePro - $chosen"`
  - `tryVoeExtraction` (lÃ­nea 831): `"SoloLatino"`, label `"Voe"`

### ðŸ”§ FIX unpackPackedJS broken URLs (30 Ago 2026) â€” CRITICAL
- **SÃ­ntoma**: interceptor ahora SÃ llamado pero CDN retorna 403. URL desempaquetada tenÃ­a nombres de parÃ¡metros faltantes: `?=TOKEN&=1788145211&=129600` (roto) vs `?t=TOKEN&s=1788145211&e=129600` (correcto).
- **Causa raÃ­z**: `unpackPackedJS` usaba `HashMap` para el diccionario â†’ iteraciÃ³n en orden aleatorio. El JS packer reemplaza de ÃNDICE ALTOâ†’BAJO para evitar que palabras cortas del diccionario (como `t`, `s`, `e`, `f`) corrompan reemplazos mÃ¡s largos. `HashMap` procesa en orden random â†’ las palabras cortas se reemplazan primero y corruptan los valores.
- **Fix**: eliminar `HashMap`, usar loop directo `for (idx in count - 1 downTo 0)` que procesa de altoâ†’bajo, igual que el JS packer `while(c--)`.
- **Verificado con Python (PC)**: CDN retorna 200 OK con token fresco, incluso sin headers â€” el problema era 100% la URL rota del unpacker.

### ðŸ”§ FIX preferOrder y relative URLs (30 Ago 2026)
- **PreferOrder cambiado**: `hls2 > hls3 > hls4` (antes `hls4 > hls2 > hls3`). `hls2` es el CDN conocido que funciona (`dramiyos-cdn.com`/`acek-cdn.com`). `hls4` tiene URLs relativas (`/stream/...`) que pueden no funcionar.
- **Relative URL fix**: si el m3u8 URL empieza con `/`, se prepende `https://vidhidepro.com`.

### âœ… VERIFICADO en dispositivo (30 Ago 2026)
- `source = "SoloLatino"` â†’ interceptor llamado (`[intercept] CDN request`)
- Unpacker produce URLs correctas â†’ CDN responde 200 (`application/vnd.apple.mpegurl`)
- Segmentos `.ts` descargados seg-1 a seg-8 â†’ 200 `video/MP2T`
- ReproducciÃ³n funcional en ambos servidores (dramiyos-cdn y acek-cdn)

### PatrÃ³n general para plugins
- `newExtractorLink(source=..., ...)` â€” el parÃ¡metro `source` DEBE ser `this.name` (el nombre del provider) para que `getApiFromNameNull()` lo encuentre y llame a `getVideoInterceptor()`.
- Providers que lo hacen bien: Uniquestream (`this.name`), Netflix (`name`), Primevideo (`name`), JioHotstar (`name`), Reanime (`name`), SoloLatino (`"SoloLatino"`)
- Providers con el mismo bug: Plushd (usa `"VidHide"` en vez de `"PlusHD"`)
- â±ï¸ **`timeout` de `app.get` estÃ¡ en SEGUNDOS (03 Oct 2026)** â€” verificado por bytecode en NiceHttp 0.4.13 (`custom$suspendImpl` pasa el `long timeout` directo a `call/connect/write/readTimeout(J, SECONDS)`, sin conversiÃ³n). `timeout = 20000L` = 20000 segundos (sin timeout efectivo) â†’ el request se cuelga hasta el killer de 120s (`Timed out waiting for 120000 ms`). CausÃ³ el bug "2do episodio sin links vidhide, solo se arregla reiniciando la app" (conexiÃ³n envenenada al CDN throttled + sin timeout real). **Regla**: usar valores en segundos (`timeout = 20L`, no `20000L`). `withTimeoutOrNull/delay/postDelayed/waitMs` SÃ son ms (kotlinx/Android, no tocar). Corregido en SoloLatino(+Extractors) y SerieskaoExtractors (03 Oct 2026) + DanimadosExtractors (13 Oct 2026 v2). Pendiente el mismo barrido en
Mhdflix/PoseidonHD/Gnula/Plushd/Monoschinos.

### ðŸ”§ FIX streamwish + voe sin links â†’ fallback WebView (03 Sep 2026)
- **SÃ­ntoma**: solo se emitÃ­a el link vidhide hls2. Streamwish daba 0 links (pÃ¡gina challenge "Loading... please wait", len=811, sin m3u8). Voe daba 0 links (redirect a mirror `johnbeyondnation.com` con CAPTCHA Altcha).
- **Causa raÃ­z**: ambos requieren ejecuciÃ³n JS â€” StreamWish para pasar su challenge, Voe/Altcha que es PoW y se auto-resuelve en navegador real. `app.get` plano nunca lo pasa.
- **Fix**: `renderViaWebView()` (WebView + `outerHTML` tras 12s, patrÃ³n Tvenvivo) + refactor de parseos reutilizables:
  - `SoloStreamWish.parseHtml()` (miembro, antes inline en `getUrl`) â€” rama `streamwish` en `loadSourceNameExtractor`: primero `loadExtractor`, si 0 links â†’ WebView + parse, emite con `source="SoloLatino"`.
  - `VoeExtractor.parseHtml()` (miembro, antes inline en `getUrl`) â€” `tryVoeExtraction`: si CAPTCHA o sin m3u8/mp4 â†’ WebView + parse con `source="SoloLatino"`.
- `SoloLatinoPlugin.load()` guarda `SoloLatinoProvider.pluginContext` para el WebView.
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar en dispositivo â€” buscar `[SW] WebView` / `[Voe] WebView fallback` en logs y verificar que streamwish/voe emiten links.

### ðŸ”§ FIX subtÃ­tulos eliminados en rewrite vidhide (03 Sep 2026)
- **SÃ­ntoma**: antes del cambio de vidhide salÃ­an subtÃ­tulos, despuÃ©s ya no.
- **Causa raÃ­z**: el commit `078d4457` ("original sin subs") eliminÃ³ TODA la emisiÃ³n de subtÃ­tulos (`scanPageForSubs`, `tryExtractSubsFromM3u8`, tracks `.srt/.vtt` del DOM).
- **Fix**: restauradas las 2 funciones adaptadas al flujo actual:
  - `scanHtmlForSubs()` + `scanPageForSubs()` â€” busca `.vtt/.srt` en HTML (con dedupe por `seen` set). Se llama en `loadSourceNameExtractor` (paralelo, por servidor) y en `tryVidHideProExtraction` (pÃ¡gina + JS desempaquetado).
  - `tryExtractSubsFromM3u8()` â€” parsea `#EXT-X-MEDIA:TYPE=SUBTITLES` del master m3u8. Se llama en `tryVidHideProExtraction` tras emitir el link.
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar en dispositivo â€” buscar `[PageSubs]` / `[M3u8Subs]` en logs y verificar subtÃ­tulos en el player.

### ðŸ”§ FIX streamwish post-challenge sin parse + voe mirrors (03 Sep 2026)
- **Hallazgo en logs**: WebView SÃ pasa el challenge SW (`pageHasJW=true hasSources=true`) pero el parser no hallaba nada â€” el video viene en packer Dean Edwards real, `file:` con URL relativa/protocol-relative, o iframe.
- **Fix SW** (`SoloStreamWish.parseHtml`): regex `file|src` con URLs `//` y `/` (resueltas vs `pageUrl`), desempaquetador Dean Edwards altoâ†’bajo (`unpackDeanEdwards`, igual que el fix vidhide), un nivel de iframes, y log de contexto `file|sources` si falla.
- **Fix Voe**: si WebView sigue en CAPTCHA o no hay fuentes â†’ probar mirrors alternos (`yip.su`, `donaldlineelse.com`, `tubelessceliolymph.com`) con el mismo hash `/e/` (portable en la red voe).
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar â€” buscar `[SW] M3U8 (eval)` / `[SW] file` / `[Voe] probando mirror` en logs.

### âš¡ Speedup loadLinks: paralelizar servidores + mirrors (03 Sep 2026)
- **SÃ­ntoma**: el video tarda ~45-60s en iniciar. vidhide listo a los ~5s pero `loadLinks` no termina hasta pasar SW-WebView (~20s) + Voe-WebView (~13s) + mirrors (~60s) **en serie** (`encryptedLinks.forEach`).
- **Fix**: `forEach` â†’ `amap` en servidores embed69 (vidhide/SW/voe en paralelo â‰ˆ 25s total); mirrors voe en paralelo con `AtomicBoolean` + timeout 15sâ†’10s (donaldlineelse se colgaba 66s).
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar tiempo total hasta `loadLinks FIN`.

### âš¡ Speedup v2: WebView con salida temprana + mirrors primero + PoW rÃ¡pido (03 Sep 2026)
- **SÃ­ntoma**: aÃºn tarda en cargar (~30s). Los WebView esperan 12s fijos aunque el challenge se resuelva antes.
- **Fix**:
  - `renderViaWebView` ahora acepta `readyJs` y hace polling cada 2s: dumpea el HTML en cuanto aparecen marcadores (`SW_READY_JS` = jwplayer/m3u8, `VOE_READY_JS` = sin altcha + con blob/m3u8) en vez de esperar los 12s siempre.
  - Voe: mirrors (baratos, paralelos) ANTES que WebView en ambas ramas.
  - PoW: hex con tabla lookup (`toHexFast`) en vez de `"%02x".format` por byte (~5x mÃ¡s rÃ¡pido).
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar â€” buscar `[WebView] listo antes de tiempo` en logs.

### ðŸ”§ FIX freeze: emitir todas las variantes hls + cubrir nuevos CDNs (03 Sep 2026)
- **SÃ­ntoma**: Looney Tunes reproduce 5s y congela 40-60s. Log: segmentos `acek-cdn` tardan 10-20s (primer intento cuelga, el reintento OK). Mismo veredicto que Plushd v6: CDN lento, no cÃ³digo.
- **Hallazgo**: el unpack trae `hls2/hls3/hls4` (3 CDNs distintos) pero solo se emitÃ­a `hls2`. AdemÃ¡s el interceptor NO cubrÃ­a `premilkyway.com` (host del link SW) ni `honeycombbrandatelier.cyou` (hls3) â†’ esos segmentos iban sin headers.
- **Fix**: emitir las 3 variantes como links separados (`VidHidePro - hls2/hls3/hls4`) para elegir CDN rÃ¡pido; `cdnDomains` += `premilkyway`, `honeycombbrandatelier`.
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar eligiendo otra variante cuando acek-cdn se congele.

### ðŸ”§ FIX crash WebView thread + probe de variantes + mirrors acotados (03 Sep 2026)
- **Bug 1 (crÃ­tico)**: `onPoll` corre en thread JavaBridge y llamaba `evaluateJavascript` directo â†’ `WebViewMethodCalledOnWrongThreadViolation`, el dump nunca ocurrÃ­a (SW WebView daba 0 links). **Fix**: postear el dump al Main via `mainHandler`.
- **Bug 2**: `acek-cdn` devuelve **502** (caÃ­do) â†’ 2004 al reproducir hls2. **Fix**: probe paralelo de masters (timeout 10s) y solo se emiten variantes con 200; si ninguna responde se emiten todas (fallback).
- **Bug 3**: `donaldlineelse.com` se cuelga a nivel DNS 60-115s ignorando timeouts â†’ CloudStream mata `loadLinks` a los 120s. **Fix**: fuera de la lista de mirrors + `withTimeoutOrNull(20s)` global.
- **Extra**: `cdnDomains` += `cyou` genÃ©rico (cubre `honeycombbrandatelier`, `autumnmeadowcollective`, `publicshowcase` y futuros hls3).
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar â€” buscar `[VH-Pro] probe hls2 -> 502` y elegir `hls4` (vidhidepro da 200).

### ðŸ”§ FIX interceptor por path + voe multi-candidato (03 Sep 2026)
- **hls3 rota dominios** (`.shop`, `.space`, `.store`, `.sbs` ademÃ¡s de `.cyou`) â†’ el match por marca quedaba obsoleto cada episodio. **Fix**: matchear por path (`/hls2/`, `/hls3/`, `.urlset/`) + hosts fijos. Cubre marcas presentes y futuras.
- **Mirrors voe inÃºtiles**: el primer base64 largo de la pÃ¡gina suele ser seÃ±uelo (hash integrity) y el decrypt fallaba. **Fix**: probar TODOS los candidatos base64 con `decryptVoeF7(quiet=true)` hasta hallar uno con `source`/`direct_access_url`.
- CompilaciÃ³n OK: `:SoloLatinoProvider:compileReleaseKotlin`
- â¸ï¸ **Pendiente**: probar mirrors voe â€” buscar `[Voe] Found M3U8` tras `probando mirror`.

### ðŸ”§ FIX voe nunca emite: solver ALTCHA PoW (08 Oct 2026 v22)
- **SÃ­ntoma (usuario)**: en ningÃºn episodio/pelÃ­cula sale VOE, solo vidhide y streamwish.
- **Causa raÃ­z**: `parseHtml()` ABANDONABA explÃ­citamente ante `altcha-widget` (`return false`). VOE ahora lo exige casi siempre.
- **Fix**: rama `solveAltcha()` en `VoeExtractor` (lÃ³gica portada de redblacker8/storm-ext, **verificada en vivo**: de `voe.sx/e/fhbq8gdmw3ta` sacÃ³ master m3u8 con token + mp4 directo):
  - `_token` + challenge URL del HTML â†’ GET challenge (con cookies: las de `getUrl` o GET fresco) â†’ PoW PBKDF2 (`solveAltchaPow`, hex rÃ¡pido con tabla, NO `"%02x".format`) â†’ POST `_token/access/altcha` â†’ HTML post-soluciÃ³n â†’ flujo decrypt normal.
  - `parseHtml()` ganÃ³ param opcional `cookies: Map<String,String>? = null` (llamadores existentes intactos); `voeHeaders` subido a val de clase.
  - Imports nuevos: `android.util.Base64`, `java.net.URLEncoder`, `javax.crypto.Mac/SecretKeySpec` (todo del SDK, sin deps).
- **Nota versiones**: `build.gradle` iba en 21 pero `plugins.json` seguÃ­a en 2 (desync; resto de providers sincronizados). Ambos a **22** â€” confirmar que es lo deseado.
- CompilaciÃ³n: pendiente por el usuario (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`) â€” **no compilado por regla del repo**.
- â¸ï¸ **Pendiente**: probar episodio con mirror voe â€” buscar `[Voe] ALTCHA detectado` â†’ `ALTCHA resuelto en N intentos` â†’ `[Voe] Found M3U8`.

### ðŸ” DiagnÃ³stico ALTCHA falla en ~330ms + logging granular (08 Oct 2026 v23)
- **Log v22**: `[Voe] ALTCHA detectado` â†’ `[Voe] ALTCHA no resuelto` en ~330ms, sin lÃ­nea de error. Demasiado rÃ¡pido para PoW (prefijo "00" â‰ˆ 256 intentos Ã— ~20ms â‰ˆ 5s esperados) â†’ el fallo es ANTES del cÃ³mputo.
- **Verificado en PC**: todos los regexes matchean el HTML/JSON real (`_token`, challenge URL, nonce/salt/keyPrefix/cost). El challenge GET da 200 + JSON vÃ¡lido sin cookies.
- **HipÃ³tesis**: el challenge GET en el dispositivo devuelve no-JSON (challenge con cookies de sesiÃ³n rancia â†’ 4xx HTML). NiceHttp no lanza con 4xx, asÃ­ que llegaba silencioso y los params daban null.
- **Fix**: logging por paso (`sin input _token` / `sin token` / `sin challenge URL` / `challenge -> code/len` / `params incompletos` con keys+err) + reintento del challenge SIN header `Cookie` si la respuesta no empieza con `{`.
- Versiones a **23**. â¸ï¸ **Pendiente**: compilar y buscar en logcat quÃ© paso falla ahora.

### ðŸ”§ FIX voe: invocaciÃ³n directa (loadExtractor ignora customs) (08 Oct 2026 v24)
- **Log v23**: `[Voe] no m3u8/mp4 found` â†’ mirrors muertos â†’ WebViews â†’ a los ~22s el timeout global cancela TODO (`StandaloneCoroutine was cancelled`). Clave: **jamÃ¡s aparece `[Voe] URL:`** â€” primera lÃ­nea de `VoeExtractor.getUrl` â€” o sea, el fallback `loadExtractor` **nunca lo invoca** (solo consulta extractores nativos; los 13 customs del plugin, aunque registrados, no participan).
- **Fix (2 puntos)**:
  1. `tryVoeExtraction`: la detecciÃ³n de challenge (`captcha`/`CAPTCHA`/`cf-challenge`) NO matcheaba la gate actual (`Confirm you're human` + `altcha-widget`, sin la palabra "captcha"). AÃ±adido `altcha-widget` y, en esa rama, llamada DIRECTA a `VoeExtractor().parseHtml(finalHtml, ...)` (con solver v22) **antes** de quemar presupuesto en mirrors/WebView.
  2. Fallback de `loadSourceNameExtractor`: si el dominio es `voe.sx`, invoca `VoeExtractor().getUrl(...)` directamente (con re-wrap de labels como el resto); el resto sigue por `loadExtractor`.
- Versiones a **24**. â¸ï¸ **Pendiente**: compilar y buscar `[Voe] ALTCHA detectado` â†’ `ALTCHA resuelto` â†’ `[Voe] Found M3U8` (o el paso exacto que falle ahora).
- **Nota**: el mismo patrÃ³n aplica a `RumbleExtractor` (Seriesdonghua/Donghualife): sus "0 links" vÃ­a fallback probablemente tambiÃ©n sean no-invocaciÃ³n. Migrarlos a llamada directa cuando toque.

### ðŸ› Fix comilla fantasma en cost/keyLength (08 Oct 2026 v25) â€” LA CAUSA REAL
- **Log v24**: `params incompletos (nonce=true salt=true prefix=00 cost=null)` con `"cost":10000` visible en el mismo texto. Imposible... salvo que el patrÃ³n exija algo que el JSON no tiene.
- **Causa raÃ­z**: mis regexes `(\d+)""""` incluÃ­an una comilla de cierre (`(\d+)"`) copiada del formato string (`"nonce":"..."`). Pero `cost`/`keyLength` son NUMÃ‰RICOS sin comillas (`"cost":10000,`) â†’ el patrÃ³n jamÃ¡s matcheaba â†’ `cost=null` â†’ solver sin correr (de ahÃ­ los ~330ms). `keyLength` sobrevivÃ­a solo por su default `?: 32` (que encima era el valor correcto).
- **LecciÃ³n**: el test en PC usÃ³ (sin querer) el patrÃ³n SIN comilla y pasÃ³ â€” verificaba otro patrÃ³n distinto al compilado. **Regla**: al verificar regexes Kotlin con `"""`, extraer el contenido efectivo contando comillas (`""""x"` = open+`"x`, `x""""` = `x`+close) o mejor: evitar `""""` usando `"""..."""` con el contenido sin comillas adyacentes.
- **Nota**: el `loadVoe` de storm-ext tiene el MISMO bug en su `ALTCHA_COST`/`ALTCHA_KEY_LENGTH` (misma comilla fantasma) â†’ su solver tampoco corre nunca. No tocar (repo ajeno).
- Versiones a **25**. â¸ï¸ **Pendiente**: compilar y buscar `[Voe] ALTCHA resuelto en N intentos` â†’ `[Voe] Found M3U8`.

### âš¡ Speedup VOE: ALTCHA-direct primero + PoW paralelo (08 Oct 2026 v26)
- **Log v25**: VOE **SÃ funciona** (`ALTCHA resuelto` â†’ `Found M3U8/MP4` â†’ segmentos 200) pero LENTO: PoW 20-50s por video (81 intentos/23s, 202/48s) y el SUB (802 intentos) muriÃ³ por el kill de 120s de CS3 **despuÃ©s** de resolver (`Timed out waiting for 120000 ms` tras `resuelto en 802`). AdemÃ¡s el flujo quemaba ~15-30s en mirrors muertos + WebViews **antes** del solver.
- **Fix 1 (orden)**: en `tryVoeExtraction`, tras el regex directo, llamada a `VoeExtractor().parseHtml()` (solver) **antes** de mirrors/WebView.
- **Fix 2 (paralelo)**: `solveAltchaPow` reparte contadores por stride en 4 hilos (`Dispatchers.Default`, `AtomicLong` ganador, `ensureActive()` para cancelar). `Mac` propio por hilo (JCA no es thread-safe); `Mac.init` hoisteado fuera del loop (antes se recreaba por intento). Esperado: 20-50s â†’ 5-15s.
- Versiones a **26**. â¸ï¸ **Pendiente**: compilar y medir `ALTCHA resuelto en N intentos (Xms, 4h)`.

### ðŸ” Veredicto: edge de VOE alterna 404/200 por ventanas (08 Oct 2026, sin cambio de cÃ³digo)
- **Reporte**: `voe.sx/e/zlfn6grnrtgx` (somos-osos S4E5) da 404 en el plugin pero el usuario dice que en la web reproduce.
- **Medido**: el MISMO hash dio 404 (curl/OkHttp, 118945 B = pÃ¡gina "not found" completa) y 200-stub (requests, 759 B) con ~10 min de diferencia; luego 4/4 404s seguidos. **No es cachÃ©** (el provider no cachea nada: resuelve hashes frescos en cada play) **ni fingerprint estable** â€” es el edge de VOE que esconde el video por ratos (rotaciÃ³n de backend).
- **ImplicaciÃ³n**: si en la web reproduce VOE en ESTE momento, el plugin tambiÃ©n lo harÃ­a (mismo hash, misma pÃ¡gina). Lo mÃ¡s probable es que en la web reprodujera el servidor por defecto (vidhide/streamwish) â€” el plugin emitiÃ³ esos 4 links bien.
- **Sin fix de cÃ³digo**: el flujo ya cubre main â†’ mirrors (con solver) â†’ WebView â†’ fallback. Si un video estÃ¡ en ventana mala en todos sus mirrors, no hay nada que extraer. Reintentar mÃ¡s tarde.

### ðŸ› Fix _token atado a sesiÃ³n (08 Oct 2026 v27)
- **Log Serieskao v22**: PoW **resuelto** (19 intentos/2.3s, 146/10s) pero luego `encoded string not found` â€” el POST no sirviÃ³.
- **Causa raÃ­z**: el `_token` del gate va atado a la sesiÃ³n Laravel. Serieskao pasaba el HTML pero NUNCA las cookies (`parseHtml` de 5 args, `cookies=null` â†’ el solver hacÃ­a GET fresco = sesiÃ³n B distinta del token de la sesiÃ³n A â†’ POST rechazado â†’ gate re-renderizado sin config).
- **Fix (ambos providers)**:
  - `tryVoeExtraction`: capturar `res.cookies`/`mResp.cookies` del MISMO fetch que trajo el HTML y pasarlas a `parseHtml(..., cookies)`.
  - WebView path: `webViewCookies(url)` nuevo (lee `CookieManager` del WebView, que tiene su propio jar).
  - `solveAltcha`: log del POST (`code/len/gate=` para distinguir rechazo de markup distinto).
  - SoloLatino: mismo threading en sus 4 llamadas (2 directas + 2 WebView).
- Versiones: SoloLatino **27**, Serieskao **23**. â¸ï¸ **Pendiente**: compilar ambos y buscar `[Voe] POST solucion -> code=200` + `[Voe] Found M3U8`.

### ðŸ›¡ï¸ Blindaje anti-cierre en Serieskao (08 Oct 2026 v24)
- Mismo riesgo que SoloLatino v28 (varios WebViews en paralelo OOMean celulares de poca RAM; `WebView()` roto lanza `Error`): `webViewMutex` global + `catch (t: Throwable)` con rethrow de `CancellationException` en `renderViaWebView`.
- Versiones a **24**. â¸ï¸ **Pendiente**: probar en el celular fÃ­sico.

### ðŸ›¡ï¸ Blindaje anti-cierre en celular (08 Oct 2026 v28)
- **SÃ­ntoma (usuario)**: en el celular fÃ­sico, al reproducir, la app se cierra en vez de cargar enlaces; en el emulador no pasa.
- **Causa probable**: varios WebViews en paralelo ( Rama `amap` Ã— mirrors con challenge) + pÃ¡ginas con ads = pico de memoria que el LMK mata en celulares de poca RAM (en el emulador hay RAM de sobra). Contribuye: `WebView(appCtx)` sin red ante `Error` del sistema (solo se capturaba `Exception`).
- **Fix (sin cambiar resultados)**:
  - `webViewMutex` global: **un solo WebView vivo a la vez** (serializa los fallbacks).
  - `catch (t: Throwable)` con rethrow de `CancellationException`: un WebView roto degrada a null en vez de matar la app.
- Versiones a **28**. âœ… **VERIFICADO en celular fÃ­sico (09 Oct 2026)**: logcat v28 (somos-osos-2015 T4E9 â†’ T4E15) sin `FATAL EXCEPTION` ni `lowmemorykiller`; los `WebView.<init>` de `renderViaWebView` (SoloLatinoProvider.kt:716) aparecen **serializados** (el `webViewMutex` funciona, nunca en paralelo); `loadLinks FIN total emitidos=7 servidores=2`; VOE resuelve ALTCHA (`resuelto en 72/99/47/317 intentos`, `Found M3U8/MP4`) y los segmentos responden `[intercept] CDN response: 200 video/MP2T` (seg-1..5 de `cloudwindow-route.com`). TransiciÃ³n ep9â†’ep15 sin cierre. **La app ya no se cierra.**

### ðŸ” Repo externo redblacker8/storm-ext DESCARTADO (08 Oct 2026)
- El usuario propuso su `loadLinks` como alternativa compatible pre/stable. Verificado: usa las MISMAS APIs (todo existe en stable v4.8.0) â†’ no hay ventaja de compatibilidad.
- Diferencia real: resuelve token vÃ­a `POST /api/player-url` (form `t=`, `X-CSRF-TOKEN` del meta, sin cookies) en vez del handshake Sanctum+XSRF del nuestro. Probado en vivo: **419 CSRF mismatch** (el sitio, tras Cloudflare, no da cookies en GET plano y exige sesiÃ³n). Su flujo estÃ¡ obsoleto contra el sitio actual.
- Lo Ãºnico rescatable: su `loadVoe` con solver ALTCHA (PoW PBKDF2) por si VOE lo exige algÃºn dÃ­a; hoy no hace falta.

---

## TorrentioProvider â€” Plugin Stremio Torrentio (04 Sep 2026)

### Arquitectura (igual que Stremio real, sin servidor propio)
- **CatÃ¡logo/bÃºsqueda/detalle**: Cinemeta `https://v3-cinemeta.strem.io` (sin API key):
  - `GET /catalog/{movie|series}/top.json` â†’ main page (PelÃ­culas/Series Top)
  - `GET /catalog/{movie|series}/top/search={q}.json` â†’ search (ambos tipos en paralelo)
  - `GET /meta/{movie|series}/{ttId}.json` â†’ detalle + episodios (`videos[]` con season/episode)
- **Streams**: Torrentio pÃºblico `https://torrentio.strem.fun/stream/{movie|series}/{ttId[:s:e]}.json` â†’ `{streams:[{infoHash, fileIdx, name, title, behaviorHints{filename}, sources[tracker:...]}]}` (verificado con tt0111161: ~50 resultados con seeders/tamaÃ±o/proveedor).
- **Magnets**: `magnet:?xt=urn:btih:{hash}&dn={filename}&tr={trackers}` â€” trackers del propio stream (`sources[]`) + lista ngosang (cachÃ© 1h, tope 15). Se emiten como `ExtractorLink` y los reproduce el **torrent player interno de CloudStream** (igual que TotalTorrent).
- Label: `Torrentio 1080p ðŸ‘¤100 ðŸ’¾6.9GB (+[fileIdx] si â‰ 0)`; quality vÃ­a `getQualityFromName`; tope 50 links.

### Reglas aplicadas
- JSON con Jackson vÃ­a `AppUtils.parseJson`/`toJson` (NUNCA kotlinx-serialization en plugins).
- Sin TMDB API key (Cinemeta no la pide). Sin Real-Debrid en v1 (los links son magnets, no HTTP).

### Limitaciones conocidas
- `fileIdx â‰  0` (packs multi-archivo): el reproductor torrent puede elegir mal el archivo â€” se muestra `[N]` en el label como aviso.
- Sin seeders = no reproduce (normal en torrents). Elegir links con ðŸ‘¤ alto.
- Solo IDs `tt...` (IMDb). Lo demÃ¡s se omite.

### Archivos
- `TorrentioProvider/build.gradle.kts` (v1, mx, Movie+TvSeries, logo torrentio)
- `TorrentioProvider/src/main/AndroidManifest.xml`
- `TorrentioProvider/src/main/kotlin/com/example/TorrentioPlugin.kt`
- `TorrentioProvider/src/main/kotlin/com/example/TorrentioProvider.kt`
- `plugins.json` â†’ entrada `Torrentio` v1 (29517 bytes)

### Estado
- CompilaciÃ³n OK: `.\gradlew.bat :TorrentioProvider:compileReleaseKotlin --console=plain`
- Plugin empaquetado: `:TorrentioProvider:make` â†’ `TorrentioProvider/build/TorrentioProvider.cs3` (29631 bytes)
- â¸ï¸ **Pendiente**: push a `master` (el bot lo publica en `builds`), instalar y probar bÃºsqueda â†’ detalle â†’ link magnet â†’ reproducciÃ³n.

### Enfoques adoptados de yuzono/anime-extensions (04 Sep 2026)
- **Config inline en la URL**: `qualityfilter=cam,scr|sort=seeders/stream/...` â€” filtrado server-side (verificado: ordena por ðŸ‘¤ desc, sin CAM/SCR). Evita settings UI en v1.
- **`&index={fileIdx}` en el magnet**: selecciona archivo en packs multi-archivo (inofensivo si el reproductor lo ignora).
- **No adoptado**: trackers anime extra (ngosang basta), filtros por provider/idioma (requiere settings UI â†’ v2 con Real-Debrid), TMDB API key (Cinemeta no la pide), `torrentioanime`/kitsu (solo anime; club de fans aparte).

---

## RetrotveProvider â€” MEGA.nz extraction (30 Ago 2026)

### Problem
- RetrotveProvider's `processPlayerPage` had a switch case for MEGA links that just logged "MEGA links require app installation, skipping" and did nothing
- The site uses MEGA as one of 6 servers (Iframe/blenditall, Opt2/mega.nz, Opt3/1fichier, Opt4/mega.nz embed, Opt5/yourupload, Opt6) for episode playback
- **Paso a paso 1x1** (trid=10532) has MEGA as the ONLY working server

### Solution: Local HTTP Proxy + AES-CTR Decryption
MEGA files are **AES-128-CTR encrypted** â€” ExoPlayer cannot play them directly. Solution is a local HTTP proxy:

1. **Parse URL**: Extract `file_id` and `key` from `mega.nz/embed/{id}#{key}`
2. **MEGA API**: POST to `https://g.api.mega.co.nz/cs` â†’ get temporary download URL + file size
3. **Local proxy server**: Random port, ExoPlayer connects to `http://127.0.0.1:PORT/video`
4. **On-the-fly decryption**: Proxy fetches encrypted data from MEGA, decrypts AES-CTR, serves plaintext
5. **Range requests**: For seeking support, compute AES-CTR counter offset from byte position

### Key Implementation Details
- **Key derivation**: `aes_key[i] = key_bytes[i] XOR key_bytes[i+16]` for i=0..15; `iv[0..7] = key_bytes[16..23]`, rest zeros
- **Attribute decryption**: AES-CBC (not CTR) with zero IV for filename extraction; plaintext starts with "MEGA" prefix
- **Block alignment**: When Range start isn't block-aligned (16 bytes), decrypt dummy prefix bytes to advance CTR counter
- **ServerSocket timeout**: 60s; server thread stops when socket closed or process ends

### Files
- `RetrotveProvider/src/main/kotlin/com/example/MegaExtractor.kt` â€” MEGA API client + local HTTP proxy + AES decryption
- `RetrotveProvider/src/main/kotlin/com/example/RetrotveProvider.kt` â€” integration in `processPlayerPage` (lines 409-421)

### Build
- Plugin: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (51 KB)
- Compilation: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`

### â¸ï¸ Pending
- Test on device: Paso a paso 1x1 (trid=10532, mega.nz embed) â€” verify local proxy starts, ExoPlayer connects, video plays
- Test seeking (Range requests) â€” pause + seek to different position
- Test file naming â€” decrypt filename from MEGA attributes
- Test edge cases: large files (>1GB), slow connections, MEGA download quotas

### ðŸ”§ Fix msd:1 multi-CDN shard download (01 Sep 2026 v54)
- **Sintoma**: episodios con `msd:1` (multi-server download) y 6 URLs CDN fallan â€” chunk 0 funciona (ftyp OK en CDN #1), pero chunks mas alla de ~55-70MB obtienen 0 bytes de TODAS las URLs CDN
- **Causa raiz (3 bugs)**:
  1. **CDN #0 skip**: despues de obtener URLs frescas (`retries % 5 == 0`), `cdnUrlIndex` se pone en 0 pero se incrementa inmediatamente a 1 â€” CDN #0 NUNCA se reintenta
  2. **Descarga parcial aceptada**: cuando CDN retorna 2.7MB de 4MB, `totalWritten > 0` se acepta como completo â€” deja gaps en el archivo
  3. **Sin routing aware de shards**: `msd:1` retorna 6 URLs que sirven diferentes rangos de bytes, pero se usan rangos absolutos en TODAS â€” CDN retorna 0 bytes cuando el rango esta fuera de su shard
- **Fix: Shard probing**: descarga 4MB de cada URL CDN, intenta descifrar en cada posicion de CHUNK_SIZE para encontrar MP4 valido â†’ mapea cada URL a su offset de shard
- **Fix: Rangos relativos al shard**: para cada chunk, encuentra la URL cuyo shard contiene la posicion del chunk, calcula rango relativo (`start - shardOffset`), usa `$url/$relStart-$relEnd`
- **Fix: Deteccion de descarga parcial**: si `totalWritten < expectedSize`, revierte y reintenta
- **Fix: CDN #0 fresh URLs**: usa CDN #0 directamente sin incrementar despues de URLs frescas
- Compilacion OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (72 KB)
- Pendiente: instalar cs3 en dispositivo y probar episodio 5 (44NxgQha, 333MB, 6 URLs CDN, `msd:1`) â€” verificar que el shard probing encuentra los offsets correctos y que los chunks se descargan de las URLs correctas

### ðŸ”§ Fix UFA URL fallback + exponential 509 backoff (02 Sep 2026 v66)
- **SÃ­ntoma (v65)**: episodio 5 (44NxgQha) obtuvo HTTP 509 en TODOS los CDNs para chunk 0. IntentÃ³ CDN#1â†’#5, URLs frescas, no-range test â€” todos 509 con Content-Length=0. Es throttle de bandwidth por IP, no bug de cÃ³digo.
- **Causa raÃ­z**: el throttle 509 es por IP, NO por CDN. Probar diferentes CDNs no ayuda. El backoff anterior (15sÃ—retries, cap 60s) era insuficiente â€” el cooldown de MEGA puede durar 5-10 minutos.
- **Fix 1: UFA URL como fallback** â€” `performUfaUnlock()` ahora retorna la URL UFA (antes solo retornaba boolean). Se guarda en `DiskStream.ufaUrl`. Cuando TODOS los CDNs fallan con 509 despuÃ©s del retry loop, se intenta la UFA URL como Ãºltimo recurso (bucket de rate-limit diferente).
- **Fix 2: Exponential backoff mejorado** â€” delays: 15s, 15s, 20s, 25s, 30s, 40s, 50s, 60s, 90s, 120s (antes: 15sÃ—retries, cap 60s). Da tiempo al cooldown de MEGA para expirar.
- **No se puso UFA URL en cdnUrls** â€” porque el probe de shards la detecta como "mirrors" (sirve todo el archivo), rompiendo el mapeo de shards.
- CompilaciÃ³n OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin empaquetado: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (89 KB)
- â¸ï¸ **Pendiente**: probar en dispositivo â€” episode 5 aislado (no despuÃ©s de episode 1) para verificar UFA fallback + backoff.

### ðŸ”§ Fix Phase 4 shard parallel download (05 Sep 2026 v72)
- **SÃ­ntoma (v70/v71 en dispositivo)**: ep5 `44NxgQha` (msd:1, 6 CDN shards) nunca completa la descarga â†’ al cambiar de episodio o re-abrir da 2001. Logs MegaExtractor (22:05-22:09, build v70):
  - `Chunk 16 incomplete: 2772555/4194304` reintentado **hasta attempt 29 sin Ã©xito** (chunk que cruza el boundary del shard).
  - `HEARTBEAT port=32945 chunks=75/84 bytes=439192934/349407051` â†’ **writtenBytes 439MB > fileSize 349MB** (doble conteo).
  - `Partial download deleted for Zt8m3LYQ` (v70 borraba parciales; v71 ya no).
- **Causa raÃ­z (3 bugs en Phase 4)**:
  1. **`shardEnd = stream.fileSize` en TODOS los shard threads** (lÃ­nea ~275) â†’ cada thread iteraba TODOS los chunks del rango phase4 (solo skip si `chunkEndByte < shardOffset`), asÃ­ que varios threads descargaban los MISMO chunks â†’ `writtenBytes` se disparaba por encima de fileSize (doble conteo) + sobrescrituras concurrentes.
  2. **`downloadChunkFromShard` SIN cross-shard split**: pedÃ­a el chunk completo como rango absoluto a UNA shard URL â†’ para un chunk que cruza el boundary de shard, el CDN solo sirve su porciÃ³n â†’ `incomplete` infinito (chunk 16).
  3. **`raf.setLength(start)` en incomplete** truncaba el ARCHIVO entero mientras otros threads escribÃ­an otros offsets â†’ corrupciÃ³n.
- **Fix (v72)**:
  - Phase 4: cada shard thread ahora tiene su rango `[shardStart, shardEndExclusive)` (siguiente offset o fileSize). Solo el thread dueÃ±o de cada chunk lo descarga (`chunkStartByte < shardStart â†’ continue`, `>= shardEndExclusive â†’ break`). Sin duplicados.
  - `downloadChunkFromShard` reescrito con **sub-chunks cross-shard** (misma lÃ³gica `SubChunk` que `downloadChunkWithFreshUrl`): cada sub-rango que cae en otra shard se pide a la URL de ESA shard con `relStart-relEnd` relativo. Reintento de chunk completo `if (totalWritten < expectedSize)` tras revertir `writtenBytes`.
  - Crucially: `raf.setLength` eliminado â€” no se trunca el archivo en descargas paralelas.
- CompilaciÃ³n OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (**98.4 KB**)
- `plugins.json`: version 72, fileSize 95762 â†’ 98441
- â¸ï¸ **Pendiente**: instalar cs3 v72 y probar ep5 `44NxgQha` (aislado): esperar `chunks=84/84`, sin doble conteo en HEARTBEAT, y que al volver a ep1 se sirva desde cache/parcial.

### ðŸ”§ Fix shard CDN hang â†’ UFA fallback en Phase 4 (05 Sep 2026 v73)
- **Verificado en dispositivo (logs 22:53-22:55, build v72)**: el fix v72 FUNCIONÃ“:
  - ep1 `Zt8m3LYQ` (1 CDN URL -> Phase 4 secuencial): descarga fluida + al cambiar de episodio: `Partial download kept for resume: mega_Zt8m3LYQ.mp4 (363MB, 35 chunks)` â€” resume en disco funciona.
  - ep5 `44NxgQha`: `CROSS-SHARD split` de chunks 16 y 82 OK (ambos se completan), shard threads con sus rangos exactos terminan (16/16/16/17 chunks), `HEARTBEAT` ya no excede fileSize (`bytes=286492491/349407051`).
  - âœ… **pero NUEVO bug**: shard thread `gfs270n459` (132MB-198MB) se quedÃ³ colgado en **chunk 35** (22:55:41 Â«downloadingÂ» sin completar) â†’ HEARTBEAT congelado en `chunks=69/84` durante 15+s â†’ ExoPlayer 2001.
- **Causa raÃ­z**: cada shard thread usa SOLO su URL de shard y nunca cambia de fuente. Si un nodo CDN se cuelga (read timeout 60s repetido, p.ej. `gfs270n459` throttleado por IP), ese thread no avanza y el archivo nunca llega a 84/84 â†’ el serve loop corta la conexiÃ³n a los 30s â†’ 2001.
- **Fix (v73)** en `downloadChunkFromShard` (`MegaExtractor.kt`): **UFA URL como fallback** en TODOS los fallos del shard:
  - **509**: probar UFA inmediatamente (igual que `downloadChunkWithFreshUrl`).
  - **HTTP != 200/206**: probar UFA desde el attempt 2.
  - **Catch (read timeout / socket)**: probar UFA desde el primer intento fallido.
  - **Incomplete / 0 bytes**: probar UFA desde el attempt 2.
  - UFA (`stream.ufaUrl`) es un mirror del archivo completo con bucket de rate-limit DISTINTO â†’ sirve el rango absoluto `start-end` aun si el nodo shard estÃ¡ muerto. Ya venÃ­a del flujo v66 (`performUfaUnlock`) y se pasa a `DiskStream` en `startStreamProxy` (tambiÃ©n en la rama resume).
  - **Lock fix**: `tryDownloadFromUfa` usaba `synchronized(raf)` mientras los shard threads usan `synchronized(stream.fileLock)` â†’ riesgo de seek/write race en paralelo. Ahora usa `synchronized(stream.fileLock)` + `raf.seek(ufaWritePos)` incrementado por buffer.
  - `start`/`end` movidos fuera del `try` para que el catch pueda llamar a `tryDownloadFromUfa`.
- CompilaciÃ³n OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (**99.1 KB = 99071 B**)
- `plugins.json`: version 73, fileSize 98441 â†’ 99071 (JSON vÃ¡lido, 66 entradas)
- â¸ï¸ **Pendiente**: instalar cs3 v73 y probar ep5 `44NxgQha` de nuevo â€” si `gfs270n459` se cuelga, el log debe mostrar `UFA fallback: requesting bytes ...` y el chunk 35+ debe completarse; expectativa `chunks=84/84`.

### ðŸ”§ Fix seek-ahead 2001 â†’ serve on-demand via UFA (05 Sep 2026 v74)
- **Verificado en dispositivo (logs 23:31-23:33, build v73)**: el test NO llegÃ³ a ejercitar el fallback UFA del shard:
  - ep1 `Zt8m3LYQ` (91 chunks, 363MB): resume a 36/91, descarga secuencial fluida chunks 35â†’60 (~2s cada uno, ~8MB/s sostenido). **PERO** el usuario hizo seek a ~306MB y el `handleStreamRange` esperÃ³ 30s por el chunk 73 (`Timeout waiting for chunk 73 pos=306430588`) â†’ sirviÃ³ **0 bytes** â†’ **2001**.
  - ep5 `44NxgQha` (84 chunks, 333MB): resume a 69/84; **el probe de 6 CDNs seguÃ­a corriendo al final del log** (`Shard #5: probing` 25s+), asÃ­ que Phase 4 y el UFA fallback de v73 **jamÃ¡s se ejecutaron**; los chunks 35-49 (shard gfs270n459) quedaron sin descargar durante ese tiempo.
- **Causa raÃ­z**: el serve loop (`handleStreamRange`) solo espera a que el background downloader alcance el chunk pedido (mÃ¡x 30s) y nunca lo descarga Ã©l mismo. Si el usuario hace seek AVANTE del progreso (ep1), o un shard estÃ¡ caÃ­do/throttlado y todavÃ­a no se ha mapeado (ep5), el chunk pedido por ExoPlayer no llega a tiempo â†’ 0 bytes â†’ 2001.
- **Fix (v74)** en `MegaExtractor.kt`:
  - `DiskStream.fetchChunkOnDemand(ci)` + `fetchChunkOnDemandUnlocked(ci)`: **descarga UN chunk concreto vÃ­a la UFA URL (mirror del archivo completo, rango absoluto `bytes=start-end`)** cuando ExoPlayer lo necesita. Guard `ondemandFetching` (synchronizedSet) para que varios threads de serve no descarguen el mismo chunk en paralelo; si otro ya lo estÃ¡ descargando, espera hasta 20s.
  - `serveAesKey`/`serveBaseIv` en `DiskStream`, seteado en `startStreamProxy` (mismos keys que usa el downloader). `fetchChunkOnDemand` usa `resolvedAesKey ?: serveAesKey` (igual que `downloadChunkWithFreshUrl` L.438/`tryDownloadFromUfa`).
  - `handleStreamRange` reescrito: espera 5s al background (`waitForChunk(5000)`), luego loop hasta 30s intentando `fetchChunkOnDemand(ci)` cada 5s. Si aÃºn no hay chunk â†’ timeout â†’ break (como antes).
- **Resultado esperado**: seek al minuto 15 de ep1 â†’ sirve el chunk 73 vÃ­a UFA en 1-3s en vez de 30s de timeout; ep5 con shard caÃ­do â†’ el serve obtiene el chunk 35 vÃ­a UFA aunque el probe no haya terminado.
- CompilaciÃ³n OK: `.\gradlew.bat :RetrotveProvider:compileReleaseKotlin --console=plain -q`
- Plugin: `:RetrotveProvider:make` â†’ `RetrotveProvider/build/RetrotveProvider.cs3` (101190 B)
- `plugins.json`: version 74, fileSize 99071 â†’ 101190 (JSON vÃ¡lido, 66 entradas)
- â¸ï¸ **Pendiente**: instalar cs3 v74 y probar: (1) ep1 con seek AVANTE â€” log `On-demand UFA fetch chunk N` y reproducciÃ³n sin 2001; (2) ep5 â€” el probe puede tardar pero el play debe continuar con `On-demand UFA fetch chunk 35+` segÃºn llegue el playhead.

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

### MigraciÃ³n a tele-libre.icu â€” rediseÃ±o total (12 Oct 2026 v3)
**Reporte (usuario)**: la web se mudÃ³ a `https://tele-libre.icu/` con diseÃ±o nuevo. El provider (v2, `a.channel-link` + `tele-libre.live`) devolvÃ­a home/search vacÃ­os.
**Estructura nueva (verificada en HTML real)**:
| Antes (.live) | Ahora (.icu) |
|---|---|
| Cards `a.channel-link` (img + texto) | `article.channel-card > a[href^=/en-vivo/]` (nombre en `.channel-name`, logo en `.logo-box img`, `data-category` + `data-name` en el article) |
| Detalle: `h1` + `img.imgCanal` | `h1` ("Telefe en vivo") + `.channel-logo img` / `.big-logo img` / `og:image` + relacionados en `.related-grid a[href^=/en-vivo/]` (img + `b`) |
| 1 iframe (`#playerFrame`/`#embed`) | `iframe#iframe` (`src=/en-vivo/{id}/embed.php`) + botones `.option-buttons a[target=iframe]` (Opcion 1 â†’ `embed.php`, Opcion 2 â†’ `embed2.php`) |
| embed â†’ mpd2 â†’ sensa | **Igual**: `embed.php` â†’ iframe `bestleague.life//tok.html?get=...` (cvattv, `handleTokHtml` lo cubre); `embed2.php` â†’ iframe `/mpd2.php?id=...` (sensa, `handleSensaConfig` lo cubre). Cadena intacta, solo cambiÃ³ la entrada. |
- **Rewrite parcial** (`TelelibreProvider.kt`): `mainUrl` â†’ `https://tele-libre.icu`; `getMainPage` agrupa por `data-category` en secciones (General/Noticias/Deportes/PelÃ­culas y series/Infantil/Documentales/MÃºsica/+18, salta la card externa de Agenda); `search` mismo parse + filtro; `load` con nuevos selectores de poster + recomendaciones de `.related-grid`; `loadLinks` extrae la cadena a `resolveChain(startUrl, pageReferer)` y **prueba las opciones en orden** (Opcion 1 â†’ 2 â†’ iframe, mÃ¡x 3, retorna al primer Ã©xito).
- **Bug extra encontrado**: `safeGet` usaba `timeout = 15000L` y el check MPD `timeout = 10000L` â€” en NiceHttp el timeout va en SEGUNDOS (regla SoloLatino 03 Oct 2026) â†’ eran 15000s/10000s = sin timeout efectivo, colgaba hasta el killer de 120s. Corregido a `15L`/`10L`. (Mismo bug visto en `TvenvivoProvider.safeAppGet`: `timeoutMs = 10000L` â†’ `timeout = timeoutMs * attempt` = 10000s+; pendiente el mismo fix ahÃ­.)
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :TelelibreProvider:make --console=plain -q`), instalar y probar home (secciones por categorÃ­a) + Telefe Opcion 1 (cvattv DASH) y Opcion 2 (sensa DASH). Logs: `adb logcat -s Telelibre:V` buscando `opciones:` y `step1:`.

### Veredicto v3 en dispositivo + probar TODAS las opciones (12 Oct 2026 v4)
**Log v3 (usuario)**: el cÃ³digo v3 funciona Ã­ntegro â€” `getMainPage: 10 secciones, 155 canales`, `load` OK, `opciones: [embed.php, embed2.php]`, `step 2 iframe tok.html`, `tok DASH mpd=...` y `sensa DASH mpd=... kid=...` resueltos. Los fallos son del CDN, no del parseo:
- **sensa (`cdn.sensa.com.ar/.../Universal.mpd`) â†’ 403 `text/html`** en el MPD (Ã—4 reintentos de ExoPlayer) â†’ error 2004. Reproducido desde PC con UA Chrome + Referer + Origin + `webtoken: 1.0` y sin ellos â†’ **403 siempre** (270 B, `403 Forbidden` genÃ©rico, sin Set-Cookie en `mpd2.php`). Bloqueo por IP/fingerprint TLS del CDN, no por headers. La propia web avisa que estas seÃ±ales piden AR/UY/PY o VPN.
- **cvattv (`edge-live01-sl.cvattv.com.ar:443`) â†’ TCP connect timeout** (000 desde PC + `failed to connect ... after 10000ms` en el dispositivo) â†’ error 2001. Edge caÃ­do o bloqueado a nivel red para todo el mundo, no solo el usuario. AdemÃ¡s el JWT lleva `sip: 84.252.114.2` atado a otra IP.
**Fix v4 (cÃ³digo)**: `loadLinks` probaba solo hasta la primera emisiÃ³n â€” ej. Universal Opcion 1 emitÃ­a sensa (403) y la Opcion 2 jamÃ¡s se intentaba. Ahora prueba **TODAS** las opciones y emite todos los links hallados (`var ok` acumulado). No resucita CDNs bloqueados, pero convierte "1 link muerto" en "2 links para probar" (y donde una opciÃ³n sÃ­ sirva, reproduce).
- `plugins.json`: `iconUrl` â†’ `.icu/favicon.ico` (la description ya decÃ­a `.icu`).
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 23000).
- â¸ï¸ **Pendiente**: compilar, instalar y (1) comprobar que cada canal lista 2 links; (2) **probar con VPN en Argentina** â€” si sensa/cvattv responden con IP AR, el bloqueo es geo y no hay mÃ¡s cÃ³digo que escribir.

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

### SubtÃ­tulos (ASS -> VTT)
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
- â¸ï¸ **Pendiente**: probar en dispositivo ok.ru (extractor + interceptor), rumble/dailymotion/odysee via `loadExtractor`, y los subtitulos ASS->VTT con Referer.

### ðŸ› Fix "sin episodios o temporadas" + search vacio (05 Oct 2026 v5)
**Sintoma (logcat del usuario)**: `sin temporadas en great-ruler-2-41`, `el-cazador-de-demonios-3-28`, `combat-continent-2-la-inigualable-secta-tang-1-173` y `GET https://donghualife.com/{uuid}-temporada-1-21 fallo: StandaloneCoroutine was cancelled`. Ademas el search no mostraba ninguna tarjeta.

**Causa raiz 1 (ids sueltos)**: `parseLatestEpisodes` y la lista de episodios de `load()` usaban el **watchId desnudo** como `SearchResponse.url` / `EpisodeItem.id` (`great-ruler-2-41`). `load()` solo detectaba `/watch/` en la URL, asi que un id suelto caia en la rama de serie -> `GET /{id}` -> pagina vacia -> "sin temporadas". CS3 ademas prepende `mainUrl` a las urls relativas (por eso los GET salian a `https://donghualife.com/{id}`).

**Causa raiz 2 (temporadas truncadas)**: `initialEpisodes` del RSC viene **incompleto** en temporadas != 1. Verificado: `great-ruler-1` inline=52 de 52, `great-ruler-2` inline=**10 de 41** (la API `/api/series/great-ruler/seasons/great-ruler-2/episodes` devuelve los 41). `seasonEpisodes` devolvia los inline sin comprobar nada -> la T2 aparecia con 10 episodios.

**Causa raiz 3 (peliculas)**: `load()` hacia `MOVIE_PREFIX + slug` con `slug = "pelis:{slug}"` (el id ya traia prefijo) -> `pelis:pelis:{slug}` -> `/peliculas/pelis:{slug}` -> 0 fuentes. Afectaba a peliculas de portada y de search.

**Fixes**:
- `parseCards` y `parseLatestEpisodes` emiten **URLs absolutas** (`$mainUrl/series/{slug}`, `$mainUrl/peliculas/{slug}`, `$mainUrl/watch/{id}`). Elimina la ambigÃ¼edad con el prepending de `mainUrl` que hace CS3.
- Nueva `WATCH_PREFIX = "watch:"` para los ids de episodio. `load()` reconoce episodio por (a) prefijo `watch:`, (b) `/watch/` en la URL, o (c) id suelto con forma `{seasonSlug}-{ep}` (`EPISODE_SHAPE`) -> `episodeResponse()` devuelve `MovieLoadResponse` con `movieData = "watch:{id}"`. `loadLinks()` quita el prefijo al construir la URL.
- `seasonEpisodes(seriesSlug, slug, initial, expected)`: usa la API cuando `inline.size < episodeCount` (nuevo data class `SeasonRaw`).
- `movieData = MOVIE_PREFIX + (movieSlug ?: slug)` â€” evita el doble prefijo.
- `search()`: log de entrada/salida (`search 'q' -> N series, M peliculas`), `CancellationException` re-lanzada, excepcion general -> `null`, y `null` si la lista queda vacia.
- `plugins.json`: `tvTypes` ahora `["Anime","OVA","AnimeMovie"]` (solo declaraba `["Anime"]`, aunque `build.gradle.kts` ya tenia los tres y el provider sobrescribe `supportedTypes` en codigo).

**Verificado por bytecode por que el search deberia funcionar**: `SearchViewModel.search` -> `APIRepository.search(query,page)` -> `withTimeout(getTimeout(searchTimeoutMs))` (**120000ms** por defecto, `coerceIn(5000,480000)`) -> `MainAPI.search(query,page)` -> **`invokevirtual search(String,Continuation)`** (offset 131 del `search$suspendImpl`), o sea que **si llama al override de 1 argumento** del provider. El endpoint del provider coincide con el del buscador del sitio: el JS de la home hace `router.push('/series?q=' + encodeURIComponent(q))`.

**Descartado**: los UUID legacy `{uuid}-temporada-N-M` **NO** son enlaces muertos. Verificado: `/watch/1c684f19-...-temporada-1-21` tiene `sources` (rumble, dailymotion) y el token resuelve 200 a `rumble.com/embed/...`. Se habiaadded un filtro `DEAD_WATCH` que los descartaba -> **eliminado**.

**Otros verificados**: `/peliculas/*` tiene `sources` (rumble/vk, dailymotion/ok.ru) y tokens que resuelven 200; los labels vienen en variantes de caja (`Ok.ru`, `Dailymotion`, `rumble`, `vk`) pero el dispatch se hace por URL resuelta, no por label; las peliculas **no** traen `episodeId` (sin subtitulos); la busqueda del sitio es server-side y correcta (0 resultados solo para titulos ausentes del catalogo: "one piece", "re:zero", "solo leveling").

### Estado v5
- `build.gradle.kts`: `version = 5`; `plugins.json`: version 5, tvTypes `[Anime, OVA, AnimeMovie]`, `fileSize` **pendiente de actualizar** tras compilar.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar. Para diagnosticar el search: `adb logcat -s DonghuaLife:V` y buscar `search() llamado:` / `search '...' -> N series`.
- â¸ï¸ Verificar tambien ok.ru, rumble/dailymotion/odysee/vk y los subtitulos ASS->VTT.

### ðŸ“Š Logging de diagnostico + seccion Action en home (05 Oct 2026 v5.1)
- **Motivo**: el search no mostraba nada en dispositivo y hacia falta ver donde falla exactamente cada etapa.
- **Seccion nueva en `getMainPage`**: `AcciÃ³n` desde `/genres/Acci%C3%B3n?page=N` (verificado: 50 poster-cards en la pagina 1, paginado `?page=N`). Se usa `encGenre()` porque el sitio exige **percent-encoding UTF-8** en el path (`URLEncoder.encode` + `+`â†’`%20`).
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
- âš ï¸ **TRAMPA CRITICA (no revertir)**: `fetchDoc` hace `Jsoup.parse(text, url)` â€” el **baseUri es obligatorio**, porque `parseCards` lee `a.attr("abs:href")`. Con `Jsoup.parse(text)` (sin baseUri) `abs:href` devuelve la ruta RELATIVA (`/series/xyz`) y se rompen los links de todas las tarjetas. Se cambiÃ² de `.document` a `.text` + `Jsoup.parse` solo para poder loguear el tamaÃ±o real del body.

### ðŸŽ¬ Generos del home: como aÃ±adir mas (05 Oct 2026 v5.2)
- **Mecanismo**: todo esta en la constante `GENRE_SECTIONS` (`DonghualifeProvider.kt:38`). **AÃ±adir un genero = aÃ±adir una linea**. `getMainPage` hace un `async` (fetch paralelo) por genero a `/genres/{nombre}?page=N` y crea un `HomePageList` por cada uno. Los generos vacios se omiten solos (`takeIf { it.isNotEmpty() }`).
- **El nombre debe coincidir EXACTAMENTE con el de `/genres/{nombre}`** (ver tabla). El path se codifica en UTF-8 con `encGenre()` = `URLEncoder.encode` + `+`â†’`%20` (`Artes marciales` â†’ `Artes%20marciales`, `AcciÃ³n` â†’ `Acci%C3%B3n`).
- **Lista canonica** (sacada de `https://donghualife.com/genres`, que es un indice con `a.surface[href^=/genres/]` â†’ `h2` = etiqueta, `p` = nÂº de series). Total 352 series.:
  | # | Genero | # | Genero | # | Genero | # | Genero |
  |---|---|---|---|---|---|---|---|
  | 272 | AcciÃ³n | 74 | Artes marciales | 14 | Demonios | 6 | Militar |
  | 150 | Aventura | 41 | Comedia | 13 | Isekai | 5 | BÃ©lico |
  | 149 | Cultivo | 40 | ReencarnaciÃ³n | 11 | Magia | 5 | Alquimia |
  | 102 | AnimaciÃ³n | 40 | Venganza | 10 | Seinen | 5 | Escolar |
  | 98 | FantasÃ­a | 33 | Ciencia FicciÃ³n | 9 | Sistema de cultivo | 5 | Historia |
  | 96 | Romance | 30 | Misterio | 4 | Estrategia | 4 | Harem |
  | 87 | Drama | 29 | Superpoder | 3 | Guerra y polÃ­tica | 4 | ConspiraciÃ³n |
- **Ojo**: `/genres/{X}` responde **200 con 0 cards** para nombres inexistentes o vacios (p.ej. `Sci-Fi`, `Wuxia`, `Mecha`, `Cultura`, `PsicolÃ³gico`) â†’ la seccion simplemente no aparece, no rompe.
- **Ojo 2**: el sitio duplica enlaces en minusculas (`/genres/acci%C3%B3n`, `/genres/acci%C3%B3n.`) y esos **NO** traen series. Usar siempre la variante con mayusculas de la tabla.
- **Coste**: cada seccion de genero son ~180-260 KB. Con 8 generos son 12 fetches en paralelo por pagina del home (4 base+ 8 generos) â†’ ~2 MB por paginacion. Si molesta en datos moviles, recortar `GENRE_SECTIONS`.
- El log de `getMainPage` ahora incluye el desglose: `generos=AcciÃ³n=50, Aventura=44, Cultivo=50, ...`

### ðŸ›ðŸ› CRITICO fixUrl(): "enlaces no encontrados" en TODOS los episodios (05 Oct 2026 v6)
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
// MainAPIKt.fixUrl(api, url) â€” decompilado
if (url.startsWith("http") || url.startsWith("{\"") || url.startsWith("[")) return url  // intacta
if (url.isEmpty()) return ""
if (url.startsWith("//")) return "https:$url"
if (url.startsWith("/")) return api.mainUrl + url
return api.mainUrl + "/" + url        // <-- "watch:x" cae aqui
```
Y **`newEpisode()` SIEMPRE la aplica**: `newEpisode(api, url, init, fixUrl=true)` (el default de `newEpisode$default` pone `iconst_1` en el flag, offset 20-21) -> `fixUrl(api, url)` en el offset 29. Log: `watch:blades-guardians-1-1` -> `https://donghualife.com/watch:blades-guardians-1-1` -> el sitio responde **200 con la pagina de 404 (37019 B)**, sin `sources` â†’ 0 enlaces.
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

### ðŸ—‘ï¸ Quitada la fila "Ultimos episodios" + fix numeracion de temporadas (05 Oct 2026 v6)
- **Peticion del usuario**: la fila "Ultimos episodios" del home estorba (generaba cards de episodio con MovieLoadResponse y ruido en el log). Eliminada de `getMainPage` junto con el fetch de `$mainUrl/` y la funcion `parseLatestEpisodes`. El home queda: Popular, Recientes, Peliculas y las 8 secciones de genero.
- **Bug numeracion de temporadas**: `westward-5-0` salia como **T9** (era la T5, 64 eps). Causa: el numero se sacaba de `slug.substringAfterLast('-')` -> "0" -> no >0 -> caia en `base+1+idx` = 4+1+4 = 9. Ahora `seasonNumber(slug)` recorre los segmentos **de derecha a izquierda** y devuelve el primer entero > 0: `x-1`->1, `x-season-2`->2, **`x-5-0`->5**. Los `isSpecial` se numeran al final (base+1...).

### ðŸ› Fix dailymotion: loadExtractor devuelve 0 links (05 Oct 2026 v7)
**Sintoma (logcat del usuario, v6)**: el fixUrl quedo **resuelto** (`loadLinks data='https://donghualife.com/watch/blades-guardians-1-1'` -> pagina 91401 B -> `fuentes=1 [dailymotion]` -> `source ok 141ms -> https://geo.dailymotion.com/player/xhojl.html?video=k5DBqQlgteD31hzaRfI`), pero:
```
sin links para dailymotion (https://geo.dailymotion.com/player/xhojl.html?video=k5DBqQlgteD31hzaRfI)
loadLinks ... -> SIN LINKS (1 fuentes) 1717ms
```

**Investigacion (bytecode de `Dailymotion.getVideoId`, LA CAUSA RAIZ)**:
```kotlin
// Dailymotion.getVideoId(url) â€” decompilado
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

### ðŸ› Fix rumble: CS3 no trae extractor de Rumble (05 Oct 2026 v8)
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
- Logs: `rumble embed fallÃ³ (...)`, `rumble: reintentando con embed ...`, `rumble -> HLS OK` / `MP4 OK` / `0 URLs`.
- El `getVideoInterceptor` no necesita cambios (el link ya lleva headers+referer; el interceptor es pass-through salvo `__sub` y ok.ru).

### ðŸ” Comparativa RumbleExtractor (archivo del plugin) vs emitRumble (05 Oct 2026 v8)
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
- **Plan B si `app.get` da 403 tambien en el movil**: `com.lagradost.cloudstream3.network.WebViewResolver` existe en el jar (`resolveUsingWebView` devuelve `Pair<Request, List<Request>>` interceptados por regex). Solo implementarlo si el logcat muestra `rumble embed fallÃ³ (HTTP 403 ...)` en el dispositivo.

### ðŸ”§ FIX rumble WebView + log de cÃ³digo HTTP (05 Oct 2026 v9)
**Log del dispositivo (v8) que lo aclara todo**:
```
source ok 161ms -> https://rumble.com/embed/v78yvtc/
rumble -> 0 URLs              <- SIN mensaje "embed fallÃ³": app.get NO lanzÃ³ excepciÃ³n
```
- **NiceHttp NO lanza excepciÃ³n con 403** (verificado por bytecode: `Requests` no chequea `isSuccessful`, solo envuelve la respuesta; `NiceResponse` expone `code`/`isSuccessful` sin validar). Un challenge de Cloudflare llega como HTML silencioso -> regex sin match -> `0 URLs`.
- El HTML de `https://rumble.com/embed/v78yvtc/` desde PC (curl) **SÃ trae** `https://rumble.com/hls-vod/3x3_LqIIOso/playlist.m3u8`. ConclusiÃ³n: el dispositivo recibiÃ³ el challenge (403) en vez de la pÃ¡gina real.
- **Odysee aparcado**: el embed `$/embed/` es un shell JS (15 KB, sin stream); el API `resolve` da 404; `player.odysee.com` estÃ¡ muerto; el JSON-LD trae `contentUrl` (`player.odycdn.com/api/v3/streams/free/...mp4`) pero responde **401 "edge credentials missing"** (CDN77 con token por sesiÃ³n: ni Referer, ni firma del embed como query/body, ni cookies lo abren). Requiere el player JS vivo o WebView con autoplay+intercept.

**Fix v9 (`emitRumble` + `renderViaWebView`, patrÃ³n TvenvivoProvider)**:
1. `emitRumble` ahora loguea `rumble embed -> code=${resp.code} len=...` (diagnÃ³stico definitivo: 403 = challenge, 200 + len~165K = pÃ¡gina real).
2. ExtracciÃ³n movida a `extractRumbleLinks(clean, callback)` reutilizable (HLS + mp4s).
3. Si OkHttp no trae playlist -> **fallback WebView**: `WebView(appCtx)` en `Dispatchers.Main` con JS activado, `addJavascriptInterface` + sondeo de `outerHTML` cada 2s (hasta 10) buscando `hls-vod`; `withTimeout(26s)` con devoluciÃ³n del Ãºltimo HTML capturado.
4. `DonghualifePlugin.load` ahora guarda `DonghualifeProvider.pluginContext = context` (igual que Tvenvivo).
5. Imports nuevos: `android.webkit.WebView/WebViewClient`, `Dispatchers/withContext/withTimeout/CompletableDeferred/TimeoutCancellationException`.

### ðŸ› Rumble irresoluble por red + Odysee vÃ­a WebView (05 Oct 2026 v10)
**Log del dispositivo (v9)**:
```
rumble embed -> code=403 len=5720 https://rumble.com/embed/v78yvtc/
WebView HTTP 403 -> https://rumble.com/embed/v78yvtc/   (x2)
WebView HTTP 401 -> https://challenges.cloudflare.com/.../pat/.../
WebView timeout, devolviendo Ãºltimo HTML (len=28660)
```

**Veredicto Rumble (PC)**:
- El master `rumble.com/hls-vod/.../playlist.m3u8` con `requests` -> **403** (5799 B). Cloudflare desafÃ­a TODO `rumble.com` para fingerprints bloqueados: aunque se descubriera la URL por otro lado, **ExoPlayer tambiÃ©n recibirÃ­a 403**. Evidencia PC (misma IP): curl -> 200 en todo, `requests` -> 403 en todo = 100% fingerprint, no IP.
- Proxy `r.jina.ai` -> devuelve el challenge (403). Muerto.
- `Odysseusa` del jar es un mirror de Vidara, NO Odysee. **CS3 no trae extractor Odysee.**
- **Rumble queda como estÃ¡**: el cÃ³digo es correcto y funciona donde CF no desafÃ­a. En esta red es imposible por cÃ³digo.

**Cambios Rumble (v10)**:
- Si `resp.code == 403 || == 401` -> **fail rÃ¡pido**: se omite WebView y `loadExtractor` (probado fÃºtil: Challenge-Platform 401 + ~8s quemados). Log: `rumble bloqueado por Cloudflare (403): sin WebView ni loadExtractor`. El episodio pasa de 28-39s a ~2s en este caso.
- Timeout del WebView 26s -> 15s (el auto-solve de CF ocurre pronto o nunca).

**`emitOdysee` nuevo** (el dominio `odysee.com` NO tiene challenge: todo dio 200 desde PC):
1. **VÃ­a rÃ¡pida**: canÃ³nica `https://odysee.com/@canal:cid/stream:sid` (decodificada del path `/$/embed/`), JSON-LD `contentUrl` (`player.odycdn.com/api/v3/streams/free/...mp4`), probe con `Range: bytes=0-0`. Si 200/206 -> se emite directo.
2. **WebView con autoplay**: `interceptMediaViaWebView(url, Regex("(player\.odycdn\.com|\.mp4(\?|$)|\.m3u8)"))` â€” `shouldInterceptRequest` captura el stream que pide el player (el player consigue sus edge-credentials solo). `&autoplay=true` si falta. Timeout 25s.
- Dispatch `url.contains("odysee.com")` **antes** del `loadExtractor` genÃ©rico.
- Logs: `odysee contentUrl -> code=...`, `odysee -> directo OK`, `WebView media capturado: ...`, `odysee -> WebView OK` / `0 URLs`.

**Nota de verificaciÃ³n**: el checker `ghd_check.py` da falsos positivos con `""""` (raw string que empieza/termina en comilla, ej. lÃ­nea `contentUrl` original). `ghd_bal.py` maneja mejor los strings pero TAMPOCO soporta `""""`. Se reescribiÃ³ esa lÃ­nea con `substringAfter/substringBefore` (sin regex) -> balance 0/0/0 real.

### â›” Veredicto Rumble: irresoluble por red en dispositivos desafiados (05 Oct 2026, sin cambio de versiÃ³n)
**El usuario confirmÃ³ que falla igual en celular fÃ­sico y emulador** (misma WiFi). Barrido completo de vÃ­as (todas desde PC salvo indicaciÃ³n):
| VÃ­a | Resultado |
|---|---|
| `app.get` / WebView / ExoPlayer (huella mÃ³vil) | **403** en `/embed/`, master `hls-vod`, `oembed`, `embedJS` |
| Master `hls-vod/.../playlist.m3u8` con fingerprint bloqueado | **403** (5799 B) -> aunque se descubra la URL, ExoPlayer tambiÃ©n fallarÃ­a |
| Variantes `.tar?r_file=chunklist.m3u8` y segmentos en `hugh.cdn.rumble.cloud` | **200 sin headers**, TS vÃ¡lido -> el CDN estÃ¡ abierto, solo `rumble.com` desafÃ­a |
| Proxy `r.jina.ai` | 403 (devuelve el challenge) |
| Google Translate (`rumble-com.translate.goog/embed/...`) | **200 HTML con playlist** (Google sÃ­ pasa CF en pÃ¡ginas) PERO el master por el proxy -> **403** (Google tambiÃ©n recibe challenge en `.m3u8`) |
| CORS proxies (allorigins raw/get, corsproxy.io, codetabs) | 522 / 408 / 403 "domain blocked" / 522 (sus servidores tampoco pasan CF) |
| Cobalt API nueva (`POST api.cobalt.tools/`) | `error.api.auth.jwt.missing` (requiere API key); v7 apagada desde nov-2024 |
| `Odysseusa` del jar | Mirror de Vidara, NO Odysee |
- **ConclusiÃ³n**: con la red del usuario (CF desafÃ­a todo `rumble.com` a huellas mÃ³viles), Rumble no se puede resolver ni reproducir por cÃ³digo. El fail-rÃ¡pido de v10 (`bloqueado por Cloudflare (403)`, episodio en ~2s en vez de ~35s) es el comportamiento correcto.
- **Workaround real**: otra red (datos vs WiFi) o VPN con IP limpia â€” si CF no desafÃ­a, el cÃ³digo v8+ funciona directo (`rumble -> HLS OK`).
- **Ãšltimo recurso (no implementado)**: dependencia `cronet-embedded` (TLS real de Chromium en la app). Pesado (~MBs nativos, empaquetado cs3 incierto) y resultado incierto. Solo con visto bueno del usuario.

### Estado v10
- `build.gradle.kts`: `version = 10`; `plugins.json`: version 10, `fileSize` **pendiente**.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar `blades-guardians-season-2-1`. Esperado: rumble falla rÃ¡pido con `bloqueado por Cloudflare (403)` y **odysee emite link vÃ­a WebView** (`WebView media capturado` -> `odysee -> WebView OK`).
- â¸ï¸ Si el autoplay no dispara el stream en el WebView, el siguiente paso es click JS al botÃ³n play tras `onPageFinished`.

---

## SerieskaoProvider â€” VOE con solver ALTCHA (08 Oct 2026 v22)

### Port desde SoloLatino (verificado ahÃ­ en dispositivo: `ALTCHA resuelto` â†’ `Found M3U8/MP4` â†’ segmentos 200)
- `KaoVoeExtractor` tenÃ­a el MISMO abandono explÃ­cito ante `altcha-widget` (`return false`, lÃ­nea 133) â†’ VOE nunca emitÃ­a.
- Portado: `solveAltcha()` + `solveAltchaPow()` **paralelo (4 hilos, stride, `AtomicLong` ganador, `ensureActive`)** + `pbkdf2Sha256()` + `cookies` opcional en `parseHtml()` + `voeHeaders` a nivel de clase.
- `tryVoeExtraction`: detecciÃ³n ampliada con `altcha-widget` + llamada DIRECTA a `KaoVoeExtractor().parseHtml()` (solver) **antes** de mirrors/WebView (orden v26: ahorra ~15-30s).
- Imports ya existentes (`kotlinx.coroutines.*`, `Base64`, `Mac`/`Atomic*` aÃ±adidos).
- **PoseidonHD**: NO aplica â€” es 100% Byse, sin cÃ³digo VOE.
- `build.gradle.kts`: 21 â†’ **22**; `plugins.json`: 1 â†’ **22** (mismo desync que SoloLatino tenÃ­a; confirmar convenciÃ³n).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :SerieskaoProvider:make --console=plain -q`), instalar y buscar `[Voe] ALTCHA resuelto en N intentos` â†’ `[Voe] Found M3U8`.

### Nota de verificaciÃ³n (08 Oct 2026)
- El checker ingenuo de balance (`ghd_bal.py`) da falsos +/- en archivos con regexes que contienen `"`/`{}` y templates con comillas internas. Se escribiÃ³ `ghd_bal4.py` (mÃ¡quina de estados con pila: code/str/raw/char/comments/templates) â€” tras arreglar su manejo de `""""` (comilla-de-contenido + delimitador), reporta 0/0/0. El `+1` residual verificado como artefacto (mismo resultado en HEAD compilable).

---

## CinehdplusProvider â€” MigraciÃ³n a la nueva estructura (Tailwind) (06 Oct 2026 v3)

### Estructura nueva (verificada)
El sitio se rediseÃ±Ã³ por completo (Tailwind/daisyUI). Nada del markup viejo existe (`card__cover`, `details__title`, `episodios-todos`, `li.clili`, `data-tplayernv`, `#OptYt` â†’ 0 matches).
| Antes | Ahora |
|---|---|
| Cards `div.card__cover` | `<a class="group ..." href="/series-tv-{id}/{slug}/">` y `/pelicula-{id}/{slug}/` (img + `<p>` tÃ­tulo) |
| Detalle serie/peli | JSON-LD `TVSeries`/`Movie` (`name`, `datePublished`, `description`, `image`, `genre[]`) + `og:title/description/image` |
| Episodios DOM (`div.episodios-todos`) | 1 link por episodio: `/episodio-{id}/{slug}-{S}x{E}/` dentro de `div#season-content-{N}` (857/857 server-rendered, sin AJAX) |
| Servidores `li.clili` + `div#id iframe` | `<button data-url="//api.cinehdplus.org/ir/player.php?h=..." data-domain="voe" data-lang="EspaÃ±ol Latino">` (youtube con `data-lang="Oficial"` se salta) |
| Trailer `#OptYt iframe` | `button[data-url*=youtube.com]` â†’ `watch?v=` |

### Lo que NO cambiÃ³
- **Cadena `ir/` intacta** (verificada end-to-end con hash real): `goto.php?h=` â†’ form `input#url` â†’ POST `rd.php` â†’ `input#url` â†’ POST `redir_ddh.php` (`url`, `dl=0`) â†’ form `action` + `#vid`/`#hash` â†’ POST â†’ `link = 'b64'` â†’ `voe.sx/...`.
- PaginaciÃ³n `/series/page/N`, `/peliculas/page/N` (page/1 redirige a canÃ³nica pero trae contenido).
- `/?s=` redirige a `/search/{q}/` con el mismo markup de tarjetas.
- Episodios: langs `EspaÃ±ol Latino`/`Oficial`, domains voe/cdnwish/streamtape/hqq/upstream.

### Rewrite (`CinehdplusProvider.kt`)
- `getMainPage`: tarjetas `a[href*=/series-tv-], a[href*=/pelicula-]` (tÃ­tulo = `img[alt]` o primer `p`, poster `img[src]`); `hasNext` por link `/page/{N+1}/`.
- `search()`: mismo parser sobre `/search/{q}/`.
- `load()`: JSON-LD primero (`name`, `datePublished`, `description`, `image`, `genre`), fallback a `og:*`; `/episodio-` â†’ `MovieLoadResponse` con `movieData = url` (usa `TVEpisode`: serie, `episodeNumber`, regex `-{S}x{E}`); `/pelicula-` â†’ pelÃ­cula; resto â†’ serie con episodios de `div[id^=season-content-]` (temporada del id, `{S}x{E}` del slug, nombre de `h3`/`img[alt]`, URLs absolutas por `fixUrl`).
- Recomendaciones: links de detalle excluyendo self (capped 12). Trailer â†’ `addTrailer`.
- `loadLinks()`: `button[data-url]` (salta youtube, `//` â†’ `https:`, `.m3u8` directo se emite, resto `player.php?h=` â†’ `resolveIrChain()` con la cadena `ir/` original). Retorna `found` (antes `true` siempre).
- `loadSourceNameExtractor` y `fixHostsLinks` sin cambios. `waaw` (StreamSB) sin cambios.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 20000).
- CompilaciÃ³n OK: pendiente de compilar por el usuario (`.\gradlew.bat :CinehdplusProvider:make --console=plain -q`) â€” **no compilado por regla del repo**.
- â¸ï¸ **Pendiente**: compilar, instalar y probar home/search/detalle serie+pelÃ­cula/episodios/servidores (voe, streamtape, etc.).

### ðŸ› Fix search (tarjetas overlay) + logging (06 Oct 2026 v3)
**SÃ­ntoma**: el search no devolvÃ­a nada (ej. `one punch` â†’ `/search/one+punch/` con 2 resultados reales en el HTML).
**Causa raÃ­z**: la pÃ¡gina de bÃºsqueda usa tarjetas DISTINTAS a los listados: `<a href="..." class="absolute inset-0" aria-label="One Punch Man"></a>` **vacÃ­o** (overlay), con el `<img>` fuera en el contenedor `div.group`. `toCard()` exigÃ­a `img` o `p` **dentro** del `<a>` â†’ retornaba null para todo â†’ 0 resultados.
**Fixes**:
- `toCard()`: fallbacks `closest("div.group")?.selectFirst("img")`, `a[aria-label]`, `h2/h3` del contenedor. Sirve para ambos markups (listados + bÃºsqueda + recomendaciones).
- `search()`: `URLEncoder.encode(query, "UTF-8")` (espacio â†’ `+`, igual que el `/search/one+punch/` del sitio).
- **Logs** con `TAG = Cinehdplus` (`adb logcat -s Cinehdplus:V`): `getMainPage` (secciÃ³n, items, hasNext, ms), `search` (query, resultados, ms), `load` (episodio/pelÃ­cula/serie con temporadas+episodios, ms), `loadLinks` (nÂº botones + `domain:lang`, OK/FALLO por servidor, final), `resolveIrChain` (paso exacto que falla: goto/rd/redir/action/vid/link).
- Sin cambio de versiÃ³n (v3 aÃºn no compilada/publicada).

---

## SeriesdonghuaProvider â€” Plugin nuevo (06 Oct 2026 v1)

Sitio: `seriesdonghua.com` (PHP custom, server-rendered, **sin Cloudflare**: todo 200 con UA plano).

### Estructura verificada
| Parte | Ruta / markup |
|---|---|
| Home | `/` (En emisiÃ³n, Nuevos Episodios) |
| Listados | `/donghuas-en-emision`, `/donghuas-finalizados`, `/episodios`, `/genero/` + `?page=N` |
| Search | `/buscar.php?q=` (mismo markup de cards) |
| Serie | `/{slug}/` (h1 tÃ­tulo, og:description/image, `div.genre-pill-list a.genre-pill`, 263/263 eps server-rendered) |
| Episodio | `/{slug}-episodio-{n}/` (`article.episode-card-item[data-ep]`, href, img alt/poster) |
| Secuelas = entradas separadas | (`doupo-cangqiong-7`, `i-will-eternal-4`) â€” sin multi-temporada por pÃ¡gina |
| Servidores | `button.server-tab-btn` (nombre en `span` sin clase, `data-video-id`, `data-server-index`, badges Sub ES/1080p) |
| Subs | No hay softsubs (embeds con Sub ES quemado) |

### Player API (sin auth, verificada)
`POST /api/player/get-server` con `Content-Type: application/json` + `X-CSRF-TOKEN` (de `meta[name=csrf-token]`) + `X-Requested-With: XMLHttpRequest` + Referer/Origin, body `{"video_id":15512,"server_index":0}` â†’ `{"success":true,"embed_url":"https://..."}`. En NiceHttp el JSON va en `requestBody` (nunca `data = String`). Fuentes vistas: dailymotion `geo...player...?video=`, ok.ru `videoembed`, rumble `embed/...?pub=`.

### ImplementaciÃ³n (v1, sin compilar por regla del repo)
- `getMainPage`: home (2 secciones pedidas) + 5 gÃ©neros en paralelo (`En emisiÃ³n`, `Finalizados`, `AcciÃ³n`, `Aventura`, `Cultivo`, `FantasÃ­a`, `Romance` â€” todos verificados 200 con 24 cards); `hasNext` por `a[href$="page=N+1"]`.
- `search()`: `/buscar.php?q=` + `URLEncoder`.
- `load()`: serie â†’ `newTvSeriesLoadResponse(TvType.Anime, season=1)` (rama TvSeries determinista, lecciÃ³n Uniquestream); episodio â†’ `MovieLoadResponse` mÃ­nimo con `movieData=url`; URLs **relativas** â†’ siempre `fixUrl()` (lecciÃ³n inversa a DonghuaLife, que eran absolutas).
- `loadLinks()`: botones â†’ `postPlayerServer()` â†’ dispatch ok.ru/dailymotion/rumble/`.m3u8`/`loadExtractor` (mismo cÃ³digo probado de Donghualife, con `CancellationException` re-lanzada).
- `RumbleExtractor.kt` incluido y registrado (copia mejorada de Donghualife) como red de seguridad.
- `getVideoInterceptor`: solo rama ok.ru (`__sub` no aplica: sin subtÃ­tulos en el sitio).
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `SeriesdonghuaPlugin.kt` (+`pluginContext`), `SeriesdonghuaProvider.kt`, `RumbleExtractor.kt`; `plugins.json` â†’ entrada `SeriesDonghua` v1 (68 entradas, `fileSize: 0` pendiente).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar home/search/load/links. Logs: `adb logcat -s SeriesDonghua:V` y `RumbleExt:V`.

---

## VeranimeProvider â€” Plugin nuevo (08 Oct 2026 v1)

Sitio: `veranime.ninja` (WordPress + tema DooPlay, **sin Cloudflare** pero servidor MUY lento: detalle 19-40s por pÃ¡gina).

### Cadena verificada (end-to-end en PC)
Episodio `/ver/{slug}-episodio-{n}/` â†’ `li.dooplay_player_option` (`data-post`, `data-nume`, `data-type`) â†’ `POST wp-admin/admin-ajax.php` (`action=doo_player_ajax`, form-data, **sin nonce**) â†’ `{"embed_url":"https://saidochesto.top/embed.php?id=..."}` â†’ hub con `go_to_player('URL')` por idioma (`OD_SUB`/`OD_LAT`/`OD_ES`: FileLions, StreamWish, StreamTape, LuluStream, HexLoad, FileMoon, Mp4Upload, Uqload).

### Playback verificado (mirror por mirror)
| Mirror | Estado |
|---|---|
| StreamWish / Uqload | 200 con m3u8+packer â†’ extractor CS3 OK |
| StreamTape | 200 player real â†’ extractor CS3 OK |
| LuluStream / Mp4Upload | Muertos ("deleted/expired") â†’ se saltan solos |

### ImplementaciÃ³n (v1, sin compilar por regla del repo)
- `getMainPage`: `article.item` (tÃ­tulo `div.data h3 a`, poster `img[data-src]` â€” lazy, `src` es placeholder SVG); secciones CatÃ¡logo + 6 gÃ©neros (`/genero/accion/page/2/` verificado); `hasNext` por `a[href$="page=N+1"]`.
- `search()`: `/?s=` + `URLEncoder` + filtro `matchesQuery()` (WP es laxo).
- `load()`: serie â†’ temporadas reales `#seasons .se-c` (`.se-t` + `ul.episodios li`, `div.numerando` "T - E", stills TMDB) con `TvSeriesLoadResponse(TvType.Anime)`; episodio `/ver/...-episodio-N/` â†’ `MovieLoadResponse` mÃ­nimo con `movieData=url`; poster/info vÃ­a `div.poster`, `#info .wp-content`, `nav.genres`, `div.custom_fields`.
- `loadLinks()`: AJAX â†’ hub â†’ mirrors por idioma (**LAT primero**, repo latino) â†’ `fixMirrorHost()` (filemooonâ†’filemoon.sx, uqload.ioâ†’.com) â†’ `loadExtractor` con `withTimeout(25s)` por mirror.
- **Timeouts largos**: detalle/episodio `120L`, listados `60L` (en NiceHttp el timeout va en SEGUNDOS; con 30L las pÃ¡ginas de 40s morirÃ­an).
- Sin WebView/subs/interceptor (embeds con subs quemados; extractores CS3 manejan sus headers).
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `VeranimePlugin.kt`, `VeranimeProvider.kt`; `plugins.json` â†’ entrada `VerAnime` v1 (69 entradas, `fileSize: 0` pendiente).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :VeranimeProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar home/search/load/links. Logs: `adb logcat -s VerAnime:V`.

### ðŸ› Fix search result-item + hubs alternos (08 Oct 2026 v2)
**Log del dispositivo (v1)**: search `one punch` â†’ `0 total`; `loadLinks` EP5 â†’ `opciones=1` y `SIN LINKS` sin ningÃºn log intermedio (ni embed ni hub).

**Causa search**: la pÃ¡gina de resultados usa `div.result-item > article` (`div.title a` + `.image img`), NO `article.item` (ese es solo el widget lateral "ANIMES RECIENTES"). Mi conteo con regex `class="[^"]*item` me engaÃ±Ã³ (matcheaba `w_item_a`).
**Fix**: nuevo `toSearchCard()` (`div.title a`, poster `.image img`); `search()` usa `div.result-item` y solo cae a `article.item` si no hay ninguno (para no devolver widgets). Log si hay result-item sin parsear.

**Causa loadLinks**: el EP5 resuelve a OTRO hub (`darkanimes.com/...`, no saidochesto). Su pÃ¡gina no tiene bloques OD ni `go_to_player` â†’ los 3 `?: continue` saltaban en silencio â†’ 0 links.
**Hallazgo**: darkanimes trae JSON limpio `{"cyberlocker","link","language","quality"}` (14 mirrors: streamtape/netu/filemooon/uqload/filelions/streamwish/savefiles/mxdrop/hexload/mp4upload; `Japones`=SUB, `EspaÃ±ol`=LAT).
**Fixes**:
- Branch por tipo de hub: OD_* â†’ `emitSaidochesto()` (extraÃ­do tal cual); si no â†’ `emitCyberlockerJson()` (orden EspaÃ±ol-latino primero); si no hay items â†’ fallbacks genÃ©ricos (`.m3u8` directos, `iframe[src]` â†’ loadExtractor).
- `fixMirrorHost()` += `uqload.cx/is â†’ uqload.com`.
- Cobertura CS3 verificada por bytecode: filelionsâ†’VidHidePro1-3, mxdropâ†’MxDropTo, hexloadâ†’Hexload, mp4uploadâ†’Mp4Upload, luluvdoâ†’LuluStream. Sin cobertura: savefiles, netuplayer (se saltan solos).

### Estado v2
- `build.gradle.kts`: `version = 2`; `plugins.json`: version 2, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y probar search `one punch` (â†’ One Punch Man) y EP5 de Kage (â†’ links ES/LAT via cyberlocker JSON).

### ðŸ”§ Fix zilla-direct + Byse + mapeos (08 Oct 2026 v3)
**Log del dispositivo (v2)**: OPM emite OK (varios mirrors CS3 funcionan en silencio), pero Link Click EP1 solo trae 5 mirrors SUB y fallan 3: `zilla-networks` (`player.zilla-networks.com/m3u8/{hash}`), `uns` (animeav1), `mega`. AdemÃ¡s `filemooon`/`uqload` dan 0 links en varios episodios. El usuario apunta que el Latino con "byse" (igual que Monoschinos) deberÃ­a funcionar.

**InvestigaciÃ³n (PC + bytecode)**:
- `player.zilla-networks.com/m3u8/{hash}` â†’ **200 `application/x-mpegURL`** (media playlist VOD con segmentos `.html`). Mi check `contains(".m3u8")` lo pasaba por alto (es `/m3u8/`, sin punto). Mismo patrÃ³n que Animeav1 (sus segmentos exigen headers completos o dan 403).
- `filemooon.link/e/...` â†’ `<title>Byse Frontend</title>`: **FileMoon migrÃ³ al stack Byse** (challenge ECDSA + PoW + AES-GCM) que los extractores CS3 no resuelven. De ahÃ­ los `sin links [filemooon]`.
- `uqload.vc` es XUpload clÃ¡sico (**NO** Byse); CS3 cubre uqload.com/.co/.bz/.cx/.xyz pero no `.vc`/`.io`/`.is`.
- `dood.sh` â†’ 301 a `playmogo.com` (con extractor `Playmogo`); sin mapeo, `loadExtractor` no matchea host y da 0 sin intentarlo.
- `MEGA` (`mega.nz/embed/...`) requiere el flujo proxy+AES de Retrotve: pesado, aparcado (otros mirrors cubren).
- `uns` (`animeav1.uns.bio/#codigo`): host propio de AnimeAv1, aparcado.

**Fixes (v3)**:
- `emitCyberlockerJson`: `isPlaylist = contains(".m3u8") || contains("/m3u8/")`; links zilla con `zillaHeaders` (port de `Animeav1Provider.kt:63`, log `hub playlist directa`).
- `getVideoInterceptor` nuevo: inyecta `zillaHeaders` a todo `zilla-networks.com` (master + segmentos).
- `ByseExtractor.kt` portado de Monoschinos (retagueado a `VerAnime`, autocontenido: Jackson + OkHttp propio + JCA).
- `emitByse()` nuevo: fetch del embed, si trae "Byse Frontend" â†’ `ByseHttpExtractor().extract(url, hubUrl, hubHost)` â†’ emite sources + subtÃ­tulos; si no, `false` (cae a `loadExtractor`).
- Ruta filemoon/byse en `emitCyberlockerJson`: Byse primero, `loadExtractor` despuÃ©s.
- `fixMirrorHost()` += `filemoon0.topâ†’filemoon.sx`, `dood.shâ†’playmogo.com`, `uqload.(io|is|vc|com)â†’uqload.cx`.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y probar (1) Link Click EP1 â†’ `hub playlist directa [zilla-networks]` + zilla reproduce; (2) episodio con filemoon â†’ `byse sources=N`; (3) `adb logcat -s VerAnime:V`.

### âš¡ Fix hubs sin latino + speedup paralelo (08 Oct 2026 v4)
**Log del dispositivo (v3)**:
- Link Click EP1-3: hubs con **solo 5/5/4 mirrors Japones, CERO Latino** en todos. **No es bug**: el sitio no subiÃ³ latino para Link Click. La ruta Byse queda lista por si aparece.
- JJK S2E19: 54 mirrors (18 LAT + 9 ES + 27 SUB), **todos** dan 0 links incluso streamwish/uqload (vivos desde PC) en 109s. Causa probable: degradaciÃ³n transitoria de red en el dispositivo (OPM 30 min antes sÃ­ emitÃ­a con el mismo cÃ³digo). Riesgo real: 54 mirrors en serie rozan el kill de 120s de CS3.
- **Audio**: StreamWish etiquetado "Subtitulado" reproduce en latino â†’ **mislabel del sitio** (el JSON del hub trae `language` errÃ³neo). El provider emite la etiqueta tal cual; nada que arreglar en cÃ³digo. Si el archivo trae pista dual, ExoPlayer permite cambiarla en el player.

**Fixes (v4)**:
- Paralelizado con `amap` (precedente SoloLatino): mirrors en paralelo **dentro** de cada grupo de idioma (se mantiene prioridad LAT>ES>SUB). 109s â†’ ~15-25s.
- Refactor: `resolveSaidoMirror()` + `resolveCyberMirror()` extraÃ­dos (retornan Boolean; `results.any { it }`).
- `amap` verificado en el jar: `ParCollectionsKt.amap(List<A>, suspend (A)->B): List<B>` (paquete raÃ­z, cubierto por wildcard).

### Estado v4
- `build.gradle.kts`: `version = 4`; `plugins.json`: version 4 (+ nombre visible `AnimeNINJA`), `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y probar JJK S2E19 (deberÃ­a resolver en ~20s) + Link Click EP1 (zilla directo).

### ðŸ”§ Fix regex con espacios: Latino/Castellano invisibles (08 Oct 2026 v5)
**Reporte del usuario (v4)**: en la web, el Latino de Link Click EP1 muestra 2 links (`byseraguci`, `luluvdoo`), pero el plugin solo emitÃ­a Subtitulado.
**Causa raÃ­z**: el hub mezcla DOS formatos JSON. Mi regex exigÃ­a sin espacios y solo capturaba el formato Japones:
```json
{"cyberlocker":"netu","link":"...","language":"Japones","quality":"720p"}          // matcheaba
{"cyberlocker": "byseraguci","link":"https://byseraguci.com/e/...","language": "EspaÃ±ol-Latino", "quality": "720p"}   // NO matcheaba
```
**Fix**: regex tolerante `\{\s*"cyberlocker"\s*:\s*"([^"]+)"\s*,...` (verificado en PC: 5 â†’ 9 items en EP1). `EspaÃ±ol-Latino`/`EspaÃ±ol` â†’ Latino por reglas existentes (`contains("lat")`, else â†’ Latino). `byseraguci.com` cae en la ruta `emitByse` (contiene "byse"); `luluvdoo.com`/`luluvdo.com` tienen extractor dedicado en CS3 (`Luluvdoo`â†’luluvdoo.com, `LuluStream`â†’luluvdo.com, verificado por bytecode) â†’ sin mapeo.
**Nota**: `EspaÃ±ol` a secas se mapea a Latino (coherente con OPM, donde esos links reproducÃ­an latino real).

### Estado v5
- `build.gradle.kts`: `version = 5`; `plugins.json`: version 5, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Link Click EP1 â€” esperado `hub cyberlocker: 9 mirrors` + links `Latino [byseraguci]` (vÃ­a `byse sources=N`) y `Latino [luluvdoo]`.

### ðŸ”§ Fix "EspaÃ±ol" = Castellano, no Latino (08 Oct 2026 v6)
**Reporte del usuario (v5)**: los dos links byse salÃ­an como "Latino", pero uno es Castellano.
**Evidencia (hub, lÃ­neas 168-170)**: la lista de idiomas del propio sitio dice `{"name": "EspaÃ±ol-Latino","icon": "latin_spanish.png"...}` vs `{"name": "EspaÃ±ol","icon": "spanish.png"...}`. "EspaÃ±ol" a secas es **EspaÃ±a** (castellano). Se corrige la nota de v5 (donde se habÃ­a asumido latino por el caso OPM â€” ahÃ­ el uploader etiquetÃ³ mal o el caso era OD_LAT explÃ­cito; la etiqueta canÃ³nica del sitio manda).
**Fix**: `EspaÃ±ol-Latino`/`*lat*` â†’ Latino; `*cast*` o `EspaÃ±ol` exacto â†’ Castellano; `*jap*` â†’ Subtitulado; resto â†’ etiqueta cruda del sitio. Orden: Latino(0), Castellano(1), resto(2).

### Estado v6
- `build.gradle.kts`: `version = 6`; `plugins.json`: version 6, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y probar Link Click EP1 â€” esperado `Latino [byseraguci]` (1 link) + `Castellano [byseraguci]` (1 link) + `Latino/Castellano [luluvdoo]`.

### ðŸ”§ Fix StreamWish vÃ­a WebView (09 Oct 2026 v7)
**Reporte del usuario (v6)**: Maou Gakuin EP5 â€” en la web sale StreamWish Latino, pero el plugin daba `sin links para MULTISERVER Latino [streamwish] (https://streamwish.to/e/...)` y solo quedaba Streamtape. (FileMoon/Byse dio 404 `video record missing` = video borrado, nada que hacer.)
**Causa raÃ­z**: `streamwish.to/e/` devuelve **challenge a HTTP plano** (misma firma que en SoloLatino: 200 len~819 sin jwplayer/sources) y el extractor CS3 `loadExtractor` da 0 links. En la web el challenge se auto-resuelve (navegador real).
**Fix (port de SoloLatino, verificado ahÃ­ en fÃ­sico)**:
- `VeranimePlugin.load` guarda `VeranimeProvider.pluginContext`.
- `renderViaWebView()` + `webViewMutex` + `catch (t: Throwable)` (un solo WebView vivo, sin matar la app), `SW_READY_JS`/`SW_DUMP_JS`.
- `parseStreamwishHtml()` (m3u8 directo â†’ `file:`/`src:` â†’ mp4 â†’ unpack Dean Edwards â†’ iframes) + `unpackDeanEdwards()` copiados; regex con `\}` escapado (el `}` suelto es error en ICU-Android, regla Plushd v3).
- Hook en `resolveSaidoMirror` y `resolveCyberMirror`: si `loadExtractorCollect` falla y el host es streamwish â†’ `emitStreamwishWebView()` (emite `"$label [SW-Web]"`, referer = pÃ¡gina SW).
- `getVideoInterceptor` += rama `premilkyway` (UA + Referer + Origin derivado del referer del link, timeouts 30s) para master/segmentos SW.
- Balance de llaves verificado con checker que respeta `"""` y `${...}` (`%TEMP%\opencode\bal_check.py`).

### Estado v7
- `build.gradle.kts`: `version = 7`; `plugins.json`: version 7, `fileSize` **pendiente** (sigue 0).
- v7 NUNCA se publicÃ³: antes de compilarla, el usuario reportÃ³ que el fallo es por **estado**, no por challenge permanente (ver v8). Si se compilÃ³ v7 local, descartarla.
- ~~Nota diagnÃ³stico (09 Oct 2026, usuario)~~ **CORREGIDA por v8**: el challenge NO es por fingerprint del dispositivo â€” en el MISMO celular con datos borrados (app vacÃ­a) StreamWish sale por `loadExtractor` directo; con datos (favoritos, etc.) no sale. Es estado (cookies rancias), no hardware.

### ðŸ”§ Fix StreamWish con cliente sin cookies (09 Oct 2026 v8)
**Reporte del usuario (v7 sin compilar)**: "no es que en mi fÃ­sico sÃ­ [y emulador no], sino que al borrar todos los datos del celular, en una app completamente vacÃ­a reciÃ©n me sale el streamwish. Con todos mis datos (favoritos y demÃ¡s) no me sale, solo en aplicaciones vacÃ­as".
**Causa raÃ­z (hipÃ³tesis fuerte)**: Cloudflare deja cookies (`cf_clearance`, `__cf_bm`, sesiÃ³n) en el **jar compartido de OkHttp**. Con el tiempo quedan rancias â†’ el servidor responde challenge en loop â†’ `loadExtractor` da 0 links. Al borrar datos el jar queda limpio y la red pasa sin challenge â†’ funciona. Afecta a `streamwish.to` (Streamtape no usa ese challenge y por eso sÃ­ sale con datos).
**Fix**: replicar la "app vacÃ­a" solo para StreamWish â€” `freshHttpClient` (OkHttp propio con `CookieJar.NO_COOKIES`, timeouts 15s, nombres totalmente calificados `okhttp3.*` para evitar ambigÃ¼edad con el wildcard `com.lagradost.cloudstream3.*`):
- Orden en ambas ramas (saido/cyber): `loadExtractorCollect` â†’ `emitStreamwishFresh()` (fetch sin cookies + `parseStreamwishHtml`, emite `"$label [SW]"`, log `[SW] fresh fetch len=` / `[SW] fresh OK`) â†’ `emitStreamwishWebView()` (red de seguridad v7).
- `fetchStreamwishFresh` con `withTimeoutOrNull(25s)` + `withContext(Dispatchers.IO)`; re-lanza `CancellationException`.

### Estado v8
- `build.gradle.kts`: `version = 8`; `plugins.json`: version 8, `fileSize` **pendiente** (sigue 0).
- âœ… **VERIFICADO v8 en fÃ­sico (09 Oct 2026, 23:48, Maou Gakuin EP5, app con datos)**: `[SW] fresh fetch len=819` (challenge TAMBIÃ‰N sin cookies â†’ en este dispositivo el challenge es por fingerprint, no solo por cookies rancias) â†’ `[SW] WebView fallback` â†’ `[WebView] listo antes de tiempo` â†’ `[SW] M3U8 (eval): https://2goita23a7njj.premilkyway.com/hls2/...` â†’ `[SW] WebView OK: 1 urls` â†’ `loadLinks -> OK 13615ms`. **Link `[SW-Web]` confirmado por el usuario en la lista.**
- â¸ï¸ **Pendiente**: reproducir el link `[SW-Web]` y confirmar segmentos `[intercept] SW CDN response: 200` (rama premilkyway del interceptor, nueva en v8, aÃºn sin probar en playback).
- âœ… **VERIFICADO playback (09 Oct 2026, 23:53-23:54, mismo EP5)**: master `200 application/vnd.apple.mpegurl`, variantes f1/f3 en 200, segmentos seg-15..seg-24 `200 video/MP2T` â€” todo por la rama `premilkyway` del interceptor. StreamWish Latino reproduce completo en app con datos.

### ðŸ”§ Fix compilaciÃ³n: `takeIf { it.isNotEmpty() }` en receivers nulables (08 Oct 2026)
**Error**: `Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'String?'` (lÃ­nea 286).
**Causa**: en cadenas como `selectFirst(...)?.text()?.trim().takeIf { it.isNotEmpty() }`, el `it` dentro de `takeIf` es `String?` (el `?.` propaga nulabilidad) e `isNotEmpty()/isNotBlank()` exigen receptor no-nulo.
**Fix global** (`replaceAll`): `takeIf { it.isNotEmpty() }` â†’ `takeIf { !it.isNullOrEmpty() }`, `takeIf { it.isNotBlank() }` â†’ `takeIf { !it.isNullOrBlank() }` (semÃ¡ntica idÃ©ntica, `isNullOr*` acepta `CharSequence?`).
**Regla para futuros providers**: despuÃ©s de cualquier `?.`, dentro de `takeIf`/`let` usar siempre `isNullOrEmpty()/isNullOrBlank()`, nunca `isNotEmpty()/isNotBlank()` directo.

### ðŸ› Fix get-server sin embed + filtro de search (08 Oct 2026 v2)
**Log del dispositivo (v1)**: home/search/load OK (7 secciones Ã— 24, `search 'blades' -> 24`, series con episodios), pero `player/get-server v=14922 s=0..3 -> sin embed_url` en los 4 servidores (DM/OK.ru/Rumble/VOE) â†’ `SIN LINKS`. AdemÃ¡s el search devolvÃ­a 24 resultados irrelevantes.

**InvestigaciÃ³n (PC)**:
- El MISMO video (14922) por API responde `200 {"success":true,"embed_url":...}` en los 4 servidores (incluido VOE `voe.sx/e/...`). La API estÃ¡ bien; el dispositivo recibe otra cosa.
- El log v1 no mostraba QUÃ‰ recibÃ­a (solo "sin embed_url") â†’ imposible distinguir 419/500 HTML de `success:false` JSON.
- Search `/buscar.php?q=blades` devuelve 24 cards, casi todas irrelevantes (`Xi Xing Ji`, `Gu An`...), pero `Blade of The Guardians 2` SÃ estÃ¡ (#19). El buscador del sitio es laxo por diseÃ±o.

**Fixes**:
- `postPlayerServer()`: captura `resp.code` + body; si no hay `embed_url` loguea `code=... sin embed_url: <200 chars del body>` (distingue HTML de error de JSON). AÃ±adido header `Accept: application/json`.
- `search()`: filtro de relevancia `matchesQuery()` â€” TODOS los tokens del query (y su singular si termina en `s`) deben aparecer en el tÃ­tulo. `blades` â†’ `blade` matchea "Blade of The Guardians 2", descarta las 23 restantes. Log: `N total, M filtrados`.

### Estado v2
- `build.gradle.kts`: `version = 2`; `plugins.json`: version 2, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar, instalar y (1) ver el log `player/get-server ... code=... sin embed_url: ...` para diagnosticar la respuesta real del dispositivo; (2) probar search `blades` â†’ debe devolver 1 resultado.

### ðŸ› Fix 419 CSRF token mismatch (08 Oct 2026 v3)
**Log del dispositivo (v2)** â€” el diagnÃ³stico funcionÃ³ a la primera:
```
player/get-server v=14922 s=0 -> code=419 sin embed_url: {"message": "CSRF token mismatch.",
"exception": "Symfony\Component\HttpKernel\Exception\HttpException", ...}
```
- El token `X-CSRF-TOKEN` SÃ se envÃ­a (viene del meta fresco), pero Laravel lo ata a la **sesiÃ³n**: sin la cookie de sesiÃ³n del GET, crea sesiÃ³n nueva en el POST â†’ token invÃ¡lido â†’ 419. Desde PC funciona porque `requests.Session` persiste cookies; el jar de CS3 (`NiceResponse` expone `code` pero no valida nada, y su jar no siempre persiste el `CustomCookieJar` entre `app.get`/`app.post`).
- **Fix**: round-trip manual de cookies (precedente: `bypass()` de Netmirror parsea `Set-Cookie` a mano). `loadLinks` guarda `pageResp.cookies` (`Map<String,String>` de NiceHttp) como header `Cookie: k=v; ...` y lo pasa a `postPlayerServer()`. Si el jar ya enviaba sus cookies, los valores son idÃ©nticos (mismo GET) â†’ duplicado inofensivo. Log nuevo: `loadLinks cookies=[nombres] code=...` (solo nombres, sin valores).
- **Nota search**: el log prueba que SeriesDonghua devuelve exactamente 1 resultado para `blade` y `blades`. Si la UI muestra 3, los otros 2 vienen de OTROS providers instalados (CS3 busca en todos a la vez: DonghuaLife, etc.). Verificar por etiqueta de provider en cada card.

### Estado v3
- `build.gradle.kts`: `version = 3`; `plugins.json`: version 3, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), instalar y probar el episodio de Blade â€” esperado: `player/get-server v=14922 s=0 -> ok` + links DM/OK.ru/Rumble/VOE.

### ðŸ› Fix DM sin qualities + mirrors muertos + cap loadExtractor (08 Oct 2026 v4)
**Log del dispositivo (v3)**: EP1 perfecto (DM OK + subs, ok.ru HLS, cookies OK, search 3/3). EP8 (`blades-of-the-guardians-8`): DM `sin qualities` para `k2dpzipls098HizodoS` (pero ok.ru HLS salva el episodio â†’ OK global).

**InvestigaciÃ³n (PC)**:
- El metadata de `k2dpzipls098HizodoS` responde **200 CON `qualities.auto`** ("BOTG 008"). El dispositivo recibiÃ³ JSON sin qualities â†’ varianza por IP/throttle de DM, no video muerto.
- **VOE muerto**: `voe.sx/e/atrym6yf0gvl` â†’ **404 real** del sitio. Correcto ignorarlo (en la web se reproduce por DM/ok.ru).
- **Embedwish muerto**: `embedwish.com/e/...` â†’ 200 de 426 B con "File is no longer available as it expired or has been deleted". TambiÃ©n correcto ignorarlo. Pero su `loadExtractor` **colgÃ³ ~17s** antes de fallar.
- El usuario confirma que en la web SÃ reproduce Dailymotion (coherente: el video estÃ¡ vivo).

**Fixes**:
- `emitDailymotion`: extraÃ­do `fetchDmMeta(id, referer, noHeaders)` (fetch + parse + log); **hasta 2 intentos** cuando falta `qualities` (el 2Âº sin headers). Log con `keys=` + `err=` del JSON para diagnosticar la varianza.
- `loadExtractorCollect`: `withTimeout(25_000L)` â€” un mirror muerto ya no atasca el episodio (solo el fallback genÃ©rico; los extractores propios no se tocan).
- Sin cambio en Rumble/VOE/Embedwish (comportamiento correcto actual: fail rÃ¡pido / 0 links).

### Estado v4
- `build.gradle.kts`: `version = 4`; `plugins.json`: version 4, `fileSize` **pendiente** (sigue 0).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :SeriesdonghuaProvider:make --console=plain -q`), instalar y probar EP8 de Blades â€” esperado: `dailymotion ... -> OK` (al 1Âº o 2Âº intento) + `ok.ru HLS`; Embedwish/VOE siguen en 0 links (muertos) pero sin colgar.

### ðŸ› Fix search: el parÃ¡metro correcto es `?s=`, no `?q=` (08 Oct 2026 v3, reportado por el usuario)
**El usuario indicÃ³**: en `https://seriesdonghua.com/buscar.php?s=blade` salen 3 resultados (`Blade of The Guardians 2`, `Blades of the Guardians`, `Blade of Vengers`).
**Verificado**: el `<input>` del form es `name="s"`. Con `?s=blade` â†’ 3 relevantes; con `?q=blade` el sitio **ignora** el parÃ¡metro y devuelve 24 sin filtrar (de ahÃ­ venÃ­a el ruido que el filtro `matchesQuery()` tenÃ­a que limpiar).
**Fix**: `buscar.php?q=` â†’ `buscar.php?s=` en `search()`. Se mantiene `matchesQuery()` como red de seguridad. Sin cambio de versiÃ³n (v3 aÃºn no compilada).

### Estado v9
- `build.gradle.kts`: `version = 9`; `plugins.json`: version 9, `fileSize` **pendiente**.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar `blades-guardians-season-2-1` (fuentes Rumble+odysee). Buscar `rumble embed -> code=` (confirma 403) y `rumble -> HLS OK` vÃ­a WebView.
- â¸ï¸ Odysee sigue sin extractor (serÃ¡ `emitOdysee` con WebView+autoplay en v10 si hace falta).

### Estado v8
- `build.gradle.kts`: `version = 8`; `plugins.json`: version 8, `fileSize` **pendiente**.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar un episodio con rumble (ej. `record-mortals-journey-immortality-season-6 EP5`). Buscar en logcat `rumble -> HLS OK`.
- â¸ï¸ Si rumble da 403 en el dispositivo (Cloudflare tambien bloquea a OkHttp movil), la alternativa es WebView (`WebViewResolver`) â€” avisar.

### Estado v7
- `build.gradle.kts`: `version = 7`; `plugins.json`: version 7, `fileSize` **pendiente**.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y probar. Para diagnosticar: `adb logcat -s DonghuaLife:V` y buscar `dailymotion` â€” los 4 mensajes de log distinguishes exactamente el fallo (red / no-JSON / sin qualities / 0 URLs).
- â¸ï¸ Pendiente de verificar en dispositivo: rumble / odysee / vk (siguen yendo por `loadExtractor`; si alguno falla tambien, se Vera en el log `sin links para <label>`).

### Estado v6
- `build.gradle.kts`: `version = 6`; `plugins.json`: version 6, `fileSize` **pendiente** tras compilar.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DonghualifeProvider:make --console=plain -q`), instalar y comprobar que `loadLinks data='https://donghualife.com/watch/...'` (con `/watch/` y **sin** `watch:`) trae `fuentes=N` y emite enlaces.

---

## SeriesKao + SoloLatino â€” Fix VOE: stub JS â†’ mirror + POST 419 (09 Oct 2026; SeriesKao v25, SoloLatino v29)

### ðŸ› SÃ­ntoma
- SeriesKao: episodio T1C3 de "El ChapulÃ­n Colorado" (y cualquier VOE) â†’ log `[Voe] POST solucion -> code=419`, sin links. En la web SÃ reproduce.
- El usuario confirmÃ³ que el 419 era el Ãºnico fallo; el resto del flujo (byse/vidhide/sw) OK.

### âœ… Causa raÃ­z (verificada desde PC, `voe_mirror_test2.py`/`voe_verify.py`)
- **VOE cambiÃ³ de flujo**: `https://voe.sx/e/{hash}` ahora devuelve **200 con un stub JS de 759 B**:
  ```html
  <title>Redirecting...</title>
  if (localStorage.permanentToken) { const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.set('permanentToken', temporaryToken); window.location.href = currentUrl.toString(); }
  else { window.location.href = 'https://teresapoliticallearn.com/e/{hash}'; }
  ```
  (ambas ramas y tambiÃ©n el else). El **gate ALTCHA real vive en el mirror** (`teresapoliticallearn.com`, rotativo), no en `voe.sx`.
- El provider **no seguÃ­a el redirect JS** (OkHttp no ejecuta JS): `finalUrl` seguÃ­a siendo `voe.sx` â†’ el HTML no tenÃ­a `altcha-widget` â†’ caÃ­a en `no m3u8/mp4 found` â†’ `parseHtml` (stub, sin encoded string) â†’ `tryMirrors` (lista `["yip.su","tubelessceliolymph.com"]`, **sin** el mirror nuevo) â†’ **WebView** (renderiza y ejecuta el JS â†’ llegar al gate del mirror y auto-resolver) â†’ `parseHtml(rendered, finalUrl=voe.sx, webViewCookies(voe.sx))`.
- `solveAltcha` extrae `_token` y `challenge` correctos (del HTML del mirror), pero hace `app.post(pageUrl=voe.sx, ...)` con cookies de `voe.sx` â†’ **host/sesiÃ³n equivocados** â†’ **419**.
- **No era doble-encoding**: verificado por bytecode de NiceHttp 0.4.13 â†’ `UtilsKt.getData` usa **`okhttp3.FormBody.Builder.addEncoded(key, value)`** (no `add()`), asÃ­ que NiceHttp **NO re-encodea**. El `URLEncoder.encode(base64)` de Kotlin es el **Ãºnico** encoding, correcto. (Confirmado en PC: `altcha` con base64 **crudo** + form-encode de `requests` = single-encode â†’ POST 200 â†’ player real; con `quote()` extra = double-encode â†’ gate re-renderizado 3156 B.)

### ðŸ”§ Fix aplicado (idÃ©ntico en ambos)
En `tryVoeExtraction` (Serieskao `SerieskaoExtractors.kt:912`; SoloLatino `SoloLatinoProvider.kt:1162`):
1. Tras el GET inicial, **seguir el redirect JS** hasta 3 saltos:
   ```kotlin
   val jsRedirectRegex = Regex("""window\.location\.href\s*=\s*['\"](https?://[^'\"]+)['\"]""")
   // captura SOLO literales http(s) (ignora la rama currentUrl.toString())
   ```
   reasigna `finalUrl`/`finalHtml`/`finalCookies` (mergando cookies: `finalCookies + rr.cookies`) en cada salto. Guardas: `target.isNullOrBlank() || target == finalUrl` â†’ break.
2. `tryMirrors()` += `"teresapoliticallearn.com"` al inicio de la lista (belt-and-suspenders; en Serieskao `mResp.cookies` ya se pasaba; en SoloLatino se **aÃ±adiÃ³** `mResp.cookies` que antes se descartaba).
3. Como `finalUrl` ahora es el mirror, el path WebView y `webViewCookies(finalUrl)` usan el host del mirror â†’ sin 419.

### âœ… Verificado desde PC (flujo completo real)
- Stub â†’ mirror; gate ALTCHA en `teresapoliticallearn.com/e/{hash}` (200, 3156 B); `_token` + `altcha-widget challenge` OK; PoW resuelto (counter=16); POST **raw-b64** â†’ **200, 135730 B**, `gate=False`, `<script type="application/json">` con `["DROH...` (lo que extrae `parseHtml`) y `<title>Watch {hash}.mp4 - VOE...` = player real. Con double-encode â†’ 3156 B + gate.

### Estado
- **SeriesKao v25** (`build.gradle.kts` 24â†’25; `plugins.json` version 24â†’25) â€” pendiente `fileSize`.
- **SoloLatino v29** (`build.gradle.kts` 28â†’29; `plugins.json` version 28â†’29) â€” pendiente `fileSize`. (Nota: SoloLatino v28 ya estaba verificado en dispositivo para el cierre por OOM; este cambio es aditivo, no lo revierte.)
- â¸ï¸ **Pendiente (usuario)**: compilar `.\gradlew.bat :SerieskaoProvider:make --console=plain -q` y `.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`, actualizar `fileSize` en `plugins.json`, instalar y probar un episodio con VOE. Buscar en logcat `[Voe] JS redirect (1) -> https://teresapoliticallearn.com/...` â†’ `[Voe] POST solucion -> code=200` â†’ `[Voe] Found M3U8/MP4`.
- â¸ï¸ **Nota**: el dominio mirror es rotativo; si cambia, el fix lo sigue solo (regex genÃ©rica). Solo `tryMirrors` quedarÃ­a desactualizado (red de seguridad secundaria).

### ðŸ§© Regla derivada
- Antes de mirar el solver ALTCHA, **resolver el redirect JS** del embed: `window.location.href='...'` es client-side y OkHttp **no** lo sigue. El host del `POST`/`_token`/cookies DEBE ser el del mirror, no `voe.sx`.

---

## SeriesKao + SoloLatino â€” VOE verificado en fÃ­sico + etiqueta "Voe" (09 Oct 2026; SoloLatino v30, Serieskao v26)

### âœ… SOLOLATINO v29 VERIFICADO EN DISPOSITIVO (09 Oct 2026, 16:20)
- Episodio "somos-osos-2015" T4E11 (Escandalosos). Log completo del fix v29:
  - `[Voe] JS redirect (1) -> https://teresapoliticallearn.com/e/6owdslxfwypt` (el mirror se resuelve solo).
  - `[Voe] contest detectado` â†’ `challenge -> code=200 len=278 cookies=[__ddg8_, __ddg10_, __ddg9_, __ddg1_, XSRF-TOKEN, voe_session]`.
  - `[Voe] ALTCHA resuelto en 408 intentos (32480ms, 4h)` (PoW paralelo tardÃ³ ~32 s en el mÃ³vil â€” normal con prefijo difÃ­cil).
  - `[Voe] Found M3U8` (`ugc-cdn-caching-n3ad05wdiiynz8qnwf.cloudwindow-route.com/engine/hls2-c/01/17661/6owdslxfwypt_,n,.urlset/index-v1-a1.m3u8?t=...&e=14400...`) + `[Voe] Found MP4` (`.../engine/download/01/17661/...`).
  - `loadLinks FIN total emitidos=7 servidores=2`; `[intercept] CDN response: 200 video/MP2T` seg-1..6 â†’ **reproduce, la app NO se cierra**.
- **El cierre de la app del intento 16:13 NO se reproduce** (sin FATAL EXCEPTION / lowmemorykiller capturado): el reintento 16:20 completÃ³ igual. Protecciones v28 intactas (`webViewMutex` + `catch (t: Throwable)`, un solo WebView serializado en los logs).
- **IntercepciÃ³n del CDN VOE**: `cloudwindow-route.com` NO estÃ¡ en `cdnDomains`, pero matchea por `cdnPaths` **`.urlset/`** â†’ el m3u8 VOE sÃ­ recibe UA/Referer/Origin. El MP4 (path `/engine/download/`) no matchea â€” funciona directo.

### ðŸ› Fix etiqueta: los enlaces VOE salÃ­an como "SoloLatino" (09 Oct 2026)
- **SÃ­ntoma (usuario)**: "no me sale voe, solo un enlaces de nombre SÃ³laLatino". En la lista de enlaces los sources VOE aparecÃ­an con nombre "SoloLatino" / "SoloLatino MP4".
- **Causa raÃ­z**: `VoeExtractor.parseHtml` / `KaoVoeExtractor.parseHtml` usan el parÃ¡metro `sourceName` (= nombre del provider, necesario como `source` para que `getApiFromNameNull` encuentre el interceptor) TAMBIÃ‰N como **nombre visible**: `M3u8Helper.generateM3u8(sourceName, ...)` y `newExtractorLink("$sourceName MP4", "$sourceName MP4", ...)`.
  - `ExtractorLink.name` es **inmutable** (solo `getName`, sin `setName`) â†’ el label hay que fijarlo en la creaciÃ³n.
  - `M3u8Helper2.generateM3u8(source, masterUrl, referer, quality, headers, name = source)` acepta un 6Âº parÃ¡metro `name` (verificado por bytecode: en el mÃ©todo, slot 1=source y slot 6=name se cargan como 1Âº y 2Âº argumento de `newExtractorLink` â†’ `name: String = source`).
- **Fix** (en `Extractors.kt` `VoeExtractor.parseHtml` y `SerieskaoExtractors.kt` `KaoVoeExtractor.parseHtml`):
  - `generateM3u8(...)` += `name = "Voe"` â†’ las variantes HLS se listan como **"Voe"** (source sigue siendo `"SoloLatino"`/`"SeriesKao"` â†’ interceptor intacto).
  - MP4: `newExtractorLink("$sourceName MP4", "$sourceName MP4", ...)` â†’ `newExtractorLink(sourceName, "Voe (MP4)", ...)` (corrige etiqueta Y el source del interceptor).
- **Ruta `getUrl` no se tocÃ³**: el fallback `loadSourceNameExtractor` ya re-etiqueta con `"$source[${link.source}]"` y source `"SoloLatino"` (SoloLatinoProvider.kt:873-886).

### Estado
- **SoloLatino v30** (`build.gradle.kts` 29â†’30; `plugins.json` version 29â†’30) â€” `fileSize` **pendiente** tras compilar.
- **SeriesKao v26** (`build.gradle.kts` 25â†’26; `plugins.json` version 25â†’26) â€” `fileSize` **pendiente** tras compilar.
- Archivos tocados: `SoloLatinoProvider/src/.../Extractors.kt`, `SerieskaoProvider/src/.../SerieskaoExtractors.kt`, `SoloLatinoProvider/build.gradle.kts`, `SerieskaoProvider/build.gradle.kts`, `plugins.json`, `AGENTS.md`.
- âœ… **VERIFICADO SoloLatino v30 en fÃ­sico (09 Oct 2026, 16:58)**: "somos-osos-2015" T4E11 completo â€” `[Voe] JS redirect (1)`, `challenge code=200`, `ALTCHA resuelto en 116 intentos (15891ms, 4h)`, `Found M3U8/MP4`, `loadLinks FIN total emitidos=7 servidores=2`, player con segmentos `200 video/MP2T`. **Etiqueta "Voe" confirmada por el usuario en la lista.**
- âœ… **VERIFICADO SeriesKao v26 en fÃ­sico (09 Oct 2026, 17:00-17:01)**: ChapulÃ­n T1C3 (`voe.sx/e/6i0pzhzdn3i7`) completo â€” search/load OK (396 episodios, 7 temporadas), vidhide solo hls4 (hls2/hls3 â†’ 502, probe los descarta bien), SW vÃ­a WebView OK, `[Voe] JS redirect (1) -> teresapoliticallearn`, `ALTCHA resuelto en 193 intentos (10960ms, 4h)`, `POST solucion -> code=200 len=135730 gate=false`, `Found M3U8/MP4`, `3 links`, player con master/variante/seg-1..3 en 200. **Etiqueta "Voe" confirmada por el usuario.**
- âš ï¸ **PatrÃ³n pendiente (no bloqueante)**: tras instalar/actualizar el plugin, el **primer** play cierra la app (16:13, 16:56); al reabrir y reproducir de nuevo, funciona (16:20, 16:58, 17:01). Causa sin confirmar â€” la prÃ³xima vez que pase en un primer intento, capturar las lÃ­neas de muerte del sistema (`FATAL EXCEPTION` / `lowmemorykiller` / `Process .* has died` / `WIN DEATH`) sin filtro de tag.
- âœ… **VEREDICTO sin-filtro (10 Oct 2026, 07:46, log completo del sistema)**: el usuario capturÃ³ el buffer entero sin filtrar. **No hay crash**: cero `FATAL EXCEPTION`, cero `lowmemorykiller` para la app (ojo: el buffer de kernel NO se capturÃ³, asÃ­ que un LMK no se verÃ­a aquÃ­).
- âš ï¸ **CORRECCIÃ“N del veredicto anterior (10 Oct 2026, reporte del usuario)**: "remove task" NO prueba que el usuario quitara la app de recientes â€” tambiÃ©n se registra al vaciarse la tarea (salir con Back) o en limpieza tras muerte. El usuario confirma que NO la quita; la app se cierra sola. Dos modos de fallo distintos:
  1. **Celular A**: reproduce bien, minutos despuÃ©s **se congela TODA la pantalla + reproductor** y luego se cierra en pleno playback. Pantalla completa congelada = stall del sistema, no excepciÃ³n de app (sin FATAL). Nuestro cÃ³digo durante playback es solo el interceptor (inyectar headers, trivial).
  2. **Celular B (este log)**: el proceso viejo (pid 23505) muriÃ³ a las 07:46:10 (`has died: fg TOP`, sin FATAL) **en pleno loadLinks** del primer intento; el reintento (pid 25640) cargÃ³ y reprodujo perfecto. PatrÃ³n "primer intento muere, reintento OK" = pico de memoria del primer arranque (dexopt/JIT + WebView/Chromium + PoW 4 hilos + probes en paralelo) â†’ sospecha LMK (invisible sin buffer de kernel).
- **Siguiente paso diagnÃ³stico**: tras una muerte, capturar `adb shell dmesg` y buscar `lowmemorykiller ... com.lagradost.cloudstream3` (+ `adb logcat -b crash -d` para descartar FATAL). Para el caso A ademÃ¡s aislar: Â¿pasa con todos los providers o solo SoloLatino? Â¿con todos los links o uno? Â¿equipo caliente/poca RAM?
- âœ… **SIN REPRODUCCIÃ“N (10 Oct 2026, reporte del usuario)**: el cierre ya no aparece â€” el video reproduce con normalidad y en SeriesKao nunca pasÃ³. Cuadro de transitorio (presiÃ³n de memoria puntual) sin evidencia para cambiar cÃ³digo. **Regla: solo reabrir si vuelve con `dmesg` positivo o `FATAL` en mano.**
- En el mismo log (reintento OK): etiquetas `LATINO[Voe]` / `LATINO[Voe (MP4)]` confirmadas en `RepoLink`, SW por WebView OK (`[SW] M3U8 (eval)` â†’ `LATINO[StreamWish]`, 7 links), reproducciÃ³n con `Rendered first frame` + pausa/reanudaciÃ³n + salida normal (`releasePlayer`, `saveData`).

### ðŸ·ï¸ Fix etiqueta VOE con idioma (09 Oct 2026; SoloLatino v31, SeriesKao v27)
- **Reporte (usuario)**: los enlaces VOE salÃ­an como "Voe" pero sin idioma (no se sabÃ­a si era latino/castellano/subtitulado).
- **Causa**: la rama VOE de `loadSourceNameExtractor` / `loadKaoSourceExtractor` pasaba los links **sin envolver** (`callback.invoke(link)`), mientras VidHide (`"VidHidePro - hls2 [LATINO]"`) y StreamWish (`"$source[StreamWish]"`) sÃ­ incluÃ­an el `source` (= langTag: LATINO/SUBTITULADO/CASTELLANO/VOSE/JAPONES, mapeado de `video_language` en SoloLatinoProvider.kt:520 / SerieskaoProvider.kt:425).
- **Fix**: envolver como el resto â€” `newExtractorLink("<Provider>", "$source[${link.name}]", ...)` copiando quality/type/referer/headers/extractorData (SoloLatinoProvider.kt:837, SerieskaoExtractors.kt:1021). Resultado: **"LATINO[Voe]"** / **"LATINO[Voe (MP4)]"**. `source` sigue siendo el nombre del provider â†’ interceptor intacto.
- **Estado**: SoloLatino **31** / SeriesKao **27** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente). â¸ï¸ Pendiente: compilar, instalar y comprobar que el enlace VOE sale como `LATINO[Voe]`.

### ðŸ›¡ï¸ MitigaciÃ³n anti-cierre: PoW serializado + 2 hilos + callbacks directos (11 Oct 2026; SoloLatino v32, SeriesKao v28)
- **Reporte (usuario)**: al reproducir serie o pelÃ­cula, cargando los enlaces la app se cierra sola (como si la quitara de recientes). Pasa en sus celulares, NO en el emulador, y NO en otros plugins. HipÃ³tesis del usuario: empezÃ³ al agregar el enlace de VOE. Los enlaces (incluido VOE) SÃ reproducen, pero hay que reiniciar la app varias veces; si reproduce, el celular se congela y luego se cierra.
- **Causa raÃ­z (confirmada en cÃ³digo, sin necesidad de log)**: el camino VOE/embed69 es el Ãºnico con trabajo pesado de CPU en paralelo:
  1. `solveEmbed69PoW` (hasta 500k SHA-256) corrÃ­a una vez POR idioma/servidor dentro de `amap` anidados, SIN `yield()`/`ensureActive()` â†’ si CS3 cancelaba el `loadLinks` (usuario sale, timeout 120s), los hilos seguÃ­an quemando CPU en fondo â†’ celular caliente â†’ congelamiento posterior + muerte del proceso (sin `FATAL`, como quitada de recientes).
  2. `solveAltchaPow` con **4 hilos** PBKDF2 (cost=10000) por cada video VOE, tambiÃ©n dentro de `amap` (servidores Ã— mirrors) â†’ pico de 12-16 hilos CPU a la vez en celulares de poca RAM â†’ LMK.
  3. SeriesKao emitÃ­a links con `CoroutineScope(Dispatchers.IO).launch { callback(link) }` **desacoplado** del `coroutineScope` del `loadLinks` (2 sitios) â†’ trabajo que sobrevive a la cancelaciÃ³n y mantiene red/CPU vivos tras salir.
  4. En el emulador no pasa por tener mÃ¡s nÃºcleos/RAM, sin LMK y con SHA-256 x86 mÃ¡s rÃ¡pido; por eso solo falla en fÃ­sicos.
- **Fix (sin cambiar resultados: mismos links, mismo orden)**:
  - `powMutex` global por mÃ³dulo (SoloLatino: `internal val powMutex` en `Extractors.kt`, usado tambiÃ©n en `SoloLatinoProvider.kt`; SeriesKao: `private val powMutex` en `SerieskaoExtractors.kt`): PoW embed69 y PoW ALTCHA **nunca corren en paralelo** (uno a la vez).
  - ALTCHA `nThreads` 4 â†’ **2** (la mitad de pico CPU; el tiempo sube pero sin congelar).
  - `solveEmbed69PoW`: `yield()` cada 4096 nonces â†’ cancelable al salir/cancelar CS3.
  - SeriesKao: los 2 `CoroutineScope(Dispatchers.IO).launch { callback(link) }` â†’ `callback(link)` directo (hijo del scope del `loadLinks`, se cancela limpio).
- **Estado**: SoloLatino **32** / SeriesKao **28** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente). **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q` y `.\gradlew.bat :SerieskaoProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar en celular fÃ­sico (NO emulador): cargar un episodio con VOE varias veces seguidas sin reiniciar â€” esperado: sin cierres ni congelamiento (a cambio, el PoW puede tardar unos segundos mÃ¡s por ir serializado).
- â¸ï¸ **Si aÃºn se cierra**: capturar `adb logcat -b crash -d` (descarta `FATAL EXCEPTION`) + `adb shell dmesg | grep -i lowmemorykiller` (confirma LMK) + intervalo completo del logcat sin filtros con hora inicio-fin.

### âœ‚ï¸ Recorte peor caso VOE: fuera el fallback WebView (11 Oct 2026; SoloLatino v33, SeriesKao v29)
**Pedido del usuario**: VOE tarda 20-60s en estar disponible (PoW ALTCHA: 81 intentos/23s, 202/48s en sus logs) vs VidHide (2-5s) y StreamWish (ms). PreguntÃ³ si quitar el WebView afectaba lo que ya reproduce.
**Veredicto (logs del propio usuario)**: el que resuelve SIEMPRE es el solver (`ALTCHA resuelto` â†’ `Found M3U8` â†’ segmentos 200 en ambos providers); el WebView de VOE nunca fue el hÃ©roe. Cadena: solver â†’ mirrors â†’ WebView (Ãºltimo).
**Fix (sin cambiar resultados)**: las 2 ramas WebView de `tryVoeExtraction` ahora retornan `tryMirrors()` directo; eliminados `VOE_READY_JS` + `webViewCookies()` (quedaban sin uso). `renderViaWebView` + `webViewMutex` se conservan (StreamWish los usa). **Danimados sin cambios**: su VOE nunca tuvo WebView (solver+mirrors desde v2, peor caso ya ~60s).
- **Estado**: SoloLatino **33** / SeriesKao **29** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente). **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar ambos, instalar y medir tiempo hasta links VOE en celular fÃ­sico (esperado: peor caso ~60s en vez de ~90s; caso normal idÃ©ntico).

---

## OkruProvider â€” Extractor OK.ru de Kraptor123/Cs-Karma (10 Oct 2026 v1)

Pedido del usuario: agregar `https://github.com/Kraptor123/Cs-Karma/tree/master/OK` como plugin del repo.

### QuÃ© trae (upstream `com.byayzen`: OKRU.kt + OKRUPlugin.kt + OkRuExtractor.kt)
- **OKRU.kt**: provider completo del catÃ¡logo ok.ru (main page/search/load en ruso). **SÃ portado** (el usuario lo pidiÃ³: hay pelis en ok.ru que no estÃ¡n en otro lado y ya comprobÃ³ que reproduce). Adaptaciones mÃ­nimas:
  - `class OKRU` â†’ `OkruProvider` (convenciÃ³n del repo); `com.lagradost.api.Log` â†’ `android.util.Log`; sin `hasQuickSearch` (open no confirmado en la base); `newSearchResponseList` posicional (nombre del 2Âº parÃ¡metro no verificado).
  - search 2-arg + `quickSearch` + `SearchResponseList` + `Episode.description` + `ShowStatus` + `HomePageList(isHorizontalImages)` â†’ **todo existe** en este CS3 (verificado por bytecode) â†’ resto 1:1.
- **OkRuExtractor.kt**: `ExtractorApi` (`ok.ru` + subclase `Odnoklassniki` para `odnoklassniki.ru`). `getUrl`: id de `(videoembed|video)/(\d+)` â†’ GET `/videoembed/{id}` â†’ `[data-module=OKVideo]`/`data-options` â†’ redirect YouTube o `"videos":[{name,url}]` ordenado por calidad (full/hd/sd/low/lowest/mobile) â†’ emite progresivos VIDEO con Referer/Origin/UA. **SÃ portado** (ok.ru es la fuente #1 de Donghualife/Seriesdonghua/Pandrama; los overloads `loadExtractor` de 3 y 4 args existen).

### Compatibilidad verificada por bytecode (antes de portar)
- `ExtractorApiKt.fixUrl(ExtractorApi, String)` existe â†’ en el port se usa SOLO `com.lagradost.cloudstream3.utils.fixUrl` (el doble import upstream sobraba).
- Overloads `loadExtractor(url, subCb, cb)` y `(url, referer, subCb, cb)` existen â†’ el redirect YouTube compila tal cual.
- `loadExtractor` **itera todos** los `extractorApis` coincidentes (aditivo): el nativo sigue corriendo ademÃ¡s del custom â†’ sin riesgo de regresiÃ³n.
- `registerExtractorAPI` ya se usa en el repo (SeriesdonghuaPlugin) â†’ el plugin registra `OkRuExtractor()` + `Odnoklassniki()` SIN MainAPI (solo extractores, quedan globales para el `loadExtractor` de todos los providers).
- **CorrecciÃ³n (el usuario pidiÃ³ el provider completo)**: `search 2-arg`, `quickSearch`, `SearchResponseList`, `Episode.description`, `ShowStatus`, `HomePageList(isHorizontalImages)`, `MovieLoadResponse.duration` â†’ **todo existe** en este CS3 (verificado por bytecode) â†’ `OKRU.kt` SÃ portado casi 1:1 como `OkruProvider` (ver adaptaciones arriba). `OkruPlugin` registra MainAPI + ambos extractores.

### Archivos
- `OkruProvider/build.gradle.kts` (v1, es, Ranita+Kraptor123), `src/main/AndroidManifest.xml`, `src/main/kotlin/com/example/OkruPlugin.kt`, `.../OkRuExtractor.kt` (package `com.example`, resto 1:1).
- `settings.gradle.kts` auto-incluye el mÃ³dulo (escanea dirs con `build.gradle.kts`) â€” sin registro manual.
- `plugins.json`: entrada `OkRu`/`OkruProvider` v1 (`fileSize: 0` pendiente).

### Estado v1
- CompilaciÃ³n: pendiente por el usuario (`.\gradlew.bat :OkruProvider:make --console=plain -q`) â€” **no compilado por regla del repo**.
- â¸ï¸ **Pendiente**: compilar, actualizar `fileSize`, instalar y probar: (1) home/search/load del catÃ¡logo ok.ru; (2) reproducciÃ³n de una peli (ej. la que el usuario ya verificÃ³ en el upstream); (3) un embed ok.ru en Donghualife â€” buscar `OkRU` en logcat y comprobar que emite junto al nativo.

### ðŸ› Fix 2001: emitir HLS `ondemandHls` en vez del MP4 progresivo (24 Oct 2026 v2)
**SÃ­ntoma (usuario)**: "Scooby-Doo Estrella del circo (2012) Latino HD" (`videoembed/16964466444806`) da `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED (2001)` / "Enlaces no encontrados / Error remoto / Source error". El extractor solo emitÃ­a los progresivos `"videos":[{name,url}]`.
**DiagnÃ³stico (medido desde PC con el `data-options` real del embed)**:
- `video[0].url` (progresivo, `vd686.okcdn.ru/?expires=...&srcAg=GECKO&sig=...`) â†’ con UA Firefox: **200 con `Content-Length` pero 0 bytes de cuerpo** (`ChunkedEncodingError: IncompleteRead(0 bytes read, ...)`), tambiÃ©n con `curl.exe` â†’ endpoint roto/cerrado en origen. Con UA `ExoPlayerLib` o sin UA â†’ **400 con cuerpo `10`**.
- `ondemandHls` (misma ruta, `.../srcAg/GECKO/.../ondemand/hls4_<contentId>.<hash>.m3u8`) â†’ UA Firefox: **206, `application/x-mpegURL`**, master real con 5 variantes (256x144 / 424x240 / 640x360 / 856x480 / 1280x720, `avc1`+`mp4a.40.2`), variante 200 con segmentos `sN.ts` de ~6 s. Probado 4 veces: determinista.
- `ondemandDash` (`dash4_<contentId>.<hash>.mpd`) â†’ UA Firefox: **206, `application/dash+xml`** (URL distinta de la HLS: `/ct/28/` vs `/ct/29/`).
- El cuerpo `10` es el chequeo de `srcAg`: la URL estÃ¡ firmada a la familia de UA **GECKO**; por eso el UA de ExoPlayer no sirve y hay que mandar el Firefox del extractor en `headers` (CS3IPlayer los aplica al link).
**Causa raÃ­z**: el MP4 progresivo de este video no entrega bytes (el player agota la conexiÃ³n â†’ 2001) y el extractor no conocÃ­a los campos `ondemandHls`/`ondemandDash` del `data-options` (el regex `"videos":(\[.*?\])` ademÃ¡s apunta a la raÃ­z, pero el array estÃ¡ en `flashvars.metadata.videos`).
**Fix (`OkRuExtractor.kt`)**:
- Nueva `jsonUrl(dataOptions, key)` (regex `"<key>":"([^"]+)"`, desescapa `\u0026`/`\/`/`&amp;`, exige `http`).
- Nueva `invokeStreamLink(label, url, type, callback)` con `Qualities.Unknown.value` y los mismos headers (UA Firefox 135 + `Referer`/`Origin` `https://ok.ru`).
- Orden de emisiÃ³n: **`ondemandHls` â†’ M3U8** (`"OkRU HLS"`); si no existe, **`ondemandDash` â†’ DASH**; si tampoco, los progresivos `videos[]` como hasta ahora. Cuando hay HLS se emite solo ese (el master ya trae todas las calidades y evita ofrecer arriba una opciÃ³n rota).
- Logs nuevos: `HLS ondemand` / `DASH ondemand` antes de emitir.
**Estado**: `OkruProvider/build.gradle.kts` **1 â†’ 2**; `plugins.json` OkruProvider **1 â†’ 2** (`fileSize` pendiente, sigue 32000). Balance de llaves verificado con un checker que respeta los raw strings `""""` (`bal_check.py` da falso MISMATCH en este archivo). **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :OkruProvider:make --console=plain -q`), actualizar `fileSize`, instalar y reproducir el video â€” en logcat debe verse `OkRU HLS -> https://vd686.okcdn.ru/.../ondemand/hls4_...m3u8` y el player adaptar la calidad sin 2001.
- â¸ï¸ Si otro video no trae `ondemandHls`, cae a `ondemandDash`; si el DASH tambiÃ©n fallara, el Ãºltimo recurso sigue siendo el progresivo.

### ðŸ›¡ï¸ v3/v4: probe de HLS+DASH (fail-open) + MP4 tambiÃ©n + logs de diagnÃ³stico (24 Oct 2026 v4)
**Pedido (usuario)**: "por si algo falla nuevamente" â€” que el extractor valide los enlaces antes de emitirlos y que `load`/`loadLinks` dejen rastro en logcat. Luego: "esto no afecta la reproducciÃ³n de los demÃ¡s videos, porque los demÃ¡s sÃ­ me reproducen" â†’ se decidiÃ³ **no quitar** el MP4 progresivo.
**Cambios (`OkRuExtractor.kt`)**:
- Nueva `probeStream(url, type): Boolean?` â†’ `app.get` con headers (Referer/Origin/UA Firefox), `timeout = 10L`; acepta `200`/`206` + marcador de cuerpo (`#EXTM3U` para M3U8, `<MPD` para DASH). Devuelve `true` = OK, `false` = fallo definitivo, `null` = excepciÃ³n (**fail-open**: se emite igual). Re-lanza `CancellationException`.
- EmisiÃ³n: ahora se prueban y emiten **HLS y DASH a la vez** (HLS primero), no "HLS o DASH". Cada uno se omite solo si el probe devuelve `false` (`Log.w "HLS/DASH ondemand no responde, se omite"`).
- **v4 â€” sin regresiÃ³n**: aunque HLS/DASH se emitan, **tambiÃ©n se emiten los MP4 progresivos** (antes habÃ­a `return` tras emitir HLS/DASH). AsÃ­ ningÃºn video que hoy reproduce por MP4 pierde su enlace; los adaptativos quedan arriba en la lista. Log `Emitidos N (HLS/DASH) id=..; agregando MP4 progresivo`.
- Si ninguno de los dos se emite â†’ solo MP4 (`Log.w "HLS/DASH descartados por el probe; probando MP4 progresivo"`).
- Log de diagnÃ³stico: `id=$id hls=<bool> dash=<bool> videos=<bool>` + `claves=<lista>` cuando faltan ambos campos (permite ver si ok.ru renombrÃ³ las claves). Conteo de progresivos: `MP4 progresivos: N`.
- `probeStream` **no** se usa en los progresivos (descargarÃ­a MBs): los MP4 siguen sin probe.
**Cambios (`OkruProvider.kt`)** â€” solo logging: `getMainPage '<secciÃ³n>' page=N -> M items`, `search '<q>' page=N` + `len` del `data-props`/API, `load <url>` + `album/title`, `SerieLoad aid=.. eps=N (crudos=M)`, `Filmvideo title/eurl/duration`, `loadLinks <data>` + rama (`OkRuExtractor` vs `loadExtractor`) + `isFound`.
**Estado**: `OkruProvider/build.gradle.kts` **3 â†’ 4**; `plugins.json` OkruProvider **3 â†’ 4** (`fileSize` pendiente, sigue 32000). Balance verificado con tokenizer que respeta raw strings (`""""`) â†’ pila vacÃ­a en ambos `.kt`; `plugins.json` vÃ¡lido (73 entradas). **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :OkruProvider:make --console=plain -q`), actualizar `fileSize`, instalar y reproducir Scooby-Doo â€” buscar `probe HLS -> code=206 len=1379 ok=true`, `probe DASH -> code=206 ... ok=true`, `Emitidos 2 (HLS/DASH) id=..; agregando MP4 progresivo`.
- âš ï¸ **NUNCA editar ficheros del repo con `Set-Content`/`Out-File`** (PS 5.1): corrompiÃ³ `OkruProvider/build.gradle.kts` a 542 bytes de `0x00`. Usar el tool `edit`.

### ðŸŽ¯ v5: probe del MP4 progresivo (Range) + polÃ­tica fail-open (24 Oct 2026 v5)
**SÃ­ntoma (usuario)**: con v4 el video ya trae HLS/DASH vÃ¡lidos, pero el player abre con el **MP4 progresivo** y falla (`ERR_CONTENT_LENGTH_MISMATCH` / "Source error"), porque ese MP4 estÃ¡ roto en origen.
**Causa raÃ­z (por quÃ© el MP4 va primero)**: el MP4 se emite con `quality = mapQuality("hd") = Qualities.P720`, mientras HLS/DASH llevan `Qualities.Unknown.value` (-1) â†’ CloudStream ordena **descendente** y el MP4 roto queda arriba, por delante de HLS/DASH.
**DecisiÃ³n de diseÃ±o**: **no tocar las calidades**. Un `P720` en HLS empatarÃ­a con el MP4 y el desempate del player no es determinista (podrÃ­a seguir eligiendo el roto). Se valida el MP4 y se omite si no responde.
**Cambios (`OkRuExtractor.kt`)**:
- Imports nuevos: `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.withContext`, `okhttp3.OkHttpClient`, `okhttp3.Request`, `java.util.concurrent.TimeUnit` (`okhttp3` ya llega por dependencia transitiva; PlushdProvider lo usa sin declararla).
- `probeClient` (`by lazy`): `OkHttpClient` con `connectTimeout`/`readTimeout` de **8 s** y `followRedirects(true)`.
- `probeProgressive(videoUrl): Boolean?`: `withContext(Dispatchers.IO)` + `Request` con `Range: bytes=0-1023` y headers (Referer `$mainUrl/`, Origin, UA Firefox); lee **mÃ¡ximo 1024 B** del body (`byteStream().read(ByteArray(1024))`) y cierra. `true` si `200/206` **y** `read > 0`; `false` si respondiÃ³ mal o con 0 bytes; `null` si lanza excepciÃ³n (re-lanza `CancellationException`). Log `probe MP4 -> code=.. read=.. ok=..`.
- **No se usa `app.head(`** (no existe en el repo) ni `app.get` con `Range` (el servidor puede ignorarlo y devolver ~350 MB): de ahÃ­ el cliente OkHttp con lectura parcial.
- PolÃ­tica en el bucle de progresivos: `mp4Ok == true` â†’ emitir; `mp4Ok == false` â†’ omitir (`Log.w "MP4 <calidad> no responde, se omite"`); `null` â†’ **omitir solo si `emitted > 0`** (ya hay HLS/DASH) y **emitir si no hay otros enlaces** (fail-open, nunca dejar el video sin enlaces). El `return@forEach` para `youtube.com`/`youtu.be` se conserva.
**Estado**: `OkruProvider/build.gradle.kts` **4 â†’ 5**; `plugins.json` OkruProvider **4 â†’ 5** (estaba en 4, no en 3 como decÃ­a la nota de v3/v4) â€” `fileSize` **pendiente** (sigue 32000). `OkruProvider.kt` balance OK; `plugins.json` vÃ¡lido (73 entradas). El checker ingenuo sigue dando **falso MISMATCH** en `OkRuExtractor.kt` (raw strings `""""` en L88/97/169/184/189/237/241): verificado a mano, el fichero estÃ¡ balanceado (274 lÃ­neas). **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :OkruProvider:make --console=plain -q`), actualizar `fileSize`, instalar y reproducir Scooby-Doo â€” buscar `probe MP4 -> code=200 read=1024 ok=true` (se emite) o `probe MP4 -> code=200 read=0 ok=false` + `MP4 hd no responde, se omite` (se descarta y reproduce por HLS), y que **no** aparezca `ERR_CONTENT_LENGTH_MISMATCH`.
- â¸ï¸ Si el video solo tuviera MP4 roto (sin HLS/DASH), se emite igual (fail-open) y el error se verÃ¡ en el player: no hay otra fuente.

---

## TvenvivoProvider ï¿½ HTTPS roto del sitio, cambio a HTTP (11 Oct 2026 v7)

### ?? Sï¿½ntoma (log del usuario)
- `safeAppGet error (intento 1 y 2): SSLHandshakeException - CertPathValidatorException: Trust anchor for certification path not found` ? `getMainPage: failed to get HTML`. El home no carga nada.

### ? Causa raï¿½z (verificada, lado servidor ï¿½ NO es el celular)
- `https://www.tvenvivo.org/` falla el handshake TLS desde DOS redes distintas (celular del usuario + PC): certificado invï¿½lido/roto (el dominio ademï¿½s tiene historial de cert invï¿½lido y de parking/expiraciï¿½n segï¿½n websearch).
- `http://www.tvenvivo.org/` responde 200 con el catï¿½logo completo (verificado: lista EN VIVO 01-80+ con todos los canales).
- Conclusiï¿½n: no es fecha/hora del dispositivo ni MITM ï¿½ el HTTPS del sitio estï¿½ roto en origen. No hay nada que "reparar" en el celular.

### ?? Fix
- `TvenvivoProvider.kt:30`: `mainUrl = "https://www.tvenvivo.org/"` ? `"http://www.tvenvivo.org/"`. Todo lo demï¿½s deriva de `mainUrl` (`safeAppGet(mainUrl)`, `fixUrl`), asï¿½ que home/search/load pasan a HTTP sin mï¿½s cambios. El filtro de la lï¿½nea 690 (`contains("tvenvivo.org")`) es agnï¿½stico al esquema.
- `plugins.json`: `iconUrl` tambiï¿½n a `http://` (el icono tampoco cargarï¿½a por HTTPS).
- **Desync detectado**: `build.gradle.kts` iba en 5 pero `plugins.json` en 6 (mismo patrï¿½n SoloLatino/SeriesKao/Pandrama). Ambos a **7**.
- Compilaciï¿½n: pendiente por el usuario (`.\gradlew.bat :TvenvivoProvider:make --console=plain -q`) ï¿½ **no compilado por regla del repo**. `plugins.json`: version 7, `fileSize` **pendiente** (sigue 47315).
- ?? **Pendiente**: compilar, instalar y probar home (debe listar los ~80+ canales EN VIVO) + reproducir un canal.

---

## DanimadosProvider ï¿½ VOE con solver + StreamWish vï¿½a WebView (13 Oct 2026 v2)

Sitio: `danimados.cc` (DooPlay). Rama `loadLinks`: AJAX `doo_player_ajax` ? embeds (VidHide/minochinos+callistanise, Byse, VOE, HgLink/hglink.to, fastream, archive.org, TurboVid, WaaW, CubeEmbed).

### ?? Diagnï¿½stico (log v1 en dispositivo, Scooby-Doo + Galaxia Wander + Lakebottom + Mr Young)
1. **VOE 0 links**: `voe.sx/e/...` ? 200 + `encoded string not found` en voe.sx y en los 3 mirrors (yip.su, tubelessceliolymph, eugenemakedraw). Causa: sin solver ALTCHA (abandono explï¿½cito) + sin seguir el stub JS al mirror + sin `teresapoliticallearn.com` en la lista (lecciï¿½n SoloLatino v29).
2. **HgLink 0 links**: `hglink.to/e/...` es backend StreamWish; sin mapeo a `streamwish.to` y sin parse, `loadExtractor` da 0 (challenge).
3. **`source` rompï¿½a el interceptor**: CubeEmbed y la ruta genï¿½rica emitï¿½an `source = "Danimados (Server1)"` ? `"Danimados"` ? `getApiFromNameNull` null ? sin interceptor ? el log muestra `Using CronetDataSource` y el CDN da **403** (caso fastream `s40.fastream.to`).
4. **archive.org roto**: candidatos `/download/...` se armaban como `"https:" + "/download/..."` = `https:/download/...` (host `download` irresoluble) ? fallback ? basura.
5. **Timeouts en SEGUNDOS sin corregir**: `timeout = 20000L/15000L/10000L/8000L` en `DanimadosExtractors.kt` = sin timeout efectivo (regla 03 Oct 2026; este era uno de los pendientes listados en SoloLatino). TurboVid `turbovidhls.com` ademï¿½s con DNS muerto (falla graceful, se deja).

### ?? Fix (v2, sin cambiar resultados de lo que ya funcionaba)
- **VOE**: port del stack SeriesKao ï¿½ `solveDaniAltcha()` + `solveDaniAltchaPow()` con **`daniPowMutex` + 2 hilos** (anti-freeze SoloLatino v32/SeriesKao v28: nunca en paralelo, mitad de pico CPU) + `parseVoeHtml(..., cookies)` + `tryVoeExtractDanimados` con **JS-redirect al mirror** (hasta 3 saltos, merge de cookies) + mirrors `teresapoliticallearn.com` (primero), `yip.su`, `tubelessceliolymph.com` en `amap` con timeout 20s + rama directa m3u8/mp4. Etiquetas `Voe` / `Voe (MP4)` con `source = "Danimados"` (patrï¿½n v30/v26).
- **StreamWish**: rama nueva en `loadLinks` (hglink/hgglink/streamwish/swdyu/cybervynx/dumbalag/awish) ? `tryStreamwishDanimados()`: mapeo a `streamwish.to` + parse estï¿½tico (`parseDaniStreamwishHtml`: m3u8/`file:`/mp4 + unpack Dean Edwards + iframes) + re-emit de `loadExtractor` con `source = "Danimados"` + **fallback WebView** (`renderDaniWebView` + `daniWebViewMutex` + `catch Throwable`, patrï¿½n VerAnime v8; `DanimadosProvider.pluginContext` guardado en `DanimadosPlugin.load`).
- **`source = "Danimados"`** en CubeEmbed (ï¿½2), ruta genï¿½rica (ï¿½2) y MP4 de VOE ? el interceptor vuelve a aplicar (adiï¿½s 403 fastream/Cronet).
- **archive.org**: `candidate` `/...` se resuelve contra el host del embed (`https://archive.org/download/...` correcto); `//` ? `https:`; resto igual.
- **Timeouts a segundos**: `20000L?20L` (ï¿½2), `8000L?8L`, VOE `15000L?15L`/`10000L?10L`. `withTimeoutOrNull(10000L/20000L)` intactos (kotlinx = ms, correcto).
- Balance verificado con `bal_check.py` ? OK en ambos archivos.
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).

### ðŸ“º Home por networks en vez de dÃ©cadas (13 Oct 2026 v3)
**Pedido del usuario**: secciones por canal (ej. `https://danimados.cc/network/cartoon-network/`) en vez de por dÃ©cadas/`genero/`.
**Estructura verificada** (subagente explore sobre HTML real): no existe Ã­ndice `/network/` (404); la lista canÃ³nica son 20 slugs del widget `Series por canal` (`a.canal-item`): cartoon-network, boomerang, nickelodeon, disney-xd, disney-channel, fox-kids, fox, fx, the-wb, adult-swim, mtv, comedy-central, locomotion-tv, hbo-max, netflix, prime-video, disney, paramount, apple-tv, hulu (se ignoran intrusos `/studio/*` y `/tag/*` del mismo widget). Las cards usan el **mismo** `article.item.tvshows` que gÃ©neros (tÃ­tulo `.data h3 a`, poster `.poster img[src]`, ~40 por pÃ¡gina). PaginaciÃ³n `/page/N/` triple-confirmada (canonical `rel=next`, `div.pagination`, `a.arrow_pag`).
**Fix**: `mainPage` = `/` (Ãšltimas) + 20 networks; `getMainPage` pide la canÃ³nica en pag 1 (`/page/1/` puede redirigir) y `hasNext` por `div.pagination a.arrow_pag/a[href*=/page/]` con fallback al conteo anterior.
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y comprobar el home (21 filas) + paginar una network (ej. Cartoon Network pag 2).

### ðŸ–¼ï¸ Posters en episodios (13 Oct 2026 v4)
**Pedido del usuario**: los episodios muestran posters en la web.
**Verificado** (HTML real de `/series/phineas-y-ferb/`): cada `ul.episodios li` trae `<div class='imagen'><img src='...-300x170.jpg'></div>` + `.numerando` + `.episodiotitle a` + `span.date`.
**Fix**: `extractEpisodes()` (ambas ramas: por temporada y plana) lee `li.selectFirst(".imagen img")?.attr("src")` â†’ `this.posterUrl` en `newEpisode` (vÃ­a `fixUrl` miembro, igual que el poster de la serie).
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y abrir Phineas y Ferb â€” los episodios deben mostrar su thumbnail.

### ðŸ–¼ï¸ Backdrop en el detalle (13 Oct 2026 v5)
**Pedido del usuario**: en `/series/house-of-mouse/` la web muestra 2 posters (vertical + grande) pero el plugin solo el vertical.
**Verificado** (HTML real): poster vertical en `.sheader .poster img` (TMDB w185) + backdrop landscape en `meta[property='og:image']` (TMDB w780, ej. `n0VOCRcEVsshvYwtmLvolGx3sFR.jpg`). El `load()` nunca seteaba `backgroundPosterUrl`.
**Fix**: `background = og:image` â†’ `this.backgroundPosterUrl` en serie y pelÃ­cula (el poster vertical no se toca).
- Versiones a **5** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y abrir House of Mouse â€” el detalle debe mostrar el backdrop grande arriba.

### ðŸ·ï¸ Recomendaciones (TÃ­tulos similares) (13 Oct 2026 v6)
**Pedido del usuario**: la web muestra "TÃ­tulos similares" dentro de las series.
**Verificado** (HTML real): `div.sbox.srelacionados > div#single_relacionados > article > a[href=/series/slug/] > img[src, alt=tÃ­tulo]` (12 items).
**Fix**: `load()` los parsea (tÃ­tulo de `img[alt]`, poster `img[src]`) â†’ `this.recommendations` en serie y pelÃ­cula.
- Versiones a **6** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y abrir House of Mouse â€” deben salir recomendaciones (Coraje, Ciudad de Perros, Ren y Stimpy...).

### ðŸ”§ Extractor propio de ok.ru (15 Oct 2026 v7)
**Log v6 (usuario, Silvestre y PiolÃ­n)**: un solo servidor `ok.ru/videoembed/2487200909897` â†’ `loadExtractor` retornÃ³ `true` con 0 links â†’ `success=true` pero "enlaces no encontrados". Danimados no tenÃ­a rama ok.ru (Donghualife sÃ­, con `emitOkru`).
**Fix**: port de `emitOkru` â†’ `tryOkruDanimados()` (`data-options` â†’ `flashvars.metadata.hlsManifestUrl` â†’ HLS; fallback `videos[]` progresivos con calidades full/hd/sd/low/mobile) + rama en `loadLinks` + etiqueta `Okru` + `okcdn`/`ok.ru` al interceptor (Referer = embed, como Donghualife). `source = "Danimados"` en todo lo emitido.
- Versiones a **7** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- âœ… **VERIFICADO en dispositivo (15 Oct 2026, Silvestre y PiolÃ­n)**: `[Okru] 2487200909897 -> HLS` â†’ link `Danimados [ok.ru]` (`vd641.okcdn.ru/video.m3u8?...`) â†’ `OkHttpDataSource` + segmentos fluyendo por el interceptor â†’ **reproduce**. Y el fix archive.org tambiÃ©n: Duckman emite `https://archive.org/download/duckman-temporada-1/...mp4` bien formado (antes `https:/download/...` roto).

### ðŸ·ï¸ Etiqueta VOE como los demÃ¡s (15 Oct 2026 v8)
**Pedido del usuario**: el link VOE salÃ­a como `Voe` solo; debe ser `Danimados [Voe]` como el resto.
**Fix**: `name = "Danimados [Voe]"` en `generateM3u8` y en la rama directa m3u8/mp4 (`source` sigue `"Danimados"` â†’ interceptor intacto).
- Versiones a **8** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y comprobar el nombre en la lista.

### ðŸ” Filtro de search contra ruido de WP (13 Oct 2026 v9)
**Pedido del usuario**: buscar algo inexistente devolvÃ­a series sin nada que ver en vez de vacÃ­o.
**Causa**: `/?s=` de WP es laxo (coincidencias parciales en contenido) y el provider devolvÃ­a todo tal cual (ademÃ¡s sin URL-encode del query).
**Fix** (patrÃ³n VerAnime): `matchesQuery()` â€” todos los tokens del query (â‰¥2 letras, con tolerancia a plural `s` final) deben aparecer en el tÃ­tulo; `URLEncoder.encode` del query; se devuelve lista vacÃ­a (no null) cuando nada es relevante.
- Versiones a **9** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y buscar algo inexistente (ej. "xyzqwe") â†’ debe salir vacÃ­o; y algo real (ej. "naruto") â†’ solo resultados con ese nombre.

### ðŸ” Search vacÃ­o aunque la web sÃ­ tiene resultados (13 Oct 2026 v10)
**Reporte (usuario)**: en la web "naruto" trae resultados pero el plugin sale vacÃ­o.
**Causa raÃ­z (HTML real de `/?s=naruto`)**: el search usa cards DISTINTAS (`article.w_item_a > a[href=/series/] > .image img + .data h3` con tÃ­tulo SIN link) que ningÃºn selector cubrÃ­a (`article.item.tvshows`, `article.item`, `.result-item`, `.search-item` â†’ 0; el fallback exigÃ­a `h3 a` o `.title` â†’ tambiÃ©n 0).
**Fix**: rama `article.w_item_a` en `search()` (tÃ­tulo `.data h3` o `img[alt]`, poster `img[src]` w780) + `fold()` con `Normalizer` (quita acentos: "pokemon" matchea "PokÃ©mon") en `matchesQuery()`.
- Versiones a **10** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y buscar "naruto" â†’ deben salir los resultados con naruto en el tÃ­tulo.
- âœ… **Verificado sin cÃ³digo (query "paso a paso")**: la web devuelve exactamente 10 cards genÃ©ricas (Ed Edd y Eddy, Spiderman, Phineas...) y **ninguna contiene "paso"** â€” es lista de relleno, no matches. El filtro v9/v10 las descarta todas â†’ vacÃ­o correcto (lo pedido en v9). Sin fix.

### ðŸ› Fix match real con markup pelado (13 Oct 2026 v11)
**Log v10 (usuario)**: `search 'zorro'` â†’ 10 descartados, pero la web muestra 1 resultado real (`El Zorro: Serie Animada`, `/series/zorro/`).
**Causa raÃ­z (HTML real)**: el match verdadero viene en `<article>` SIN clase (`.image .thumbnail a` + `.details .title a` + `.meta`), seguido de los 10 `w_item_a` de relleno. Mi fallback agarraba el PRIMER `a[href*=/series/]` (el del thumbnail, texto vacÃ­o) â†’ `title.isBlank()` â†’ descartado siempre.
**Fix**: el fallback prefiere el link CON texto (`.details .title a` primero) y si el link sale vacÃ­o rescata el tÃ­tulo de `.details .title` o `img[alt]`.
- Versiones a **11** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y buscar "zorro" â†’ debe salir `El Zorro: Serie Animada`.

### ðŸ› Fix fallback condicionado (13 Oct 2026 v12)
**Log v11 (usuario)**: `search 'zorro' â†’ 10 descartados` OTRA VEZ con v11 instalada. `phineas: 1 relevantes` OK.
**Causa raÃ­z**: el fallback de `<article>` pelados solo corrÃ­a `if (results.isEmpty())` â€” pero los 10 `w_item_a` de relleno SÃ parsean, asÃ­ que el fallback jamÃ¡s ejecutaba y el match real (`/series/zorro/`) nunca se parseaba.
**Fix**: el loop de `<article>` pelados corre SIEMPRE (no solo en vacÃ­o); `distinctBy { url }` evita duplicados.
- Versiones a **12** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y buscar "zorro" â†’ `El Zorro: Serie Animada` + `phineas` sigue en 1.
- âœ… **VERIFICADO v12 en dispositivo (15 Oct)**: `zorro: 1 relevantes`, `phineas: 1`, `randy: 2`, `house: 3`, `boon: 1`, `naruto: sin resultados (20 descartados)` â€” search OK. Bonus: Los Boondocks fluye Ã­ntegro (VH-Pro Ã—3, SW vÃ­a `loadExtractor` â†’ `hglamioz`, Byse 404 `video record missing` bien manejado). **Mirror VOE nuevo**: `megankiddifference.com` â€” el regex de JS-redirect lo siguiÃ³ solo, sin cambios. El `StandaloneCoroutine was cancelled` del PoW VOE es benigno: el usuario tocÃ³ un link a los ~60s y CS3 cancelÃ³ el scope (ya habÃ­a `success=true, 4 players`).

### ðŸ”§ Rama Byse generalizada (15 Oct 2026 v13)
**Log v12 (usuario, House of Mouse EP5)**: `loadLinks Server1: https://bysekoze.com/e/...` â†’ `success=true` pero "enlaces no encontrados" (el `loadExtractor` genÃ©rico retornÃ³ `true` con 0 links, mismo patrÃ³n que ok.ru en v6).
**Causa**: la rama Byse solo matcheaba `bysedikamoum.com`/`4meplayer.pro`; `bysekoze.com` caÃ­a al genÃ©rico. El `ByseHttpExtractor` es agnÃ³stico al host (probea `Byse Frontend`), asÃ­ que basta rutear.
**Fix**: condiciÃ³n `videoUrl.contains("byse")` (cubre `bysekoze.com` y futuros mirrors `byse*.com`) en etiqueta + rama.
- Versiones a **13** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- â¸ï¸ **Pendiente**: compilar, instalar y probar House of Mouse EP5 buscando `[Byse] flujo HTTP` + sources.
- ?? **Pendiente**: compilar (`.\gradlew.bat :DanimadosProvider:make --console=plain -q`), instalar y probar Scooby-Doo (VOE ? `[Voe] JS redirect` ? `ALTCHA resuelto` ? `Found M3U8`; HgLink ? `[SW] M3U8 (eval)` o `[SW-Web]`), Galaxia Wander (fastream con interceptor ? sin 403) y Mr Young (archive.org con URL bien formada).

---

## VertvcableProvider ï¿½ Plugin nuevo (13 Oct 2026 v1)

Sitio: `vertvcable.com` (WordPress tema Appyn). Directorio de TV en vivo. OJO: la web incluye un falso popup "Java Update" (plugin `akismet-antl-spam`) que descarga un ejecutable de `staybonvivant.com/dw4.php` ï¿½ solo afecta a visitantes Windows en navegador (el JS se auto-desactiva en Android); al provider (`app.get`, sin JS) no le afecta.

### Cadena verificada end-to-end (ESPN, en vivo desde PC)
1. Ficha `/{slug}-en-vivo/` ? botones `.vtc-pill[data-post][data-index][data-nonce]` (Opciï¿½n 1, Opciï¿½n 2 VPN MX...).
2. `POST wp-admin/admin-ajax.php` (`action=vtc_get_stream_url`, `post_id`, `stream_index`, `nonce`) con Referer + UA navegador (sin cookies OK; POST pelado da 403 Cloudflare) ? `{"url":"https://play.vertvcable.com/play/?canal=espn","type":"EMBED"}`.
3. Embed ? `<iframe src="/ver/?id=espn">` ? `const config = {"url":"//cdn-live.mundogo.cl/espn/espn.mpd","clearkey":{kidHex:keyHex ï¿½3}}` (Clappr+Shaka DASH ClearKey, keys en hex plano).
4. Mismo patrï¿½n que sensa/Telelibre ? `newDrmExtractorLink` + `hexToB64Url()` (con el caveat del UUID ClearKey de la app).

### Implementaciï¿½n (v1, sin compilar por regla del repo)
- `getMainPage`: `/` + 5 categorï¿½as (`/category/cine|peliculas|deportes|infantil|musica`); cards `div.bav a[href]` (tï¿½tulo `span.title`, poster `img[data-src]` por lazyload con fallback a `src`); pag `page/N/`; `hasNext` por `link[rel=next]`.
- `search()`: `/?s=` + `URLEncoder`, mismo parser.
- `load()`: `h1` + `og:image` + meta description ? `TvSeriesLive` con 1 episodio.
- `loadLinks()`: pills ? AJAX por opciï¿½n ? dispatch por `type` (EMBED?`resolveEmbed`, HLS/MP4/DASH?directo). `resolveEmbed`: config ClearKey ? DRM; m3u8/mp4 directo; 1 nivel de iframe anidado (`/ver/`); YouTube/externos ? `loadExtractor`.
- Timeouts en segundos (`15L`); JSON con `org.json` (nunca kotlinx-serialization).
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `VertvcablePlugin.kt`, `VertvcableProvider.kt`; `plugins.json` ? entrada `VerTvCable` v1 (`fileSize: 0` pendiente).
- Balance verificado con `bal_check.py` ? OK.
- ?? **Pendiente**: compilar (`.\gradlew.bat :VertvcableProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar ESPN. **Aviso VPN**: el MPD (`cdn-live.mundogo.cl`) no resolviï¿½ DNS desde la red de PC (posible geo-DNS o CDN caï¿½do); si en el dispositivo tampoco resuelve, probar con VPN (hay "Opciï¿½n 2 VPN MX" por canal). Logs: `adb logcat -s VerTvCable:V` buscando `ajax OK` y `DASH mpd=`.

- Fix compilacion Vertvcable (13 Oct 2026): `Argument type mismatch: String? vs String` (linea 261) ï¿½ en el bloque anidado de `resolveEmbed` se usaba `key` sin declararla (faltaba `val key = keys.optString(kid)`). Sin compilacion exitosa `:make` no genera el `.cs3` en `VertvcableProvider/build/`.

### Interceptor faltante ? 2002 en HLS directo (13 Oct 2026 v2)
**Log v1 (usuario)**: home (193 canales + categorï¿½as), search, load y AJAX OK ï¿½ Studio Universal resuelve `type=HLS` directo (`cdn1tlinkgo.tlink.cl/.../mono.m3u8`, sin VPN). Pero el player usaba `CronetDataSource` con `headers={}` ? `SocketTimeoutException` ? error 2002.
**Causa raï¿½z**: el provider v1 **no tenï¿½a `getVideoInterceptor`** ï¿½ CS3 caï¿½a a Cronet sin headers y el CDN (que exige Referer) dejaba morir la conexiï¿½n.
**Fix**: `getVideoInterceptor` que inyecta UA + Referer (del link) + Origin + Accept a todo request de nuestros links (`source == "VerTvCable"` ? `getApiFromNameNull` lo encuentra).
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y probar Studio Universal ï¿½ buscar `[intercept]` en el log y confirmar que el HLS arranca (si el CDN chileno bloquea por geo, el siguiente paso es VPN).

### Veredicto v2 en dispositivo: CDN geo-bloqueado, no es cï¿½digo (13 Oct 2026)
**Log v2 (usuario, Studio Universal)**: el interceptor Sï¿½ aplica (`Using OkHttpDataSource` + `[intercept]` ï¿½4 por intento). Pero el TCP a `cdn1tlinkgo.tlink.cl/45.4.0.11:443` muere (`SocketTimeoutException ... after 10000ms`, ï¿½2 intentos) ? 2001.
**Verificado desde PC**: el host ni resuelve DNS (sin respuesta) y por HTTP tampoco conecta. En el dispositivo sï¿½ resuelve (45.4.0.11) pero los SYN se pierden ? tï¿½pico geo-fence chileno (tlink.cl) por IP, no por headers. **No hay fix de cï¿½digo posible.**
- Siguiente paso (usuario): probar con **VPN en Chile** (y si no, MX ï¿½ hay "Opciï¿½n 2 VPN MX" por canal). Si con IP CL el `[intercept]` devuelve 200, confirmado geo y el provider queda como estï¿½.
- Tambiï¿½n probar un canal tipo EMBED (ej. ESPN ? `/ver/` ? MPD `cdn-live.mundogo.cl`, tambiï¿½n chileno, mismo pronï¿½stico).

---

## InfinityProvider ï¿½ Plugin nuevo desde API Solodroid (14 Oct 2026 v1)

Origen: APKs `ACTIVADOR v1.0` (WebView shell inofensivo) + `INFINITY FREE V6.4.4` (plantilla Solodroid) + pï¿½gina `fluxa.strymix.com/activar_25.php` (teatro: expiraciï¿½n en negativo, cï¿½digos demo). Decompilado con apktool local.

### Cadena de descubrimiento
1. `Config.smali`: `REST_API_KEY=cda11...` + `SERVER_KEY` (doble base64) ? `https://strymix.com/admin_panel_applicationid_com.infinity.free` (**MUERTO: 404 en todo**).
2. `SharedPref.api_url` se provisiona vï¿½a `saveConfig` (getConfig) ï¿½ la app guarda el api real tras activar.
3. La app trae blocklist anti-interceptaciï¿½n (AdGuard/Blokada/HttpCanary/PCAP/DNS privado/LuckyPatcher) ? HTTP Toolkit/PCAP rebotan (el "VPN detectado" del usuario).
4. `RestAdapter`: base = `apiUrl + "/api/"`, endpoints `api.php?get_category_index|get_category_posts(id,page,count)|get_post_detail(id)|get_config(package_name)` + `api_key` (flag sin valor, como Retrofit).
5. Logcat del usuario revelï¿½ la base VIVA: `strymix.com/admin_panel/` (las imgs van a `/admin_panel/upload/category/`). `GET .../admin_panel/api/api.php?get_category_index&api_key=...` ? `{"status":"ok",...}` + **27 categorï¿½as** (cids verificados).
6. Pelï¿½culas: `locotv.alwaysdata.net/budpeli/movies_data.json` (17MB, ~15k pelis, mp4 directo `nvmevod.btv.mx`) ï¿½ pï¿½blico pero NO se portï¿½ (usuario: bastantes plugins de pelis).

### Implementaciï¿½n (v1, sin compilar por regla del repo)
- `getMainPage`: 27 secciones fijas (`cat:{cid}`) ? `get_category_posts` (count=50); `hasNext` por `pages`.
- `search()`: `get_search_results&search=` (parï¿½metro inferido del template; con fallback a vacï¿½o si falla).
- `load()`/`loadLinks()`: data = `{mainUrl}/infinity/channel/{id}` (URL absoluta por regla Donghualife v6) ? `get_post_detail` ? `channel_url` (m3u8 directo `fluxa.strymix.com/...`) con header `User-Agent` = `user_agent` del canal (asï¿½ lo pide la app); `channel_type=YOUTUBE` ? `loadExtractor`.
- **Anti rate-limit**: el panel baneï¿½ mi IP tras ~6 requests ? 1 sola peticiï¿½n por llamada, sin `amap`/probes.
- JSON con `org.json` (nunca kotlinx-serialization). Timeouts en segundos.
- Archivos: `build.gradle.kts` (v1), `AndroidManifest.xml`, `InfinityPlugin.kt`, `InfinityProvider.kt`; `plugins.json` ? entrada `Infinity` v1 (`fileSize: 0` pendiente). `settings.gradle.kts` auto-incluye (verificado en Okru).
- Balance `bal_check.py` ? OK.
- ?? **Pendiente**: compilar (`.\gradlew.bat :InfinityProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar home (27 secciones) + un canal de Mï¿½xico. Logs: `adb logcat -s Infinity:V`.

### ?? Freeze: headers en segmentos + interceptor con diagnï¿½stico (14 Oct 2026 v2)
**Reporte (usuario)**: en la app original casi no se congela, en el plugin sï¿½ ? no es (solo) el CDN, falta algo que la app sï¿½ manda.
**Hipï¿½tesis**: el link v1 solo llevaba `User-Agent` y referer al panel; los segmentos del CDN fluxa probablemente exigen el set completo (la app los pide con UA+Referer propios). Sin interceptor, CS3 puede pedir variantes/segmentos sin ellas ? el CDN estrangula ? freeze.
**Fix**: link con `Referer: https://fluxa.strymix.com/` + `getVideoInterceptor` que re-aplica UA/Referer/Origin/Accept a todo `strymix.com` y loguea `[cdn] METODO url -> codigo` (diagnï¿½stico del freeze en vivo).
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- ?? **Pendiente**: compilar, instalar y comparar frecuencia de congelamiento + mirar `[cdn]` en el momento del freeze (401/403/timeout vs 200 lento decide el siguiente paso).

### ?? Veredicto Studio Universal HD: edge sin margen (14 Oct 2026, sin cambio de cï¿½digo)
**Medido en vivo (PC)**: hay DOS entradas ï¿½ `Studio Universal HD` (1628, cat HD) y `Studio Universal` (1627, cat Pelï¿½culas).
- HD: playlist viva (seq 157xxx, segs 5s en `ts6.tvcluboficial.com`), pero los 3 segs anunciados dieron **200 con 0B** (expirados) y el mï¿½s fresco **1.75MB en 4.5s** (~3.1 Mbps efectivos para ~5s de 1080p = sin margen; cualquier jitter ? freeze cï¿½clico).
- Conclusiï¿½n: el freeze del usuario en este canal es caudal del edge al lï¿½mite, no headers (prueba 1: mismos headers on/off = mismo resultado) ni cï¿½digo. La app original lo disimula con mï¿½s buffer.
- Recomendaciï¿½n al usuario: probar la entrada **no-HD** (1627, edge `ts1`, segs 6s) que debe tener mï¿½s margen.

### ?? Error fetchProjectId en cs3d + motivo real (14 Oct 2026 v3)
**Reporte (usuario, cs3d)**: `No se encontrï¿½ el proyecto 10. ï¿½ltimo error: fetchProjectId: no accesible`.
**Anï¿½lisis**: `fetchProjectId` = `query { viewer { projectV2(number) { id } } }` con id=null. El parseo Sï¿½ funcionï¿½ (kotlinx-serialization sigue en `SyncNetwork.kt:239` ï¿½ la migraciï¿½n a Jackson fue parcial; en cs3d no crasheï¿½ esta vez). Causas probables en orden: (1) el proyecto no es **V2** (los classic devuelven null); (2) nï¿½mero/cuenta no coincide (`viewer` = dueï¿½o del token; proyectos de ORG no salen por esa query); (3) token sin scope `project`/`read:project`.
**Fix v3**: el error ahora incluye los `errors[]` de GraphQL (`no accesible (<msgs>)`) para distinguir scope vs nï¿½mero sin adivinar.
- Versiones a **3** (`build.gradle.kts` iba en 2, `plugins.json` en 1 ï¿½ otro desync; `fileSize` pendiente).
- ?? **Pendiente**: compilar, instalar y reintentar ï¿½ el nuevo mensaje dirï¿½ la causa exacta.

### ??? Token OK en Android pero falla en cs3d: error real enmascarado (14 Oct 2026 v4)
**Reporte (usuario)**: mismo token+proyecto funcionan en celular y emulador, pero en cs3d da `fetchProjectId: no accesible`.
**Causa (cï¿½digo)**: `graphql()` Sï¿½ guardaba la causa real (`graphql HTTP ...` / `graphql failed: ...`), pero `fetchProjectId` la **sobrescribï¿½a** con el mensaje genï¿½rico. El fallo estï¿½ en el request desktop (red de la PC: proxy/VPN/TLS), no en el token.
**Fix v4**: si `resp==null` (fallï¿½ el request), NO sobrescribir `lastError` ï¿½ la UI mostrarï¿½ la causa real de `graphql()`.
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- ?? **Pendiente**: compilar, instalar en cs3d y reintentar ï¿½ el nuevo `ï¿½ltimo error` dirï¿½ la causa (ej. `graphql failed: ...`).

---

## TeleonlineProvider ï¿½ hang en links http + timeouts en segundos (14 Oct 2026 v3)

**Reporte (usuario)**: Studio Universal opciï¿½n 3 (`http://187.102.210.46/universo/index.m3u8`, sin S) se queda "cargando" eternamente en CloudStream Android, pero abre en VLC y en cs3d Windows. Mismo sï¿½ntoma en el provider de Ranita (v1) y en el del beta (v2).
**Verificado**: el servidor responde 200 con playlist vï¿½lida (1080p, dual audio) en 0.2s; NO acepta https (000). Red y servidor OK ? es lado app Android.
**Hipï¿½tesis principal**: bloqueo de cleartext (`http://`) en la cadena de la app Android (VLC/mpv no lo aplican) ? ExoPlayer reintenta = loading eterno. Pendiente confirmar con logcat del hang (`playerError`/Cleartext/timeout) + test de otro link `http` en CS3.
**Fix v3 (cï¿½digo)**: `safeGet` usaba `timeout = timeoutMs * attempt` con `15000L/20000L` = miles de segundos sin timeout real (regla 03 Oct 2026). Ahora `timeoutSec` en segundos (15Lï¿½intento, load 20L). `withTimeoutOrNull(20000L/15000L)` intactos (ms, correcto).
- Versiones a **3** (`build.gradle.kts` iba en 1, `plugins.json` en 2 ï¿½ otro desync; `fileSize` pendiente).
- ?? **Pendiente**: compilar, instalar y (1) pedir logcat del hang en Android buscando la excepciï¿½n exacta; (2) probar otro `http` en CS3 para confirmar bloqueo app-wide (workaround: reproductor externo VLC).

### ?? Veredicto por opciï¿½n (14 Oct 2026, sin cambio de cï¿½digo)
**Medido (usuario en dispositivo + PC)**:
| Opciï¿½n | URL | En telï¿½fono | Desde PC | Veredicto |
|---|---|---|---|---|
| 3 | `187.102.210.46` (:80) | cuelga en silencio | 200 instantï¿½neo | ruta/middlebox puerto 80 |
| 4 | `15.204.246.24:8080` | 2004 | **403** con y sin headers | proxy muerto/IP-locked, nada que hacer |
| 5 | `138.121.15.230:9002` | reproduce + freezes | ï¿½ | CDN lento (clï¿½sico) |
| 6 | `190.93.224.42` (:80) | cuelga igual que 3 | 200 instantï¿½neo | igual que 3 |
- Patrï¿½n: las dos en **puerto 80** cuelgan en el telï¿½fono pero responden al instante desde fuera + abren en VLC ? middlebox transparente del operador/red hacia esas IPs (tarpit en sesiones largas; VLC reconecta agresivo y lo tolera, ExoPlayer espera). Descartado: headers (200 con y sin ellos), cleartext-policy (sin excepciï¿½n), DNS, servidor.
- Siguiente paso (usuario): **VPN en el telï¿½fono** y probar 3 y 6 ï¿½ el tï¿½nel cifrado evita la intercepciï¿½n del puerto 80. Si con VPN abren, caso cerrado.

### ?? Auto-activaciï¿½n ante 403 (14 Oct 2026 v3)
**Hallazgo (cï¿½digo de activar_25.php)**: la "activaciï¿½n" es `fetch("", {method:"POST"})` ï¿½ POST vacï¿½o a la propia pï¿½gina, SIN enviar ningï¿½n cï¿½digo (`copyCode` solo copia al portapapeles) y con el `10h 00m` hardcodeado en el JS. El ï¿½nico efecto servidor posible es listar la IP de origen (~10h). Mecanismo de la hipï¿½tesis del usuario CONFIRMADO a nivel cï¿½digo.
**Fix**: `tryActivate()` replica ese POST (misma IP del dispositivo, lock + cooldown 30min) y el interceptor, ante 403 de `strymix.com`, activa y **reintenta UNA vez** (`[activar] POST -> code` + `[cdn] retry -> code`). Sin 403 no hay requests extra. Omite leer estado de la app INFINITY (imposible entre paquetes) y no muestra avisos (un provider no puede).
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- ?? **Pendiente**: compilar, instalar y validar en el experimento 22:30 ï¿½ esperado `[cdn] ... -> 403` ? `[activar] POST -> 2xx` ? `[cdn] retry -> 200`.

### ??? Renombre a StrymixTV (14 Oct 2026, reseteo a v1)
**Motivo (usuario)**: no usar la marca de la app INFINITY.
- Mï¿½dulo `InfinityProvider/` ? `StrymixtvProvider/` (clases `StrymixtvPlugin`/`StrymixtvProvider`, `name = "StrymixTV"`, TAG de logs `StrymixTV`). Los labels `"$name [...]"` se actualizan solos. Namespace interno de data-URLs (`/infinity/channel/`) se deja intacto (invisible).
- `plugins.json`: entrada `Infinity` reemplazada por `StrymixtvProvider`/`StrymixTV` v1 (cï¿½digo ex-v3, `fileSize: 0` pendiente).
- Icono recomendado: favicon auto de Google sobre `fluxa.strymix.com` (`strymix.com/favicon.ico` da 404) ï¿½ neutral, sin marca ajena, siempre resuelve.
- ?? **Consecuencia**: al cambiar `internalName`+`name`, CS3 lo ve como plugin NUEVO ï¿½ desinstalar el viejo `Infinity` tras instalar este; favoritos guardados con apiName `Infinity` no migran solos.
- ?? **Pendiente**: compilar (`.\gradlew.bat :StrymixtvProvider:make --console=plain -q`), `fileSize`, instalar y filtrar logs con `StrymixTV:V` (el TAG cambiï¿½).

---

## TokyvideoProvider ï¿½ Plugin nuevo (16 Oct 2026 v1)

Sitio: `tokyvideo.com` (portal de videos + series subidos por usuarios, ES/EN).

### Cadena verificada end-to-end (serie Mis amigos + video 1x14)
- Serie `/es/serie/{slug}/{N}` (paginada): header `.series-detail--content--title/desc`, poster `img[data-src]` (lazy: `src` es gif 1px), episodios `div.thumb-video` (link `a.thumb-duracion[href=/es/video/]`, thumb `img[data-src]` `img89.../thumbs_0001.jpg`, `span.duracion`, tï¿½tulo `.thumb-video__meta h3 a` con forma `1x14. ...`).
- Video `/es/video/{slug}`: **`video#tokyvideo_player > source[src]` con mp4 directo `https://cdnst{N}.tokyvideo.com/videos/.../mp4/{hash}.mp4?secure={token},{expiry}`** (server-rendered, token fresco por request, atado a la IP que pide la pï¿½gina).
- Playback verificado desde PC: `206 video/mp4` con Range y SIN headers/referer. Search `/search?q=` server-side OK.

### Implementaciï¿½n (v1, sin compilar por regla del repo)
- `getMainPage`: 4 secciones (Tendencia `/series/trends`, Pelï¿½culas `/movies`, Dibujos `/cartoon-series`, Series TV `/television-series`); pag `?page=N` (patrï¿½n inferido, verificar); `hasNext` por `.pagination/[rel=next]/[href*=page]`.
- `search()`: `/search?q=` + `URLEncoder`, mismo parser de cards.
- `load()`: `/serie/` ? `TvSeriesLoadResponse` (temporada/episodio del `1x14` del tï¿½tulo, poster por episodio); `/video/` ? `MovieLoadResponse` con `dataUrl=url`.
- `loadLinks()`: `video#tokyvideo_player source[src]` ? fallback `video source` ? fallback regex mp4; emite VIDEO directo con referer = pï¿½gina.
- Timeouts en segundos (`15L`); sin interceptor (mp4 directo sin requisitos); JSON innecesario (todo DOM).
- Archivos: `build.gradle.kts` (v1, es, Movie+TvSeries), `AndroidManifest.xml`, `TokyvideoPlugin.kt`, `TokyvideoProvider.kt`; `plugins.json` ? entrada `Tokyvideo` v1 (`fileSize: 0` pendiente).
- Balance `bal_check.py` ? OK.
- ?? **Pendiente**: compilar (`.\gradlew.bat :TokyvideoProvider:make --console=plain -q`), `fileSize`, instalar y probar home/search (cards y paginaciï¿½n de secciones), serie Mis amigos (episodios con posters) y reproducciï¿½n del mp4 directo. Logs: `adb logcat -s Tokyvideo:V`.

### Search multi-palabra + seccion Series (Tokyvideo 16 Oct 2026 v2)
**Reporte (usuario)**: buscar "mis amigos tigger y pooh" no trae lo esperado (la pagina de la serie).
**Causa**: el `/search?q=` del sitio es flojo con multi-palabra (OR ruidoso: musica con "mi"/"amigos") y no devuelve paginas de serie, solo videos sueltos. No hay endpoint de suggest/series (`/api/search` y `/search/suggest` dan 404; `/series?search=` se ignora).
**Fix**:
- `search()`: query completa + cada token significativo (>=3 letras, folding de acentos) como queries separadas (max 5 fetches); score = n. de tokens matcheados en el titulo; filtra `score>0`, ordena desc, tope 40. Asi esa query trae los episodios con Pooh/Tigger arriba.
- Nueva seccion home `/series` (indice paginado `/series/{N}`, cards `div.thumb-serie` con `.thumb-serie__name` + `img[data-src]`); `pageUrl()` distingue ese patron de `?page=`.
- `load()` ya cubria `/series/{slug}` por la rama plural (episodios = videos listados).
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y buscar "mis amigos tigger y pooh" (deben salir episodios con Pooh/Tigger arriba) + abrir la seccion Series.

### ?? Fix hrefs absolutos + fallback ï¿½ndice de series (Tokyvideo 16 Oct 2026 v3)
**Log v2 (usuario)**: search con scoring OK (37 resultados), secciones de video OK (24 c/u), pero **secciï¿½n `Series` ? 0 items** con HTML recibido. Misma causa raï¿½z probable que el `fixUrl()` de Donghualife v6: el selector `a[href^=/series/]` (empieza-por) no matchea si el servidor manda hrefs **absolutos** (`https://...`).
**Fix**:
- `parseSerieCards`: `^=` ? `*=` (contiene) + fallback genï¿½rico (`a[href*=/series/]` con nombre de `.thumb-serie__name`/img-alt/texto/slug) + log con `html.length` para diagnosticar el markup real si sigue en 0.
- `search()`: si el mejor video matchea <2 tokens y hay =2 tokens, barre el ï¿½ndice `/series/{N}` (mï¿½x 6 pï¿½gs, stop al primer match bueno): serie con score=2 (o =1 si videos en 0) se antepone a los videos. Es lo que pone la pï¿½gina "Mis amigos" arriba para "mis amigos tigger y pooh".
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y (1) abrir secciï¿½n Series (debe listar); (2) buscar "mis amigos tigger y pooh" (la serie arriba + episodios Pooh/Tigger).

### ?? Secciï¿½n Series en espaï¿½ol (Tokyvideo 16 Oct 2026 v4)
**Detalle (usuario)**: `https://www.tokyvideo.com/es/series` es el ï¿½ndice de series en espaï¿½ol.
**Verificado**: paginado `/es/series/{N}`, cards `div.thumb-serie` ? `/es/serie/{slug}` (mismo formato que Mis amigos, `load()` lo cubre).
**Fix**: secciï¿½n Series ? `/es/series` (antes `/series` en inglï¿½s); el barrido de search tambiï¿½n usa `/es/series` (nombres en espaï¿½ol matchean mejor queries en espaï¿½ol). `pageUrl()` ya lo cubrï¿½a (`endsWith("/series")`).
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar e instalar (trae todo v2-v4 de una vez).

### ?? Fix singular/plural serie (Tokyvideo 16 Oct 2026 v5)
**Log v4**: secciï¿½n `Series` ? 0 items con HTML recibido (y el barrido de search tambiï¿½n fallaba por lo mismo).
**Causa raï¿½z**: las cards de `/es/series` enlazan a `/es/serie/` (SINGULAR) y el selector pedï¿½a `href*=/series/` (plural). Mismo bug conceptual que el `^=` de v3, al revï¿½s.
**Fix**: match `href*=/serie` (cubre singular + plural) en `parseSerieCards` directo y fallback (+ exclusiï¿½n del propio ï¿½ndice `/es/series`). Verificado contra HTML con headers de la app: 24 `div.thumb-serie` + 48 hrefs `/es/serie/`.
- Versiones a **5** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y abrir Series + buscar "mis amigos tigger y pooh" (serie antepuesta).

### ?? Barrido de series tambiï¿½n en empates (Tokyvideo 16 Oct 2026 v6)
**Log v5**: `search mis amigos tigger y pooh -> 37 videos (best=2)` sin lï¿½neas de serie: con `best=2` no se disparaba el barrido y la pï¿½gina Mis amigos nunca se anteponï¿½a (los videos con 2 tokens suelen ser ruido musical que empata).
**Fix**: el barrido corre tambiï¿½n con 3+ tokens y `best<=2` (los videos con match fuerte 3+ lo siguen evitando). La serie empatada (Mis amigos = mis+amigos) va primera.
- Versiones a **6** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y buscar "mis amigos tigger y pooh" (serie antepuesta) + abrir secciï¿½n Series (aï¿½n sin verificar en v5).

### ?? Episodios multi-pï¿½gina (Tokyvideo 16 Oct 2026 v7)
**Reporte (usuario)**: los videos `2x20-3` y `2x21-3` no aparecï¿½an en la lista de Mis amigos.
**Verificado**: sï¿½ estï¿½n ï¿½ en la pï¿½gina 1 (24 videos; /2 ? 24, /3 ? 15 = 63 total). El `load()` solo leï¿½a UNA pï¿½gina (la de entrada), asï¿½ que segï¿½n por dï¿½nde entraras faltaban episodios.
**Fix**: `load()` normaliza la base (quita `/N` final) y junta `div.thumb-video` de p1..p10 con stop sin hrefs nuevos (`seenHrefs`), numeraciï¿½n global. Ambos videos resuelven mp4 directo (`cdnst90...?secure=`, verificado) sin cambios en `loadLinks`.
- Versiones a **7** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y abrir Mis amigos (deben salir los 63).

### ?? Serie vï¿½a pï¿½gina del video (Tokyvideo 16 Oct 2026 v8)
**Log v7**: el barrido del ï¿½ndice corre (p1-p6) pero da **0 matches en las 6** ï¿½ al dispositivo `/es/series` le llega sin cards parseables (en PC sï¿½ hay 24 `div.thumb-serie`; causa exacta sin diagnosticar: posible challenge/consent para esa ruta en el cliente).
**Hallazgo (HTML de video)**: cada pï¿½gina de video trae `global_tokyvideo_serie_name = 'mis-amigos'` (+ `_mtm serieVideo`). Es la vï¿½a que usa el propio sitio para asociar episodios?serie.
**Fix**: en vez de barrer el ï¿½ndice, se resuelve la serie de los 4 mejores videos (fetch de la pï¿½gina + regex del slug ? card `/es/serie/{slug}` antepuesta). Mï¿½s barato (=4 fetches chicos) y fiable (las pï¿½ginas de video sï¿½ parsean en el dispositivo).
- Versiones a **8** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y buscar "mis amigos tigger y pooh" buscando `search ... serie: Mis Amigos` en el log.

### ?? Search vï¿½a API de sugerencias (Tokyvideo 16 Oct 2026 v9)
**Hallazgo (usuario)**: al escribir en el buscador salen dos tipos (series + videos) ANTES de buscar ï¿½ es el select2 con AJAX a `https://api.tokyvideo.com/search-ajax?q=` (URL en `global_tokyvideo_search_api` del HTML; funciona sin Referer).
**Verificado**: devuelve `{results:[{text, children:[{title|name, thumb, id, type: video|serie}]}]}` (~15KB). Grupo "Serie results" existe (ej. `q=running` ? 1 serie + 56 videos), pero para "mis amigos" el grupo serie trae ruido o vacï¿½o (el API matchea flojo igual que la web).
**Fix**: `search()` usa el API (query + tokens, mï¿½x 5 fetches chicos) parseando AMBOS grupos con el mismo scoring; series del API primero, luego resoluciï¿½n video?serie (v8, se conserva para "Mis amigos" vï¿½a sus episodios). JSON con `org.json` (nunca kotlinx).
- Versiones a **9** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y buscar "mis amigos tigger y pooh" (log `api -> N series + M videos`).

### ?? lang=es_ES abre el grupo de series (Tokyvideo 16 Oct 2026 v10)
**Aporte (usuario, curl con cookies de navegador)**: su request con `?lang=es_ES&q=mis+amigos&wr=` + cookies devolvï¿½a "Resultados en series" con **Mis amigos** (`/es/serie/mis-amigos`) + Spidey, cosa que mis pruebas sin `lang` jamï¿½s traï¿½an.
**Aislado**: `lang=es_ES` es la llave (probado `?lang=es_ES&q=&wr=` sin cookies ? "Resultados en series" 2 + vï¿½deos 56; sin `lang` ? solo "Video results" en inglï¿½s). Las cookies (`url_type=series_videos` etc.) son contexto de su navegaciï¿½n, no necesarias.
**Fix**: `search-ajax?lang=es_ES&q=...&wr=` en todas las queries del `search()`.
- Versiones a **10** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- Pendiente: compilar, instalar y buscar "mis amigos" (serie Mis amigos primera, sin necesidad del fallback video?serie).

### ? Veredicto EP1/EP5 Mis amigos: archivos mal en origen (16 Oct 2026, sin cambio de cï¿½digo)
**Reporte (usuario)**: EP5 se congela cada 2-3s, EP1 no inicia. V10 verificado OK (serie 63 eps + links mp4 emitidos).
**Medido**: ambos mp4 ~390MB (~2.7 Mbps), mismo host `cdnst89`, descarga rï¿½pida desde PC (2MB en <1s), extracciï¿½n idï¿½ntica.
**Confirmaciï¿½n (usuario)**: en la web pasa IGUAL (EP5 congela, EP1 no arranca) ? archivos/streaming mal en origen (CDN), no provider, no dispositivo, no ruta.
- Sin fix de cï¿½digo. Recomendaciï¿½n: otros episodios u otro horario; si un episodio falla igual en web y plugin, es la fuente.

---

## DoggitvProvider â€” Plugin TV en vivo desde indice IPTV (16 Oct 2026 v1)

### Que es
- Seccion de **TV en vivo** del APK Serbero Play TV (`com.serberoplaytv` v3.0, versionCode 12) portada a CloudStream. Solo el live TV; el catalogo de pelis/series (Firestore+TMDB+extractores de embeds) NO se porto.
- Nombre elegido por el usuario para evitar la marca original: **DoggiTV** (modulo `DoggitvProvider`). Creditos: autor Ranita; fuentes = playlists publicas de **iptv-org**; inspirado en Serbero Play TV.

### Cadena verificada (APK, con jadx)
- `ChannelsApi.getChannels()` = `@GET("canales.json")` sobre base `UrlApi.CHANNELS_API_URL = https://sswwswsw.github.io/` -> `GET https://sswwswsw.github.io/canales.json` (200, ~13 KB) -> array `{Flag (PNG base64), listURL, Name}` con **23 paises/categorias**.
- Cada `listURL` es un **M3U** estandar; `ChannelsRepositoryImpl.parseM3U` lee `#EXTINF` (`tvg-id`,`tvg-logo`,`group-title`,`,name`) + linea URL -> `Channel{tvgId,tvgLogo,groupTitle,name,url}`.
- 21 fuentes son de `iptv-org.github.io/iptv/countries/{bo,ar,co,us,br,mx,es,cl,uy,ec,pa,hn,pe,cu,ni,ve,cr,fr,ru,pr}.m3u` + `categories/sports.m3u`; 2 propias de Serbero: `sswwswsw.github.io/sports.m3u` (77 canales, verificado) y una XXX. Sin auth ni cifrado; los canales son HLS `.m3u8` directos (tamano listas: us 318 KB max).

### Implementacion (v1, sin compilar por regla del repo)
- `mainPage = mainPageOf(...)`: 23 secciones (una por pais, `listUrl to label`) con los paises hardcodeados (derivados de `canales.json`); asi el home carga **solo la seccion que se ve** (lazy), estilo StrymixTV.
- `getMainPage(page, request)`: `request.data` = `listUrl` -> `fetchChannels` -> `HomePageList(request.name, canales)` con **los canales directos** (no una fila de paises). Al tocar un canal va a `load()`.
- `load(url)`: si `isPlaylist(url)` (`.m3u`) -> lista los canales del pais (fallback); si es canal -> `newTvSeriesLoadResponse(<nombre del canal>, url, TvType.Live, [episodio])`. **El nombre real del canal sale de `channelNames`** (ConcurrentHashMap url->nombre, poblado en `parseM3U`), asi al abrir NO dice "En Vivo" sino el nombre del canal.
- `loadLinks(data=HLS)`: emite `newExtractorLink(name, name, url, type)` (type por extension: `.mpd`->DASH, `.mp4/.mkv/.webm`->VIDEO, resto M3U8). Sin headers/referer (los streams iptv-org no los piden). `source == "DoggiTV"`.
- `search(query)`: `amap` sobre las 23 listas (sintaxis de punto `countries.amap { }`, NO `amap(countries)` â€” en Kotlin las extensiones no se llaman asi), filtra por nombre (fold de acentos con `Normalizer`), `distinctBy url`, tope 80 -> `newLiveSearchResponse`. **Verificado contra datos reales: 5122 canales; "espn"->86, "tyc"->6, "cnn"->8.**
- Timeouts en segundos (`40L`). Sin interceptores. URLs siempre absolutas (evita el prepend de `fixUrl`). `import com.lagradost.cloudstream3.amap`.
- Etiquetas de pais con acentos via escapes `\uXXXX` (`M\u00e9xico`, `Espa\u00f1a`, `Panam\u00e1`, `Per\u00fa`) para evitar problemas de encoding.
- **Posters**: la mayoria de canales traen `tvg-logo` (paises LATAM 0 sin logo; USA 27/1483; sports 2/476). Los que no lo traen tampoco tienen `tvg-id`, asi que no se recuperan de la base de logos de iptv-org (`api/logos.json`, 5.7 MB). Relleno con `posterFor()` -> `https://placehold.co/320x320/1e1e2e/e0e0e0/png?text=<nombre>` (verificado 200 image/png) solo cuando falta el logo; se quita el sufijo `(720p)` del texto.
- **Filtro de adultos**: se quito la seccion `XXX` (la lista `THEMMMINAT/xxx-IPTV`) y ademas `parseM3U` descarta canales con `isAdult()` (keywords `xxx/adult/nsfw/porn/erotic/18+/brazzers/penthouse/hustler/...` sobre nombre+group+id; excepcion `adult swim` para no borrar el canal de Cartoon Network). Verificado: 0 falsos positivos en us/mx/es/pe/co/br/ar (no descarta canales legitimos).
- Archivos: `build.gradle.kts` (v1, mx, `Live`), `AndroidManifest.xml`, `DoggitvPlugin.kt`, `DoggitvProvider.kt`. `settings.gradle.kts` auto-incluye (el dir tiene `build.gradle.kts`). Balance `bal_check.py` OK.
- `plugins.json`: entrada `DoggiTV`/`DoggitvProvider` v1 (`fileSize: 0` pendiente).

### Pendiente
- Compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar home (23 paises) + un pais (p.ej. Argentina/Mexico) + reproducir un canal. Logs: `adb logcat -s DoggiTV:V`.
- Opcional si el usuario quiere: mostrar flag por pais (decodificar el base64 `Flag` a archivo local) y/o listar canales directamente en el home.

### ?? Canales negros/sin audio en emulador WSA (18 Oct 2026, diagnostico, sin cambio de codigo)
**Reporte (usuario)**: en el CloudStream original corriendo en **emulador WSA**, algunos canales quedan "reproduciendo" en negro sin audio; en **cs3d** (Windows, misma red) reproducen. Ejemplos: `Studio Universal Latin America Brazil (720p)`, `Sony Channel (Argentina) (1080p)`.
**Medido (PC)**:
- Ambas IPs son **restreamers** con el patron `tracks-v1a1/mono.m3u8` + audio en grupo `#EXT-X-MEDIA TYPE=AUDIO` (spa default + eng):
  - `Studio Universal LA Brazil` = `http://187.102.210.46/universo/index.m3u8` (**puerto 80**, 1080p avc1.640028, 5.5 Mbps).
  - `Sony Channel Argentina` = `http://138.121.15.230:9002/SONY/index.m3u8` (**puerto 9002**, 1080p).
- **Son las mismas IPs ya documentadas en la seccion Teleonline** (`187.102.210.46:80` = "cuelga en el telefono en silencio, 200 instantaneo desde PC, middlebox puerto 80"; `138.121.15.230:9002` = "reproduce + freezes, CDN lento").
- Master y **segmentos responden 200/206 desde PC** (por eso cs3d reproduce). Master con UA ExoPlayer vs Chrome = identico; codecs `avc1`+`mp4a` estandar; CloudStream Android tiene `usesCleartextTraffic="true"` (verificado en el manifest oficial) -> **no es headers, ni codecs, ni cleartext**.
**Veredicto WSA**: en WSA el problema puede ser su NAT; pero el usuario confirmo que **tambien falla en celular fisico en la MISMA WiFi que la PC** (donde cs3d reproduce) -> **no es la red, es el player Android**.
**Descartado en PC (misma red)**: master/segmentos 200/206; UA ExoPlayer vs Chrome identico; `Keep-Alive` reutilizando conexion OK (no es tarpit de pooling); audio renditions `tracks-a1/a2` y video `tracks-v1a1` con segmentos OK. Cleartext permitido en la app. Codecs `avc1`(H.264)+`mp4a`(AAC) estandar.
**Estructura del master (sospechosa)**: `#EXTM3U` **sin `#EXT-X-VERSION`** + `#EXT-X-MEDIA:TYPE=AUDIO` (spa/eng) con URIs `tracks-a{1,2}/mono.m3u8` + un unico `#EXT-X-STREAM-INF AUDIO="aac" RESOLUTION=1920x1080 CODECS="avc1.640028"` -> `tracks-v1a1/mono.m3u8`. Verificado por bytes del segmento: `tracks-v1a1/mono.m3u8` **ya trae video+audio muxado** (H.264 startcodes + sync ADTS AAC). Hipotesis: ExoPlayer gestiona mal este master (rendicion de audio separada / version ausente) -> "cargando" infinito; un player desktop lo tolera.
**Posible fix (pendiente de decidir)**: en `loadLinks`, si el `.m3u8` es un master restreamer, emitir la **media playlist del variant muxado** (`tracks-v1a1/mono.m3u8`) directo en vez de/ademas del master, evitando `#EXT-X-MEDIA`. Falta confirmar con logcat del celular fisico (`ExoPlayer`/`MediaCodec`/`CS3IPlayer`) si es parseo del master o decodificador.

### Fix: master con grupo de audio -> emitir variant muxado (19 Oct 2026 v2)
**Logcat del celular fisico (SM-A065M, Android 36, app `com.lagradost.cloudstream3.prerelease`, AndroidXMedia3/1.9.3, `Using CronetDataSource`)** en `Studio Universal Latin America Brazil`:
- `loadOnlinePlayer ExtractorLink(name=DoggiTV, url=http://187.102.210.46/universo/index.m3u8, referer=, type=M3U8)`.
- `avc_utils: found AVC codec config (1920 x 1080, High-profile level 4.0)` -> el video SI se parsea; se crean decoders de video (`c2.mtk.avc.decoder`) **y de audio** (`c2.sec.mp3.decoder.mpeg-L2`, mime `audio/mpeg` = MP3, 48000 Hz 2ch, `DEFAULT=YES` = rendition `tracks-a1`).
- El decoder de audio da `E/Codec2-ComponentInterface: We have a failed config` + `D/CCodecConfig: config failed => CORRUPTED`; y el estado queda **`BUFFERING(6), position=-1, buffered=-1`** repitiendose cada ~3s sin avanzar (nunca primer frame) -> "reproduce" en negro sin audio.
**Prueba TS (PC, `doggi_pids.py`/`doggi_pmt.py` sobre el primer segmento)**:
- Master real: `#EXTM3U` **sin `#EXT-X-VERSION`** + 2x `#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="aac",...URI="tracks-a{1,2}/mono.m3u8"` + un unico `#EXT-X-STREAM-INF ...,AUDIO="aac",RESOLUTION=1920x1080,CODECS="avc1.640028",CLOSED-CAPTIONS=NONE` -> `tracks-v1a1/mono.m3u8`.
- Conteo de PIDs del TS: `tracks-a1` = **solo audio** (PID 0xdd, 83-89%); `tracks-v1a1` = **muxado video+audio** (PID 0xd3 55-56% + PID 0xdd 38-39%). O sea el variant ya trae el audio; el grupo `#EXT-X-MEDIA` es redundante.
**Causa raiz**: media3 1.9.3 + este master no estandar (sin version + grupo de audio separado + variante unica muxada) selecciona la rendition de audio separada (MP3) y se atasca en `BUFFERING` (`position=-1`) con el decoder MP3 en `CORRUPTED`; un player desktop (cs3d) lo tolera.
**Fix (`DoggitvProvider.kt:loadLinks`)**: nueva `muxedVariant(master)`: si el body del `.m3u8` contiene `#EXT-X-MEDIA:TYPE=AUDIO` y hay **exactamente un** `#EXT-X-STREAM-INF`, devuelve la URI del variant resuelta absoluta (`java.net.URI(master).resolve(...)`); si falla -> null. En `loadLinks`, para type `M3U8`, si `muxedVariant` no es null se emite **esa** playlist en vez del master (evita el grupo de audio). 1 fetch extra por reproduccion (barato; los masters que no matchean devuelven null). Fail-open: si el fetch/parse falla, emite el master como antes.
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0). Balance `bal_check.py` OK.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar `Studio Universal Latin America Brazil` + `Sony Channel (Argentina)` â€” buscar `master con grupo de audio -> variant muxado` en logcat y confirmar que reproduce con audio (sin `BUFFERING position=-1`).
- Nota: si algun dia un canal iptv-org trae un variant **solo-video** con grupo de audio, este fix lo dejaria sin audio (edge case aceptado; los restreamers de estas listas son muxados).

### âœ… VEREDICTO FINAL: audio MPEG-1 Layer II + ajuste "Decodificacion por software" (19 Oct 2026 v3, SIN cambio de codigo)
**v2 verificado como aplicado** en el celular fisico: logcat `08:24:23.563 CS3ExoPlayer(4809): loadOnlinePlayer ExtractorLink(name=DoggiTV, url=http://187.102.210.46/universo/tracks-v1a1/mono.m3u8, ..., type=M3U8)` â€” emite el **variant muxado**, no el master. O sea la hipotesis "v2 no instalada" quedo descartada, y aun asi el canal sigue en negro -> el `#EXT-X-MEDIA` **no era la causa**.
**Evidencia del celular (pid 4809, SM-A065M, AndroidXMedia3/1.9.3)**: `ExoPlayerImpl Init` -> `PlayerView: Handle event: TracksChangedEvent(source=Player)` (pistas preparadas) -> `CCodec: allocate(c2.mtk.avc.decoder)` (video, RUNNING, `raw.crop 319x239`) y `allocate(c2.sec.mp3.decoder.mpeg-L2)` + `CCodecConfig: raw.sample-rate 48000` + `C2SecMp3Dec: Reconfiguring decoder: 0->48000 Hz, 0->2 channels` (`state->set(RUNNING)`) + `AudioTrack: setVolume(1.0)`. Estado clavado en **`PlaybackState {state=BUFFERING(6), position=-1, buffered=-1}`** de 08:24:23 a 08:24:50, sin `Rendered first frame`, sin `DecoderInitializationException`/`PlaybackException`/`IOException`. La red **no** es el problema: `PesReader(4809)` (TsExtractor de ExoPlayer) lee un segmento TS cada ~6s; los `MediaHTTPConnection(4809) 200/206` son el **generador de previews** (`MediaMetadataRetriever`, destructor a 24.793), no ExoPlayer.
**Causa raiz CONFIRMADA (codigo real del stream)**: el audio de estos canales es **MPEG-1 Layer II**, no AAC. Segmento real `2026/10/17/13/34/44-06406.ts` (1507008 B) -> PMT: `pid=0xd3 stream_type=0x1b` (H.264), `pid=0xdd stream_type=0x04` (MPEG-2 audio = Layer II); headers de frame en PID 0xdd: `lay=2`, `ver=3`. El master **miente** con `GROUP-ID="aac"`. El canal que SI funciona (`Global TV (Peru)` = `http://190.93.224.42/GLOBAL/index.m3u8`) trae **AAC** (`CODECS="avc1.4d4028,mp4a.40.2"`) y **sin** `#EXT-X-MEDIA`. No existe variante AAC en el restreamer que falla (`tracks-v1a1/mono.ts.m3u8` y `...mono.aac.m3u8` -> 404; `tracks-a1/a2/v1a1` devuelven la MISMA playlist).
**Desempate del usuario**: **VLC reproduce el canal con normalidad** en el mismo movil -> el stream esta bien y el fallo es **del reproductor ExoPlayer/Media3 del equipo** con el audio Layer II (mismo caso que androidx/media#2519 y recloudstream#1790).
**Fix real = ajuste de la APP (no del plugin)**: CloudStream `Settings > Player > Software decoding` (`@string/software_decoding_key`). En `CS3IPlayer.kt`: `-1`(auto) en PHONE = FFmpeg activo pero `EXTENSION_RENDERER_MODE_ON` = **prefiere hardware** -> elige `c2.sec.mp3.decoder.mpeg-L2` y se atasca; `2` (SW+HW) usa `EXTENSION_RENDERER_MODE_PREFER` -> decodifica con `FfmpegAudioRenderer`. **Cambiar de "Automatico" a la opcion de software**.
- **El mismo ajuste sirve en el TV**: `SettingsPlayer.kt` solo oculta en TV gestos/rotacion/velocidad (`software_decoding` NO esta en `hidePrefs`). Muchos Android TV decodifican MP2 nativo, asi que en TV puede que ni haga falta.
- **No hay fix posible desde el provider**: un plugin no puede transcodificar audio ni cambiar el decoder elegido. `muxedVariant` (v2) queda **neutro** (devuelve null en masters sin grupo de audio; 1 fetch extra por reproduccion) â€” se puede dejar o revertir, no afecta el resultado.
- âœ… **Probado por el usuario (SW+HW)**: cambio "Decodificacion por software" a **SW+HW** en el movil y **tampoco reproduce** (seguia en negro). Posible motivo: o el build FFmpeg de CloudStream no expone MP2 (en media#2519 los decoders habilitados listan `mp3`, no `mp2`), o al forzar software en TODO el video 1080p el decoder no da frames. No se investigo mas.
- **Decision del usuario (cerrado)**: usar **VLC** para estos canales. Un player independiente (y cs3d en Windows) SI reproduce -> el stream esta bien y el limite es el **player interno de CloudStream en Android**. Se retomaria solo si CloudStream cambia/mejora su reproductor. `muxedVariant` (v2) se deja tal cual: inofensivo.
- **TV**: mismo ajuste `software_decoding` disponible ahi, pero con el mismo pronostico (mismo ExoPlayer); si el TV trae decoder MP2 nativo puede que funcione, si no -> VLC/player externo en el TV tambien.

### ðŸ›°ï¸ Chequeo `Mira-2.5.0.apk` (otra app con TV en vivo) â€” nada que rescatar (19 Oct 2026)
**Pregunta del usuario**: revisar `C:\Users\Ruth Riveiro\Downloads\Mira-2.5.0.apk` (96508988 B) por si hay algo rescatable para el repo. Decompilado con jadx en `%TEMP%\opencode\mira`.
- **Que es**: app **React Native/Expo** (SDK 58, `com.mira.app` v2.5.0/6, Expo projectId `982df5d6-...`) con **libVLC** como player (`assets/lua/*`, `.sofa`) y core CloudStream embebido (`com.lagradost.*`, solo utils; **sin providers nativos**). Su catalogo sale de extensiones en runtime (esquemas `aniyomi`/`tachiyomi`/`cloudstreamrepo`; repos `yuzono/anime-repo`, MangaYomi). Toda la logica propia vive en el JS bundle (`resources/assets/index.android.bundle`, 7.6 MB).
- **TV en vivo = iptv-org, identico al nuestro**: `iptv-org.github.io/iptv/index.m3u` (global) + `iptv-org.github.io/iptv/languages/spa.m3u` (2402 canales, 512 KB). Parser igual (`tvg-id`/`tvg-logo`/`group-title`, filtro `XXX`/`ADULT`/`NOT 24/7`). EPG via `iptv-epg.org` (no portable: CloudStream no tiene UI de EPG).
- **Scrapers propios** (dominios del bundle): pelis/series `cineova.site`, `cinemitas.org`, `pelis182.net`, `pelispanda.org`, `megadede.mobi`, `peliculasflix.com`, `allcalidad.re`, `allpeliculas.la`, `www3.seriesmetro.net`; anime `animeav1.com`, `tioanime.com`, `3asq.online`, `api.jikan.moe`; embeds `streamtape/voe.sx/ok.ru/embed69.org/tioplus.app`. Casi todos ya cubiertos por providers del repo.
- **Valor colateral**: Mira reproduce esos restreamers con libVLC/FFmpeg -> **confirma el veredicto de DoggiTV** (el fallo es el player interno de CloudStream, no la fuente).
- **Decision del usuario (cerrado)**: sin cambios. Si algun dia se retoma, la unica mejora de TV seria aÃ±adir listas por **idioma** de iptv-org (ej. `spa`).

### ðŸ–¼ï¸ Fix posters que no cargaban (19 Oct 2026 v3)
**Reporte (usuario)**: en DoggiTV algunos canales seguian sin poster ("los que no tienen posters").
**Diagnostico (verificado en PC, `%TEMP%\opencode\logo_check2.py` / `logo_check3.py`)**: iptv-org ya NO tiene entradas sin `tvg-logo` (0 sin-logo en las 22 listas, incl. us 1483 y sports 476). O sea "los que no tienen poster" = entradas cuyo `tvg-logo` **no renderiza**:
1. **URLs muertas** (404/400): `chikitoonz.com`, `assets.rudo.video` (Latina Noticias/Podcast), `i.postimg.cc` y `i0.wp.com` (de `sswwswsw.github.io/sports.m3u`) -> la imagen falla y CloudStream no tiene fallback por-imagen.
2. **SVG crudo** (sin `.png`): `2024_Disney_Channel_text_logo.svg`, `2024_Disney_Jr._Logo.svg` -> Coil no decodifica SVG -> card vacia.
**Causa raiz**: `posterFor()` solo usaba el placeholder cuando el logo venia vacio; un logo presente-pero-roto quedaba como URL unica sin fallback.
**Fix**: `posterFor()` ahora envuelve el logo en **wsrv.nl** con fallback: `https://wsrv.nl/?url=<logo>&w=320&h=320&fit=contain&default=<placeholder>`. Verificado (200 image/png): logo png ok (8209 B), svg convertido a png (9522 B), **URL muerta -> devuelve el placeholder** (4358 B = tamano exacto del `placehold.co`), http/svg/hosts con 429-404-400 cubiertos por `default`. Sin logo sigue devolviendo `placehold.co` directo (ya funcionaba). Ventaja extra: resize 320px -> posters mas livianos.
- Versiones a **3** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0). Balance `bal_check.py` OK.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y comprobar que TODOS los canales muestran poster (logo real o placeholder con el nombre) â€” en particular `chikitoonz`, `Latina Noticias 24/7`, `Disney Channel Latin America` y `GOLPERU`.

### ðŸ–¼ï¸ Fix poster negro en el detalle (load) (19 Oct 2026 v4)
**Reporte (usuario)**: al entrar a un canal, la pantalla de detalle (`load`) sale **negra** (sin poster), aunque la card del home sÃ­ tenga imagen.
**Causa raiz**: `load()` devolvia `newTvSeriesLoadResponse(...)` **sin `posterUrl`** en ninguna de las 2 ramas (canal suelto y playlist de pais). El detalle usa el poster del `LoadResponse`, no el del `SearchResponse` de la card -> negro siempre, aunque el logo exista.
**Fix**:
- Nuevo mapa `channelLogos` (`ConcurrentHashMap<String,String>`) poblado en `parseM3U` junto a `channelNames` (misma key = URL del stream).
- Rama canal: `posterUrl = posterFor(title, channelLogos[u])` en el `LoadResponse` **y** en el `Episode` (el `newEpisode` antes tampoco lo tenia).
- Rama playlist de pais: `posterUrl = posterFor(title, null)` (placeholder con el nombre del pais).
- Nota: si `load()` corre en una sesion donde la lista no se descargo antes (app recien abierta), `channelLogos[u]` es null y cae al placeholder (nunca negro).
- Versiones a **4** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0). v3+v4 salen en el mismo build (v3 nunca se compilo). Balance `bal_check.py` OK.
- â¸ï¸ **Pendiente**: compilar, actualizar `fileSize`, instalar y abrir un canal (ej. `Studio Universal`) â€” el detalle debe mostrar el logo (o el placeholder con el nombre), no negro.

### ðŸ› Causa raiz: wsrv.nl NO puede con imgur (Peru = 166/207 imgur) (19 Oct 2026 v5)
**Reporte (usuario)**: en los canales de **Peru** (ATV Peru, Global TV, Ucayalina de Television) en vez del poster sale **el nombre** (el placeholder); America Television (logo de **wikimedia**) si muestra poster.
**Causa raiz (medida con Python en la red del usuario)**: `wsrv.nl` **devuelve el placeholder (`default`) para `i.imgur.com`** (404 upstream). El clasificador de los tests anteriores era flojo (comparaba contra un tamano fijo de placeholder y el placeholder cambia de tamano segun el texto) -> daba "207/207 OK". Con deteccion por **hash del placeholder real de cada canal**:
| Lista | logos | wsrv OK | placeholder | hosts que fallan |
|---|---|---|---|---|
| pe | 207 | 38 | 169 | **imgur 166**, rudo.video 2, chikitoonz 1 |
| ar | 207 | 70 | 137 | imgur 132, i0.wp 2 |
| mx | 173 | 20 | 153 | imgur 153 |
| co | 148 | 36 | 112 | imgur 112 |
| cl | 247 | 61 | 186 | imgur 179, postimg 3, paste.pics 2 |
| es | 323 | 107 | 216 | imgur 214, wikia 2 |
- En **todas** las listas imgur es el **70% de los logos (3074 de 4348)** -> afecta sobre todo a Peru (166/207 = 80%).
- wsrv SI sirve (comprobado): `upload/thumb.wikimedia.org`, `images.pluto.tv`, `i.ibb.co`, `cdn.m3u.cl`, `imgx.fubo.tv`, `provider-static.plex.tv`, `gia.tv`, `yt3.googleusercontent.com`, `iili.io`, `m.media-amazon.com`, `encrypted-tbn0.gstatic.com`.
- wsrv NO sirve: `i.imgur.com`, `i.postimg.cc`, `static.wikia.nocookie.net` (directo tb 403), `i2.paste.pics` (SSL roto), `i0.wp.com`/`assets.rudo.video`/`chikitoonz.com` (muertos en origen, ahi el placeholder es correcto).
- **Directo imgur funciona**: 206 sin headers (ojo: **sin User-Agent imgur da 429**). Proxies alternativos descartados para imgur: allorigins/codetabs 522, corsproxy.io 401, images.weserv.nl 404.
**Fix**: nueva `directPoster(l)` en `posterFor()`: los logos `i.imgur.com` se emiten **directos con la variante `m` de 320px** (`https://i.imgur.com/{id}m.{ext}`) y `i.postimg.cc` directo; el resto sigue por wsrv (mantiene rasterizado de SVG + placeholder para hosts muertos). La variante `m` se verifico **300/300 OK** (mediana **10 KB**, max 35 KB, vs originales de hasta 1.8 MB).
- Versiones a **5** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0). v3+v4+v5 salen en el mismo build. Balance `bal_check.py` OK.
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y comprobar que ATV Peru / Global TV / Ucayalina muestran poster (y que el detalle ya no sale negro, fix v4).

---

## TVLibre.st - revision de factibilidad (17 Oct 2026) - NO se implementa (decision del usuario: "no hacer nada por ahora")

**Pregunta del usuario**: evaluar `https://tvlibre.st/` para un plugin nuevo.
**Veredicto**: es **gemelo de Telelibre** (mismo operador y backend: assets `bestleague.pl`, players `bestleague.life`, CDN `cvattv.com.ar`, mismo `tok.html`). Es *escribible* pero **no reproduce sin IP de Argentina/Uruguay/Paraguay** -> mismo geo-bloqueo ya documentado para Telelibre v3/v4. Descartado.

### Catalogo (trivial, verificado en PC)
- Home `/` (108 KB) trae los **177 canales** completos: `a.canal[href=/en-vivo/{slug}]` con badge de pais/genero (`span.badge`), logo `//bestleague.pl/img/{nombre}.{png|webp}` (lazy) y titulo `h2.title`.
- Sin paginacion. Filtros (`#filters`) y buscador (`input#q` placeholder "Buscar canal o etiqueta..") son **client-side** -> se replicarian en Kotlin sobre la lista cacheada.
- Badges/categorias: Entretenimiento 44, Deportes 34, Argentina 34, Documentales 17, Espana 14, Paraguay 10, Infantiles 9, Mexico 9, Musica 7, Uruguay 7, Colombia 3, Adultos 3, EEUU 2, Gastronomia 2, Europa 1, Peru 1.

### Cadena de streams (misma que Telelibre)
- Detalle `/en-vivo/{slug}`: `iframe#iframe` + botones `.btn-md` (`Opcion 1 (FL)`, `Opcion PC`, y sub-senales).
- **Opcion 1 (FL)**: `/html/fl/?get={b64}` -> JS `GET /html/cv.json` -> `{"urls":["bestleague.life"]}` -> iframe `//bestleague.life/cvatt.html?get={b64}&lang=1` -> **DASH ClearKey**: `var mpd = "//"+mt[random]+".cvattv.com.ar/live/c"+number+"eds/"+atob(getURL)+"/SA_Live_dash_enc_C/"+atob(getURL)+".mpd"` con `mt=["cdn","chromecast"]`, `number` 3 o 4 segun canal, y `keyId`/`key` **hardcodeados por canal** (hex) en el propio HTML.
- **Opcion PC**: `bestleague.life/tok.html?get={b64}` -> lista de `token: "tok_eyJ..."` (JWT con `sip` atado a la IP origen y `path` `/live/cXeds/{canal}/SA_Live_dash_enc_/`) sobre `edge-live01-cen.cvattv.com.ar`; el HTML exige PC ("Dispositivo no compatible").
- **Opcion 2 (Telefe)**: `thl.html` -> `GET tlf.php` -> `{url: ...akamaized.net/.../master.m3u8?hdnea=...}` - **token expirado 21-jun-2026** (Akamai 400). `tlf.php` devuelve SIEMPRE Telefe (hardcodeado, no varia por Referer).

### Bloqueador medido (igual que Telelibre)
- `cdn.cvattv.com.ar` (201.235.66.115) y `chromecast.cvattv.com.ar` (181.12.36.150): resuelven DNS pero **TCP 443 y 80 = False** desde PC.
- El propio sitio lo dice: *"Las opciones (FL) estan disponibles en Argentina, Uruguay y Paraguay. Fuera de esos paises necesitas usar una VPN."*
- `bestleague.life` y `bestleague.pl` (:443) SI responden (el bloqueo es solo del CDN de video).

### Como se implementaria (si se retoma)
- Modulo nuevo reutilizando la logica ClearKey/`cvattv` de `TelelibreProvider` (`handleTokHtml`/`hexToB64Url`), o anadirlo como segunda fuente dentro de TelelibreProvider.
- Catalogo: parsear el home una vez y filtrar en memoria (search/filtros client-side).
- Requisito: VPN/red en AR-UY-PY (mismo caveat que Telelibre).
- **No se implemento por decision del usuario (17 Oct 2026).**
---

## IptvProvider - Plugin nuevo: listas IPTV (M3U) + cuentas Xtream Codes (18 Oct 2026 v1)

Origen: plugin **IPTV** de doGior (`github.com/doGior/doGiorsHadEnough`), portado al repo y **extendido** para aceptar cuentas **Xtream Codes**.

### Que hace
- Reproduce **listas IPTV** (.m3u/.m3u8) y **cuentas Xtream Codes**.
- Un `MainAPI` por lista (`IptvProvider`) registrado dinamicamente desde `IptvPlugin.reload()`.
- `getMainPage`: parsea la lista y agrupa los canales por `group-title` (una fila por grupo). `search`/`quickSearch` filtran por titulo. `load` = canal + recomendaciones del mismo grupo. `loadLinks` emite el link del canal.
- El reproductor usa el tipo por extension: los canales en vivo de Xtream (`.ts`) salen como VIDEO y los HLS (`.m3u8`) como M3U8.

### Dialogo en dos pasos: primero el tipo, luego el dato
- Al pulsar "Add link" se abre un selector **"Tipo de entrada"** con 2 opciones: **URL M3U** y **Cuenta Xtream**.
- Segun la eleccion, el dialogo de alta cambia titulo, etiqueta del campo y validacion (`looksLike(raw, type)`):

| Tipo | Etiqueta del campo | Validacion | Normalizacion |
|---|---|---|---|
| **URL M3U** (`TYPE_M3U`) | "URL M3U" | empieza con `http` o contiene `.m3u` | `normalizeM3uLink()` (sin esquema -> antepone `http://`) |
| **Cuenta Xtream** (`TYPE_XTREAM`) | "Cuenta Xtream (host\|usuario\|clave)" | 3 partes separadas por `\|`, o contiene `get.php` / `player_api.php` | `normalizeXtreamLink()` |

- `IptvLink` tiene un 3er campo `type` (`"m3u"` / `"xtream"`, nullable); en la lista de guardados el nombre se muestra con su etiqueta `[M3U]` / `[Xtream]` / `[Auto]`.
- **Compatibilidad**: los guardados previos (sin `type`) siguen funcionando via `normalizeIptvLink()` (auto-deteccion: `\|`/`get.php`/`player_api.php` -> Xtream; si no -> M3U).

### Formatos aceptados (IptvUtil.kt)
| Entrada | Resultado |
|---|---|
| `http(s)://host/lista.m3u` (tipo M3U) | se usa tal cual |
| `host/lista.m3u` sin esquema (tipo M3U) | antepone `http://` |
| `host\|usuario\|clave` o `host:puerto\|usuario\|clave` (tipo Xtream) | `http(s)://host/get.php?username=..&password=..&type=m3u` |
| `http(s)://host/player_api.php?username=..&password=..` (tipo Xtream) | mismo `get.php` (extrae credenciales y base) |
| `http(s)://host/get.php?...&type=m3u_plus` (tipo Xtream) | fuerza `type=m3u` (solo TV en vivo) |
| URL/incompleto (cualquier tipo) | `normalizeXtreamLink()` antepone `http://` si falta esquema |
- El dialogo guarda en la clave `iptv_links` (`Array<IptvLink>`), mismo formato base que el plugin original -> **no migra** los datos de una instalacion previa del IPTV de doGior (otro plugin id).

### Cambios respecto al original de doGior
1. **Xtream**: `normalizeXtreamLink()` + `normalizeM3uLink()` (nuevo `IptvUtil.kt`); dialogo en **dos pasos** (elige tipo -> pide el dato) con campo `host|usuario|clave` o URL del panel.
2. **Tipo de link por extension** en `loadLinks`: `.mpd` -> `newDrmExtractorLink` (ClearKey con `key`/`keyid` del `#EXTINF`); `.m3u8`/`.m3u` -> `M3U8`; `.ts`/`.mp4`/`.mkv`/`.avi`/`.flv`/`.webm` -> `VIDEO`; resto -> `M3U8`. (Antes forzaba `M3U8` siempre: los VOD `.ts`/`.mp4` de Xtream fallaban.)
3. **`loadLinks` null-safe**: si `items[name]` es null re-descarga la lista, y usa `firstOrNull { it.url == .. }` con fallback (antes `items[name]!!.items.first{}` -> NPE al volver de fondo / saltar de episodio).
4. **`reload()` idempotente** (`IptvPlugin.unregisterExisting()`): quita los `IptvProvider` previos de `APIHolder.allProviders` + `removePluginMapping` antes de re-registrar. Evita duplicados al re-guardar y aplica los **borrados** de la lista. Se elimino el uso de `PluginManager.getPluginsOnline()/unloadPlugin` del original (podia descargar plugins ajenos: matcheaba `internalName.contains(link.name)`).
5. `getKey`/`setKey` via `CloudStreamApp.Companion` (el original usaba el mismo; `AcraApplication.Companion` esta **deprecado** en el jar actual y rompe la compilacion con `DeprecationLevel.ERROR`).
6. Sin `getVideoInterceptor` (el original devolvia un interceptor no-op).
7. Clases en `com.example`: `IptvPlugin`, `IptvProvider`, `IptvParser` (`IptvPlaylistParser` + `IptvLink`/`Playlist`/`PlaylistItem`/`LoadData`), `IptvSettingsFragment`. Se renombro `Link` -> `IptvLink` por posible choque con el wildcard de `com.lagradost.cloudstream3.*`.
8. Recursos copiados **verbatim** (4 layouts + 5 drawables) con `requiresResources = true`; se anadio `material` 1.13.0 (BottomSheetDialogFragment) y `viewBinding`.

### Archivos
- `IptvProvider/build.gradle.kts` (version 1, `tvTypes = ["Live"]`, `language = "un"`, `requiresResources = true`)
- `IptvProvider/src/main/AndroidManifest.xml`
- `IptvProvider/src/main/kotlin/com/example/{IptvPlugin,IptvProvider,IptvParser,IptvUtil,IptvSettingsFragment}.kt`
- `IptvProvider/src/main/res/layout/{add_link,list_link,manager,settings}.xml`
- `IptvProvider/src/main/res/drawable/{delete_icon,edit_icon,outline,settings_icon,telegram}.xml`

### Estado v1
- `settings.gradle.kts` auto-incluye el modulo (escanea dirs con `build.gradle.kts`) - sin registro manual.
- `plugins.json`: entrada `IPTV`/`IptvProvider` v1 (`fileSize: 0` pendiente; 72 entradas).
- Balance `bal_check.py` -> BALANCE OK en los 5 `.kt`.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :IptvProvider:make --console=plain -q`) - **no compilado por regla del repo**.
- Pendiente: compilar, actualizar `fileSize`, instalar y probar: (1) el selector "Tipo de entrada" (M3U vs Xtream) al pulsar Add link; (2) una lista M3U; (3) una cuenta Xtream `host|usuario|clave`; (4) que los canales `.ts` de Xtream reproduzcan (VIDEO); (5) que borrar una lista la quite sin reiniciar (idempotencia).

### Notas tecnicas (verificado por bytecode en el jar de este repo)
- `newDrmExtractorLink` tiene overload para `java.util.UUID` (`CLEARKEY_UUID`) y `kotlin.uuid.Uuid` (`CLEARKEY_DRM_UUID`); se usa `CLEARKEY_UUID` (igual que el original de doGior).
- `AtomicMutableList.remove(Object)`/`removeAll(Collection)` SI estan implementados (no lanzan `UnsupportedOperationException`); `AtomicList.filter()` devuelve una **copia** -> seguro iterar mientras se elimina.
- `APIHolder` expone `allProviders` (`AtomicMutableList`), `apis`, `getApiFromNameNull`, `addPluginMapping`, `removePluginMapping`.
- `registerMainAPI` vive en `BasePlugin`; `MainActivity.afterPluginsLoadedEvent` es accesible y refresca la lista de providers.
- `AppUtils.parseJson`/`toJson` se usan en ~29 modulos del repo -> seguros en este plugin (el transporte del `LoadData` va como JSON en la URL del canal).

### Causa raiz: home/search vacios tras agregar una lista (19 Oct 2026, v1 sin compilar)
**Sintoma (usuario)**: tras guardar una URL M3U no aparece el getMainPage ni el search.
**Causa raiz (verificado por bytecode en el jar del repo)**: `BasePlugin.registerMainAPI(MainAPI)` solo hace `APIHolder.allProviders.add(element)` + `addPluginMapping(element)`; **NO** toca `APIHolder.apis`. `apis` (lo que consumen `HomeViewModel` linea ~312 y `SearchViewModel`) se re-construye con `setApis(...)` en solo 2 sitios, ambos de arranque: `MainActivity$onCreate$19` y `MainActivity$onAllPluginsLoaded$1` (este ultimo: `getAllProviders().withLock { ... setApis(distinctBy { lang+name+mainUrl+clase }) ; setApiMap(null) }`). El original de doGior confiaba solo en `MainActivity.afterPluginsLoadedEvent.invoke(true)`.
**Fix**: ademas del evento, `IptvPlugin.refreshApi(provider)` hace **append** de cada provider a `APIHolder.apis` (`apis = apis.plus(provider)`; no toca `apiMap` -> los indices existentes no cambian y `getApiFromNameNull` resuelve el nuevo por el fallback de `allProviders`). Idempotente (`none { it === provider || it.name == provider.name }`).
**`lang`**: `IptvProvider.lang` estaba en `"mx"`; ahora `"un"` (igual que el original de doGior y que `language` de `plugins.json`).
**Logs nuevos** (TAG `IPTV`, `adb logcat -s IPTV:V`): `reload: guardados=N`, `registrando '<nombre>' type=.. url=..`, `refreshApi: .. agregado a apis`, `reload: apis=N providers=N`, `unregisterExisting: quitados=N`; en el provider: `ensureLoaded: fetch <url>` -> `ensureLoaded: len=.. canales=..` (o `ensureLoaded fallo`), `getMainPage: canales=.. grupos=..`, `search: '<q>' resultados=..`, `load: .. recomendaciones=..`, `loadLinks: .. item encontrado=.. headers=..` + tipo emitido; en el fragmento: `guardar '..' type=.. raw=.. -> normalized`, `borrado .. restantes=..`, `listar links: N`. Los `catch` de `IptvPlugin` ya no se tragan la excepcion (antes `catch (e: Exception) {}` sin log).
**`newExtractorLink`**: `source` ahora es `this.name` (nombre de la lista) -> regla del repo para que `getApiFromNameNull(link.source)` resuelva el provider; el titulo del canal sigue en `name` (display).
**`ensureLoaded`**: `app.get(mainUrl, headers, timeout = 40L)` (timeout en SEGUNDOS, regla 03 Oct 2026; antes sin timeout -> el fetch podia colgarse hasta el kill de 120s de CS3).
- Balance: BALANCE OK en los 5 `.kt`.
- â¸ï¸ Pendiente: compilar, `fileSize`, instalar y con `adb logcat -s IPTV:V` confirmar si el provider entra a `apis` (`refreshApi: .. agregado a apis`) y que devuelve `ensureLoaded`.
### Fix: get.php devuelve 47 bytes -> camino API JSON de Xtream (19 Oct 2026 v1)
**Logs del usuario (xdplus.site:2082, panel Xtream)**:
```
unregisterExisting: quitados=0 / reload: guardados=1 / registrando 'tv' type=m3u url=...get.php?...type=m3u
refreshApi: tv ya estaba en apis / reload: apis=1 providers=1     <- registro OK (el evento ya poblaba apis)
borrado 'tv' restantes=0 -> unregisterExisting: quitados=1 -> reload: apis=0 providers=0   <- borrado OK
getMainPage page=1 name=tv / ensureLoaded: fetch ...&type=m3u_plus
ensureLoaded: len=47 canales=0        <- el get.php NO devuelve la lista M3U (47 bytes)
getMainPage: canales=0 grupos=0 / search: 'studio' resultados=0
```
Conclusion: el registro (`refreshApi` + `afterPluginsLoadedEvent`) y el borrado funcionan; el problema es que ese panel no entrega M3U por `get.php` (devuelve 47 bytes, 0 canales). Nota: `apis=1` con `refreshApi: ya estaba en apis` demuestra que el `invoke` del evento ya re-construye `apis` (el append solo evita duplicados).
**Fix**: camino **API JSON de Xtream** antes del M3U (mismo mecanismo que usa la app original de Xtream):
- `parseXtreamCreds(url)` en `IptvUtil.kt` (publico): solo si la URL contiene `get.php`/`player_api.php` y trae `username`+`password` -> `XtreamCreds(base, user, pass)` (base = origen, reutiliza `baseUrl`/`queryParam`).
- `loadXtreamApi(creds)` en el provider: `GET {base}/player_api.php?username=..&password=..&action=get_live_categories` (mapa `category_id`->`category_name`) + `&action=get_live_streams` (timeout 90s) -> por stream: titulo=`name`, grupo=`category_name`, poster=`stream_icon`, url=`{base}/live/{user}/{pass}/{stream_id}.{extension|ts}`. Si da canales, se usa eso y no se toca el M3U.
- Fallback intacto: si no hay creds o la API da 0 -> M3U por `get.php` como antes.
**Logs nuevos**: `ensureLoaded: fetch <url> xtream=<bool>`, `xtream categorias=N nombres=M`, `xtream API: canales=N categorias=M`, `xtream categorias/streams no JSON: <300 chars>`, `xtream API fallo`, y en la rama M3U `ensureLoaded: 0 canales code=.. len=.. body=<300 chars>` / `ensureLoaded: M3U invalido code=.. len=.. body=..` (antes no se veia el cuerpo, por eso los 47 bytes eran invisibles).
- Balance: BALANCE OK en los 5 `.kt`.
- â¸ï¸ Pendiente: compilar, `fileSize`, instalar y en `adb logcat -s IPTV:V` buscar `xtream API: canales=N` (si sale >0, la lista anda); si sale `xtream streams no JSON` el cuerpo dira si el panel rechaza credenciales/UA.
- Versiones a **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0).

### Cuenta Xtream con campos separados (19 Oct 2026 v2)
**Pedido del usuario**: en vez de una sola caja `host|usuario|clave`, cajas independientes (nombre, host, usuario, clave).
**Fix**: `add_link.xml` -> se conservan `name` + `link_label`/`link` (solo M3U) y se aÃ±ade el contenedor `xtream_fields` (visibilidad `gone`) con labels "Host"/"Usuario"/"Clave" y los EditText `host` (inputType `textUri`), `user`, `pass`. `IptvSettingsFragment.showAddLinkDialog` oculta/muestra segÃºn el tipo; `xtreamRaw()` arma `host|usuario|clave` (si el host viene como `host|user|pass` o como URL de panel, `parseXtreamCreds()` extrae base/user/pass); `fillXtreamFields()` prellena las 3 cajas desde el portapapeles; `saveLink()` unifica el guardado (evita duplicados por nombre, log `guardar ...`). El tÃ­tulo "Nombre" del layout se corrigiÃ³ de "Name" a "Nombre".
- Versiones a **2** (mismo build que el fix de la API JSON).
### Veredicto: las 2 cuentas son rechazadas por el panel (19 Oct 2026, sin cambio funcional)
**Logs del usuario (v2, ambos paneles)**: `player_api.php` -> `{"error":"INVALID_AUTH","message":"Invalid Authorization or URL / 404 Error.","status":401}` y `get.php` -> `code=512` + `{"login":false,"message":"invalid user, pass."}`, en las DOS cuentas (`livetv-plus.com:8880` Bluefox180/ENTWJ y `xdplus.site:2082` AndresLLpro/TqhsHqdxsT).
**Verificado desde la PC (curl, UA `VLC/3.0.18` y `okhttp/4.12.0`, otra IP)**: respuestas IDENTICAS (91 y 47 bytes) -> **el rechazo es del panel, no del plugin**; el formato de peticion es el correcto (el endpoint existe y contesta JSON de auth). Extra: ambos dominios resuelven a la MISMA IP (`149.51.49.111`) y su raiz es un `200 - Everything running normal...` (proxy/health) -> mismas reglas de auth. HTTPS en esos puertos no negocia (schannel SEC_E_INVALID_TOKEN) -> el panel solo habla HTTP.
**Conclusion**: credenciales/usuario-clave invalidos, vencidos o de otro servidor. Que verificar en el navegador/app del proveedor: `http://<host>:<puerto>/get.php?username=<u>&password=<p>&type=m3u_plus` (si tambien falla ahi, la cuenta no sirve). No hay nada que corregir en el codigo.
**Mejora aÃ±adida (v3)**: `isAuthError()` detecta `INVALID_AUTH` / `"login":false` / `invalid user` / `invalid authorization` -> `loadXtreamApi` corta tras el primer request (no gasta `get_live_streams`) y `ensureLoaded` loguea `xtream AUTH rechazada por el panel: <body>` / `get.php AUTH rechazada por el panel: <body>` (mensaje claro en vez de "no JSON"/"0 canales").

### Cerrado: veredicto de la lista de tecnotv.club + v3 (19 Oct 2026) - sin cambio funcional
**Sintoma (usuario)**: al reproducir canales de la lista `https://tecnotv.club/xz3c/lista.m3u` (946 canales) CloudStream da `ERROR_CODE_IO_UNSPECIFIED (2001)`; el log muestra `loadLinks ... item encontrado=.. headers=[]` y la emision OK (el plugin emite bien).
**Estructura de la lista (medida en PC)**: 946 URLs; 150 pasan por el wrapper propio `http://tecnotv.club/xz3c/phpcode/ssiptv.php?c=<id>&token=tecnotokenplustv230516F&f=.m3u8`; el resto (~790) son CDNs publicos directos.
**Causa raiz (verificada)**: TODOS los canales del wrapper redirigen al MISMO `http://200.35.176.154:8000/play/<id>/index.m3u8` (302 estable, igual con Referer del sitio, UA Android o `f=.ts`). Ese host: ping OK, **solo el puerto 80 abierto** (da 404); TODOS los puertos de streaming filtrados desde la red del usuario (`8000/8080/8001/8081/8880/1935/554/443` -> `TcpTestSucceeded=False`, `curl (7) Could not connect`) -> 2001 garantizado. No es codigo.
**Calidad de los "directos"**: muestra de 35 hosts distintos -> **21 responden 200 + `application/x-mpegURL`** y 14 fallan (403 geo: `mdstrm.com`, `origin-us.streaminghd.cl`; 404 muertos; `000` sin conexion). Ademas la lista trae URLs malformadas en origen (`...mediatiquestream.com&token=...` con el token pegado al host, `https://v2.tustreaming.cl:/...`, `https://m3u.cl/channel/c13.php`).
**Canales verificados 200 (para probar el plugin)**: `13 Cultura` (`redirector.dps.live/hls/13cultura/playlist.m3u8` -> `dai.google.com`), `RTQ Queretaro`, `8 NTV`, `Canal 10 Chiapas`, `Mexiquense TV`, `AMX MEXICO`, `Kanade TV`, `EnerGeek Retro`, `70-80 TV (1080P)`, `Solobailalo TV`, `30A Music Channel`, `13 Festival (1080p)`, `Stingray Classic Rock`, `Flash - Freetv.com`, `MMC - Freetv.com`, `Retro Music Television`, `ESTRELLA TV`, `Cali TV`, `Canal VM Latino`, `TV4 MEXICO`, `TecnoRadio`.
**Conclusion / decision del usuario**: el plugin esta bien; la lista es de baja calidad y los canales "propios" estan geo/firewall-restringidos (VPN con IP chilena seria el unico test posible). **Caso cerrado en IptvProvider.**
**Cierre administrativo (v3)**: `isAuthError()` (que habia quedado sin publicar) sube a `version = 3` en `build.gradle.kts` + `plugins.json`; `plugins.json.language` corregido de `"mx"` a `"un"` (coincide con el provider y con una lista internacional). `fileSize` sigue 0 -> actualizar tras compilar.
- â¸ï¸ Pendiente: compilar (`.\gradlew.bat :IptvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar `13 Cultura` en el plugin.
- Notas: en VLC los en vivo muestran barra/duracion (ventana deslizante HLS; algunas entradas son VOD/archivo, no live puro). CloudStream no guarda progreso de `TvType.Live` -> no aparecen en "Continuar viendo" (usar favoritos). Trampa interna: en PowerShell `$Host` es de solo lectura -> usar otra variable al parsear listas.

---

## PlushdProvider (PlusHD) - Causa raiz "enlaces no encontrados": emision desacoplada (19 Oct 2026 v22)

**Sintoma (usuario)**: `loadLinks` loguea `=== loadLinks FIN: hasValidServer=true linksFound=3 ===` y las 3 variantes VidHide se emiten con probe OK, pero el reproductor muestra **"enlaces no encontrados"**.

**Causa raiz (codigo)**: `wrappedCallback` (`PlushdProvider.kt:325`) era el UNICO punto donde un link se entrega al callback real de CloudStream, y lo hacia dentro de `CoroutineScope(Dispatchers.IO).launch { callback(...) }`. O sea `loadLinks` **retornaba ANTES** de entregar ningun link (el conteo propio `foundLinks=3` ya lo habia incrementado `wrappedCallback2`), y las emisiones aterrizaban en hilos ajenos al scope de `loadLinks` (el log mostraba 2 hilos `Dispatchers.IO` distintos). Es el mismo anti-patron ya documentado aqui para SeriesKao (v28: "trabajo que sobrevive a la cancelacion"), y coincide con el bug historico ya anotado de PlusHD ("primer play -> Enlaces no encontrados, segundo play -> carga", origen de la 2a pasada).

**Evidencia bytecode (jar `~\.gradle\caches\cloudstream\cloudstream\cloudstream.jar`)**:
- El caller del provider en el player es `com.lagradost.cloudstream3.ui.player.RepoLinkGenerator.generateLinks` (no `ExtractorLinkGenerator`, que es solo la ruta de descarga). Offsets 695-727: `APIHolder.getApiFromNameNull(episode.apiName)` -> si null lanza `Exception("This provider does not exist")`; crea `APIRepository(api)` y llama `APIRepository.loadLinks(data, isCasting, subCb, linkCb, cont)` (offset 862).
- **Justo despues del retorno** (offsets 974-1050): `booleanValue()` -> `Cache.getLinkCache().isEmpty()` -> `Cache.setSaturated(...)` -> `Cache.setLastCachedTimestamp(getUnixTime())`. El cache de links se marca vacio+saturado con timestamp previo a nuestra emision tardia.
- El callback de link agrega al `Cache.getLinkCache()` (`ConcurrentHashMap.newKeySet`) y luego invoca el callback del player (offsets 456-484); `PlayerGeneratorViewModel` agrega al `VideoLive` via `modifyState` guardado por `CoroutineScopeKt.isActive(coroutineScope)`.
- `APIRepository.loadLinks` (bytecode): `if (Companion.isInvalidData(data)) return false;` + `withTimeout(getTimeout(api.getLoadLinksTimeoutMs()))` envolviendo `loadLinks$2`, que llama **directo** `MainAPI.loadLinks(data, isCasting, subtitleCallback, callback, cont)` -> **no hay filtro por `source`** en el lado app. Descartado: `APIRepository$extractorVerifierJob$1/$2` -> `MainAPI.extractorVerifierJob` es un hook del provider, no filtra links.

**Fix (`PlushdProvider.kt:325-352`, drains en 462/471)**:
- **Nota de compilacion**: `newExtractorLink(...)` es **suspend** -> NO se puede emitir desde el lambda `(ExtractorLink) -> Unit` (`Suspension functions can only be called within coroutine body`). Era la razon real del `CoroutineScope(Dispatchers.IO).launch` del autor original.
- `wrappedCallback` eliminado: `wrappedCallback2` ahora solo **encola** (`pendingLinks`, `ConcurrentLinkedQueue<ExtractorLink>`) e incrementa `foundLinks`; la emision real la hace `suspend fun emitPendingLinks()` (builder con `extraHeaders` + `Log.d("callback -> ...")` + `newExtractorLink`) desde el body de `loadLinks`.
- `emitPendingLinks()` se llama **dentro del scope de `loadLinks`**, justo despues de cada `coroutineScope { ... }` de las 2 pasadas (462 y 471) -> los links llegan a CloudStream **antes** de que `loadLinks` retorne, cortando la carrera del `Cache.setSaturated` + `lastCachedTimestamp` descrita arriba.
- `newExtractorLink(name, ...)` en vez de `link.source` -> fuerza `source = "PlusHD"` (regla del repo para `getApiFromNameNull`/interceptor; importa en la rama `loadExtractor`, donde el source interno podia ser `VidHide`/`Vudeo`/etc).
- Imports `CoroutineScope`/`Dispatchers` eliminados (solo los usaba esa linea; `coroutineScope`/`launch` siguen en uso en las 2 pasadas).

**Estado / versiones**: `PlushdProvider/build.gradle.kts` **21 -> 22**; `plugins.json` PlusHD **version 3 -> 22** (habia **DESYNC**: build.gradle ya iba en 21 mientras plugins.json seguia en 3 -> la app no ofrecia actualizacion; mismo patron que SoloLatino/SeriesKao/Pandrama/Telelibre/Tvenvivo). `fileSize` sigue 44712 (actualizar tras compilar). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar un episodio/pelicula buscando `callback -> VidHidePro - hlsX (source=PlusHD)` en logcat; debe reproducir sin "enlaces no encontrados". Si persistiera, la 2a hipotesis sigue viva (throttle por IP del CDN vidhide, veredicto v4.2).

### ðŸŽ¯ Causa raiz del "otro enlace que si reproduce": server "Plus" (emturbovid) descartado (19 Oct 2026 v23)
**Reporte (usuario)**: Goblin EP1 -> "enlaces no encontrados"; "el vidhide en la web no reproduce y no hay, hay otro enlace que si reproduce pero el plugin no lo detecta".
**Servidores del episodio** (`https://tioplus.app/serie/goblin/season/1/episode/1`, HTML real): Earnvids, UPFAST, RPM, P2P, MobileFast, Plus, Netu, LuLu. Cada `li[data-server]` trae **base64-de-base64**; el provider decodifica una vez -> no matchea `REGEX_LINK` -> arma `$mainUrl/player/{b64}` (pagina stub `PLAYERHD` con `window.location.href=...` a los 250ms).
**Causa raiz (2 lineas que se anulan entre si)**:
- `fixPelisplusHostsLinks` (linea 214) reescribia `https://emturbovid.com` -> `https://turbovidhls.com`.
- `processServer` (linea 407) hacia `if (fixedLink.contains("turbovidhls.com")) { Log.w("turbovid (error 3003), saltando"); return }`.
- Resultado: el server "Plus" (emturbovid) **nunca** llegaba a `loadExtractor`.
**Verificado por bytecode (`javap`) en el jar del repo**: existe `com.lagradost.cloudstream3.extractors.EmturbovidExtractor`, `name="Emturbovid"`, `mainUrl="https://emturbovid.com"`; su `getUrl` hace GET a la pagina, `select("script")` cuyo `data(...)` contiene `var urlPlay`, extrae el literal entre `var urlPlay = '` y `'` y emite el `ExtractorLink` con `referer = mainUrl` (lambda `getUrl$2`). `loadExtractor` matchea por **prefijo de host** -> hay que pasarle la forma `emturbovid.com` (la forma `turbovidhls.com` no matchea ningun extractor: es el origen del viejo error 3003).
**Fix**:
1. Eliminada la reescritura `emturbovid.com -> turbovidhls.com` de `fixPelisplusHostsLinks`.
2. El descarte se sustituye por una rama: `if (fixedLink.contains("turbovid"))` -> convierte `https://turbovidhls.com` -> `https://emturbovid.com` (no-op si ya viene asi) y llama `loadExtractor(...)` con `wrappedCallback2`; log nuevo `turbovid -> EmturbovidExtractor: <url>`. El skip de SPA (`#` + upns/rpmstream/strp2p/4meplayer/pelisplusto) queda intacto y sigue ANTES de `hasValidServer = true`.
3. La emision sigue pasando por `emitPendingLinks()` -> `source = "PlusHD"` (interceptor OK).
**Notas de red**: `emturbovid.com` **no conecta desde la PC del usuario** (timeout TCP, igual que `turbovidhls.com`) -> el extractor solo se valida en el dispositivo; `luluvdo.com` (extractor `LuluStream` en el jar) da 0 links, y `vudeo.co`/`waaw.to` **no tienen extractor** en el jar (por eso logueaban `OK` sin links).
**Estado / versiones**: `PlushdProvider/build.gradle.kts` **22 -> 23**; `plugins.json` PlusHD **version 22 -> 23** (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar Goblin EP1 buscando `turbovid -> EmturbovidExtractor` y `callback -> ... (source=PlusHD)`. Si sigue sin links, los candidatos restantes son los 3 SPA de pelisplus (UPFAST/RPM/P2P: requieren JS) y LuLu/MobileFast (hosts sin extractor o extractor devolviendo 0).

### ðŸ”§ Fix LuLu (luluvdo) con desempaquetado propio (19 Oct 2026 v24)
**Reporte (usuario)**: en Goblin EP1 "hay otro enlace que si reproduce pero el plugin no lo detecta" (ademas del "Plus" ya arreglado en v23). Los candidatos que quedaban eran LuLu (`luluvdo.com/e/{code}`, extractor `LuluStream` en el jar) y MobileFast (`vudeo.co`).
**Verificado en PC (conexion real, no supuestos)**:
- **LuLu VIVO**: `https://luluvdo.com/e/wo70vtmvtau9` GET -> JS **empaquetado Dean Edwards** (`}('...',36,303,'...'.split('|')`) -> al desempaquetar -> `sources:[{file:"https://i2gcnsz23x2n.tnmr.org/hls2/01/02389/,wo70vtmvtau9_h,lang/spa/.../master.m3u8?t=..."}]`. `POST /dl` (`op=embed&file_code=...&auto=1`) devuelve la misma pagina empaquetada. Mismo host sirve `luluvdoo.com`/`lulustream.com`.
- **CDN `tnmr.org` exige UA de navegador**: master m3u8 -> **200 con UA Chrome**, **403 con `ExoPlayerLib` o sin UA** (Referer/Origin irrelevantes). La URL contiene `.urlset/` -> ya pasa el gate `isPlaylist` del interceptor, que **inyecta el UA de Chrome** (confirmado en `getVideoInterceptor`).
**Causa raiz (bytecode del jar, `javap` de `extractors/helper/JwPlayerHelper`)**: el `LuluStream` de CloudStream hace `POST mainUrl/dl` y entrega el script a `JwPlayerHelper.extractStreamLinks`, que solo regexea `"?sources"?:\s*(\[.*?\])` y `[:=]\s*"([^"\s]+(\.m3u8|master\.txt)[^"\s]*)`. **No tiene NINGUN desempaquetador Dean Edwards** (`split('|')`/`p,a,c,k,e,d` ausentes en su bytecode) -> sobre la pagina empaquetada devuelve **0 links**. Diagnostico definitivo.
**Fix (`PlushdProvider.kt`)**:
1. Nueva rama en `processServer`: `else if (fixedLink.contains("lulu"))` -> `tryLuluExtraction(...)` y si falla `loadExtractor`.
2. `tryLuluExtraction(url, referer, callback)`: GET con UA Chrome + `Referer: <url>` -> `unpackLuluPacked(html)` -> emite `file|src|source:` (`.m3u8|.mp4|.txt`) como `LuluStream` (`source = name` = "PlusHD" -> interceptor; `referer = "https://luluvdo.com/"`), con fallback a m3u8/mp4 suelto por scan.
3. `unpackLuluPacked(html)`: port del `unpackDeanEdwards` de `MonoschinosProvider/MonosExtractors.kt` (dict `split("|")`, base-N, `for (idx in count-1 downTo 0)`, `Regex("\\b${Regex.escape(key)}\\b")`, `replace("\\'","'")`), regex packer `\}\('(.*?)',(\d+),(\d+),'(.*?)'\.split\('\|'\)` con `DOT_MATCHES_ALL` (sin `""""`, evita el falso error de ICU-Android).
**Descartados (verificado)**: `vudeo.co` (MobileFast) ya **no es un player**: la pagina devuelve el dominio en venta (GoDaddy for-sale + red de ads `northwavepoint.com`), sin packer ni m3u8. `waaw.to` (Netu) es un player JS con API propia (`cdn-*.cfglobalcdn.com/flv/api/...` + `/e/`), sin extractor en el jar y no portable trivialmente.
**Estado / versiones**: `PlushdProvider/build.gradle.kts` **23 -> 24**; `plugins.json` PlusHD **version 23 -> 24** (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar Goblin EP1 -> en logcat buscar `URL LuLu detectada` / `PlushdProvider-Lulu: decoded len=` / `emit https://...tnmr.org/...m3u8` / `callback -> LuluStream (source=PlusHD)`. Debe aparecer el link `LuluStream` y reproducir. Quedan sin links los 3 SPA de pelisplus (UPFAST/RPM/P2P, requieren JS) y Netu.

### ðŸ”§ Fix master de 1 sola variante: el filtro 1080p vaciaba la playlist (19 Oct 2026 v25)
**Log del usuario (WSA, 21:15)**: la extraccion LuLu ya funciona completa (`decoded len=5187` -> `emit .../master.m3u8?t=...` -> `emitted=1` -> `callback -> LuluStream (source=PlusHD)` -> `loadLinks FIN linksFound=1`) pero al reproducir da **`ERROR_CODE_IO_BAD_HTTP_STATUS (2004)`**.
**Causa raiz (codigo + master real)**: el master de LuLu (`tnmr.org`) trae **UNA SOLA variante** (`#EXT-X-STREAM-INF:...,RESOLUTION=1920x1080,...` -> `.../wo70vtmvtau9_h/index-v1-a1.m3u8`) + una rendition `#EXT-X-MEDIA:TYPE=SUBTITLES`. El interceptor (`getVideoInterceptor`, filtro historico de "1080p" heredado del veredicto acek-cdn) **eliminaba la linea 1080p y la URL siguiente** -> master resultante **SIN variantes** -> ExoPlayer falla al reproducir. Con `vidhide hls2` no se notaba porque trae varias variantes (>1); LuLu tiene una sola.
**Fix (`PlushdProvider.kt:~259`)**: guarda `val streamInfCount = html.lines().count { it.contains("#EXT-X-STREAM-INF") }` y el filtro 1080p ahora exige **`streamInfCount > 1`** (nunca vacia la playlist). Cuando hay <=1 variante se loguea `M3U8 con N variante(s), sin filtrar (len=...)`. Ademas se aÃ±adio log de diagnostico por playlist en el interceptor: `intercept playlist <code> <url.take(120)>` (permite ver el codigo HTTP real del master/variante en el proximo test).
**Estado / versiones**: `PlushdProvider/build.gradle.kts` **24 -> 25**; `plugins.json` PlusHD **version 24 -> 25** (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar Goblin EP1 -> en logcat buscar `intercept playlist 200 ...master.m3u8` y `M3U8 con 1 variante(s), sin filtrar`; el link `LuluStream` debe reproducir. Si sigue `2004`, capturar el log **de reproduccion** (posterior a `loadLinks FIN`) para ver el `code` real que devuelve `tnmr.org` (el interceptor ya lo loguea).

### ðŸŽ¯ Causa raiz del 403 de LuLu: el CDN tnmr.org exige el UA exacto Chrome/127 (19 Oct 2026 v26)
**Log del usuario (WSA, 21:41)**: extraccion OK (`callback -> LuluStream (source=PlusHD)`, `linksFound=1`) pero al reproducir: `intercept playlist 403 https://i2gcnsz23x2n.tnmr.org/.../master.m3u8?t=...` (x3) + `M3U8 con 0 variante(s), sin filtrar (len=146)` -> `2004`.
**Diagnostico en PC (decisivo)**: el CDN `*.tnmr.org` acepta **solo** el UA exacto `Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36`, byte a byte. Barrido 110/115/120/122/124/125/126/**127**/128/130/135/140/144-151 -> **solo 127 = 200**; tambien 403: sin UA, `ExoPlayerLib`, Chrome/127 con plataforma Android, Mac/Linux, `Chrome/127` sin sufijo, Firefox 127, y **Chrome/120 (el que ponia `emitPendingLinks`)**. Aplica a **master, variante y segmentos**. Descartados: TLS/JA3 (`curl.exe` Schannel: 127 -> 200, 149 -> 403), HTTP/2 (ALPN negocia http/1.1), IP/sesion (mismo PC, token reutilizable), Referer/Origin/Accept/Accept-Encoding (irrelevantes con 127).
**Bytecode CS3 (`CS3IPlayer$Companion.createVideoSource`)**: el UA sale de `link.headers["User-Agent"]` (si falta -> Chrome/149) y se pasa a `OkHttpDataSource.Factory(client.newBuilder().addInterceptor(nuestro interceptor)).setUserAgent(ua)`.
**Fix (`PlushdProvider.kt`)**: constante `chrome127Ua` usada en (1) el interceptor, (2) `emitPendingLinks` (antes Chrome/120), (3) `tryLuluExtraction`. Red de seguridad: si el interceptor recibe **403** en host `tnmr`/`lulu`, reintenta con un cliente OkHttp propio sin cookies (`CookieJar.NO_COOKIES`, UA 127, `Range` reenviado) y devuelve esa respuesta; logs `403 CDN <url> wire-UA=... conCookie=...` y `reintento limpio -> <code> len=<n>`. El diagnostico del `wire-UA` dice si en el dispositivo el UA que llega al CDN es 127 o no.
**Estado / versiones**: `PlushdProvider/build.gradle.kts` **25 -> 26**; `plugins.json` PlusHD **version 25 -> 26** (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar Goblin EP1 **en el celular fisico** (no WSA) buscando `403 CDN ... wire-UA=` y `reintento limpio -> 200`: el link `LuluStream` debe reproducir. Si el `wire-UA` ya sale 127 y aun asi da 403, la regla del CDN sera por red/IP (probar otra red/VPN).


### Evidencia 22:13 (v26 en celular): el UA 127 SI llega al CDN y aun asi 403 (18 Oct 2026)
**Log (celular fisico, pid 2053)**: extraccion OK (`emitted=1`, `callback -> LuluStream (source=PlusHD)`, `loadLinks FIN linksFound=1`) y el interceptor registra `403 CDN https://i2gcnsz23x2n.tnmr.org/.../master.m3u8 wire-UA=Mozilla/5.0 (Windows NT 10.0; Win64; x64) ... Chrome/127.0.0.0 Safari/537.36 conCookie=false` + `reintento limpio -> 403 len=146` (x4). Es decir: el UA 127 **si** llega al CDN, sin cookies, y el reintento con cliente propio cookie-less **tambien** recibe 403 (mismo cuerpo de 146 B que da "sin UA" en PC).
**Lectura**: en el dispositivo el 403 **no** depende del UA (127 ya va puesto) ni de cookies. Desde PC (red del PC) el mismo builder con 127 devuelve 200. Sospechosos restantes: (a) **huella TLS de OkHttp/Cronet** frente a la de python/curl; (b) **red/IP del celular** (intercepcion del operador, patron ya visto en Teleonline/DoggiTV sobre puerto 80).
**Prueba pendiente (sin codigo, 2 min)**: (1) abrir `https://luluvdo.com/e/wo70vtmvtau9` en Chrome del celular EN LA MISMA RED y ver si reproduce; (2) instalar v26 en WSA y probar el mismo episodio. Si Chrome del celular reproduce o WSA da 200 -> el CDN si sirve a esa red y el fallo es del cliente; si ambos dan 403 -> bloqueo de red/IP.


### Fix v27: LuLu/tnmr sin interceptor -> Cronet (stack de Chrome) (19 Oct 2026 v27)
**Diagnostico (PC, medido)**: con token fresco, `requests`/OpenSSL, `curl`/Schannel e `httpx` (h1 y "h2") dan **200** con UA127; el CDN negocia **HTTP/1.1** (h2 descartado); `no-ua`/`ExoPlayerLib` -> 403; DNS devuelve una sola IP (`141.94.30.163`, OVH, igual con 8.8.8.8/1.1.1.1). El token es reutilizable (mismo token: 2x200 y luego 403 solo con UA malo). El log del dispositivo corta el URL a 120 chars (no era truncacion de la URL).
**Causa raiz**: en el dispositivo Chrome reproduce (misma IP/red), pero OkHttp no -> la discriminacion es el **stack de red (huella TLS/HTTP de OkHttp vs Chrome)**, no los headers ni la red. En bytecode (`CS3IPlayer$Companion.createVideoSource`): si el provider devuelve **`null`** como interceptor y hay `CronetEngine`, CS3 usa `CronetDataSource` (= stack de Chromium) y le aplica `setUserAgent(link.headers["User-Agent"])` + `setDefaultRequestProperties(referer + headers)`; si el interceptor no es null, usa `OkHttpDataSource` con addInterceptor.
**Fix**: (1) `getVideoInterceptor` devuelve **null** cuando el link es `tnmr`/`lulu` (log `lulu -> sin interceptor (Cronet)`) -> CS3 pasa a Cronet; (2) `tryLuluExtraction` setea `this.headers = mapOf("User-Agent" to chrome127Ua)` en los 2 emits (el UA viaja por `link.headers` en ambos caminos de createVideoSource).
**Versiones**: `PlushdProvider/build.gradle.kts` 26 -> **27**; `plugins.json` PlusHD 26 -> 27 (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- â¸ï¸ **Pendiente (usuario)**: compilar (`.\gradlew.bat :PlushdProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar Goblin EP1. En logcat buscar `lulu -> sin interceptor (Cronet)` + `Using CronetDataSource` (si sale `Using OkHttpDataSource` es que no hay CronetEngine -> hipotesis TLS descartada por esa via). Si con Cronet da 200 -> JA3/OkHttp era la causa y queda reproducir con segmentos.


### Sondeo de huella TLS para LuLu/tnmr (19 Oct 2026 v28)
**Motivo**: v27 (interceptor null -> Cronet) NO resolvio: el usuario reporta el mismo ERROR_CODE_IO_BAD_HTTP_STATUS (2004) en el link LuluStream. Evidencia acumulada: mismo IP/red -> python/OpenSSL (PC) 200, Chrome (celular) reproduce, OkHttp (celular y WSA) 403 con el UA correcto y sin cookies -> la discriminacion es del **stack cliente** (huella TLS), no de headers/cookies/IP. Falta saber si con Cronet tambien da 403 (el log del usuario se corto antes de Using CronetDataSource).
**Fix (auto-diagnostico + sorteo)**: en vez de elegir interceptor-vs-Cronet a ciegas, el plugin **prueba 4 perfiles TLS** contra el master LuLu y usa el primero que devuelva 2xx; si ninguno funciona, deja el camino Cronet de v27.
- luluChromeCiphers: orden de cifrados tipo Chrome (AES-GCM primero, luego CHACHA20, luego CBC).
- luluClient(profile): OkHttp propio cookie-less, HTTP/1.1 forzado; perfil 1 = solo HTTP/1.1, 2 = +TLS1.2, 3 = +cifrados en orden Chrome, 4 = ConnectionSpec.COMPATIBLE_TLS. Clientes cacheados en luluClients.
- probeLuluTls(master, referer): recorre 1..4, loguea perfil TLS N -> code, guarda el ganador en luluTlsProfile (0=sin probar, -1=ninguno, 1..4=ganador). Se llama desde 	ryLuluExtraction (rama loop y rama scan) cuando luluTlsProfile == 0.
- getVideoInterceptor: para links 	nmr/lulu -> si luluTlsProfile > 0 devuelve interceptor que NO usa chain.proceed sino luluClient(profile) (log lulu perfil N -> code url); si luluTlsProfile <= 0 devuelve null (Cronet de v27). El bloque 403-retry de v26 se elimino (queda cubierto por el perfil).
- El filtro 1080p/CF del interceptor se salta para lulu; el resto de hosts (vidhide/acek) intacto.
**Versiones**: PlushdProvider/build.gradle.kts 27 -> **28**; plugins.json PlusHD 27 -> 28 (ileSize pendiente, sigue 44712). al_check.py -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar, instalar y probar Goblin EP1 buscando perfil TLS N -> code (probara 1..4) y luego lulu perfil <ganador> -> 200; si sale perfil TLS elegido: -1 + sin interceptor (Cronet) es que ninguna huella OkHttp sirve y queda medir Cronet/WebView.


### Perfiles TLS descartados -> hipotesis IPv6 (19 Oct 2026 v29)
**Log del usuario (v28, celular fisico, Goblin EP1)**: los 4 perfiles dieron 403 seguidos y luego el camino Cronet:
```
perfil TLS 1 -> 403
perfil TLS 2 -> 403
perfil TLS 3 -> 403
perfil TLS 4 -> 403
perfil TLS elegido: -1
emit https://i2gcnsz23x2n.tnmr.org/hls2/01/02389/,wo70vtmvtau9_h,lang/spa/wo70vtmvtau9_spa,.urlset/master.m3u8?t=Kgp3ls5aKbNw
callback -> LuluStream (source=PlusHD)
lulu -> sin interceptor (Cronet) perfil=-1
```
**Lectura**: ni ciphers ni versiones TLS ni `COMPATIBLE_TLS` cambian nada -> **descartado que sea el perfil TLS de OkHttp**. Queda: (a) extensiones/GREASE del ClientHello (Conscrypt no las replica; Chrome si) o (b) algo de la **red del dispositivo** que hace fallar la ruta usada por OkHttp pero no la de Chrome.
**Hipotesis nueva (barata y muy probable): IPv6 vs IPv4.** Chrome (Happy Eyeballs) y Chrome-movil pueden caer a IPv4 mientras OkHttp usa la **primera** IP resuelta (normalmente IPv6); si el ISP del movil entrega IPv6 por una ruta interceptada/rota hacia el CDN (OVH), el 403 llega de un nodo distinto mientras Chrome acaba yendo por IPv4. El PC (donde python da 200) puede no tener IPv6.
**Fix (v29, sigue siendo auto-diagnostico)**:
- `luluClient` perfiles 5 y 6: `dns(luluIpv4Dns)` / `dns(luluIpv6Dns)` (objetos `okhttp3.Dns` que filtran `Inet4Address`/`Inet6Address`; OJO: `okhttp3.Dns` es interfaz **Kotlin**, no `fun interface` -> se implementa con `object : okhttp3.Dns { override fun lookup(...) }`, no con lambda).
- `probeLuluTls` ahora recorre **1..6** y loguea: `DNS <host> -> <ip,ip>` (las IPs que resuelve el telefono) y `cronet disponible: true/false` (via `Class.forName("org.chromium.net.CronetEngine")`, para saber si el camino Cronet de v27 existe de verdad en la app).
- Si el perfil 5 (IPv4) da 200 -> causa raiz confirmada y queda como ruta fija; si 6 (IPv6) da 200 -> el ISP prefiere IPv6 y hay que forzar esa familia; si los 6 dan 403 -> la discriminacion es el ClientHello (extensiones/GREASE) o un middlebox, y el unico camino es el stack de Chrome real (WebView vertical/JS) o reproductor externo (VLC).
**Versiones**: `PlushdProvider/build.gradle.kts` 28 -> **29**; `plugins.json` PlusHD 28 -> 29 (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar `.\gradlew.bat :PlushdProvider:make --console=plain -q`, instalar y probar Goblin EP1 -> buscar `DNS i2gcnsz23x2n.tnmr.org -> ...`, `cronet disponible: ...` y `perfil TLS 5 -> code` / `perfil TLS 6 -> code`.
- Pendiente extra: pegar las lineas **posteriores** a `lulu -> sin interceptor (Cronet)` (las de reproduccion: `Using CronetDataSource`/`Using OkHttpDataSource`, `ExoPlayer`, `2004`, `403`), que en el ultimo paste no llegaron.
- Workaround sin codigo vigente: long-press en el link LuLu -> abrir con reproductor externo (VLC usa otra pila TLS).


### v30: sonda Chromium/WebView + cuerpo y cabeceras del 403 (19 Oct 2026)
**Datos del log v29 en celular (18 Oct 23:28)**: `DNS i2gcnsz23x2n.tnmr.org -> 141.94.30.163` (**una sola IPv4, sin IPv6**) y `perfil TLS 1..6 -> 403` con `perfil TLS elegido: -1` -> **descartadas la hipotesis IPv6/IPv4 y TODOS los perfiles TLS de OkHttp** (ciphers, versiones, COMPATIBLE_TLS, familia de IP). Tambien `cronet disponible: true` -> las clases de Cronet SI estan en la app (el camino v27 existe). El paste vuelve a cortar en `lulu -> sin interceptor (Cronet) perfil=-1` (faltan las lineas de reproduccion).

**Fix v30 (solo diagnostico, no cambia el comportamiento)**:
1. El probe del 403 ahora imprime **cuerpo y cabeceras** del perfil 1: `perfil TLS 1 -> 403 server=... via=... body=<200 chars>` -> identifica QUIEN bloquea (WAF propio del CDN vs middlebox/adblock/red del dispositivo).
2. Nueva sonda **Chromium/WebView** (`probeLuluWebView`, `luluWebViewProbed`): solo corre cuando los 6 perfiles fallan. Carga el master en un `WebView`, y ya con el origen del CDN hace `fetch()` del master **y de un segmento**, logueando `[Wv] fetch -> <code|len|codeSeg|bytesSeg>` (o `[Wv] http error <code>` / `[Wv] resultado: timeout`). Mide si el stack de navegador del propio telefono puede leer master y segmentos.
3. `PlushdProvider.pluginContext` (nuevo, seteado en `PlushdPlugin.load`) para poder crear el WebView.

**Lectura / siguiente paso**:
- `[Wv] fetch -> 200|<len>|200|<bytes>` -> el stack Chrome del dispositivo SI pasa -> v31 = **proxy local** (`ServerSocket` en 127.0.0.1, precedente `MegaExtractor` de Retrotve) que sirve el master reescrito y obtiene cada segmento por el puente WebView.
- `[Wv] http error 403` / `[Wv] resultado: fetch err|...` -> bloqueo por **red o dispositivo** (app de VPN/adblock/DNS privado con tunel por-app, o el operador). Test sin codigo: desactivar esas apps y/o probar con datos moviles.

**Versiones**: `PlushdProvider/build.gradle.kts` 29 -> **30**; `plugins.json` PlusHD 29 -> 30 (`fileSize` pendiente, sigue 44712). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar `.\gradlew.bat :PlushdProvider:make --console=plain -q`, instalar y probar Goblin EP1 -> buscar `perfil TLS 1 -> 403 server=`, `probando Chromium/WebView del dispositivo...`, `[Wv] ...` y `[Wv] resultado: ...`.
- Pendiente extra: las lineas **posteriores** a `lulu -> sin interceptor (Cronet)` (`Using CronetDataSource`/`Using OkHttpDataSource`, `ExoPlayer`, `2004`), que siguen sin llegar.


### Causa raiz REAL del 403 de LuLu/tnmr: `Accept-Language` (19 Oct 2026 v31)
**Hallazgo medido en PC (matriz de cabeceras, token fresco, `lulu_hdrshape.py` + `lulu_lang.py`)**:
- El CDN `*.tnmr.org` devuelve **200 len=623** con `User-Agent` Chrome/127 Windows **y SIN `Accept-Language`**.
- Con **cualquier** `Accept-Language` no vacia -> **403 len=146** (pagina nginx). Probadas: `es-ES,es;q=0.9`, `es-419,...`, `es`, `en-US,en;q=0.9`, `en`, `en-GB`, `fr-FR`, `pt-BR`, `zh-CN`, `*` -> todas 403.
- `Accept-Language` **vacia** (`Accept-Language: `) -> **200**. Es decir: la regla del WAF es "si viene la cabecera, bloquear" (no importa el idioma).
- Otras cabeceras NO influyen (todas 200 con AL ausente): `Accept: */*`, `Accept-Encoding: gzip|gzip, deflate|identity`, `Connection: Keep-Alive`, `Accept-Charset: utf-8`, `Range: bytes=0-`, `Referer` (propio o `https://luluvdo.com/`), `Cache-Control`, `TE: trailers`, header en minusculas.
- La pagina **embed** (`luluvdo.com/e/...`) NO se ve afectada por `Accept-Language` (200 con es-419 y en-US): la regla vive solo en el CDN de video.
- La matriz de UA sigue vigente: **solo** el UA exacto `Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36` pasa; Android/`wv`/149/Firefox/Edge/Safari/VLC/python -> 403. Sufijos y variantes del UA127 tambien fallan (`sin Safari`, `Chrome/127`, `+ Coffee`).
- **Por que fallaban los caminos de la app**: Cronet (Chromium) y WebView inyectan `Accept-Language` del sistema -> 403; la sonda WebView de v30 ademas usaba el UA `; wv)` -> doble motivo.
- **FALSA PISTA (descartada en v31)**: parecia que el WAF discriminaba por **huella TLS** porque `luluClient` (OkHttp limpio, UA127, sin AL) tambien daba 403 en dispositivo. La causa real era el **token envenenado**: `tryLuluExtraction` pedia el embed con `Accept-Language` en `pageHeaders` -> el token nacia atado a esa cabecera -> 403 en todas las peticiones siguientes aunque el cliente ya no enviara AL. Verificado con OkHttp 4.12 + Conscrypt 2.5.2 en PC: 200, asi que TLS/Conscrypt nunca fue el problema.
- **Espejos**: `luluvdo.com`, `lulustream.com`, `luluvdoo.com`, `/e/`, `/d/`, `/embed/`, `POST /dl` (`op=embed`) devuelven **el mismo CDN y la misma ruta** (`i2gcnsz23x2n.tnmr.org/hls2/.../master.m3u8`) -> no hay CDN alternativo que usar. `download`/`download2` no devuelven packer. `/v/` -> 404.
- **Master (payload real, PC)**: `#EXTM3U` + `#EXT-X-MEDIA:TYPE=SUBTITLES` (es) + una sola variante 1920x1080 (BANDWIDTH 2501341). El payload del player trae `duration:"5310.96"` (~88 min) -> es el episodio completo.

**CAUSA RAIZ DEFINITIVA (medida en PC, `lulu_embedhdr.py`)**: el **token esta firmado con el `Accept-Language` del fetch del EMBED**. Prueba A/B/C/D (mismo codigo, token fresco por caso):
| Embed pedido con | Master pedido con | Resultado |
|---|---|---|
| `UA127 + Accept + Accept-Language: es-ES,es;q=0.9,en;q=0.8` | `UA127` (sin AL) | **403** |
| `UA127` solo | `UA127` (sin AL) | **200 len=623** |
| `UA127 + Accept` | `UA127` (sin AL) | **200 len=623** |
| `UA127 + Referer` | `UA127` (sin AL) | **200 len=623** |
- O sea: **el WAF del CDN bloquea cualquier peticion con `Accept-Language`**, y ademas el token emitido por un embed pedido con `Accept-Language` nace envenenado (403 en TODAS las peticiones de video, incluso sin AL). Eso explica por que fallaban a la vez OkHttp (perfiles 1..6), Cronet y WebView: los tres usaban el token envenenado.
- **Descartado el fingerprint TLS**: experimento con **OkHttp 4.12 + Conscrypt 2.5.2 (BoringSSL) en la PC** -> `200 len=622 proto=http/1.1` (mismo resultado que JDK TLS). Conscrypt/Android no es el bloqueo; era el token.

**FIX REAL v31 (`PlushdProvider.kt`)**:
1. **`tryLuluExtraction`**: eliminado `"Accept-Language" to "es-ES,es;q=0.9,en;q=0.8"` de `pageHeaders` -> el token sale limpio y el master/variante/segmentos responden 200. **Esta es la correccion que arregla la reproduccion.**
2. `luluRequest`: `removeHeader("Accept-Language")` defensivo (OkHttp no la anade por defecto, pero se blinda).
3. `probeLuluWebView`: `wv.settings.userAgentString = chrome127Ua` + `fetch` A/B (`plain` vs `noal` con `Accept-Language: ''`), master + segmento; resultado JSON con `ua`, `lang`, ambos estados. Queda solo como diagnostico de ultimo recurso.
4. `probeLuluTls`: log `proto=` en el perfil 1 (confirma HTTP/1.1) y `lulu req headers=<nombres>` en el interceptor (que manda ExoPlayer).
- **OJO con el camino Cronet**: Cronet/Chromium inyectan `Accept-Language` del sistema -> 403 por la regla del WAF. Por eso, si algun dia los perfiles OkHttp fallan, Cronet NO es la salida (habria que servir por WebView con `fetch` y AL vacia).
- Verificacion: `bal_check.py` -> BALANCE OK; `plugins.json` -> JSON valido, 72 entradas.
- Versiones: `build.gradle.kts` 30 -> **31**; `plugins.json` PlusHD 30 -> **31** (`fileSize` pendiente, sigue 44712). **No compilado por regla del repo.**

#### âœ… VERIFICADO EN DISPOSITIVO (19 Oct 2026, celular fisico, v31 compilada por el usuario)
Logcat de Goblin EP1: `GET .../e/wo70vtmvtau9 len=14166` -> `decoded len=5187` -> `perfil TLS 1 -> 200 server=nginx via=- proto=http/1.1` -> `perfil TLS elegido: 1` -> `emit .../wo70vtmvtau9_spa,.urlset/master.m3u8?t=...`; interceptor: `lulu req headers=Accept,Origin,referer,User-Agent` (**sin `Accept-Language`**) y `lulu perfil 1 -> 200` para master, variante `index-v1-a1.m3u8`, subtitulos y segmentos `seg-1..6-v1-a1.ts`. Sintoma resuelto (el usuario confirmo la reproduccion; VidHide hls3/hls4 del mismo episodio tambien OK). No se dispararon `[ip]`/`[Wv]` porque el perfil 1 paso a la primera.
- **Pendiente menor**: actualizar `fileSize` de PlusHD en `plugins.json` (sigue 44712; el `.cs3` compilado cambio de tamano).
- **`LuLu len=1124` = archivo BORRADO (verificado en PC, sin fix)**: `6z8x6d4kj5tg`, `4pmpp56xyoeu`, `qt81f6otsl4r` devuelven 200 con **757 B** y el texto `File is no longer available as it expired or has been deleted.` (mas el script de Cloudflare, que en el dispositivo redondea a 1124 B). No es un stub/redirect que se pueda seguir: la fuente esta muerta y caer a `loadExtractor` (0 links) es correcto. El episodio se salva por VidHide.
- **Descartados**: SPA `upns.pro`/`rpmstream.live`/`strp2p.com` (siguen en `error 2001, saltando`) y `emturbovid.com` (0 links en dispositivo).
- Leccion general para el repo: cuando un CDN devuelve 403 fijo con `Server: nginx` y cuerpo de ~146 B, probar **presencia/ausencia de `Accept-Language`** tanto en la peticion de video como en la del embed que emite el token (el token puede estar firmado con esas cabeceras). Y no confundir con TLS: si el embed lleva AL, todos los clientes nuevos heredan el 403.

---

## DoggitvProvider - Fix nombre del canal en load / favoritos (19 Oct 2026 v6)

**Sintoma (usuario)**: al abrir un canal, el detalle muestra **"En Vivo"** en vez del nombre real del canal. Tras cerrar y reabrir la app, los canales guardados en **favoritos** tambien salen como "En Vivo" (hay que volver a abrir la lista del pais para que aparezca el nombre).

**Causa raiz**: channelNames / channelLogos eran mapas **solo en memoria** (ConcurrentHashMap) que se llenan unicamente en parseM3U (al bajar la lista M3U de un pais). Al reabrir la app, CS3 llama load(urlDelStream) directamente (favoritos / continue watching) sin haber parseado ninguna lista -> channelNames[u] era 
ull -> titulo "En Vivo" y poster de placeholder.

**Fix v6 (DoggitvProvider.kt)**:
1. **Cache en disco** ilesDir/doggitv_channels.tsv (formato url<TAB>nombreEnc<TAB>logoEnc, enc/dec con URLEncoder): persistChannels() escribe (merge) al terminar getMainPage (lista del pais completa) y search (solo los 80 resultados); loadDiskCache() lo lee una vez por sesion al arranque de un load() fallido.
2. **esolveChannel(url)** en load(): memoria -> cache disco -> **escaneo de respaldo** (scanAllLists(): baja las 22 listas en paralelo con countries.amap {} = ParCollectionsKt.amap, verificado que corre en paralelo) y persiste el resultado. Con esto los favoritos **creados antes del fix** tambien recuperan el nombre (tarda ~5-15 s la primera vez).
3. DoggitvProvider.pluginContext (setado en DoggitvPlugin.load con pplicationContext) -> acceso a ilesDir.
4. El logo tambien sale del cache -> el poster del detalle sigue siendo el real tras reiniciar.

**Estado**: uild.gradle.kts 5 -> **6**; plugins.json DoggiTV ersion: 6 (ileSize sigue 0, pendiente). al_check.py -> BALANCE OK en los 2 .kt. **No compilado por regla del repo.**

- Pendiente (usuario): compilar (.\gradlew.bat :DoggitvProvider:make --console=plain -q), actualizar ileSize, instalar y abrir un canal -> el log debe traer load canal 'TUDN' -> ... (nombre real) en vez de load canal 'En Vivo'. Tras reiniciar la app, el favorito debe mostrar el nombre tambien.
- Nota: el cache de disco se crea la primera vez que se abre un pais (o se hace una busqueda); el escaneo de respaldo cubre el resto.
---

## DoramasytProvider - Fix enlaces no encontrados (19 Oct 2026 v2)

**Sintoma (usuario, logcat)**: Botones encontrados: 0 -> "enlaces no encontrados", aunque en la web hay varios enlaces.

**Causa raiz (verificada en PC)**: el provider construye la URL del episodio a mano (.../ver/{slug}-episodio-{num}), pero **el indice del slug NO siempre coincide con el numero de episodio**. El propio sitio da la URL correcta en su AJAX:
- POST /ajax/ajax_pagination/{id} -> {"paginate_url":".../ajax/caplist/{id}","perpage":50,"eps":[{"num":1}]} (sin URLs)
- POST /ajax/caplist/{id} -> {"caps":[{"episodio":1,"url":"https://www.doramasyt.com/ver/you-are-the-apple-of-my-eye-episodio-0","thumb":"..."}]} (**con la URL real**)
- Ejemplos medidos: you-are-the-apple-of-my-eye (1 eps) usa **-episodio-0**; la-luz-que-aun-nos-guia-latino (10 eps) usa -episodio-1..10. Con el patron viejo, el primero daba **404** -> pagina sin botones -> 0 links.

**Fix v2 (DoramasytProvider.kt)**:
1. load() ahora lee paginate_url del primer AJAX y hace el 2Âº POST a /ajax/caplist/{id} -> usa **cap.url** (URL real del sitio) para cada episodio; fallback al constructor viejo (uildEpisodeUrl) si ese endpoint falla. CapList + CapDetail ganan los campos paginate_url y url.
2. **ByseExtractor.kt portado** (17 KB, autocontenido: Jackson + OkHttp propio + JCA) desde TudoramaProvider -> el server "filemoon" del sitio apunta a **ysekoze.com**, que **NO** esta en los extractores de CS3 (solo byse.sx / bysebuho / byseqekaho / bysevepoin / bysezejataos). Verificado en PC: POST https://bysekoze.com/api/videos/access/challenge -> 200 con el schema de Byse (challenge_id, 
once).
3. **LuLu**: port del desempaquetador Dean Edwards de PlushdProvider (unpackLuluPacked + 	ryLuluExtraction) para luluvdo.com, **sin Accept-Language** en el fetch del embed (causa raiz del 403 en tnmr, ver PlusHD v31) + getVideoInterceptor que fuerza User-Agent Chrome/127 y quita Accept-Language en hosts 	nmr/lulu.
4. Labels por servidor (ilemoon - Byse, lulu - LuluStream) y dsvplay.com -> doodstream.com.
5. loadLinks devuelve ound real (antes 	rue siempre) y usa cookies + response.cookies para /reproductor?video=.

**Servidores del sitio (medidos, 8 por episodio)**: mega (mega.nz, sin extractor en CS3) | filemoon (ysekoze.com -> Byse) | voe (voe.sx, stub JS + ALTCHA -> CS3 puede dar 0) | lulu (luluvdo.com, empaquetado) | mxdrop (mixdrop.top, CS3 MixDropTo) | doodstream (dsvplay.com, CS3 no lo cubre) | streamtape (streamtape.com, CS3) | mp4upload (embed-undef.html -> el sitio mismo trae la URL rota).

**Estado**: uild.gradle.kts 6 (bogno, copiado) -> **2**; plugins.json DoramasYT 1 -> **2** (ileSize sigue 20000, pendiente). al_check.py (reescrito: maneja raw strings """, templates ${} y comentarios) -> BALANCE OK en los 2 .kt. **No compilado por regla del repo.**

- Pendiente (usuario): compilar (.\gradlew.bat :DoramasytProvider:make --console=plain -q), actualizar ileSize, instalar y abrir un episodio -> debe verse Botones encontrados: 8 + Link final (filemoon): https://bysekoze.com/e/... y yse sources=N.
- **Pendiente de portar (opcional)**: solver ALTCHA de VOE (desde SoloLatino/Serieskao) para el server "voe", y MegaExtractor (proxy local + AES-CTR, desde RetrotveProvider) para el server "mega".
#### âœ… VERIFICADO EN DISPOSITIVO (20 Oct 2026, v2, celular fisico)
Logcat (You Are the Apple of My Eye EP1 y Acaramelados Latino EP1): Botones encontrados: 8 / 9 -> Link final (filemoon): https://bysekoze.com/e/gfxg3ku18yak/ -> [Byse] attest ok -> captcha difficulty=16 -> captcha ok -> decrypted len=542 -> **yse sources=1**; Link final (lulu): https://luluvdo.com/e/em52xe4f7zvb -> lulu GET len=16285 -> **lulu emit https://ismhvhamoflz.tnmr.org/hls2/03/03457/em52xe4f7zvb_h/master.m3u8?t=...**. Los demas servidores van a loadExtractor (CS3). Tambien funciono el slug con typo: 	winkling-watermelon -> el sitio dio er/winkling-watermelon-episodio-1 (URL real del caplist) y abrio bien. **"Enlaces no encontrados" RESUELTO.**

#### ðŸ› Fix mapping dsvplay (20 Oct 2026 v3)
El v2 remapeaba dsvplay.com -> doodstream.com (por analogia con otros providers) y eso **rompia** el server: CS3 **ya tiene** los extractores Dsvplay (https://dsvplay.com) y Dhcplay (https://dhcplay.com) (verificado por bytecode del jar). Ademas doodstream.com/e/{id} responde **404** desde PC. **Fix**: quitar el remapeo (v3).

**Cobertura real por servidor (extractores CS3 del jar verificados)**: mega.nz **no** hay extractor | filemoon.bysekoze.com -> **nuestro Byse** | voe.sx -> Voe/Voe1/Voe2 (probable 0 por el stub JS + ALTCHA) | luluvdo/luluvdoo -> **nuestro unpacker** (CS3 LuluStream no desempaqueta) | mixdrop.top / mxdrop.to -> MixDropTo/MxDropTo | dsvplay.com -> Dsvplay | streamtape.com -> StreamTape* | filemoon.sx -> FileMoonSx | dhcplay.com -> Dhcplay | mp4upload.com -> Mp4Upload (el sitio a veces da embed-undef.html, roto en origen) | savefiles / listeamed / do7go -> **sin extractor** (0 links).
- Versiones a **3** (uild.gradle.kts + plugins.json; ileSize pendiente, sigue 20000). **No compilado por regla del repo.**
- Pendiente (usuario): compilar (.\gradlew.bat :DoramasytProvider:make --console=plain -q), actualizar ileSize, instalar y comprobar que el server **doodstream** (dsvplay) ahora emite link.
- Opcional a futuro: solver ALTCHA de VOE (desde SoloLatino) y MegaExtractor (desde RetrotveProvider) para cubrir oe y mega.
---

## RetrotveProvider - Fix MEGA: cambio de episodio rompe y deja el archivo corrupto (20 Oct 2026 v75)

**Sintoma (usuario)**: en una serie con servidor MEGA, el **episodio 1 se reproducia bien**; al **cambiar a otro episodio** -> error, no reproducia y **el reproductor se cerraba** (sin enlace); y una vez cerrado, **volver a intentar el episodio 1 ya no reproducia** (quedaba roto de forma persistente).

**Causa raiz (3 bugs en MegaExtractor.kt, todos de corrupcion/estado)**:
1. **Escritura sin lock en el path principal**: en downloadChunkWithFreshUrl/downloadChunkFromShard el af.seek(sc.filePos) + af.write(dec) iban **fuera de synchronized(fileLock)** (solo el path UFA y el on-demand lo usaban). En Phase 4 los **hilos de shard escriben en el MISMO RandomAccessFile**: seek()+write() no son atomicos -> las escrituras se intercalan y el **MP4 queda corrupto** (ExoPlayer no lo parsea -> 2001). El ileLock era **por instancia de DiskStream**, asi que un resume no se sincronizaba con el downloader viejo.
2. **af.setLength(start)** (truncado del archivo) en el camino de "chunk incompleto": **borra lo que escribieron otros hilos** en paralelo (el fix de v72 lo habia quitado solo del path cross-shard).
3. **Sin validacion de integridad**: cleanup() meteia en cachedFiles cualquier archivo con downloadComplete && length >= fileSize -> un MP4 corrupto (por 1 y 2) queda **cacheado en disco para siempre** -> el episodio 1 "ya no reproducÃ­a" ni aunque se reinicie la app.

**Fix v75 (MegaExtractor.kt)**:
1. **Escrituras con lock compartido por archivo**: ileLocks (companion) indexado por ruta absoluta -> todas las instancias del mismo archivo (downloader, on-demand, UFA) comparten el lock; el path CDN ahora hace synchronized(stream.fileLock) { raf.seek(writePos); raf.write(dec) } con writePos creciente (seek+write atomicos).
2. **Eliminado el setLength()** (no truncar en descargas paralelas).
3. **alidateMp4File(file, expectedSize)**: comprueba tamano exacto + cabecera MP4 valida (typ/moov...). Si el archivo cacheado esta corrupto -> **se borra y se re-descarga** en vez de servir basura.
4. **Retry de la API de MEGA** (getFileInfo): hasta **3 intentos** (1.5s/3s) + timeouts de la API 10s/20s. Antes un 509/429 transitorio -> 
ull -> **sin enlace** -> el reproductor cerraba.
5. **Registro de downloaders** (ctiveDownloaders): al cambiar de episodio, cleanup() marca ailed=true y **espera (join 3s)** a que el downloader viejo termine antes de escribir el bitmap/resumir -> no hay dos escritores sobre el mismo archivo.

**Estado**: uild.gradle.kts 35 (desync) -> **75**; plugins.json RetroTVE 74 -> **75** (ileSize pendiente, sigue 101190). al_check.py -> BALANCE OK. **No compilado por regla del repo.**

- Pendiente (usuario): compilar (.\gradlew.bat :RetrotveProvider:make --console=plain -q), actualizar ileSize, instalar y probar la serie: cambiar de episodio y volver al 1. Logs a mirar (TAG MegaExtractor): Downloader anterior para <id> detenido, Archivo corrupto al cachear ... se borra, MEGA metadata fallo (intento N/3), y que NO aparezca MEGA metadata fallo permanente.
- Si aun asi el episodio 2 no da enlace, pegar el logcat completo de MegaExtractor (con MEGA API[...] y Phase 1..4) para ver si es throttle 509 del CDN.- **Fix de compilacion (v75)**: ConcurrentHashMap.computeIfAbsent es **API 24+** (el min del plugin es 21) -> lint error. Reemplazado por getFileLock(path) con get + putIfAbsent (Map, API 1). Verificar en cualquier codigo nuevo: nada de computeIfAbsent/computeIfPresent/merge en plugins.
### ðŸ”§ Fix timeout 2001: on-demand de chunk cross-shard sin mapa (20 Oct 2026 v76)
**Sintoma (logcat del usuario, v75)**: el episodio 1 (Zt8m3LYQ) descarga bien, pero al cambiar al episodio 2 (44NxgQha) el player falla con ERROR_CODE_IO_NETWORK_CONNECTION_FAILED (2001). En el log:
`
Chunk 15 not ready after 5s, fetching on-demand from CDN (pos=66251780)
On-demand CDN sub[0] chunk 16 incomplete: got 2772555/4194304      <-- x12 intentos, todos iguales
`
**Causa raiz**: el chunk 16 (bytes 67108864-71303167) **cruza la frontera de shard** (66MB). El on-demand calculaba los sub-chunks con stream.shardOffsets, pero **el mapa de shards todavia no existia** (el probe tardaba ~28s) -> usaba un solo CDN con el rango completo -> ese CDN solo tiene su shard (66MB) -> devolvia **2772555 B truncados** -> el fetch fallaba siempre. Cuando el mapa llegaba (12:21:33) el mismo fetch con 2 sub-chunks funcionaba (On-demand CDN chunk 16 OK: 4194304 bytes), pero ExoPlayer ya habiaæ™¯å¾·izado la conexion (reset a los ~18s).
**Fix v76 (MegaExtractor.kt)**:
1. **Probe 20x mas rapido**: PROBE_START_SIZE/PROBE_END_SIZE de **1MB -> 512B** (el scan solo usa los primeros 512B) -> el mapa de shards pasa de ~28s a ~2-3s.
2. **El mapa se publica al instante**: el hilo del probe ahora hace stream.shardOffsets = probeResult (ya no espera al join) -> el on-demand lo ve de inmediato.
3. **El on-demand espera el mapa**: si shardOffsets.size <= 1 y hay varios CDN, espera hasta 20s a que el probe termine antes de construir los sub-chunks (si no, el rango truncado falla siempre).
4. Log de diagnostico en el serve: Serve abort: chunk N no disponible (pos=..., mapa=...).
**Estado**: uild.gradle.kts 75 -> **76**; plugins.json RetroTVE 75 -> **76** (ileSize pendiente, sigue 101190). al_check.py -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar (.\gradlew.bat :RetrotveProvider:make --console=plain -q), actualizar ileSize, instalar y cambiar de episodio. En el log debe verse Phase 2 probe complete: 6 shards (mapa publicado) **antes** de ~5s y On-demand CDN chunk 16 OK: 4194304 bytes (no incomplete).
### ðŸŽ¯ CAUSA RAIZ REAL del 2001: los archivos MEGA de retrotve estan TRUNCADOS (20 Oct 2026 v77)
**Sintoma (logcat del usuario, v76)**: el on-demand ya funciona (On-demand CDN chunk 98 OK: 4194304 bytes, mapa de shards = 6) pero el player sigue fallando con ERROR_CODE_IO_NETWORK_CONNECTION_FAILED (2001) y ExoPlayer pide offsets imposibles: ytes=3962471763-, ytes=453646114-, ytes=2465293655- (mayores que el archivo).

**Investigacion desde PC (medida con la API de MEGA + descifrado AES-CTR real)**:
- Cada URL de g (6) sirve una **ventana de ~76MB** (no 66MB): Content-Length = 76291842 / 76291840 (EP1) y 83837152 / 83837136 (EP2). La suma de ventanas (457MB) > archivo (381MB) -> las ventanas **se solapan** y una de las 6 URLs esta **sobrante** (el archivo son **5 partes** de 76291840 B: la suma cuadra EXACTO).
- g[1] es la que tiene el **ftyp** (descifrado en 0 =  0000020 66747970 69736f6d); el probe lo confirma (Shard #1: MATCH at 0MB). El orden de la lista **NO** es el orden de las partes -> el mapa por eliminacion (66MB fijo) asignaba offsets equivocados -> **MP4 corrupto**.
- **El dato clave**: el box 2 del archivo (el mdat) declara un tamaÃ±o **mayor que el archivo**:
  | archivo | fileSize | mdat declarado | falta |
  |---|---|---|---|
  | Zt8m3LYQ (SBS 1x01) | 381.459.202 (364MB) | **3.962.471.731 (3779MB)** | 3.6GB |
  | 44NxgQha (SBS 1x05) | 349.407.051 (333MB) | **2.465.293.623 (2351MB)** | 2GB |
  | I10hCa6C (SBS 1x02) | 419.185.698 (400MB) | **453.646.082 (433MB)** | 33MB |
  Y ExoPlayer pide **exactamente** mdat_size + 32 (3962471763 = 3962471731+32) -> busca el moov al final del mdat, que **no esta** (el archivo esta truncado = subida parcial).
- Los atributos (t) se descifran bien (MEGA{"n":"SBS - 1x01 - Piloto.SeriesRetro.com.mp4"}) -> la clave y el descifrado **son correctos**.
- **Conclusion**: los MEGA de retrotve son **subidas parciales**: tienen el typ + parte del mdat pero **sin moov** -> **ExoPlayer no puede parsearlos nunca** -> el 2001 es **del origen, no del plugin**.

**Fix v77 (MegaExtractor.kt)**:
1. detectTruncatedMega(): antes de emitir el enlace, lee 128 B de la cabeza, descifra, lee el tamano del box 2 y compara con ileSize; si typ + box2 > fileSize -> **no se emite el enlace** (log MEGA truncado: el archivo declara X B pero solo hay Y B (falta el moov)) -> el player ofrece los OTROS servidores del episodio en vez de un link que da 2001.
2. El probe usa el **tamaÃ±o de ventana medido** (Content-Length del no-range) en vez del 66MB hardcodeado (shardSize).
3. Orden del sondeo de la cabeza: indice 1 primero (historicamente el del ftyp), luego 0 y 2..n.

**Limitacion pendiente**: para archivos MEGA **completos** el mapa de shards sigue siendo aproximado (la lista g no viene en orden de partes y las ventanas pueden diferir en 2 B). Solucion definitiva: deducir los offsets exactos por **continuidad del keystream CTR** entre partes (o pedir a MEGA el mapa). Pendiente si aparecen MEGA completos.
**Estado**: uild.gradle.kts 76 -> **77**; plugins.json RetroTVE 76 -> **77** (ileSize pendiente, sigue 101190). al_check.py -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar (.\gradlew.bat :RetrotveProvider:make --console=plain -q), actualizar ileSize, instalar y abrir un episodio con MEGA -> en el log debe verse MEGA truncado: el archivo declara ... pero solo hay ... y **no** debe aparecer el enlace MEGA (se Offeredcen los otros servidores).
- Si un episodio tiene MEGA como unico servidor, dara "sin enlaces" -> es correcto (el archivo esta roto en origen).
### ðŸ”§ Detectar la cuota de MEGA (509) y no ofrecer el enlace (20 Oct 2026 v78)
**Reporte (usuario)**: al reproducir el episodio, MEGA muestra *"Cuota de transferencia agotada - La reproducciÃ³n de este video se ha interrumpido porque sobrepasarias la cuota de transferencia gratuita asignada a tu direccion IP. Amplia tu cuenta a Pro..."*.
**Que es**: es el **limite de la cuenta free de MEGA por IP** (450 GB/mes). El mismo limite aplica a la API/CDN: las peticiones devuelven **HTTP 509 "Bandwidth Limit Exceeded"** (el codigo ya lo sabia: en downloadChunkWithFreshUrl hay backoff exponencial + fallback UFA, pero si la cuota esta agotada no se recupera). No es cosa del plugin ni de la web del sitio: es MEGA.
**Fix v78 (MegaExtractor.kt)**: detectTruncatedMega() ahora devuelve el problema detectado ("truncado" / "quota" / null) y httpGetRange devuelve el codigo HTTP: si la cabeza responde **509** -> log MEGA 509: cuota de transferencia agotada para esta IP -> **no se emite el enlace** (se ofrecen los otros servidores del episodio) en vez de dejar un link que va a fallar con 2001/509.
**Estado**: uild.gradle.kts 77 -> **78**; plugins.json RetroTVE 77 -> **78** (ileSize pendiente, sigue 101190). al_check.py -> BALANCE OK. **No compilado por regla del repo.**
- Nota: la cuota es **por IP** -> otra red (datos moviles/VPN) la evita, pero el archivo **sigue truncado** (sin moov), asi que ese servidor no reproduce en ningun caso.
---

## PelismasterProvider - Plugin nuevo (20 Oct 2026 v1)

Sitio: pelismaster.com (CMS **MovieMaster / MoneyPlayer**, API en pi-encrypted.moviemaster.lat). Peliculas y series en Latino/Castellano/Subtitulado.

### Estructura verificada (PC, sin captcha)
| | |
|---|---|
| Home | / Â· /movies Â· /series (24 items/vista, **sin paginacion**) Â· /page/N |
| Orden | /movies?sort=trending|popular|release|rating (funciona) |
| Busqueda | /search?q=batman&page=2 (**si pagina**) |
| Pelicula | /movie/{slug} |
| Serie | /series/{slug} + /series/{slug}/season/{n}/episode/{m} |
| Tarjetas | <a class="cd" href="..."><img src=pÃ³ster><span class="cd-t">titulo</span><span class="cd-m">% aÃ±o dur</span></a> |
| Episodios | <ol class="eps"><li><a class="ep" href="..."><span class="ep-n">1</span><span class="ep-img"><img pÃ³ster><span class="ep-d">23 min</span><span class="ep-h"><span class="ep-t">1. Episodio 1</span> |
| Metadata | og:title ("Ver X Online Gratis en EspaÃ±ol Latino HD - PelisMaster"), og:image, og:description, p.meta a[href*='/year/'] (aÃ±o) |

### Flujo del player (3 llamadas, verificado end-to-end)
1. La pagina del episodio/pelicula trae el iframe **HTML-escapeado** en data-embed:
   data-pass="/api/player-pass?t={movie|tv}:{tmdbId}:{hash}" + src="https://api-encrypted.moviemaster.lat/embed/{movie|tv}/{tmdb}[/{s}/{e}]?lang=LAT".
   Ojo: hay que **des-escapar** (&quot; &lt; &gt; &amp; &#039;) antes de leer los atributos.
2. GET /api/player-pass?t=... (mismo origen, X-Requested-With: XMLHttpRequest) -> {"ok":true,"pass":"v1.<payload_b64>.<hmac>","ttl":600} (pass firmado, 10 min, atado al titulo; payload = {d:dominio,x:exp,e:exp,n:nonce,t:tipo:id}).
3. GET https://api-encrypted.moviemaster.lat/servers?tmdb=..&type=..[&season=..&episode=..]&pass=.. -> {"meta":{..},"servers":[{id,k,site,src,name,lang,mode,type:"hls",video}]}.
   Medido: pelicula 8 servidores (3 LAT / 5 SUB), episodio 21-24 (**16 LAT**, 3 SUB, 2 ESP). mode = directo | proxy.
4. GET {video} (ya trae &noiframe=1) -> **m3u8 master** (200, pplication/vnd.apple.mpegurl) con variantes y segmentos en pi-encrypted.moviemaster.lat/s/....
   **Los segmentos responden 200 SIN Referer y con CUALQUIER UA (probado con ExoPlayerLib) -> no hace falta interceptor.**

### Implementacion (v1, sin compilar por regla del repo)
- PelismasterProvider/build.gradle.kts (v1, language = "mx", tvTypes Movie + TvSeries), AndroidManifest.xml, PelismasterPlugin.kt, PelismasterProvider.kt.
- getMainPage: 6 filas (/movies, /series, trending, populares, estrenos, mejor valoradas) -> parseCards (.cd).
- search: /search?q=&page= en 3 paginas en paralelo (map), dedupe por url.
- load: /movie/ -> MovieLoadResponse; /series/ -> TvSeriesLoadResponse con **una peticion por temporada** ([href*='/season/']) y episodios de .ep (	his.season + 	his.episode + poster + duracion); /episode/ -> MovieLoadResponse (se reproduce directo).
- loadLinks: des-escape data-embed -> pass -> servers -> por cada servidor pide el master para detectar la calidad y **descarta los caidos (502)**, ordena LAT > ESP > SUB y directo antes que proxy, y emite hasta 12 links Servidor N [LAT].
- Si /servers responde 401/403 se **renueva el pass** (&renew=1) y se reintenta (igual que el JS del sitio).
- plugins.json: entrada PelisMaster/PelismasterProvider v1 (ileSize 0 pendiente; 73 entradas).

**Estado**: al_check.py -> BALANCE OK. **No compilado por regla del repo.**
- Pendiente (usuario): compilar (.\gradlew.bat :PelismasterProvider:make --console=plain -q), actualizar ileSize, instalar y probar home/busqueda/detalle/links. Logs: db logcat -s PelisMaster:V (TAG PelisMaster), buscar servidores -> N y link: Servidor 1 [LAT] (hls, P720).
- **Riesgo principal**: hay Cloudflare delante (server: cloudflare) pero **no bloqueo nada desde PC**; si el movil recibe challenge, habria que anadir UA de navegador/cookies (aun no se mitido en movil).
- Los servidores mode=proxy y algunos directo devuelven **502** (muertos) -> se filtran con el chequeo del master.
---

## KatanimeProvider - Fix load() tras rediseÃ±o del sitio (20 Oct 2026 v2)

**Sintoma (usuario)**: "el load no me carga como deberÃ­a, creo que la estructura cambiÃ³ en la web".

**Causa raiz (verificada en PC)**: la pagina de anime fue **rediseÃ±ada**. Los selectores viejos devolvian null/vacio:
| Antes (roto) | Ahora | Estado |
|---|---|---|
| h1.comics-title | h1.anime-title | **cambiado** |
| div#animeinfo img | img[data-src*='/original/'] (el src es un GIF placeholder) | **eliminado** |
| span#estado | span.badge.badge-status (+ div.detail-item "Estado") | **eliminado** |
| div.details-by (aÃ±o) | div.anime-meta (<span class="meta-text">OtoÃ±o 2026</span>) y div.detail-item | **eliminado** |
| div.anime-genres a | div.genres-anime a.genre-tag | **eliminado** |
| #sinopsis p | p.anime-synopsis | **eliminado** |
| div._pagination (data-url) | div.episodes-grid._pagination (misma data-url) | **ok** (mas especifico) |
| API de episodios | POST {data-url} + _token + cookies -> {"ep":{current_page,data,total,last_page..},"last":{numero}} | **ok** (misma forma) |

Lo que **NO** cambio y sigue funcionando (verificado end-to-end desde PC): el flow de loadLinks (ul.ul-drop li a[data-player] -> /reproductor?url= -> ar e='...' -> descifrado AES-CBC con MD5/salt) -> devuelve mega.nz, voe.sx, sfastwish.com, bysetayico.com... con los 10 servers (Mega, StreamW, VOE, FileMoon, Mp4Upload, Streamtape, LuluStream, Hexupload, Doodstream, Mediafire).

**Fix v2 (KatanimeProvider.kt)**:
1. load() con los selectores nuevos + **fallback a los viejos** (h1.comics-title, div#animeinfo img, span#estado, div.anime-genres a, #sinopsis p) para no romper si vuelven a cambiar.
2. AÃ±o desde div.anime-meta (con fallback a los div.detail-item).
3. piUrl con selector mas especifico div.episodes-grid._pagination (fallback div._pagination).
4. **Parseo de tarjetas robusto**: helper parseCards(doc) que saca el **titulo del lt del <img>** (atributo estable) en vez de la clase hasheada ._2uHIS, y el link de ._1A2Dc con fallback [href*='/anime/']. Asi el home/search sobreviven a un rebuild de Tailwind (las clases _135yj/_1A2Dc/_2uHIS son hasheadas y cambian).
5. uild.gradle.kts 21 (desync) -> **2**; plugins.json Katanime 1 -> **2** (ileSize pendiente). al_check.py -> BALANCE OK. **No compilado por regla del repo.**

- Verificado desde PC: 	itle, poster, status (Finalizado / En emision), year, genres, plot y la lista de episodios (**One Piece: total=1179, 48 paginas de 25**) extraen bien con los selectores nuevos.
- Pendiente (usuario): compilar (.\gradlew.bat :KatanimeProvider:make --console=plain -q), actualizar ileSize, instalar y abrir un anime -> debe verse titulo + poster + sinopsis + episodios.
- **Aviso**: el sitio usa clases hasheadas (Tailwind) -> en el proximo rebuild los selectores div._135yj pueden cambiar. Por eso el parseo usa lt/href/itemprop cuando se puede.

---

## TeleonlineProvider - Fix lentitud del home/search (20 Oct 2026 v4)

**Pregunta (usuario)**: "no se si sea el plugin o la misma pagina pero demora regular en cargar el search como el load, es normal o no?"

**Medicion desde PC (con los headers que usa el plugin)**:
| Peticion | Resultado |
|---|---|
| `/` (home) | 200 en **1.5s** (352KB) |
| `/canales/peru/` | 200 en **3.7-6.8s** (295KB) |
| `/?s=espn` (search) | **HTTP 500 tras 122s** (el search del sitio esta ROTO/colgado) |
| `/?s=a` | no responde (>25s) |
| `/wp-json/wp/v2/posts?cat=326` | 200 en **41s** |
| `admin-ajax.php` (infinite scroll) | timeout >45s |
| `/wp-json/` | 200 en 5.9s |

**Diagnostico: el sitio esta saturado y su search esta roto; ademas el provider lo empeoraba**:
1. El tema "asap" (WP) carga los canales por **infinite scroll (AJAX)**: la 1a `article-loop` de cada pagina es la **plantilla JS** (`${post.url}`, `${post.title}`) y las siguientes 30 son reales. El provider parseaba la plantilla -> 1 item basura por seccion.
2. El `getMainPage` hacia **10 peticiones SECUECIALES** (home + /page/2/ + 8 paises) a ~4-7s cada una -> **~50s** de espera.
3. El `search` usaba `/?s=` (roto en el sitio) con 2 intentos (15s + 30s) -> **~45s esperando para nada** y resultado vacio.

**Fix v4 (`TeleonlineProvider.kt`)**:
1. `loadAllSections()`: las 10 secciones se piden **EN PARALELO** (`amap`) -> **14.4s medidos** (antes ~50s) y 330 canales.
2. **Search local**: filtra por nombre (con `fold` de acentos) sobre las secciones ya cargadas -> **instantaneo** con la cache de 5 min. En paralelo se intenta el `?s=` del sitio con **timeout corto (8s, 1 intento)**: si el sitio lo arregla, sus resultados se suman; si no, se pierde solo ese intento (no los 45s de antes).
3. **Plantilla JS filtrada**: se descartan los items con `${` en la URL/titulo/poster (la plantilla del tema).
4. `safeGet` con parametro `attempts` (el search usa 1 intento).
5. `fetchSection` pide las paginas de una seccion en paralelo.
6. `build.gradle.kts` 3 -> **4**; `plugins.json` Teleonline 3 -> **4** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- El `load` (canal concreto) es **1 sola peticion** y el sitio tarda 3-7s en responder -> esa parte es del sitio, no del plugin.
- Opcional a futuro: cargar mas paginas por seccion (`posts_per_page=30`, `maxPages=10` en el config del tema) para ampliar el search local; de momento son 330 canales.


#### ðŸ”§ Fix search: usar la API REST en vez del `?s=` roto (21 Oct 2026 v5)
**Sintoma (usuario, log v4)**: `search 'universal' -> 0 (local=10 secciones, sitio=false)` -> el search no encontraba canales (p.ej. "Studio Universal").
**Causa raiz**: el `/?s=` del **tema** (asap) esta roto: cuelga y el PHP lo mata a los **122s** (500). El search por paginas del plugin tampoco servia: el catalogo es enorme (p.ej. Alemania 350, y 10 secciones x 30 = 300 canales de la 1a pagina) y el sitio tardaba 4-7s por pagina -> con 3 rondas solo se veian 660 canales y aun asi 0 resultados (ademas varias peticiones fallaban por saturacion: 180 de 300).
**Descubrimiento clave**: la **API REST de WordPress SI busca** (lo que cuelga es el search del tema):
- `GET /wp-json/wp/v2/search?search=universal&per_page=30&subtype=post` -> **200 en 9.6-12.9s** -> `[{id,title,url,type,subtype}]` (el `url` es el link del canal) -> "Studio Universal Africa", "Studio Universal Latin America"... y `page=2` trae el resto.
- `GET /wp-json/wp/v2/posts?search=...&per_page=30&_embed=wp:featuredmedia` -> **>50s** (descartado, demasiado pesado).
- `GET /wp-json/wp/v2/categories?per_page=100` -> 200 en 13s (mapa pais -> cat, util a futuro).

**Fix v5 (`TeleonlineProvider.kt`)**:
1. `search()` usa **`/wp-json/wp/v2/search`** (timeout 35s, 1 peticion, hasta 60 resultados) como via principal. Los posters se toman de la cache local de secciones (`posterForUrl`).
2. **Fallback local**: si el REST devuelve menos de 5, se filtran las secciones ya cargadas (2 rondas de paginacion) -> cubre el caso de que el REST falle.
3. Se **elimina el uso de `/?s=`** (cuelga 122s y no aportaba nada).
4. `fetchSectionPage` + cache por `nombre|pagina` -> el home y las rondas del search comparten cache (no se vuelve a pedir lo ya descargado).
5. `build.gradle.kts` 4 -> **5**; `plugins.json` Teleonline 4 -> **5** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- Nota: el sitio esta **muy saturado** (REST 9-41s, AJAX 8-44s, HTML 1.5-7s). Si el search sigue tardando, es el servidor.
- Pendiente (usuario): compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize`, instalar y buscar "universal" -> debe salir `Studio Universal ...` (log `rest search -> N items`).


#### ðŸ› Fix mojibake en los nombres de los links + posters del search (22 Oct 2026 v6)
**Sintoma (usuario, log v5)**: (1) "no me salen los posters en los resultados del search"; (2) "los nombres de los enlaces me salen raros" -> el log mostraba `M3U8 from base64 (opciÃƒÆ’Ã‚Â³n 3)`.

**Causa raiz (1) - los nombres raros**: el source tenia **mojibake** (doble codificacion UTF-8 -> cp1252 -> UTF-8) en los literales: `"En Vivo - OpciÃƒÆ’Ã‚Â³n ${idx + 1}"`, `"PerÃƒÆ’Ã‚Âº"`, `"MÃƒÆ’Ã‚Â©xico"`, `"EspaÃƒÆ’Ã‚Â±a"`. **Era preexistente en git** (commit "teleonline timeout"), no de esta sesion. El fix correcto es ì—­ (revertir 2 niveles de cp1252->utf8); un regex ingenuo que captura solo 2 caracteres **parte** la secuencia y la deja peor, asi que se aplico un mapa explicito de secuencias -> `Ã³` (o), `Ãº` (u), `Ã©` (e), `Ã­` (i), `Ã±` (n), `Â¡` (!), y `->` en el log del interceptor.
**Nota**: el resto del repo tambien tiene mojibake preexistente (`DanimadosExtractors.kt` "Espa??ol" en los subtitulos, `TudoramaProvider.kt`, `TwitchProvider/Lang.kt`, `YoutubeProvider/Lang.kt`, `AnimeioProvider.kt`) -> pendientes de arreglar aparte.

**Causa raiz (2) - sin posters**: el endpoint compacto `/wp-json/wp/v2/search` no trae media, y la cache local de secciones solo conoce los canales ya descargados (el search corria antes que el home).

**Fix v6 (`TeleonlineProvider.kt`)**:
1. El search usa **`/wp-json/wp/v2/posts?search={q}&per_page=30&_fields=id,featured_media,link,title`** (1.9s medidos) -> trae `featured_media`.
2. Los posters se piden con **`/wp-json/wp/v2/media/{id}?_fields=source_url`** para los 15 primeros, **en paralelo** (`amap`) -> +1.5s.
3. `cleanHtml()` limpia el titulo (viene como HTML renderizado: entities y tags).
4. Se mantienen el fallback local y `posterForUrl` (cache de secciones) por si el REST falla.
5. `build.gradle.kts` 5 -> **6**; `plugins.json` Teleonline 5 -> **6** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- **Regla para el futuro**: al editar `.kt` desde PowerShell 5.1 NO usar `Set-Content -Encoding UTF8` / `Out-File` (decodifican con el ANSI del sistema y producen mojibake). Usar la herramienta de edicion, o escribir con python `io.open(..., encoding="utf-8", newline="")`. Verificar despues con un escaneo de caracteres no-ASCII.
- Pendiente (usuario): compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize`, instalar -> el search debe salir con poster y los links deben llamarse "En Vivo - Opcion N" (con la tilde correcta).


#### ðŸ› Fix titulo del search (objeto rendered) + posters de TODOS los resultados (22 Oct 2026 v7)
**Sintoma (usuario, log v6)**: (1) los nombres de los canales en el search salian como `{"rendered":"Studio Universal Africa"}`; (2) "solo me salen algunos posters, no de todos los resultados".
**Causa raiz**:
1. Con `_fields=...,title` el titulo de WP viene como **objeto renderizado** (`{"rendered":"..."}`), no como string plano -> `optString("title")` devolvia el `toString` del JSONObject.
2. Los posters se pedian solo para los **15 primeros** (`take(15)`) -> los otros 15 salian sin poster.
**Fix v7 (`TeleonlineProvider.kt`)**:
1. El titulo se lee como `post.optJSONObject("title")?.optString("rendered")` con fallback a string plano, y se pasa por `cleanHtml()`.
2. Los posters se piden para **TODOS** los items, en **tandas de 10 en paralelo** (`chunked(10)` + `amap`) -> ~3-5s en vez de 1.5s, pero ningun resultado se queda sin poster (medido: 30/30 con poster).
3. `build.gradle.kts` 6 -> **7**; `plugins.json` Teleonline 6 -> **7** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- **La web ya no esta saturada** (medido 22 Oct): `/wp-json/wp/v2/posts?search=...&per_page=30&_fields=...` -> 1.9s; `/wp-json/wp/v2/media/{id}` -> 1.4s; `?_embed=wp:featuredmedia` -> 17.4s con 30/30 posters (alternativa en 1 solo request, mas pesada).
- Pendiente (usuario): compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize`, instalar -> el search debe salir con el titulo limpio y poster en todos los resultados.


#### ðŸ›¡ Search a prueba de saturacion: REST y local EN PARALELO + reintentos (22 Oct 2026 v8)
**Pregunta (usuario)**: "con esta correccion no importa si la web este saturada, siempre funcionara el search??"
**Respuesta honesta**: no al 100%. Antes (v3/v4) con la web saturada el search **nunca** funcionaba (el `?s=` del tema moria a los 122s). Desde v5 usa el REST (query ligera, indexada por titulo) -> medida en el peor caso: **24.4s** con la web saturada y **1.9s** con la web normal. Pero si el REST supera el timeout, el search se quedaba vacio.
**Fix v8 (`TeleonlineProvider.kt`)**:
1. **REST y fallback local en PARALELO** (`async` + `coroutineScope`) y se fusionan los resultados -> el tiempo total es el max de ambos, no la suma. Si el REST cae, los resultados locales ya estan listos.
2. **Timeout del REST subido a 40s** y con **2 intentos** (1.5s de pausa entre ellos) -> aguanta la saturacion sin quedarse sin resultados.
3. `localSearchResults()` separado (2 rondas de paginacion de las secciones, tope 20).
4. `build.gradle.kts` 7 -> **8**; `plugins.json` Teleonline 7 -> **8** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- Limite real: si **todo** el sitio esta caido (REST + secciones), el search devuelve 0. No hay forma de buscar en un servidor que no responde.
- El search de CS3 tiene 120s de timeout (`APIRepository.search` -> `withTimeout(getTimeout(searchTimeoutMs))`), asi que 40s + fallback entran de sobra.


#### ðŸŽ¯ Filtro de servidores MUERTOS en los links (22 Oct 2026 v9)
**Sintoma (usuario, WSA)**: "al momento de iniciar la reproduccion se me sale la app" (emulador WSA).
**Lo que NO era**: el error pegado (`TransactionTooLargeException: data parcel size 1054620 bytes` en `com.microsoft.windows.systemapp`) es la **redireccion de portapapeles de WSA** (el portapapeles de Windows -> Android, 1MB > limite de Binder) -> es del emulador, no de nuestra app. Para saber si hay crash real hace falta `adb logcat -b crash -d`.
**Causa raiz encontrada (medida desde PC)**: de las 4 opciones que emitia el provider para "Studio Universal", **2 estan muertas**:
| Opcion | URL | Estado |
|---|---|---|
| 3 | `http://38.134.250.110:8000/play/a006/index.m3u8` | **200 m3u8** âœ“ |
| 4 | `http://15.204.246.24:8080/UniversalHD/index.m3u8` | **403** âœ— |
| 5 | `http://138.121.15.230:9002/UNIVERSAL-CHANNEL/index.m3u8` | **200 m3u8** âœ“ |
| 6 | `http://181.119.66.28:8081/Universal-HD/index.m3u8` | **no conecta** âœ— |
Es decir, el usuario podia elegir un link que da 403/timeout -> error de red en el player.
**Fix v9 (`TeleonlineProvider.kt`)**:
1. `emitAlive(candidates, referer, callback)`: **prueba** cada m3u8 (GET con los mismos headers del player, timeout 8s) y **solo emite los que devuelven 200 con `#EXTM3U`**; los muertos se omiten con log `servidor caido, se omite: ...` y `links vivos: X/Y`.
2. Aplicado en las 3 ramas (base64, m3u8 directo e iframes).
3. `PLAYER_UA` como constante.
4. `build.gradle.kts` 8 -> **9**; `plugins.json` Teleonline 8 -> **9** (`fileSize` pendiente). `bal_check.py` -> BALANCE OK. **No compilado por regla del repo.**

- Nota: el WSA tiene su propio historial con cierres de app (ver DoggiTV: el emulador no reproduce MPEG-1 Layer II y el player queda en BUFFERING). Si el crash persiste, hace falta `adb logcat -b crash -d` para ver la excepcion real.
- Pendiente (usuario): compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize`, instalar y abrir el canal -> deben salir solo los links vivos (log `links vivos: 2/4`).


#### ðŸŽ¯ Diagnostico del "se me sale la app" en WSA: es ANR, no crash (22 Oct 2026)
**Sintoma (usuario, WSA)**: "al momento de iniciar la reproduccion se me sale la app".
**Como se diagnostico (WSA con adb)**: `adb connect 127.0.0.1:<puerto>` (el puerto lo muestra WSA -> Settings -> Advanced -> Developer options -> ADB debugging).
- `adb logcat -b crash -d` -> **sin FATAL EXCEPTION de `com.lagradost.cloudstream3.prerelease`** (el unico crash es de `com.android.vending`/Play Store, sin relacion).
- `adb logcat -d | findstr "has died lmkd Killing ANR"` ->
  ```
  ActivityManager:   0% 21/lmkd: 0% user + 0% kernel          <- ANR
  ActivityManager: Killing 1217:com.lagradost.cloudstream3.prerelease/u0a87 (adj 0): user request after error
  ActivityManager: Killing 3584:com.lagradost.cloudstream3.prerelease/u0a87 (adj 0): user request after error
  ```
  -> **la app se CUELGA (ANR) y el sistema la mata**. No es un crash de Java del plugin.
**Causa raiz**: los streams de Teleonline son **http a IP:puerto alto** (`http://38.134.250.110:8000/...`, `http://138.121.15.230:9002/...`) -> el NAT del WSA no los enruta (ya estaba documentado para el movil fisico: "Opcion 3 cuelga en el telefono pero responde al instante desde PC -> middlebox de puerto 80"). El player espera ese stream -> ANR -> el sistema mata la app.
**Mitigacion aplicada (v9)**: `emitAlive()` **prueba** cada m3u8 desde el propio dispositivo y descarta los que no responden -> en el WSA no aparezcan los links muertos (403 / no conecta) -> no se cuelga al elegirlos. Si el dispositivo no llega a ninguno, el search/play se queda sin enlaces (correcto, no es un crash).
**Test valido**: **cs3d (Windows)** reproduce (el server responde al instante desde Windows) o movil fisico **con VPN**.
- El error pegado por el usuario (`TransactionTooLargeException: data parcel size 1054620 bytes` en `com.microsoft.windows.systemapp`) es la **redireccion de portapapeles de WSA** (Windows -> Android, 1MB > limite de Binder) -> es del emulador, no de la app.


#### ðŸŽ¯ Diagnostico del "se me sale la app" en WSA: es ANR, no crash (22 Oct 2026)
**Sintoma (usuario, WSA)**: "al momento de iniciar la reproduccion se me sale la app".
**Como se diagnostico (WSA con adb)**: `adb connect 127.0.0.1:<puerto>` (el puerto lo muestra WSA -> Settings -> Advanced -> Developer options -> ADB debugging).
- `adb logcat -b crash -d` -> **sin FATAL EXCEPTION de `com.lagradost.cloudstream3.prerelease`** (el unico crash es de `com.android.vending`/Play Store, sin relacion).
- `adb logcat -d | findstr "has died lmkd Killing ANR"` ->
  ```
  ActivityManager:   0% 21/lmkd: 0% user + 0% kernel          <- ANR
  ActivityManager: Killing 1217:com.lagradost.cloudstream3.prerelease/u0a87 (adj 0): user request after error
  ActivityManager: Killing 3584:com.lagradost.cloudstream3.prerelease/u0a87 (adj 0): user request after error
  ```
  -> **la app se CUELGA (ANR) y el sistema la mata**. No es un crash de Java del plugin.
**Causa raiz**: los streams de Teleonline son **http a IP:puerto alto** (`http://38.134.250.110:8000/...`, `http://138.121.15.230:9002/...`) -> el NAT del WSA no los enruta (ya estaba documentado para el movil fisico: "Opcion 3 cuelga en el telefono pero responde al instante desde PC -> middlebox de puerto 80"). El player espera ese stream -> ANR -> el sistema mata la app.
**Mitigacion aplicada (v9)**: `emitAlive()` **prueba** cada m3u8 desde el propio dispositivo y descarta los que no responden -> en el WSA no aparezcan los links muertos (403 / no conecta) -> no se cuelga al elegirlos. Si el dispositivo no llega a ninguno, el search/play se queda sin enlaces (correcto, no es un crash).
**Test valido**: **cs3d (Windows)** reproduce (el server responde al instante desde Windows) o movil fisico **con VPN**.
- El error pegado por el usuario (`TransactionTooLargeException: data parcel size 1054620 bytes` en `com.microsoft.windows.systemapp`) es la **redireccion de portapapeles de WSA** (Windows -> Android, 1MB > limite de Binder) -> es del emulador, no de la app.

## TeleonlineProvider - Reversion a la version estable v1 (22 Oct 2026 v11)

**Pedido (usuario)**: "reinicie el codigo de teleonline a la mas estable; el unico problema seria que en las saturaciones de la pagina el search ya no funciona y el load se demora en cargar".

### Estado del working tree al iniciar
- `TeleonlineProvider.kt` estaba reescrito a mano a **272 lineas**, igual a `ad79dd6d` (v1, "add getVideoInterceptor...") **menos** el bloque `getVideoInterceptor` (32 lineas eliminadas). Es decir: se volvio al codigo de la era v1, sin las reescrituras de search (v4-v10) y sin interceptor.
- **DESYNC critico**: `build.gradle.kts` decia `version = 1` mientras `plugins.json` seguia en `version: 10`. Con eso la app NUNCA instala la reversion (10 > 1). Corregido.

### Cambios aplicados (v11)
1. `TeleonlineProvider/build.gradle.kts`: `version = 1` -> **11**; `plugins.json` Teleonline `version: 10` -> **11** (el .cs3 con codigo estable se publica como build nuevo).
2. **Fix del timeout que causaba "el load se demora"**: `safeGet(url, timeoutMs: Long = 15000L)` hacia `app.get(..., timeout = timeoutMs * attempt)` -> en NiceHttp el `timeout` va en **SEGUNDOS** (regla 03 Oct 2026), asi que eran **15000 s / 30000 s** (sin timeout efectivo: la peticion colgaba hasta el killer de 120 s de CS3). Ahora `safeGet(url, timeoutSec: Long = 15L)` con `timeout = timeoutSec * attempt`; en `load()` la llamada pasa de `safeGet(url, 20000L)` a `safeGet(url, 20L)`.
3. `fileSize` en `plugins.json`: **pendiente** (sigue 24485) tras compilar.
- `bal_check.py` -> BALANCE OK; `plugins.json` valido (73 entradas).

### Limitaciones aceptadas por el usuario
- `search()` usa el `/?s=` del tema **asap**, que bajo saturacion cuelga hasta el kill de 122 s del PHP -> devuelve vacio. No se reintrodujo el REST search (v4-v10) porque el usuario pidio la version estable.
- `getMainPage` pide los 10 bloques (Canales + 9 paises) **en secuencia** (hasta 15 s x2 cada uno) -> es la parte lenta del "load". La version v4 lo paralelizaba (`amap`) pero se omitio al restaurar la estable.

### Diagnostico previo (queda documentado, sin fix en v11)
- Con la v10 (REST search) el log mostraba `rest search -> 9 items` OK, pero al reproducir: `playerUrls: []` -> en **WSA** los streams `http://IP:puerto` (38.134.250.110:8000, 138.121.15.230:9002, etc.) no se enrutan por su NAT -> `emitAlive` los descarta todos -> 0 enlaces, y el player se cuelga (ANR, `MainActivity because it is not responsive`) hasta que el sistema mata la app.
- Test valido: **cs3d (Windows)** o **movil fisico con VPN**; en WSA esos canales no reproducen.

- â¸ï¸ **Pendiente (usuario)**: compilar `.\gradlew.bat :TeleonlineProvider:make --console=plain -q`, actualizar `fileSize` en `plugins.json`, instalar y comprobar que el home carga y que un canal reproduce (en cs3d o movil con VPN).


## TeleonlineProvider - Audio en espanol: causas y opciones (22 Oct 2026, SIN cambio de codigo)

**Pregunta (usuario)**: "Studio Universal Latin America" no da audio en espanol (la opcion 8 trae `eng`+`spa` y solo suena/reconoce `eng`).

### Como se generan las "opciones" (v11)
- El provider NO lee la UI del sitio (`div.tab-buttons`/`select.tab-select` estan **vacios**: los rellena JS). Usa `Regex("""(?:atob|btoa)\s*\(\s*['\"]([A-Za-z0-9+/=]+)['\"]""")` sobre el HTML (linea ~171) y `opcion ${idx + 1}` es el **indice global** entre TODOS los `atob`, mostrando solo los que decodifican a `.m3u8`.
- El post de Latin America es **55577** (`/canal/studio-universal-latin-america/`, HTML 318859 B). En una descarga fresca hay **6 `atob`** (`/js/toc.js`, `DisableDevtool` y **4 m3u8**) -> las opciones 8/9 vistas en el log **no** estaban en esa descarga: **la lista de opciones rota por request / por variante del canal**. No fiarse de un solo fetch.
- Busqueda del canal: `https://teleonline.org/wp-json/wp/v2/search?search=studio+universal&per_page=10&subtype=post` (2 resultados).

### Opciones medidas (Studio Universal Latin America, PC + parseo PMT)
| URL | Master | Renditions | Variante | PMT del segmento |
|---|---|---|---|---|
| `http://187.102.210.46/universo/index.m3u8` | 200 (426 B) | **2** (`spa a1` DEFAULT=YES, `eng a2`) | `tracks-v1a1/mono.m3u8` 200 | video `0xd3` (0x1B) + audio `0xdd` **`spa`** |
| `http://190.93.224.42/STUDIO-UNIVERSAL/index.m3u8` | 200 (435 B) | **2** (`spa a1` DEFAULT=YES, `eng a2`) | `tracks-v1a1/mono.ts.m3u8` 200 | video + audio **`spa`** |
| `http://138.121.15.230:9002/STUDIO-UNIVERSAL/index.m3u8` | 200 (172 B) | 0 | `tracks-v1a1/mono.m3u8` 200 | video + audio **`spa`** (unico) |
| `http://15.204.246.24:8080/StudioUniversalHD/index.m3u8` | **403** | - | - | muerta |
- En las dos primeras, las renditions de audio son **TS solo-audio**: `tracks-a1/mono.m3u8` -> `0xdd` **`spa`** (153 KB), `tracks-a2/mono.m3u8` -> `0xdd` **`eng`** (117 KB). La variante muxada trae **solo `spa`** (el audio en ingles solo existe como rendition aparte).

### Causa raiz de la opcion 8 (audio `eng`, sin forma de cambiarlo en la app)
- Su master tiene **una sola variante y NINGUNA `#EXT-X-MEDIA`**; el TS lleva `eng` en PID `0x121` y `spa` en `0x3f8`, **ambos `streamType 0x04` (MPEG-1 Layer II)**.
- **media3 1.9.3 `TsExtractor.java`** (verificado en el fuente de la version que usa la app): `trackId = mode == MODE_HLS ? streamType : elementaryPid` + dedupe que conserva el **PID mas bajo por `streamType`** -> los dos audios MP2 colapsan en **un solo track** (`eng`); el segundo ni se crea.
- Consecuencia: **no hay 2 pistas** -> `GeneratorPlayer.onTracksInfoChanged()` no muestra `playerTracksBtt` (solo aparece con `>1` pista de audio o video) -> **imposible elegir espanol in-app**. No es un bug del provider y no se arregla con headers/interceptor.

### Que opcion usar (y por que funciona sin tocar codigo)
- Masters reales (texto completo): `#EXTM3U` + las 2 `#EXT-X-MEDIA` de arriba + `#EXT-X-STREAM-INF:...,AUDIO="aac",RESOLUTION=1920x1080,CODECS="avc1.640028"` -> la variante **referencia el grupo de audio** (`AUDIO="aac"`), condicion que media3 exige para crear los 2 tracks (187: 1.99 Mbps; 190: 2.78 Mbps).
- Con `187.102.210.46/universo` o `190.93.224.42/STUDIO-UNIVERSAL` (ambas con `#EXT-X-MEDIA`): ExoPlayer crea **2 tracks de audio** -> el boton de pistas se muestra y la auto-seleccion usa `setPreferredAudioTrackLanguage(...)` (`CS3IPlayer.Companion.getPreferredAudioTrackLanguage()`, publico). **DEFAULT=YES es `spa`** -> arranca en espanol y se puede cambiar a `eng` desde el boton.
- Con `138.121.15.230:9002` el audio es **solo `spa`** (un track) -> suena en espanol aunque no haya boton de pistas.
- `15.204.246.24:8080` (403) es opcion muerta: si el usuario la elige, error de red.

### Estado
- **Sin cambios de codigo**: v11 se queda como la version estable pedida. `build.gradle.kts` = `plugins.json` = **11**; `fileSize` sigue **24485** (pendiente tras compilar).
- â¸ï¸ **Pendiente (usuario)**: compilar e instalar; en "Studio Universal Latin America" elegir la opcion que apunte a `187.102.210.46/universo` o `190.93.224.42/STUDIO-UNIVERSAL` (log `M3U8 from base64 (opcion N)`); debe sonar en espanol y, en esas dos, aparecer el boton de pistas con `spa`/`eng`.
- â¸ï¸ Si se quisiera mejorar la UX (no pedido): probar cada master y marcar el link como `[ESP]`/`[ESP+ENG]` segun `#EXT-X-MEDIA`, o forzar `setPreferredAudioTrackLanguage` â€” ambas son cambios de codigo (v12) y con coste extra por opcion bajo saturacion.


## TeleonlineProvider - cs3d ve el audio y Android no: libmpv vs media3 TsExtractor (22 Oct 2026, SIN cambio de codigo)

**Pregunta (usuario, caso Cartoon Network Latin America, "opcion 8")**: en cs3d (Windows) el reproductor **detecta y permite el audio en espanol**; en CloudStream Android el mismo canal suena en **ingles** y no aparece el boton de pistas. Por que?

### Por que cs3d si puede
- cs3d (`lelouchcodes11/cs3d`, Compose Desktop) **no usa ExoPlayer**: reutiliza la UI del player (`com.lagradost.cloudstream3.ui.player/*`) pero la **reproduccion real la hace libmpv/FFmpeg**, que expone **todas** las pistas de audio del contenedor (enumera los PIDs del MPEG-TS). De ahi el selector de audio y el espanol.
- No es nada del provider ni de la URL: ambos reciben el mismo enlace.

### Por que Android no (media3 1.9.3, fuente real)
- `androidx/media3/extractor/ts/TsExtractor.java` en `MODE_HLS` documenta "Enable single PMT mode, map TrackOutputs by their type (instead of PID)" y hace `int trackId = mode == MODE_HLS ? streamType : elementaryPid;`.
- Con dos audios del mismo `streamType` se queda con **un solo track**, el del **PID mas bajo** (`trackIdToPidScratch`; condicion `elementaryPid < trackIdToPidScratch.get(trackId, MAX_PID_PLUS_ONE)`).
- En estos canales los dos audios son **MPEG-1 Layer II (`streamType 0x04`)**: `eng 0x121` y `spa 0x3f8` -> `0x121` gana y **el espanol se descarta antes de llegar al player**. No hay 2 pistas -> sin boton de pistas y sin forma de cambiar idioma.
- Con un master que SI trae `#EXT-X-MEDIA` (renditions separadas) media3 crea 2 tracks y el boton aparece (caso `187.102.210.46/universo`).

### Cartoon Network Latin America (`/canal/cartoon-network-latin-america/`, post **33301**): 6 opciones sondeadas desde PC
| Opcion | URL | Master | Renditions | PMT del segmento | Veredicto |
|---|---|---|---|---|---|
| 1 | `15.204.246.24:8080/CartoonNetworkHD/index.m3u8` | **403** | - | - | muerta |
| 2 | `45.162.64.114/CARTOON_NETWORK/index.m3u8` | 200 | **2**: `por a1` DEFAULT + `eng a2` | audio `0xdd` AAC `por` + video `0xd3` 0x1B | **portugues + ingles** (feed brasil) -> boton de pistas, pero **sin espanol** |
| 3 | `38.252.238.18:8000/play/a053/index.m3u8` | **ConnectTimeout** | - | - | muerta |
| 4 | `170.244.209.30:8000/play/a027/index.m3u8` | **ConnectTimeout** | - | - | muerta |
| 5 | `158.172.217.18:8000/play/a03a/index.m3u8` | 200 | **0** (sin `#EXT-X-MEDIA`), 1 variante | `eng 0x121` + `spa 0x3f8`, ambos **MP2 0x04**; video 0x02 | **el caso reportado** (eng por el dedupe). Unico con espanol, pero muxado -> inservible in-app |
| 6 | (truncado en la salida) | ? | ? | ? | ? |
- **Los indices de "opcion" rotan por request** (el indice es la posicion entre todos los `atob` del HTML): el usuario la ve como **opcion 8** y en este fetch salio como **5**. Identificar la buena por URL/IP, no por numero.
- **Conclusion**: para CN Latin America **no hay ninguna opcion reproducible con espanol en Android** hoy: la unica con `spa` es la muxada `a03a` (media3 pierde el PID) y la de `#EXT-X-MEDIA` es portugues/ingles.
- Otras entradas CN del sitio: `cartoon-network-latino` (17776), `cartoon-network-live-latin-america`, `cartoon-network-live-latin-america-2` -> page 200 pero **0 opciones** (sin `atob` con `.m3u8`); `cartoon-network-hd` (33299) -> 1 opcion `https://drk6xq0vhn.gpcdn.net/live/cn_hd_abr/index.m3u8` (200, 0 renditions, 1 variante, segmento 883040 B, PMT no parseado: posible fMP4) -> mismo limite, sin pista elegible.

### Salidas posibles
1. **Reproductor externo** (VLC/mpv/MPV-Android): decodifican TS con varias pistas y dejan elegir espanol. Via inmediata, cero codigo.
2. **Proxy-remux local** (estilo `MegaExtractor` de Retrotve): descargar el segmento, reescribir el **PMT** dejando solo el PID `0x3f8` (spa) y servir por `127.0.0.1`. Costoso (CPU, red local, seek) y solo para canales asi; **no implementado** (decision pendiente del usuario).
3. **Studio Universal LA** (si el problema es ese canal): usar la opcion de `187.102.210.46/universo` o `190.93.224.42/STUDIO-UNIVERSAL` (traen `#EXT-X-MEDIA` spa DEFAULT + eng -> 2 tracks y boton de pistas in-app).
- **Nota cs3d**: sirve como referencia de "fuente viva/idiomas", no como prueba del comportamiento en Android.

- **Estado**: sin cambios de codigo; `TeleonlineProvider` queda en **v11** (`build.gradle.kts` + `plugins.json`), `fileSize` **24485** pendiente tras compilar.
- Scripts: `%TEMP%\opencode\tele_cnla.py` (canal completo: page -> `atob` -> master -> variante -> segmento + PMT) y `tele_cn_probe.py` (multi-canal, resumen `(esPid, streamType, langs)`).

---

## TeleonlineProvider - Proxy local TS para audio en espanol (22 Oct 2026 v12)

### Sintoma
- Cartoon Network Latin America (`/canal/cartoon-network-latin-america/`, post 33301) suena en **ingles** en Android y **no aparece el boton de pistas**; en cs3d (libmpv/FFmpeg) si se ve y se puede elegir espanol.
- La opcion buena es `http://158.172.217.18:8000/play/a03a/index.m3u8`: master 200 (134 B), **una sola variante**, **sin `#EXT-X-MEDIA`**; el TS trae dos audios MPEG-1 Layer II (`0x121` eng y `0x3f8` spa).

### Causa raiz (confirmada en `TsExtractor` de media3 1.9.3)
- `MODE_HLS` mapea `trackId = streamType` (no el PID) y el dedupe conserva el **PID mas bajo por `streamType`** -> los dos MP2 (`0x04`) colapsan en un solo track (`eng`, PID menor). El `spa` se descarta **al parsear el PMT**, no al filtrar paquetes.
- Por eso **filtrar solo los paquetes del PID `0x121` NO alcanza**: hay que reescribir el PMT (y el PAT si difiere). Con 1 track de audio, `GeneratorPlayer.onTracksInfoChanged()` no muestra `playerTracksBtt`.

### Arquitectura del fix
- `TeleonlineTsProxy.kt` (nuevo, 604 lineas): servidor HTTP local (`ServerSocket(0, 50, 127.0.0.1)`, threads daemon, solo GET, `Connection: close`, reaper por inactividad `IDLE_MS = 180000L`).
  - `playlistUrl(variantUrl, keepPid)` -> `http://127.0.0.1:{port}/live.m3u8?u=..&p=..`; `servePlaylist` reescribe cada linea no-`#` a `/seg?u=<abs>&v=<variant>&p=<keep>` y re-consulta la variante en cada request (live reload).
  - `serveSegment` -> descarga el segmento original (tope `PROBE_LIMIT` solo en la sonda) y llama `filterTs(data, variant, keepPid)`.
  - `filterTs`: `alignOffset` (3 syncs `0x47`), `findPmt` por escaneo real (`table_id 0x02` + CRC ok, ignora lo que declare el PAT), `findPat`; reconstruye el PMT quitando las ES de audio != `keepPid` (`buildPmtSection`, recalcula `section_length` + CRC MPEG-2) y el PAT apuntando al PMT real (`buildPatSection`); reescribe **todas** las apariciones PSI del segmento con `rewriteSection` (AFC=3, stuffing en adaptation field, CC preservado). Fail-open al `data` original ante cualquier excepcion.
  - Cache `ConcurrentHashMap` con clave **`"$variant|$keepPid"`** (dos links del mismo master con distinto PID no se contaminan).
- `TeleonlineProvider.kt` v12: `emitM3u8Options()` en **dos fases** (los indices de opcion rotan por request, un budget secuencial fallaba):
  - Fase A (paralela, barata): `options.amap { candidateOf(idx, url) }`; master con `#EXT-X-MAP`/`#EXT-X-KEY`, con `#EXT-X-MEDIA:...TYPE=AUDIO` o con >1 `#EXT-X-STREAM-INF` se descarta (se emite el crudo, sin regresion).
  - Fase B (secuencial, `budget = 2`): `firstSegment(variant, 15000L)` + `probeTracks` (exige 2+ ES de audio) -> `ProxyPlan`.
  - Emision: un link por pista, `spa` primero, labels `En Vivo - Opcion N [ESP]` / `[ENG]`; el crudo de esa opcion se omite solo si al menos un proxy salio.
- Timeouts por llamada (`newCall(url, timeoutMs)` -> `Call.timeout()`), no cliente aparte: master 12 s, segmento 15 s, pagina principal 20 s, serving connect 8 s / read 20 s. Peor caso ~54 s (margen amplio vs los 120 s de CS3).
- Sin `getVideoInterceptor`, sin tocar headers globales, `directM3u8` y el branch de iframes intactos.

### Verificacion offline (sin compilar)
- `bal_check.py` -> BALANCE OK en `TeleonlineProvider.kt` y `TeleonlineTsProxy.kt`.
- `tele_psi_dump.py` sobre `a03a`: PMT real en PID `0x20` (43 B), pcr `0x3f3`, ES `0x04/0x121/eng`, `0x02/0x3f3` (video), `0x04/0x3f8/spa`; el PAT declara **tambien `0x20`** (el `0x5E0` de un analisis previo era misparse: `e0 20` -> `0x20`), asi que `fixPat` casi nunca se activa pero queda como salvaguarda.
- `tele_proxy_sim.py` (simulacion fiel del `filterTs` en Python) sobre un segmento real de 432024 B: PMT idx 2, `drop=[0x121]`, 254 paquetes eliminados (delta 47752 = 254*188), salida 384272 B, PMT de salida con ES `[0x3f3, 0x3f8]`, **6 secciones PMT reescritas ok / 0 mal** y 6 PAT con CRC valido -> media3 vera un unico track de audio (`spa`).
- `cn45` (opcion 2 de CN LA) descartada para espanol: sus renditions son `por` (DEFAULT) + `eng`; ademas media3 ya crea 2 tracks ahi (el boton aparece, pero sin espanol).

### Archivos
- `TeleonlineProvider/src/main/kotlin/com/example/TeleonlineTsProxy.kt` (nuevo, sin trackear en git)
- `TeleonlineProvider/src/main/kotlin/com/example/TeleonlineProvider.kt`
- `TeleonlineProvider/build.gradle.kts`: `version = 11` -> **12**
- `plugins.json`: Teleonline `version` 11 -> **12**; `fileSize` **pendiente** (sigue 24485)

### Estado / pendiente de probar en dispositivo
- Compilacion: pendiente por el usuario (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`) - **no compilado por regla del repo**.
- Probar en **celular fisico + VPN** o **cs3d** (en WSA el NAT no enruta `http://IP:puerto`): CN LA debe listar `En Vivo - Opcion N [ESP]` y `[ENG]`; reproducir el `[ESP]` y confirmar audio espanol.
- Logs: `adb logcat -s Teleonline:V TeleonlineProxy:V` -> `opciones=.. candidatas=..`, `opcion N probe ok: 0x121:eng, 0x3f8:spa`, `PAT declara PMT 32, PMT real 32`, y en el proxy `seg ...`/`handle` sin `handle fallo`.
- Si el `[ESP]` sale en ingles, revisar que el segmento tenga el PMT en el mismo PID en todas sus apariciones (el filtro exige `fitsPacket`: secciones >188 B no se reescriben; las de CN LA son de 43 B).
- Opcional sin sondear: opcion 6 de CN LA (puede que el listado rote y aparezcan otras IPs).

### âœ… VERIFICADO en dispositivo (22 Oct 2026)
- El usuario confirma: en Cartoon Network Latin America el listado muestra **En Vivo - Opcion 8 [ESP]** y **[ENG]** (mismo numero, dos pistas), y el [ESP] reproduce con audio en espanol.
- uild.gradle.kts y plugins.json en **version 12**; ileSize sigue **24485** (pendiente de actualizar tras compilar).
- Nota para futuros canales: el proxy solo actua cuando el master NO declara #EXT-X-MEDIA:TYPE=AUDIO y el TS trae 2+ ES de audio (caso muxado). Si el master declara las renditions, media3 ya crea 2 pistas y muestra el boton nativo (caso GloboView), sin necesidad de proxy.

---

## TeleonlineProvider - Enlaces [ESP]/[ENG] intermitentes: cache de CS3 + sondeo paralelo (26 Oct 2026 v13)

### Sintoma (usuario)
- A veces, al entrar a un canal, **no aparecen** los enlaces etiquetados `En Vivo - Opcion N [ESP]` / `[ENG]`; solo sale la opcion plana (sin proxy TS, sin audio en espanol elegible).
- Al **matar la app** y volver a entrar, si aparecen. Es decir: no es el sitio, es estado interno.

### Causa raiz (verificada por bytecode en `cloudstream.jar`)
- El player usa `com.lagradost.cloudstream3.ui.player.RepoLinkGenerator.generateLinks`, que cachea por `Pair(apiName, episodeId)` en `ui.player.Cache.getLinkCache()` (TTL 1200 s) y marca `Cache.setSaturated(!linkCache.isEmpty())` + `setLastCachedTimestamp(...)` **justo despues** de que `loadLinks` retorne.
- Con cache valida y `saturated == true`, CS3 **NO vuelve a llamar a `loadLinks`** durante 20 min. Si la primera visita no logro plan de proxy (solo emitio la opcion plana), ese resultado queda cacheado y persiste hasta matar la app. **El provider no puede invalidar esa cache** -> el fix tiene que acertar en la primera llamada.

### Por que fallaba el plan de proxy en la 1a visita
- El sondeo de candidatas era **secuencial con `budget = 2`**: si las 2 primeras candidatas no eran el TS muxado con 2 ES de audio, no se generaba ningun `ProxyPlan`.
- Los **indices de "opcion" rotan por request** (el indice es la posicion entre todos los `atob` del HTML), asi que "las 2 primeras" cambian entre visitas -> intermitencia.

### Fix v13 (`TeleonlineProvider.kt`, `emitM3u8Options`)
1. **Sondeo en PARALELO de todas las candidatas**: `withTimeoutOrNull(30000L) { candidates.amap { c -> ... probeCandidate(c) ... } }`, con escritura en `synchronized(plans)`, `catch (CancellationException) throw` y `catch (Exception)` con log por opcion. Eliminado `var budget = 2` y el `for` secuencial.
2. **Reintento secuencial** si `plans.isEmpty()`: `Log.w("Teleonline", "sin planes en paralelo, reintento secuencial")` + `withTimeoutOrNull(15000L)` sobre todas las candidatas (red de seguridad para redes lentas).
3. Log final `planes=N/M` para diagnosticar en logcat.
4. **`PROBE_LIMIT` 3 MB -> 768 KB** (`TeleonlineTsProxy.kt`): `firstSegment` solo necesita el PMT (que va al inicio del segmento, ~100-300 KB) y los segmentos pesan ~432 KB-1,5 MB; bajar el tope acelera el probe sin riesgo. `serveSegment` usa `fetchBytes(url, Int.MAX_VALUE)` y **no** depende de esta constante.
- Presupuesto de tiempo: `candidateOf` 12 s + probes 30 s + reintento 15 s + `mainHtml` 20 s ~ 77 s < limite de 120 s de CS3.

### Estado / versiones
- `TeleonlineProvider/build.gradle.kts` **12 -> 13**; `plugins.json` Teleonline **12 -> 13**; `fileSize` **pendiente** (sigue 24485).
- Balance verificado con `dbg_bal.py`: `TeleonlineProvider.kt` 380 lineas, cierre de clase en L380; `emitM3u8Options` L248-358 correctamente cerrado.
- **No compilado por regla del repo.**

### Pendiente (usuario)
- Compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar varias veces seguidas el mismo canal (sin matar la app) en celular fisico + VPN o cs3d.
- Logs: `adb logcat -s Teleonline:V TeleonlineProxy:V` -> debe salir `opciones=N candidatas=M` y `planes=M/M` (no `planes=0`), y el listado debe traer `[ESP]`/`[ENG]` en la primera visita.

---

## DoggitvProvider - Proxy TS local para audio en espanol (26 Oct 2026 v7)

### Sintoma
- Canales IPTV con **doble audio** (TNT Latin America Center y TNT LA Colombia, confirmado) suenan solo en **ingles** y no aparece el boton de pistas, aunque el TS trae `spa`.
- En un reproductor externo (VLC/libmpv) si se puede elegir espanol.

### Causa raiz (confirmada en `TsExtractor` de media3 1.9.3 y con sondeo real del TS)
- `MODE_HLS` mapea `trackId = streamType` (no el PID) y el dedupe conserva el **PID mas bajo por `streamType`** -> los dos audios colapsan en un solo track. En estos canales el PID menor es `eng`, asi que `spa` se descarta al parsear el PMT.
- **TNT LA Center** (`http://45.171.64.30:6060/play/a03i/index.m3u8`): segmento 810844 B, PAT progs `[(669,'0x2b')]`, PMT pid `0x2b` pcr `0x6cb`, ES: **AAC eng `0x6c3`**, AAC spa `0x6c4`, H264 `0x6cb`.
- **TNT LA Colombia** (`http://181.78.211.244:8005/play/a09g/index.m3u8`): segmento 431836 B, PAT progs `[(422,'0x38')]`, PMT pid `0x38` pcr `0x457`, ES: **MP2 eng `0x45a`**, MP2 spa `0xeef`, MPEG2V `0x457`.
- Por eso filtrar paquetes del PID `eng` no alcanza: hay que **reescribir el PMT** (y el PAT si apunta a otro) para que media3 solo vea un audio.

### Fix v7 (`DoggitvProvider.kt` + `DoggitvTsProxy.kt` nuevo)
- `DoggitvTsProxy.kt`: port del `TeleonlineTsProxy` (proxy HTTP local `ServerSocket(0, 50, 127.0.0.1)`, threads daemon, solo GET, `Connection: close`, reaper `IDLE_MS = 180000L`, `PROBE_LIMIT = 768 * 1024`, `TAG = "DoggiProxy"`).
  - `playlistUrl(variantUrl, keepPid)` reescribe la variante a URLs `/seg?u=..&v=..&p=..` locales y re-consulta la variante en cada request (live reload).
  - `serveSegment` descarga el segmento original y llama `filterTs`: `alignOffset` (3 syncs `0x47`), `findPat`/`findPmt` por escaneo real con CRC (ignora lo que declare el PAT), `buildPmtSection` quita las ES de audio != `keepPid` (recalcula `section_length` + CRC MPEG-2), `buildPatSection` apunta al PMT real, `rewriteSection` reescribe todas las apariciones PSI (AFC=3, stuffing en adaptation field, CC preservado). Fail-open al `data` original.
  - Cache `ConcurrentHashMap` con clave `"$variant|$keepPid"` (no se contaminan dos links del mismo master).
- `loadLinks` reescrito: valida `u`; `type` por extension sobre `u.substringBefore('?').lowercase()` (`.mpd` DASH, `.mp4/.mkv/.webm` VIDEO, resto M3U8); descarga el master **una sola vez** (`app.get(u, timeout = 15L)`); si `muxedVariantOf` devuelve variante (master con `#EXT-X-MEDIA:TYPE=AUDIO` y 1 sola `#EXT-X-STREAM-INF`) la emite directo (media3 ya crea 2 pistas); si hay `#EXT-X-MAP`/`#EXT-X-KEY` se salta el probe; si no, `singleVariantOf` + `withTimeoutOrNull(20000L) { firstSegment(variant, 15000L)?.let { probeTracks(it) } }` y con `tracks.size >= 2` emite **un enlace por pista** (`playlistUrl(variant, t.pid)`), orden `sortedBy { langRank(it.lang) }`.
  - Etiquetas: `langRank` (esp/spa=0, en/eng=1, resto=2), `langLabel` (ESP/ENG/AUDIO/`uppercase()`); titulo `"$name [ESP]"`.
  - Helpers nuevos: `emitLink` (suspend, `quality = Qualities.Unknown.value`), `singleVariantOf`, `muxedVariantOf`. Import `kotlinx.coroutines.withTimeoutOrNull`.
  - Fallback fail-open: si no hay plan de proxy se emite el master crudo como M3U8 (comportamiento anterior, nunca deja sin enlaces).
- `withTimeoutOrNull` **no interrumpe** llamadas OkHttp bloqueantes (solo comprueba al volver); los timeouts reales los dan los `call.timeout()` del proxy. Se deja como red de seguridad inofensiva.

### Estado / versiones
- `DoggitvProvider/build.gradle.kts` **6 -> 7**; `plugins.json` DoggiTV **6 -> 7**; `fileSize` **pendiente** (sigue 0).
- **No compilado por regla del repo.**

### Pendiente (usuario)
- Compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar **TNT Latin America Center** (pais Panama/Venezuela) y **TNT LA Colombia**.
- Logs: `adb logcat -s DoggiProxy:V DoggiTV:V` -> debe salir el probe con 2+ pistas y, en el player, dos enlaces `DoggiTV [ESP]` / `DoggiTV [ENG]`; reproducir el `[ESP]` y confirmar audio espanol.
- Si el `[ESP]` sigue en ingles, revisar que el PMT del segmento tenga el mismo PID en todas sus apariciones (el filtro exige `fitsPacket`: secciones >188 B no se reescriben).
- Canales con master que ya declara `#EXT-X-MEDIA:TYPE=AUDIO` no usan el proxy (media3 crea las 2 pistas solo, caso GloboView); el proxy es solo para el caso muxado.

---

## DoggitvProvider - Blindaje del probe: 2 intentos y segmento fresco (26 Oct 2026 v8)

### Motivo
- **Riesgo de cache de CS3**: `RepoLinkGenerator` cachea por `Pair(apiName, episodeId)` (TTL 1200 s) y marca `saturated = !linkCache.isEmpty()` tras `loadLinks`; con cache valida **no se vuelve a llamar a `loadLinks`** durante 20 min. El provider no puede invalidarla.
- Si el probe falla **una sola vez** (segmento caducado, timeout o red), el fallback emite el master crudo y ese resultado queda cacheado -> el canal se queda **sin `[ESP]`/`[ENG]`** hasta matar la app (el mismo sintoma intermitente ya documentado en Teleonline v13).
- **Debilidad del v7**: `firstSegment` cogia el **primer** segmento de la ventana live (el mas viejo, a punto de expirar). El servidor "Astra" responde `Content-Length: 188` (un unico paquete TS nulo, PID `0x1FFF`) para un segmento caducado.

### Fix v8
- `DoggitvTsProxy.kt`: eliminado `firstSegment`; nuevos `segmentUris(text)` (lineas no-`#`, trim, no vacias) y `probeVariantTracks(variantUrl, timeoutMs = 0L)`: descarga la variante, toma los **2 ultimos segmentos** (`takeLast(2).asReversed()`, el ultimo es el mas fresco) y devuelve las pistas del primero que tenga 2+ audios. Logs `probe variant: segmento N/M sin 2+ audios (X B)` y `probeVariantTracks: variante sin segmentos`.
- `DoggitvProvider.kt` (`loadLinks`): bucle `for (attempt in 1..2)` con `tracks = withTimeoutOrNull(40000L) { DoggitvTsProxy.probeVariantTracks(variant, 10000L) }`; sale al obtener 2+ pistas, si no `Log.w(TAG, "probe intento $attempt sin 2+ audios, reintentando")`. El fallback fail-open (master crudo) queda igual.
- Presupuesto peor caso: master 15 s + 2 x (variante 10 s + 2 segmentos x 10 s) = 75 s < limite de 120 s de CS3. `withTimeoutOrNull` no interrumpe OkHttp bloqueante; los timeouts reales los da `call.timeout()`.

### Estado / versiones
- `DoggitvProvider/build.gradle.kts` **7 -> 8**; `plugins.json` DoggiTV **7 -> 8**; `fileSize` **pendiente** (sigue 0). JSON valido (73 entradas).
- Balance verificado con `bal2.py`: `DoggitvTsProxy.kt` (617 lineas) y `DoggitvProvider.kt` (452 lineas) sin desbalances.
- **No compilado por regla del repo.**

### Pendiente (usuario)
- Compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar **TNT Latin America Center** y **TNT LA Colombia** varias veces seguidas **sin matar la app**.
- Logs: `adb logcat -s DoggiProxy:V DoggiTV:V` -> en el caso normal no debe aparecer `probe intento 2`; `probe variant: segmento 1/2 ...` solo si el ultimo segmento fallo. En el player deben salir `DoggiTV [ESP]` / `DoggiTV [ENG]`; reproducir el `[ESP]` y confirmar audio espanol.
- Si sigue en ingles, revisar que el PMT del segmento tenga el mismo PID en todas sus apariciones (el filtro exige `fitsPacket`: secciones >188 B no se reescriben).

---

## DoggitvProvider - Fix 502 al reproducir por segunda vez: variante Astra caducada (26 Oct 2026 v9)

### Sintoma (usuario, log 18:17-18:24)
- `TNT LA Colombia` reproducia bien la **primera** vez (18:18:28) y despues, al volver a entrar o al elegir `[ENG]`, el player daba `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)`.
- En el log: `handle GET /live.m3u8?u=...&p=1114` (y `p=3823`) con URLs **identicas** en todos los reintentos (18:21:41, 18:21:59, 18:22:16, 18:24:51) -> el proxy respondia **502** en el **playlist** (no en el segmento; `ParsingLoadable -> CronetDataSource.open`). Sin `probe intento 2` (el probe si acerto).

### Causa raiz (medida con `curl.exe` UA Chrome/148 desde PC)
- La URL de **variante** del servidor Astra es **rodante y con TTL de segundos**:
  - `master_co 200 134` / `var_co 404 118` (`http://181.78.211.244:8005/play/a09g/92700371.m3u8`)
  - `master_center 200 132` / `var_center 404 118` (`http://45.171.64.30:6060/play/a03i/81692604.m3u8`)
  - El master devuelve un **numero distinto en cada peticion** (`72346376.m3u8`, luego `20170673.m3u8`); el `20170673` pedido al instante dio `200 size=356`.
- El proxy v8 pedia en **cada request** la variante capturada en `loadLinks` (`u`); al caducar -> upstream 404 -> proxy 502 -> CS3 2004. Y como CS3 **cachea los links 1200 s** (`RepoLinkGenerator`), no se vuelve a llamar a `loadLinks` y el canal queda roto hasta matar la app.
- El **master** (`index.m3u8`) es estable; los **segmentos** tambien (URLs absolutas `.../215220481_fd06.ts`), aunque la variante caduque.
- Descartado el reaper como causa (el proxy respondio 502, no "connection refused": el servidor estaba vivo). Aun asi `IDLE_MS` subido a 30 min porque la cache de CS3 (1200 s) puede sobrevivir al proxy.

### Fix v9
- `DoggitvTsProxy.kt`:
  1. `playlistUrl(sourceUrl, keepPid)` ahora recibe el **master** y emite `http://127.0.0.1:<port>/live.m3u8?m=<master>&p=<pid>` (param `m`).
  2. `servePlaylist` resuelve la variante **fresca en cada request**: `source = query["m"] ?: query["u"]` (compat legacy); `fetchText(source)`; si el cuerpo tiene `#EXT-X-STREAM-INF` es master -> primera linea no-`#` (`segmentUris(...).firstOrNull()`), `absolute(source, ref)`, `fetchText(abs)`; **bucle `for (attempt in 1..2)`** que **re-pide el master** (`masterText = fetchText(source)`) si la variante falla (la variante cambia de numero). Si el cuerpo **no** tiene STREAM-INF se asume media playlist y se usa `source` tal cual (listas que apuntan directo a la variante). Si no hay texto -> `502 upstream`. Log final `playlist master=... variant=... keep=... segmentos=N`.
  3. `IDLE_MS` 180000 -> **1800000**.
  4. `filterTs` **fail-open**: si el PMT trae audios pero **ninguno** coincide con `keepPid` (PIDs rotados), log `PMT sin audio <pid> (...), sin filtrar` y devuelve `data` sin tocar (evita silencio).
- `DoggitvProvider.kt` (`loadLinks`): `DoggitvTsProxy.playlistUrl(u, t.pid)` â€” se pasa el **master `u`** de la lista m3u, no la variante; el caso "media playlist" lo cubre el punto 2. `variant` sigue usandose solo en `probeVariantTracks`/logs.

### Estado / versiones
- `DoggitvProvider/build.gradle.kts` **8 -> 9**; `plugins.json` DoggiTV **8 -> 9**; `fileSize` **pendiente** (sigue 0). JSON valido (73 entradas).
- Balance verificado con `bal2.py`: `DoggitvTsProxy.kt` (648 lineas) y `DoggitvProvider.kt` (453 lineas) sin desbalances.
- **No compilado por regla del repo.**

### Pendiente (usuario)
- Compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar **TNT LA Colombia** reproduciendo **dos veces seguidas sin matar la app** (ESP y luego ENG, y volver a entrar al canal).
- Logs: `adb logcat -s DoggiProxy:V DoggiTV:V` -> cada request del playlist debe mostrar `playlist master=... variant=http://181.78.211.244:8005/play/a09g/<numero>.m3u8 ... segmentos=N` con **numero distinto** entre visitas y **sin 502**. Si aparece `variante ... sin respuesta (intento 1)` y luego resuelve, el reintento esta funcionando.
- Limite conocido: si una lista m3u apunta **directamente a la variante** (sin master), v9 no puede refrescarla; queda el fail-open de `filterTs`. Sin caso observado aun.

### ðŸ› Fix compilacion v9: `Argument type mismatch: String? -> String` (26 Oct 2026)
**Sintoma (usuario)**: `DoggitvTsProxy.kt` lineas 239 y 242 no compilan: `Argument type mismatch: actual type is 'String?', but 'String' was expected` (`absolute(variant, l)` y `enc(variant)`).

**Causa raiz (reproducida con el compilador Kotlin 2.3.0 aislado, sin Gradle)**: en `servePlaylist` la linea `val source = query["m"] ?: query["u"]` da `String?`. El guard `if (source.isNullOrBlank()) return` si aplica smart-cast **en contexto de expresion** (`fetchText(source)` compila bien), pero **NO al inferir el tipo de una declaracion sin tipo explicito**: `var variant = source` y `var resolvedUrl = source` quedan tipadas como **`String?`**. Ese `variant` nullable llega a las lineas 239/242 -> los 2 errores. (Verificado: poner `var variant: String = source` solo mueve el error a `variant = resolvedUrl`, porque `resolvedUrl` tambien es `String?`.)

**Fix (1 sola linea)**: `val source = query["m"] ?: query["u"] ?: ""` -> `source: String` de verdad; `if (source.isNullOrBlank())` -> `if (source.isBlank())`. Con eso `variant` y `resolvedUrl` se infieren `String` y desaparecen los 2 errores. La funcion real extraida del archivo compila **sin errores de tipo** (frontend limpio).

**Regla para el repo**: el smart-cast de `isNullOrBlank()`/`== null` protege los **usos en expresion**, no la **inferencia de tipo de `var`/`val` sin anotar**. Cuando una local viene de `query[...] ?: ...` (nullable) y luego se asigna a otra variable, hacerla no-nula en el origen (`?: ""`) o anotar el tipo explicito.

- Sin cambio de version: el fix pertenece a la **v9 todavia no compilada** (`build.gradle.kts` = `plugins.json` = 9, `fileSize` pendiente = 0).
- `bal_check.py` -> BALANCE OK.
- â¸ï¸ **Pendiente (usuario)**: compilar (`.\gradlew.bat :DoggitvProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar TNT LA Colombia (v9 completa).

---

## DailymotionProvider - Plugin nuevo (29 Oct 2026 v1)

### Origen
- Portado del plugin `DailymotionProvider` de `recloudstream/extensions` (upstream `version = 4`, `status = 1`, `isCrossPlatform = true`), adaptado al repo BETA.
- **Cambio deliberado de paquete**: upstream usa `package recloudstream`; aqui va en **`com.example`** (consistente con el resto de modulos del BETA) y el manifest es `<manifest package="com.example"/>`.
- `settings.gradle.kts` **no se toco**: auto-incluye todo directorio de primer nivel con `build.gradle.kts`.

### Que hace
- `getMainPage`: `GET https://api.dailymotion.com/videos?fields=id,title,thumbnail_360_url&limit=26&page=N` -> una fila "Popular" (`newHomePageResponse`).
- `search`: `GET /videos?search={q}&fields=id,title,thumbnail_360_url&limit=26&page=N` con `encodeUri` -> `toNewSearchResponseList()`.
- `load`: `GET /video/{id}?fields=id,title,description,thumbnail_720_url` -> `newMovieLoadResponse(TvType.Movie)` con `movieData = id` (el **ID crudo**, no una URL).
- `loadLinks`: `data` es el **ID**; pide el metadata del player `https://www.dailymotion.com/player/metadata/video/{id}` con UA Chrome/127 + Referer/Origin y emite las `qualities.*` (`.m3u8` como `M3U8`, `.mp4` como `VIDEO`, calidad por `getQualityFromName`), ademas de los subtitulos (`parseSubtitles`); si no se emitio nada cae a `loadExtractor` con el embed y devuelve `emitted > 0`.

### Causa raiz del bug del upstream (0 links)
- El DTO declara `subtitles.data` como `Map<String, SubtitleData>`, pero la API devuelve **`"data": []`** (array vacio) -> `SerializationException` de Jackson en el primer intento, y el fallback tambien falla -> la excepcion sale de `getUrl$suspendImpl`, `loadExtractor` la captura y devuelve **`true` con 0 links** (sintoma "enlaces no encontrados" con `success=true`).
- Verificado desde PC: 5 volcados del metadata (`xbs20xm`) dan `200` y siempre `subtitles = {"enable": false, "data": []}`; descartada la hipotesis de UA/headers (200 con Chrome, OkHttp, ExoPlayerLib y sin UA).
- **Fix**: leer el JSON con `org.json` (sin DTOs), `parseSubtitles` acepta `data` como **JSONArray o JSONObject**, y `videoIdOf` acepta un **ID desnudo** (`^[A-Za-z0-9]{4,}$`) antes de los regex de URL (`[?&]video=`, `/video/`, `/embed/video/`).

### Archivos
- `DailymotionProvider/build.gradle.kts` (v1, `language = "en"`, `tvTypes = listOf("Others")`, `isCrossPlatform = true`).
- `DailymotionProvider/src/main/AndroidManifest.xml`.
- `DailymotionProvider/src/main/kotlin/com/example/DailymotionPlugin.kt` (`registerMainAPI(DailymotionProvider())`).
- `DailymotionProvider/src/main/kotlin/com/example/DailymotionProvider.kt` (provider completo).
- `plugins.json`: entrada `DailymotionProvider`/`Dailymotion` v1 (`fileSize: 0` pendiente; 74 entradas, JSON validado).

### Estado v1
- `bal_check.py` -> BALANCE OK.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :DailymotionProvider:make --console=plain -q`) - **no compilado por regla del repo**.
- â¸ï¸ **Pendiente (usuario)**: compilar, actualizar `fileSize` en `plugins.json`, instalar y probar home / search / detalle / reproduccion. Logs: `adb logcat -s Dailymotion:V`.

---

## TeleonlineProvider - Variante rodante: refresco en el proxy y fail-open (29 Oct 2026 v14)

### Sintoma
- Los enlaces `[ESP]`/`[ENG]` del proxy TS reproducen la primera vez y despues el player da `ERROR_CODE_IO_BAD_HTTP_STATUS (2004)`, sin recuperarse aunque se vuelva a entrar al canal (el canal queda roto hasta matar la app).
- Es el mismo sintoma ya diagnosticado en DoggiTV v9: la **variante** de los restreamers Astra es **rodante y con TTL de segundos** (el master devuelve un numero de variante distinto en cada peticion) y el proxy pedia en cada request la variante capturada en `loadLinks` -> upstream 404 -> proxy 502 -> 2004.

### Causa raiz
1. `ProxyPlan` guardaba solo `variantUrl` (la variante capturada al sondear) y `playlistUrl(variantUrl, keepPid)` emitia `?u=<variante>`. Al caducar la variante, `servePlaylist` devolvia 502 para siempre.
2. La cache de links de CS3 (`RepoLinkGenerator`, TTL 1200 s) impide re-ejecutar `loadLinks`, asi que el 502 queda cacheado 20 min.
3. Ademas, `IDLE_MS = 180000L` (3 min) permitia que el reaper cerrase el `ServerSocket` mientras la cache de CS3 (1200 s) seguia viva; `port` no se resetea en `reaper` -> el link cacheado apuntaba a un puerto muerto.

### Fix v14 (mismo patron que DoggiTV v9)
1. `ProxyPlan(val master: String, val variantUrl: String, val tracks: List<TsTrackInfo>)` -> se guarda el **master**; se construye `ProxyPlan(c.url, c.variant, tracks)` (en `candidateOf`, `c.url` es el master cuando hay `#EXT-X-STREAM-INF`, y `c.url == c.variant` si la opcion ya es media playlist, asi que cubre ambos casos).
2. `TeleonlineTsProxy.playlistUrl(masterUrl, keepPid)` emite `?m=<master>&p=<pid>` (antes `?u=<variante>`); en `emitM3u8Options` la llamada pasa a `playlistUrl(plan.master, t.pid)`.
3. `servePlaylist` reescrito: `val source = query["m"] ?: query["u"] ?: ""` (compat con links viejos), 400 `falta m` si viene vacio; si el cuerpo tiene `#EXT-X-STREAM-INF` se resuelve la variante fresca con **bucle `for (attempt in 1..2)`** re-pidiendo el master entre intentos (`segmentUris(masterText).firstOrNull()` + `absolute(source, ref)`); si no hay texto o no resuelve -> `Log.e("master ... sin variante fresca")` + 502 `upstream`. Nuevo helper `segmentUris(text)` (lineas no vacias que no empiezan por `#`). Log final `playlist master=... variant=... keep=... segmentos=N`.
4. `filterTs` **fail-open**: si el PMT trae audios pero **ninguno** coincide con `keepPid` (PIDs rotados), log `PMT sin audio <pid> (...), sin filtrar` y se devuelve el segmento original sin tocar (evita silencio).
5. `IDLE_MS` 180000 -> **1800000** (30 min, cubre la cache de 1200 s de CS3).

### Archivos
- `TeleonlineProvider/src/main/kotlin/com/example/TeleonlineTsProxy.kt` (`IDLE_MS`, `playlistUrl`, `segmentUris`, `servePlaylist`, `filterTs`).
- `TeleonlineProvider/src/main/kotlin/com/example/TeleonlineProvider.kt` (`ProxyPlan.master`, construccion en `probeCandidate`, `playlistUrl(plan.master, t.pid)`).
- `TeleonlineProvider/build.gradle.kts`: `version = 13` -> **14**.
- `plugins.json`: Teleonline `version` 13 -> **14**; `fileSize` **pendiente** (sigue 24485).

### Estado
- `bal_check.py` -> BALANCE OK; `plugins.json` valido.
- **No compilado por regla del repo.**
- Pendiente (usuario): compilar (`.\gradlew.bat :TeleonlineProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json`, instalar y probar Cartoon Network Latin America (o Studio Universal) reproduciendo **dos veces seguidas sin matar la app** (ESP y luego ENG, y volver a entrar al canal). Logs: `adb logcat -s TeleonlineProxy:V Teleonline:V` -> cada request del playlist debe mostrar `playlist master=... variant=<numero distinto en cada visita> ... segmentos=N` y **sin 502**; si aparece `variante ... sin respuesta (intento 1)` y luego resuelve, el reintento esta funcionando.
- Limite conocido: si una lista apunta **directamente a la variante** (sin master), el refresco no aplica; queda mitigado por el fail-open de `filterTs`.
---

## SoloLatinoProvider â€” "Enlaces no encontrados": Cloudflare bloquea /api/player-url a OkHttp (29 Oct 2026 v34)

### SÃ­ntoma (log del usuario, build v33 instalado)
- `search`/`load` OK (HTTP 200, tÃ­tulo limpio). En `loadLinks`: Sanctum 204 â†’ Page GET 200 â†’ **`2 tokens encontrados`** â†’ `POST /api/player-url HTTP 403, body=<!DOCTYPE html>...<title>Just a moment...` (Ã—2) â†’ `No se pudo parsear respuesta JSON` â†’ **`No se encontraron servidores en la pÃ¡gina.`** â†’ "enlaces no encontrados".
- **No aparece ninguna lÃ­nea `[WV]`** â†’ el build instalado no traÃ­a el fallback WebView (se compilÃ³ despuÃ©s).

### Causa raÃ­z medida en PC (`post_probe.py`, `post_matrix.py`)
Cloudflare protege `/api/player-url` por **huella del cliente**, no por headers:
| Prueba | Resultado |
|---|---|
| POST headers del provider (X-Requested-With + Accept json + X-XSRF-TOKEN + sec-fetch) | **403 challenge** |
| Sin X-Requested-With / sin X-XSRF-TOKEN / Accept text/html / mÃ­nimos (solo UA+Content-Type) / con sec-ch-ua completos | **403 challenge** (todas) |
| `GET /api/player-url?t=...` | **403 challenge** |
| `curl` (Schannel) y Python (OpenSSL) con los mismos headers | **403 challenge** |
| Chrome (CDP) ejecutando el MISMO `fetch` dentro de la pÃ¡gina | **200 â†’ devuelve los 2 servidores** |
- El JS del sitio (`/build/assets/app-Bd31cN3M.js`) hace exactamente `N("POST","/api/player-url",{t: token})` con `Content-Type: application/json`, `X-XSRF-TOKEN` de la cookie, `credentials: same-origin` â†’ nuestro OkHttp ya lo replica al dedillo; **solo falla el stack de red (JA3/TLS)**.
- Los GET de pÃ¡ginas (episode/search/`/f/`) sÃ­ pasan de OkHttp â†’ por eso `search`/`load` funcionan y solo revienta este POST.

### Cadena completa verificada offline antes de tocar el cÃ³digo
1. **kickJs** (el JS que se inyecta al WebView) extraÃ­do del propio `.kt` y ejecutado contra la pÃ¡gina real del episodio â†’ devuelve `["https://player.pelisserieshoy.com/f/tt1726839-1x01","https://embed69.org/f/tt1726839-1x01"]`.
2. Esas 2 URLs responden **200 con player real** (`title=Reproductor` / `EMBED69`), sin challenge.
3. **Rama embed69 simulada en Python** (`e69_sim.py`): `dataLink = [{video_language:"LAT", sortedEmbeds:[vidhide,streamwish,voe]}]` + `POW_CHALLENGE`/`POW_SALT` â†’ PoW `sha256(chal+nonce)` con prefijo `000` (nonce 3774) â†’ clave `sha256(chal+nonce+salt)` â†’ AES-256-CBC â†’ **6 enlaces** (`voe.sx/e/...`, `hglink.to/e/...`, `morencius.com/embed/...` + sus `/download`). `fixHostsLinks` los mapea a `voe.sx` / `streamwish.to` / `vidhidepro.com` â†’ los 3 tienen extractor propio en el provider.
4. `data-lazy-embed` no existe en la pÃ¡gina â†’ `lazyToken = null` (el kick solo resuelve los 2 tokens).

### Fix (v34, `SoloLatinoProvider.kt`)
1. `resolvePlayerUrlsViaWebView()` (ya estaba): si OkHttp recibe 403, carga la pÃ¡gina en un **WebView (Chromium real)** y lanza el mismo POST desde su propio JS â†’ `SlBridge.onUrls(json)`.
2. **kickJs blindado**:
   - recoge `errs` (`tok0 http=403 body=<!DOCTYPE...`) y los manda por el nuevo `SlBridge.onErr` â†’ log `[WV] player-url sin JSON (Cloudflare?): ...` (diagnÃ³stico definitivo en logcat);
   - si no hubo URLs, resetea `window.__slRun` y **reintenta hasta 6 veces** (`window.__slTries < 6`); solo al 6Âº rinde `onUrls('[]')`.
3. `kick()` ahora **sondea cada 1.5s hasta que el bridge complete** (antes solo re-lanzaba si el resultado era `wait`/`err`; con `started` se quedaba esperando sin reintentar y el primer fallo vacÃ­o cerraba el proceso).
- Tiempos: peor caso con WebView fallando â‰ˆ 6 s de reintentos + timeout 45 s â†’ muy por debajo del kill de 120 s de CS3.

### Estado
- **CompilaciÃ³n: pendiente por el usuario** (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`) â€” **no compilado por regla del repo**. Verificado con kotlinc suelto (classpath del proyecto, sin Gradle): **0 errores, 106 clases** en los 3 `.kt` del mÃ³dulo.
- Versiones: `build.gradle.kts` 33 â†’ **34**; `plugins.json` SoloLatino 33 â†’ **34** (`fileSize` **pendiente**, sigue 31000). JSON vÃ¡lido (74 entradas).

### â¸ï¸ Pendiente de probar en dispositivo
- Compilar, actualizar `fileSize`, instalar y abrir un episodio (ej. El Show de los Looney Tunes T1E1). Esperado en `adb logcat -s SoloLatino:V`:
  `[WV] resolviendo 2 tokens en WebView` â†’ `[WV] kick -> started` â†’ `[WV] servidores resueltos: 2 -> [...]` â†’ `BRANCH: embed69.org` â†’ `embed69 - script con dataLink encontrado` â†’ `embed69 - PoW solved` â†’ `embed69 - decrypted: https://voe.sx/...` â†’ `loadLinks FIN total emitidos=N`.
- Si sale `[WV] player-url sin JSON (Cloudflare?): tok0 http=403 body=<!DOCTYPE...` â†’ el WebView tambiÃ©n recibe el challenge: siguiente paso (no implementado) = navegar una vez a la API en el WebView para conseguir `cf_clearance` y reintentar el fetch.
- Si sale `[WV] sin context` â†’ `SoloLatinoPlugin.load` no seteÃ³ `pluginContext`.

### Notas
- El segundo servidor (`player.pelisserieshoy.com/f/...`) cae en la rama genÃ©rica â†’ 0 links; **no hace falta** para emitir porque embed69 ya trae los 3 hosts. Su protocolo serÃ­a `POST /s.php` con `a=1&tok=<const _t>` del script inline â†’ pendiente solo si se quiere ese servidor tambiÃ©n.
- Errores del log que NO son bug: `Unloading plugin / Couldn't find plugin / version=-2147483648` = recarga normal del plugin online.

## build.yml - CI "verde" aunque gradle fallara (30 Oct 2026)

**Sintoma (usuario)**: instalar/actualizar SoloLatino da `Failed to load plugin SoloLatinoProvider: No manifest found`. No era de codigo ni de reproduccion.

**Causa raiz (2)**:
1. En `build.yml`, el loop `./gradlew make ... && break` / `sleep 30` termina en `sleep` (exit 0) -> el paso "Build Plugins" queda **verde aunque los 3 intentos fallaran**. Gradle falla en algun modulo -> ese cs3 no existe -> se copia solo lo que alcanzo a compilar (parcial: A-C de commits viejos) -> rama `builds` congelada sin SoloLatino (y sin medio repo). El clean solo borraba `*.cs3` de top-level -> los 2 `.cs3` anidados (`AnizoneProvider/build/...`, `RetrotveProvider/build/...`) sobrevivian.
2. **Desync de versiones**: `plugins.json` SoloLatino `version: 34` vs `build.gradle.kts` `version = 32` (revert "solo stable") -> la app buscaba actualizar a 34 -> cs3 inexistente -> "No manifest found".

**Fix**:
1. `build.yml`: el build corre con `--continue` (los modulos sanos compilan aunque uno falle); el loop captura el exit real (`if ./gradlew ...; then BUILD_OK=1; break`) -> `GITHUB_ENV`; nuevo paso final "Build status" **falla el run** si `BUILD_OK=0` (cs3 parciales publicados, log rojo). El log del paso muestra `> Task :X:make FAILED` para identificar el modulo culpable. Clean builds folder ahora borra TODO directorio suelto de top-level.
2. `plugins.json`: SoloLatino `version` 34 -> **32** (fileSize 60000 sin cambio). Verificado por diff programatico: 1/74 entradas cambia solo en version; el resto es normalizacion de indentacion de ediciones manuales previas.

**Diagnostico parcial**: los cs3 parciales del CI llegan hasta modulos C -> el modulo que rompe el build empieza por **D** (Dailymotion/Danimados/Doggitv/Donghualife/Doramasyt, sospechosos). Con el yml arreglado, el log del proximo run dira el modulo exacto.

**Estado**: editado y verificado localmente (JSON valido 74 entradas, semantica 1/74). **Sin compilar ni commitear por regla del repo.**

**Pendiente (usuario)**: (1) `git push` (build.yml + plugins.json disparan el run de Actions); (2) revisar el run: si sale ROJO, buscar `FAILED` en "Build Plugins" y arreglar el modulo que reporte; si sale VERDE con BUILD_OK=1, SoloLatino se publica en `builds/SoloLatinoProvider.cs3`; (3) instalar/actualizar en la app -> debe dejar de dar "No manifest found".

---

## SoloLatinoProvider - Restauracion del fix Cloudflare (30 Oct 2026 v35)

**Sintoma (usuario)**: tras el revert "solo stable" (`ca606da4`), loadLinks vuelve a dar **"enlaces no encontrados"**: `POST /api/player-url HTTP 403 (Just a moment...)` -> `No se encontraron servidores` -> sin links. ConAntiForgeryToken: el revert NO era la cura del "No manifest found" (eso era el CI y el desync de versiones) y **borro el fix** de Oct 29 v34.

**Causa raiz**: `ca606da4 "solo stable"` elimino ~201 lineas del provider: cabeceras `sec-fetch-*` + Origin en `apiHeaders`, el tracking de `lazyToken` (POST `/api/player-embed`), y `resolvePlayerUrlsViaWebView()` (resolver tokens via WebView con `credentials:'same-origin'` cuando Cloudflare bloquea el POST a OkHttp por huella TLS). Sin eso, el 403 de `/api/player-url` no tiene fallback.

**Fix (v35)**:
1. `SoloLatinoProvider.kt` restaurado **completo** desde el commit `58442670 "cloudflare en solo"` (`git checkout 58442670 -- ...`), verificado **byte-idÃ©ntico** (SHA256 `8f1bd18a...`, 65758 B). Esa version ya estaba probada: trae el kickJs con reintentos + `SlBridge.onErr` y SIN los helpers defectuosos (`getWebViewCookies`/`parseWebViewCookies`/`VOE_READY_JS` ya retirados en su dia); el fallback WebView de VOE queda retirado como queria la v33.
2. Versiones sincronizadas a **35**: `SoloLatinoProvider/build.gradle.kts` (32 -> 35) y `plugins.json` SoloLatino `version: 35`. El diff de plugins.json incluye de paso la normalizacion de indentacion de 2 entradas mal alineadas (Tvenvivo/Telelibre/SeriesKao/Teleonline: `"url"/"status"/"version"` a 3 espacios -> 2).
3. Nota de checkpoint: el checker de balance `bal3.py` da FALSO MISMATCH en `SoloLatinoProvider.kt` y `Extractors.kt` (raw strings `""""`); la verificacion valida es la identidad por bytes contra `58442670` (y/o kotlinc suelto, ya validado en su dia con 0 errores).

**Estado**: restaurado y verificado por bytes; JSON valido (74 entradas). **Sin compilar ni commitear por regla del repo.**

**Archivos tocados**: `SoloLatinoProvider/src/main/kotlin/com/example/SoloLatinoProvider.kt` (restore), `SoloLatinoProvider/build.gradle.kts` (v35), `plugins.json` (v35), `AGENTS.md` (seccion build.yml + esta), `.github/workflows/build.yml` (del fix CI anterior, mismo push).

**Pendiente (usuario)**: compilar (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`), actualizar `fileSize` en `plugins.json` (aprox 75-80 KB), y `git push` TODO junto (un run de CI trae build.yml + SoloLatino v35). Esperado en el log del run: `Compilando SoloLatinoProvider...` verde y publicacion en `builds/`; en la app: actualizar -> abrir episodio -> `[WV] resolviendo N tokens en WebView` -> `embed69 - PoW solved` -> `loadLinks FIN total emitidos=N`.

---

---

## DailymotionProvider - ensureJarCompatibility falla en CI (30 Oct 2026 v2)

**Sintoma**: run de Actions ROJA con `> Task :DailymotionProvider:ensureJarCompatibility FAILED` (y `:YoutubeProvider:compileDebugKotlin FAILED`, otro modulo). El step "Build status" del build.yml arreglado hace fallar la run correctamente - el CI publica cs3 parciales.

**Causa raiz (decompilado por javap de `EnsureJarCompatibilityTask.class` del plugin recloudstream 81b1d424d2, session 29-30 Oct)**: la task SOLO corre cuando `isCrossPlatform = true`. Ejecuta `jdeps --print-module-deps` sobre el jar del modulo y si la salida contiene `android.` lanza:
`GradleException("The cross-platform JAR file contains Android imports! ... Remove 'isCrossPlatform = true' or remove the Android imports.")`
Nuestro puerto del upstream recloudstream usa el plugin estandar (`DailymotionPlugin : Plugin()` con `import android.content.Context`) -> jdeps ve `android.content` -> fallo garantizado. El otro modulo con `true` (Onetouchtv) pasa por usar el viejo `BasePlugin.load()` SIN imports de android.

**Fix**: `DailymotionProvider/build.gradle.kts`: `version = 1 -> 2` y `isCrossPlatform = true -> false` (repo android-only; el check queda sin efecto). `plugins.json`: Dailymotion `version: 2` (fileSize 0, pendiente tras compilar). El provider (`DailymotionProvider.kt`) no tiene imports de android; solo el Plugin lo necesita y con `false` ya no se le exige pureza.

**Estado**: editado y verificado por `git diff` (2 lineas) + JSON valido (74 entradas). **No compilado por regla del repo.**

**Pendiente (usuario)**: (1) pegar del log de Actions las lineas `e:` del bloque `> Task :YoutubeProvider:compileDebugKotlin FAILED` para diagnosticar el segundo fallo; (2) `git push` todo junto (build.yml fix + SoloLatino v35 + Dailymotion v2). Con Dailymotion reparado, si la run sale VERDE con BUILD_OK=1 se publican los 74 cs3.

---

## SoloLatinoProvider â€” Cloudflare challenge en /api/player-url: fix con 3 fases en WebView (30 Oct 2026 v36)

**Sintoma (log del usuario, build v35)**: search/load OK (200), `2 tokens encontrados`, pero
`POST /api/player-url HTTP 403` con body `Just a moment...` (tanto en OkHttp como en el WebView: `tok0 http=403`, `tok1 http=403`)
â†’ `WebView no devolvio servidores` â†’ **"enlaces no encontrados"**.

### Arquitectura verificada (medido en PC, Chrome real por CDP + Python)
- La pagina del episodio trae 2 botones con `data-player-token` (Laravel encryptado) y `<meta name="csrf-token">`.
- `POST /api/player-url` con JSON `{t: token}` + header `X-XSRF-TOKEN` (cookie XSRF-TOKEN decodificada) â†’ **200 `{"url":"https://player.pelisserieshoy.com/f/tt...","type":"iframe"}`**.
- El backend TAMBIEN acepta `Content-Type: application/x-www-form-urlencoded` con `t=...` (misma respuesta).
- **El challenge NO depende de headers**: probados sin/con `sec-ch-ua*` completos, `sec-fetch-*`, `X-Requested-With`, `Accept` html, `Accept-Encoding: identity`, y con **HTTP/2 (httpx)** â†’ siempre 403 `Cf-Mitigated: challenge`, challenge JS **sin** form/hcaptcha/turnstile.
- **Solo Python/OkHttp recibe 403; Chrome real pasa siempre** (con cualquier set de headers) â†’ discriminacion por **fingerprint TLS (JA3) del cliente**, no por headers, cookies ni version HTTP. Por eso el POST de OkHttp del provider **nunca** va a pasar.
- En Chrome el 403 no aparece; el unico fallo de Chrome sin el header es `419 CSRF token mismatch` (falta `X-XSRF-TOKEN`).
- Descartado: navegacion POST con form (Cloudflare la deja pasar, pero Laravel devuelve **419 PAGE EXPIRED** aunque se mande `_token` del meta csrf-token â†’ en navegacion no se puede enviar el header `X-XSRF-TOKEN` y el `_token` del body no valida).

### Causa raiz
Cloudflare challengea `/api/*` de sololatino.net. El WebView del plugin (Chromium) hace `fetch` y **un fetch nunca ejecuta el challenge** (solo lo resuelven las navegaciones de documento) â†’ 403 perpetuo. Ademas el plugin setea UA Windows mientras los `sec-ch-ua-platform` del Android WebView siguen diciendo "Android" (spoof detectable).

### Fix (v36, `SoloLatinoProvider.kt`, funcion `resolvePlayerUrlsViaWebView`)
Cascada de 3 fases (timeout total 60000ms):
1. **Fase 0 â€” cf_clearance**: navegar primero a `$mainUrl/api/player-url` (GET). Si `document.title` contiene "Just a moment" se recarga cada 800ms (max 12 veces) hasta que `CookieManager` tenga `cf_clearance`; si no hay challenge, pasa al instante. Watchdog de 12s.
2. **Fase 1 â€” fetch**: como antes (JSON + `X-XSRF-TOKEN` desde cookie), con `__slTries` subido de 6 a 12 (~18s de reintentos) y `lastErr` capturado para el log.
3. **Fase 2 â€” UA Android**: si la fase 1 devuelve 0 URLs, se cambia el `userAgentString` a Chrome Android (sin `; wv`) y se recarga la pagina (client hints coherentes con el UA) â†’ ultimo reintento.
- Import nuevo: `android.webkit.CookieManager`. Logs: `[WV] pagina cargada (fase N)`, `[WV] fase API: challenge=.. cf_clearance=..`, `[WV] kick(fase N)`, `[WV] 0 URLs en fase N (ultimo error: ...) -> reintentando con UA Android`.

### Estado / versiones
- `SoloLatinoProvider/build.gradle.kts` **35 -> 36**; `plugins.json` SoloLatino **35 -> 36** (`fileSize` sigue 60000, pendiente tras compilar).
- Balance verificado con checker que respeta raw strings: OK (los 2 avisos de L1258/1264 son el falso positivo preexistente de los regex `""""(hls\d)..."`).
- **No compilado por regla del repo.**

### Pendiente (usuario)
- Compilar (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar un episodio (ej. Looney Tunes T1E1) con `adb logcat -s SoloLatino:V`:
  - Esperado: `[WV] fase API: challenge=.. cf_clearance=..` â†’ `[WV] servidores resueltos: 2 -> [...]` â†’ `BRANCH: embed69.org` â†’ `loadLinks FIN`.
  - Si sigue en 403: mirar si `cf_clearance=false` (fase 0 no obtuvo cookie â†’ el challenge no es resoluble en navegacion) y si la fase 2 (`reintentando con UA Android`) llega a emitir.
- Scripts de diagnostico en `$TEMP/opencode/`: `cf_cdp_test.py` (POST real en Chrome), `cf_matrix.py` (matriz de headers en Chrome), `cf_form.py`/`cf_formtok.py`/`cf_formnav.py` (form-urlencoded y navegacion POST â†’ 419).

## TvguatemalaProvider â€” Estado (31 Oct 2026 v1)

### âœ… Implementado
- getMainPage(): home con secciones por colecciÃ³n (h2 + tarjetas div.single-video, [href*=/livetv/details/] con 	itle/img). Soporta paginaciÃ³n (page > 1 â†’ null).
- search(): filtra las tarjetas del home por tÃ­tulo (fold sin acentos, distinctBy url, tope 40).
- load(): ficha de canal /livetv/watch/<slug>/<id> (sigue 301 desde /details/); tÃ­tulo de og:title/h1, poster de og:image; TvSeriesLoadResponse con 1 episodio "En Vivo".
- loadLinks(): extrae .m3u8 del <video src>/<source src> o texto del body; probe barato (pp.get, 8 s, requiere 200 + #EXTM3U); emite M3U8 con quality = Unknown. Devuelve alse sin lanzar si no hay enlace vÃ¡lido.
- Sin WebView, sin interceptor de video (los streams m3u8 no requieren headers especiales; verificado guastream.com:3091 200 sin Referer/UA).

### Archivos
- TvguatemalaProvider/build.gradle.kts (v1, es, Live, status = 1)
- TvguatemalaProvider/src/main/AndroidManifest.xml (package="com.example")
- TvguatemalaProvider/src/main/kotlin/com/example/TvguatemalaPlugin.kt (pluginContext + egisterMainAPI)
- TvguatemalaProvider/src/main/kotlin/com/example/TvguatemalaProvider.kt (provider completo)
- plugins.json: entrada TvguatemalaEnvivo/TvguatemalaProvider v1, ileSize: 0 pendiente.

### CompilaciÃ³n
- Comando:  .\gradlew.bat :TvguatemalaProvider:make --console=plain -q â€” **no ejecutado por regla del repo.**
- â¸ï¸ Pendiente: compilar, actualizar ileSize en plugins.json, instalar y probar en dispositivo: home (secciones por colecciÃ³n), search, abrir canal y reproducciÃ³n del m3u8. Logs: db logcat -s TvguatemalaEnvivo:V.

## CxtvProvider â€” Estado (31 Oct 2026 v1)

### âœ… Implementado
- getMainPage(): filas por secciÃ³n â€” Lo mÃ¡s destacado, CategorÃ­as, PaÃ­ses, Top 50 â€” parseadas del home /tv y /tv/paises.
- search(): usa /busqueda/?query= del sitio (GET con parÃ¡metro query), filtra por tÃ­tulo con fold de acentos, tope 40.
- load(): detecta canal individual (/tv-en-vivo/<slug>) â†’ 1 episodio "En Vivo"; o secciÃ³n (paÃ­s/categorÃ­a/top/idioma) â†’ lista de canales como episodios.
- loadLinks(): extrae data-stream-url del <video id="my-video"> (HLS directo); probe barato (pp.get, 8 s, requiere 200 + #EXTM3U); emite M3U8 con quality = Unknown. Devuelve alse sin lanzar si no hay stream.
- Stream verificado en PC: playlist.m3u8 â†’ 200, master con 1 variante 720p; chunklist_*.m3u8 â†’ 200.
- Sin WebView, sin interceptor de video.

### Archivos
- CxtvProvider/build.gradle.kts (v1, es, Live, status = 1)
- CxtvProvider/src/main/AndroidManifest.xml (package="com.example")
- CxtvProvider/src/main/kotlin/com/example/CxtvPlugin.kt (pluginContext + egisterMainAPI)
- CxtvProvider/src/main/kotlin/com/example/CxtvProvider.kt (provider completo)
- plugins.json: entrada CxtvEnvivo/CxtvProvider v1, ileSize: 0 pendiente.

### CompilaciÃ³n
- Comando:  .\gradlew.bat :CxtvProvider:make --console=plain -q â€” **no ejecutado por regla del repo.**
- â¸ï¸ Pendiente: compilar, actualizar ileSize en plugins.json, instalar y probar en dispositivo: home (filas por secciÃ³n), search, abrir paÃ­s/categorÃ­a/canal, reproducciÃ³n del m3u8. Logs: db logcat -s CxtvEnvivo:V.

## CxtvProvider â€” Limpieza de tÃ­tulos y secciones por paÃ­s (1 Nov 2026 v2)
- `getMainPage()` ahora toma el directorio `/tv/paises`, visita cada pÃ¡gina de paÃ­s y crea una fila independiente con los canales de ese paÃ­s, siguiendo el modelo de DoggiTV. Se conserva â€œLo mÃ¡s destacadoâ€ y â€œTop 50â€; se quitÃ³ la fila agregada de categorÃ­as/paÃ­ses.
- `cleanChannelTitle()` se aplica a los nombres de canales extraÃ­dos de tarjetas y al tÃ­tulo del detalle. Recorta `| cxtv`, frases SEO como â€œver online gratisâ€ y el sufijo de ciudad/paÃ­s; en el ejemplo queda â€œLa Teleâ€. El detalle prioriza el `<h1>` y usa `og:title` como respaldo.
- `CxtvProvider/build.gradle.kts` y `plugins.json` actualizados de v1 a v2; `fileSize` sigue pendiente de generar el artefacto.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :CxtvProvider:make --console=plain -q`.
- Pendiente probar en dispositivo: filas separadas por paÃ­s y nombres limpios en bÃºsqueda/detalle/reproductor. La carga del inicio ahora consulta la pÃ¡gina de cada paÃ­s listado y puede tardar mÃ¡s si el directorio contiene muchos paÃ­ses.

## CxtvProvider â€” Diagnostico de inicio vacio (1 Nov 2026 v3)
- Se agregaron logs en `getMainPage()` para page/request, cantidad de tarjetas destacadas, enlaces detectados y parseados del directorio de paises, resultado por pagina de pais, Top 50 y resumen final de filas/canales.
- `fetchDoc()` registra status HTTP, longitud de respuesta y URL para distinguir fallos HTTP/HTML vacio de selectores que no coinciden.
- Versiones actualizadas a v3 en `CxtvProvider/build.gradle.kts` y `plugins.json`; `fileSize` sigue pendiente.
- Compilacion no ejecutada por regla del repo. Comando: ` .\gradlew.bat :CxtvProvider:make --console=plain -q`.
- Pendiente instalar y compartir logs `CxtvEnvivo` desde el inicio de `getMainPage` hasta `FIN` para ajustar el selector/flujo segun lo que entregue realmente el sitio.

## CxtvProvider â€” Fix parseo del inicio a partir de logs (1 Nov 2026 v4)
- Los logs v3 mostraron HTTP 200 en todas las pÃ¡ginas, pero 0 canales parseados. Causa: destacados/Top 50 enviaban contenedores `div.listagem-cards` al parser que lee enlaces `a`; las pÃ¡ginas por paÃ­s sÃ­ tenÃ­an rutas `/tv-en-vivo/`, pero `data-item_name` vacÃ­o bloqueaba el fallback al texto visible.
- Corregido: las consultas de destacados/Top 50 apuntan a enlaces de canal; la extracciÃ³n de nombre ignora atributos vacÃ­os y continÃºa con el texto; el nombre del paÃ­s elimina el contador final. AÃ±adido log de diagnÃ³stico para tarjetas paÃ­s sin resultado.
- `CxtvProvider/build.gradle.kts` y entrada exacta de `CxtvProvider` en `plugins.json` actualizados a v4. `plugins.json` validado; `fileSize` pendiente.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :CxtvProvider:make --console=plain -q`.
- Pendiente instalar y revisar que `getMainPage FIN` muestre filas/canales por paÃ­s y que destacados/Top 50 ya no estÃ©n vacÃ­os.

## CxtvProvider â€” Paises seleccionables en portada (1 Nov 2026 v5)
- Se aÃ±adiÃ³ `homeCountrySlugs` como lista editable en `CxtvProvider.kt`; `getMainPage()` solo consulta y muestra los paÃ­ses incluidos. La selecciÃ³n predeterminada incluye 22 paÃ­ses de LatinoamÃ©rica, EspaÃ±a y Estados Unidos, reduciendo las peticiones frente a las 98 pÃ¡ginas del directorio.
- El filtro usa el slug de `/tv/paises/<slug>` y el log informa paÃ­ses encontrados/seleccionados. Para cambiar el conjunto, editar Ãºnicamente `homeCountrySlugs`.
- Versiones v5 en `CxtvProvider/build.gradle.kts` y entrada del provider en `plugins.json`; `fileSize` pendiente. CompilaciÃ³n no ejecutada. Comando: ` .\gradlew.bat :CxtvProvider:make --console=plain -q`.
- Pendiente probar v5 en dispositivo y confirmar filas y tiempos de carga.

## SoloLatinoProvider â€” fallback WebView de Embed69 (3 Nov 2026 v38)
- Los logs del dispositivo (v37) confirman que el WebView sÃ­ resuelve los dos tokens de `/api/player-url`. El servidor `player.pelisserieshoy.com` responde 200 pero no emite; `embed69.org` devuelve HTTP 502 con pÃ¡gina de Cloudflare sin `dataLink`; el resultado es `total emitidos=0`.
- Se aÃ±adiÃ³ recuperaciÃ³n solo en la rama Embed69: si falta `dataLink` y el GET nativo devuelve 502 o el challenge `Just a moment`, renderizar la URL con `renderViaWebView()` esperando `dataLink`, `POW_CHALLENGE` y `POW_SALT`. El HTML renderizado vuelve a pasar por el parseo existente y sigue PoW â†’ AES â†’ extractores.
- `SoloLatinoProvider/build.gradle.kts` y `plugins.json` pasan de v37 a v38; `fileSize` pendiente hasta generar el artefacto.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SoloLatinoProvider:make --console=plain -q`.
- Pendiente probar v38 en Looney Tunes T1E1. Esperado: `embed69 - respuesta bloqueada`, luego `WebView HTML ... dataLink=true`, `PoW solved` y `loadLinks EMIT`. Si WebView tambiÃ©n queda en 502, la fuente sigue bloqueada en esa red.
## SoloLatinoProvider â€” fallback WebView para VidHidePro (3 Nov 2026 v39)
- Los logs de Looney Tunes T1E1 y El increÃ­ble mundo de Gumball T1E1 muestran que la reproducciÃ³n sÃ­ funciona por VOE y StreamWish: se emiten enlaces y el interceptor recibe HTTP 200 para el manifiesto y segmentos. VidHidePro (`morencius.com` â†’ `vidhidepro.com/embed/...`) falla con `SSLHandshakeException: connection closed`/timeout; `loadExtractor` tampoco emite.
- Se aÃ±adiÃ³ un fallback solo para VidHidePro: si el GET nativo no responde con 2xx o no se puede desempaquetar el player, renderizar `/embed/...` con `renderViaWebView()`, esperar marcadores HLS y volver a desempaquetar el HTML. Si tampoco funciona, permanecen los fallbacks existentes y los otros servidores no se bloquean.
- `SoloLatinoProvider/build.gradle.kts` y `plugins.json` pasan de v38 a v39; `fileSize` pendiente hasta generar el artefacto.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :SoloLatinoProvider:make --console=plain -q`.
- Pendiente probar v39 en los dos episodios: buscar `[VH-Pro] GET nativo fallÃ³`, `[VH-Pro] WebView len=..., unpacked=true` y `[VH-Pro] emitted N variants`. Si ambos perfiles de red fallan, VOE/StreamWish siguen siendo enlaces vÃ¡lidos; el fallo queda limitado al host VidHidePro.
- Resultado en dispositivo v39 (3 Nov, Looney Tunes T1E1): el GET nativo termina en timeout/handshake TLS cerrado; el fallback WebView devuelve `len=0, unpacked=false`; el `loadExtractor` genÃ©rico tambiÃ©n acaba en timeout. Por tanto VidHidePro no llega a exponer HTML para parsear en esta ejecuciÃ³n. Voe emitiÃ³ HLS/MP4 y StreamWish emitiÃ³ HLS; sus manifiestos y segmentos respondieron HTTP 200. La evidencia apunta a conectividad/acceso al host VidHidePro desde el dispositivo o su red, no a que falte un patrÃ³n de extracciÃ³n. No hay fix de parser que pueda recuperar una pÃ¡gina que ambos clientes no logran cargar.
- Fallback de dominio v40 (3 Nov): Embed69 entrega primero dominios espejo como `morencius.com`, pero el flujo normal los reescribÃ­a a `vidhidepro.com` antes de abrir el player. Ahora SoloLatino intenta el host espejo original y, si no resuelve, reintenta el canÃ³nico. Se reconocen los espejos VidHide conocidos en la rama propia y las URLs relativas usan el origen del host que respondiÃ³. Versiones v40 en Gradle y `plugins.json`; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo: ` .\gradlew.bat :SoloLatinoProvider:make --console=plain -q`. Resultado en logs de dispositivo: para Gumball y Looney Tunes, `morencius.com` responde HTTP 200, el JS se desempaqueta y se emiten variantes HLS; pruebas de manifiesto/segmentos visibles confirman playback funcional. En Gumball el CDN `hls2` y la URL `hls4` tambiÃ©n se emitieron, asÃ­ que no se puede afirmar que ambas variantes funcionen solo por el probe.

## DanimadosProvider â€” omitir variante VidHide redirigida (3 Nov 2026 v14)
- Los logs del dispositivo muestran que VidHide entrega `hls2` en un CDN (`dramiyos-cdn.com`/`acek-cdn.com`) con probe HTTP 200, y `hls4` como `/stream/...` en `vidhidepro.com` con probe HTTP 301. Al reproducir, CloudStream falla solo en `hls4` por timeout/`SSLHandshakeException` al conectar a `vidhidepro.com` (IP 2.59.170.20); Voe/StreamWish sÃ­ alcanzan sus CDN.
- Causa: el probe deshabilita redirects pero aceptaba cualquier respuesta 200â€“399 como reproducible, asÃ­ que emitÃ­a como HLS la URL `/stream/...` que devuelve 301 y no el manifiesto CDN. Cambiado a aceptar Ãºnicamente 2xx cuando selecciona variantes; si `hls2` devuelve 200, se emite esa variante CDN y se descarta el `hls4` 301.
- `DanimadosProvider/build.gradle.kts` y `plugins.json` pasan de v13 a v14; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :DanimadosProvider:make --console=plain -q`.
- Pendiente compilar e instalar v14, y probar la opciÃ³n `Danimados - VidHidePro - hls2`. Los logs deben mostrar `probe hls2 -> 200`, no emitir `hls4 -> 301`, y despuÃ©s confirmar HTTP 200 para manifiesto/segmentos del CDN. No se modificaron Voe ni StreamWish.

## DanimadosProvider â€” resolver y conservar hls4 (3 Nov 2026 v15)
- El usuario reporta que hls4 suele ser mÃ¡s fluido y hls2 se congela. Se revierte la decisiÃ³n de v14 de omitir cualquier 3xx: el extractor ahora lee `Location` de la respuesta, resuelve la URL absoluta y prueba el destino. Si el destino responde 2xx, emite la URL CDN final con el nombre de variante hls4. Si no se puede validar, conserva tambiÃ©n la URL 3xx original como opciÃ³n seleccionable, junto a variantes 2xx como hls2.
- Corregido `Origin` de las solicitudes VidHide para que contenga solo protocolo y host, sin el path `/embed`.
- Versiones v15 en `DanimadosProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente. CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :DanimadosProvider:make --console=plain -q`.
- Pendiente instalar y probar v15. Revisar `[VH-Pro] probe hls4 redirect -> 200`; comparar fluidez de hls4 redirigido y hls2 con el mismo episodio. Si el destino no es accesible, la URL original queda visible para selecciÃ³n manual, aunque su reproducciÃ³n dependerÃ¡ del acceso de red a VidHide.

## DanimadosProvider â€” usar el host espejo como base de hls4 (3 Nov 2026 v16)
- ComparaciÃ³n con SoloLatino: ambos desempaquetan VidHide igual, pero SoloLatino resuelve `/stream/...` con el host original (`morencius.com`), mientras Danimados anteponÃ­a `vidhidepro.com`. Los logs Danimados confirman que la pÃ¡gina fuente `minochinos.com` responde 200 pero el reproductor falla al conectar a `vidhidepro.com` por TLS; SoloLatino obtiene streams bajo el host espejo.
- Corregido el armado de rutas relativas para usar el origen de la pÃ¡gina espejo (`minochinos.com`, `morencius.com`, etc.), manteniendo la resoluciÃ³n de redirecciones y el fallback seleccionable de v15. Versiones v16 en Gradle y `plugins.json`; `fileSize` pendiente.
- CompilaciÃ³n no ejecutada por regla del repo. Comando: ` .\gradlew.bat :DanimadosProvider:make --console=plain -q`. Pendiente instalar v16 y revisar que hls4 salga como `https://minochinos.com/stream/...` y cargue manifiesto/segmentos; comparar con hls2 en fluidez.

### LacartoonsProvider â€” extracciÃ³n propia para OK.ru (5 Nov 2026 v26)
- **Log del usuario, Los Campeones de Disney**: el episodio `/serie/capitulo/20164?t=1` entrega `https://ok.ru/videoembed/1828324182601`. `loadExtractor` reporta `matched=true`, pero el callback produce cero links; por eso la pÃ¡gina del episodio se parsea bien y el fallo estÃ¡ en el extractor OK.ru global.
- **Causa/fix**: LaCartoons ahora detecta host `ok.ru` y procesa el iframe directamente: lee `[data-options]`, parsea `flashvars.metadata`, emite `hlsManifestUrl` como HLS y, si falta, emite `videos[]` progresivos con calidades. El patrÃ³n estÃ¡ basado en `tryOkruDanimados`, que ya funciona con fuentes OK.ru en el repo. AÃ±ade logs de status/longitud/presencia de metadata y conteos, sin imprimir URLs completas.
- Versiones a **26** en `LacartoonsProvider/build.gradle.kts` y `plugins.json`; `fileSize` pendiente de regenerar el artefacto. Gradle no ejecutado por regla del repo. Comando: ` .\gradlew.bat :LacartoonsProvider:make --console=plain -q`.
- **Pendiente en dispositivo**: instalar v26 y probar Los Campeones de Disney T1E1; esperar `[OK.ru] ... HLS emitido` o `progresivos emitidos=N`, luego confirmar playback. Si metadata estÃ¡ ausente, compartir las lÃ­neas `[OK.ru]` para decidir si el reproductor cambiÃ³ de formato.

## VeranimeProvider â€” Fix msp-data decoding (07 Nov 2026 v9)

### SÃ­ntoma
- Los episodios de darkanimes.com (ej. One Punch Man S2E12) devolvÃ­an "enlaces no encontrados" porque el hub https://darkanimes.com/one-punch-man-2-12/ usa el plugin **multiserver-pro**, cuyos servidores estÃ¡n ocultos en atributos data-url="..." codificados en **base64 â†’ XOR** con la clave pk del JSON embebido en <script id="msp-data">.

### Causa raÃ­z
- El provider buscaba enlaces directos .m3u8/.mp4 e iframes en el HTML del hub, pero multiserver-pro no expone URLs en claro: cada data-url es una cadena base64 que al decodificar da bytes, y cada byte se XOR-ea con pk[i % pk.length] (pk = clave del JSON msp-data, ej. "13c2e890f443").
- Ejemplo verificado: WUcXQhYCFh8AXVhWXVoMXBYWTV8WG0IcWwBTQQ5WUwccXldF con pk 13c2e890f443 â†’ https://filelions.top/v/j30sknj7zjcv.

### Fix
- AÃ±adido **Intento 0** en loadLinks: extrae pk del JSON msp-data con regex "pk"\s*:\s*"([^"]+)", busca todos los data-url="..." (distinct), los decodifica con decodeMspUrl() (base64 decode + XOR byte a byte con pk) y los pasa por loadExtractorCollect con ixMirrorHost().
- Nueva funciÃ³n decodeMspUrl(encoded, pk): String? (helper privado).
- Si algÃºn data-url no decodifica, se loguea y se omite (fail-open). Si no se encontraron data-url, se continÃºa con los intentos anteriores (directos + iframes).

### Archivos tocados
- VeranimeProvider/src/main/kotlin/com/example/VeranimeProvider.kt (decodeMspUrl + Intento 0 en loadLinks)
- VeranimeProvider/build.gradle.kts (version 8 â†’ 9)
- plugins.json (VeranimeProvider version 8 â†’ 9, fileSize pendiente)

### Estado
- No compilado por regla del repo. Comando: .\gradlew.bat :VeranimeProvider:make --console=plain -q
- â¸ï¸ Pendiente: compilar, actualizar fileSize, instalar y probar. Logs esperados: loadLinks msp-data pk=..., loadLinks data-url decodificado: https://..., loadLinks ... -> OK.

## VeranimeProvider â€” Idiomas en enlaces MSP (07 Nov 2026 v10)

### SÃ­ntoma
- Los enlaces decodificados del msp-data no mostraban el idioma (Latino/Castellano/Subtitulado). El usuario pedÃ­a saber quÃ© idioma es cada enlace.

### Fix
- El bloque "Intento 0" ahora usa Jsoup (hubDoc.select("[data-url]")) para parsear cada elemento con data-url y extrae data-lang del contenedor padre (ul).
- Nueva funciÃ³n langIdToName(langId): String mapea IDs de idioma: 1â†’Latino, 2â†’Castellano, 3â†’Subtitulado, 4â†’JaponÃ©s, 5â†’InglÃ©s.
- Cada enlace se emite con label " [] MSP" (ej. MULTISERVER [Latino] MSP).
- Si el idioma no se reconoce, se usa el label sin idioma ($optTitle MSP).

### Archivos tocados
- VeranimeProvider/src/main/kotlin/com/example/VeranimeProvider.kt (Jsoup parsing + langIdToName en Intento 0)
- VeranimeProvider/build.gradle.kts (version 9 â†’ 10)
- plugins.json (VeranimeProvider version 9 â†’ 10, fileSize pendiente)
- AGENTS.md (esta secciÃ³n)

### Estado
- No compilado por regla del repo. Comando: .\gradlew.bat :VeranimeProvider:make --console=plain -q
- â¸ï¸ Pendiente: compilar, actualizar fileSize, instalar y probar. Logs esperados: loadLinks msp-data decodificado: https://... (Latino), loadLinks ... -> OK.

## VeranimeProvider â€” Idiomas + Byse para Filemoon (07 Nov 2026 v11)

### SÃ­ntoma
- Los idiomas mostraban vacÃ­os () porque elem.parent() buscaba en li (padre directo del botÃ³n con data-url), pero data-lang estÃ¡ en ul (abuelo).
- Filemoon no emitÃ­a enlaces: Intento 0 llamaba a loadExtractorCollect directo sin pasar por emitByse, que es el que usa ByseHttpExtractor para servers tipo Byse (filemoon, bysekoze, etc.).

### Fix
- **Idiomas**: elem.parents().firstOrNull { it.hasAttr("data-lang") } busca en todos los ancestros hasta encontrar ul[data-lang]. Luego langIdToName() mapea 1â†’Latino, 2â†’Castellano, 3â†’Subtitulado, 4â†’JaponÃ©s, 5â†’InglÃ©s.
- **Filemoon/Byse**: Antes de loadExtractorCollect, se verifica si el host contiene ilemoon o yse; si es asÃ­, se llama primero a emitByse() (que usa ByseHttpExtractor). Si falla, cae a loadExtractorCollect como fallback.

### Archivos tocados
- VeranimeProvider/src/main/kotlin/com/example/VeranimeProvider.kt (Intento 0: elem.parents() + emitByse para filemoon/byse)
- VeranimeProvider/build.gradle.kts (version 10 â†’ 11)
- plugins.json (VeranimeProvider version 10 â†’ 11, fileSize pendiente)
- AGENTS.md (esta secciÃ³n)

### Estado
- No compilado por regla del repo. Comando: .\gradlew.bat :VeranimeProvider:make --console=plain -q
- â¸ï¸ Pendiente: compilar, actualizar fileSize, instalar y probar. Logs esperados: loadLinks msp-data decodificado: https://filemooon.link/... (Latino), yse sources=N, loadLinks ... -> OK.

## ElChavo8TVProvider â€” Plugin nuevo (07 Nov 2026 v1)

### Sitio
- https://elchavodel8tv.com/ â€” WordPress con capÃ­tulos de El Chavo del 8, ChapulÃ­n Colorado, Chespirito, Kiko, etc.

### Arquitectura
| Componente | PatrÃ³n | ImplementaciÃ³n |
|---|---|---|
| Home | PÃ¡ginas de serie WordPress | getMainPage devuelve filas por serie con enlaces a temporadas |
| Temporadas | /temporada-1979-el-chavo-del-8-latino/ | load parsea enlaces javascript:ServidorXChespTV(id) |
| Episodios | Cada capÃ­tulo tiene OpciÃ³n 1 y OpciÃ³n 2 | load crea EpisodeItem con fragmento #ep{id} |
| ReproducciÃ³n | Servidor1ChespTV(id) / Servidor2ChespTV(id) arrays JS inline | loadLinks parsea urls[] del HTML, indexa por epId, construye URL haz-clic-para-reproducirv2/?UrlPok=<encoded>&tipo=<tipo> |

### Flujo de loadLinks
1. Obtiene HTML del episodio/temporada
2. Extrae enlaces [href^='javascript:Servidor'] â†’ obtiene serverNum y epId
3. Parsea el array urls[] del script JS correspondiente
4. Accede a urls[epId] para obtener rchivo.mp4&tipo=chespirito
5. Construye https://elchavodel8tv.com/haz-clic-para-reproducirv2/?UrlPok=<urlencode>&tipo=...
6. Emite como ExtractorLink tipo VIDEO con referer = pÃ¡gina del episodio

### Limitaciones conocidas
- El redirect haz-clic-para-reproducirv2/ puede no resolver a un video directo desde pp.get (Cloudflare/JS). Si el link emitido no reproduce, puede requerir WebView para seguir el redirect.
- Se enfoca en Servidor 1 y 2 (ChespTV/Chespirito). Servidor Kiko estÃ¡ incluido pero son videos extras.

### Archivos
- ElChavo8TVProvider/build.gradle.kts (v1, es, TvSeries+Cartoon)
- ElChavo8TVProvider/src/main/AndroidManifest.xml
- ElChavo8TVProvider/src/main/kotlin/com/example/ElChavo8TVPlugin.kt
- ElChavo8TVProvider/src/main/kotlin/com/example/ElChavo8TVProvider.kt
- plugins.json: entrada ElChavo8TV/ElChavo8TVProvider v1 (ileSize: 0 pendiente)
- AGENTS.md: esta secciÃ³n

### Estado
- No compilado por regla del repo. Comando: .\gradlew.bat :ElChavo8TVProvider:make --console=plain -q
- â¸ï¸ Pendiente: compilar, actualizar fileSize, instalar y probar. Logs: db logcat -s ElChavo8TV:V buscando loadLinks video URL: y loadLinks ... -> OK.

## XuperVODProvider â€” Plugin nuevo (07 Nov 2026 v1)
### Sitio
- https://modlyo.com/magis-web/ â€” Magis/Xuper con contenido VOD (pelÃ­culas y series) y TV en vivo
- Requiere sesiÃ³n (anonima o con cuenta) para acceder al catÃ¡logo
- ImplementaciÃ³n de referencia en lordmacu/kino-light (com.arkiv.player.data.magis/)

### Arquitectura (dos plugins separados)
| Plugin | tvTypes | Enfoque |
|--------|---------|---------|
| XuperVODProvider | Movie, TvSeries, Anime | Biblioteca VOD: catalogo, busqueda, detalle, resolucion de streams |
| XuperLiveProvider | Live | TV en vivo: lista de canales, resolucion de streams M3U8/MP4 |

### Archivos XuperVODProvider
- XuperVODProvider/build.gradle.kts (v1, es, Movie+TvSeries+Anime)
- XuperVODProvider/src/main/AndroidManifest.xml
- XuperVODProvider/src/main/kotlin/com/example/XuperVODPlugin.kt
- XuperVODProvider/src/main/kotlin/com/example/XuperVODProvider.kt
- plugins.json: entrada XuperVOD v1 (fileSize: 0 pendiente)
- AGENTS.md: esta seccion

### Archivos XuperLiveProvider
- XuperLiveProvider/build.gradle.kts (v1, es, Live)
- XuperLiveProvider/src/main/AndroidManifest.xml
- XuperLiveProvider/src/main/kotlin/com/example/XuperLivePlugin.kt
- XuperLiveProvider/src/main/kotlin/com/example/XuperLiveProvider.kt
- plugins.json: entrada XuperLive v1 (fileSize: 0 pendiente)
- AGENTS.md: esta seccion

### Estado
- âœ… Estructuras creadas para ambos plugins
- âœ… mainPageOf definido con secciones iniciales (Descubrir/Biblioteca para VOD, TV en Vivo para Live)
- âœ… search, load, loadLinks con stubs que emiten logs de diagnostico
- â¸ï¸ Pendiente: definir endpoints reales de modlyo.com/magis-web/ para catalogo VOD, busqueda, detalle y resolucion de streams
- â¸ï¸ Pendiente: definir endpoints de TV en vivo (lista de canales, resolucion de streams, posible firma de segmentos)
- â¸ï¸ Pendiente: criptografia propietaria (TweakedMd5 / signO3) para firmar segmentos de live stream
- â¸ï¸ Pendiente: gestion de sesiones (v3/snToken, v8/active, v8/login)
- Compilacion: pendiente por el usuario (.\gradlew.bat :XuperVODProvider:make --console=plain -q y :XuperLiveProvider:make --console=plain -q) â€” no compilado por regla del repo.

## Notas de implementacion (basado en lordmacu/kino-light)
- Session Modes: Anonymous (v3/snToken + v8/active) y Account (v8/login)
- Criptografia: TweakedMd5 con metodo signO3 para firmar segmentos de live stream
- Capas: criptografia â†’ HTTP transport â†’ sesion â†’ catalogo/resolucion
- Desafio geo: puede requerir VPN/proxy si el ISP bloquea el acceso a Magis/Xuper
- Live TV: requiere cuenta; anonymous solo da acceso a catalogo VOD


## XuperVODProvider + XuperLiveProvider Ã¢â‚¬â€ API Magis/Xuper y geo-bloqueo (08 Nov 2026 v2)

### Estructura de la API (descubierta en `magis_app_core.js`)
- Endpoint ÃƒÂºnico: `https://modlyo.com/magis_web/index.php?action=<accion>&<params>`
- Respuesta: `{"ok":true,"data":...}`. **`data` suele ser un ARRAY** (no objeto) Ã¢â‚¬â€ usar `optJSONArray("data")`, NO `optJSONObject("data")`.
- Acciones:
  | Accion | Params | data |
  |---|---|---|
  | `home` | - | filas[] con `items[]` (peliculas/series) |
  | `search` | `q` | items[] directos (o filas con items) |
  | `live` | - | filas[] con `items[]` (canales) |
  | `meta` | `id` | objeto: `type`, `seasons[]` (cada una `number`, `contentId`, `episodes[]`) |
  | `streams` | `id`, `type` (episode/movie), `seriesContentId` | streams[] |
  | `live_stream` | `id` | streams[] |
- Temporadas usan **`number`** (no `season`); episodios: `id`, `title`, `number`, `poster`, `description`.

### Reproduccion: OBLIGATORIO proxy.php
- La web reproduce via `proxy.php?url=<url>&license=<...>&auth=<...>&ua=<...>` (funcion `proxyUrl` en el JS). La URL directa del CDN sin proxy/headers es rechazada.
- Implementado `buildProxyUrl(stream)` en ambos providers. Headers del stream vienen en `stream.headers` (`Content-License`, `Content-Auth`, `User-Agent`) o en `stream.license`/`stream.auth`.
- TS VOD en la web usa `stream.php` (remux + audio), pero el plugin usa `proxy.php` por simplicidad.

### Geo-bloqueo (BLOQUEADOR principal)
- Error real en dispositivo: `{"ok":false,"error":"Sin sesion Magis valida (seeds agotadas o region). v8/active: Ã¦â€°â‚¬Ã¥Å“Â¨Ã¥Å’ÂºÃ¥Å¸Å¸Ã¤Â¸ÂºÃ©Â»â€˜Ã¥ÂÂÃ¥Ââ€¢Ã¯Â¼Å’Ã¦Â ÂÃ§â€ºÂ®Ã¤Â¸ÂÃ¥ÂÂ¯Ã¨Â§Â"}`.
- `Ã¦â€°â‚¬Ã¥Å“Â¨Ã¥Å’ÂºÃ¥Å¸Å¸Ã¤Â¸ÂºÃ©Â»â€˜Ã¥ÂÂÃ¥Ââ€¢` = **region en lista negra**. Sin VPN/proxy en pais permitido, `home`/`search`/`live`/`meta`/`streams` devuelven `ok:false` y el plugin queda vacio. No es bug del codigo.
- El usuario debe usar VPN (region AR/CL/PE/etc. permitida) y reinstalar.

### Estado v2
- XuperVODProvider: `apiRoot()` devuelve el JSON raiz; home/search leen `data` como array; `meta` parsea `seasons[].number`/`contentId`/`episodes[]`; `loadLinks` usa accion `streams` (id/type/seriesContentId) + `proxy.php`.
- XuperLiveProvider: home/search leen `live` array de filas; `loadLinks` usa `live_stream` + `proxy.php`.
- `build.gradle.kts` y `plugins.json` de ambos a **v2**; `fileSize` pendiente tras compilar.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :XuperVODProvider:make --console=plain -q` y `:XuperLiveProvider:make --console=plain -q`). **No compilado por regla del repo.**
- Ã¢ÂÂ¸Ã¯Â¸Â Pendiente: probar con VPN en region permitida. Sin VPN el resultado es vacio por diseno del servidor.


## ElChavo8TVProvider Ã¢â‚¬â€ Home por serie + videos Servidor1/2 (08 Nov 2026 v4)
### Sintomas
- Home mostraba una fila por temporada (nombres feos tipo "el chapulin colorado temporada 1 1972.html"), no agrupado por serie.
- `loadLinks` daba "enlaces no encontrados": `extractByseUrl: no se encontrÃƒÂ³ array urls para Servidor1/2`.

### Causas raiz
- Regex del array exigÃƒÂ­a `\(id\) \{ ` (llave + ESPACIO) pero el HTML tiene `{\n  var urls`. AdemÃƒÂ¡s usaba el nÃƒÂºmero de servidor y no el nombre exacto de la funciÃƒÂ³n.
- Hay VARIAS funciones Servidor en la pÃƒÂ¡gina (`Servidor1ChespTV`, `Servidor2ChespTV`, `Servidor1Kiko`, `Servidor2Kiko`, `ServidorLC1`, `ServidorLC2`). El array de `Servidor1ChespTV` es **global (1397 items)** y el `epId` (ej. 433) es ÃƒÂ­ndice directo en ese array. El ÃƒÂ­ndice 433 = "Chapulin colorado original - Temporada 72" capÃƒÂ­tulo 1.
- `loadLinks` recorrÃƒÂ­a TODOS los enlaces de la pÃƒÂ¡gina (7 cap Ãƒâ€” 2 servidores) en vez de solo el episodio pulsado (`#ep<id>`), y re-fetcheaba muchos.

### Fix
- **Home agrupado**: `getMainPage` devuelve `newHomePageResponse(request.name, items, false)` Ã¢â€ â€™ una fila por serie (nombre de mainPage) con las temporadas como items. `cleanSeasonName()` produce "Temporada 1 (1972)", "Temporada 1971-1972", "Temporada 1994".
- **load()**: dedupe por id global; tÃƒÂ­tulo del capÃƒÂ­tulo tomado del TextNode anterior al `<br>`/ancla (`precedingTitle`); renumeraciÃƒÂ³n limpia 1..N; URL de episodio `.../temporada#ep<idGlobal>`.
- **loadLinks()**: filtra solo el id pulsado; `extractArrayEntry()` usa el **nombre exacto** de funciÃƒÂ³n y regex tolerante `function\s+<func>\s*\(id\)\s*\{[\s\S]*?var\s+urls\s*=\s*\[...\];`.
  - **Servidor 1** (`.../e/...`): Byse (POST a bysedikamoum/api/source), timeout 15s.
  - **Servidor 2** (`...mp4&tipo=...`): MP4 directo `media.pokemonlaserielatino.xyz` + base por `tipo` (chavo/animado/chapulinreal/chapulinanimado/chespirito/sinquererqueriendo/Pelis/kiko). Verificado 200 con Referer `https://pokemonlaserielatino.xyz/`.
- **getVideoInterceptor**: inyecta UA + Referer + Origin a todo `pokemonlaserielatino` (MP4 directo y sus range requests). `source=name` en todos los links para que CS3 llame al interceptor.

### Estado v4
- `build.gradle.kts` y `plugins.json` a **v4**; `fileSize` pendiente tras compilar.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :ElChavo8TVProvider:make --console=plain -q`). **No compilado por regla del repo.**
- Ã¢ÂÂ¸Ã¯Â¸Â Pendiente: probar en dispositivo un capÃƒÂ­tulo de El ChapulÃƒÂ­n T1 (deben salir 2 links: `Servidor1ChespTV Byse` y `Servidor2ChespTV MP4`), y verificar home con 7 filas de serie y temporadas dentro.


## ElChavo8TVProvider Ã¢â‚¬â€ Byse muerto, solo MP4 directo + search cards + posters (08 Nov 2026 v5)
### Hallazgos verificados (08 Nov)
- **Byse (Servidor 1) MUERTO para estos IDs**: `POST bysedikamoum.com/api/source` con `s=sjlp62u3lsfw` -> **404 page not found**. La nueva Byse usa challenge/attestation (ByseHttpExtractor), no el API simple. Se descarta Servidor 1 (`.../e/...`).
- **Servidor 2 (MP4 directo) SIRVE**: `media.pokemonlaserielatino.xyz/Series/.../<tipo>/<ruta>.mp4` -> **200 video/mp4** con `Referer: https://pokemonlaserielatino.xyz/`. Verificado Chavo T1971-72 ep1 (32 MB) y Chapulin T1 ep1.
- **Entradas de Servidor 2 a veces SIN `&tipo=`** (Chavo original): hay que inferir el tipo desde la URL de la temporada (`inferTipo`). Las de animado/chapulin/chespirito/kiko/Pelis sÃƒÂ­ traen `&tipo=`.
- **Search**: el theme usa `div.card` con `div.post_title h2 a` (href+tÃƒÂ­tulo) e `img` (pÃƒÂ³ster). El selector anterior (`article.post`, `entry-title`) daba 0.
- **Posters home**: se usa `og:image` de la pÃƒÂ¡gina de la serie (y `img` cercana al enlace de temporada si existe) como pÃƒÂ³ster de cada card de temporada.

### Fix v5
- `loadLinks`: descarta entradas `http` (Byse) y emite **solo MP4 directo** (entradas relativas `.mp4`), tipo por `&tipo=` o inferido. `getVideoInterceptor` inyecta UA+Referer+Origin a `pokemonlaserielatino` (MP4 y range requests).
- `search`: `div.card` -> `.post_title h2 a` + `img`; dedupe por url, tope 40.
- Home: pÃƒÂ³ster por temporada (og:image de la serie / img cercana).

### Estado v5
- `build.gradle.kts` y `plugins.json` a **v5**; `fileSize` pendiente tras compilar.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :ElChavo8TVProvider:make --console=plain -q`). **No compilado por regla del repo.**
- Ã¢ÂÂ¸Ã¯Â¸Â Pendiente: probar en dispositivo un capitulo (esperado `MP4[Servidor2ChespTV] tipo=... -> ...mp4` y reproduccion), home con posters y search con resultados.


## ElChavo8TVProvider Ã¢â‚¬â€ Servidor 1 (Byse) reactivado con ByseHttpExtractor (08 Nov 2026 v6)
- **Aclaracion del usuario**: en la web **ambas opciones** (Servidor 1 y Servidor 2) reproducen. Servidor 1 NO usa el concat de media; redirige a `elchavodel8tv-haz-clic-para-reproducir/?UrlPok=<byse embed>` (player Byse real). El API simple `bysedikamoum/api/source` da 404; se requiere el frontend Byse con challenge.
- **Fix v6**: se copiÃƒÂ³ `ByseExtractor.kt` (17 KB, autocontenido: Jackson + OkHttp propio + JCA) desde VeranimeProvider al modulo. Nueva `emitByse()` en el provider (patron de VerAnime): fetch del embed, exige `Byse Frontend`, `ByseHttpExtractor().extract(embedUrl, pageUrl, host)` -> emite sources + subtÃƒÂ­tulos con `source=name`.
- `loadLinks`: entradas `http` -> `emitByse` (Servidor 1); entradas relativas `.mp4` -> MP4 directo (Servidor 2). **Se emiten ambas** para que OpciÃƒÂ³n 1 y 2 funcionen.
- Versiones a **6** (`build.gradle.kts` + `plugins.json`); `fileSize` pendiente.
- Compilacion: pendiente por el usuario (`.\gradlew.bat :ElChavo8TVProvider:make --console=plain -q`). **No compilado por regla del repo.**
- Ã¢ÂÂ¸Ã¯Â¸Â Pendiente: probar en dispositivo un capitulo; esperar 2 links (`Servidor 1 Byse` y `Servidor2ChespTV MP4`) y reproduccion en ambos.

## ElChavo8TVProvider â€” ByseExtractor requiere JVM 11 (08 Nov 2026 v6)
- Al copiar ByseExtractor.kt (usa AppUtils.parseJson, inline JVM 11) en un mÃ³dulo con el JVM target 1.8 del root, la clase ByseHttpExtractor queda sin compilar -> Unresolved reference 'ByseHttpExtractor' y los iterator() is ambiguous en cascada.
- **Fix**: ElChavo8TVProvider/build.gradle.kts ahora fija kotlin { compilerOptions { jvmTarget.set(JVM_11) } } dentro de cloudstream {} (mismo patron que SerieskaoProvider, que compila). El build usa compileDebugKotlin + compileDex (sin Java), asi que no hay mismatch de target.
- **Importante**: tras agregar el archivo nuevo, correr build LIMPIO para que el compilador incremental lo tome: .\gradlew.bat :ElChavo8TVProvider:clean :ElChavo8TVProvider:make --console=plain -q.
- Si aun asi ByseHttpExtractor no resuelve, plan B: eliminar ByseExtractor.kt + emitByse y quedarse solo con Servidor 2 (MP4 directo), que compila sin JVM 11.

## ElChavo8TVProvider â€” Fix real JVM 11 para ByseExtractor (08 Nov 2026 v6)
- **Verificado por bytecode**: com/lagradost/cloudstream3/utils/AppUtils.class en el jar de CloudStream es **major 55 (JVM 11)**; parseJson es inline JVM 11.
- **Root** (uild.gradle.kts lineas 53-69) fuerza en TODOS los subproyectos: compileOptions Java 1.8 y 	asks.withType<KotlinJvmCompile> jvmTarget=JVM_1_8. Por eso ByseExtractor.kt (usa parseJson) falla con "Cannot inline bytecode built with JVM target 11 into ... 1.8" -> ByseHttpExtractor queda Unresolved y los iterator() ambiguos son cascada.
- El bloque kotlin { compilerOptions { jvmTarget=JVM_11 } } DENTRO de cloudstream {} NO basta (no sobreescribe el 	asks.withType<KotlinJvmCompile> del root).
- **Fix correcto (patron YoutubeProvider)**: en ElChavo8TVProvider/build.gradle.kts, a nivel de modulo, alinear Java y Kotlin a 11:
  - ndroid { compileOptions { sourceCompatibility/targetCompatibility = JavaVersion.VERSION_11 } }
  - 	asks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach { compilerOptions.jvmTarget.set(JVM_11) }
- **Obligatorio build limpio** tras agregar el archivo nuevo: .\gradlew.bat :ElChavo8TVProvider:clean :ElChavo8TVProvider:make --console=plain -q.
- Plan B si aun falla: eliminar ByseExtractor.kt + emitByse y dejar solo Servidor 2 (MP4 directo), que compila a JVM 1.8.

## ElChavo8TVProvider â€” Fix nombres con slash final + search paginado (08 Nov 2026 v7)
- **Byse + MP4 verificados funcionando en dispositivo** ([Byse] sources=1 + MP4[...] + loadLinks -> OK). Gracias al fix JVM 11 (root fuerza 1.8, AppUtils es JVM 11; se alineo Java+Kotlin a 11 en el modulo).
- **Bug nombres home**: las temporadas del Chavo y Chapulin Animado usan URLs que **terminan en /** (ej. /temporada-1971-1972-el-chavo-del-8-latino/), por lo que url.substringAfterLast("/") daba "" -> fallback a la URL. Las de Chapulin/Chespirito usan .html y sÃ­ funcionaban. **Fix**: cleanSeasonName ahora hace url.substringBefore("?").trimEnd('/') antes de sacar el slug.
- **Bug search (solo pagina 1)**: el search de WordPress pagina con /page/N/?s=. Ahora search() recorre paginas 1..6 (o hasta que una pagina no aporte nuevos URLs), dedupe por url, tope 60. parseSearchDoc() separado.
- Versiones a **7** (uild.gradle.kts + plugins.json); ileSize pendiente.
- Compilacion: pendiente por el usuario (.\gradlew.bat :ElChavo8TVProvider:clean :ElChavo8TVProvider:make --console=plain -q). **No compilado por regla del repo.**
- â¸ï¸ Pendiente: probar home del Chavo (nombres "Temporada YYYY" en vez de URL) y search "chapulin" (debe traer varias paginas, ej. ~30+ resultados).

## Animeav1Provider â€” Fix ONA/OVA tratados como pelÃ­cula (08 Nov 2026 v2)
- **Sintoma (usuario)**: en AnimeAV1, Horimiya (TV Anime, 13 eps) salia bien con todos los episodios, pero "Guimi Zhi Zhu: Xiaochou Pian" (tipo **ONA**, 13 eps) salia como **pelicula**.
- **Causa raiz**: getTvType() solo reconocia "TV Anime" y "OVA" como Anime; cualquier otro tipo (incluye **ONA**) caia en else -> TvType.Movie, asi que load() entraba a la rama de pelicula y no extraia episodios.
- **Fix**: getTvType() ahora solo devuelve TvType.Movie si el texto contiene "PelÃ­cula"/"Pelicula"/"Movie"; **todo lo demas** (TV Anime, ONA, OVA, Especial, vacio) -> TvType.Anime (con episodios). Se agrego log LOAD METADATA: rawtype='...' -> type=....
- Versiones a **2** (uild.gradle.kts + plugins.json); ileSize pendiente.
- Compilacion: pendiente por el usuario (.\gradlew.bat :Animeav1Provider:make --console=plain -q). **No compilado por regla del repo.**
- â¸ï¸ Pendiente: probar Guimi Zhi Zhu (debe listar 13 episodios, no como pelicula).

## LacartoonsProvider â€” Cubeembed/TurboEmbed ahora es Turbovio SPA (08 Nov 2026 v27)
- **Sintoma (usuario)**: El Chavo Del 8 T5 (cap 30028) -> iframe cubeembed.rpmvid.com -> redirige a 	urboembed.com/1ybb; el WebView log TurboEmbed WebView terminÃ³ sin fuente JW; el API legado da 404; emitted=0 -> "enlaces no encontrados".
- **Causa raiz**: 	urboembed.com ya NO usa JWPlayer con ile en getPlaylistItem de entrada. Ahora es **Turbovio** (SPA Vite) con: clave AES derivada de Math.floor(Date.now()/864e5) (cambia cada dÃ­a), IV = salt fijo + origin, endpoints /api/stream?s=<enc>&pv=, tabla de strings ofuscada con rotaciÃ³n en runtime y anti-bot (webdriver/headless/okhttp). Replicar su cripto desde OkHttp es frÃ¡gil.
- **Fix v27**: esolveTurboEmbed ahora **captura el .m3u8 por intercepciÃ³n de red** (WebViewClient.shouldInterceptRequest, devuelve null para no romper) + sondeo JS que mira ideo.currentSrc, performance.getEntriesByType('resource') (busca .m3u8) y jwplayer, y pulsa #player-button/.tvp-poster-play/.tvp-center-play. Timeout subido a 45s / 40 intentos. lastPageUrl = URL real de turboembed para el referer.
- Nota ads: Turbovio intercepta el primer click si hay ads pendientes (h>0); el poll insiste cada segundo hasta que pasan.
- Versiones a **27** (uild.gradle.kts + plugins.json); ileSize pendiente.
- Compilacion: pendiente por el usuario (.\gradlew.bat :LacartoonsProvider:make --console=plain -q). **No compilado por regla del repo.**
- â¸ï¸ Pendiente: probar El Chavo T5 cap 30028; logs TurboEmbed page loaded, y si aparece TurboEmbed JW source found o captura por interceptor. Si el CDN del m3u8 exige cookies de la sesiÃ³n WebView, habrÃ­a que relay; revisar logs.

## LacartoonsProvider â€” Turbovio: extraer via window.simpleCrypto (08 Nov 2026 v28)
- **v27 fallÃ³ en dispositivo**: WebView cargaba 	urboembed.com/1ybb pero NO capturaba .m3u8 en 45s (el player no llega a pedir el stream: el gate de ads intercepta el primer click y/o no se pulsa). emitted=0.
- **v28**: en vez de depender del DOM/click/intercepciÃ³n, se **ejecuta JS en el contexto de la pagina** usando su propia cripto (window.simpleCrypto) para llamar /api/stream y descifrar la respuesta:
  - slug = ultimo segmento de la URL; sales candidatas = [origin, hostname, host, ""].
  - Por cada salt: s = simpleCrypto.encrypt(slug, salt) -> etch('/api/stream?s='+s+(&pv)) -> recorrer data buscando hls/	iktok/direct; si estan cifrados, simpleCrypto.decrypt(field, salt) + JSON.parse.
  - Prefiere hls > 	iktok > direct. unApi() se lanza a 1.2s y 4.5s tras onPageFinished. Sigue habiendo poll() + shouldInterceptRequest(.m3u8) como red de seguridad.
  - NativeBridge.onErr loguea [TurboJS] ... para diagnosticar (no simpleCrypto / api XXX / no source in payload).
- Versiones a **28** (uild.gradle.kts + plugins.json); ileSize pendiente.
- Compilacion: pendiente por el usuario (.\gradlew.bat :LacartoonsProvider:make --console=plain -q). **No compilado por regla del repo.**
- â¸ï¸ Pendiente: probar El Chavo T5 cap 30028. Logs clave: [TurboJS] api 200 salt=N, TurboEmbed JW source found (captura), o [TurboJS] no source in payload. Si sale pi 200 en todos los salts pero 
o source in payload, el objeto descifrado usa otras claves (compartir el JSON para ajustar).

## LacartoonsProvider â€” Turbovio resuelto con AES directo (08 Nov 2026 v29)
- **Flujo real descifrado del bundle (deobfuscado con Node)**: NO se usa WebView ni /api/stream?s=. El endpoint correcto es /api/stream/tracking?c=<enc>&pv= y la carga util viene en el **header de respuesta x-tracking** (hex AES-CBC).
- **Cadena verificada end-to-end (PC)**:
  1. iframe https://cubeembed.rpmvid.com/#<file1> -> GET https://cubeembed.rpmvid.com/api/public/resolve?file1=<file1> -> {url:"https://turboembed.com/<slug>", file2:<slug>}.
  2. c = AES/CBC/PKCS5 encrypt(slug, salt=hostname).
     - key = KEYBASE + (diasDesdeEpoch en hex) -> slice(0,32).padEnd(32,"0").
     - iv = IVBASE + hex(utf8(hostname)) -> slice(0,32).padEnd(32,"0").
     - KEYBASE = codepoints hex de keyChars = 1d54a1d53c1d56e1d6811d53c1d6831d4da1d4d41d4e81d7d91d7da1d7db.
     - IVBASE = codepoints hex de saltChars = 1f5101f5dd1f5111f6aa1f6e126a1.
  3. GET https://<host>/api/stream/tracking?c=<c> (UA Chrome + Referer pagina + Origin) -> **204** con header x-tracking (hex cifrado).
  4. decrypt(x-tracking, hostname) -> JSON {"stream":{"hls":"https://.../master.txt",...}}.
  5. stream.hls (o 	iktok/direct) es la fuente. El CDN **exige Referer https://turboembed.com/** (403 sin el); segmentos seg-N-s1080p-v1.woff2 = fMP4 (200).
- **Fix v29**: Cubeembed.getUrl reescrito: resuelve slug (cubeembed->resolve; turboembed->path), cifra, llama tracking, lee x-tracking, descifra y emite el hls como M3U8 con headers (UA + Referer https://<host>/ + Origin). Sin WebView. LacartoonsPlugin ya no setea Cubeembed.pluginContext.
- Versiones a **29** (uild.gradle.kts + plugins.json); ileSize pendiente.
- Compilacion: pendiente por el usuario (.\gradlew.bat :LacartoonsProvider:make --console=plain -q). **No compilado por regla del repo.**
- â¸ï¸ Pendiente: probar El Chavo T5 cap 30028; log Turbovio src host=... + emitted https://.../master.txt. Si el CDN rechaza segmentos por headers, agregar interceptor para esos hosts.

## ReanimeProvider â€” migraciÃ³n de dominio reanime.to â†’ reanime.cz (12 Nov 2026 v2)
- **SÃ­ntoma**: `getMainPage: code=200 title='Re:ANIME â€” Stream Anime Free Online' anchors=0` y `search 'rascal' fallo: Value <!doctype...`. Todo salÃ­a vacÃ­o.
- **Causa raÃ­z**: `reanime.to` **redirige todo** a la raÃ­z de `reanime.cz` (SvelteKit SPA). El provider usaba `reanime.to` y recibÃ­a el app-shell HTML en lugar del contenido/API.
- **Verificado en PC (nuevos endpoints funcionan)**:
  - `https://reanime.cz/home` (SSR 390 KB): 12 anchors `/watch/<slug>?ep=latest` y 24 `/anime/<slug>`, `img alt` = tÃ­tulo, `src` = poster.
  - `https://reanime.cz/api/v1/search?q=` â†’ JSON `{results:[...]}` (igual que antes).
  - `https://reanime.cz/api/v1/anime/{slug}` â†’ JSON metadata (anilist_id, title, banner, genres...).
  - `https://reanime.cz/api/v1/anime/{slug}/episodes?limit=2000` â†’ `{data:[...]}`.
  - `https://reanime.cz/api/flix/{anilist_id}/{episode}` â†’ `{success, servers:[...]}`.
- **Fix**: `MAIN_URL` `reanime.to` â†’ `reanime.cz`; `getMainPage` ahora selecciona `a[href*='/anime/']` y `a[href*='/watch/']` y extrae el slug con `(?:/anime/|/watch/)([a-z0-9\-_]+)` (el regex anterior solo miraba `/watch/` y con el query `?ep=latest` funcionaba igual). Sin cambios en load/loadLinks (los endpoints `/api/v1/...` se mantienen en el nuevo dominio).
- `description`/`iconUrl` de `plugins.json` y las URLs del provider apuntan a `reanime.cz`.
- Versiones **2** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente).
- CompilaciÃ³n pendiente por el usuario: ` .\gradlew.bat :ReanimeProvider:make --console=plain -q`.
- â¸ï¸ Pendiente en dispositivo: home (secciones Latest/Trending), bÃºsqueda, carga de un anime y reproducciÃ³n flixcloud (HD-1/HD-2).

## VeranimeProvider â€” challenge Cloudflare nuevo: fallback WebView para todas las pÃ¡ginas (12 Nov 2026 v12)
- **SÃ­ntoma (12 Nov, log del usuario)**: home en **0 items hasNext=false** en todas las secciones (CatÃ¡logo, AcciÃ³n, Aventura, Comedia, Drama, FantasÃ­a, Romance; con una red a la vez â€” cada secciÃ³n tardÃ³ ~740-900 ms) y `search 'jujutsu' -> 0 total, 0 filtrados 95ms`.
- **Causa raÃ­z (verificada hoy desde PC con curl/python)**: `veranime.ninja` ahora responde **403 con `Cf-Mitigated: challenge` ("Just a moment...")** en TODAS las rutas HTML y tambiÃ©n en `wp-json/wp/v2/search`. El provider parsea ese 403 como si fuera HTML normal â†’ 0 `article.item` â†’ 0 tarjetas, sin error visible. El challenge es un **challenge JS**; ni cambiar UA ni headers lo pasa (probado con Chrome/148 completo + Sec-CH-UA + Accept-Language; solo un navegador real lo resuelve).
- **Fix v12 (`VeranimeProvider.kt`)**: nuevo helper `getDoc()` que (1) hace `app.get`, (2) si la respuesta es <400 y el HTML **no** contiene marcadores de challenge (`Just a moment`, `challenge-platform`, `cf-chl`, `Attention Required`) devuelve el documento; (3) si no, reintenta con el **`renderViaWebView()` ya existente** en el provider (mismo WebView que el fallback de StreamWish), esperando con `readyJs` a que aparezcan los marcadores reales (`article.item`, `.result-item`, `#seasons`, `.se-c`, `.dooplay_player_option`) y parseando el outerHTML. `Jsoup.parse(rendered, url)` preserva el baseUrl. Aplicado a `getMainPage`, `search`, `load` y `loadLinks`. Logs: `[home|search|load|loadLinks] HTTP ... challenge/error (len=...), probando WebView` y `... WebView ok len=...`.
- **Coste esperado**: cada pÃ¡gina ahora pasa primero por `app.get` (403 rÃ¡pido, ~<1 s) y luego por el WebView (hasta 30 s, normalmente 2-10 s con el challenge resuelto). El challenge lo supera Chromium real; el OkHttp seguirÃ¡ dando 403 siempre (es rÃ¡pido, no cuelga).
- **No tocado**: los mirrors/extractors (`resolveSaidoMirror`, `resolveCyberMirror`, `emitByse`, `SW-Web`), los timeouts de `loadLinks` y el `getVideoInterceptor`.
- Versiones **12** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 60000).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :VeranimeProvider:make --console=plain -q`), actualizar `fileSize`, instalar y probar: home (7 filas), bÃºsqueda ("jujutsu"), abrir un anime, reproducir un episodio. Buscar `challenge/error` seguido de `WebView ok` para confirmar que el bypass funciona; si el WebView tampoco pasa (devuelve el challenge otra vez), avisar â€” quedarÃ­a solo el camino de resolver cf_clearance vÃ­a sesiÃ³n de navegador, que no es viable para OkHttp.

## VeranimeProvider â€” decisiÃ³n del usuario: dejar el cÃ³digo, no pelear con Cloudflare (12 Nov 2026)
- **Reporte del usuario (12 Nov, tras v12)**: el WebView **no supera** el challenge (`[WebView] no superÃ³ el challenge`, ~30-40 s por pÃ¡gina; home y search vacÃ­os). Decide **dejar el provider como estÃ¡** y no insistir con el challenge.
- **Estado**: v12 compilada por el usuario, subida como `VeranimeProvider.cs3` v12. El cÃ³digo v12 se deja en el repo (fail-open con WebView) por si Cloudflare relaja el challenge en el futuro; `getMainPage/search/load/loadLinks` vuelven a funcionar sin tocar nada el dÃ­a que el challenge desaparezca.
- **Importante para el dispositivo**: con v12 instalada, cada pantalla espera hasta 30 s en el WebView que nunca resuelve â†’ el arranque/bÃºsqueda se sienten lentos aunque el resultado sea "vacÃ­o". Si molesta, **desinstalar/ocultar AnimeNINJA en la app** hasta que se reabra el challenge.
- El home/bÃºsqueda **solo** funcionarÃ¡n cuando veranime.ninja deje de servir `403 Cf-Mitigated: challenge` (probado 12 Nov desde PC: todas las rutas y `wp-json`). No se busca / no se propone bypass por VPN ni por sesiÃ³n de navegador.
- **Alternativas ya disponibles en el repo** (ninguna necesita pelearse con Cloudflare hoy): `TioanimeProvider` (tioanime.com, 200), `JkanimeProvider` (jkanime.net, 200), `Animeav1Provider` (animeav1.com, 200), `MonoschinosProvider` (v11, dominios propios con bypass propio), `AnimeflvnetProvider` (animeflv.net estaba en 522 al momento de probar).
- **Pendiente (si se retoma)**: probar sin instalar v12 primero (la v11 responde 0 items en home/search, pero rÃ¡pido), o simplemente comprobar desde PC si el sitio sigue dando 403; si un dÃ­a responde 200, v12 deberÃ­a funcionar sola.

## VeranimeProvider â€” migraciÃ³n a verani.me (12 Nov 2026 v13)
- **Motivo**: `veranime.ninja` responde 403 con challenge JS de Cloudflare en TODAS las rutas (probado 12 Nov desde PC; el WebView no lo supera). El usuario decidiÃ³ **reemplazar el plugin por `https://verani.me/`** (mismo sitio del mismo operador, sin Cloudflare).
- **Host nuevo**: `verani.me` es WordPress (tema propio "veranime", es_MX) y responde **200 sin challenge** en todas las rutas probadas (home, `/animes/`, `/?s=`, `/genero/`, detalle, episodio).
- **Selectores verificados** (PC, 12 Nov):
  - Home: secciÃ³n "Ãšltimos Animes" â†’ `.animes-section .content-card.anime-card`; "Ãšltimas PelÃ­culas" â†’ `.peliculas-section .content-card.pelicula-card`; "Top Animes" â†’ `a.home-top-animes-item` (title/poster/rank). Cards: `a.card-link[href]` + `span.card-title` + `img.card-img[alt][src]`.
  - BÃºsqueda `/?s=`: `article.content-card` con `a.card-link` â€” **ojo**: algunos resultados apuntan a dominios externos (`jujutsukaisenonline.com`) â†’ el plugin filtra `href` que contenga `verani.me`. `pelicula-card` â†’ TvType.AnimeMovie.
  - Detalle serie: `h1.anime-hero-title`, poster `meta[property=og:image]`, backdrop `img.anime-hero-bg-image[src]`, plot `p.anime-hero-description`, aÃ±o `.anime-hero-year`, estado `.anime-hero-status` (Finalizadoâ†’Completed), trailer `.anime-trailer-iframe[src]`, episodios `.temporada-group > .temporada-name[data-season-num]` y `.capitulo-card > a.capitulo-card-link` (`h3.capitulo-card-title` = "Capitulo N", `p.capitulo-card-subtitle`, `img.capitulo-card-img`, badges `.cap-lang-badge` SUB/LAT). Recomendaciones `.anime-recommendation-card a.card-link`.
  - Detalle pelÃ­cula: `h1.pelicula-title` (+ `span.pelicula-year-line`), poster `img.pelicula-poster-image`, backdrop `.pelicula-backdrop-image`, plot `.pelicula-overview-text`, gÃ©neros `.pelicula-meta-generos`, servers: `button.servidor-tab[data-url][data-label][data-idioma][data-subtitulos]` (igual que las series).
  - Episodio: `button.servidor-tab` con la misma estructura (ejemplo Rg Tokyo Magnitude 8.0 EP11: 5 servers JaponÃ©s/SUB).
- **Servidores (5 por episodio, todos verificados 200 desde PC)**:
  1. `player.zilla-networks.com/m3u8/{hash}` â†’ **M3U8 directo** (master + segmentos fMP4). Segmentos exigen headers completos de navegador (Cloudflare) â†’ `zillaHeaders` + interceptor zilla.
  2. `animeav1.uns.bio/#{hash}` â†’ `GET /api/public/resolve?file1={hash}` â†’ `{url:"https://turboembed.com/{slug}"}`. Turbovio = mismo flujo criptogrÃ¡fico que Cubeembed (v29 Lacartoons): AES-CBC con key=KEYBASE+dÃ­a UTC hex (slice 32), iv=IVBASE+hex(host) â†’ `POST/GET /api/stream/tracking?c=` â†’ header **`x-tracking`** â†’ decrypt â†’ `{"stream":{"hls":".../master.txt"}}`; CDN exige `Referer: https://turboembed.com/`. Implementado idÃ©ntico (port).
  3. `voe.sx/e/{id}` â†’ `loadExtractor` (CS3; en el mejor de los casos emite, si ALTCHA no se resuelve no emite â€” el usuario tiene otros servers).
  4. `byselapuix.com/e/{id}` (y otros `byse*`) â†’ `ByseHttpExtractor` (copiado) con fallback a `loadExtractor`.
  5. `www.mp4upload.com/embed-{id}.html` â†’ `loadExtractor` (CS3 Mp4Upload).
- **Eliminado por completo**: cÃ³digo DooPlay (seasons `#seasons`, AJAX `doo_player_ajax`, msp-data de multiserver-pro, byse N para veranime.ninja, fallback WebView de StreamWish, `pluginContext`).
- **Cambios**: `VeranimeProvider.kt` reescrito (mismo internalName), `VeranimePlugin.kt` simplificado, `tvTypes` = Anime+AnimeMovie, `iconUrl` = favicon verani.me, `name` = **VerAnime**, `language` es/mx. Versiones **13** (`build.gradle.kts` + `plugins.json`; `fileSize` = **0** pendiente).
- CompilaciÃ³n: pendiente por el usuario (`.\gradlew.bat :VeranimeProvider:make --console=plain -q`) â€” **no compilado por regla del repo**.
- â¸ï¸ **Pendiente**: compilar, subir .cs3, actualizar `fileSize`, instalar y probar: home (Ãšltimos/Top/PelÃ­culas), bÃºsqueda "jujutsu" (debe traer ~4 resultados), abrir serie (eps multi-temporada) y reproducir Zilla + Turbovio (logs `resolveServer`, `emitted=`). Si los segmentos zilla dan 403, el interceptor ya inyecta headers completos; si el CDN de turbovio exige otra cosa, comparte el `[intercept]` host.

## VeranimeProvider â€” fix poster en favoritos (12 Nov 2026 v14)
- **SÃ­ntoma (usuario)**: al guardar en favoritos desde el detalle, series que NO son anime (ej. Samurai Jack) muestran **el logo del sitio** como poster; las de anime (Classroom of the Elite) sÃ­ muestran el pÃ³ster. En getMainPage/search/load sÃ­ se veÃ­an pÃ³steres (vienen de `card-img` de TMDB).
- **Causa raÃ­z verificada desde PC**: `og:image` de la pÃ¡gina de Samurai Jack = `wp-content/uploads/2026/01/Gemini_Generated_Image_...-1024x401.png` (el **logo del sitio**). El theme/Yoast cae al logo cuando el post NO tiene thumbnail propio; Classroom sÃ­ tiene su pÃ³ster local (`mmhx3dImdsfYpcFm3J1tlQt5IRN.jpg`). El provider usaba `og:image` como `posterUrl` en `load()` â†’ los favoritos heredaban el logo.
- **Fix v14**: helper `isSiteLogo(url)` (`contains("Gemini_Generated_Image_", ignoreCase = true)`); `load()` serie usa `og:image` solo si no es logo, si no â†’ backdrop `img.anime-hero-bg-image[src]` como pÃ³ster; pelÃ­cula: `.pelicula-poster-image` primero, `og:image` solo si no es logo.
- **DecisiÃ³n de alcance**: no se toca `toCard`/`search` (funcionan). No se hizo detecciÃ³n por dimensiones (Jsoup no las conoce) ni peticiÃ³n extra.
- Versiones a **14** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente, sigue 0).
- â¸ï¸ **Pendiente**: compilar (`.\gradlew.bat :VeranimeProvider:make --console=plain -q`), subir `.cs3`, actualizar `fileSize`, y **guardar en favoritos Samurai Jack** â†’ debe salir con el backdrop de la serie en vez del logo (si Yoast cambia el fallback del logo, el substring quizÃ¡ haya que ampliarlo).

## VeranimeProvider — quitar prefijo `E$n` del nombre del episodio (12 Nov 2026 v15)
- **Detalle**: en `load()` el nombre del episodio salía como `E1 E2 ... <nombre>`. El usuario pidió que solo muestre el nombre del episodio.
- **Fix**: `this.name = subtitle ?: "Capítulo $epNum"` en `parseEpisodes` (antes `"E$epNum $it"`).
- Versiones a **15** (`build.gradle.kts` + `plugins.json`; `fileSize` pendiente sigue 0).

## SoloLatinoProvider — embeb69.org tras Anubis (PoW v1.28.2) (13 Nov 2026 v43)
- **Síntoma (log usuario Doctors Who T1E8)**: search/load/Sanctum OK; `loadLinks` devuelve los 3 servidores; embed69 200 len=8288 pero sin `dataLink =` → `embed69 - No se encontró script con 'dataLink ='` → 0 links → "enlaces no encontrados". Los otros 2: pelisserieshoy = página-anuncio (0 correcto); playhydrax = JWPlayer sin fuente accesible.
- **Causa raíz verificada desde PC**: embed69.org ahora está tras **Anubis** ("¡Asegurándonos de que no eres un robot!", `<script id="anubis_challenge" type="application/json">`): rules `{algorithm:"fast", difficulty:4}`, challenge `{id, randomData, difficulty}`. Mitiga el fetch de HTTP plano (mismo UA que el app).
- **Protocolo decodificado** (del propio `main.mjs` + `sha256.mjs` del sitio):
  1. GET página → challenge HTML.
  2. PoW FAST: buscar `nonce` (string decimal) tal que `SHA-256(randomData + nonce)` tenga los primeros `difficulty/2` bytes en 0x00 (y si difficulty es impar, nibble alto del siguiente byte en 0). difficulty=4 → hash empieza por `0000`. Verificado en vivo: nonce~120k resuelto en ~0.7 s.
  3. GET `/.within.website/x/cmd/anubis/api/pass-challenge?id=...&response=<hash>&nonce=<nonce>&redir=<path?query>&elapsedTime=<ms>` → 200, setea cookies `techaro.lol-anubis-auth-*` y `techaro.lol-anubis-cookie-verification-*`.
  4. GET la página con esas cookies → **200 len=67552 con `dataLink =`** (LAT: streamwish/voe/...). Verificado end-to-end desde PC.
- **Fix (`SoloLatinoProvider.kt`)**: nueva `fetchBypassingAnubis()` usada por la rama embed69: detecta `anubis_challenge` vía regex sobre el `<script id="anubis_challenge">`, resuelve PoW con `solveAnubisPow()` (en `Dispatchers.Default`), hace pass-challenge con cookies, re-fetch con cookies combinadas. Devuelve la página real → el camino existente (`dataLink` + POW_CHALLENGE + AES decriptación) sigue siendo el mismo (sin cambios).
- **Versiones desalineadas detectadas**: `build.gradle.kts` iba en 40 mientras `plugins.json` ofrecía 42 (nadie había sincronizado). Ambos a **43**. `fileSize` pendiente tras compilar (`.\gradlew.bat :SoloLatinoProvider:make --console=plain -q`).
- **Pendiente en dispositivo**: instalar v43 y abrir Doctor Who T1E8 (`https://sololatino.net/serie/doctor-who/temporada-1/episodio-8`). Esperado en logcat: `Anubis: PoW difficulty=4` → `Anubis: resuelto nonce=...` → `Anubis: pass-challenge HTTP 200 cookies=[techaro.lol-anubis-auth-..., ...]` → `embed69 - script con dataLink encontrado` → servidores emitidos. Si la página final sigue sin `dataLink`, compartir el log `Anubis:` completo (principalmente `cookies=[]` o `PoW no resuelto`).
- ~~WebView de 502/Just a moment~~: rama conservada. playhydrax y pelisserieshoy siguen sin emitir (preexistente; no relacionados con Anubis).
