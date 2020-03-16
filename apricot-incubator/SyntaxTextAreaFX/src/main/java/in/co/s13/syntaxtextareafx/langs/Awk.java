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
public class Awk implements Language {

    String KEYWORDS[] = new String[]{"break","continue","do","delete","else","exit","for","function","getline","if","next","nextfile","print","printf","return","while"};
    String PATTERNS[] = new String[]{"BEGIN","END"};
    String VARIABLES[] = new String[]{"ARGC","ARGV","FILENAME","FNR","FS","NF","NR","OFMT","OFS","ORS","RLENGTH","RS","RSTART","SUBSEP","ARGIND","BINMODE","CONVFMT","ENVIRON","ERRNO","FIELDWIDTHS","IGNORECASE","LINT","PROCINFO","RT","RLENGTH","TEXTDOMAIN"};
    String BUILT_IN_FUNCTIONS[] = new String[]{"gsub","index","length","match","split","sprintf","sub","substr","tolower","toupper"};
    String ARITHMETIC_FUNCTIONS[] = new String[]{"atan2","cos","exp","int","log","rand","sin","sqrt","srand"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String KEYWORDS_PATTERN;
        String PATTERNS_PATTERN;
        String VARIABLES_PATTERN;
        String BUILT_IN_FUNCTIONS_PATTERN;
        String ARITHMETIC_FUNCTIONS_PATTERN;

        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        PATTERNS_PATTERN = "\\b(" + String.join("|", PATTERNS) + ")\\b";
        VARIABLES_PATTERN = "\\b(" + String.join("|", VARIABLES) + ")\\b";
        BUILT_IN_FUNCTIONS_PATTERN = "\\b(" + String.join("|", BUILT_IN_FUNCTIONS) + ")\\b";
        ARITHMETIC_FUNCTIONS_PATTERN = "\\b(" + String.join("|", ARITHMETIC_FUNCTIONS) + ")\\b";

        pattern = Pattern.compile(
                 "(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<PATTERNS>" + PATTERNS_PATTERN + ")"
                + "|(?<VARIABLES>" + VARIABLES_PATTERN + ")"
                + "|(?<BUILTINFUNCTIONS>" + BUILT_IN_FUNCTIONS_PATTERN + ")"
                + "|(?<ARITHMETICFUNCTIONS>" + ARITHMETIC_FUNCTIONS_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return  matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("PATTERNS") != null ? "patterns"
                : matcher.group("VARIABLES") != null ? "variables"
                : matcher.group("BUILTINFUNCTIONS") != null ? "built-in-functions"
                : matcher.group("ARITHMETICFUNCTIONS") != null ? "arithmetic-functions"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(PATTERNS));
        keywordList.addAll(Arrays.asList(VARIABLES));
        keywordList.addAll(Arrays.asList(BUILT_IN_FUNCTIONS));
        keywordList.addAll(Arrays.asList(ARITHMETIC_FUNCTIONS));
        Collections.sort(keywordList);
        return keywordList;
    }

}
