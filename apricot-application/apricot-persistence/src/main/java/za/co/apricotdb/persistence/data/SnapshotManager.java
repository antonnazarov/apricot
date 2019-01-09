package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The SNAPSHOT- related operations.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class SnapshotManager {
    
    @Resource
    EntityManager em;
    
    public ApricotSnapshot getDefaultSnapshot(ApricotProject project) {
        ApricotSnapshot ret = null;
        
        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getDefaultSnapshot", ApricotSnapshot.class);
        query.setParameter("project", project);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res.get(0);
        }
        
        return ret;
    }
}
