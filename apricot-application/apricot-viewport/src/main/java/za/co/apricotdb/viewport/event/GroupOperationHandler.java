package za.co.apricotdb.viewport.event;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.entity.shape.DetailedEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;

public class GroupOperationHandler {

    public void setEntityTranslatePosition(ApricotCanvas canvas, double translateX, double translateY,
            ElementStatus elementStatus) {
        translateRelationshipRulers(canvas, translateX, translateY, elementStatus);

        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY && element.getElementStatus() == elementStatus) {
                ApricotEntity entity = (ApricotEntity) element;
                ApricotEntityShape entityShape = entity.getEntityShape();
                entityShape.setTranslateX(translateX);
                entityShape.setTranslateY(translateY);
            }
        }
    }

    /**
     * Translate the rulers of the relationships with the given status of element.
     * If the parameter elementStatus is null, the operation will be applied for all
     * entities/relationships.
     */
    public void translateRelationshipRulers(ApricotCanvas canvas, double translateX, double translateY,
            ElementStatus elementStatus) {
        // prepare a list of the selected Entities
        List<ApricotEntity> entities = new ArrayList<>();
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY
                    && (element.getElementStatus() == elementStatus || elementStatus == null)) {
                entities.add((ApricotEntity) element);
            }
        }

        for (ApricotEntity e : entities) {
            List<ApricotRelationship> primaryLinks = e.getPrimaryLinks();
            for (ApricotRelationship r : primaryLinks) {
                ApricotEntity child = r.getChild();
                if (entities.contains(child) && r.getShape() != null) {
                    ApricotRelationshipShape rShape = (ApricotRelationshipShape) r.getShape();
                    // this operation allows to keep topology of the relationship when both sides of
                    // the relationship have being moved simultaneously
                    rShape.translateRelationshipRulers(translateX, translateY);
                }
            }
        }
    }

    /**
     * This method returns true if there were non zero translations found for the
     * handled objects.
     */
    public boolean applyCurrentPosition(ApricotCanvas canvas, ElementStatus elementStatus) {
        boolean ret = false;
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY && element.getElementStatus() == elementStatus) {
                ApricotEntity entity = (ApricotEntity) element;
                ApricotEntityShape shape = entity.getEntityShape();
                if (shape.getTranslateX() != 0 || shape.getTranslateY() != 0) {
                    ret = true;
                }
                shape.setLayoutX(shape.getLayoutX() + shape.getTranslateX());
                shape.setLayoutY(shape.getLayoutY() + shape.getTranslateY());
                shape.setTranslateX(0);
                shape.setTranslateY(0);

                resetRelationshipRulers(entity);
                applyPrimaryKeyStacks(entity);
            }
        }

        return ret;
    }

    private void resetRelationshipRulers(ApricotEntity entity) {
        for (ApricotRelationship r : entity.getPrimaryLinks()) {
            if (r.getShape() != null && r.getShape() instanceof ApricotRelationshipShape) {
                ApricotRelationshipShape rShape = (ApricotRelationshipShape) r.getShape();
                rShape.resetRelationshipRulers();
            }
        }
    }

    public void resetRelationshipRulers(ApricotCanvas canvas) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY) {
                resetRelationshipRulers((ApricotEntity) element);
            }
        }
    }

    /**
     * Rebuild all relationships related to the given entity.
     */
    public void rebuildRelationships(ApricotEntity entity) {
        List<ApricotRelationship> relationships = new ArrayList<>();
        relationships.addAll(entity.getPrimaryLinks());
        relationships.addAll(entity.getForeignLinks());

        for (ApricotRelationship r : relationships) {
            r.buildShape();
        }
    }

    /**
     * Translate all the stacks associated with the given entity.
     */
    public void translatePrimaryKeyStacks(ApricotEntity entity, double translateX, double translateY) {
        ApricotEntityShape s = entity.getEntityShape();
        if (s instanceof DetailedEntityShape) {
            DetailedEntityShape entityShape = (DetailedEntityShape) s;
            entityShape.getLeftStack().translateStack(translateX, translateY);
            entityShape.getRightStack().translateStack(translateX, translateY);
            entityShape.getTopStack().translateStack(translateX, translateY);
        }
    }

    private void applyPrimaryKeyStacks(ApricotEntity entity) {
        ApricotEntityShape s = entity.getEntityShape();
        if (s instanceof DetailedEntityShape) {
            DetailedEntityShape entityShape = (DetailedEntityShape) s;
            entityShape.getLeftStack().applyStackPosition();
            entityShape.getRightStack().applyStackPosition();
            entityShape.getTopStack().applyStackPosition();
        }
    }
}
