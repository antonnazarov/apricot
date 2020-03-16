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
public class C implements Language {

    String KEYWORDS[] = new String[]{"asm", "break", "case", "continue", "default", "do", "else", "enum", "for", "fortran", "goto", "if", "return", "struct", "switch", "typedef", "union", "while"};
    String OPERATORS[] = new String[]{"alignof","_Alignof", "_Generic", "offsetof", "static_assert","_Static_assert", "sizeof", "typeof"};
    String TYPES[] = new String[]{"_Bool", "_Complex", "_Imaginary", "bool", "char", "char16_t","char32_t", "double", "float", "int", 
        "int_least8_t",
        "int_least16_t",
        "int_least32_t",
        "int_least64_t",
        "int_fast8_t",
        "int_fast16_t",
        "int_fast32_t",
        "int_fast64_t",
        "uint_least8_t",
        "uint_least16_t",
        "uint_least32_t",
        "uint_least64_t",
        "uint_fast8_t",
        "uint_fast16_t",
        "uint_fast32_t",
        "uint_fast64_t",
         "intmax_t","uintmax_t", "uintptr_t","intptr_t", "long", "ptrdiff_t", "off_t","off64_t", "short", "signed", "size_t", "ssize_t", "unsigned", "void", "wchar_t", "wint_t"};
    String STORAGE_CLASS[] = new String[]{"_Alignas","alignas", "_Atomic", "noreturn","_Noreturn", "thread_local","_Thread_local", "auto", "const", "extern", "inline", "register", "restrict", "static", "volatile"};
    String BOOLEAN[] = new String[]{"true", "false"};

    String COMMON_DEFINES[] = new String[]{"null", "MAX", "MIN", "true", "false", "__LINE__", "__DATA__", "__FILE__", "__func__", "__TIME__", "__STDC__"};
    String STANDARD_STREAMS[] = new String[]{"stdin", "stdout", "stderr"};
    String SIGNALS[] = new String[]{"SIGABRT", "SIGALRM", "SIGCHLD", "SIGCONT", "SIGFPE", "SIGHUP", "SIGILL", "SIGINT", "SIGKILL", "SIGPIPE", "SIGQUIT", "SIGSEGV", "SIGSTOP", "SIGTERM", "SIGTRAP", "SIGTSTP", "SIGTTIN", "SIGTTOU", "SIGUSR1", "SIGUSR2"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String KEYWORDS_PATTERN;
        String OPERATORS_PATTERN;
        String TYPES_PATTERN;
        String STORAGE_CLASS_PATTERN;
        String BOOLEAN_PATTERN;
        String COMMON_DEFINES_PATTERN;
        String STANDARD_STREAMS_PATTERN;
        String SIGNALS_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;
        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        OPERATORS_PATTERN = "\\b(" + String.join("|", OPERATORS) + ")\\b";
        TYPES_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
        STORAGE_CLASS_PATTERN = "\\b(" + String.join("|", STORAGE_CLASS) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        COMMON_DEFINES_PATTERN = "\\b(" + String.join("|", COMMON_DEFINES) + ")\\b";
        STANDARD_STREAMS_PATTERN = "\\b(" + String.join("|", STANDARD_STREAMS) + ")\\b";
        SIGNALS_PATTERN = "\\b(" + String.join("|", SIGNALS) + ")\\b";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        pattern = Pattern.compile(
                "(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<OPERATORS>" + OPERATORS_PATTERN + ")"
                + "|(?<TYPES>" + TYPES_PATTERN + ")"
                + "|(?<STORAGECLASS>" + STORAGE_CLASS_PATTERN + ")"
                + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
                + "|(?<COMMONDEFINES>" + COMMON_DEFINES_PATTERN + ")"
                + "|(?<STANDARDSTREAMS>" + STANDARD_STREAMS_PATTERN + ")"
                + "|(?<SIGNALS>" + SIGNALS_PATTERN + ")"
                + "|(?<STRING>" + STRING_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return  matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("OPERATORS") != null ? "operators"
                : matcher.group("TYPES") != null ? "types"
                : matcher.group("STORAGECLASS") != null ? "storage-class"
                : matcher.group("BOOLEAN") != null ? "boolean"
                : matcher.group("COMMONDEFINES") != null ? "common-defines"
                : matcher.group("STANDARDSTREAMS") != null ? "standard-streams"
                : matcher.group("SIGNALS") != null ? "signals"
                : matcher.group("STRING") != null ? "string"
                : matcher.group("COMMENT") != null ? "comment"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(OPERATORS));
        keywordList.addAll(Arrays.asList(TYPES));
        keywordList.addAll(Arrays.asList(STORAGE_CLASS));
        keywordList.addAll(Arrays.asList(BOOLEAN));
        keywordList.addAll(Arrays.asList(COMMON_DEFINES));
        keywordList.addAll(Arrays.asList(STANDARD_STREAMS));
        keywordList.addAll(Arrays.asList(SIGNALS));
        Collections.sort(keywordList);
        return keywordList;
    }

}
