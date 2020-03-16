/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.syntaxtextareafx.meta;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nika
 */
public class Syntax {

    private Language language;

    public Syntax(Language language) {
        this.language = language;
    }

    public Pattern generatePattern() {
        return language.generatePattern();
    }

    public String getStyleClass(Matcher matcher) {
        return language.getStyleClass(matcher);
    }

    public ArrayList<String> getKeywords() {
        return language.getKeywords();
    }
}
