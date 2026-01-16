@file:Suppress("UnstableApiUsage")

// use an integer for version numbers
version = 12

cloudstream {
    description = "Videos, playlists and channels from YouTube"
    authors = listOf("Ranita")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1

    tvTypes = listOf("Others")

    requiresResources = true

    /*iconUrl = "https://www.youtube.com/s/desktop/711fd789/img/logos/favicon_144x144.png"*/
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.github.teamnewpipe:NewPipeExtractor:v0.25.0")
    //noinspection GradleDependency
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.0.4")
}
