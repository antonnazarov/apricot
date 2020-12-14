package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.map.MapHandler;
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
    
    @Autowired
    MainAppController appController;

    @Autowired
    MapHandler mapHandler;

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

        mapHandler.drawMap();
    }

    public void resetScaleIndicator(ComboBox<String> scale) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        double dScale = canvas.getScale();
        String sScale = String.valueOf((int) (dScale * 100)) + "%";
        if (!scale.getSelectionModel().getSelectedItem().equals(sScale)) {
            scale.getSelectionModel().select(sScale);
        }
    }

    /**
     * Find the scale, which suits the current canvas.
     */
    public void fitCanvasScale() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        Pane canvasBox = (Pane) canvas;
        ScrollPane sp = canvasHandler.getCurrentViewTabInfo().getScroll();
        double ratioX = sp.getWidth() / canvasBox.getPrefWidth();
        double ratioY = sp.getHeight() / canvasBox.getPrefHeight();

        double ratio = Math.max(ratioX, ratioY);
        setScale(getFitScale(ratio));
        resetScaleIndicator(appController.getScale());
    }

    private double getFitScale(double ratio) {
        double ret = 0;
        if (ratio < 0.2) {
            ret = 0.2;
        } else if (ratio < 0.5) {
            ret = 0.5;
        } else if (ratio < 0.8) {
            ret = 0.8;
        } else {
            ret = 1;
        }

        return ret;
    }
}
