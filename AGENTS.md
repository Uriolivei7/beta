# AGENTS.md - Netmirror Plugin Development

## Goal
- Proveer streams completos (no preview 10 min) desde cncverse para Netflix/PrimeVideo/JioHotstar providers en CloudStream.

## The REAL Flow (discovered 07 Jul 2026 via tPacketCapture + curl validation)

### Endpoints
| Endpoint | USAGE | Status |
|---|---|---|
| `net52.cc/newtv/main.php` | Main page (categorías) | ✅ Funciona |
| `net52.cc/newtv/search.php` | Búsqueda | ✅ Funciona |
| `net52.cc/newtv/post.php` | Detalles de contenido | ✅ Funciona |
| `net52.cc/newtv/episodes.php` | Episodios (series) | ✅ Funciona |
| `tv.imgcdn.kim/newtv/*` | Ídem (API base alternativa) | ✅ Funciona (no está caído) |
| **`net52.cc/mobile/hls/{id}.m3u8`** | **ENDPOINT DE PLAYBACK** | ✅ **CLAVE** — devuelve master playlist |
| `s21.freecdn4.top/files/{internalId}/{quality}/` | Preview CDN (60 JPG segments = 10 min) | ❌ Solo preview |
| **`s23.nm-cdn9.top/files/{contentId}/{quality}/`** | **CONTENIDO COMPLETO CDN** | ✅ **Full content (1030+ JPG segments)** |
| `net52.cc/verify.php` | Bypass (POST → Set-Cookie t_hash_t) | ✅ Funciona |
| `net52.cc/newtv/playlist.php` | Antiguo endpoint (preview only) | ❌ Solo preview |
| `net52.cc/newtv/player.php` | Antiguo endpoint (preview only) | ❌ Solo preview |

### Full Playback Flow (verified with curl)

1. **`POST net52.cc/verify.php`** → `Set-Cookie: t_hash_t=h1::h2::ts::ep::99`  
   (OkHttp `followRedirects=false` para capturar cookie antes del redirect 301)

2. **`GET net52.cc/mobile/hls/{id}.m3u8?q=720p&in=<cookie_token>::ep::99&hd=on&lang=eng`**  
   Headers: Chrome/149 WebView UA, `app.netmirror.netmirrornew`, sec-ch-ua, Cookie  
   → Server **REESCRIBE hash2** en el `in=` parameter de las URLs de video  
   → Devuelve audio de `s23.nm-cdn9.top` y video de `s21.freecdn4.top` (preview)

3. **IGNORAR las URLs de s21 del server.** Construir URLs directamente a s23:  
   `GET https://s23.nm-cdn9.top/files/{id}/{quality}/{quality}.m3u8?in=<rewritten_token>`  
   Cookie: `hd=on; t_hash_t=<cookie_value>`  
   → **1030+ JPG segments = contenido completo** (ej: 6809_000.jpg ... 6809_1029.jpg)

4. Cada segmento JPG (~295KB) se sirve desde s23 sin token en la URL (solo Cookie).

### CDN Key Differences
| CDN | Path Pattern | Content | Segments |
|---|---|---|---|
| `s21.freecdn4.top` | `/files/{internalId}/{quality}/` | 10-min preview | 60 JPG segments |
| `s23.nm-cdn9.top` | `/files/{contentId}/{quality}/` | **Full content** | **1030+ JPG segments** |

### Token Format & Behavior
```
verify.php cookie:    h1::h2::ts::ep::99
URL in= parameter:    h1::h2::ts::ep::99  (same, server rewrites hash2)
Server rewritten:     h1::h2_new::ts::ep  (hash2 CHANGES)
s23 URL in=:          h1::h2_new::ts::ep  (uses rewritten hash2)
```

**KEY FINDING**: The raw cookie's hash2 does NOT work on s23. Only the **server-rewritten** hash2 (from the mobile/hls response) works. The rewrite happens per-content-id.

### Correct Headers (from cncverse tPacketCapture)
```
User-Agent: Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0
X-Requested-With: app.netmirror.netmirrornew
sec-ch-ua: "Android WebView";v="149", "Chromium";v="149", "Not)A;Brand";v="24"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Android"
Sec-Fetch-Dest: empty
Sec-Fetch-Mode: cors
Sec-Fetch-Site: same-origin
Referer: https://net52.cc/mobile/home?app=1
Cookie: t_hash_t=<URL-encoded>; ott=nf|pv; hd=on
```

**CRITICAL**: `X-Requested-With` must be `app.netmirror.netmirrornew`, NOT `NetmirrorNewTV v1.0` (decompiled code had a different value than what cncverse actually sends).

### Bypass Flow
1. ✅ Cache: SharedPreferences `nf_cookie` + timestamp (<10 min)
2. ✅ GET `tv/p.php` → Cloudflare 403
3. ✅ WebView (5s timeout → fallback)
4. ✅ **POST verify.php via OkHttp `followRedirects(false)`** → captures t_hash_t

## Implementation (NetflixProvider.kt / PrimevideoProvider.kt)

### `loadLinks` — Primary Flow
```kotlin
// 1. Get bypass token (verify.php → t_hash_t cookie)
val cookieRaw = currentBypassToken  // h1::h2::ts::ep::99 (decoded)

// 2. Get rewritten token from mobile/hls endpoint
val inParam = cookieRaw.substringBefore("::ep") + "::ep::99"
val mobileResp = app.get("$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng",
    headers = mobileHeaders)  // Chrome/149 WebView + sec-ch-ua + Cookie

// 3. Extract rewritten token from response
val rewrittenToken = Regex("""in=([^&\s]+)""").find(mobileResp)?.groupValues?.get(1)

// 4. Construct s23 full-content URLs
for (q in listOf("1080p", "720p")) {
    val s23Url = "https://s23.nm-cdn9.top/files/$id/$q/$q.m3u8?in=$rewrittenToken"
    // callback(ExtractorLink) with Cookie: hd=on; t_hash_t=<escaped>
}
```

### Fallbacks (in order)
1. ❌ ~~Scraping HTML~~ — reemplazado por mobile/hls
2. `playlist.php` — preview only, pero mantiene compatibilidad
3. `player.php` — preview only

### `getVideoInterceptor`
- Adds `Cookie: hd=on` to all `.m3u8` requests (preserves existing cookie from ExtractorLink)
- Replaces `in=unknown::ep` watermark with bypass token
- Fixes relative JPG segment URLs by appending `in=<token>` param
- Does NOT strip audio/subtitle groups (s23 has everything on same CDN)

## Current State (07 Jul 2026)
- ✅ `newTvBaseHeaders` actualizado: Chrome/149 WebView + `app.netmirror.netmirrornew`
- ✅ `loadLinks` usa mobile/hls → s23 como primario
- ✅ Fallbacks: playlist.php → player.php
- ✅ Bypass: OkHttp no-redirect POST verify.php
- ✅ Interceptor: Cookie hd=on + token replacement
- ✅ PlutoTVProvider: logs PLUTOTV + Clip.originalReleaseDate nullable fix
- ✅ `tv.imgcdn.kim` confirmado VIVO para UI endpoints
- ⚠️ Contenido completo verificado desde PC vía curl con token reescrito → 1030 JPG segments
- ⏸️ Falta probar en dispositivo Android real (CloudStream) para ver si reproduce JPG-frame HLS

## Files
- `NetflixProvider.kt` — `loadLinks()` mobile/hls → s23 primary
- `PrimevideoProvider.kt` — idem (ott="pv")
- `Utils.kt` — `bypass()`, `newTvBaseHeaders`, `m3u8CdnFixInterceptor()`
- `PlutotvProvider/PlutotvProvider.kt` — PlutoTV provider (separado)

## Next Steps
1. ✅ Instalar APK compilado en dispositivo y probar reproducción real
2. ✅ Verificar que los JPG segments de s23 se reproducen en CloudStream
3. ⏸️ Si funciona, replicar en JioHotstarProvider.kt (ott="hs")
