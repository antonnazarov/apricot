package za.co.apricotdb.ui.map;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

/**
 * This component contains the operations, related to the current active frame.
 *
 * @author Anton Nazarov
 * @since 21/11/2020
 */
@Component
public class ActiveFrameHandler {

    public Pane getActiveFrame(ScrollPane scroll, double canvasRatio, double scale) {
        Pane activeFrame = new Pane();

        activeFrame.setPrefHeight(scroll.getHeight() * canvasRatio / scale);
        activeFrame.setPrefWidth(scroll.getWidth() * canvasRatio / scale);

        activeFrame.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        return activeFrame;
    }

    public Point2D getActiveFramePosition(ScrollPane scroll, Pane mapCanvas, Pane activeFrame) {
        double X = mapCanvas.getPrefWidth() * scroll.getHvalue() - activeFrame.getPrefWidth() * scroll.getHvalue();
        double Y = mapCanvas.getPrefHeight() * scroll.getVvalue() - activeFrame.getPrefHeight() * scroll.getVvalue();

        return new Point2D(X, Y);
    }
}
