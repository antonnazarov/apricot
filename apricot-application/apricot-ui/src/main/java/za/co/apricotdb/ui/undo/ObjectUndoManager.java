package za.co.apricotdb.ui.undo;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The manager of the undo operations on the object undo type.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
@Component
public class ObjectUndoManager {

    /**
     * Perform the Object- specific undo operation.
     */
    public void undo(UndoChunk chunk) {

    }

    public UndoChunk buildChunk(Point2D screenPosition, List<String> elements, String currentTabName) {
        ApricotSnapshot savepointSnapshot = getSavepointSnapshot();
        return new ObjectSavepoint(screenPosition, elements, currentTabName, savepointSnapshot);
    }

    private ApricotSnapshot getSavepointSnapshot() {
        return null;
    }
}
