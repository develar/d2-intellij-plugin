package org.jetbrains.plugins.d2

import kotlinx.serialization.Serializable

@Serializable
data class D2Sketch(@JvmField val enabled: Boolean) {
  val name: String = "Sketch"
}
