package javafxapplication.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafxapplication.align.OrderManager;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.ApricotEntityLink;

/**
 * The basic implementation of the Apricot- canvas.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public class ApricotBasicCanvas extends Pane implements ApricotEntityRelationshipCanvas {

    private final List<ApricotERElement> elements = new ArrayList<>();
    private final Map<String, ApricotEntity> entities = new HashMap<>();
    private final List<ApricotEntityLink> links = new ArrayList<>();

    /**
     * Register new Entity Shape into the canvas.
     */
    @Override   
    public void addElement(ApricotERElement element) {
        elements.add(element);
        if (element.getElementType() == ElementType.ENTITY) {
            ApricotEntity entity = (ApricotEntity) element;
            entities.put(entity.getTableName(), entity);
        } else if (element.getElementType() == ElementType.LINK) {
            ApricotEntityLink link = (ApricotEntityLink) element;
            links.add(link);
        }
        
        this.getChildren().add(element.getShape());
    }

    @Override
    public void orderElements(OrderManager orderManager) {
        orderManager.order(this);
    }

    @Override
    public ApricotEntity findEntityByName(String name) {
        return entities.get(name);
    }

    @Override
    public void removeElement(ApricotERElement element) {
        elements.remove(element);
        if (element.getElementType() == ElementType.ENTITY) {
            entities.remove(((ApricotEntity)element).getTableName());
        }
        this.getChildren().remove(element.getShape());
    }

    @Override
    public void sendToFront(ApricotEntity entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void changeAllElementsStatus(ElementStatus status) {
        // TODO Auto-generated method stub
        
    }
}
