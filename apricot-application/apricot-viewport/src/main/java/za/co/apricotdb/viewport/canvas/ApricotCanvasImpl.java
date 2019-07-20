package za.co.apricotdb.viewport.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.align.OrderManager;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.entity.shape.DefaultEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
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
    private final RelationshipTopology topology = new RelationshipTopologyImpl();
    private final ApplicationEventPublisher applicationEventPublisher;
    private String detailLevel;
    private String erdNotation;
    private boolean canvasChanged;
    private ScrollPane scroll;

    public ApricotCanvasImpl(ApplicationEventPublisher applicationEventPublisher, String detailLevel,
            String erdNotation) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.detailLevel = detailLevel;
        this.erdNotation = erdNotation;
    }

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
    public ApricotRelationship findRelationshipByName(String name) {
        for (ApricotRelationship r : relationships) {
            if (r.getRelationshipName().equals(name)) {
                return r;
            }
        }

        return null;
    }

    @Override
    public void removeElement(ApricotElement element) {
        elements.remove(element);
        if (element.getElementType() == ElementType.ENTITY) {
            entities.remove(((ApricotEntity) element).getTableName());
            removeEntityLinkedRelationships((ApricotEntity) element);
        } else {
            relationships.remove(element);
        }
        this.getChildren().remove(element.getShape());
    }

    /**
     * Remove all relationships, linked to the entity.
     */
    private void removeEntityLinkedRelationships(ApricotEntity entity) {
        List<ApricotRelationship> relationships = new ArrayList<>(entity.getPrimaryLinks());
        relationships.addAll(entity.getForeignLinks());

        for (ApricotRelationship r : relationships) {
            r.getParent().getPrimaryLinks().remove(r);
            r.getParent().getForeignLinks().remove(r);
            r.getChild().getPrimaryLinks().remove(r);
            r.getChild().getForeignLinks().remove(r);
            removeElement(r);
        }
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
                    if (entity.getEntityShape() instanceof DefaultEntityShape) {
                        DefaultEntityShape entityShape = (DefaultEntityShape) entity.getEntityShape();
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
                            default:
                                break;
                            }
                        }

                        // the simplified view requires
                        if (detailLevel.equals("SIMPLE")) {
                            for (ApricotRelationship r : entity.getForeignLinks()) {
                                Side side = topology.getRelationshipSide(r, false);
                                switch (side) {
                                case LEFT:
                                    entityShape.getLeftStack().addChildRelationship(r);
                                    break;
                                case RIGHT:
                                    entityShape.getRightStack().addChildRelationship(r);
                                    break;
                                case TOP:
                                    entityShape.getTopStack().addChildRelationship(r);
                                    break;
                                default:
                                    break;
                                }
                            }
                        } else {
                            // check if the normal and extended views have the primary and foreign key
                            // in the same time
                            for (ApricotRelationship r : entity.getForeignLinks()) {
                                if (r.getRelationshipType() == RelationshipType.IDENTIFYING) {
                                    Side side = topology.getRelationshipSide(r, false);
                                    switch (side) {
                                    case LEFT:
                                        if (entityShape.getLeftStack().getPrimaryLinkSize() > 0) {
                                            entityShape.getLeftStack().addChildRelationship(r);
                                        }
                                        break;
                                    case RIGHT:
                                        if (entityShape.getRightStack().getPrimaryLinkSize() > 0) {
                                            entityShape.getRightStack().addChildRelationship(r);
                                        }
                                        break;
                                    case TOP:
                                        break;

                                    default:
                                        break;
                                    }
                                }
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

    /**
     * Create and return the current allocation map.
     */
    @Override
    public CanvasAllocationMap getAllocationMap() {
        CanvasAllocationMap allocationMap = new CanvasAllocationMap();
        for (ApricotElement ae : elements) {
            ApricotShape shape = null;
            if (ae.getElementType() == ElementType.ENTITY) {
                shape = ((ApricotEntity) ae).getEntityShape();
            } else {
                shape = (ApricotShape) ae.getShape();
            }

            if (shape != null) {
                CanvasAllocationItem item = shape.getAllocation();
                allocationMap.addCanvasAllocationItem(item);
            }
        }

        return allocationMap;
    }

    @Override
    public void applyAllocationMap(CanvasAllocationMap map, ElementType elementType) {
        Map<String, ApricotRelationship> relMap = getRelationshipMap();

        for (CanvasAllocationItem item : map.getAllocations()) {
            if (item.getType() == elementType) {
                if (item.getType() == ElementType.ENTITY) {
                    ApricotEntity entity = entities.get(item.getName());
                    if (entity != null) {
                        ApricotEntityShape shape = entity.getEntityShape();
                        shape.applyAllocation(item);
                    }
                } else {
                    ApricotRelationship relationship = relMap.get(item.getName());
                    if (relationship != null) {
                        ApricotRelationshipShape shape = (ApricotRelationshipShape) relationship.getShape();
                        if (shape != null) {
                            shape.applyAllocation(item);
                        }
                    }
                }
            }
        }
    }

    private Map<String, ApricotRelationship> getRelationshipMap() {
        Map<String, ApricotRelationship> map = new HashMap<>();

        for (ApricotRelationship r : relationships) {
            map.put(r.getRelationshipName(), r);
        }

        return map;
    }

    @Override
    public List<ApricotEntity> getSelectedEntities() {
        List<ApricotEntity> ret = new ArrayList<>();

        for (ApricotElement e : elements) {
            if (e.getElementType() == ElementType.ENTITY && e.getElementStatus() == ElementStatus.SELECTED) {
                ret.add((ApricotEntity) e);
            }
        }

        return ret;
    }

    @Override
    public List<ApricotRelationship> getSelectedRelationships() {
        List<ApricotRelationship> ret = new ArrayList<>();

        for (ApricotElement e : elements) {
            if (e.getElementType() == ElementType.RELATIONSHIP && e.getElementStatus() == ElementStatus.SELECTED) {
                ret.add((ApricotRelationship) e);
            }
        }

        return ret;
    }

    @Override
    public void cleanCanvas() {
        getChildren().clear();
        elements.clear();
        entities.clear();
        relationships.clear();
        canvasChanged = false;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public boolean isCanvasChanged() {
        return canvasChanged;
    }

    @Override
    public void setCanvasChanged(boolean canvasChanged) {
        this.canvasChanged = canvasChanged;
    }

    @Override
    public void renameEntity(String oldName, String newName) {
        ApricotEntity entity = findEntityByName(oldName);
        if (entity != null) {
            entities.remove(oldName);
            entity.setTableName(newName);
            entities.put(newName, entity);
        }
    }

    /**
     * Sets the current detail level of the canvas.
     */
    @Override
    public void setDetailLevel(String level) {
        this.detailLevel = level;
    }

    @Override
    public String getErdNotation() {
        return erdNotation;
    }

    @Override
    public void setErdNotation(String erdNotation) {
        this.erdNotation = erdNotation;
    }

    @Override
    public void setScrollPane(ScrollPane scroll) {
        this.scroll = scroll;

    }

    @Override
    public ScrollPane getScrollPane() {
        return scroll;
    }
}
