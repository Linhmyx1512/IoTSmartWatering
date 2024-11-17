plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    // parcelize
    id("kotlin-parcelize")
}

android {
    namespace = "com.minhdn.smartwatering"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.minhdn.smartwatering"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }

    defaultConfig {
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${"ce3479c50dbe43008e281338241711"}\"")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    //import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)

    // dimen
    implementation(libs.sdp)
    implementation(libs.ssp)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // play service location
    implementation(libs.play.services.location)

    //swipe refresh layout
    implementation(libs.swipeRefreshLayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}