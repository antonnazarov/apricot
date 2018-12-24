package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class HatShapeBuilder extends RelationshipShapeBuilderImpl {

    public HatShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder) {
        super(primitivesBuilder);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Point2D parentStart = getParentStart(relationship);
        Point2D childEnd = getChildEnd(relationship);

        HatRelationship shape = new HatRelationship(relationship);
        
        double defaultLeftRulerX = getDefaultLeftRulerX();
        
        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.HAT;
    }

    @Override
    protected Point2D getParentStart(ApricotRelationship relationship) {
        Point2D ret = null;

        VBox pBox = (VBox) relationship.getParent().getShape();
        if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
        } else {
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX() + pBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
        }

        return ret;
    }

    @Override
    protected Point2D getChildEnd(ApricotRelationship relationship) {
        Point2D ret = null;

        VBox cBox = (VBox) relationship.getChild().getShape();
        if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX() + cBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
        } else {
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
        }

        return ret;
    }
    
    private double getDefaultLeftRulerX() {
        return 0;
    }
}
