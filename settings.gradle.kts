pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://maven.pkg.github.com/BKPlusResearch/BKPlus-Ads")
            credentials {
                username = "bkplus.firebase.apero@gmail.com"
                password = "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"
            }
        }

        //AppFlyer
        maven {
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
        maven {
            url = uri("https://artifact.bytedance.com/repository/pangle/")
        }
    }

}

rootProject.name = "4K Wallpaper"
include(":app")
