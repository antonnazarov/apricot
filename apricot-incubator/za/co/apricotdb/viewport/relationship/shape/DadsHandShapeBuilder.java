package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class DadsHandShapeBuilder extends RelationshipShapeBuilderImpl {

    public DadsHandShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder, 
            RelationshipTopology relationshipTopology) {
        super(primitivesBuilder, relationshipTopology);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.DADS_HAND;
    }
}
