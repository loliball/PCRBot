import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    application
}

group = "loli.ball"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    api("net.mamoe:mirai-core:2.12.3")
//    implementation("org.xerial:sqlite-jdbc:3.39.3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    excludes += "META-INF/BC1024KE.DSA"
    excludes += "META-INF/BC1024KE.SF"
    excludes += "META-INF/BC2048KE.DSA"
    excludes += "META-INF/BC2048KE.SF"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(mapOf("Main-Class" to "MainKt"))
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    val sourcesMain = sourceSets.main.get()
    sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
    from(sourcesMain.output)
}