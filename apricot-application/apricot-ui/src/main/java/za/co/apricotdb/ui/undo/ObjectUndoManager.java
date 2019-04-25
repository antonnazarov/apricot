package za.co.apricotdb.ui.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

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

    /**
     * Perform the Object- specific undo operation.
     */
    public void undo(UndoChunk chunk) {
        ObjectSavepoint osp = (ObjectSavepoint) chunk;
        
        //  undo the layout first
        LayoutSavepoint lsp = osp.getLayoutSavepoint();
        if (lsp != null) {
            layoutUndoManager.undo(lsp);
        }
        
        //  do object changes undo
        
    }

    public UndoChunk buildChunk() {
        ApricotSnapshot savepointSnapshot = getSavepointSnapshot();
        LayoutSavepoint lSave = (LayoutSavepoint) layoutUndoManager.buildChunk();
        return new ObjectSavepoint(savepointSnapshot, lSave);
    }

    private ApricotSnapshot getSavepointSnapshot() {
        return null;
    }
}
