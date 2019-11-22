package za.co.apricotdb.ui.handler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class UniqueSetTest {

    @Test
    public void testUniqueCollections() {

        List<String> l1 = new ArrayList<>();
        l1.add("ONE");
        l1.add("ONE");
        l1.add("TWO");
        l1.add("TWO");

        List<String> l2 = new ArrayList<>();
        l2.add("ONE");
        l2.add("TWO");
        l2.add("THREE");

        Set<String> set = new HashSet<>(l1);
        set.addAll(l2);

        System.out.println(set);

        assertEquals(3, set.size());
    }

}
