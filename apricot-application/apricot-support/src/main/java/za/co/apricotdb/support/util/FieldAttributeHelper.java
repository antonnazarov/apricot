package za.co.apricotdb.support.util;

import com.microsoft.sqlserver.jdbc.StringUtils;

/**
 * The implementation of the project wide functionality: view of the field length.   
 *  
 * @author Anton Nazarov
 * @since 19/11/2019
 */
public class FieldAttributeHelper {
    
    public static String formFieldLength(String length) {
        StringBuilder sb = new StringBuilder("");
        
        if (!StringUtils.isEmpty(length) && !length.equals("0")) {
            sb.append(" (").append(length).append(")");
        }
        
        return sb.toString();
    }
}
