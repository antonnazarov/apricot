package javafxapplication.modifiers;

import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ApricotShape;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.event.EntityOnMouseDraggedEventHandler;
import javafxapplication.event.EntityOnMouseEnteredEventHandler;
import javafxapplication.event.EntityOnMouseMovedEventHandler;
import javafxapplication.event.EntityOnMousePressedEventHandler;
import javafxapplication.event.EntityOnMouseReleasedEventHandler;
import javafxapplication.event.EventOnMouseExitedEventHandler;

/**
 * Initialize entity events.
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
            ApricotElement element = entityShape.getElement();
            String tableName = ((ApricotEntity) element).getTableName();

            entityShape.setOnMousePressed(new EntityOnMousePressedEventHandler(tableName, canvas));
            entityShape.setOnMouseReleased(new EntityOnMouseReleasedEventHandler(tableName, canvas));
            entityShape.setOnMouseDragged(new EntityOnMouseDraggedEventHandler(tableName, canvas));
            entityShape.setOnMouseMoved(new EntityOnMouseMovedEventHandler(tableName, canvas));
            entityShape.setOnMouseEntered(new EntityOnMouseEnteredEventHandler(tableName, canvas));
            entityShape.setOnMouseExited(new EventOnMouseExitedEventHandler(tableName, canvas));
        }
    }
}
