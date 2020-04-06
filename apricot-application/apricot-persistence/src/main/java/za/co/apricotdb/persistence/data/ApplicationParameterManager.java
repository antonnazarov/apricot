package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;

/**
 * The database related functionality for the Application Parameter.
 * 
 * @author Anton Nazarov
 * @since 27/02/2019
 */
@Component
public class ApplicationParameterManager {
    
    @Resource
    EntityManager em;
    
    public ApricotApplicationParameter getParameterByName(String name) {
        TypedQuery<ApricotApplicationParameter> query = em.createNamedQuery("ApricotApplicationParameter.getParameterByName", ApricotApplicationParameter.class);
        query.setParameter("name", name);
        List<ApricotApplicationParameter> params = query.getResultList();
        if (params != null && params.size() > 0) {
            return params.get(0);
        }
        
        return null;
    }
    
    public void saveParameter(String name, String value) {
        ApricotApplicationParameter param = new ApricotApplicationParameter(name, value);
        em.persist(param);
    }
}
