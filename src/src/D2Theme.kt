package org.jetbrains.plugins.d2

import com.intellij.openapi.util.NlsSafe
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

val themeList = listOf(
    D2Theme.DEFAULT,
    D2Theme.NEUTRAL_GREY,
    D2Theme.FLAGSHIP_TERRASTRUCT,
    D2Theme.COOL_CLASSICS,
    D2Theme.MIXED_BERRY_BLUE,
    D2Theme.GRAPE_SODA,
    D2Theme.AUBERGINE,
    D2Theme.COLORBLIND_CLEAR,
    D2Theme.VANILLA_NITRO_COLA,
    D2Theme.ORANGE_CREAMSICLE,
    D2Theme.SHIRLEY_TEMPLE,
    D2Theme.EARTH_TONES,
    D2Theme.EVERGLADE_GREEN,
    D2Theme.BUTTERED_TOAST,
    D2Theme.DARK_MAUVE,
    D2Theme.TERMINAL,
    D2Theme.TERMINAL_GRAYSCALE
)

@Serializable
data class D2Theme(@JvmField val id: Int, @Suppress("UnstableApiUsage") @Transient @JvmField @NlsSafe val name: String = "") {
    companion object {
        val DEFAULT = D2Theme(0, "Default")
        val NEUTRAL_GREY = D2Theme(1, "Neutral grey")
        val FLAGSHIP_TERRASTRUCT = D2Theme(3, "Flagship Terrastruct")
        val COOL_CLASSICS = D2Theme(4, "Cool classics")
        val MIXED_BERRY_BLUE = D2Theme(5, "Mixed berry blue")
        val GRAPE_SODA = D2Theme(6, "Grape soda")
        val AUBERGINE = D2Theme(7, "Aubergine")
        val COLORBLIND_CLEAR = D2Theme(8, "Colorblind clear")
        val VANILLA_NITRO_COLA = D2Theme(100, "Vanilla nitro cola")
        val ORANGE_CREAMSICLE = D2Theme(101, "Orange creamsicle")
        val SHIRLEY_TEMPLE = D2Theme(102, "Shirley temple")
        val EARTH_TONES = D2Theme(103, "Earth tones")
        val EVERGLADE_GREEN = D2Theme(104, "Everglade green")
        val BUTTERED_TOAST = D2Theme(105, "Buttered toast")
        val DARK_MAUVE = D2Theme(200, "Dark mauve")
        val TERMINAL = D2Theme(300, "Terminal")
        val TERMINAL_GRAYSCALE = D2Theme(301, "Terminal grayscale")
    }
}