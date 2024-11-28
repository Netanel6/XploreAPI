plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.netanel"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "3.0.0"
    val koinVersion = "4.0.0"

    testImplementation(kotlin("test"))

    implementation("ch.qos.logback:logback-classic:1.4.5")

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

    // MongoDB
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Koin
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koinVersion"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-ktor")
    implementation("io.insert-koin:koin-logger-slf4j")






    implementation("com.vladsch.flexmark:flexmark-all:0.64.0")
}

kotlin {
    jvmToolchain(17)
}
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("all")
    manifest {
        attributes(mapOf("Main-Class" to "org.netanel.MainKt"))
    }
}

tasks.register("stage") {
    dependsOn("clean", "shadowJar")
}

tasks.test {
    useJUnitPlatform()
}