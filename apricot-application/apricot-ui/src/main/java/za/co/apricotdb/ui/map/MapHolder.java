package za.co.apricotdb.ui.map;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * The holder of the map data/objects.
 */
public class MapHolder {

    private Map<String, VBox> mapEntities;
    private Pane mapCanvas;
    private Pane mapPane;
    private Pane activeFrame;

    public MapHolder(Pane mapPane, Pane mapCanvas, Pane activeFrame, Point2D activeFramePosition) {
        this.mapPane = mapPane;
        this.mapCanvas = mapCanvas;
        this.activeFrame = activeFrame;

        cleanMap();
        this.mapPane.getChildren().add(mapCanvas);
        this.mapPane.getChildren().add(activeFrame);
        this.activeFrame.setLayoutX(activeFramePosition.getX());
        this.activeFrame.setLayoutY(activeFramePosition.getY());

        mapEntities = new HashMap<>();
    }

    /**
     * Put the entity to the map canvas.
     */
    public void addEntity(String entityName, VBox entity, Point2D position) {
        mapEntities.put(entityName, entity);
        mapCanvas.getChildren().add(entity);
        entity.setLayoutX(position.getX());
        entity.setLayoutY(position.getY());
    }

    public void setEntityPosition(String entityName, Point2D position) {
        VBox entity = mapEntities.get(entityName);
        if (entity != null) {
            entity.setLayoutX(position.getX());
            entity.setLayoutY(position.getY());
        }
    }

    public void cleanMap() {
        mapPane.getChildren().clear();
    }
}
