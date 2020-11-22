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
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This component contains the operations, related to the current active frame.
 *
 * @author Anton Nazarov
 * @since 21/11/2020
 */
@Component
public class ActiveFrameHandler {

    public Pane getActiveFrame(ScrollPane scroll, double canvasRatio) {
        Pane activeFrame = new Pane();

        activeFrame.setPrefHeight(scroll.getHeight() * canvasRatio);
        activeFrame.setPrefWidth(scroll.getWidth() * canvasRatio);

        activeFrame.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        return activeFrame;
    }

    public Point2D getActiveFramePosition(ApricotCanvas canvas, ScrollPane scroll, Pane mapCanvas,
                                          Pane activeFrame, double canvasRatio) {
        double X = ((Pane)canvas).getWidth() * scroll.getHvalue() * canvasRatio;
        X -= activeFrame.getPrefWidth()/2;
        if (X < 0) {
            X = 0;
        }
        double Y = ((Pane)canvas).getHeight() * scroll.getVvalue() * canvasRatio;
        Y -= activeFrame.getPrefHeight()/2;
        if (Y < 0) {
            Y=0;
        }
        if (X+activeFrame.getPrefWidth() > mapCanvas.getPrefWidth()) {
            X = mapCanvas.getPrefWidth() - activeFrame.getPrefWidth();
        }
        if (Y+activeFrame.getPrefHeight() > mapCanvas.getPrefHeight()) {
            Y = mapCanvas.getPrefHeight() - activeFrame.getPrefHeight();
        }

        return new Point2D(X, Y);
    }
}
