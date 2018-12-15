package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.entity.shape.ApricotEntityShape;

public class EntityOnMouseEnteredEventHandler implements EventHandler<MouseEvent> {
    
    private String tableName = null;
    private ApricotCanvas canvas = null;

    public EntityOnMouseEnteredEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
    }
    
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                Pane pane = (Pane) canvas;

                pane.getScene().setCursor(Cursor.HAND);                
                
                event.consume();
            }
        }
    }
}
