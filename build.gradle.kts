import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "uk.toadl3ss"
version = "1.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

val jdaVersion = "4.2.0_223"
val logbackVersion = "1.3.0-alpha5"
val lavaplayerVersion = "1.3.66"
val yamlVersion = "1.7.2"
val groovyVersion = "3.0.7"
val mongoVersion = "3.12.7"
val jsonVersion = "20180813"

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation ("com.sedmelluq:lavaplayer:$lavaplayerVersion")
    implementation ("me.carleslc.Simple-YAML:Simple-Yaml:$yamlVersion")
    implementation ("org.codehaus.groovy:groovy-jsr223:$groovyVersion")
    compile ("org.mongodb:mongo-java-driver:$mongoVersion")
    implementation ("org.json:json:$jsonVersion")
}

application {
    mainClassName = "uk.toadl3ss.peepocop.main.Launcher"
}

tasks.withType<ShadowJar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
    archiveFileName.set("peepoCop.jar")
}