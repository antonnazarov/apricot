package za.co.apricotdb.ui.undo;

import java.util.ArrayDeque;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.UndoSnapshotManager;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.toolbar.TbUndoHandler;

/**
 * The essential UNDO logic is represented by this component.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 *
 */
@Component
public class ApricotUndoManager {

    @Autowired
    ParentWindow parent;

    @Autowired
    LayoutUndoManager layoutUndoManager;

    @Autowired
    ObjectUndoManager objectUndoManager;

    @Autowired
    UndoSnapshotManager undoSnapshotManager;
    
    @Autowired
    MainAppController appController;
    
    @Autowired
    TbUndoHandler undoHandler;

    public static final int UNDO_STACK_SIZE = 10;

    /**
     * Perform the UNDO operation.
     */
    @ApricotErrorLogger(title = "Unable to undo the last operation")
    public void undo() {
        if (getUndoBuffer().size() > 0) {
            UndoChunk chunk = getUndoBuffer().removeFirst();
            if (chunk != null) {
                switch (chunk.getUndoType()) {
                case LAYOUT_CHANGED:
                    layoutUndoManager.undo(chunk);
                    break;
                case OBJECT_EDITED:
                    // objectUndoManager.undo(chunk);
                    // break;
                }
            }
        }

        if (getUndoBuffer().size() > 0) {
            enableUndoButton(true, getUndoBuffer().size());
        } else {
            enableUndoButton(false, 0);
        }
    }

    /**
     * Add the savepoint to the undo buffer.
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void addSavepoint(UndoType type) {
        UndoChunk chunk = null;
        switch (type) {
        case LAYOUT_CHANGED:
            chunk = layoutUndoManager.buildChunk();
            if (chunk != null) {
                parent.getApplicationData().saveCurrentLayout((LayoutSavepoint) chunk);
            }
            break;
        case OBJECT_EDITED:
            // chunk = objectUndoManager.buildChunk();
            // getUndoBuffer().addFirst(chunk);
            // break;
        }

        // inform app that there is an undo chunk in the undo buffer
        enableUndoButton(true, getUndoBuffer().size());
    }

    @Transactional
    public void resetUndoBuffer() {
        getUndoBuffer().clear();
        resetCurrentLayout();
        // undoSnapshotManager.cleanUndoProject();
        enableUndoButton(false, 0);
    }

    public void resetCurrentLayout() {
        UndoChunk chunk = layoutUndoManager.buildChunk();
        if (chunk != null) {
            parent.getApplicationData().setCurrentLayout((LayoutSavepoint) chunk);
        }
    }

    private ArrayDeque<UndoChunk> getUndoBuffer() {
        return parent.getApplicationData().getUndoBuffer();
    }

    private void enableUndoButton(boolean enable, int steps) {
        if (enable) {
            undoHandler.enable();
            appController.getMenuUndo().setDisable(false);
        } else {
            undoHandler.disable();
            appController.getMenuUndo().setDisable(true);
        }
    }
}
