import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.GrammarKitConstants
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String): String = project.findProperty(key).toString()

val channel: String = properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.21"
  id("org.jetbrains.intellij") version "1.16.1"
  id("org.jetbrains.changelog") version "2.2.0"
  id("org.jetbrains.qodana") version "2023.2.1"
  id("org.jetbrains.kotlinx.kover") version "0.7.4"
  id("org.jetbrains.grammarkit") version "2022.3.2"
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
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(properties("jvm.target")))
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

//kover.xmlReport {
//  onCheck.set(true)
//}

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

  test {
    useJUnitPlatform()

    val overwriteData = providers.gradleProperty("idea.tests.overwrite.data").getOrNull() == "true"
    systemProperty("idea.tests.overwrite.data", overwriteData)
  }

  val generateParser = withType<GenerateParserTask> {
    sourceFile.set(File("./src/main/kotlin/org/jetbrains/plugins/d2/lang/d2.bnf"))
    targetRoot.set("./src/main/gen")
    pathToParser.set("/org/jetbrains/plugins/d2/lang/D2Parser.java")
    pathToPsiRoot.set("/org/jetbrains/plugins/d2/lang/psi")
    purgeOldFiles.set(true)
  }
  val generateLexer = withType<GenerateLexerTask> {
    sourceFile.set(File("./src/main/kotlin/org/jetbrains/plugins/d2/lang/_D2Lexer.flex"))
    targetDir.set("./src/main/gen/org/jetbrains/plugins/d2/lang/")
    targetClass.set("_D2Lexer")
    purgeOldFiles.set(true)
  }
  withType<KotlinCompile> {
    //dependsOn(generateParser, generateLexer)

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