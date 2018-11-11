package javafxapplication;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static javafxapplication.SimpleEntityBuilder.BORDER_WIDTH_STANDARD;
import static javafxapplication.SimpleEntityBuilder.BORDER_WIDTH_THICK;

/**
 * A helper class which creates the events,
 *
 * @author Anton Nazarov
 * @since 05/11/2018
 */
public class EntityEventBuilder implements EventBuilder {

    public static final double RIM_CONTROL_WIDTH = 10;

    @Override
    public EventHandler<MouseEvent> getOnMousePressedEventHandler(String entityId) {
        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                Node sourceNode = (Node) t.getSource();
                if (t.getButton() == MouseButton.PRIMARY && entityId.equals(sourceNode.getId())) {
                    registerEntityOriginalPosition(t, entityId);
                    setFocusToEntity(t);
                }
            }
        };

        return onMousePressedEventHandler;
    }

    private void registerEntityOriginalPosition(MouseEvent t, String entityId) {

        Node sourceNode = (Node) t.getSource();
        DragInitPosition pos = new DragInitPosition(t.getSceneX(),
                t.getSceneY(), sourceNode.getTranslateX(),
                sourceNode.getTranslateY());
        DraggingType type = getDraggingType(t, entityId);
        pos.setDraggingType(type);
        VBox box = (VBox) sourceNode;
        pos.setOrigWidth(box.getWidth());
        pos.setOrigHeight(box.getHeight());

        sourceNode.setUserData(pos);
    }

    private void setFocusToEntity(MouseEvent t) {
        Node sourceNode = (Node) t.getSource();

        sourceNode.requestFocus();
        setEntityBorderThickness(sourceNode, BORDER_WIDTH_THICK);

        Parent p = sourceNode.getParent();
        Pane parent = (Pane) p;
        ObservableList<Node> children = parent.getChildren();
        List<Node> sortedCol = new ArrayList<>();
        for (Node n : children) {
            if (n != sourceNode) {
                sortedCol.add(n);
                if (n instanceof VBox) {
                    setEntityBorderThickness(n, BORDER_WIDTH_STANDARD);
                }
            }
        }

        sortedCol.add(sourceNode);
        children.removeAll(children);
        children.addAll(sortedCol);
    }

    /**
     * The current focused entity
     */
    private void setEntityBorderThickness(Node sourceNode, double borderWidth) {
        VBox entity = (VBox) sourceNode;
        GridPane primary = (GridPane) entity.getChildren().get(1);
        BorderStroke bs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(borderWidth));
        Border b = new Border(bs);
        primary.setBorder(b);

        GridPane nonPrimary = (GridPane) entity.getChildren().get(2);
        bs = new BorderStroke(Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                new CornerRadii(0), new BorderWidths(borderWidth), Insets.EMPTY);
        b = new Border(bs);
        nonPrimary.setBorder(b);
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler(Stage primaryStage, String entityId) {
        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                Node sourceNode = (Node) t.getSource();
                if (t.getButton() == MouseButton.PRIMARY && entityId.equals(sourceNode.getId())) {
                    if (sourceNode.getUserData() != null && sourceNode.getUserData() instanceof DragInitPosition) {
                        DragInitPosition pos = (DragInitPosition) sourceNode.getUserData();

                        double offsetX = t.getSceneX() - pos.getOrgSceneX();
                        double offsetY = t.getSceneY() - pos.getOrgSceneY();
                        double newTranslateX = pos.getOrgTranslateX() + offsetX;
                        double newTranslateY = pos.getOrgTranslateY() + offsetY;

                        VBox b = (VBox) sourceNode;
                        switch (pos.getDraggingType()) {
                            case ENTITY_POSITION_DRAGGING:
                                sourceNode.setTranslateX(newTranslateX);
                                sourceNode.setTranslateY(newTranslateY);
                                break;
                            case ENTITY_HORIZONTAL_DRAGGING:
                                setNewWidth(b, pos, offsetX);
                                break;
                        }

                        primaryStage.getScene().setCursor(Cursor.HAND);
                    }
                }
            }
        };

        return onMouseDraggedEventHandler;
    }

    private void setNewWidth(VBox entity, DragInitPosition pos, double offsetX) {
        List<Node> children = entity.getChildren();
        for (Node n : children) {
            if (n instanceof GridPane) {
                GridPane gp = (GridPane) n;
                gp.setPrefWidth(pos.getOrigWidth() + offsetX);
            }
        }

        entity.setPrefWidth(pos.getOrigWidth() + offsetX);
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler(Stage primaryStage, String entityId) {
        return (event -> {
            Node sourceNode = (Node) event.getSource();
            if (event.getButton() == MouseButton.PRIMARY && entityId.equals(sourceNode.getId())) {
                if (primaryStage.getScene().getCursor() != Cursor.DEFAULT) {
                    primaryStage.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseMovedEventHandler(Stage primaryStage, String entityId) {
        EventHandler<MouseEvent> onMouseMovedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                switch (getDraggingType(t, entityId)) {
                    case ENTITY_NW_DRAGGING:
                        // primaryStage.getScene().setCursor(Cursor.NW_RESIZE);
                        break;
                    case ENTITY_HORIZONTAL_DRAGGING:
                        primaryStage.getScene().setCursor(Cursor.E_RESIZE);
                        break;
                    case ENTITY_VERTICAL_DRAGGING:
                        // primaryStage.getScene().setCursor(Cursor.N_RESIZE);
                        break;
                    default:
                        primaryStage.getScene().setCursor(Cursor.DEFAULT);
                        break;
                }

                t.consume();
            }
        };

        return onMouseMovedEventHandler;
    }

    private DraggingType getDraggingType(MouseEvent t, String entityId) {
        DraggingType type = DraggingType.ENTITY_POSITION_DRAGGING;

        Node n = (Node) t.getSource();
        if (entityId.equals(n.getId())) {
            VBox entity = (VBox) n;
            if (t.getX() > entity.getWidth() - RIM_CONTROL_WIDTH
                    && t.getY() > entity.getHeight() - RIM_CONTROL_WIDTH) {
                type = DraggingType.ENTITY_NW_DRAGGING;
            } else if (t.getX() > entity.getWidth() - RIM_CONTROL_WIDTH) {
                type = DraggingType.ENTITY_HORIZONTAL_DRAGGING;
            } else if (t.getY() > entity.getHeight() - RIM_CONTROL_WIDTH) {
                type = DraggingType.ENTITY_VERTICAL_DRAGGING;
            }
        }

        return type;
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseExitedEventHandler(Stage primaryStage) {
        EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        };

        return onMouseExitedEventHandler;
    }
}
