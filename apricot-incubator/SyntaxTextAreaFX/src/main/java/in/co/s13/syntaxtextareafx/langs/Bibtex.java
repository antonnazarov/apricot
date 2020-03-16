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
public class Bibtex implements Language {

    String ENTRY_TYPE[] = new String[]{"book","article","booklet","conference","inbook","incollection","inproceedings","manual","mastersthesis","lambda","misc","phdthesis","proceedings","techreport","unpublished"};
    String FIELD[] = new String[]{"author","title","journal","year","volume","number","pages","month","note","key","publisher","editor","series","address","edition","howpublished","booktitle","organization","chapter","school","institution","type"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String ENTRY_TYPE_PATTERN;
        String FIELD_PATTERN;

        ENTRY_TYPE_PATTERN = "\\b(" + String.join("|", ENTRY_TYPE) + ")\\b";
        FIELD_PATTERN = "\\b(" + String.join("|", FIELD) + ")\\b";

        pattern = Pattern.compile(
                 "(?<ENTRYTYPE>" + ENTRY_TYPE_PATTERN + ")"
                + "|(?<FIELD>" + FIELD_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return  matcher.group("ENTRYTYPE") != null ? "entry-type"
                : matcher.group("FIELD") != null ? "field"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(ENTRY_TYPE));
        keywordList.addAll(Arrays.asList(FIELD));
        Collections.sort(keywordList);
        return keywordList;
    }

}
