plugins {
    alias(libs.plugins.runtastic.android.library)
}

android {
    namespace = "com.asmaa.run.location"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.bundles.koin)
    implementation(project(":run:domain"))
    implementation(libs.play.services.location)
    implementation(project(":core:domain"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}