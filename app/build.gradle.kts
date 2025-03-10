plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.thewitcherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.thewitcherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.8")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.8")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}