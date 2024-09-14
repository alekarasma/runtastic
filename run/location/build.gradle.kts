plugins {
    alias(libs.plugins.runtastic.android.library)
}

android {
    namespace = "com.asmaa.run.location"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}