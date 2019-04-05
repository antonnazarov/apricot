package za.co.apricotdb.ui.util;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The auxiliary methods for ApricotEntity class.
 * 
 * @author Anton Nazarov
 * @since 04/04/2019
 */
public class ApricotTableUtils {
    
    public static String getTablesAsString(List<ApricotTable> tables) {
        StringBuilder sb = new StringBuilder();
        
        boolean first = true;
        for (ApricotTable t : tables) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(t.getName());
        }
        
        return sb.toString();
    }

}
