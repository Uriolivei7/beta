// use an integer for version numbers
version = 4


cloudstream {
    language = "es"
    description = "TV en vivo de todo el mundo. Canales en vivo de cxtvenvivo.com."
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
        "Live",
    )

    iconUrl = "https://www.cxtvenvivo.com/assets/img/ui/favicon.png"
}
