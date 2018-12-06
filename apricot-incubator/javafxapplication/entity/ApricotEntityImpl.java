package javafxapplication.entity;

import java.util.List;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.entity.shape.EntityShapeBuilder;

/**
 * Implementation of the ApricotEntity interface.
 *
 * @author Anton Nazarov
 * @since 28/11/2018
 */
public final class ApricotEntityImpl implements ApricotEntity {

    private final String tableName;
    private final List<FieldDetail> details;
    private final boolean slave;
    private final EntityShapeBuilder shapeBuilder;
    private ElementStatus status = ElementStatus.DEFAULT;
    private ApricotEntityShape entityShape;

    /**
     * Construct a new instance of the ApricotEntity.
     */
    public ApricotEntityImpl(String tableName, List<FieldDetail> details, boolean slave, EntityShapeBuilder shapeBuilder) {
        this.tableName = tableName;
        this.details = details;
        this.slave = slave;
        this.shapeBuilder = shapeBuilder;

        buildShape();
    }

    @Override
    public ApricotEntityShape getEntityShape() {
        return entityShape;
    }

    @Override
    public void setElementStatus(ElementStatus status) {
        this.status = status;
    }

    @Override
    public ElementStatus getElementStatus() {
        return status;
    }

    @Override
    public void buildShape() {
        entityShape = shapeBuilder.build(this);
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public List<FieldDetail> getDetails() {
        return details;
    }

    @Override
    public boolean isSlave() {
        return slave;
    }
}
