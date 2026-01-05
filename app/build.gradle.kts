import java.util.Properties

plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.travel.travelshare"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.travel.travelshare"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        /*
        val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        */

        //val cloudinaryUrl = properties.getProperty("CLOUDINARY_URL") ?: ""
        val cloudinary_cloud_name = properties.getProperty("CLOUDINARY_CLOUDNAME") ?: ""
        val cloudinary_api_key = properties.getProperty("CLOUDINARY_API_KEY") ?: ""
        val cloudinary_api_secret = properties.getProperty("CLOUDINARY_API_SECRET") ?: ""

        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"${cloudinary_cloud_name}\"")
        buildConfigField( "String", "CLOUDINARY_API_KEY", "\"${cloudinary_api_key}\"")
        buildConfigField("String", "CLOUDINARY_API_SECRET", "\"${cloudinary_api_secret}\"")
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))

    implementation(libs.glide)
    //annotationProcessor(libs.glide.compiler)

    implementation("com.google.firebase:firebase-auth")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    //implementation(libs.play.services.maps)
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    //implementation(libs.firebase.storage)
    implementation("com.cloudinary:cloudinary-android:2.5.0")
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.firebase:geofire-android-common:3.1.0")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")
}