package za.co.apricotdb.support.script;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EntityChainTest {
    
    EntityChain chain = new EntityChain();
    
    @Before
    public void setUp() {
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
        
        chain.addRelationship("AY", "AZ");  //  Stack Overflow !!!!        
    }
    
    @Test
    public void testSortEntities() {
        List<Entity> entities = chain.sortEntities();
        for (Entity e : entities) {
            System.out.println(e);
        }
    }

}
