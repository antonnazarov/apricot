package za.co.apricotdb.ui.undo;

import java.util.ArrayDeque;

/**
 * Implementation of the circular (limited in deep) UNDO stack.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
public class CircularUndoStack<E> extends ArrayDeque<E> {

    private static final long serialVersionUID = 3935068723945783407L;

    private int undoStackSize;

    public CircularUndoStack(int undoStackSize) {
        super(undoStackSize);
        this.undoStackSize = undoStackSize;
    }

    @Override
    public void addFirst(E e) {
        super.addFirst(e);

        while (size() > undoStackSize) {
            removeLast();
        }
    }
}
