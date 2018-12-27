package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;

/**
 * The abstract implementation of the RelationshipShapeBuilder. It contains
 * useful primitives for
 * 
 * @author Anton Nazarov
 * @since 18/12/2018
 */
public abstract class RelationshipShapeBuilderImpl implements RelationshipShapeBuilder {

    protected RelationshipPrimitivesBuilder primitivesBuilder = null;
    protected RelationshipTopology relationshipTopology = null;

    public RelationshipShapeBuilderImpl(RelationshipPrimitivesBuilder primitivesBuilder,
            RelationshipTopology relationshipTopology) {
        this.primitivesBuilder = primitivesBuilder;
        this.relationshipTopology = relationshipTopology;
    }

    protected Point2D getParentStart(ApricotRelationship relationship, Side parentSide) {
        Point2D ret = null;

        // check the relationship in the stack
        ApricotEntityShape eShape = (ApricotEntityShape) relationship.getParent().getShape();
        ret = eShape.getStackRelationshipStart(relationship);
        if (ret != null) {
            return ret;
        }

        VBox pBox = (VBox) relationship.getParent().getShape();

        switch (parentSide) {
        case LEFT:
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
            break;
        case RIGHT:
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX() + pBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
            break;
        case TOP:

            break;
        }

        return ret;
    }

    protected Point2D getChildEnd(ApricotRelationship relationship, Side childSide) {
        Point2D ret = null;

        VBox cBox = (VBox) relationship.getChild().getShape();
        
        switch (childSide) {
        case LEFT :
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
            break;
        case RIGHT :
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX() + cBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
            break;
        case TOP :
            
            break;
        }

        return ret;
    }
    
    protected void addStartElement(RelationshipType type, Point2D parentStart, Side parentSide,
            ApricotRelationshipShape shape) {
        if (type == RelationshipType.OPTIONAL_NON_IDENTIFYING) {
            Shape startElement = primitivesBuilder.getOptionalStart();
            
            switch (parentSide) {
            case RIGHT :
                startElement.setLayoutX(parentStart.getX() + 2);
                break;
            case LEFT :
                startElement.setLayoutX(parentStart.getX() - (RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH + 2));
                break;
            case TOP :
                
                break;
            }
            startElement.setLayoutY(parentStart.getY() - RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH / 2);
            shape.setStartElement(startElement);
        }
    }
    
    protected void addEndElement(Point2D childEnd, Side childSide, ApricotRelationshipShape shape) {
        Shape endElement = null;
        
        switch (childSide) {
        case RIGHT :
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() + RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
            break;
        case LEFT :
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() - RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
            break;
        case TOP :
            
            break;
        }
        shape.setEndElement(endElement);
    }
}
