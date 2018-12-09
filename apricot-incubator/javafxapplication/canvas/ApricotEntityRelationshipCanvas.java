package javafxapplication.canvas;

import javafxapplication.align.OrderManager;
import javafxapplication.entity.ApricotEntity;

/**
 * The canvas on which the ER- diagram will be drawn.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotEntityRelationshipCanvas {

    void addElement(ApricotERElement element);
    
    void removeElement(ApricotERElement element);    

    void orderElements(OrderManager orderManager);

    ApricotEntity findEntityByName(String name);
    
    void sendToFront(ApricotEntity entity);
    
    void changeAllElementsStatus(ElementStatus status);
}
