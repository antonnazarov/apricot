package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class ConstraintManager {

    @Resource
    EntityManager em;

    public List<ApricotConstraint> getConstraintsByTable(ApricotTable table) {
        List<ApricotConstraint> ret = new ArrayList<>();

        TypedQuery<ApricotConstraint> query = em.createNamedQuery("ApricotConstraint.getConstraintsByTable",
                ApricotConstraint.class);
        query.setParameter("table", table);

        return query.getResultList();
    }

    public List<ApricotColumnConstraint> getConstraintColumns(ApricotConstraint constraint) {
        List<ApricotColumnConstraint> ret = new ArrayList<>();

        TypedQuery<ApricotColumnConstraint> query = em
                .createNamedQuery("ApricotColumnConstraint.getColumnsByConstraint", ApricotColumnConstraint.class);
        query.setParameter("constraint", constraint);

        return query.getResultList();
    }
    
    public List<ApricotColumnConstraint> getConstraintColumnsByTable(ApricotTable table) {
        List<ApricotColumnConstraint> ret = new ArrayList<>();
        
        List<ApricotConstraint> constraints = getConstraintsByTable(table);
        for (ApricotConstraint c : constraints) {
            ret.addAll(getConstraintColumns(c));
        }
        
        return ret;
    }
}
