package javafxapplication.relationship;

import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.shape.RelationshipTopology;
import javafxapplication.relationship.shape.RelationshipTopologyImpl;

public class ApricotRelationshipBuilder implements RelationshipBuilder {
    
    private ApricotCanvas canvas = null;
    private RelationshipTopology topology;
    
    public ApricotRelationshipBuilder(ApricotCanvas canvas) {
        this.canvas = canvas;
        topology = new RelationshipTopologyImpl();
    }

    @Override
    public ApricotRelationship buildRelationship(String parentEntityName, String childEntityName, String primaryKeyName,
            String foreignKeyName, RelationshipType type) {
        
        ApricotEntity parentEntity = canvas.findEntityByName(parentEntityName);
        ApricotEntity childEntity = canvas.findEntityByName(parentEntityName);
        
        ApricotRelationship relationship = new ApricotRelationshipImpl(parentEntity, childEntity, 
                primaryKeyName, foreignKeyName, type, topology, canvas);
        relationship.buildShape();
        
        return relationship;
    }
}
