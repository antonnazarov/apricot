package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotColumnConstraintRepository;
import za.co.apricotdb.persistence.repository.ApricotConstraintRepository;

@Component
public class ConstraintManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotColumnConstraintRepository columnConstraintRepository;

    @Resource
    ApricotConstraintRepository constraintRepository;

    @Autowired
    SnapshotManager snapshotManager;

    public List<ApricotConstraint> getConstraintsByTable(ApricotTable table) {
        TypedQuery<ApricotConstraint> query = em.createNamedQuery("ApricotConstraint.getConstraintsByTable",
                ApricotConstraint.class);
        query.setParameter("table", table);

        return query.getResultList();
    }

    public List<ApricotColumnConstraint> getConstraintColumns(ApricotConstraint constraint) {
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

    public ApricotConstraint getConstraintByName(String name) {
        ApricotConstraint ret = null;
        TypedQuery<ApricotConstraint> query = em.createNamedQuery("ApricotConstraint.getConstraintsByName",
                ApricotConstraint.class);
        query.setParameter("snapshot", snapshotManager.getDefaultSnapshot());
        query.setParameter("name", name);

        List<ApricotConstraint> c = query.getResultList();
        if (c != null && c.size() > 0) {
            ret = c.get(0);
        }

        return ret;
    }

    public void deleteConstraint(ApricotConstraint constraint) {
        constraintRepository.delete(constraint);
    }

    public void deleteConstraintColumn(ApricotColumnConstraint columnConstraint) {
        columnConstraintRepository.delete(columnConstraint);
    }

    public void saveConstraint(ApricotConstraint constraint) {
        constraintRepository.save(constraint);
    }

    /**
     * Find all unique constraints, which contain the given column.
     */
    public List<ApricotConstraint> getConstraintsByColumn(ApricotColumn column) {
        TypedQuery<ApricotConstraint> query = em.createNamedQuery("ApricotConstraint.getConstraintsByColumn",
                ApricotConstraint.class);
        query.setParameter("column", column);

        return query.getResultList();
    }

    public ApricotConstraint getConstraintById(long id) {
        Optional<ApricotConstraint> o = constraintRepository.findById(id);

        if (o.isEmpty()) {
            return null;
        }

        return o.get();
    }
}
