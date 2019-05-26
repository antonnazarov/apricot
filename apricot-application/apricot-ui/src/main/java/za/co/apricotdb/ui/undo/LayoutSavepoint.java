package za.co.apricotdb.ui.undo;

import java.util.List;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;

/**
 * The undo information storage specific for the changes in layout.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class LayoutSavepoint implements UndoChunk {

    private static final long serialVersionUID = -2677861347453699229L;

    private Point2D screenPosition;
    private List<String> involvedElements;
    private String currentTabName;
    private CanvasAllocationMap currentAllocationMap;

    public LayoutSavepoint(Point2D screenPosition, List<String> elements, String currentTabName,
            CanvasAllocationMap allocationMap) {
        this.screenPosition = screenPosition;
        this.involvedElements = elements;
        this.currentTabName = currentTabName;
        this.currentAllocationMap = allocationMap;
    }

    @Override
    public UndoType getUndoType() {
        return UndoType.LAYOUT_CHANGED;
    }

    public CanvasAllocationMap getCurrentAllocationMap() {
        return currentAllocationMap;
    }

    public Point2D getScreenPosition() {
        return screenPosition;
    }
    
    public void setScreenPosition(Point2D screenPosition) {
        this.screenPosition = screenPosition;
    }

    public List<String> getInvolvedElements() {
        return involvedElements;
    }
    
    public void setInvolvedElements(List<String> elements) {
        this.involvedElements = elements;
    }

    public String getCurrentTabName() {
        return currentTabName;
    }
    
    public void setCurrentTabName(String tabName) {
        this.currentTabName = tabName;
    }
}
