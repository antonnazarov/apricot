/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.syntaxtextareafx.langs;

import in.co.s13.syntaxtextareafx.meta.Language;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nika
 */
public class Ada implements Language {

    String PREPROCESSOR[] = new String[]{"package",
        "pragma", "use", "with"};
    String FUNCTION[] = new String[]{"function",
        "procedure", "return"};
    String KEYWORDS[] = new String[]{
        "abort", "abs", "accept", "all",
        "and", "begin", "body", "case",
        "declare", "delay", "do", "else",
        "elsif", "end", "entry", "exception",
        "exit", "for", "generic", "goto",
        "if", "in", "is", "loop",
        "mod", "new", "not", "null",
        "or", "others", "out", "protected",
        "raise", "record", "rem", "renames",
        "requeue", "reverse", "select", "separate",
        "subtype", "task", "terminate", "then",
        "type", "until", "when", "while",
        "xor"};

    String STORAGECLASS[] = new String[]{
        "abstract", "access", "aliased", "array",
        "at", "constant", "delta", "digits",
        "interface", "limited", "of", "private",
        "range", "tagged", "synchronized"};
    String TYPE[] = new String[]{
        "boolean", "character",
        "count", "duration",
        "float", "integer",
        "long_float", "long_integer",
        "priority", "short_float",
        "short_integer", "string"};

    String BOOLEAN[] = new String[]{"true", "false"};

    @Override
    public Pattern generatePattern() {
        String STORAGECLASS_PATTERN;
        String BOOLEAN_PATTERN;
        String PREPROCESSOR_PATTERN;
        String FUNCTION_PATTERN;
        String KEYWORDS_PATTERN;
        String TYPE_PATTERN;
        String PAREN_PATTERN;
        String BRACE_PATTERN;
        String BRACKET_PATTERN;
        String SEMICOLON_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;

        PREPROCESSOR_PATTERN = "\\b(" + String.join("|", PREPROCESSOR) + ")\\b";
        FUNCTION_PATTERN = "\\b(" + String.join("|", FUNCTION) + ")\\b";
        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        STORAGECLASS_PATTERN = "\\b(" + String.join("|", STORAGECLASS) + ")\\b";
        TYPE_PATTERN = "\\b(" + String.join("|", TYPE) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        PAREN_PATTERN = "\\(|\\)";
        BRACE_PATTERN = "\\{|\\}";
        BRACKET_PATTERN = "\\[|\\]";
        SEMICOLON_PATTERN = "\\;";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "--[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        Pattern pattern = Pattern.compile(
                "(?<PREPROCESSOR>" + PREPROCESSOR_PATTERN + ")"
                + "|(?<FUNCTION>" + FUNCTION_PATTERN + ")"
                + "|(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<STORAGECLASS>" + STORAGECLASS_PATTERN + ")"
                + "|(?<TYPE>" + TYPE_PATTERN + ")"
                + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
                + "|(?<PAREN>" + PAREN_PATTERN + ")"
                + "|(?<BRACE>" + BRACE_PATTERN + ")"
                + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                + "|(?<STRING>" + STRING_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("PREPROCESSOR") != null ? "preprocessor"
                : matcher.group("FUNCTION") != null ? "function"
                : matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("STORAGECLASS") != null ? "storageclass"
                : matcher.group("TYPE") != null ? "type"
                : matcher.group("BOOLEAN") != null ? "boolean"
                : matcher.group("PAREN") != null ? "paren"
                : matcher.group("BRACE") != null ? "brace"
                : matcher.group("BRACKET") != null ? "bracket"
                : matcher.group("SEMICOLON") != null ? "semicolon"
                : matcher.group("STRING") != null ? "string"
                : matcher.group("COMMENT") != null ? "comment"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(PREPROCESSOR));
        keywordList.addAll(Arrays.asList(FUNCTION));
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(STORAGECLASS));
        keywordList.addAll(Arrays.asList(TYPE));
        keywordList.addAll(Arrays.asList(BOOLEAN));
       Collections.sort(keywordList);
         return keywordList;
    }

}
