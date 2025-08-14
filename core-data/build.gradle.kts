plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.hiltAndroid)
	alias(libs.plugins.ksp)
}

android {
	namespace = "com.monyma.core.data"
	compileSdk = 35

	defaultConfig {
		minSdk = 24
		consumerProguardFiles("consumer-rules.pro")
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
		isCoreLibraryDesugaringEnabled = true
	}

	kotlinOptions {
		jvmTarget = "17"
	}
}

dependencies {
	implementation(project(":core-model"))
	implementation(project(":core-database"))

	implementation(libs.hilt.android)
	ksp(libs.hilt.compiler)

	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.android)
}