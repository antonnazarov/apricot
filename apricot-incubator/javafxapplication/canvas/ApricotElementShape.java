package javafxapplication.canvas;

/**
 * This interface is general for all graphical (shape) elements on the Apricot Diagram.
 * 
 * @author Anton Nazarov
 * @since 18/11/2018
 */
public interface ApricotElementShape {
    
    void setDefault();
    
    void setSelected();
    
    void setGrayed();
    
    void setHidden();
}
