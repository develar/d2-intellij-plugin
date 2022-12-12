package com.dvd.intellij.d2.components

data class D2Layout(
  val name: String,
  val bundled: Boolean? = false,
  val description: String,
) {
  companion object {
    val DEFAULT = D2Layout("dagre", true, "The directed graph layout library Dagre")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as D2Layout

    if (name != other.name) return false

    return true
  }

  override fun hashCode(): Int = name.hashCode()
}