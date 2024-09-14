plugins {
    `kotlin-dsl`
}

group = "com.asmaa.runtastic.buildlogic"
dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "runtastic.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidComposeApplication") {
            id = "runtastic.android.application.compose"
            implementationClass = "AndroidApplicationComposePlugin"
        }
        register("androidLibrary") {
            id = "runtastic.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidComposeLibrary") {
            id = "runtastic.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUI") {
            id = "runtastic.android.feature.ui"
            implementationClass = "AndroidFeatureUIConventionPlugin"
        }
        register("androidRoom") {
            id = "runtastic.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidJvmLibrary") {
            id = "runtastic.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidJvmKtor") {
            id = "runtastic.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
    }
}

