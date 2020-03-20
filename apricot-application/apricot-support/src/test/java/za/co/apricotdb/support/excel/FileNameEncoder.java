package za.co.apricotdb.support.excel;

import org.junit.Test;

/**
 * The test of encoder of the file name.
 * 
 * @author Anton Nazarov
 * @since 20/03/2020
 */
public class FileNameEncoder {
    
    @Test
    public void testNameConverter() {
        String s = "Some name {with} strange \n //s/// ~dstuff";
        String name = s.replaceAll("\\W+", " ");
        name = name.trim().replaceAll(" +", " ");
        // string = string.replace(/\s\s+/g, ' ');
        
        System.out.println(name);
    }
}
