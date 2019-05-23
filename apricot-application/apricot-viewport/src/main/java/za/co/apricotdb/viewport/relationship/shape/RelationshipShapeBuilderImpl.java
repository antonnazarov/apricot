package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;
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
    protected ElementVisualModifier[] shapeModifiers = null;

    public RelationshipShapeBuilderImpl(RelationshipPrimitivesBuilder primitivesBuilder,
            RelationshipTopology relationshipTopology, ElementVisualModifier[] shapeModifiers) {
        this.primitivesBuilder = primitivesBuilder;
        this.relationshipTopology = relationshipTopology;
        this.shapeModifiers = shapeModifiers;
    }

    protected Point2D getParentStart(ApricotRelationship relationship, Side parentSide) {
        Point2D ret = null;

        // check the relationship in the stack
        ApricotEntityShape parentEntityShape = relationship.getParent().getEntityShape();
        ret = parentEntityShape.getStackRelationshipStart(relationship);
        if (ret != null) {
            return ret;
        }

        switch (parentSide) {
        case LEFT:
            ret = new Point2D(parentEntityShape.getLayoutX() + parentEntityShape.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
            break;
        case RIGHT:
            ret = new Point2D(
                    parentEntityShape.getLayoutX() + parentEntityShape.getTranslateX() + parentEntityShape.getWidth(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
            break;
        case TOP:
            break;
        default:
            break;
        }

        return ret;
    }

    protected Point2D getChildEnd(ApricotRelationship relationship, Side childSide) {
        Point2D ret = null;

        ApricotEntityShape entityShape = relationship.getChild().getEntityShape();
        ret = entityShape.getStackRelationshipEnd(relationship);
        if (ret != null) {
            return ret;
        }

        switch (childSide) {
        case LEFT:
            ret = new Point2D(entityShape.getLayoutX() + entityShape.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
            break;
        case RIGHT:
            ret = new Point2D(entityShape.getLayoutX() + entityShape.getTranslateX() + entityShape.getWidth(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
            break;
        case TOP:
            ret = entityShape.getStackRelationshipStart(relationship);
            break;
        default:
            break;
        }

        return ret;
    }

    protected void addStartElement(RelationshipType type, Point2D parentStart, Side parentSide,
            ApricotRelationshipShape shape) {
        if (type == RelationshipType.OPTIONAL_NON_IDENTIFYING) {
            Shape startElement = primitivesBuilder.getOptionalStart();

            switch (parentSide) {
            case RIGHT:
                startElement.setLayoutX(parentStart.getX() + 2);
                break;
            case LEFT:
                startElement
                        .setLayoutX(parentStart.getX() - (RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH + 2));
                break;
            case TOP:
                break;
            default:
                break;
            }
            startElement.setLayoutY(parentStart.getY() - RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH / 2);
            shape.setStartElement(startElement);
        }
    }

    protected void addEndElement(Point2D childEnd, Side childSide, ApricotRelationshipShape shape) {
        Shape endElement = null;

        switch (childSide) {
        case RIGHT:
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() + RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
            break;
        case LEFT:
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() - RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
            break;
        case TOP:
            endElement = primitivesBuilder.getEnd(new Point2D(childEnd.getX(),
                    childEnd.getY() - RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER));
            break;
        default:
            break;
        }
        shape.setEndElement(endElement);
    }

    protected void applyModifiers(ApricotRelationshipShape shape) {
        for (ElementVisualModifier modifier : shapeModifiers) {
            modifier.modify(shape);
        }
    }
}
