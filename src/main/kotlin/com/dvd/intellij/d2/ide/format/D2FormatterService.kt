package com.dvd.intellij.d2.ide.format

import com.dvd.intellij.d2.ide.file.D2File
import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.NOTIFICATION_GROUP
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFile

class D2FormatterService : AsyncDocumentFormattingService() {

  companion object {
    private val ERROR_REGEX = "err: failed to fmt: .*:\\d*:?\\d*: (.*)".toRegex()
  }

  override fun getName(): String = D2Bundle["d2.formatter"]
  override fun getNotificationGroupId(): String = NOTIFICATION_GROUP
  override fun getFeatures(): MutableSet<FormattingService.Feature> = mutableSetOf()
  override fun canFormat(file: PsiFile): Boolean = file is D2File

  override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
    val service = service<D2Service>()

    if (!service.isCompilerInstalled) return null
    val file = request.ioFile ?: return null

    return object : FormattingTask {
      override fun run() {
        when (val res = service.format(file)) {
          is D2FormatterResult.Error -> {
            val error = ERROR_REGEX.find(res.error)?.groupValues?.get(1) ?: res.error
            request.onError(
              D2Bundle["d2.formatter"],
              D2Bundle["d2.formatter.error", error]
            )
          }

          is D2FormatterResult.Success -> request.onTextReady(res.content)
        }
      }

      override fun isRunUnderProgress(): Boolean = true
      override fun cancel(): Boolean = true
    }
  }

}