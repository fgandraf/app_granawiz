import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.jpa") version "2.3.21"
    id("org.jetbrains.compose") version "1.10.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.21"
}

val appVersion = (project.findProperty("granawiz.version") as? String) ?: "1.0.0"
val appVersionSuffix = (project.findProperty("granawiz.versionSuffix") as? String) ?: ""

group = "com.felipegandra"
version = "$appVersion$appVersionSuffix"

kotlin {
    jvmToolchain(25)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}


dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.components:components-resources:1.10.3")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.53.1.0")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.6.50.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.6.50.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.50.Final")

    // Flyway
    implementation("org.flywaydb:flyway-core:11.1.1")


    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // Phosphor Icons
    implementation("com.adamglin:phosphor-icon:1.0.0")

    // Excel export
    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.26.0")

    // OFX parsing
    implementation("com.webcohesion.ofx4j:ofx4j:1.39")

}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.felipegandra.generated.resources"
    generateResClass = always
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GranaWiz"
            description = "GranaWiz — Personal finance management"
            packageVersion = appVersion
            modules("java.base", "java.sql", "java.naming")
            macOS {
                iconFile.set(project.file("src/main/resources/assets/images/icon.icns"))
            }
            windows{
                iconFile.set(project.file("src/main/resources/assets/images/icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/main/resources/assets/images/icon.png"))
            }
        }
    }
}