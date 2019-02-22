package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The model of the Edit Black List form.
 * 
 * @author Anton Nazarov
 * @since 22/02/2019
 */
public class BlackListFormModel implements Serializable {

    private static final long serialVersionUID = -2912242996437621470L;

    private List<String> allTables = new ArrayList<>();
    private List<String> blackListTables = new ArrayList<>();

    public List<String> getAllTables() {
        return allTables;
    }

    public void setAllTables(List<String> allTables) {
        this.allTables = allTables;
    }

    public List<String> getBlackListTables() {
        return blackListTables;
    }

    public void setBlackListTables(List<String> blackListTables) {
        this.blackListTables = blackListTables;
    }
    
    public String getBlackListAsString() {
        StringBuilder sb = new StringBuilder();
        
        for (String t : blackListTables) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(t);
        }
        
        return sb.toString();
    }
}
