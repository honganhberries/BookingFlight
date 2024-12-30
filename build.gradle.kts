buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("com.android.tools.build:gradle:8.1.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

//repositories {
////    google()
//    mavenCentral()  // Thêm dòng này nếu chưa có
//    jcenter()       // Thêm dòng này nếu chưa có
//}

