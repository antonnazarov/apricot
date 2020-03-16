/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.syntaxtextareafx.langs;

import in.co.s13.syntaxtextareafx.meta.Language;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nika
 */
public class Forth implements Language {

    String KEYWORDS[] = new String[]{"!", "#", "#>",
        "#S", "\\*", "\\*/", "\\*/MOD",
        "\\+", "\\+!", "\\+LOOP", ",",
        "-", "\\.", "\\.\"", "/",
        "/MOD", "0<", "0=", "1\\+",
        "1-", "2!", "2\\*", "2/",
        "2@", "2DROP", "2DUP", "2OVER",
        "2SWAP", ":", ";", "<",
        "<#", "=", ">", ">BODY",
        ">IN", ">NUMBER", ">R", "\\?DUP",
        "@", "ABORT", "ABORT\"", "ABS",
        "ACCEPT", "ALIGN", "ALIGNED", "ALLOT",
        "AND", "BASE", "BEGIN", "BL",
        "C!", "C,", "C@", "CELL\\+",
        "CELLS", "CHAR", "CHAR\\+", "CHARS",
        "CONSTANT", "COUNT", "CR", "CREATE",
        "DECIMAL", "DEPTH", "DO", "DOES>",
        "DROP", "DUP", "ELSE", "EMIT",
        "ENVIRONMENT\\?", "EVALUATE", "EXECUTE", "EXIT",
        "FILL", "FIND", "FM/MOD", "HERE",
        "HOLD", "I", "IF", "IMMEDIATE",
        "INVERT", "J", "KEY", "LEAVE",
        "LITERAL", "LOOP", "LSHIFT", "M\\*",
        "MAX", "MIN", "MOD", "MOVE",
        "NEGATE", "OR", "OVER", "POSTPONE",
        "QUIT", "R>", "R@", "RECURSE",
        "REPEAT", "ROT", "RSHIFT", "S\"",
        "S>D", "SIGN", "SM/REM", "SOURCE",
        "SPACE", "SPACES", "STATE", "SWAP",
        "THEN", "TYPE", "U\\.", "U<",
        "UM\\*", "UM/MOD", "UNLOOP", "UNTIL",
        "VARIABLE", "WHILE", "WORD", "XOR",
        "\\[CHAR\\]"};

    String EXT_KEYWORDS[] = new String[]{"\\.\\(",
        "\\.R", "0<>", "0>", "2>R",
        "2R>", "2R@", ":NONAME", "<>",
        "\\?DO", "AGAIN", "C\"", "CASE",
        "COMPILE,", "ENDCASE", "ENDOF", "ERASE",
        "HEX", "MARKER", "NIP", "OF",
        "PAD", "PARSE", "PICK", "REFILL",
        "RESTORE-INPUT", "ROLL", "SAVE-INPUT", "SOURCE-ID",
        "TO", "TUCK", "U\\.R", "U>",
        "UNUSED", "VALUE", "WITHIN", "\\[COMPILE\\]", "\\\\"};

    String OBS_EXT_KEYWORDS[] = new String[]{
        "#TIB", "CONVERT", "EXPECT", "QUERY", "SPAN", "TIB"};
    String BLOCK_KEYWORDS[] = new String[]{
        "BLK", "BLOCK", "BUFFER", "EVALUATE",
        "FLUSH", "LOAD", "SAVE-BUFFERS", "UPDATE"};
    String BLOCK_EXT_KEYWORDS[] = new String[]{
        "EMPTY-BUFFERS", "LIST", "REFILL", "SCR",
        "THRU", "\\\\"};

    String BOOLEAN[] = new String[]{"true", "false"};

    @Override
    public Pattern generatePattern() {
        String BOOLEAN_PATTERN;
        String KEYWORDS_PATTERN;
        String EXT_KEYWORDS_PATTERN;
        String OBS_EXT_KEYWORDS_PATTERN;
        String BLOCK_KEYWORDS_PATTERN;
        String BLOCK_EXT_KEYWORDS_PATTERN;
        String PAREN_PATTERN;
        String BRACE_PATTERN;
        String BRACKET_PATTERN;
        String SEMICOLON_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;

        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", EXT_KEYWORDS) + ")\\b";
        OBS_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", OBS_EXT_KEYWORDS) + ")\\b";
        BLOCK_KEYWORDS_PATTERN = "\\b(" + String.join("|", BLOCK_KEYWORDS) + ")\\b";
        BLOCK_EXT_KEYWORDS_PATTERN = "\\b(" + String.join("|", BLOCK_EXT_KEYWORDS) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        PAREN_PATTERN = "\\(|\\)";
        BRACE_PATTERN = "\\{|\\}";
        BRACKET_PATTERN = "\\[|\\]";
        SEMICOLON_PATTERN = "\\;";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "--[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        Pattern pattern = Pattern.compile(
                "(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<EXTKEYWORDS>" + EXT_KEYWORDS_PATTERN + ")"
                + "|(?<OBSEXTKEYWORDS>" + OBS_EXT_KEYWORDS_PATTERN + ")"
                + "|(?<BLOCKKEYWORDS>" + BLOCK_KEYWORDS_PATTERN + ")"
                + "|(?<BLOCKEXTKEYWORDS>" + BLOCK_EXT_KEYWORDS_PATTERN + ")"
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<String> getKeywords() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
