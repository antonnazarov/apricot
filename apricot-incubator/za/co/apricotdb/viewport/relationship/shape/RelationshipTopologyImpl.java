package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Side;
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
            } else if (isHatType(relationship)) {
                ret = RelationshipShapeType.HAT;
            } else {
                ret = RelationshipShapeType.DADS_HAND;
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

        double parentYPoint = TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName());
        double childYPoint = TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName());

        ApricotEntityShape shape = relationship.getParent().getEntityShape();
        double parentMinY = shape.getLayoutY() + shape.getTranslateY() - MIN_VERTICAL_DISTANCE;
        double parentMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight() + MIN_VERTICAL_DISTANCE;

        shape = relationship.getChild().getEntityShape();
        double childMinY = shape.getLayoutY() + shape.getTranslateY() - MIN_VERTICAL_DISTANCE;
        double childMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight() + MIN_VERTICAL_DISTANCE;

        if (parentMinY <= childYPoint && childYPoint <= parentMaxY && childMinY <= parentYPoint
                && parentYPoint <= childMaxY) {
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
            ret = getDadsHandSide(relationship, isPrimaryKey);
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

    /**
     * Find the "open" side of the DAD's Hand. If both sides are open, the highest
     * one has preference.
     */
    private Side getDadsHandSide(ApricotRelationship relationship, boolean isPrimaryKey) {
        Side ret = null;

        double parentYPoint = TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName());
        double childYPoint = TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName());

        ApricotEntityShape shape = relationship.getParent().getEntityShape();
        double parentMinY = shape.getLayoutY() + shape.getTranslateY() - RelationshipTopology.MIN_VERTICAL_DISTANCE;
        double parentMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight()
                + RelationshipTopology.MIN_VERTICAL_DISTANCE;

        shape = relationship.getChild().getEntityShape();
        double childMinY = shape.getLayoutY() + shape.getTranslateY() - RelationshipTopology.MIN_VERTICAL_DISTANCE;
        double childMaxY = shape.getLayoutY() + shape.getTranslateY() + shape.getHeight()
                + RelationshipTopology.MIN_VERTICAL_DISTANCE;

        if (parentYPoint < childMinY || parentYPoint > childMaxY) {
            if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
                ret = Side.RIGHT;
            } else {
                ret = Side.LEFT;
            }
        } else if (childYPoint < parentMinY || childYPoint > parentMaxY) {
            if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
                ret = Side.LEFT;
            } else {
                ret = Side.RIGHT;
            }
        }

        return ret;
    }
}
