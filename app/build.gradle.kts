import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.uit.melodydiary"
    compileSdk = 34

    val properties = Properties()
    properties.load(
        project.rootProject.file("local.properties")
            .inputStream()
    )

    defaultConfig {
        applicationId = "com.uit.melodydiary"
        minSdk = 26
        targetSdk = 34
        versionCode = 10
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "API_KEY",
            "\"${properties.getProperty("API_KEY")}\""
        )
        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${properties.getProperty("WEB_CLIENT_ID")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.protolite.well.known.types)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material:material:1.6.3")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.1")


    val nav_version = "2.7.7"

    implementation("androidx.navigation:navigation-compose:$nav_version")


    // room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")


    // Retrofit
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // The compose calendar library
    implementation("com.kizitonwose.calendar:compose:2.5.0")

    // chart
    implementation("com.github.jaikeerthick:Composable-Graphs:v1.2.3") //ex: v1.2.3
    implementation("com.github.tehras:charts:0.2.4-alpha")

    implementation("androidx.core:core-splashscreen:1.0.1")

    //icon
    implementation("com.github.Abhimanyu14:compose-emoji-picker:1.0.0-alpha16")
    val emoji2_version = "1.4.0"
    implementation("androidx.emoji2:emoji2:$emoji2_version")

    // google sign in
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // drive
    implementation("com.google.api-client:google-api-client-android:1.33.2")
    implementation("com.google.api-client:google-api-client-gson:1.33.2")

    // logging
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")


//    implementation("com.google.api-client:google-api-client-android:1.26.0") {
//        exclude(group = "org.apache.httpcomponents")
//        exclude(module = "guava-jdk5")
//    }
//
    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0") {
        exclude(group = "org.apache.httpcomponents")
        exclude(module = "guava-jdk5")
    }

    // exo player
    implementation(libs.androidx.media3.media3.exoplayer3)
    implementation(libs.androidx.media3.media3.exoplayer.dash)
    implementation(libs.androidx.media3.media3.exoplayer.hls)
    implementation(libs.androidx.media3.media3.exoplayer.rtsp)
    implementation(libs.androidx.media3.media3.exoplayer.smoothstreaming)
    implementation(libs.androidx.media3.media3.session)
    implementation(libs.androidx.media3.media3.container)
    implementation(libs.androidx.media3.media3.datasource.rtmp)
    implementation(libs.androidx.media3.media3.datasource.okhttp)
    implementation(libs.androidx.media3.media3.extractor)
    implementation(libs.androidx.media3.media3.ui)

}