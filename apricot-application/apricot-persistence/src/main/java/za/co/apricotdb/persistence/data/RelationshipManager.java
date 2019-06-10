package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;

@Component
public class RelationshipManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    public List<ApricotRelationship> getRelationshipsForTables(List<ApricotTable> tables) {
        List<ApricotRelationship> ret = new ArrayList<>();

        TypedQuery<ApricotRelationship> query = em.createNamedQuery("ApricotRelationship.getRelationshipsForTableList",
                ApricotRelationship.class);
        query.setParameter("tables", tables);
        ret = query.getResultList();

        return ret;
    }

    public List<ApricotRelationship> getRelationshipsForTable(ApricotTable table) {
        List<ApricotRelationship> ret = new ArrayList<>();

        TypedQuery<ApricotRelationship> query = em.createNamedQuery("ApricotRelationship.getRelationshipsForTable",
                ApricotRelationship.class);
        query.setParameter("table", table);
        ret = query.getResultList();

        return ret;
    }

    public void saveRelationship(ApricotRelationship relationship) {
        relationshipRepository.save(relationship);
    }

    public ApricotRelationship findRelationshipById(long id) {
        Optional<ApricotRelationship> o = relationshipRepository.findById(id);
        
        return o.get();
    }

    public void deleteRelationship(ApricotRelationship relationship) {
        relationshipRepository.delete(relationship);
    }

    public List<ApricotRelationship> findRelationshipsByParentConstraint(ApricotConstraint constraint) {
        TypedQuery<ApricotRelationship> query = em
                .createNamedQuery("ApricotRelationship.findRelationshipsByParentConstraint", ApricotRelationship.class);
        query.setParameter("parentConstraint", constraint);
        return query.getResultList();
    }

    public List<ApricotRelationship> findRelationshipsByConstraint(ApricotConstraint constraint) {
        TypedQuery<ApricotRelationship> query = em.createNamedQuery("ApricotRelationship.findRelationshipsByConstraint",
                ApricotRelationship.class);
        query.setParameter("constraint", constraint);
        return query.getResultList();
    }

    /**
     * Find all Relationships for the given Entities, which are not internal between
     * these Entities.
     */
    public List<ApricotRelationship> findExernalRelationships(List<ApricotTable> tables, boolean outgoing) {
        List<ApricotRelationship> ret = new ArrayList<>();

        List<ApricotRelationship> innerRel = getRelationshipsForTables(tables);
        for (ApricotTable table : tables) {
            List<ApricotRelationship> tabRel = getRelationshipsForTable(table);
            for (ApricotRelationship r : tabRel) {
                if (!innerRel.contains(r)) {
                    if (outgoing && r.getParent().getTable().equals(table)
                            || !outgoing && r.getChild().getTable().equals(table)) {
                        ret.add(r);
                    }
                }
            }
        }

        return ret;
    }
}
