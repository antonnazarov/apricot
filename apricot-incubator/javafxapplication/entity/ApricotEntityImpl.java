package javafxapplication.entity;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.canvas.ElementType;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.entity.shape.EntityShapeBuilder;
import javafxapplication.relationship.ApricotEntityLink;

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
    private List<ApricotEntityLink> primaryLinks = new ArrayList<>();
    private List<ApricotEntityLink> foreignLinks = new ArrayList<>();

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

    @Override
    public List<ApricotEntityLink> getPrimaryLinks() {
        return primaryLinks;
    }

    @Override
    public List<ApricotEntityLink> getForeignLinks() {
        return foreignLinks;
    }

    @Override
    public void addLink(ApricotEntityLink link, boolean primary) {
        if (primary) {
            primaryLinks.add(link);
        } else {
            foreignLinks.add(link);
        }
    }

    @Override
    public Node getShape() {
        return entityShape;
    }

    @Override
    public ElementType getElementType() {
        return ElementType.ENTITY;
    }
}
