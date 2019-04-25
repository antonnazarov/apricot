package za.co.apricotdb.ui.undo;

import java.io.Serializable;
import java.util.List;

import javafx.geometry.Point2D;

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
    
    List<String> getInvolvedElements();
    
    void setInvolvedElements(List<String> involvedElements);
    
    String getCurrentTabName();
    
    void setCurrentTabName(String tab);
}
