package com.github.markymarkmcdonald.navigation;

import com.intellij.lang.HelpID;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceFindUsagesProvider implements FindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
//        return new DefaultWordsScanner(new YamlLexer(),
//                TokenSet.create(NeonTokenTypes.NEON_LITERAL),
//                TokenSet.create(NeonTokenTypes.NEON_COMMENT),
//                TokenSet.create(NeonTokenTypes.NEON_STRING));
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return true;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_OTHER_USAGES;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return "";
    }
}
