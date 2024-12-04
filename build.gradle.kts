plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.dokka") version "1.9.20"
    jacoco
}

group = "setu.ie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    //4 Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.thoughtworks.xstream:xstream:1.4.18")
    implementation("org.codehaus.jettison:jettison:1.4.1")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.20")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(16)
}