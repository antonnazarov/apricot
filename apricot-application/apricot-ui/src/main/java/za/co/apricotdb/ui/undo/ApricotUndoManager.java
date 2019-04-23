package za.co.apricotdb.ui.undo;

import java.util.ArrayDeque;

import org.springframework.stereotype.Component;

/**
 * The essential UNDO logic is represented by this component.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 *
 */
@Component
public class ApricotUndoManager {
    
    public static final int UNDO_STACK_SIZE = 10;

    public void undo(ArrayDeque<UndoChunk> undoBuffer) {
        UndoChunk chunk = undoBuffer.removeFirst();
    }
    
    public void resetUndoBuffer(ArrayDeque<UndoChunk> undoBuffer) {
        undoBuffer.clear();
    }
}
