/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.syntaxtextareafx.langs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.co.s13.syntaxtextareafx.meta.Language;
import java.util.Collections;

/**
 *
 * @author nika
 */
public class Boo implements Language {

    String NAMESPACE[] = new String[]{"as", "from", "import", "namespace"};
    String PRIMITIVES[] = new String[]{"bool", "byte", "char", "date", "decimal", "double", "duck", "float", "int", "long", "object", "operator", "regex", "sbyte", "short", "single", "string", "timespan", "uint", "ulong", "ushort"};
    String DEFINITIONS[] = new String[]{"abstract", "callable", "class", "constructor", "def", "destructor", "do", "enum", "event", "final", "get", "interface", "internal", "of", "override", "partial", "private", "protected", "public", "return", "set", "static", "struct", "transient", "virtual", "yield"};
    String KEYWORDS[] = new String[]{"and", "break", "cast", "continue", "elif", "else", "ensure", "except", "for", "given", "goto", "if", "in", "isa", "is", "not", "or", "otherwise", "pass", "raise", "ref", "try", "unless", "when", "while"};
    String SPECIAL_VARIABLES[] = new String[]{"self", "super"};
    String NULL_VALUE[] = new String[]{"null"};
    String BOOLEAN[] = new String[]{"false", "true"};
    String BUILTINS[] = new String[]{"array", "assert", "checked", "enumerate", "__eval__", "filter", "getter", "len", "lock", "map", "matrix", "max", "min", "normalArrayIndexing", "print", "property", "range", "rawArrayIndexing", "required", "__switch__", "typeof", "unchecked", "using", "yieldAll", "zip"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String NAMESPACE_PATTERN;
        String PRIMITIVES_PATTERN;
        String DEFINITIONS_PATTERN;
        String KEYWORDS_PATTERN;
        String SPECIAL_VARIABLES_PATTERN;
        String NULL_VALUE_PATTERN;
        String BOOLEAN_PATTERN;
        String BUILTINS_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;

        NAMESPACE_PATTERN = "\\b(" + String.join("|", NAMESPACE) + ")\\b";
        PRIMITIVES_PATTERN = "\\b(" + String.join("|", PRIMITIVES) + ")\\b";
        DEFINITIONS_PATTERN = "\\b(" + String.join("|", DEFINITIONS) + ")\\b";
        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        SPECIAL_VARIABLES_PATTERN = "\\b(" + String.join("|", SPECIAL_VARIABLES) + ")\\b";
        NULL_VALUE_PATTERN = "\\b(" + String.join("|", NULL_VALUE) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        BUILTINS_PATTERN = "\\b(" + String.join("|", BUILTINS) + ")\\b";
        STRING_PATTERN = "\"(.|\\R)*?\"" + "|" + "\"'([^\"\\\\]|\\\\.)*\'";
        COMMENT_PATTERN = "#[^\n]*" + "|" + "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        pattern = Pattern.compile(
                "(?<NAMESPACE>" + NAMESPACE_PATTERN + ")"
                + "|(?<PRIMITIVES>" + PRIMITIVES_PATTERN + ")"
                + "|(?<DEFINITIONS>" + DEFINITIONS_PATTERN + ")"
                + "|(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<SPECIALVARIABLES>" + SPECIAL_VARIABLES_PATTERN + ")"
                + "|(?<NULLVALUE>" + NULL_VALUE_PATTERN + ")"
                + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
                + "|(?<BUILTINS>" + BUILTINS_PATTERN + ")"
                + "|(?<STRING>" + STRING_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("NAMESPACE") != null ? "namespace"
                : matcher.group("PRIMITIVES") != null ? "primitives"
                : matcher.group("DEFINITIONS") != null ? "definitions"
                : matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("SPECIALVARIABLES") != null ? "special-variables"
                : matcher.group("NULLVALUE") != null ? "null-value"
                : matcher.group("BOOLEAN") != null ? "boolean"
                : matcher.group("BUILTINS") != null ? "builtins"
                : matcher.group("STRING") != null ? "string"
                : matcher.group("COMMENT") != null ? "comment"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(NAMESPACE));
        keywordList.addAll(Arrays.asList(PRIMITIVES));
        keywordList.addAll(Arrays.asList(DEFINITIONS));
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(SPECIAL_VARIABLES));
        keywordList.addAll(Arrays.asList(NULL_VALUE));
        keywordList.addAll(Arrays.asList(BOOLEAN));
        keywordList.addAll(Arrays.asList(BUILTINS));
        Collections.sort(keywordList);
        return keywordList;
    }

}
