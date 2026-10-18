// Use an integer for version numbers
version = 3

cloudstream {
    description = "Reproduce listas IPTV (.m3u) y cuentas Xtream Codes (host|usuario|clave). Basado en el plugin IPTV de doGior."
    authors = listOf("Ranita")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1

    tvTypes = listOf("Live")

    requiresResources = true
    language = "un"

    iconUrl = "https://avatars.githubusercontent.com/u/70979529?v=4"
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.material:material:1.13.0")
}
