package za.co.apricotdb.viewport.relationship;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopology;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopologyImpl;

public class ApricotRelationshipBuilder implements RelationshipBuilder {

    private ApricotCanvas canvas = null;
    private RelationshipTopology topology;

    public ApricotRelationshipBuilder(ApricotCanvas canvas) {
        this.canvas = canvas;
        topology = new RelationshipTopologyImpl(canvas);
    }

    @Override
    public ApricotRelationship buildRelationship(String parentEntityName, String childEntityName, String primaryKeyName,
            String foreignKeyName, long relationshipId, RelationshipType type, boolean valid) {

        ApricotEntity parentEntity = canvas.findEntityByName(parentEntityName);
        ApricotEntity childEntity = canvas.findEntityByName(childEntityName);

        ApricotRelationship relationship = new ApricotRelationshipImpl(parentEntity, childEntity, primaryKeyName,
                foreignKeyName, relationshipId, type, topology, canvas);
        relationship.setValid(valid);
        relationship.setElementStatus(ElementStatus.DEFAULT);

        parentEntity.addLink(relationship, true);
        childEntity.addLink(relationship, false);

        return relationship;
    }
}
