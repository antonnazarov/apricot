package javafxapplication.align;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ApricotShape;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.canvas.ElementType;

/**
 * This order manager allocates all elements in the pane in the following
 * sequence: 1) selected entities; 2) selected links; 3) other links; 4) other
 * entities
 * 
 * @author Anton Nazarov
 * @since 11/12/2018
 *
 */
public class SelectedOnTopOrderManager implements OrderManager {

    @Override
    public void order(ApricotCanvas canvas) {
        List<Node> result = new ArrayList<>();

        List<Node> selectedEntities = new ArrayList<>();
        List<Node> selectedLinks = new ArrayList<>();
        List<Node> otherLinks = new ArrayList<>();
        List<Node> otherEntities = new ArrayList<>();

        for (Node n : canvas.getShapes()) {
            if (n instanceof ApricotShape) {
                ApricotShape shape = (ApricotShape) n;
                ApricotElement element = shape.getElement();
                if (element.getElementType() == ElementType.ENTITY
                        && element.getElementStatus() == ElementStatus.SELECTED) {
                    selectedEntities.add(n);
                } else if (element.getElementType() == ElementType.LINK
                        && element.getElementStatus() == ElementStatus.SELECTED) {
                    selectedLinks.add(n);
                } else if (element.getElementType() == ElementType.LINK
                        && element.getElementStatus() != ElementStatus.SELECTED) {
                    otherLinks.add(n);
                } else if (element.getElementType() == ElementType.ENTITY
                        && element.getElementStatus() != ElementStatus.SELECTED) {
                    otherEntities.add(n);
                }
            }
        }

        result.addAll(otherEntities);
        result.addAll(otherLinks);
        result.addAll(selectedLinks);
        result.addAll(selectedEntities);

        List<Node> nodes = canvas.getShapes();
        nodes.clear();
        nodes.addAll(result);
    }
}
