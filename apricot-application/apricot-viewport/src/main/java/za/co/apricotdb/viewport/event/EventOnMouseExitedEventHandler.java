package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

public class EventOnMouseExitedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;

    public EventOnMouseExitedEventHandler(String tableName, ApricotCanvas canvas) {
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

                scene.setCursor(Cursor.DEFAULT);

                event.consume();
            }
        }
    }
}
