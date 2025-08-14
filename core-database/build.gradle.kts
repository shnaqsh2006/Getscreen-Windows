plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinAndroid)
	alias(libs.plugins.ksp)
}

android {
	namespace = "com.monyma.core.database"
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

	implementation(libs.room.runtime)
	implementation(libs.room.ktx)
	ksp(libs.room.compiler)
}