// use an integer for version numbers
version = 1

cloudstream {
    language = "mx"
    description = "Mira tus Series, Películas y Animes en Latino Online!"
    authors = listOf("ByAyzen")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "Movie",
        "TvSeries",
        "Anime",
        "AsianDrama"
    )

    iconUrl = "https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://flixlatam.com&size=128"
}