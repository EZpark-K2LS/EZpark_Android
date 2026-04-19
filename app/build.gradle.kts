plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.ezpark.parking"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.ezpark.parking"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        resourceConfigurations += listOf("en", "ko")
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin { jvmToolchain(21) }

dependencies {
    implementation(platform(libs.composeBom))
    implementation(libs.composeUi)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    debugImplementation(libs.composeUiTooling)
    implementation(libs.activityCompose)
    implementation(libs.lifecycle)
    implementation(libs.navigationCompose)
    implementation(libs.coreKtx)
    implementation(libs.osmdroid)

    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation(libs.junitAndroidTest)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTestManifest)
}
