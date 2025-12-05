@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties

version = 486

// 1. Definir la lógica de carga de propiedades al inicio
val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    // Si el archivo existe (entorno local), cargarlo.
    properties.load(localPropertiesFile.inputStream())
} else {
    // Si no existe (entorno CI), no hay problema, usaremos System.getenv más tarde.
    println("Advertencia: local.properties no encontrado. Cargando desde variables de entorno de CI.")
}

// 2. Función de ayuda para obtener la propiedad (prioriza local.properties, luego System.getenv)
fun getProp(name: String): String {
    // Busca en las propiedades cargadas, o recurre a System.getenv (variables de entorno de CI), o usa una cadena vacía.
    return properties.getProperty(name) ?: System.getenv(name) ?: ""
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        // android.buildFeatures.buildConfig=true  // Esto es redundante, ya está en buildFeatures

        buildConfigField("String", "TMDB_API", "\"${getProp("TMDB_API")}\"")
        buildConfigField("String", "CINEMATV_API", "\"${getProp("CINEMATV_API")}\"")
        buildConfigField("String", "SFMOVIES_API", "\"${getProp("SFMOVIES_API")}\"")
        buildConfigField("String", "ZSHOW_API", "\"${getProp("ZSHOW_API")}\"")
        buildConfigField("String", "DUMP_API", "\"${getProp("DUMP_API")}\"")
        buildConfigField("String", "DUMP_KEY", "\"${getProp("DUMP_KEY")}\"")
        buildConfigField("String", "CRUNCHYROLL_BASIC_TOKEN", "\"${getProp("CRUNCHYROLL_BASIC_TOKEN")}\"")
        buildConfigField("String", "CRUNCHYROLL_REFRESH_TOKEN", "\"${getProp("CRUNCHYROLL_REFRESH_TOKEN")}\"")
        buildConfigField("String", "MOVIE_API", "\"${getProp("MOVIE_API")}\"")
        buildConfigField("String", "ANICHI_API", "\"${getProp("ANICHI_API")}\"")
        buildConfigField("String", "Whvx_API", "\"${getProp("Whvx_API")}\"")
        buildConfigField("String", "CatflixAPI", "\"${getProp("CatflixAPI")}\"")
        buildConfigField("String", "ConsumetAPI", "\"${getProp("ConsumetAPI")}\"")
        buildConfigField("String", "FlixHQAPI", "\"${getProp("FlixHQAPI")}\"")
        buildConfigField("String", "WhvxAPI", "\"${getProp("WhvxAPI")}\"")
        buildConfigField("String", "WhvxT", "\"${getProp("WhvxT")}\"")
        buildConfigField("String", "SharmaflixApikey", "\"${getProp("SharmaflixApikey")}\"")
        buildConfigField("String", "SharmaflixApi", "\"${getProp("SharmaflixApi")}\"")
        buildConfigField("String", "Theyallsayflix", "\"${getProp("Theyallsayflix")}\"")
        buildConfigField("String", "GojoAPI", "\"${getProp("GojoAPI")}\"")
        buildConfigField("String", "HianimeAPI", "\"${getProp("HianimeAPI")}\"")
        buildConfigField("String", "Vidsrccc", "\"${getProp("Vidsrccc")}\"")
        buildConfigField("String", "WASMAPI", "\"${getProp("WASMAPI")}\"")
        buildConfigField("String", "KissKh", "\"${getProp("KissKh")}\"")
        buildConfigField("String", "KisskhSub", "\"${getProp("KisskhSub")}\"")
        buildConfigField("String", "SUPERSTREAM_THIRD_API", "\"${getProp("SUPERSTREAM_THIRD_API")}\"")
        buildConfigField("String", "SUPERSTREAM_FOURTH_API", "\"${getProp("SUPERSTREAM_FOURTH_API")}\"")
        buildConfigField("String", "SUPERSTREAM_FIRST_API", "\"${getProp("SUPERSTREAM_FIRST_API")}\"")
        buildConfigField("String", "StreamPlayAPI", "\"${getProp("StreamPlayAPI")}\"")
        buildConfigField("String", "PROXYAPI", "\"${getProp("PROXYAPI")}\"")
        buildConfigField("String", "KAISVA", "\"${getProp("KAISVA")}\"")
        buildConfigField("String", "MOVIEBOX_SECRET_KEY_ALT", "\"${getProp("MOVIEBOX_SECRET_KEY_ALT")}\"")
        buildConfigField("String", "MOVIEBOX_SECRET_KEY_DEFAULT", "\"${getProp("MOVIEBOX_SECRET_KEY_DEFAULT")}\"")
        buildConfigField("String", "KAIMEG", "\"${getProp("KAIMEG")}\"")
        buildConfigField("String", "KAIDEC", "\"${getProp("KAIDEC")}\"")
        buildConfigField("String", "KAIENC", "\"${getProp("KAIENC")}\"")
        buildConfigField("String", "Nuviostreams", "\"${getProp("Nuviostreams")}\"")
        buildConfigField("String", "VideasyDEC", "\"${getProp("VideasyDEC")}\"")
        buildConfigField("String", "YFXENC", "\"${getProp("YFXENC")}\"")
        buildConfigField("String", "YFXDEC", "\"${getProp("YFXDEC")}\"")
    }
}


cloudstream {
    language = "en"
    // All of these properties are optional, you can safely remove them

     description = "#1 best extention based on MultiAPI"
     authors = listOf("Phisher98", "Hexated")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "Anime"
    )

    iconUrl = "https://i3.wp.com/yt3.googleusercontent.com/ytc/AIdro_nCBArSmvOc6o-k2hTYpLtQMPrKqGtAw_nC20rxm70akA=s900-c-k-c0x00ffffff-no-rj?ssl=1"

    requiresResources = true
    isCrossPlatform = false

}

dependencies {
    // FIXME remove this when crossplatform is fully supported
    val cloudstream by configurations
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.browser:browser:1.9.0")
    cloudstream("com.lagradost:cloudstream3:pre-release")
}
