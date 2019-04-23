package za.co.apricotdb.ui.undo;

import java.util.Deque;

public class DequeTest {
    
    public static final void main(String[] args) {
        DequeTest test = new DequeTest();
        
        test.testDeque();
    }
    
    void testDeque() {
        Deque<String> stack = new CircularUndoStack<>(5);
        
        stack.addFirst("one");
        stack.addFirst("obj2");
        stack.addFirst("obj3");
        stack.addFirst("obj4");
        stack.addFirst("obj5");
        stack.addFirst("obj6");
        stack.addFirst("obj7");
        stack.addFirst("obj8");
        System.out.println(stack);
        
        System.out.println(stack.removeFirst());
        System.out.println(stack);
        
        stack.addFirst("obj9");
        stack.addFirst("obj10");
        System.out.println(stack);
    }
}
