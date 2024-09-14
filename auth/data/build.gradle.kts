plugins {
    alias(libs.plugins.runtastic.android.library)
    alias(libs.plugins.runtastic.jvm.ktor)
}

android {
    namespace = "com.asmaa.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.bundles.koin)
}