package javafxapplication.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ElementStatus;

public class CanvasOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotCanvas && event.getButton() == MouseButton.PRIMARY) {
            ApricotCanvas canvas = (ApricotCanvas) event.getSource();
            
            canvas.changeAllElementsStatus(ElementStatus.DEFAULT);
            
            Pane pane = (Pane) canvas;
            Scene scene = pane.getScene();
            scene.setCursor(Cursor.NW_RESIZE);
            
            Rectangle lasso = createLasso(event);
            Map<String, Object> ud = getUserData(pane);
            ud.put("lasso", lasso);
            pane.getChildren().add(lasso);
            
            Map<String, Bounds> bounds = canvas.getEntityBounds();
            ud.put("bounds", bounds);
            
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
    
    public static Map<String, Bounds> getBounds(Pane pane) {
        Map<String, Bounds> ret = null;
        
        Map<String, Object> ud = getUserData(pane);
        Object o = ud.get("bounds");
        if (o != null && o instanceof Map) {
            ret = (Map<String, Bounds>) o;
        }
       
        return ret;
    }
    
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
}
