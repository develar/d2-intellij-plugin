package org.jetbrains.plugins.d2.lang.psi

import java.awt.Color

sealed interface ColorValueProvider {
    fun getColor(): Color?
}