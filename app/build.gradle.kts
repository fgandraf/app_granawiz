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

    // Driver JDBC para SQLite
    implementation("org.xerial:sqlite-jdbc:3.48.0.0")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final") // Hibernate Core
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.4.Final") // Gerenciamento de conexões
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final") // Community Dialects

    // Flyway
    implementation("org.flywaydb:flyway-core:11.1.1") // Flyway para migrações


    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // Phosphor Icons
    implementation("com.adamglin:phosphor-icon:1.0.0")

    // Excel export
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.24.3")

    // OFX parsing
    implementation("com.webcohesion.ofx4j:ofx4j:1.39")

}

tasks.withType<JavaExec> {
    jvmArgs(
        "--enable-native-access=ALL-UNNAMED"
    )
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.felipegandra.generated.resources"
    generateResClass = always
}

compose.desktop {
    application {
        mainClass = "MainKt"

        jvmArgs += listOf(
            "--enable-native-access=ALL-UNNAMED"
        )

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