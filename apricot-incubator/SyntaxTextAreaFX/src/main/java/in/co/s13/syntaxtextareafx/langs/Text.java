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
public class Text implements Language {

    @Override
    public Pattern generatePattern() {
    return Pattern.compile("");
    }

    @Override
    public String getStyleClass(Matcher matcher) {
    return "";
    }

    @Override
    public ArrayList<String> getKeywords() {
        return new ArrayList<>();
    }

}
