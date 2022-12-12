package com.dvd.intellij.d2.ide.utils

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

object D2Icons {
  val FILE = IconLoader.getIcon("/icons/d2.png", D2Icons::class.java)

  val SHAPE = AllIcons.Nodes.Gvariable
  val ATTRIBUTE = AllIcons.Nodes.Property
  val CONNECTION = AllIcons.Diff.ArrowLeftRight
}