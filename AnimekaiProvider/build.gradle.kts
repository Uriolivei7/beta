//import org.jetbrains.kotlin.konan.properties.Properties

// use an integer for version numbers
version = 56


android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        buildConfigField("String", "KAISVA", "\"https://sva.animekai.la\"")
        buildConfigField("String", "KAIDEC", "\"https://api.animekai.la/api/v1/decrypt\"")
        buildConfigField("String", "KAIENC", "\"https://api.animekai.la/api/v1/encrypt\"")
        buildConfigField("String", "KAIMEG", "\"https://megcloud.tv\"")
    }
}
dependencies {
    implementation("com.google.android.material:material:1.13.0")
}

cloudstream {
    language = "en"
    // All of these properties are optional, you can safely remove them
    description = "Animes y Películas Animes"
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
        "OVA",
        "AnimeMovie"
    )

    //iconUrl = "https://www.google.com/s2/favicons?domain=animekai.to&sz=%size%"

    requiresResources = true
    isCrossPlatform = false
}