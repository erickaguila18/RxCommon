@file:Suppress("UnstableApiUsage")

import Publishing.addRepositories
import Publishing.mutatePublicationPom
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.android.library")
    id("maven-publish")
    signing
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(18)
        targetSdkVersion(29)
        versionCode = 1
        versionName = Versions.rxcommon
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

val dokka by tasks.getting(DokkaTask::class) {
    outputDirectory = "$buildDir/dokka"
    outputFormat = "html"

    multiplatform {}
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    archiveClassifier.set("javadoc")
    from(dokka)
}

kotlin {
    configure(
        listOf(
            metadata()
        )
    ) {
        mavenPublication {
            artifactId = "rxcommon-binding-${this.artifactId.substring(8)}"

            mutatePublicationPom(projectName = "RxCommon")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":rxcommon-core"))
                implementation(kotlin("stdlib-common"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

publishing {
    addRepositories(project)
    publications.withType<MavenPublication>().apply {
        val kotlinMultiplatform by getting {
            artifactId = "rxcommon-binding"

            mutatePublicationPom(projectName = "RxCommon")
        }
    }
}

signing {
    if (Properties.isRelease) {
        sign(publishing.publications)
    }
}
