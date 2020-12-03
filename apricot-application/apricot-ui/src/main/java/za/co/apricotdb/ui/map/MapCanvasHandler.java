package za.co.apricotdb.ui.map;

import javafx.geometry.Point2D;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This component implements the functions specific to the Map Canvas.
 *
 * @author Anton Nazarov
 * @since 21/11/2020
 */
@Component
public class MapCanvasHandler {

    /**
     * Create a new Map Canvas Pane.
     */
    public Pane getMapCanvas(ApricotCanvas canvas, double canvasRatio) {
        Pane mapCanvas = new Pane();
        mapCanvas.setBorder(new Border(new BorderStroke(Color.BLUE,
                BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        mapCanvas.setPrefWidth(((Pane)canvas).getWidth() * canvasRatio);
        mapCanvas.setPrefHeight(((Pane)canvas).getHeight() * canvasRatio);

        return mapCanvas;
    }

    public void populateMapCanvas(ApricotCanvas canvas, MapHolder mapHolder, double canvasRatio) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY) {
                ApricotEntityShape shape = ((ApricotEntity)element).getEntityShape();
                Point2D position = new Point2D(shape.getLayoutX() * canvasRatio, shape.getLayoutY() * canvasRatio);
                mapHolder.addEntity(((ApricotEntity)element).getTableName(),
                        createEntity(shape.getWidth(), shape.getHeight(), canvasRatio, element.getElementStatus()), position);
            }
        }
    }

    public void moveEntity(ApricotEntityShape entityShape, double translateX, double translateY,
                           MapHolder mapHolder, double canvasRatio, String entityName) {
        Point2D newPosition = new Point2D((entityShape.getLayoutX()+translateX)*canvasRatio,
                (entityShape.getLayoutY()+translateY)*canvasRatio);
        mapHolder.setEntityPosition(entityName, newPosition);
    }

    public void changeEntityStatus(MapHolder mapHolder, String entityName, ElementStatus status) {
        VBox entity = mapHolder.getEntityByName(entityName);
        if (entity != null) {
            entity.setBorder(getBorder(status));
        }
    }

    private VBox createEntity(double originalWidth, double originalHeight, double canvasRatio, ElementStatus status) {
        double prefWidth = originalWidth * canvasRatio;
        if (prefWidth < 5) {
            prefWidth = 5;
        }
        double prefHeight = originalHeight * canvasRatio;
        if (prefHeight < 2) {
            prefHeight = 2;
        }

        VBox entity = new VBox();
        entity.setBorder(getBorder(status));
        entity.setPrefHeight(prefHeight);
        entity.setPrefWidth(prefWidth);

        return entity;
    }

    private Border getBorder(ElementStatus status) {
        BorderWidths borderWidth = new BorderWidths((0.4));
        Color color = Color.BLACK;
        if (status == ElementStatus.SELECTED) {
            borderWidth = new BorderWidths((1.5));
        }
        if (status == ElementStatus.GRAYED) {
            color = Color.LIGHTGREY;
        }

        return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, borderWidth));
    }
}
