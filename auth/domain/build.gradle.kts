plugins {
    alias(libs.plugins.runtastic.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}