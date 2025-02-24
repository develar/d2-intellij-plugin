package org.jetbrains.plugins.d2.editor

import com.intellij.lang.Commenter

private class D2Commenter : Commenter {
    override fun getLineCommentPrefix(): String = "# "

    override fun getBlockCommentPrefix(): String = "# "

    override fun getBlockCommentSuffix(): String? = null

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null
}