plugins {
    alias(libs.plugins.runtastic.android.library)
    alias(libs.plugins.runtastic.jvm.ktor)
}

android {
    namespace = "com.asmaa.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)

}