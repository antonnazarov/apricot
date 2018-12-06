package javafxapplication.modifiers;

import javafx.scene.layout.VBox;
import javafxapplication.canvas.ApricotElementShape;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.event.EntityOnMousePressedEventHandler;

/**
 * initialise entity events.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class SetEntityEventsModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotElementShape shape) {
        if (shape instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) shape;
            String tableName = entityShape.getEntity().getTableName();
            
            entityShape.setOnMousePressed(new EntityOnMousePressedEventHandler(tableName));
        }
    }
}
