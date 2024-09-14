plugins {
    alias(libs.plugins.runtastic.android.library)
    alias(libs.plugins.runtastic.android.room)
}

android {
    namespace = "com.asmaa.core.database"
}

dependencies {

    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}