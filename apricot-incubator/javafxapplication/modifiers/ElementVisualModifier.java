package javafxapplication.modifiers;

import javafxapplication.canvas.ApricotERElement;

/**
 * The visual modifiers behaves as a "Command"- design pattern and change the current appearance of the element.
 * 
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ElementVisualModifier {
    
    void modify(ApricotERElement element);
}
