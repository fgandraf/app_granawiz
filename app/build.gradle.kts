import org.gradle.jvm.toolchain.JavaLanguageVersion
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

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/buildconfig/kotlin")
    inputs.property("version", "$appVersion$appVersionSuffix")
    outputs.dir(outputDir)
    doLast {
        val dir = outputDir.get().asFile
        dir.mkdirs()
        File(dir, "BuildConfig.kt").writeText(
            "object BuildConfig {\n    const val APP_VERSION = \"$appVersion$appVersionSuffix\"\n}\n"
        )
    }
}

kotlin {
    jvmToolchain(25)
    sourceSets {
        main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/buildconfig/kotlin"))
        }
    }
}

tasks.named("compileKotlin") {
    dependsOn(generateBuildConfig)
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

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.h2database:h2:2.2.224")
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.felipegandra.generated.resources"
    generateResClass = always
}

compose.desktop {
    application {
        mainClass = "MainKt"
        javaHome = javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(25)
        }.get().metadata.installationPath.asFile.absolutePath

        jvmArgs += listOf("--enable-native-access=ALL-UNNAMED")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GranaWiz"
            packageVersion = appVersion
            description = "GranaWiz — Personal finance management"
            copyright = "© 2026 Felipe Ferreira Gandra"

            windows {
                console = false
                menu = true
                menuGroup = "Granawiz"
                shortcut = true
                perUserInstall = true
                upgradeUuid = "750021da-dce4-4670-aa77-24f3401d97a8"
                iconFile.set(project.file("src/main/resources/assets/images/icon.ico"))
            }

            macOS {
                bundleID = "com.felipegandra.granawiz"
                iconFile.set(project.file("src/main/resources/assets/images/icon.icns"))
            }

            linux {
                menuGroup = "Granawiz"
                iconFile.set(project.file("src/main/resources/assets/images/icon.png"))
            }

            appResourcesRootDir.set(project.layout.projectDirectory.dir("installer-resources"))

            modules(
                "java.base",
                "java.naming",
                "java.instrument",
                "java.net.http",
                "java.xml.crypto",
                "jdk.unsupported",
                "java.sql",
                "java.xml",
                "java.desktop",
                "java.logging",
                "jdk.localedata",
                "java.management"
            )
        }
    }
}

if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
    val resignAppAdHoc by tasks.registering(Exec::class) {
        dependsOn("createDistributable")
        val appDir = layout.buildDirectory.dir("compose/binaries/main/app/GranaWiz.app")
        onlyIf { appDir.get().asFile.exists() }
        commandLine("codesign", "--force", "--deep", "--sign", "-", appDir.get().asFile.absolutePath)
    }
    tasks.matching { it.name == "packageDmg" }.configureEach {
        dependsOn(resignAppAdHoc)
    }
}