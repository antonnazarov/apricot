package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotView;

@Component
public class ViewManager {
    
    @Resource
    EntityManager em;

    public ApricotView getGeneralView(ApricotProject project) {
        ApricotView ret = null;
        
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getGeneralView", ApricotView.class);
        query.setParameter("project", project);
        List<ApricotView> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res.get(0);
        }
        
        return ret;
    }
}
