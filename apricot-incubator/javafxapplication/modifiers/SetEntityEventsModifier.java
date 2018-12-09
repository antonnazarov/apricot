package javafxapplication.modifiers;

import javafxapplication.canvas.ApricotElementShape;
import javafxapplication.canvas.ApricotEntityRelationshipCanvas;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.event.EntityOnMousePressedEventHandler;

/**
 * initialise entity events.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class SetEntityEventsModifier implements ElementVisualModifier {
    
    ApricotEntityRelationshipCanvas canvas = null;
    
    public SetEntityEventsModifier(ApricotEntityRelationshipCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void modify(ApricotElementShape shape) {
        if (shape instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) shape;
            String tableName = entityShape.getEntity().getTableName();
            
            entityShape.setOnMousePressed(new EntityOnMousePressedEventHandler(tableName, canvas));
        }
    }
}
