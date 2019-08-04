package za.co.apricotdb.viewport.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;

@Component
@Qualifier("CanvasOnMouseDraggedEventHandler")
public class CanvasOnMouseDraggedEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotCanvas && event.getButton() == MouseButton.PRIMARY) {
            ApricotCanvas canvas = (ApricotCanvas) event.getSource();

            Pane pane = (Pane) canvas;

            Rectangle lasso = CanvasOnMousePressedEventHandler.getLasso(pane);
            if (lasso != null && lasso.getUserData() instanceof Point2D) {
                Point2D pos = (Point2D) lasso.getUserData();
                lasso.setWidth(Math.abs(event.getSceneX() - pos.getX()) / canvas.getScale());
                lasso.setHeight(Math.abs(event.getSceneY() - pos.getY()) / canvas.getScale());

                // support of the negative "lasso" movements
                if (pos.getX() > event.getSceneX()) {
                    lasso.setLayoutX((event.getSceneX() - pos.getX()) / canvas.getScale());
                }
                if (pos.getY() > event.getSceneY()) {
                    lasso.setLayoutY((event.getSceneY() - pos.getY()) / canvas.getScale());
                }

                Bounds lassoBounds = lasso.getBoundsInParent();
                Map<String, Bounds> bounds = CanvasOnMousePressedEventHandler.getBounds(pane);

                canvas.changeAllElementsStatus(ElementStatus.DEFAULT);

                for (String table : bounds.keySet()) {
                    if (lassoBounds.intersects(bounds.get(table))) {
                        ApricotEntity entity = canvas.findEntityByName(table);
                        entity.setElementStatus(ElementStatus.SELECTED);
                    }
                }

                // the lasso is always on top
                sendLassoToFront(pane, lasso);
            }
        }
    }

    private void sendLassoToFront(Pane pane, Rectangle lasso) {
        pane.getChildren().remove(lasso);
        pane.getChildren().add(lasso);
    }
}
