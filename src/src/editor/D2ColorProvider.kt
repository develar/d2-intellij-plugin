package org.jetbrains.plugins.d2.editor

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.descendantsOfType
import com.intellij.ui.ColorUtil
import org.jetbrains.plugins.d2.lang.psi.ColorValueProvider
import org.jetbrains.plugins.d2.lang.psi.PropertyValue
import java.awt.Color

private class D2ColorProvider : ElementColorProvider {
    override fun getColorFrom(element: PsiElement): Color? {
        // Not a leaf child with a value must be checked, but a holder.
        // On setColorTo we replace the leaf child, so the holder element will become invalid, and after the first change the PSI element will be invalid (as we replaced it).
        return (element as? ColorValueProvider)?.getColor()
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val newPsi = PsiFileFactory.getInstance(project).createFileFromText(
            "shapeId.style.stroke: \"#${ColorUtil.toHex(color)}\"",
            element.containingFile
        ) ?: return

        element.firstChild.replace(newPsi.descendantsOfType<PropertyValue>().first().firstChild)
    }
}