## Goal
- Complete `MhdflixProvider` by integrating metadata extraction from Next.js RSC and video extraction via the internal API `/api/links`.

## Constraints & Preferences
- Parse Next.js RSC (`self.__next_f.push`) for site metadata.
- Use discovered internal API `GET /api/links` for video link extraction.
- Support Movies, TV Series, Anime, and Doramas categories.
- `VecindadchProvider` is complete (search disabled, posters fixed, seasons grouped).

## Progress
### Done
- Created `MhdflixProvider.kt` replacing the `SoloLatino` template.
- Implemented RSC parsing logic for Home page, Search, and Media Details.
- Resolved Kotlin compilation errors (duration parsing, type inference, episode data classes).
- Implemented `loadLinks` to use internal API `GET https://ww1.mhdflix.com/api/links?id={id}&type={movie|episode}`.
- API response parsed to create `ExtractorLink` objects with server name, language, and quality.
- Subtitle extraction added from API response.
- Fallback to iframe extraction for non-direct URLs.
- Successfully compiled `MhdflixProvider-release.aar`.
- `VecindadchProvider` completed and compiled.

### In Progress
- (none)

### Blocked
- (none)

## Key Decisions
- Use `/api/links` API instead of scraping iframes/RSC for video sources, as it provides structured JSON with server names and languages.
- Keep RSC parsing for UI navigation (titles, posters, episode lists) where no specific API endpoint was found.
- `VecindadchProvider`: Disabled search as home navigation covers all content efficiently.

## Next Steps
- Test `MhdflixProvider` with CloudStream to verify:
  - Home page loads correctly with recent movies/series/anime/doramas.
  - Search returns results.
  - Detail pages show correct metadata.
  - Episode selection works for series.
  - Video links are extracted and playable.

## Critical Context
- **API Endpoint:** `GET https://ww1.mhdflix.com/api/links?id={media_id}&type={movie|episode}`.
- **API Response:** JSON Array `[{ url, language: { name }, quality: { name }, server: { name }, subtitles }]`.
- **Languages:** "Latino", "Castellano", "Sub lat".
- **Servers:** streamtape, dood, streamwish, filelions, streamhub, streamvid, luluvdo, netu, uqload, netuplayer, mixdrop, bysejikuar, vidhidepro, voe.
- **Domain Discrepancy:** Provider uses `mhdflix.com`, but API calls were captured on `ww1.mhdflix.com`.
- **mhdflix.com** is a Next.js App Router site; metadata is in `self.__next_f.push` scripts.

## Relevant Files
- `C:\Users\Ruth Riveiro\AndroidStudioProjects\beta\MhdflixProvider\src\main\kotlin\com\example\MhdflixProvider.kt`: Provider logic with API integration.
- `C:\Users\Ruth Riveiro\AndroidStudioProjects\beta\MhdflixProvider\build\outputs\aar\MhdflixProvider-release.aar`: Compiled artifact.
- `C:\Users\Ruth Riveiro\AndroidStudioProjects\beta\MhdflixProvider\src\main\kotlin\com\example\MhdflixPlugin.kt`: Plugin registration.
- `C:\Users\Ruth Riveiro\AndroidStudioProjects\beta\VecindadchProvider\src\main\kotlin\com\example\VecindadchProvider.kt`: Completed provider.
