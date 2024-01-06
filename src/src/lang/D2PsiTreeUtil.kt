package org.jetbrains.plugins.d2.lang

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object D2PsiTreeUtil {

  @JvmStatic
  fun <T : PsiElement?> getChildrenOfTypeAsList(element: PsiElement?, aClass: Class<out T>): List<T> =
    PsiTreeUtil.getChildrenOfTypeAsList(element, aClass)

}