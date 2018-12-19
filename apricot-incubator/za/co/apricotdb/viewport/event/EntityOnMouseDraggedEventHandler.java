package za.co.apricotdb.viewport.event;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * The mouse is dragged event applied on the source Entity.
 * 
 * @author Anton Nazarov
 * @since 12/12/2018
 *
 */
public class EntityOnMouseDraggedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;
    private GroupOperationHandler groupHander = null;

    public EntityOnMouseDraggedEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
        groupHander = new GroupOperationHandler();
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape && event.getButton() == MouseButton.PRIMARY) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {

                if (entityShape.getUserData() != null && entityShape.getUserData() instanceof DragInitPosition) {
                    DragInitPosition pos = (DragInitPosition) entityShape.getUserData();

                    double offsetX = event.getSceneX() - pos.getOrgSceneX();
                    double offsetY = event.getSceneY() - pos.getOrgSceneY();
                    double newTranslateX = pos.getOrgTranslateX() + offsetX;
                    double newTranslateY = pos.getOrgTranslateY() + offsetY;

                    VBox b = (VBox) entityShape;
                    switch (pos.getDraggingType()) {
                    case ENTITY_POSITION_DRAGGING:
                        groupHander.setEntityTranslatePosition(canvas, newTranslateX, newTranslateY,
                                ElementStatus.SELECTED);

                        Pane pane = (Pane) canvas;
                        Scene scene = pane.getScene();
                        scene.setCursor(Cursor.HAND);

                        break;
                    case ENTITY_HORIZONTAL_DRAGGING:
                        setNewWidth(b, pos, offsetX);
                        groupHander.rebuildRelationships((ApricotEntity) entityShape.getElement());

                        break;
                    default:
                        break;
                    }

                    event.consume();
                }
            }
        }
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
}
