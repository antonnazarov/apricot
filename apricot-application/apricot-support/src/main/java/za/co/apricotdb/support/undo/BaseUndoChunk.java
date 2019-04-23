package za.co.apricotdb.support.undo;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import za.co.apricotdb.viewport.canvas.ApricotElement;


/**
 * This abstract UndoChunk contains data common for layout and objects.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public abstract class BaseUndoChunk implements UndoChunk {

    private static final long serialVersionUID = -5207654283644415974L;
    
    private Point2D screenPosition;
    private List<ApricotElement> involvedElements;
    private Tab currentTab;
    
    @Override
    public void setScreenPosition(Point2D screenPosition) {
        this.screenPosition = screenPosition;
    }

    @Override
    public Point2D getScreenPosition() {
        return screenPosition;
    }

    @Override
    public List<ApricotElement> getInvolvedElements() {
        return involvedElements;
    }

    @Override
    public void setInvolvedElements(List<ApricotElement> involvedElements) {
        this.involvedElements = involvedElements;
    }

    @Override
    public Tab getCurrentTab() {
        return currentTab;
    }

    @Override
    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }
}
