plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tbx.admanager"
    compileSdk = 35
    signingConfigs {
        create("release") {
            storeFile = file("/Users/trinhbx/Desktop/key app store/trinhbx.jks")
            storePassword = "trinhbx"
            keyAlias = "trinhbx"
            keyPassword = "trinhbx"
        }
    }
    defaultConfig {
        applicationId = "com.tbx.admanager"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.mediation.test.suite)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    implementation("com.google.android.gms:play-services-ads:23.6.0")
    implementation(project(":admodule"))
//    implementation(files("libs/admodule-release.aar"))
//    implementation(files("libs/vietnq308lib-release.aar"))
//    implementation(files("libs/admodAdNative-1.1.0.aar"))

    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.14")

}