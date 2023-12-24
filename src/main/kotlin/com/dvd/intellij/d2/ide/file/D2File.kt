package com.dvd.intellij.d2.ide.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.jetbrains.plugins.d2.lang.D2Language

class D2File(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, D2Language) {
  override fun getFileType() = D2FileType

  override fun toString(): String = "D2 File"
}