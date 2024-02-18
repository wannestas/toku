import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.apollo3)
}

group = "me.wtas.toku"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    // see libs.versions.toml
    implementation(libs.bundles.desktop)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = name
            packageVersion = "1.0.0"
        }
    }
}

apollo {
    service("anilist") {
        packageName.set("$group.graphql.anilist")
        srcDir("src/main/graphql/anilist")
        introspection {
            endpointUrl.set("https://graphql.anilist.co")
            schemaFile.set(file("src/main/graphql/anilist/schema.graphqls"))
        }

    }
}

kotlin {
    jvmToolchain(17) // also applies to the java task
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}
