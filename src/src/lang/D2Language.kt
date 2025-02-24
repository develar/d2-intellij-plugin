package org.jetbrains.plugins.d2.lang

import com.intellij.lang.Language

object D2Language : Language("d2") {
    private fun readResolve(): Any = D2Language
}