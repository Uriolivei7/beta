// use an integer for version numbers
version = 1


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "Animes y películas en español desde animejara.com"
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

    // List of video source types. Users are able to filter for extensions in a given category.
    // You can find a list of available types here:
    // https://recloudstream.github.io/cloudstream/html/app/com.lagradost.cloudstream3/-tv-type/index.html
    tvTypes = listOf("Anime","Movie")
    //iconUrl = "https://raw.githubusercontent.com/phisher98/TVVVV/refs/heads/main/Icons/animeav1.png"

    isCrossPlatform = false
}
