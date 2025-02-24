package org.jetbrains.plugins.d2.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.FileViewProvider
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2Icons
import org.jetbrains.plugins.d2.lang.D2Language

class D2File(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, D2Language) {
    override fun getFileType() = D2FileType

    override fun toString(): String = "D2 File"
}

object D2FileType : LanguageFileType(D2Language) {
    override fun getName() = "D2"

    override fun getDescription() = D2Bundle.message("label.d2.language.file")

    override fun getDefaultExtension() = "d2"

    override fun getIcon() = D2Icons.FILE
}