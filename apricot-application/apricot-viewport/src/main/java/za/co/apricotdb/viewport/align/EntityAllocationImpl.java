package za.co.apricotdb.viewport.align;

import java.util.List;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * Implementation of the EntityAllocation interface.
 * 
 * @author Anton Nazarov
 * @since 01/07/2019
 */
public class EntityAllocationImpl implements EntityAllocation {

    private ApricotEntity entity;
    private Point2D layout;
    private double width;

    public EntityAllocationImpl(ApricotEntity entity) {
        this.entity = entity;
    }

    public ApricotEntity getApricotEntity() {
        return entity;
    }

    @Override
    public ApricotEntityShape getEntityShape() {
        return entity.getEntityShape();
    }

    @Override
    public String getTableName() {
        return entity.getTableName();
    }

    @Override
    public List<FieldDetail> getDetails() {
        return entity.getDetails();
    }

    @Override
    public List<ApricotRelationship> getPrimaryLinks() {
        return entity.getPrimaryLinks();
    }

    @Override
    public List<ApricotRelationship> getForeignLinks() {
        return entity.getForeignLinks();
    }

    @Override
    public void setLayout(double x, double y) {
        layout = new Point2D(x, y);
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public Point2D getLayout() {
        return layout;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        if (entity.getEntityShape() != null) {
            return entity.getEntityShape().getHeight();
        }
        
        return 0;
    }

    @Override
    public String toString() {
        return entity.toString();
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        EntityAllocationImpl other = (EntityAllocationImpl) obj;

        return other.getApricotEntity().equals(entity);
    }
}
