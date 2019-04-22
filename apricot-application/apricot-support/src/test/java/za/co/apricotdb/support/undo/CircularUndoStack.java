package za.co.apricotdb.support.undo;

import java.util.ArrayDeque;

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
