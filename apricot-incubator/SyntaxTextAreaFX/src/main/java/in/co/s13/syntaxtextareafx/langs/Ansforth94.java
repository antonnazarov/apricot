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
public class Ansforth94 implements Language {

    String CORE_KEYWORDS[] = new String[]{"!", "#",
        "#>", "#S", "'", "\\(",
        "\\*", "\\*/", "\\*/MOD", "\\+",
        "\\+!", "\\+LOOP", ",", "-",
        "\\.", "\\.\"", "/", "/MOD",
        "0<", "0=", "1\\+", "1-",
        "2!", "2\\*", "2/", "2@",
        "2DROP", "2DUP", "2OVER", "2SWAP",
        ":", ";", "<", "<#",
        "=", ">", ">BODY", ">IN",
        ">NUMBER", ">R", "\\?DUP", "@",
        "ABORT", "ABORT\"", "ABS", "ACCEPT",
        "ALIGN", "ALIGNED", "ALLOT", "AND",
        "BASE", "BEGIN", "BL", "C!",
        "C,", "C@", "CELL\\+", "CELLS",
        "CHAR", "CHAR\\+", "CHARS", "CONSTANT",
        "COUNT", "CR", "CREATE", "DECIMAL",
        "DEPTH", "DO", "DOES>", "DROP",
        "DUP", "ELSE", "EMIT", "ENVIRONMENT\\?",
        "EVALUATE", "EXECUTE", "EXIT", "FILL",
        "FIND", "FM/MOD", "HERE", "HOLD",
        "I", "IF", "IMMEDIATE", "INVERT",
        "J", "KEY", "LEAVE", "LITERAL",
        "LOOP", "LSHIFT", "M\\*", "MAX",
        "MIN", "MOD", "MOVE", "NEGATE",
        "OR", "OVER", "POSTPONE", "QUIT",
        "R>", "R@", "RECURSE", "REPEAT",
        "ROT", "RSHIFT", "S\"", "S>D",
        "SIGN", "SM/REM", "SOURCE", "SPACE",
        "SPACES", "STATE", "SWAP", "THEN",
        "TYPE", "U\\.", "U<", "UM\\*",
        "UM/MOD", "UNLOOP", "UNTIL", "VARIABLE",
        "WHILE", "WORD", "XOR", "\\[",
        "\\['\\]", "\\[CHAR\\]", "\\]"};

    String[] CORE_EXT_KEYWORDS = new String[]{"\\.\\(", "\\.R",
        "0<>", "0>", "2>R", "2R>",
        "2R@", ":NONAME", "<>", "\\?DO",
        "AGAIN", "C\"", "CASE", "COMPILE,",
        "ENDCASE", "ENDOF", "ERASE",
        "HEX", "MARKER", "NIP", "OF",
        "PAD", "PARSE", "PICK", "REFILL", "RESTORE-INPUT",
        "ROLL", "SAVE-INPUT", "SOURCE-ID", "TO",
        "TUCK", "U\\.R", "U>", "UNUSED",
        "VALUE", "WITHIN", "\\[COMPILE\\]", "\\\\"
    };
    String[] BLOCK_KEYWORDS = new String[]{"BLK",
        "BLOCK", "BUFFER", "EVALUATE", "FLUSH",
        "LOAD", "SAVE-BUFFERS", "UPDATE"};
    String[] OBSOLETE_CORE_EXT_KEYWORDS = new String[]{
        "#TIB", "CONVERT", "EXPECT", "QUERY",
        "SPAN", "TIB"};
    String[] BLOCK_EXT_KEYWORDS = new String[]{
        "EMPTY-BUFFERS", "LIST", "REFILL", "SCR",
        "THRU", "\\\\"};
    String[] DOUBLE_KEYWORDS = new String[]{
        "2CONSTANT", "2LITERAL", "2VARIABLE", "D\\+",
        "D-", "D\\.", "D\\.R", "D0<",
        "D0=", "D2\\*", "D2/", "D<",
        "D=", "D>S", "DABS", "DMAX",
        "DMIN", "DNEGATE", "M\\*/", "M\\+"};
    String[] DOUBLE_EXT_KEYWORDS = new String[]{
        "2ROT", "DU<"};
    String[] EXCEPTION_KEYWORDS = new String[]{
        "CATCH", "THROW"};
    String[] EXCEPTION_EXT_KEYWORDS = new String[]{
        "ABORT", "ABORT\""};
    String[] FACILITY_KEYWORDS = new String[]{
        "AT-XY", "KEY\\?", "PAGE"};
    String[] FACILITY_EXT_KEYWORDS = new String[]{
        "EKEY", "EKEY>CHAR", "EKEY\\?", "EMIT\\?",
        "MS", "TIME&DATE"};
    String[] FILE_KEYWORDS = new String[]{
        "\\(", "BIN", "CLOSE-FILE", "CREATE-FILE",
        "DELETE-FILE", "FILE-POSITION", "FILE-SIZE", "INCLUDE-FILE",
        "INCLUDED", "OPEN-FILE", "R/O", "R/W",
        "READ-FILE", "READ-LINE", "REPOSITION-FILE", "RESIZE-FILE",
        "S\"", "SOURCE-ID", "W/O", "WRITE-FILE",
        "WRITE-LINE"};
    String[] FILE_EXT_KEYWORDS = new String[]{
        "FILE-STATUS", "FLUSH-FILE", "REFILL", "RENAME-FILE"};
    String[] FLOATING_KEYWORDS = new String[]{
        ">FLOAT", "D>F", "F!", "F\\*", "F\\+", "F-", "F/", "F0<",
        "F0=", "F<", "F>D", "F@", "FALIGN", "FALIGNED", "FCONSTANT", "FDEPTH",
        "FDROP", "FDUP", "FLITERAL", "FLOAT\\+", "FLOATS", "FLOOR", "FMAX", "FMIN",
        "FNEGATE", "FOVER", "FROT", "FROUND", "FSWAP", "FVARIABLE", "REPRESENT"};
    String[] FLOATING_EXT_KEYWORDS = new String[]{
        "DF!", "DF@", "DFALIGN", "DFALIGNED", "DFLOAT\\+", "DFLOATS", "F\\*\\*", "F\\.",
        "FABS", "FACOS", "FACOSH", "FALOG", "FASIN", "FASINH", "FATAN", "FATAN2",
        "FATANH", "FCOS", "FCOSH", "FE\\.", "FEXP", "FEXPM1", "FLN", "FLNP1",
        "FLOG", "FS\\.", "FSIN", "FSINCOS", "FSINH", "FSQRT", "FTAN", "FTANH",
        "F~", "PRECISION", "SET-PRECISION", "SF!", "SF@", "SFALIGN", "SFALIGNED", "SFLOAT\\+",
        "SFLOATS"};
    String[] LOCAL_KEYWORDS = new String[]{"\\(LOCAL\\)", "TO"};
    String[] LOCAL_EXT_KEYWORDS = new String[]{"LOCALS\\|"};
    String[] MEMORY_KEYWORDS = new String[]{"ALLOCATE", "FREE", "RESIZE"};
    String[] TOOLS_KEYWORDS = new String[]{"\\.S", "\\?", "DUMP", "SEE", "WORDS"};
    String[] TOOLS_EXT_KEYWORDS = new String[]{";CODE", "AHEAD",
        "ASSEMBLER", "BYE", "CODE", "CS-PICK", "CS-ROLL", "EDITOR", "STATE", "\\[ELSE\\]", "\\[IF\\]", "\\[THEN\\]"};
    String[] OBSOLETE_TOOLS_EXT_KEYWORDS = new String[]{"FORGET"};
    String[] SEARCH_KEYWORDS = new String[]{"DEFINITIONS", "FIND", "FORTH-WORDLIST",
        "GET-CURRENT", "GET-ORDER", "SEARCH-WORDLIST", "SET-CURRENT",
        "SET-ORDER", "WORDLIST"};
    String[] SEARCH_EXT_KEYWORDS = new String[]{
        "ALSO", "FORTH", "ONLY", "ORDER", "PREVIOUS"};
    String[] STRING_KEYWORDS = new String[]{
        "-TRAILING", "/STRING", "BLANK", "CMOVE",
        "CMOVE>", "COMPARE", "SEARCH", "SLITERAL"};
    String NULL[] = new String[]{
        "null"
    };
    String BOOLEAN[] = new String[]{"true", "false"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String CORE_KEYWORDS_PATTERN;
        String CORE_EXT_KEYWORDS_PATTERN;
        String BLOCK_KEYWORDS_PATTERN;
        String OBSELETE_CORE_EXT_KEYWORDS_PATTERN;
        String BLOCK_EXT_KEYWORDS_PATTERN;
        String DOUBLE_KEYWORDS_PATTERN;
        String DOUBLE_EXT_KEYWORDS_PATTERN;
        String EXCEPTION_KEYWORDS_PATTERN;
        String EXCEPTION_EXT_KEYWORDS_PATTERN;
        String FACILITY_KEYWORDS_PATTERN;
        String FACILITY_EXT_KEYWORDS_PATTERN;
        String FILE_KEYWORDS_PATTERN;
        String FILE_EXT_KEYWORDS_PATTERN;
        String FLOATING_KEYWORDS_PATTERN;
        String FLOATING_EXT_KEYWORDS_PATTERN;
        String LOCAL_KEYWORDS_PATTERN;
        String LOCAL_EXT_KEYWORDS_PATTERN;
        String MEMORY_KEYWORDS_PATTERN;
        String TOOLS_KEYWORDS_PATTERN;
        String TOOLS_EXT_KEYWORDS_PATTERN;
        String OBSOLETE_TOOLS_EXT_KEYWORDS_PATTERN;
        String SEARCH_KEYWORDS_PATTERN;
        String SEARCH_EXT_KEYWORDS_PATTERN;
        String STRING_KEYWORDS_PATTERN;
        String NULL_PATTERN;
        String BOOLEAN_PATTERN;
        String PAREN_PATTERN;
        String BRACE_PATTERN;
        String BRACKET_PATTERN;
        String SEMICOLON_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;

        CORE_KEYWORDS_PATTERN = "\\b(" + String.join("|", CORE_KEYWORDS) + ")\\b";
        CORE_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", CORE_EXT_KEYWORDS) + ")\\b";
        BLOCK_KEYWORDS_PATTERN = "\\b(" + String.join("|", BLOCK_KEYWORDS) + ")\\b";
        OBSELETE_CORE_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", OBSOLETE_CORE_EXT_KEYWORDS) + ")\\b";
        BLOCK_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", BLOCK_EXT_KEYWORDS) + ")\\b";
        DOUBLE_KEYWORDS_PATTERN = "\\b(" + String.join("|", DOUBLE_KEYWORDS) + ")\\b";
        DOUBLE_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", DOUBLE_EXT_KEYWORDS) + ")\\b";
        EXCEPTION_KEYWORDS_PATTERN = "\\b(" + String.join("|", EXCEPTION_KEYWORDS) + ")\\b";
        EXCEPTION_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", EXCEPTION_EXT_KEYWORDS) + ")\\b";
        FACILITY_KEYWORDS_PATTERN = "\\b(" + String.join("|", FACILITY_KEYWORDS) + ")\\b";
        FACILITY_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", FACILITY_EXT_KEYWORDS) + ")\\b";
        FILE_KEYWORDS_PATTERN = "\\b(" + String.join("|", FILE_KEYWORDS) + ")\\b";
        FILE_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", FILE_EXT_KEYWORDS) + ")\\b";
        FLOATING_KEYWORDS_PATTERN = "\\b(" + String.join("|", FLOATING_KEYWORDS) + ")\\b";
        FLOATING_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", FLOATING_EXT_KEYWORDS) + ")\\b";
        LOCAL_KEYWORDS_PATTERN = "\\b(" + String.join("|", LOCAL_KEYWORDS) + ")\\b";
        LOCAL_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", LOCAL_EXT_KEYWORDS) + ")\\b";
        MEMORY_KEYWORDS_PATTERN = "\\b(" + String.join("|", MEMORY_KEYWORDS) + ")\\b";
        TOOLS_KEYWORDS_PATTERN = "\\b(" + String.join("|", TOOLS_KEYWORDS) + ")\\b";
        TOOLS_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", TOOLS_EXT_KEYWORDS) + ")\\b";
        OBSOLETE_TOOLS_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", OBSOLETE_TOOLS_EXT_KEYWORDS) + ")\\b";
        SEARCH_KEYWORDS_PATTERN = "\\b(" + String.join("|", SEARCH_KEYWORDS) + ")\\b";
        SEARCH_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", SEARCH_EXT_KEYWORDS) + ")\\b";
        STRING_KEYWORDS_PATTERN = "\\b(" + String.join("|", STRING_KEYWORDS) + ")\\b";
        NULL_PATTERN = "\\b(" + String.join("|", NULL) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        PAREN_PATTERN = "\\(|\\)";
        BRACE_PATTERN = "\\{|\\}";
        BRACKET_PATTERN = "\\[|\\]";
        SEMICOLON_PATTERN = "\\;";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "/[^\n]*" + "|" + "(\\(.|\\R)*?\\)";

        pattern = Pattern.compile(
                "(?<COREEXTKEYWORDS>" + CORE_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<COREKEYWORDS>" + CORE_KEYWORDS_PATTERN + ")"
                + "|(?<BLOCKKEYWORDS>" + BLOCK_KEYWORDS_PATTERN + ")"
                + "|(?<OBSELETECOREEXTKEYWORDS>" + OBSELETE_CORE_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<BLOCKEXTKEYWORDS>" + BLOCK_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<DOUBLEKEYWORDS>" + DOUBLE_KEYWORDS_PATTERN + ")"
                + "|(?<DOUBLEEXTKEYWORDS>" + DOUBLE_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<EXCEPTIONKEYWORDS>" + EXCEPTION_KEYWORDS_PATTERN + ")"
                + "|(?<EXCEPTIONEXTKEYWORDS>" + EXCEPTION_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<FACILITYKEYWORDS>" + FACILITY_KEYWORDS_PATTERN + ")"
                + "|(?<FACILITYEXTKEYWORDS>" + FACILITY_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<FILEKEYWORDS>" + FILE_KEYWORDS_PATTERN + ")"
                + "|(?<FILEEXTKEYWORDS>" + FILE_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<FLOATINGKEYWORDS>" + FLOATING_KEYWORDS_PATTERN + ")"
                + "|(?<FLOATINGEXTKEYWORDS>" + FLOATING_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<LOCALKEYWORDS>" + LOCAL_KEYWORDS_PATTERN + ")"
                + "|(?<LOCALEXTKEYWORDS>" + LOCAL_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<MEMORYKEYWORDS>" + MEMORY_KEYWORDS_PATTERN + ")"
                + "|(?<TOOLSKEYWORDS>" + TOOLS_KEYWORDS_PATTERN + ")"
                + "|(?<TOOLSEXTKEYWORDS>" + TOOLS_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<OBSOLETETOOLSEXTKEYWORDS>" + OBSOLETE_TOOLS_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<SEARCHKEYWORDS>" + SEARCH_KEYWORDS_PATTERN + ")"
                + "|(?<SEARCHEXTKEYWORDS>" + SEARCH_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<STRINGKEYWORDS>" + STRING_KEYWORDS_PATTERN + ")"
                + "|(?<NULL>" + NULL_PATTERN + ")"
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
        return matcher.group("COREEXTKEYWORDS") != null ? "core-ext-keywords"
                : matcher.group("COREKEYWORDS") != null ? "core-keywords"
                : matcher.group("BLOCKKEYWORDS") != null ? "block-keywords"
                : matcher.group("OBSELETECOREEXTKEYWORDS") != null ? "obsolete-core-ext-keywords"
                : matcher.group("BLOCKEXTKEYWORDS") != null ? "block-ext-keywords "
                : matcher.group("DOUBLEKEYWORDS") != null ? "double-keywords"
                : matcher.group("DOUBLEEXTKEYWORDS") != null ? "double-ext-keywords"
                : matcher.group("EXCEPTIONKEYWORDS") != null ? "exception-keywords"
                : matcher.group("EXCEPTIONEXTKEYWORDS") != null ? "exception-ext-keywords"
                : matcher.group("FACILITYKEYWORDS") != null ? "facility-keywords"
                : matcher.group("FACILITYEXTKEYWORDS") != null ? "facility-ext-keywords"
                : matcher.group("FILEKEYWORDS") != null ? "file-keywords"
                : matcher.group("FILEEXTKEYWORDS") != null ? "file-ext-keywords"
                : matcher.group("FLOATINGKEYWORDS") != null ? "floating-keywords"
                : matcher.group("FLOATINGEXTKEYWORDS") != null ? "floating-ext-keywords"
                : matcher.group("LOCALKEYWORDS") != null ? "local-keywords"
                : matcher.group("LOCALEXTKEYWORDS") != null ? "local-ext-keywords"
                : matcher.group("MEMORYKEYWORDS") != null ? "memory-keywords"
                : matcher.group("TOOLSKEYWORDS") != null ? "tools-keywords"
                : matcher.group("TOOLSEXTKEYWORDS") != null ? "tools-ext-keywords"
                : matcher.group("OBSOLETETOOLSEXTKEYWORDS") != null ? "obsolete-tools-ext-keywords"
                : matcher.group("SEARCHKEYWORDS") != null ? "search-keywords"
                : matcher.group("SEARCHEXTKEYWORDS") != null ? "search-ext-keywords"
                : matcher.group("STRINGKEYWORDS") != null ? "string-keywords"
                : matcher.group("NULL") != null ? "nullvalue"
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
        keywordList.addAll(Arrays.asList(CORE_KEYWORDS));
        keywordList.addAll(Arrays.asList(CORE_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(BLOCK_KEYWORDS));
        keywordList.addAll(Arrays.asList(OBSOLETE_CORE_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(BLOCK_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(DOUBLE_KEYWORDS));
        keywordList.addAll(Arrays.asList(DOUBLE_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(EXCEPTION_KEYWORDS));
        keywordList.addAll(Arrays.asList(EXCEPTION_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(FACILITY_KEYWORDS));
        keywordList.addAll(Arrays.asList(FACILITY_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(FILE_KEYWORDS));
        keywordList.addAll(Arrays.asList(FILE_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(FLOATING_KEYWORDS));
        keywordList.addAll(Arrays.asList(FLOATING_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(LOCAL_KEYWORDS));
        keywordList.addAll(Arrays.asList(LOCAL_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(MEMORY_KEYWORDS));
        keywordList.addAll(Arrays.asList(TOOLS_KEYWORDS));
        keywordList.addAll(Arrays.asList(TOOLS_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(OBSOLETE_TOOLS_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(SEARCH_KEYWORDS));
        keywordList.addAll(Arrays.asList(SEARCH_EXT_KEYWORDS));
        keywordList.addAll(Arrays.asList(STRING_KEYWORDS));
        keywordList.addAll(Arrays.asList(NULL));
        keywordList.addAll(Arrays.asList(BOOLEAN));
        Collections.sort(keywordList);
        return keywordList;
    }

}
