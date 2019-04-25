package za.co.apricotdb.ui.undo;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;

/**
 * The manager of the undo operations on the layout undo type.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
@Component
public class LayoutUndoManager {

    /**
     * Perform the Layout- specific undo operation.
     */
    public void undo(UndoChunk chunk) {

    }

    public UndoChunk buildChunk(Point2D screenPosition, List<String> elements, String currentTabName) {
        CanvasAllocationMap map = getAllocationMap();
        return new LayoutSavepoint(screenPosition, elements, currentTabName, map);
    }

    private CanvasAllocationMap getAllocationMap() {
        return null;
    }
}
