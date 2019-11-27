package za.co.apricotdb.persistence.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests around the ObjectLayoutManager class.
 * 
 * @author Anton Nazarov
 * @since 27/11/2019
 */
public class ObjectLayoutManagerTest {
    
    @Test
    public void testCalcDouble() {
        int a = 9;
        int b = 13;
        
        double ab = (double)a/b;
        
        assertEquals(0.6923076923076923, ab, 0.00001);
        
        System.out.println(ab);
    }
}
