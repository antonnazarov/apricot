package za.co.apricotdb.support.export;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;

/**
 * Implementation of the serialization of the Relationship.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
@Component
public class RelationshipSerializer {

    @Autowired
    RelationshipManager relationshipManager;

    @Transactional
    public List<RelationshipHolder> getRelationships(ApricotProject project) {
        List<RelationshipHolder> ret = new ArrayList<>();

        List<ApricotRelationship> rels = relationshipManager.findRelationshipsByProject(project);
        for (ApricotRelationship r : rels) {
            RelationshipHolder h = new RelationshipHolder();
            h.setParentConstraintId(r.getParent().getId());
            h.setChildConstraintId(r.getChild().getId());
            ret.add(h);
        }

        return ret;
    }
}
