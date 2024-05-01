plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.a1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.a1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.camera:camera-view:1.3.2")
    testImplementation ("androidx.test:core-ktx:1.1.0")
    implementation ("com.akexorcist:screenshot-detection:1.0.2")

    implementation("androidx.camera:camera-camera2:1.2.3")
    implementation("androidx.camera:camera-lifecycle:1.2.3")
    implementation("androidx.camera:camera-view:1.2.3")

    implementation ("com.google.firebase:firebase-auth:21.0.1") // Use the latest version available
    implementation ("com.google.firebase:firebase-firestore:24.0.1")
    testImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.android.volley:volley:1.2.1")
    testImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation ("com.google.firebase:firebase-analytics:20.1.2")
    implementation ("com.google.code.gson:gson:2.8.6")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.7")
    implementation( "androidx.appcompat:appcompat:1.0.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.0-alpha2")
    testImplementation ("junit:junit:4.12")
    testImplementation("junit:junit:4.12")
    testImplementation("junit:junit:4.12")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation ("androidx.test:runner:1.1.0-beta02")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.1.0-beta02")
    androidTestImplementation ("androidx.test:rules:1.1.0-beta02")
    androidTestImplementation ("androidx.test.ext:junit:1.0.0-beta02")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("io.agora.rtc:voice-sdk:4.3.0")

    // For Intent testing
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // Consider using the rules library if needed
    androidTestImplementation("androidx.test:rules:1.5.0")


}