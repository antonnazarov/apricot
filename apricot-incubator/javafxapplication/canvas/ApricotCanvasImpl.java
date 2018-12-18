package javafxapplication.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafxapplication.align.OrderManager;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.ApricotRelationship;

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

        this.getChildren().add(element.getShape());
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
            Node n = entity.getShape();
            ret.put(table, n.getBoundsInParent());
        }
        
        return ret;
    }
}
