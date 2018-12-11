package javafxapplication.modifiers;

import javafxapplication.canvas.ApricotShape;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.event.EntityOnMousePressedEventHandler;

/**
 * initialise entity events.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class SetEntityEventsModifier implements ElementVisualModifier {

    ApricotCanvas canvas = null;

    public SetEntityEventsModifier(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void modify(ApricotShape shape) {
        if (shape instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) shape;
            String tableName = entityShape.getEntity().getTableName();

            entityShape.setOnMousePressed(new EntityOnMousePressedEventHandler(tableName, canvas));
        }
    }
}
