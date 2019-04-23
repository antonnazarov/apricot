package za.co.apricotdb.ui.undo;

/**
 * The undo information storage specific for the changes in objects.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class UndoObjectChange extends BaseUndoChunk {

    private static final long serialVersionUID = -8403595923151574710L;

    @Override
    public UndoType getUndoType() {
        return UndoType.OBJECT_EDITED;
    }
}
