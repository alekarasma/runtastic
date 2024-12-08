plugins {
    alias(libs.plugins.runtastic.android.feature.ui)

}

android {
    namespace = "com.asmaa.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
}