package za.co.apricotdb.ui.undo;

import java.util.List;

import javafx.geometry.Point2D;

/**
 * This abstract UndoChunk contains data common for layout and objects.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public abstract class BaseUndoChunk implements UndoChunk {

    private static final long serialVersionUID = -5207654283644415974L;

    private Point2D screenPosition;
    private List<String> involvedElements;
    private String currentTabName;

    public BaseUndoChunk(Point2D screenPosition, List<String> elements, String currentTabName) {
        setScreenPosition(screenPosition);
        setInvolvedElements(elements);
        setCurrentTabName(currentTabName);
    }

    @Override
    public void setScreenPosition(Point2D screenPosition) {
        this.screenPosition = screenPosition;
    }

    @Override
    public Point2D getScreenPosition() {
        return screenPosition;
    }

    @Override
    public List<String> getInvolvedElements() {
        return involvedElements;
    }

    @Override
    public void setInvolvedElements(List<String> involvedElements) {
        this.involvedElements = involvedElements;
    }

    @Override
    public String getCurrentTabName() {
        return currentTabName;
    }

    @Override
    public void setCurrentTabName(String currentTabName) {
        this.currentTabName = currentTabName;
    }
}
