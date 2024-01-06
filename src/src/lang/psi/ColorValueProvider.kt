package org.jetbrains.plugins.d2.lang.psi

import java.awt.Color

interface ColorValueProvider {
  fun getColor(): Color?
}