package com.dvd.intellij.d2.ide.reference

import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.impl.source.resolve.reference.CommentsReferenceContributor
import org.jetbrains.plugins.d2.lang.psi.D2AttributeValue

// todo not working in strings
class D2UrlReferenceContributor : PsiReferenceContributor() {
  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    val provider = CommentsReferenceContributor.COMMENTS_REFERENCE_PROVIDER_TYPE.provider
    registrar.registerReferenceProvider(
      object : PsiElementPattern.Capture<D2AttributeValue>(D2AttributeValue::class.java) {},
      provider,
      PsiReferenceRegistrar.LOWER_PRIORITY,
    )
  }
}