import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String): String = providers.gradleProperty(key).get()

val channel: String = properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.21"
  id("org.jetbrains.intellij") version "1.16.1"
  id("org.jetbrains.changelog") version "2.2.0"
  id("org.jetbrains.qodana") version "2023.2.1"
  id("org.jetbrains.kotlinx.kover") version "0.7.4"

  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
  mavenCentral()
  maven(url = "https://cache-redirector.jetbrains.com/intellij-repository/snapshots")
}

dependencies {
  testImplementation("org.assertj:assertj-core:3.11.1")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(properties("jvm.target")))
    vendor = JvmVendorSpec.JETBRAINS
  }

  sourceSets {
    main {
      kotlin.srcDirs("src/main/gen")
    }
  }
}

sourceSets["main"].java.srcDirs("src/main/gen")

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
    gradleVersion = properties("gradleVersion")
  }

  test {
    useJUnitPlatform()

    val overwriteData = providers.gradleProperty("idea.tests.overwrite.data").getOrNull() == "true"
    systemProperty("idea.tests.overwrite.data", overwriteData)
  }

  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = properties("jvm.target")
    }
  }

  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    pluginDescription.set(
      file("README.md").readText().lines().run {
        val start = "<!-- Plugin description -->"
        val end = "<!-- Plugin description end -->"

        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end))
      }.joinToString("\n").let { markdownToHTML(it) }
    )

    changeNotes.set(provider {
      with(changelog) {
        renderItem(
          getOrNull(properties("pluginVersion")) ?: getLatest(),
          Changelog.OutputType.HTML,
        )
      }
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