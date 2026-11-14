// use an integer for version numbers
version = 5


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "Kimera â€” catÃ¡logo de series, animes, dramas y pelÃ­culas"
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
