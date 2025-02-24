package org.jetbrains.plugins.d2

import com.intellij.openapi.util.NlsSafe
import kotlinx.serialization.Serializable

@Serializable
data class D2Layout(
    @Suppress("UnstableApiUsage") @NlsSafe val name: String,
    val bundled: Boolean? = false,
    val description: String,
) {
    companion object {
        val DEFAULT = D2Layout(name = "dagre", bundled = true, description = "The directed graph layout library Dagre")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as D2Layout
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}