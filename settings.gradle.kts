pluginManagement {
	repositories {
		google()
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

rootProject.name = "MONYMA"

include(":app")
include(":core-model")
include(":core-database")
include(":core-data")
include(":feature-dashboard")
include(":feature-transactions")
include(":feature-budgets")
include(":feature-reports")
include(":feature-settings")