package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.notification.CanvasContextMenuEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
import za.co.apricotdb.viewport.topology.LineSegment;
import za.co.apricotdb.viewport.topology.TopologyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("CanvasOnMousePressedEventHandler")
public class CanvasOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    public static final double SELECTION_SPOT_SIZE = 15;

    @Autowired
    private TopologyManager topologyManager;

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotCanvas) {
            ApricotCanvas canvas = (ApricotCanvas) event.getSource();
            canvas.changeAllElementsStatus(ElementStatus.DEFAULT, false);

            if (selectNearestRelationship(canvas, event)) {
                event.consume();
                return;
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                Pane pane = (Pane) canvas;
                Scene scene = pane.getScene();
                scene.setCursor(Cursor.NW_RESIZE);

                Map<String, Object> ud = getUserData(pane);
                Rectangle lasso = createLasso(event);
                ud.put("lasso", lasso);
                pane.getChildren().add(lasso);

                Map<String, Bounds> bounds = canvas.getEntityBounds();
                ud.put("bounds", bounds);
            } else {
                // the right click on canvas - call the context menu
                CanvasContextMenuEvent cmEvent = new CanvasContextMenuEvent(canvas, event.getScreenX(),
                        event.getScreenY());
                canvas.publishEvent(cmEvent);
            }

            event.consume();
        }
    }

    private Rectangle createLasso(MouseEvent event) {
        Rectangle lasso = new Rectangle(event.getX(), event.getY(), 1, 1);
        lasso.setStroke(Color.BLUE);
        lasso.setFill(Color.TRANSPARENT);
        lasso.setStrokeWidth(0.5);
        lasso.getStrokeDashArray().addAll(2d, 2d);
        Point2D pos = new Point2D(event.getSceneX(), event.getSceneY());
        lasso.setUserData(pos);

        return lasso;
    }

    public static Rectangle getLasso(Pane pane) {
        Rectangle ret = null;

        Map<String, Object> ud = getUserData(pane);
        Object o = ud.get("lasso");
        if (o != null && o instanceof Rectangle) {
            ret = (Rectangle) o;
        }

        return ret;
    }
    
    public static void removeLasso(Pane pane) {
        Map<String, Object> ud = getUserData(pane);
        ud.remove("lasso");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Bounds> getBounds(Pane pane) {
        Map<String, Bounds> ret = null;

        Map<String, Object> ud = getUserData(pane);
        Object o = ud.get("bounds");
        if (o != null && o instanceof Map) {
            ret = (Map<String, Bounds>) o;
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getUserData(Pane pane) {
        Map<String, Object> userData = null;

        Object o = pane.getUserData();
        if (o != null && o instanceof Map<?, ?>) {
            userData = (Map<String, Object>) o;
        } else {
            userData = new HashMap<>();
            pane.setUserData(userData);
        }

        return userData;
    }

    private boolean selectNearestRelationship(ApricotCanvas canvas, MouseEvent event) {
        List<ApricotRelationship> relationships = canvas.getRelationships();
        Rectangle2D spot = new Rectangle2D(event.getX() - SELECTION_SPOT_SIZE / 2,
                event.getY() - SELECTION_SPOT_SIZE / 2, SELECTION_SPOT_SIZE, SELECTION_SPOT_SIZE);

        for (ApricotRelationship r : relationships) {
            ApricotRelationshipShape shape = (ApricotRelationshipShape) r.getShape();
            Path path = shape.getPath();

            List<LineSegment> sgmts = topologyManager.getSegments(path);
            for (LineSegment ls : sgmts) {
                if (topologyManager.intersects(ls, spot) && r.getElementStatus() != ElementStatus.GRAYED) {
                    RelationshipOnMousePressedEventHandler relationshipOnMousePressedEventHandler =
                            new RelationshipOnMousePressedEventHandler(canvas);
                    relationshipOnMousePressedEventHandler.handle(canvas, r, (event.getButton() == MouseButton.SECONDARY), event.getScreenX(), event.getScreenY());

                    return true;
                }
            }
        }

        return false;
    }
}
