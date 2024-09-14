plugins {
    alias(libs.plugins.runtastic.android.library)
}

android {
    namespace = "com.asmaa.run.data"
}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.database)
    implementation(projects.core.domain)
}