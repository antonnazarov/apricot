package za.co.apricotdb.ui;

import java.util.ArrayDeque;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.support.undo.ApricotUndoManager;
import za.co.apricotdb.support.undo.CircularUndoStack;
import za.co.apricotdb.support.undo.UndoChunk;
import za.co.apricotdb.support.undo.UndoLayoutChange;

/**
 * This Java- bean contains the overall application data.
 * 
 * @author Anton Nazarov
 * @since 05/02/2019
 */
public class ApplicationData {

    private ApricotProject currentProject;
    private boolean emptyEnv;
    private ArrayDeque<UndoChunk> undoBuffer = new CircularUndoStack<>(ApricotUndoManager.UNDO_STACK_SIZE);
    private UndoLayoutChange currentLayout; 

    public ApricotProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(ApricotProject currentProject) {
        this.currentProject = currentProject;
    }

    public boolean isEmptyEnv() {
        return emptyEnv;
    }

    public void setEmptyEnv(boolean emptyEnv) {
        this.emptyEnv = emptyEnv;
    }

    public ArrayDeque<UndoChunk> getUndoBuffer() {
        return undoBuffer;
    }
    
    public void setCurrentLayout(UndoLayoutChange currentLayout) {
        if (this.currentLayout != null) {
            undoBuffer.addFirst(currentLayout);
            this.currentLayout = currentLayout;
        }
    }
}
