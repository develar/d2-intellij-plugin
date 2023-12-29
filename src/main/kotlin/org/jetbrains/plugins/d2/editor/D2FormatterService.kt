package org.jetbrains.plugins.d2.editor

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2_NOTIFICATION_GROUP
import org.jetbrains.plugins.d2.file.D2File

private val ERROR_REGEX = "err: failed to fmt: .*:\\d*:?\\d*: (.*)".toRegex()

sealed class D2FormatterResult {
  data class Success(val content: String) : D2FormatterResult()
  data class Error(val error: String) : D2FormatterResult()
}

private class D2FormatterService : AsyncDocumentFormattingService() {
  override fun getName(): String = D2Bundle.message("d2.formatter")

  override fun getNotificationGroupId(): String = D2_NOTIFICATION_GROUP

  override fun getFeatures() = emptySet<FormattingService.Feature>()

  override fun canFormat(file: PsiFile): Boolean = file is D2File

  override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
    val file = request.ioFile ?: return null
    return object : FormattingTask {
      override fun run() {
        val service = service<D2Service>()
        if (!service.isCompilerInstalled()) {
          request.onError("", D2Bundle.message("d2.executable.not.found.format.notification"))
          return
        }

        when (val result = service.format(file)) {
          is D2FormatterResult.Error -> {
            val error = ERROR_REGEX.find(result.error)?.groupValues?.get(1) ?: result.error
            request.onError(
              D2Bundle.message("d2.formatter"),
              D2Bundle.message("d2.formatter.error", error)
            )
          }

          is D2FormatterResult.Success -> request.onTextReady(result.content)
        }
      }

      override fun isRunUnderProgress(): Boolean = true

      override fun cancel(): Boolean = true
    }
  }
}