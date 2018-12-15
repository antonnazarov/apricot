package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.entity.shape.ApricotEntityShape;

/**
 * Mouse was moved and this event was caught by the entity.
 * 
 * @author Anton Nazarov
 * @since 12/12/2018
 *
 */
public class EntityOnMouseMovedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;

    public EntityOnMouseMovedEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {

        if (event.getSource() instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                Pane pane = (Pane) canvas;
                Scene scene = pane.getScene();

                switch (EntityOnMousePressedEventHandler.getDraggingType(entityShape, event.getX(), event.getY())) {
                case ENTITY_HORIZONTAL_DRAGGING:
                    scene.setCursor(Cursor.E_RESIZE);
                    break;
                default:
                    scene.setCursor(Cursor.HAND);
                    break;
                }

                event.consume();
            }
        }
    }
}
