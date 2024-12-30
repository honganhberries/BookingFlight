plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bookingflight"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookingflight"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true // Làm rối mã
            isShrinkResources = true // Loại bỏ tài nguyên không sử dụng
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
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation ("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-messaging:24.1.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.20")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:retrofit")
    implementation("com.squareup.retrofit2:converter-gson")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.code.gson:gson:2.11.0")
    //Bycrypt
    implementation ("org.mindrot:jbcrypt:0.4")
    //Thư viện hình ảnh
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.20")
    // Google Play Services cho vị trí
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("androidx.core:core:1.15.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    //Email
//    implementation ("com.github.johnkil:android-mailcore2:0.5.1")

//    implementation ("javax.mail:javax.mail-api:1.6.2")
//    implementation ("com.sun.mail:javax.mail:1.6.2")
//    implementation ("org.eclipse.angus:angus-mail:1.0.0")
//    implementation ("org.eclipse.angus:angus-activation:1.0.0")

    implementation ("com.sun.mail:android-mail:1.6.5")
    implementation ("com.sun.mail:android-activation:1.6.5")


    //
//    implementation ("org.bouncycastle:bcprov-jdk15on:1.70") // Bouncy Castle
//    implementation ("org.conscrypt:conscrypt-android:2.5.1")  // Conscrypt
//    implementation ("org.openjsse:openjsse:1.0.0")
}