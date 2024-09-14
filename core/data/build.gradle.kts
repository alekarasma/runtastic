plugins {
    alias(libs.plugins.runtastic.android.library)
    alias(libs.plugins.runtastic.jvm.ktor)
}

android {
    namespace = "com.asmaa.core.data"
}

dependencies {

    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.bundles.koin)

}