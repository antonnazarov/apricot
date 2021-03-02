package za.co.apricotdb.ui.map;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This component contains the operations, related to the current active frame.
 *
 * @author Anton Nazarov
 * @since 21/11/2020
 */
@Component
public class ActiveFrameHandler {

    @Autowired
    ActiveFrameOnMousePressedHandler activeFrameOnMousePressedHandler;

    @Autowired
    ActiveFrameOnMouseDraggedHandler activeFrameOnMouseDraggedHandler;

    @Autowired
    ActiveFrameOnMouseReleasedEventHandler activeFrameOnMouseReleasedEventHandler;

    @Autowired
    MapHandler mapHandler;

    private boolean isDragging;

    public Pane getActiveFrame(ScrollPane scroll, double canvasRatio, double scale) {
        Pane activeFrame = new Pane();
        activeFrame.setOnMousePressed(activeFrameOnMousePressedHandler);
        activeFrame.setOnMouseDragged(activeFrameOnMouseDraggedHandler);
        activeFrame.setOnMouseReleased(activeFrameOnMouseReleasedEventHandler);

        activeFrame.setPrefHeight(scroll.getHeight() * canvasRatio / scale);
        activeFrame.setPrefWidth(scroll.getWidth() * canvasRatio / scale);

        activeFrame.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        Pane pane = mapHandler.getMapPane();
        activeFrame.setOnMouseEntered(e -> {
            pane.setCursor(Cursor.HAND);
        });
        activeFrame.setOnMouseExited(e -> {
            pane.setCursor(Cursor.DEFAULT);
        });

        return activeFrame;
    }

    public Point2D getActiveFramePosition(ScrollPane scroll, Pane mapCanvas, Pane activeFrame) {
        double X = mapCanvas.getPrefWidth() * scroll.getHvalue() - activeFrame.getPrefWidth() * scroll.getHvalue();
        double Y = mapCanvas.getPrefHeight() * scroll.getVvalue() - activeFrame.getPrefHeight() * scroll.getVvalue();

        return new Point2D(X, Y);
    }

    public void positionActiveFrame(ScrollPane scroll) {
        MapHolder mapHolder = mapHandler.getMapHolder();
        Pane activeFrame = mapHolder.getActiveFrame();
        Point2D position = getActiveFramePosition(scroll, mapHolder.getMapCanvas(), activeFrame);

        activeFrame.setLayoutX(position.getX());
        activeFrame.setLayoutY(position.getY());
    }

    public double getScrollHvalue(Pane mapCanvas, Pane activeFrame, double activeFrameX) {
        return activeFrameX / (mapCanvas.getWidth() - activeFrame.getWidth());
    }

    public double getScrollVvalue(Pane mapCanvas, Pane activeFrame, double activeFrameY) {
        return activeFrameY / (mapCanvas.getHeight() - activeFrame.getHeight());
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
