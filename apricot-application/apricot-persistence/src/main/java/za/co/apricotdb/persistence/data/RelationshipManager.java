package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class RelationshipManager {
    
    @Resource
    EntityManager em;

    public List<ApricotRelationship> getRelationshipsForTables(List<ApricotTable> tables) {
        List<ApricotRelationship> ret = new ArrayList<>();
        
        TypedQuery<ApricotRelationship> query = em.createNamedQuery("ApricotRelationship.getRelationshipsForTables", ApricotRelationship.class);
        query.setParameter("tables", tables);
        ret = query.getResultList();
        
        return ret;
    }
}
