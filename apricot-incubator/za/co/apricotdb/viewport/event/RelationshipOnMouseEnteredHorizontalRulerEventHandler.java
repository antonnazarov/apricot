package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

public class RelationshipOnMouseEnteredHorizontalRulerEventHandler implements EventHandler<MouseEvent> {
    
    private final ApricotCanvas canvas;
    
    public RelationshipOnMouseEnteredHorizontalRulerEventHandler(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        Pane pane = (Pane) canvas;
        pane.getScene().setCursor(Cursor.E_RESIZE);                
        
        event.consume();
    }
}
