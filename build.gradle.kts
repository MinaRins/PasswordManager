plugins {
    kotlin("jvm") version "2.0.21"
}

group = "setu.ie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.osha1:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(jdkVersion : 16)
}