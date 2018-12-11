package javafxapplication.modifiers;

import javafxapplication.canvas.ApricotShape;

/**
 * The visual modifiers behaves as a "Command"- design pattern and change the
 * current appearance of the element.
 * 
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ElementVisualModifier {

    void modify(ApricotShape shape);
}
