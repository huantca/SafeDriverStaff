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

rootProject.name = "base-app-bkplus"
include(":app")
