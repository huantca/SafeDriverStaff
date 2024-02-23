
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        applicationId = "com.fc.p.bk.wallpaper.live.hdwallpaper.background"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        archivesName.set(
            "Wallpaper_FC_${versionName}(${versionCode})_${
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.US
                ).format(Date())
            }"
        )

        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            abiFilters.addAll(arrayOf("armeabi-v7a", "arm64-v8a", "x86_64"))
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("key/release_fc")
            storePassword = "bkplus@123"
            keyAlias = "4k_wallpaper"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            manifestPlaceholders["ad_app_id"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField(
                "String",
                "appopen_resume",
                "\"ca-app-pub-3940256099942544/9257395921\""
            )
            buildConfigField("String", "inter_splash", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField(
                "String",
                "native_language",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "native_onbroading",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "native_welcome",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField("String", "Banner_all", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField(
                "String",
                "Banner_home_collapsible",
                "\"ca-app-pub-3940256099942544/2014213617\""
            )
            buildConfigField("String", "Native_home", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField(
                "String",
                "Native_toptrending",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "native_categories",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "native_viewcategories",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField(
                "String",
                "intersitial_backhome",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )
            buildConfigField(
                "String",
                "intersitial_setwallpaper",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )
            buildConfigField(
                "String",
                "native_viewwallpaper",
                "\"ca-app-pub-3940256099942544/2247696110\""
            )
            buildConfigField("String", "reward_gif", "\"ca-app-pub-3940256099942544/5224354917\"")
            buildConfigField("String", "intersitial_viewhistory", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "native_sucsess", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_exit", "\"ca-app-pub-3940256099942544/2247696110\"")
        }
        create("production") {
            manifestPlaceholders["ad_app_id"] = "ca-app-pub-1939315010587936~5129465726"
            buildConfigField("String", "appopen_resume", "\"ca-app-pub-1939315010587936/6488641245\"")
            buildConfigField("String", "inter_splash", "\"ca-app-pub-1939315010587936/2885256398\"")
            buildConfigField("String", "native_language", "\"ca-app-pub-1939315010587936/5018619239\"")
            buildConfigField("String", "native_onbroading", "\"ca-app-pub-1939315010587936/1930449139\"")
            buildConfigField("String", "native_welcome", "\"ca-app-pub-1939315010587936/8077055089\"")
            buildConfigField("String", "Banner_all", "\"ca-app-pub-1939315010587936/2549396230\"")
            buildConfigField("String", "Banner_home_collapsible", "\"ca-app-pub-1939315010587936/9550822817\"")
            buildConfigField("String", "Native_home", "\"ca-app-pub-1939315010587936/8488936863\"")
            buildConfigField("String", "Native_toptrending", "\"ca-app-pub-1939315010587936/9617367461\"")
            buildConfigField("String", "native_categories", "\"ca-app-pub-1939315010587936/7175855191\"")
            buildConfigField("String", "native_viewcategories", "\"ca-app-pub-1939315010587936/1236314565\"")
            buildConfigField("String", "intersitial_backhome", "\"ca-app-pub-1939315010587936/9466158370\"")
            buildConfigField("String", "intersitial_setwallpaper", "\"ca-app-pub-1939315010587936/3705537569\"")
            buildConfigField("String", "native_viewwallpaper", "\"ca-app-pub-1939315010587936/8923232899\"")
            buildConfigField("String", "reward_gif", "\"ca-app-pub-1939315010587936/2392455898\"")
            buildConfigField("String", "intersitial_viewhistory", "\"ca-app-pub-1939315010587936/4983987885\"")
            buildConfigField("String", "native_sucsess", "\"ca-app-pub-1939315010587936/7946011385\"")
            buildConfigField("String", "native_exit", "\"ca-app-pub-1939315010587936/3670906219\"")
        }
    }
}

dependencies {
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
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
    //GroupieAdapter
    implementation("com.xwray:groupie:2.8.1")
    implementation("com.xwray:groupie-viewbinding:2.8.1")
    //Dimens
    implementation("com.intuit.sdp:sdp-android:1.1.0")
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
    implementation("com.bkplus.ads:library:1.0.4-alpha15")
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
    implementation("com.facebook.fresco:fresco:2.3.0")
}
