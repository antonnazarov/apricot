package za.co.apricotdb.ui.undo;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The undo information storage specific for the changes in the objects
 * (Entities and Relationships).
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class ObjectSavepoint implements UndoChunk {

    private static final long serialVersionUID = -8403595923151574710L;

    private ApricotSnapshot savepointSnapshot;
    private LayoutSavepoint layoutSavepoint;

    public ObjectSavepoint(ApricotSnapshot savepointSnapshot, LayoutSavepoint layoutSavepoint) {
        this.savepointSnapshot = savepointSnapshot;
        this.layoutSavepoint = layoutSavepoint;
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

    public LayoutSavepoint getLayoutSavepoint() {
        return layoutSavepoint;
    }

    public void setLayoutSavepoint(LayoutSavepoint layoutSavepoint) {
        this.layoutSavepoint = layoutSavepoint;
    }
}
