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
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;
import za.co.apricotdb.viewport.relationship.RelationshipBatchBuilder;

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
    private GroupOperationHandler groupHandler = null;
    private RelationshipBatchBuilder relationshipBuilder = null;

    public EntityOnMouseDraggedEventHandler(String tableName, ApricotCanvas canvas, GroupOperationHandler groupHandler,
            RelationshipBatchBuilder relationshipBuilder) {
        this.tableName = tableName;
        this.canvas = canvas;
        this.groupHandler = groupHandler;
        this.relationshipBuilder = relationshipBuilder;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape && event.getButton() == MouseButton.PRIMARY) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {

                if (entityShape.getUserData() != null && entityShape.getUserData() instanceof DragInitPosition) {
                    DragInitPosition pos = (DragInitPosition) entityShape.getUserData();

                    double offsetX = (event.getSceneX() - pos.getOrgSceneX()) / canvas.getScale();
                    double offsetY = (event.getSceneY() - pos.getOrgSceneY()) / canvas.getScale();
                    double newTranslateX = pos.getOrgTranslateX() + offsetX;
                    double newTranslateY = pos.getOrgTranslateY() + offsetY;

                    Scene scene = ((Pane) canvas).getScene();
                    VBox b = (VBox) entityShape;
                    switch (pos.getDraggingType()) {
                    case ENTITY_POSITION_DRAGGING:
                        groupHandler.setEntityTranslatePosition(canvas, newTranslateX, newTranslateY,
                                ElementStatus.SELECTED);
                        relationshipBuilder.buildRelationships(canvas.getSelectedEntities(), canvas.getDetailLevel());
                        scene.setCursor(Cursor.HAND);

                        break;
                    case ENTITY_HORIZONTAL_DRAGGING:
                        setNewWidth(b, pos, offsetX);
                        relationshipBuilder.buildRelationships(canvas.getSelectedEntities(), canvas.getDetailLevel());
                        scene.setCursor(Cursor.E_RESIZE);

                        break;
                    default:
                        break;
                    }

                    canvas.publishEvent(new CanvasChangedEvent(canvas));

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
