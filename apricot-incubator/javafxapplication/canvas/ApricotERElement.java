package javafxapplication.canvas;

import javafx.scene.Node;

/**
 * A representation of the ER- element (entity or relationship so far).
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotERElement {

    void setElementStatus(ElementStatus status);

    ElementStatus getElementStatus();
    
    void buildShape();
    
    Node getShape();
    
    ElementType getElementType();
}
