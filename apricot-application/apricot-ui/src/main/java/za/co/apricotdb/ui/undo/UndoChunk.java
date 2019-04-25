package za.co.apricotdb.ui.undo;

import java.io.Serializable;

/**
 * An atomic data storage for undo- command.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public interface UndoChunk extends Serializable {

    UndoType getUndoType();
}
