import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings
import java.nio.file.Files

fun properties(key: String): String = providers.gradleProperty(key).get()

val channel: String = properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.21"
  id("org.jetbrains.intellij") version "1.16.1"
  id("org.jetbrains.changelog") version "2.2.0"
  id("org.jetbrains.qodana") version "2023.2.1"
  id("org.jetbrains.kotlinx.kover") version "0.7.5"

  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"

  id("idea")
  id("org.jetbrains.gradle.plugin.idea-ext") version "0.5"
}

idea {
  module {
    generatedSourceDirs.add(file("src/gen"))
    settings {
      packagePrefix["src/src"] = "org.jetbrains.plugins.d2"
      packagePrefix["src/testSrc"] = "org.jetbrains.plugins.d2"
    }
  }
}

group = "org.jetbrains.plugins.d2"
version = properties("pluginVersion")

repositories {
  mavenCentral()
  maven(url = "https://cache-redirector.jetbrains.com/intellij-repository/snapshots")
}

dependencies {
  @Suppress("SpellCheckingInspection")
  testImplementation("org.opentest4j:opentest4j:1.3.0")
  testImplementation("org.assertj:assertj-core:3.25.1")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  sourceSets {
    main {
      kotlin {
        setSrcDirs(listOf("src/src", "src/gen"))
      }
      resources.setSrcDirs(listOf("src/resources"))
    }
    test {
      kotlin {
        setSrcDirs(listOf("src/testSrc"))
      }
      resources.setSrcDirs(listOf("src/testResources"))
    }
  }
}

sourceSets["main"].java.setSrcDirs(listOf("src/gen"))

intellij {
  pluginName.set(properties("pluginName"))
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))

  plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

changelog {
  groups.set(emptyList())
  repositoryUrl.set(properties("pluginRepositoryUrl"))
}

qodana {
  cachePath.set(file(".qodana").canonicalPath)
  resultsPath.set(file("build/reports/inspections").canonicalPath)
//  saveReport.set(true)
//  showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

koverReport {
  defaults {
    xml {
      onCheck = true
    }
  }
}

tasks {
  wrapper {
    gradleVersion = "8.5"
  }

  test {
    testLogging {
      // set options for log level LIFECYCLE
      events = setOf(
        TestLogEvent.FAILED,
        TestLogEvent.SKIPPED,
        TestLogEvent.STANDARD_OUT
      )
      exceptionFormat = TestExceptionFormat.FULL
      showExceptions = true
      showCauses = true
      showStackTraces = true

      // set options for log level DEBUG and INFO
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

    // *** gradle *** html is not required - failures must be correctly printed to console
    reports.html.required = false
    useJUnitPlatform()

    val overwriteData = providers.gradleProperty("idea.tests.overwrite.data").getOrNull() == "true"
    systemProperty("idea.tests.overwrite.data", overwriteData)
  }

  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    val lines = Files.readString(file("README.md").toPath()).lines()
    val start = lines.indexOf("<!-- Plugin description -->")
    val end = lines.indexOf("<!-- Plugin description end -->")
    if (start < 0 || end < 0) {
      throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
    }
    pluginDescription.set(lines.subList(start + 1, end).joinToString("\n").let { markdownToHTML(it) })

    changeNotes.set(provider {
      changelog.renderItem(item = changelog.getOrNull(properties("pluginVersion")) ?: changelog.getLatest(), outputType = Changelog.OutputType.HTML)
    })
  }

  buildSearchableOptions {
    enabled = false
  }

  runIdeForUiTests {
    systemProperty("robot-server.port", "8082")
    systemProperty("ide.mac.message.dialogs.as.sheets", "false")
    systemProperty("jb.privacy.policy.text", "<!--999.999-->")
    systemProperty("jb.consents.confirmation.enabled", "false")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    dependsOn("patchChangelog")
    token.set(System.getenv("PUBLISH_TOKEN"))

    channels.set(listOf(channel))
  }
}