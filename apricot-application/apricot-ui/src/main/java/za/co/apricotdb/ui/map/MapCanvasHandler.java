package za.co.apricotdb.ui.map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

import java.util.Map;

/**
 * This component implements the functions specific to the Map Canvas.
 *
 * @author Anton Nazarov
 * @since 21/11/2020
 */
@Component
public class MapCanvasHandler {

    @Autowired
    CanvasOnMousePressedHandler mousePressedHandler;

    private ObjectProperty<Label> currentLabel = new SimpleObjectProperty<>();

    /**
     * Create a new Map Canvas Pane.
     */
    public Pane getMapCanvas(ApricotCanvas canvas, double canvasRatio) {
        Pane mapCanvas = new Pane();
        mapCanvas.setBorder(new Border(new BorderStroke(Color.BLUE,
                BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        mapCanvas.setPrefWidth(((Pane)canvas).getWidth() * canvasRatio);
        mapCanvas.setPrefHeight(((Pane)canvas).getHeight() * canvasRatio);
        mapCanvas.setOnMousePressed(mousePressedHandler);

        return mapCanvas;
    }

    public void populateMapCanvas(ApricotCanvas canvas, MapHolder mapHolder, double canvasRatio) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY) {
                ApricotEntityShape shape = ((ApricotEntity)element).getEntityShape();
                Point2D position = new Point2D(shape.getLayoutX() * canvasRatio, shape.getLayoutY() * canvasRatio);
                String tableName = ((ApricotEntity)element).getTableName();
                mapHolder.addEntity(tableName,
                        createEntity(shape.getWidth(), shape.getHeight(), canvasRatio,
                                element.getElementStatus()), position);
            }
        }

        addInfoOverlays(mapHolder);
    }

    private void addInfoOverlays(MapHolder mapHolder) {
        Pane mapCanvas = mapHolder.getMapCanvas();
        Map<String, VBox> entities = mapHolder.getMapEntities();
        for (String entityName : entities.keySet()) {
            VBox entity = entities.get(entityName);
            Label info = new Label(entityName);

            info.setEffect(new DropShadow(2.0, Color.BLACK));
            info.setTextFill(Color.WHITE);
            info.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-5.0))));

            info.setLayoutX(entity.getLayoutX());
            info.setLayoutY(entity.getLayoutY());
            info.setVisible(false);
            info.setOnMouseExited(e-> info.setVisible(false));
            entity.setUserData(info);

            mapCanvas.getChildren().add(info);
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

        entity.setOnMouseEntered(e -> {
            if (currentLabel.get() != null) {
                currentLabel.get().setVisible(false);
            }
            if (entity.getUserData() != null) {
                Label info = (Label) entity.getUserData();
                info.setLayoutX(entity.getLayoutX() + e.getX());
                info.setLayoutY(entity.getLayoutY() + e.getY());
                info.setVisible(true);
                currentLabel.set(info);

                e.consume();
            }
        });

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
