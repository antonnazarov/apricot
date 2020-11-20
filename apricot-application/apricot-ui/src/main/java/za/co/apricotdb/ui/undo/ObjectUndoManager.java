package za.co.apricotdb.ui.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.UndoSnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;

/**
 * The manager of the undo operations on the object undo type.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
@Component
public class ObjectUndoManager {

    @Autowired
    LayoutUndoManager layoutUndoManager;

    @Autowired
    UndoSnapshotManager undoManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    /**
     * Perform the Object- specific undo operation.
     */
    public void undo(UndoChunk chunk) {
        ObjectSavepoint osp = (ObjectSavepoint) chunk;

        // do object changes undo
        undoManager.undoCurrentSnapshot(osp.getSavepointSnapshot());
        snapshotHandler.synchronizeSnapshot(true);

        // undo the layout
        LayoutSavepoint lsp = osp.getLayoutSavepoint();
        if (lsp != null) {
            layoutUndoManager.undo(lsp);
        }
    }

    public UndoChunk buildChunk() {
        ApricotSnapshot savepointSnapshot = undoManager.buildUndoSnapshot(snapshotManager.getDefaultSnapshot());
        LayoutSavepoint lSave = (LayoutSavepoint) layoutUndoManager.buildChunk();

        return new ObjectSavepoint(savepointSnapshot, lSave);
    }
}
