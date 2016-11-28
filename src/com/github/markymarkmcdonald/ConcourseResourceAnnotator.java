package com.github.markymarkmcdonald;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kid.vermut.intellij.yaml.psi.YamlFile;
import lv.kid.vermut.intellij.yaml.psi.YamlMapping;
import lv.kid.vermut.intellij.yaml.psi.YamlSequence;
import lv.kid.vermut.intellij.yaml.psi.YamlTuple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcourseResourceAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof YamlTuple) {
            String value = element.getText();
            if (value != null && value.contains("get:") && !Pattern.compile(".*get:.*get:.*", Pattern.DOTALL).matcher(value).matches()) {

                Pattern resourceNamePattern;
                if (value.contains("resource:")) {
                    resourceNamePattern = Pattern.compile(".*resource:\\s*(.*)\\s*.*");
                } else {
                    resourceNamePattern = Pattern.compile(".*get:\\s*(.*)\\s*.*");
                }
                Matcher matcher = resourceNamePattern.matcher(value);
                if (matcher.find()) {
                    String resourceName = matcher.group(1).trim();

                    List<YamlTuple> properties = findConcourseResources(element.getContainingFile(), resourceName);

                    if (properties.size() == 1) {
                        TextRange range = new TextRange(element.getTextRange().getStartOffset() + 4, element.getTextRange().getStartOffset() + 4);
                        Annotation annotation = holder.createInfoAnnotation(range, null);
                        annotation.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);
                    } else if (properties.size() == 0) {
                        TextRange range = new TextRange(element.getTextRange().getStartOffset() + 5, element.getTextRange().getEndOffset());
                        holder.createErrorAnnotation(range, "Unresolved concourse resource");
                    }
                }
            }
        }
    }

    @NotNull
    private List<YamlTuple> findConcourseResources(PsiFile fileWithResources, String resourceName) {
        List<YamlTuple> result = null;

        if (fileWithResources instanceof YamlFile) {
            YamlFile yamlFile = (YamlFile) fileWithResources;
            YamlMapping[] yamlMappings = PsiTreeUtil.getChildrenOfType(yamlFile, YamlMapping.class);
            if (yamlMappings != null) {
                YamlMapping yamlMapping = yamlMappings[0];
                for (PsiElement topLevelChild : yamlMapping.getChildren()) {
                    if (topLevelChild instanceof YamlTuple && ((YamlTuple) topLevelChild).getKey().getText().equals("resources")) {
                        PsiElement[] resourcesNode = topLevelChild.getChildren();
                        PsiElement[] resources = PsiTreeUtil.getChildrenOfType(resourcesNode[1], YamlSequence.class)[0].getChildren();

                        for (PsiElement resource : resources) {
                            for (YamlTuple resourceAttribute : PsiTreeUtil.getChildrenOfType(resource, YamlTuple.class)) {
                                if (resourceAttribute.getKey().getText().contains("name") && resourceAttribute.getValue().getText().replaceAll(":", " ").trim().equals(resourceName)) {
                                    if (result == null) {
                                        result = new ArrayList<>();
                                    }
                                    result.add(resourceAttribute);

                                }
                            }
                        }
                    }
                }
            }

        }

        return result != null ? result : Collections.emptyList();
    }
}
