package za.co.apricotdb.support.undo;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;

/**
 * The undo information storage specific for the changes in layout.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class UndoLayoutChange extends BaseUndoChunk {

    private static final long serialVersionUID = -2677861347453699229L;

    private CanvasAllocationMap currentAllocationMap;

    public UndoLayoutChange(Point2D screenPosition, List<ApricotElement> elements, Tab currentTab,
            CanvasAllocationMap allocationMap) {
        setScreenPosition(screenPosition);
        setInvolvedElements(elements);
        setCurrentTab(currentTab);
        this.currentAllocationMap = allocationMap;
    }

    @Override
    public UndoType getUndoType() {
        return UndoType.LAYOUT_CHANGED;
    }

    public CanvasAllocationMap getCurrentAllocationMap() {
        return currentAllocationMap;
    }

    public void setCurrentAllocationMap(CanvasAllocationMap currentAllocationMap) {
        this.currentAllocationMap = currentAllocationMap;
    }
}
