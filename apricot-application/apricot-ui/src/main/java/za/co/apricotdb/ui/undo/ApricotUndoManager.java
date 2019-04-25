package za.co.apricotdb.ui.undo;

import java.util.ArrayDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;

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
    MainAppController appController;

    public static final int UNDO_STACK_SIZE = 10;

    /**
     * Perform the UNDO operation.
     */
    public void undo() {
        UndoChunk chunk = getUndoBuffer().removeFirst();
        if (chunk != null) {
            switch (chunk.getUndoType()) {
            case LAYOUT_CHANGED:
                layoutUndoManager.undo(chunk);
                break;
            case OBJECT_EDITED:
                objectUndoManager.undo(chunk);
                break;
            }
        }

        if (getUndoBuffer().size() == 0) {
            // there is no undo chunks anymore
            enableUndoButton(false);
        }
    }

    /**
     * Add the savepoint to the undo buffer.
     */
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
            chunk = objectUndoManager.buildChunk();
            getUndoBuffer().addFirst(chunk);
            break;
        }

        // inform app that there is an undo chunk in the undo buffer
        enableUndoButton(true);
    }

    public void resetUndoBuffer() {
        getUndoBuffer().clear();
        addSavepoint(UndoType.LAYOUT_CHANGED);
        enableUndoButton(false);
    }

    private ArrayDeque<UndoChunk> getUndoBuffer() {
        return parent.getApplicationData().getUndoBuffer();
    }
    
    private void enableUndoButton(boolean enable) {
        Button undoBttn = appController.getUndoButton();
        if (enable) {
            undoBttn.setDisable(false);
            appController.getSaveButton().setStyle("-fx-font-weight: bold;");
        } else {
            undoBttn.setDisable(true);
            appController.getSaveButton().setStyle("-fx-font-weight: normal;");
        }
    }
}
