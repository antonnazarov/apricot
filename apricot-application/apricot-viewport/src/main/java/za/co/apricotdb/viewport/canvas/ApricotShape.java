package za.co.apricotdb.viewport.canvas;

/**
 * This interface is general for all graphical (shape) elements on the Apricot
 * Diagram.
 * 
 * @author Anton Nazarov
 * @since 18/11/2018
 */
public interface ApricotShape {

    void setDefault();

    void setSelected();

    void setGrayed();

    void setHidden();

    ApricotElement getElement();
    
    CanvasAllocationItem getAllocation();
    
    void applyAllocation(CanvasAllocationItem item);
}
