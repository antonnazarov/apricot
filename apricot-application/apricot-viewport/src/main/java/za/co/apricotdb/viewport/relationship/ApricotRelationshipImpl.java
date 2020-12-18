package za.co.apricotdb.viewport.relationship;

import javafx.scene.Node;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeBuilder;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeType;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopology;

/**
 * A basic implementation of the Apricot Relationship.
 * 
 * @author Anton Nazarov
 * @since 18/12/1018
 */
public class ApricotRelationshipImpl implements ApricotRelationship {

    private ApricotEntity parent;
    private ApricotEntity child;
    private String primaryKeyName;
    private String foreignKeyName;
    private long relationshipId;
    private RelationshipType type;
    private ApricotRelationshipShape relationshipShape;
    private RelationshipTopology topology;
    private ApricotCanvas canvas;
    private ElementStatus status = null;
    private boolean valid;

    public ApricotRelationshipImpl(ApricotEntity parent, ApricotEntity child, String primaryKeyName,
            String foreignKeyName, long relationshipId, RelationshipType type, RelationshipTopology topology,
            ApricotCanvas canvas) {
        this.parent = parent;
        this.child = child;
        this.primaryKeyName = primaryKeyName;
        this.foreignKeyName = foreignKeyName;
        this.relationshipId = relationshipId;
        this.type = type;
        this.canvas = canvas;
        this.topology = topology;
    }

    @Override
    public void setElementStatus(ElementStatus status) {
        if (this.status != status) {
            this.status = status;
            if (relationshipShape != null) {
                setShapeStyle();
                canvas.sendToFront(this);
            }
        }
    }

    @Override
    public ElementStatus getElementStatus() {
        return status;
    }

    /**
     * Build or re-build the shape if the relationship drawing was affected by some
     * movements of entities in the user interface.
     */
    @Override
    public void buildShape() {
        RelationshipShapeBuilder shapeBulder = topology.getRelationshipShapeBuilder(this);
        if (relationshipShape != null && relationshipShape.getShapeType() == shapeBulder.getShapeType()) {
            shapeBulder.alterExistingRelationshipShape(this);
        } else if (relationshipShape != null && relationshipShape.getShapeType() != shapeBulder.getShapeType()) {
            canvas.getShapes().remove(relationshipShape);
            relationshipShape = shapeBulder.buildRelationshipShape(this);
            if (relationshipShape != null) {
                canvas.getShapes().add(relationshipShape);
            }
        } else if (relationshipShape == null) {
            relationshipShape = shapeBulder.buildRelationshipShape(this);
            if (relationshipShape != null) {
                canvas.getShapes().add(relationshipShape);
            }
        }

        setShapeStyle();
    }

    private void setShapeStyle() {
        if (relationshipShape != null) {
            switch (status) {
            case DEFAULT:
                relationshipShape.setDefault();
                break;
            case SELECTED:
                relationshipShape.setSelected();
                break;
            case GRAYED:
                relationshipShape.setGrayed();
                break;
            case HIDDEN:
                relationshipShape.setHidden();
                break;
            }
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
    public long getRelationshipId() {
        return relationshipId;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public RelationshipType getRelationshipType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ApricotRelationship: ");
        sb.append(parent.getTableName()).append(" (").append(primaryKeyName).append("), ").append(child.getTableName())
                .append(" (").append(foreignKeyName).append(")");
        return sb.toString();
    }

    @Override
    public RelationshipShapeType getRelationshipShapeType() {
        return topology.calculateRelationshipShapeType(this);
    }

    @Override
    public String getRelationshipName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getParent().getTableName()).append("|");
        sb.append(getChild().getTableName()).append("|");
        sb.append(getPrimaryKeyName()).append("|");
        sb.append(getForeignKeyName()).append("|");

        return sb.toString();
    }

    @Override
    public void resetShape() {
        relationshipShape = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRelationshipName() == null) ? 0 : getRelationshipName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApricotRelationshipImpl other = (ApricotRelationshipImpl) obj;
        return getRelationshipName().equals(other.getRelationshipName());
    }
}
