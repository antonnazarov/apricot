package za.co.apricotdb.viewport.event;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

@Component
@Qualifier("CanvasOnMouseReleasedEventHandler")
public class CanvasOnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotCanvas && event.getButton() == MouseButton.PRIMARY) {
            ApricotCanvas canvas = (ApricotCanvas) event.getSource();
            
            Pane pane = (Pane) canvas;
            Scene scene = pane.getScene();
            scene.setCursor(Cursor.DEFAULT);
            
            Rectangle lasso = CanvasOnMousePressedEventHandler.getLasso(pane);
            pane.getChildren().remove(lasso);
            
            event.consume();
        }
    }
}
