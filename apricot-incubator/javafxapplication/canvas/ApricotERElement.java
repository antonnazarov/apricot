package javafxapplication.canvas;

/**
 * A representation of the ER- element (entity or relationship so far).
 * 
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotERElement {
    
    /**
     * Command the element to become select/unselected.
     */
    void setSelected(boolean selected);

    /**
     * Hide the element.
     */
    void hide();
    
    /**
     * Show the element.
     */
    void show();
    
    /**
     * Set element grayed and back.
     */
    void grayDown(boolean grayed);
}
