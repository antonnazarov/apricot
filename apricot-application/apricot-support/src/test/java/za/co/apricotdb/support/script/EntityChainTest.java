package za.co.apricotdb.support.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EntityChainTest {

    EntityChain chain;

    @Before
    public void setUp() {
        chain = new EntityChain();

        Entity e = new Entity("A");
        chain.addEntity(e);
        e = new Entity("B");
        chain.addEntity(e);
        e = new Entity("C");
        chain.addEntity(e);
        e = new Entity("AX");
        chain.addEntity(e);
        e = new Entity("AY");
        chain.addEntity(e);
        e = new Entity("AZ");
        chain.addEntity(e);
        e = new Entity("BZ");
        chain.addEntity(e);
        e = new Entity("SZ");
        chain.addEntity(e);
        e = new Entity("MZ");
        chain.addEntity(e);
        e = new Entity("LA");
        chain.addEntity(e);
        e = new Entity("LB");
        chain.addEntity(e);

        chain.addRelationship("A", "AX");
        chain.addRelationship("B", "AX");
        chain.addRelationship("B", "AY");
        chain.addRelationship("C", "AZ");
        chain.addRelationship("AZ", "BZ");
        chain.addRelationship("BZ", "AY");
        chain.addRelationship("AY", "AX");
        chain.addRelationship("AY", "SZ");
        chain.addRelationship("SZ", "MZ");
        chain.addRelationship("BZ", "MZ");
        chain.addRelationship("AX", "MZ");
        chain.addRelationship("AX", "LA");
        chain.addRelationship("SZ", "LB");
        chain.addRelationship("BZ", "LB");
        chain.addRelationship("MZ", "LB");
        chain.addRelationship("BZ", "SZ");
    }

    @Test
    public void testSortEntitiesNoLoops() {
        List<Entity> sortedEntities = chain.sortEntities();
        for (Entity e : sortedEntities) {
            System.out.println(e);
        }

        assertEquals(11, sortedEntities.size());
    }

    @Test
    public void testNoDeadLoop() {
        chain.sortEntities();
        List<Entity> loopEntities = chain.getDeadLoopEntities();
        assertNull(loopEntities);
    }

    @Test
    public void testSortEntitiesWithLoops() {
        chain.addRelationship("AY", "AZ");
        List<Entity> sortedEntities = chain.sortEntities();
        assertTrue(sortedEntities.size() < 11);
    }

    @Test
    public void testGetDeadLoop() {
        chain.addRelationship("AY", "AZ");
        chain.sortEntities();
        List<Entity> loopEntities = chain.getDeadLoopEntities();
        assertEquals(3, loopEntities.size());

        System.out.println("The dead loop:");
        for (Entity e : loopEntities) {
            System.out.println(e);
        }
    }
}
