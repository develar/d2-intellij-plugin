package com.dvd.intellij.d2.components

import com.intellij.openapi.util.NlsSafe
import java.io.Serializable

enum class D2Theme(val id: Int, @NlsSafe val tName: String) : Serializable {
  DEFAULT(0, "Default"),
  NEUTRAL_GREY(1, "Neutral grey"),
  FLAGSHIP_TERRASTRUCT(3, "Flagship Terrastruct"),
  COOL_CLASSICS(4, "Cool classics"),
  MIXED_BERRY_BLUE(5, "Mixed berry blue"),
  GRAPE_SODA(6, "Grape soda"),
  AUBERGINE(7, "Aubergine"),
  COLORBLIND_CLEAR(8, "Colorblind clear"),

  VANILLA_NITRO_COLA(100, "Vanilla nitro cola"),
  ORANGE_CREAMSICLE(101, "Orange creamsicle"),
  SHIRLEY_TEMPLE(102, "Shirley temple"),
  EARTH_TONES(103, "Earth tones"),
  EVERGLADE_GREEN(104, "Everglade green"),
  BUTTERED_TOAST(105, "Buttered toast");
}