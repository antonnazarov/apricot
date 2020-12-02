package za.co.apricotdb.ui.map;

import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This is a central component which provides access to the functionality of Apricot Map.
 *
 * @author Anton Nazarov
 * @since 20/11/2020
 */
@Component
public class MapHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    MapCanvasHandler mapCanvasHandler;

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    private MapHolder mapHolder;
    private Pane mapPane;
    private TabInfoObject currentTabInfo;

    public void setMapPane(Pane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * Draw/re-draw the map from scratch.
     */
    @ApricotErrorLogger(title = "Unable to draw the scaled map")
    public void drawMap() {
        if (mapPane == null) {
            throw new IllegalArgumentException("The mapPane object was hot initialized");
        }
        currentTabInfo = canvasHandler.getCurrentViewTabInfo();

        double ratio = calculateCanvasRatio(currentTabInfo.getCanvas());
        Pane mapCanvas = mapCanvasHandler.getMapCanvas(currentTabInfo.getCanvas(), ratio);
        Pane activeFrame = activeFrameHandler.getActiveFrame(currentTabInfo.getScroll(), ratio, currentTabInfo.getCanvas().getScale());

        mapHolder = new MapHolder(mapPane, mapCanvas, activeFrame,
                activeFrameHandler.getActiveFramePosition(currentTabInfo.getScroll(), mapCanvas, activeFrame));
        mapCanvasHandler.populateMapCanvas(currentTabInfo.getCanvas(), mapHolder, ratio);
    }

    /**
     * Calculate the Ratio which the current canvas has to be resized down to fit the current map pane.
     */
    private double calculateCanvasRatio(ApricotCanvas canvas) {
        double ratio = 1d;
        Pane pCanvas = (Pane) canvas;
        if (mapPane.getHeight() < pCanvas.getHeight()) {
            double vRatio = mapPane.getHeight()/pCanvas.getHeight();
            double widthScaled = pCanvas.getWidth() * vRatio;

            if (widthScaled < mapPane.getWidth()) {
                ratio = vRatio;
            } else {
                if (mapPane.getWidth() < pCanvas.getWidth()) {
                    ratio = mapPane.getWidth()/pCanvas.getWidth();
                }
            }
        }

        return ratio;
    }
}
