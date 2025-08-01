pluginManagement {
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

rootProject.name = "Openlysis"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":feature:tools")
include(":core:designsystem")
include(":core:data:analysis:model")
include(":core:data:analysis:core")
include(":core:data:remote")
include(":core:data:database")
include(":core:data:attachment")
include(":feature:results")
include(":core:common")
include(":core:data:datastore")
include(":core:data:auth")
include(":core:data:datastore-proto")
include(":feature:auth")
include(":core:notification")
include(":core:data:work")
