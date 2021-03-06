package za.co.apricotdb.viewport.entity;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.entity.shape.DetailedEntityShape;
import za.co.apricotdb.viewport.entity.shape.EntityShapeBuilder;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

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
    private List<ApricotRelationship> primaryLinks = new ArrayList<>();
    private List<ApricotRelationship> foreignLinks = new ArrayList<>();
    private ApricotCanvas canvas;

    /**
     * Construct a new instance of the ApricotEntity.
     */
    public ApricotEntityImpl(String tableName, List<FieldDetail> details, boolean slave,
            EntityShapeBuilder shapeBuilder, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.details = details;
        this.slave = slave;
        this.shapeBuilder = shapeBuilder;
        this.canvas = canvas;
    }

    @Override
    public ApricotEntityShape getEntityShape() {
        return entityShape;
    }

    @Override
    public void setElementStatus(ElementStatus status) {
        this.status = status;

        switch (status) {
        case DEFAULT:
            entityShape.setDefault();
            makePrimaryRelationshipsDefault();
            break;
        case SELECTED:
            entityShape.setSelected();
            canvas.sendToFront(this);
            makePrimaryRelationshipsSelected();
            break;
        default:
            break;
        }

        if (status == ElementStatus.DEFAULT) {
            entityShape.setDefault();
        }
    }
    
    private void makePrimaryRelationshipsDefault() {
        if (entityShape instanceof DetailedEntityShape) {
            ((DetailedEntityShape) entityShape).getLeftStack().setDefault();
            ((DetailedEntityShape) entityShape).getRightStack().setDefault();
            ((DetailedEntityShape) entityShape).getTopStack().setDefault();
        }        
    }
    
    private void makePrimaryRelationshipsSelected() {
        for (ApricotRelationship r : primaryLinks) {
            r.setElementStatus(ElementStatus.SELECTED);
        }
        if (entityShape instanceof DetailedEntityShape) {
            ((DetailedEntityShape) entityShape).getLeftStack().setSelected();
            ((DetailedEntityShape) entityShape).getRightStack().setSelected();
            ((DetailedEntityShape) entityShape).getTopStack().setSelected();
        }
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
    public List<ApricotRelationship> getPrimaryLinks() {
        return primaryLinks;
    }

    @Override
    public List<ApricotRelationship> getForeignLinks() {
        return foreignLinks;
    }

    @Override
    public void addLink(ApricotRelationship link, boolean primary) {
        if (primary) {
            primaryLinks.add(link);
        } else {
            foreignLinks.add(link);
        }
    }

    @Override
    public Node getShape() {
        return entityShape.getEntityGroup();
    }

    @Override
    public ElementType getElementType() {
        return ElementType.ENTITY;
    }
}
