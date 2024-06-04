plugins {
    kotlin("jvm") version "1.9.24"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24"

}

group = "me.szydelko"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
//kotlin {
//    jvmToolchain(17)
//}