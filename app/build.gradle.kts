import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.9.10"
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.felipegandra"
version = "1.0.0-Alpha"

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
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.3.1.Final") // Hibernate Core
    implementation("org.hibernate.orm:hibernate-hikaricp:6.3.1.Final") // Gerenciamento de conexões
    implementation("org.hibernate.orm:hibernate-community-dialects:6.3.1.Final") // Community Dialects

    // Flyway
    implementation("org.flywaydb:flyway-core:9.22.0") // Flyway para migrações

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.12")

    // Phosphor Icons
    implementation("com.adamglin:phosphor-icon:1.0.0")

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GranaWiz"
            packageVersion = "1.0.0"
        }
    }
}
