package org.jetbrains.plugins.d2

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object D2Icons {
    val FILE by lazy { IconLoader.getIcon("icons/d2.svg", D2Icons::class.java.classLoader) }

    val PROPERTY: Icon = AllIcons.Nodes.Property

    val CONNECTION: Icon = AllIcons.Diff.ArrowLeftRight
}