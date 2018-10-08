package za.co.apricotdb.persistence.entity;

/**
 * The types of constraints.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public enum ConstraintType {
    PRIMARY_KEY, FOREIGN_KEY, UNIQUE, UNIQUE_INDEX, NON_UNIQUE_INDEX;
    
    public int getOrder() {
        int order = 0;
        if (this == PRIMARY_KEY) order = 0;
        if (this == FOREIGN_KEY) order = 1;
        if (this == UNIQUE) order = 2;
        if (this == UNIQUE_INDEX) order = 3;
        if (this == NON_UNIQUE_INDEX) order = 4;
        
        return order;
    }
    
    public String getAbbreviation() {
        String abbr = null;
        if (this == PRIMARY_KEY) abbr = "PK";
        if (this == FOREIGN_KEY) abbr = "FK";
        if (this == UNIQUE) abbr = "UC";
        if (this == UNIQUE_INDEX) abbr = "UIDX";
        if (this == NON_UNIQUE_INDEX) abbr = "IDX";
        
        return abbr;
    }
}
