import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.serializable)
    alias(libs.plugins.kotlinx.serialization)



}

android {
    namespace = "com.srg.neighbourhoodwatchcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.srg.neighbourhoodwatchcompanion"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        debug {
            val p = Properties()
            p.load(project.rootProject.file("local.properties").reader())
            val supabaseKey: String = p.getProperty("SUPABASE_KEY")
            val supabaseUrl: String = p.getProperty("SUPABASE_URL")
            val mapsKey: String = p.getProperty("MAPS_API_KEY")
            val placesKey: String = p.getProperty("PLACES_API_KEY")
            buildConfigField("String", "SUPABASE_KEY", supabaseKey)
            buildConfigField("String", "SUPABASE_URL", supabaseUrl)
            buildConfigField("String", "PLACES_KEY", placesKey)
            resValue("string", "maps_api_key", mapsKey)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
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
    implementation(libs.androidx.splashScreen)
    implementation(libs.timber)
    implementation(libs.firebase.crashlytics)
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.compose.destination)
    implementation(libs.accompanist.permissions)
    ksp(libs.compose.destination.ksp)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.ktor)
    implementation(libs.supabase.realtime)
    implementation(libs.supabase.postgrest)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.androidx.datastore)

    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
    implementation(libs.maps.compose.widgets)
    implementation(libs.places.compose.wrapper)
    implementation(libs.android.places)

    implementation(project(":framework"))
    kapt(libs.dagger.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}