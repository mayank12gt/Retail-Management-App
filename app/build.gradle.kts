plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.shopmanager"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.shopmanager"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.makeramen:roundedimageview:2.3.0")


    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson Library dependency for json to object conversion
    implementation ("com.google.code.gson:gson:2.8.6")




    implementation ("androidx.room:room-runtime:2.5.2")
    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")

    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.2")
    implementation ("com.karumi:dexter:6.2.3")

    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-view:1.1.0")
    implementation ("androidx.camera:camera-extensions:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")

    implementation ("com.itextpdf:itext7-core:7.1.15")


    implementation ("com.github.kenglxn.QRGen:android:3.0.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson Library dependency for json to object conversion
    implementation ("com.google.code.gson:gson:2.8.6")










}