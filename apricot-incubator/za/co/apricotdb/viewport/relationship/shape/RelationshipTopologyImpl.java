package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Side;
import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class RelationshipTopologyImpl implements RelationshipTopology {

    private RelationshipShapeBuilder directBuilder = null;
    private RelationshipShapeBuilder hatBuilder = null;
    private RelationshipShapeBuilder dadsHandBuilder = null;
    private RelationshipShapeBuilder roofHandBuilder = null;

    public RelationshipTopologyImpl() {
        RelationshipPrimitivesBuilder primitivesBuilder = new RelationshipPrimitivesBuilderImpl();
        directBuilder = new DirectShapeBuilder(primitivesBuilder, this);
        hatBuilder = new HatShapeBuilder(primitivesBuilder, this);
        dadsHandBuilder = new DadsHandShapeBuilder(primitivesBuilder, this);
        roofHandBuilder = new RoofShapeBuilder(primitivesBuilder, this);
    }

    @Override
    public RelationshipShapeBuilder getRelationshipShapeBuilder(ApricotRelationship relationship) {
        RelationshipShapeBuilder ret = null;
        switch (calculateRelationshipShapeType(relationship)) {
        case DIRECT:
            ret = directBuilder;
            break;
        case HAT:
            ret = hatBuilder;
            break;
        case DADS_HAND:
            ret = dadsHandBuilder;
            break;
        case ROOF:
            ret = roofHandBuilder;
            break;
        case HOOK:
            ret = null;
            break;
        }

        return ret;
    }

    /**
     * Calculate, which type of relationship should be drawn, depending on the
     * current entities mutual allocation.
     */
    private RelationshipShapeType calculateRelationshipShapeType(ApricotRelationship relationship) {
        RelationshipShapeType ret = null;

        double hDist = TopologyHelper.getHorizontalDistance(relationship.getParent(), relationship.getChild());
        if (relationship.getRelationshipType() != RelationshipType.IDENTIFYING) {
            if (hDist > MIN_HORIZONTAL_DISTANCE) {
                ret = RelationshipShapeType.DIRECT;
            } 
            else if (isHatType(relationship)) {
                ret = RelationshipShapeType.HAT;
            } else {
                ret = RelationshipShapeType.DADS_HAND;
/*
                boolean isDadsHand = false;
                double parentYPoint = TopologyHelper.getFieldY(relationship.getParent(),
                        relationship.getPrimaryKeyName());
                double childYPoint = TopologyHelper.getFieldY(relationship.getChild(),
                        relationship.getForeignKeyName());
                if (parentYPoint > childYPoint) {
                    isDadsHand = isDadsHand(relationship.getParent(), relationship.getChild(), parentYPoint,
                            childYPoint);
                } else {
                    isDadsHand = isDadsHand(relationship.getChild(), relationship.getParent(), childYPoint,
                            parentYPoint);
                }

                if (isDadsHand) {
                    ret = RelationshipShapeType.DADS_HAND;
                } else {
                    ret = RelationshipShapeType.HAT;
                }
*/                
            }
        } else {
            // identifying relationships: ROOF or HOOK
            ret = RelationshipShapeType.ROOF;
        }

        return ret;
    }

    /**
     * Detect the "HAT"- type shape.
     */
    private boolean isHatType(ApricotRelationship relationship) {
        boolean ret = false;
        
        double parentYPoint = TopologyHelper.getFieldY(relationship.getParent(),
                relationship.getPrimaryKeyName());
        double childYPoint = TopologyHelper.getFieldY(relationship.getChild(),
                relationship.getForeignKeyName());        
        
        ApricotEntityShape shape = relationship.getParent().getEntityShape();
        double parentMinY = shape.getLayoutY() + shape.getTranslateY() - MIN_VERTICAL_DISTANCE;
        double parentMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight() + MIN_VERTICAL_DISTANCE;
        
        shape = relationship.getChild().getEntityShape();
        double childMinY = shape.getLayoutY() + shape.getTranslateY() - MIN_VERTICAL_DISTANCE;
        double childMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight() + MIN_VERTICAL_DISTANCE;
        
        if (parentMinY <= childYPoint && childYPoint <= parentMaxY && 
                childMinY <= parentYPoint && parentYPoint <= childMaxY) {
            ret = true;
        }
        
        return ret;
    }

    private boolean isDadsHand(ApricotEntity hEntity, ApricotEntity lEntity, double hY, double lY) {
        boolean ret = false;

        VBox hBox = (VBox) hEntity.getShape();
        VBox lBox = (VBox) lEntity.getShape();

        if (hY < lBox.getLayoutY() + lBox.getTranslateY() - MIN_VERTICAL_DISTANCE) {
            ret = true;
        } else if (lY > hBox.getLayoutY() + hBox.getTranslateY() + hBox.getHeight() + MIN_VERTICAL_DISTANCE) {
            ret = true;
        }

        return ret;
    }

    /**
     * Calculate the exact side of the relationship primary key output.
     */

    @Override
    public Side getRelationshipSide(ApricotRelationship relationship, boolean isPrimaryKey) {
        Side ret = null;

        RelationshipShapeType rShapeType = calculateRelationshipShapeType(relationship);
        switch (rShapeType) {
        case DIRECT:
            if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
                ret = isPrimaryKey ? Side.RIGHT : Side.LEFT;
            } else {
                ret = isPrimaryKey ? Side.LEFT : Side.RIGHT;
            }

            break;
        case DADS_HAND:
            if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
                ret = Side.RIGHT;
            } else {
                ret = Side.LEFT;
            }
            break;
        case HAT:
            if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
                ret = isPrimaryKey ? Side.LEFT : Side.RIGHT;
            } else {
                ret = isPrimaryKey ? Side.RIGHT : Side.LEFT;
            }
            break;
        case ROOF:
            ret = Side.TOP;
            break;
        case HOOK:
            ret = Side.TOP;
            break;
        }

        return ret;
    }
}
