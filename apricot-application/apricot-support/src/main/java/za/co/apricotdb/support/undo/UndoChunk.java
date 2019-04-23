package za.co.apricotdb.support.undo;

import java.io.Serializable;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import za.co.apricotdb.viewport.canvas.ApricotElement;

/**
 * An atomic data storage for undo- command.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public interface UndoChunk extends Serializable {

    UndoType getUndoType();

    void setScreenPosition(Point2D screenPosition);

    Point2D getScreenPosition();
    
    List<ApricotElement> getInvolvedElements();
    
    void setInvolvedElements(List<ApricotElement> involvedElements);
    
    Tab getCurrentTab();
    
    void setCurrentTab(Tab tab);
}
