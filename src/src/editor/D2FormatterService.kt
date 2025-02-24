package org.jetbrains.plugins.d2.editor

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessNotCreatedException
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2_NOTIFICATION_GROUP
import org.jetbrains.plugins.d2.file.D2File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

private val ERROR_REGEX = Regex("err: failed to fmt: .*:\\d*:?\\d*: (.*)")

private class D2FormatterService : AsyncDocumentFormattingService() {
    override fun getName(): String = D2Bundle.message("d2.formatter")

    override fun getNotificationGroupId(): String = D2_NOTIFICATION_GROUP

    override fun getFeatures() = emptySet<FormattingService.Feature>()

    override fun canFormat(file: PsiFile): Boolean = file is D2File

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val file = request.ioFile?.toPath() ?: return null
        return object : FormattingTask {
            override fun run() {
                if (!isCompilerInstalled()) {
                    request.onError("", D2Bundle.message("d2.executable.not.found.format.notification"))
                    return
                }

                format(file, request)
            }

            override fun isRunUnderProgress(): Boolean = true

            override fun cancel(): Boolean = true
        }
    }
}

private fun format(file: Path, request: AsyncFormattingRequest) {
    try {
        val result = Files.createTempFile(file.fileName.toString(), ".formatted.d2")
        val command = GeneralCommandLine("d2").withCharset(Charsets.UTF_8)
        val params = command.parametersList
        params.add("fmt")
        Files.copy(file, result, StandardCopyOption.REPLACE_EXISTING)
        params.add(result.toString())
        val output = ScriptRunnerUtil.getProcessOutput(
            command,
            ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
            500
        )
        if (output.contains("err: failed")) {
            Files.deleteIfExists(result)
            request.onError(
                D2Bundle.message("d2.formatter"),
                D2Bundle.message("d2.formatter.error", ERROR_REGEX.find(output)?.groupValues?.get(1) ?: output)
            )
            return
        } else {
            val content = Files.readString(result)
            Files.deleteIfExists(result)
            request.onTextReady(content)
        }
    } catch (e: Exception) {
        logger<D2FormatterService>().error(e)
        request.onError(
            D2Bundle.message("d2.formatter"),
            D2Bundle.message("d2.formatter.error", e.message!!)
        )
    }
}

private fun isCompilerInstalled(): Boolean {
    try {
        val process = OSProcessHandler(GeneralCommandLine(listOf("d2", "--version")).withCharset(Charsets.UTF_8))
        process.startNotify()
        process.waitFor(500)
        return process.exitCode == 0
    } catch (ignore: ProcessNotCreatedException) {
    } catch (e: Throwable) {
        logger<D2FormatterService>().error(e)
    }
    return false
}