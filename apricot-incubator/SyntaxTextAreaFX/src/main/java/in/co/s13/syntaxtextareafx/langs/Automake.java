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
public class Automake implements Language {


    @Override
    public Pattern generatePattern() {
        Pattern pattern;
String COMMENT_PATTERN;
COMMENT_PATTERN = "#[^\n]*" ;//+ "|" + "/\\*(.|\\R)*?\\*/";


        pattern = Pattern.compile(
          "(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("COMMENT") != null ? "comment"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        Collections.sort(keywordList);
        return keywordList;
    }

}
