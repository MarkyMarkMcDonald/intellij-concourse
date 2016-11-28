package lv.kid.vermut.intellij.yaml.psi;

import com.intellij.psi.PsiElement;

/**
 * Created by VermutMac on 10/31/2015.
 */
public interface YamlTuple extends PsiElement {

    YamlElement getKey();

    PsiElement getValue();
}
