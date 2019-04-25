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
public class LayoutSavepoint extends BaseUndoChunk {

    private static final long serialVersionUID = -2677861347453699229L;

    private CanvasAllocationMap currentAllocationMap;

    public LayoutSavepoint(Point2D screenPosition, List<String> elements, String currentTabName,
            CanvasAllocationMap allocationMap) {
        super(screenPosition, elements, currentTabName);
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
