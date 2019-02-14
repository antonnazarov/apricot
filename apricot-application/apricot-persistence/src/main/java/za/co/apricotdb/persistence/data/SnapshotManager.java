package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.repository.ApricotSnapshotRepository;

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
    
    @Resource
    ApricotSnapshotRepository snapshotRepository;
    
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
    
    public List<ApricotSnapshot> getAllSnapshots(ApricotProject project) {
        List<ApricotSnapshot> ret = new ArrayList<>();
        
        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getAllSnapshotsInProject", ApricotSnapshot.class);
        query.setParameter("project", project);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size()>0) {
            ret = res;
        }
        
        return ret;
    }
    
    public ApricotSnapshot getSnapshotById(long id) {
        return snapshotRepository.getOne(id);
    }
    
    
    public ApricotSnapshot getSnapshotByName(String name) {
        ApricotSnapshot ret = null;
        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getSnapshotByName", ApricotSnapshot.class);
        query.setParameter("name", name);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size()>0) {
            ret = res.get(0);
        }
        
        return ret;
    }
    
    public ApricotSnapshot saveSnapshot(ApricotSnapshot snapshot) {
        return snapshotRepository.save(snapshot);
    }
}
