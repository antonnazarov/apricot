package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;

/**
 * Object Layout related low level- DB- operations are implemented in this class.
 * 
 * @author Anton Nazarov
 * @since 17/01/2019
 */
@Component
public class ObjectLayoutManager {
    
    @Resource
    EntityManager em;

    public List<ApricotObjectLayout> getObjectLayoutsByType(ApricotView view, LayoutObjectType type) {
        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutsByType", ApricotObjectLayout.class);
        query.setParameter("view", view);
        query.setParameter("objectType", type);
        
        return query.getResultList();
    }
}
