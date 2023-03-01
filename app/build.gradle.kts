plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

tasks.compileKotlin {
    kotlinOptions {
        javaParameters = true
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:5.3.2")
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation(project(":autorouter"))


    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Use the JUnit 5 integration.
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

application {
    // Define the main class for the application.
    mainClass.set("pt.isel.AppKt")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
