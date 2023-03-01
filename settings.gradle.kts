pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.8.10"
    }
}

rootProject.name = "autorouter"
include("app")
include("autorouter")
include("autorouter-bench")
