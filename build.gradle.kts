import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("com.apollographql.apollo3") version "4.0.0-beta.4"
}

group = "me.wtas.fileLocator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation("io.ktor:ktor:2.3.8")
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.8")
    implementation("com.apollographql.apollo3:apollo-runtime")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Anime_Locator"
            packageVersion = "1.0.0"
        }
    }
}

apollo {
    service("anilist") {
        packageName.set(group.toString() + ".graphql.anilist")
        srcDir("src/main/graphql/anilist")
        introspection {
            endpointUrl.set("https://graphql.anilist.co")
            schemaFile.set(file("src/main/graphql/anilist/schema.graphqls"))
        }

    }
}

tasks {
    withType(KotlinCompile::class) {
        kotlinOptions {
            jvmTarget = "21"
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }
}