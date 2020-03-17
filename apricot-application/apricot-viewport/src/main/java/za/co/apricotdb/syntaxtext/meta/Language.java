package za.co.apricotdb.syntaxtext.meta;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The language interface.
 * 
 * @author Anton Nazarov
 * @since 17/03/2020
 */
public interface Language {

    /**
     * Generate a Pattern for Syntax Area which then be matched to highlight the
     * special keywords.
     */
    public Pattern generatePattern();

    /**
     * Matches special keywords with css fields.
     */
    public String getStyleClass(Matcher matcher);

    /**
     * Return all special keywords for suggestions Auto complete.
     */
    public ArrayList<String> getKeywords();
}
