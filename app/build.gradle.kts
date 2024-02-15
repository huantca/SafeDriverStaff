import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
}

android {
    namespace = "com.harrison.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.harrison.myapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        archivesName.set(
            "ScreenCall_v${versionName}(${versionCode})_${
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.US
                ).format(Date())
            }"
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("key/bkplus")
            storePassword = "bkplus@123"
            keyAlias = "wifi_password"
            keyPassword = "bkplus@123"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            manifestPlaceholders["ad_app_id"] = "\"ca-app-pub-3940256099942544~3347511713\""
            buildConfigField("String", "open_resume", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "inter_splash", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "native_language", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboard", "\"ca-app-pub-3940256099942544/2247696110\"")
        }
        create("production") {
            manifestPlaceholders["ad_app_id"] = "\"ca-app-pub-3940256099942544~3347511713\""
            buildConfigField("String", "open_resume", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "inter_splash", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "native_language", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboard", "\"ca-app-pub-3940256099942544/2247696110\"")
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
        dataBinding = true
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    //AndroidX
    implementation("androidx.preference:preference-ktx:1.2.1")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    //lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //UI
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.airbnb.android:lottie:6.1.0")
    //Dimens
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //tbuomono dotIndicator
    implementation("com.tbuonomo:dotsindicator:4.3")
    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")
    //Ads
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.0")
    implementation("com.google.gms:google-services:4.4.0")
    //Api
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    //BK Ads Lib
    implementation("com.bkplus.ads:library:1.0.3-alpha15")
    //Appsflyer
    implementation("com.appsflyer:adrevenue:6.9.0")
    // https://mvnrepository.com/artifact/com.appsflyer/af-android-sdk
    implementation("com.appsflyer:af-android-sdk:6.12.5")
    implementation("com.android.installreferrer:installreferrer:2.2")
    implementation("com.miui.referrer:homereferrer:1.0.0.6")
    implementation("com.appsflyer:adrevenue:6.9.0")

    //Mediation Ads
    implementation("com.google.ads.mediation:applovin:12.1.0.0")
    implementation("com.google.ads.mediation:inmobi:10.6.2.0")
    implementation("com.google.ads.mediation:vungle:7.1.0.0") {
        exclude(group = "com.google.protobuf")
    }
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.google.ads.mediation:mintegral:16.5.91.1")
    implementation("com.google.ads.mediation:pangle:5.7.0.2.0")
}