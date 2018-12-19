package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.event.EntityOnMouseDraggedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseEnteredEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseMovedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseReleasedEventHandler;
import za.co.apricotdb.viewport.event.EventOnMouseExitedEventHandler;

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
