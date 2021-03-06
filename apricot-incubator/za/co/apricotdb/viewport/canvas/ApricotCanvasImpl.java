package za.co.apricotdb.viewport.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.align.OrderManager;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.DetailedEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopology;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopologyImpl;

/**
 * The basic implementation of the Apricot- canvas.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public class ApricotCanvasImpl extends Pane implements ApricotCanvas {

    private final List<ApricotElement> elements = new ArrayList<>();
    private final Map<String, ApricotEntity> entities = new HashMap<>();
    private final List<ApricotRelationship> relationships = new ArrayList<>();
    private final RelationshipTopology topology = new RelationshipTopologyImpl(this);

    /**
     * Register new Entity Shape into the canvas.
     */
    @Override
    public void addElement(ApricotElement element) {
        elements.add(element);
        if (element.getElementType() == ElementType.ENTITY) {
            ApricotEntity entity = (ApricotEntity) element;
            entities.put(entity.getTableName(), entity);
        } else if (element.getElementType() == ElementType.RELATIONSHIP) {
            ApricotRelationship relationship = (ApricotRelationship) element;
            relationships.add(relationship);
        }

        if (element.getShape() != null) {
            this.getChildren().add(element.getShape());
        }
    }

    @Override
    public void orderElements(OrderManager orderManager) {
        orderManager.order();
    }

    @Override
    public ApricotEntity findEntityByName(String name) {
        return entities.get(name);
    }

    @Override
    public void removeElement(ApricotElement element) {
        elements.remove(element);
        if (element.getElementType() == ElementType.ENTITY) {
            entities.remove(((ApricotEntity) element).getTableName());
        } else {
            relationships.remove(element);
        }
        this.getChildren().remove(element.getShape());
    }

    @Override
    public void sendToFront(ApricotElement element) {
        if (this.getChildren().contains(element.getShape())) {
            this.getChildren().remove(element.getShape());
        }
        this.getChildren().add(element.getShape());
    }

    @Override
    public void changeAllElementsStatus(ElementStatus status) {
        for (ApricotElement e : elements) {
            e.setElementStatus(status);
        }
    }

    @Override
    public List<ApricotElement> getElements() {
        return elements;
    }

    @Override
    public List<Node> getShapes() {
        return this.getChildren();
    }

    @Override
    public Map<String, Bounds> getEntityBounds() {
        Map<String, Bounds> ret = new HashMap<>();

        for (String table : entities.keySet()) {
            ApricotEntity entity = entities.get(table);
            Node n = entity.getEntityShape();
            ret.put(table, n.getBoundsInParent());
        }

        return ret;
    }

    /**
     * Redraw all relationships on the canvas.
     */
    @Override
    public void buildRelationships() {
        for (ApricotElement e : elements) {
            if (e.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) e;
                if (entity.getEntityShape() != null) {
                    if (entity.getEntityShape() instanceof DetailedEntityShape) {
                        DetailedEntityShape entityShape = (DetailedEntityShape) entity.getEntityShape();
                        entityShape.resetAllStacks();
                        entityShape.getEntityGroup().getChildren().remove(entityShape.getLeftStack());
                        entityShape.getEntityGroup().getChildren().remove(entityShape.getRightStack());
                        entityShape.getEntityGroup().getChildren().remove(entityShape.getTopStack());

                        for (ApricotRelationship r : entity.getPrimaryLinks()) {
                            Side side = topology.getRelationshipSide(r, true);
                            switch (side) {
                            case LEFT:
                                entityShape.getLeftStack().addRelationship(r);
                                break;
                            case RIGHT:
                                entityShape.getRightStack().addRelationship(r);
                                break;
                            case TOP:
                                entityShape.getTopStack().addRelationship(r);
                                break;
                            }
                        }

                        // add foreign identifying relationships (if any)
                        for (ApricotRelationship r : entity.getForeignLinks()) {
                            if (r.getRelationshipType() == RelationshipType.IDENTIFYING) {
                                entityShape.getTopStack().addRelationship(r);
                            }
                        }

                        if (entityShape.getLeftStack().hasRelationships()) {
                            entityShape.getLeftStack().build();
                            entityShape.getEntityGroup().getChildren().add(entityShape.getLeftStack());
                        }
                        if (entityShape.getRightStack().hasRelationships()) {
                            entityShape.getRightStack().build();
                            entityShape.getEntityGroup().getChildren().add(entityShape.getRightStack());
                        }
                        if (entityShape.getTopStack().hasRelationships()) {
                            entityShape.getTopStack().build();
                            entityShape.getEntityGroup().getChildren().add(entityShape.getTopStack());
                        }
                    }
                }
            }
        }

        for (ApricotRelationship r : relationships) {
            r.buildShape();
        }
    }

    @Override
    public List<ApricotRelationship> getRelationships() {
        return relationships;
    }
}
