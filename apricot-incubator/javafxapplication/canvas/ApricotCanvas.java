package javafxapplication.canvas;

import java.util.List;
import java.util.Map;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafxapplication.align.OrderManager;
import javafxapplication.entity.ApricotEntity;

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
}
