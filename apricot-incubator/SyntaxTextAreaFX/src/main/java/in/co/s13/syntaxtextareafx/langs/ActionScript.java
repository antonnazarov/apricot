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
public class ActionScript implements Language {

    String DECLARATIONS[] = new String[]{"class", "extends",
        "function", "implements", "instanceof", "interface",
        "is", "namespace", "throws", "var", "const"};
    String PRIMITIVES[] = new String[]{
        "arguments", "Array",
        "Boolean", "Class",
        "Bitmap", "BitmapData",
        "BitmapDataChannel", "ByteArray",
        "Camera", "Capabilities",
        "CapsStyle", "ColorTransform",
        "ContextMenu", "Dictionary",
        "DisplayObject", "DisplayObjectContainer",
        "Endian", "Error",
        "Event", "EventDispatcher",
        "ExternalInterface", "FileFilter",
        "FileReference", "FileReferenceList",
        "Function", "Graphics",
        "int", "IBitmapDrawable",
        "IEventDispatcher", "IOError",
        "Keyboard", "KeyboardEvent",
        "KeyLocation", "Loader",
        "LocalConnection", "Math",
        "Matrix", "Microphone",
        "Mouse", "MovieClip",
        "Namespace", "NetConnection",
        "NetStream", "Number",
        "Object", "Point",
        "PrintJob", "Proxy",
        "QName", "Rectangle",
        "RegExp", "Responder",
        "Scene", "Security",
        "Shape", "SharedObject",
        "Socket", "Sound",
        "SoundChannel", "SoundTransform",
        "Sprite", "Stage",
        "String", "StyleSheet",
        "SWFVersion", "System",
        "TextField", "TextFormat",
        "Timer", "uint",
        "Video", "XML",
        "XMLDocument", "XMLList",
        "XMLNode", "XMLNodeType",
        "XMLSocket"};
    String EXTERNALS[] = new String[]{
        "import", "include", "package"};
    String STORAGECLASS[] = new String[]{
        "dynamic", "internal",
        "final", "static"};
    String SCOPEDECLARATIONS[] = new String[]{
        "flash_proxy", "internal",
        "override", "private",
        "protected", "public",
        "set", "get"};
    String FLOW[] = new String[]{
        "break", "case",
        "catch", "continue",
        "default", "do",
        "else", "for",
        "if", "is",
        "return", "throw",
        "switch", "try",
        "while"};
    String MEMORY[] = new String[]{
        "new", "super",
        "this", "void"};
    String FUTURE[] = new String[]{
        "goto"};
    String NULL[] = new String[]{
        "null"};
    String BOOLEAN[] = new String[]{"true", "false"};

    @Override
    public Pattern generatePattern() {
        String DECLARATIONS_PATTERN;
        String PRIMITIVES_PATTERN;
        String EXTERNALS_PATTERN;
        String STORAGECLASS_PATTERN;
        String SCOPEDECLARATIONS_PATTERN;
        String FLOW_PATTERN;
        String MEMORY_PATTERN;
        String FUTURE_PATTERN;
        String NULL_PATTERN;
        String BOOLEAN_PATTERN;
        String PAREN_PATTERN;
        String BRACE_PATTERN;
        String BRACKET_PATTERN;
        String SEMICOLON_PATTERN;
        String STRING_PATTERN;
        String COMMENT_PATTERN;

        DECLARATIONS_PATTERN = "\\b(" + String.join("|", DECLARATIONS) + ")\\b";
        PRIMITIVES_PATTERN = "\\b(" + String.join("|", PRIMITIVES) + ")\\b";
        EXTERNALS_PATTERN = "\\b(" + String.join("|", EXTERNALS) + ")\\b";
        STORAGECLASS_PATTERN = "\\b(" + String.join("|", STORAGECLASS) + ")\\b";
        SCOPEDECLARATIONS_PATTERN = "\\b(" + String.join("|", SCOPEDECLARATIONS) + ")\\b";
        FLOW_PATTERN = "\\b(" + String.join("|", FLOW) + ")\\b";
        MEMORY_PATTERN = "\\b(" + String.join("|", MEMORY) + ")\\b";
        FUTURE_PATTERN = "\\b(" + String.join("|", FUTURE) + ")\\b";
        NULL_PATTERN = "\\b(" + String.join("|", NULL) + ")\\b";
        BOOLEAN_PATTERN = "\\b(" + String.join("|", BOOLEAN) + ")\\b";
        PAREN_PATTERN = "\\(|\\)";
        BRACE_PATTERN = "\\{|\\}";
        BRACKET_PATTERN = "\\[|\\]";
        SEMICOLON_PATTERN = "\\;";
        STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
        COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        Pattern pattern = Pattern.compile(
                "(?<DECLARATIONS>" + DECLARATIONS_PATTERN + ")"
                + "|(?<PRIMITIVES>" + PRIMITIVES_PATTERN + ")"
                + "|(?<EXTERNALS>" + EXTERNALS_PATTERN + ")"
                + "|(?<STORAGECLASS>" + STORAGECLASS_PATTERN + ")"
                + "|(?<SCOPEDECLARATIONS>" + SCOPEDECLARATIONS_PATTERN + ")"
                + "|(?<FLOW>" + FLOW_PATTERN + ")"
                + "|(?<MEMORY>" + MEMORY_PATTERN + ")"
                + "|(?<FUTURE>" + FUTURE_PATTERN + ")"
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
        return matcher.group("DECLARATIONS") != null ? "declarations"
                : matcher.group("PRIMITIVES") != null ? "primitives"
                : matcher.group("EXTERNALS") != null ? "externals"
                : matcher.group("STORAGECLASS") != null ? "storageclass"
                : matcher.group("SCOPEDECLARATIONS") != null ? "scopedeclarations"
                : matcher.group("FLOW") != null ? "flow"
                : matcher.group("MEMORY") != null ? "memory"
                : matcher.group("FUTURE") != null ? "future"
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
        keywordList.addAll(Arrays.asList(DECLARATIONS));
        keywordList.addAll(Arrays.asList(PRIMITIVES));
        keywordList.addAll(Arrays.asList(EXTERNALS));
        keywordList.addAll(Arrays.asList(STORAGECLASS));
        keywordList.addAll(Arrays.asList(SCOPEDECLARATIONS));
        keywordList.addAll(Arrays.asList(FLOW));
        keywordList.addAll(Arrays.asList(MEMORY));
        keywordList.addAll(Arrays.asList(FUTURE));
        keywordList.addAll(Arrays.asList(NULL));
        keywordList.addAll(Arrays.asList(BOOLEAN));
        Collections.sort(keywordList);
        return keywordList;
    }

}
