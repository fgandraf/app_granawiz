import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.jpa") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

group = "com.felipegandra"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}


dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
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

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GranaWiz"
            description = "Version Alpha 1"
            packageVersion = "1.0.0"
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