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
public interface Language {
    /***
     * Generates a Pattern for Syntax Area
     * which then be matched to highlight special keywords
     * @return 
     */
         public Pattern generatePattern();
         /****
          * Matches special keywords with css fields 
          * @param matcher
          * @return 
          */
    public String getStyleClass(Matcher matcher);
    
    
    /***
     * Return all special keywords for suggestions Auto complete
     * @return 
     */
    public ArrayList<String> getKeywords();
}
