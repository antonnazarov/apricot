package za.co.apricotdb.ui.undo;

import java.util.List;

import javafx.geometry.Point2D;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The undo information storage specific for the changes in the objects
 * (Entities and Relationships).
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class ObjectSavepoint extends BaseUndoChunk {

    private static final long serialVersionUID = -8403595923151574710L;

    private ApricotSnapshot savepointSnapshot;

    public ObjectSavepoint(Point2D screenPosition, List<String> elements, String currentTabName,
            ApricotSnapshot savepointSnapshot) {
        super(screenPosition, elements, currentTabName);
        this.savepointSnapshot = savepointSnapshot;
    }

    @Override
    public UndoType getUndoType() {
        return UndoType.OBJECT_EDITED;
    }

    public ApricotSnapshot getSavepointSnapshot() {
        return savepointSnapshot;
    }

    public void setSavepointSnapshot(ApricotSnapshot savepointSnapshot) {
        this.savepointSnapshot = savepointSnapshot;
    }
}
