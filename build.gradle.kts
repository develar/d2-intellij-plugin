import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.GrammarKitConstants
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.7.21"
  id("org.jetbrains.intellij") version "1.10.0"
  id("org.jetbrains.changelog") version "2.0.0"
  id("org.jetbrains.qodana") version "0.1.13"
  id("org.jetbrains.kotlinx.kover") version "0.6.1"
  id("org.jetbrains.grammarkit") version "2022.3"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
  mavenCentral()
  maven(url = "https://cache-redirector.jetbrains.com/intellij-repository/snapshots")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(properties("jvm.target")))
  }

  sourceSets {
    main {
      kotlin.srcDirs("src/main/gen", "src/main/java")
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
  reportPath.set(file("build/reports/inspections").canonicalPath)
  saveReport.set(true)
  showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

kover.xmlReport {
  onCheck.set(true)
}

grammarKit {
  intellijRelease.set(properties("platformVersion"))
}

configurations.getByName(GrammarKitConstants.GRAMMAR_KIT_CLASS_PATH_CONFIGURATION_NAME).apply {
  exclude(mapOf("group" to "io.ktor"))
  exclude(mapOf("group" to "com.jetbrains.infra"))
  exclude(mapOf("group" to "ai.grazie.spell"))
  exclude(mapOf("group" to "ai.grazie.nlp"))
  exclude(mapOf("group" to "ai.grazie.utils"))
  exclude(mapOf("group" to "ai.grazie.model"))
}

tasks {
  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  val generateParser = withType<GenerateParserTask> {
    source.set("./src/main/kotlin/com/dvd/intellij/d2/lang/d2.bnf")
    targetRoot.set("./src/main/gen")
    pathToParser.set("/com/dvd/intellij/d2/lang/D2Parser.java")
    pathToPsiRoot.set("/com/dvd/intellij/d2/lang/psi")
    purgeOldFiles.set(true)
  }
  val generateLexer = withType<GenerateLexerTask> {
    source.set("./src/main/kotlin/com/dvd/intellij/d2/lang/_D2Lexer.flex")
    targetDir.set("./src/main/gen/com/dvd/intellij/d2/lang/")
    targetClass.set("_D2Lexer")
    purgeOldFiles.set(true)
  }
  withType<KotlinCompile> {
    dependsOn(generateParser, generateLexer)

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

//    changeNotes.set(provider {
//      with(changelog) {
//        renderItem(
//          getOrNull(properties("pluginVersion")) ?: getLatest(),
//          Changelog.OutputType.HTML,
//        )
//      }
//    })
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
    channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
  }
}