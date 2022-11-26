package com.dvd.intellij.d2.ide.file

import com.dvd.intellij.d2.ide.lang.D2Language
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class D2File(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, D2Language) {
  override fun getFileType() = D2FileType.INSTANCE

  override fun toString(): String = "D2 File"
}