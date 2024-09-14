pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Runtastic"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":auth:data")
include(":auth:presentation")
include(":auth:domain")
include(":core:data")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:domain")
include(":core:database")
include(":run:data")
include(":run:presentation")
include(":run:domain")
include(":run:location")
include(":run:network")

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
//gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:testClasses"))