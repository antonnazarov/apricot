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
public class Bennugd implements Language {

    String BOOLEAN[] = new String[]{"false", "true"};
    String KEYWORDS[] = new String[]{"begin", "break", "call", "case", "clone", "const", "continue", "debug", "declare", "default", "dup", "elif", "else", "elseif", "elsif", "end", "error", "exit", "for", "frame", "from", "function", "global", "goto", "if", "import", "include", "jmp", "local", "loop", "mod", "mouse", "next", "offset", "on", "onerror", "onexit", "private", "process", "program", "public", "repeat", "resume", "return", "sizeof", "step", "switch", "to", "until", "while"};
    String GLOBALS[] = new String[]{"argc", "argv", "os_id"};
    String LOCALS[] = new String[]{"bigbro", "father", "frame_percent", "id", "process_type", "reserved", "reserved.frame_percent", "reserved.process_type", "reserved.saved_priority", "reserved.saved_status", "reserved.status", "saved_priority", "saved_status", "smallbro", "status", "son"};
    String OPERATORS[] = new String[]{"==", "!=", "!", "<", ">", "<=", ">=", "&&", "&", "=", "||", "|", "^^", "^"};
    String TYPES[] = new String[]{"byte", "char", "dword", "float", "int", "pointer", "short", "signed", "string", "struct", "type", "unsigned", "word"};
    String COMMONMACROS[] = new String[]{"COMPILER_VERSION", "__DATE__", "__FILE__", "__LINE__", "max_byte", "max_dword", "max_int", "max_sbyte", "max_short", "max_word", "min_byte", "min_dword", "min_int", "min_sbyte", "min_short", "min_word", null, "os_beos", "os_bsd", "os_dc", "os_gp32", "os_linux", "os_macos", "os_win32", "status_dead", "status_frozen", "status_killed", "status_running", "status_sleeping", "status_waiting", "__TIME__", "__VERSION__"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String BOOLEAN_PATTERN;
        String KEYWORDS_PATTERN;
        String GLOBALS_PATTERN;
        String LOCALS_PATTERN;
        String OPERATORS_PATTERN;
        String TYPES_PATTERN;
        String COMMONMACROS_PATTERN;
        String COMMENT_PATTERN;

        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        GLOBALS_PATTERN = "\\b(" + String.join("|", GLOBALS) + ")\\b";
        LOCALS_PATTERN = "\\b(" + String.join("|", LOCALS) + ")\\b";
        OPERATORS_PATTERN = "\\b(" + String.join("|", OPERATORS) + ")\\b";
        TYPES_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
        COMMONMACROS_PATTERN = "\\b(" + String.join("|", COMMONMACROS) + ")\\b";
        COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        pattern = Pattern.compile(
                "(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
                + "|(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<GLOBALS>" + GLOBALS_PATTERN + ")"
                + "|(?<LOCALS>" + LOCALS_PATTERN + ")"
                + "|(?<OPERATORS>" + OPERATORS_PATTERN + ")"
                + "|(?<TYPES>" + TYPES_PATTERN + ")"
                + "|(?<COMMONMACROS>" + COMMONMACROS_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")");
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("BOOLEAN") != null ? "boolean"
                : matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("GLOBALS") != null ? "globals"
                : matcher.group("LOCALS") != null ? "locals"
                : matcher.group("OPERATORS") != null ? "operators"
                : matcher.group("TYPES") != null ? "types"
                : matcher.group("COMMONMACROS") != null ? "commonmacros"
                : matcher.group("COMMENT") != null ? "comment"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(BOOLEAN));
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(GLOBALS));
        keywordList.addAll(Arrays.asList(LOCALS));
        keywordList.addAll(Arrays.asList(OPERATORS));
        keywordList.addAll(Arrays.asList(TYPES));
        keywordList.addAll(Arrays.asList(COMMONMACROS));
        Collections.sort(keywordList);
        return keywordList;
    }

}
