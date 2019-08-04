package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The scale handler of the canvas
 * 
 * @author Anton Nazarov
 * @since 04/08/2019
 */
@Component
public class CanvasScaleHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;
    
    @Autowired
    ObjectAllocationHandler allocationHandler;

    public void setScale(String scale) {
        double sc = Double.parseDouble(scale.substring(0, scale.length() - 1)) / 100.0;
        setScale(sc);
    }

    public void setScale(double scale) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        canvas.setScale(scale);
        Pane canvasPane = (Pane) canvas;
        canvasPane.setScaleX(scale);
        canvasPane.setScaleY(scale);
        Group g = new Group();
        g.getChildren().add(canvasPane);

        ScrollPane sp = canvasHandler.getCurrentViewTabInfo().getScroll();
        sp.setContent(g);
        
        allocationHandler.scrollToSelected(canvasHandler.getCurrentViewTabInfo());
    }
}
