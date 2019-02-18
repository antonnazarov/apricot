package za.co.apricotdb.ui.util;

import java.util.Base64;

/**
 * The simplest Base64 encoder/decoder.
 *  
 * @author Anton Nazarov
 * @since 18/02/2019
 */
public class StringEncoder {
    
    public static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }
    
    public static String decode(String text) {
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        
        return new String(decodedBytes);
    }
}
