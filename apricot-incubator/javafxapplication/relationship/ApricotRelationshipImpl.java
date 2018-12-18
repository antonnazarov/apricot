package javafxapplication.relationship;

import javafx.scene.Node;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.canvas.ElementType;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.shape.ApricotRelationshipShape;
import javafxapplication.relationship.shape.RelationshipShapeBuilder;
import javafxapplication.relationship.shape.RelationshipTopology;

/**
 * A basic implementation of the Apricot Relationship.
 * 
 * @author Anton Nazarov
 * @since 18/12/1018
 */
public class ApricotRelationshipImpl implements ApricotRelationship {
    
    private ApricotEntity parent = null;
    private ApricotEntity child = null;
    private String primaryKeyName = null;
    private String foreignKeyName = null;
    private RelationshipType type = null;
    private ApricotRelationshipShape relationshipShape = null;
    private RelationshipTopology topology = null;
    private ApricotCanvas canvas = null;
    private ElementStatus status = null;

    public ApricotRelationshipImpl(ApricotEntity parent, ApricotEntity child,
            String primaryKeyName, String foreignKeyName, RelationshipType type, 
            RelationshipTopology topology, ApricotCanvas canvas) {
        this.parent = parent;
        this.child = child;
        this.primaryKeyName = primaryKeyName;
        this.foreignKeyName = foreignKeyName;
        this.type = type;
        this.canvas = canvas;
        this.topology = topology;
    }
    
    @Override
    public void setElementStatus(ElementStatus status) {
        this.status = status;
    }

    @Override
    public ElementStatus getElementStatus() {
        return status;
    }

    /**
     * Build or re-build the shape if the relationship drawing was affected by some movements of entities in the user interface.  
     */
    @Override
    public void buildShape() {
        RelationshipShapeBuilder shapeBulder = topology.getRelationshipShapeBuilder(this);
        if (relationshipShape != null && relationshipShape.getShapeType() == shapeBulder.getShapeType()) {
            shapeBulder.alterExistingRelationshipShape(this);
        } else if (relationshipShape != null) {
            canvas.getShapes().remove(relationshipShape);
            relationshipShape = shapeBulder.buildRelationshipShape(this);
            canvas.getShapes().add(relationshipShape);
        } else {
            relationshipShape = shapeBulder.buildRelationshipShape(this);
        }
    }

    @Override
    public Node getShape() {
        return relationshipShape;
    }

    @Override
    public ElementType getElementType() {
        return ElementType.RELATIONSHIP;
    }

    @Override
    public ApricotEntity getParent() {
        return parent;
    }

    @Override
    public ApricotEntity getChild() {
        return child;
    }

    @Override
    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    @Override
    public String getForeignKeyName() {
        return foreignKeyName;
    }

    @Override
    public RelationshipType getRelationshipType() {
        return type;
    }
}
