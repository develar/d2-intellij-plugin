package com.dvd.intellij.d2.ide.file

import com.dvd.intellij.d2.ide.lang.D2Language
import com.dvd.intellij.d2.ide.utils.D2Icons
import com.intellij.openapi.fileTypes.LanguageFileType

object D2FileType : LanguageFileType(D2Language) {
  override fun getName() = "D2"

  override fun getDescription() = "D2 language file"

  override fun getDefaultExtension() = "d2"

  override fun getIcon() = D2Icons.FILE
}