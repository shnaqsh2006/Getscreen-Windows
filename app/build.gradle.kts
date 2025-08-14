plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlinCompose)
}

android {
	namespace = "com.monyma.app"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.monyma.app"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "0.1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables.useSupportLibrary = true
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
		isCoreLibraryDesugaringEnabled = true
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	buildFeatures {
		compose = true
	}
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material.icons.extended)

	debugImplementation(libs.androidx.compose.ui.tooling)
	coreLibraryDesugaring(libs.desugar.jdk.libs)

	implementation(libs.hilt.android)
	ksp(libs.hilt.compiler)
	implementation(libs.androidx.hilt.navigation.compose)

	implementation(project(":core-model"))
	implementation(project(":core-database"))
	implementation(project(":core-data"))
	implementation(project(":feature-dashboard"))
	implementation(project(":feature-transactions"))
	implementation(project(":feature-budgets"))
	implementation(project(":feature-reports"))
	implementation(project(":feature-settings"))
}