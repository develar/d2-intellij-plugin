import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
// ------------------------------------------------------------------------------
// Delegated Gradle Properties
// ------------------------------------------------------------------------------

val pluginVersion: String by project
val pluginGroup: String by project
val pluginName: String by project
val pluginRepositoryUrl: String by project

val platformType: String by project
val platformVersion: String by project
val platformSinceBuild: String by project
val platformUntilBuild: String by project

val platformPlugins: String by project
val platformBundledPlugins: String by project

// ------------------------------------------------------------------------------
// Plugins
// ------------------------------------------------------------------------------
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.changelog") version "2.2.1"

    // Migration to new plugin
    // Replaces old "org.jetbrains.intellij" plugin version "1.x"
    id("org.jetbrains.intellij.platform") version "2.2.1"

    id("org.jetbrains.qodana") version "2024.3.4"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"

    id("org.jetbrains.grammarkit") version "2022.3.2.2"


    id("idea")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
}

// ------------------------------------------------------------------------------
// Project/Plugin Group and Version
// ------------------------------------------------------------------------------
group = pluginGroup       // e.g. "org.jetbrains.plugins.d2"
version = pluginVersion   // e.g. "1.0.0"

// ------------------------------------------------------------------------------
// Repositories
// ------------------------------------------------------------------------------
repositories {
    mavenCentral()
    intellijPlatform{
        defaultRepositories()
        jetbrainsRuntime()

    }
}

// ------------------------------------------------------------------------------
// IntelliJ Platform Dependencies
// ------------------------------------------------------------------------------
dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.3", useInstaller = false)
        jetbrainsRuntime()
        bundledPlugins(platformBundledPlugins.split(","))
        testFramework(TestFrameworkType.Platform)
    }

    // Other dependencies
    @Suppress("SpellCheckingInspection")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("junit:junit:4.13.2")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

// ------------------------------------------------------------------------------
// Kotlin Configuration
// ------------------------------------------------------------------------------
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceSets {
        main {
            kotlin.setSrcDirs(listOf("src/src", "src/gen"))
            resources.setSrcDirs(listOf("src/resources"))
        }
        test {
            kotlin.setSrcDirs(listOf("src/testSrc"))
            resources.setSrcDirs(listOf("src/testResources"))
        }
    }
}

// We set main sourceSets in addition to the Kotlin DSL above
sourceSets["main"].java.setSrcDirs(listOf("src/gen"))


// ------------------------------------------------------------------------------
// Changelog Configuration
// ------------------------------------------------------------------------------
changelog {
    groups.set(emptyList())
    repositoryUrl.set(pluginRepositoryUrl)
}

// ------------------------------------------------------------------------------
// Qodana Configuration
// ------------------------------------------------------------------------------
qodana {
    cachePath.set(file(".qodana").canonicalPath)
    resultsPath.set(file("build/reports/inspections").canonicalPath)
    //  saveReport.set(true)
    //  showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

// ------------------------------------------------------------------------------
// Kotlinx Kover (Coverage) Configuration
// ------------------------------------------------------------------------------
koverReport {
    defaults {
        xml {
            onCheck = true
        }
    }
}

// ------------------------------------------------------------------------------
// Idea Project Configuration (idea-ext)
// ------------------------------------------------------------------------------
idea {
    module {
        generatedSourceDirs.add(file("src/gen"))
        settings {
            // Maps the custom source/test directories to package prefix
            packagePrefix["src/src"] = "org.jetbrains.plugins.d2"
            packagePrefix["src/testSrc"] = "org.jetbrains.plugins.d2"
        }
    }
}

// ------------------------------------------------------------------------------
// Tasks Configuration
// ------------------------------------------------------------------------------
tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_OUT
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true

            debug {
                events = setOf(
                    TestLogEvent.STARTED,
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_ERROR,
                    TestLogEvent.STANDARD_OUT
                )
                exceptionFormat = TestExceptionFormat.FULL
            }
            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat
        }

        reports.html.required = false

        // e.g. to allow overwriting data in tests
        val overwriteData = providers.gradleProperty("idea.tests.overwrite.data").getOrNull() == "true"
        systemProperty("idea.tests.overwrite.data", overwriteData)
    }
}