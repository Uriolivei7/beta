// use an integer for version numbers
version = 1


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "Kimera — catálogo de series, animes, dramas y películas"
    language    = "es"
    authors = listOf("Ranita")

    /**
    * Status int as the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta only
    * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "Anime",
        "TvSeries",
        "Movie",
    )
    iconUrl = "https://kimeraweb.com/icon.svg"
    isCrossPlatform = false
}
