package za.co.apricotdb.viewport.canvas;

import java.util.List;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import za.co.apricotdb.viewport.align.OrderManager;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The canvas on which the ER- diagram will be drawn.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotCanvas {

    void addElement(ApricotElement element);

    void removeElement(ApricotElement element);

    void orderElements(OrderManager orderManager);

    ApricotEntity findEntityByName(String name);

    void sendToFront(ApricotElement entity);

    void changeAllElementsStatus(ElementStatus status);

    List<ApricotElement> getElements();

    List<Node> getShapes();
    
    Map<String, Bounds> getEntityBounds();
    
    void redrawRelationships();
}
