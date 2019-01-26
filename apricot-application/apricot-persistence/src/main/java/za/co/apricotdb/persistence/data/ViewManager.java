package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.repository.ApricotObjectLayoutRepository;
import za.co.apricotdb.persistence.repository.ApricotViewRepository;

@Component
public class ViewManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotViewRepository viewRepository;
    
    @Resource
    ApricotObjectLayoutRepository objectLayoutRepository;

    public ApricotView getGeneralView(ApricotProject project) {
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getGeneralView", ApricotView.class);
        query.setParameter("project", project);
        
        return query.getSingleResult();
    }

    /**
     * Get all views of the given project ordered by the ordinal position.
     */
    public List<ApricotView> getAllViews(ApricotProject project) {
        List<ApricotView> ret = new ArrayList<>();

        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getAllViewsOrdered", ApricotView.class);
        query.setParameter("project", project);
        List<ApricotView> res = query.getResultList();

        if (res != null && res.size() > 0) {
            return res;
        }

        return ret;
    }
    
    public void removeGeneralView(ApricotProject project) {
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getGeneralView", ApricotView.class);
        query.setParameter("project", project);
        
        List<ApricotView> res = query.getResultList();
        if (res != null && res.size() > 0) {
            for (ApricotView v : res) {
                objectLayoutRepository.delete(v.getObjectLayouts());
                viewRepository.delete(v);
            }
        }
    }
    
    public ApricotView saveView(ApricotView view) {
        return viewRepository.save(view);
    }
    
    public List<ApricotView> getViewByName(ApricotProject project, String name) {
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getViewByName", ApricotView.class);
        query.setParameter("project", project);
        query.setParameter("name", name);
        
        return query.getResultList();
    }
    
    public int getMaxOrdinalPosition(ApricotProject project) {
        TypedQuery<Integer> query = em.createNamedQuery("ApricotView.getViewMaxOrdinalPosition", Integer.class);
        query.setParameter("project", project);
        
        return query.getSingleResult();
    }
}
