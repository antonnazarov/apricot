package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.notification.EditEntityEvent;
import za.co.apricotdb.viewport.notification.EntityContextMenuEvent;

/**
 * The mouse is pressed.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class EntityOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    public static final double RIM_CONTROL_WIDTH = 10;

    private String tableName = null;
    private ApricotCanvas canvas = null;

    public EntityOnMousePressedEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape
                && tableName.equals(((ApricotEntityShape) event.getSource()).getId())) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            ApricotElement entity = entityShape.getElement();

            // check if the element is filtered out
            if (entity.getElementStatus() == ElementStatus.GRAYED) {
                event.consume();
                return;
            }

            if (event.getButton() == MouseButton.PRIMARY) {
                DraggingType type = getDraggingType(entityShape, event.getX(), event.getY());
                registerEntityOriginalPosition(entityShape, event.getSceneX(), event.getSceneY(), type);
                if (event.getClickCount() == 2) {
                    // the double click runs the Edit Entity command
                    canvas.publishEvent(new EditEntityEvent(canvas, tableName));
                } else if (event.isAltDown()) {
                    entity.setElementStatus(ElementStatus.DEFAULT);
                } else if (event.isControlDown()) {
                    entity.setElementStatus(ElementStatus.SELECTED);
                } else {
                    canvas.changeAllElementsStatus(ElementStatus.DEFAULT, false);
                    entity.setElementStatus(ElementStatus.SELECTED);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // handle the context menu
                if (entity.getElementStatus() == ElementStatus.DEFAULT) {
                    canvas.changeAllElementsStatus(ElementStatus.DEFAULT, false);

                    ApricotEntity apricotEntity = (ApricotEntity) entity;
                    apricotEntity.setSelectPrimaryRelationshipsFlag(false);
                    entity.setElementStatus(ElementStatus.SELECTED);
                    apricotEntity.setSelectPrimaryRelationshipsFlag(true);
                }

                if (entity.getElementStatus() == ElementStatus.SELECTED) {
                    EntityContextMenuEvent cmEvent = new EntityContextMenuEvent((ApricotEntity) entity,
                            event.getScreenX(), event.getScreenY());
                    canvas.publishEvent(cmEvent);
                }
            }

            event.consume();
        }
    }

    private void registerEntityOriginalPosition(ApricotEntityShape entityShape, double sceneX, double sceneY,
            DraggingType type) {

        DragInitPosition pos = new DragInitPosition(sceneX, sceneY, entityShape.getTranslateX(),
                entityShape.getTranslateY());

        pos.setDraggingType(type);

        pos.setOrigWidth(entityShape.getWidth());
        pos.setOrigHeight(entityShape.getHeight());

        entityShape.setUserData(pos);
    }

    /**
     * Detect the dragging type for the current entity. Not all dragging types have
     * been used.
     */
    public static DraggingType getDraggingType(ApricotEntityShape entityShape, double eventX, double eventY) {
        DraggingType type = DraggingType.ENTITY_POSITION_DRAGGING;

        if (eventX > entityShape.getWidth() - RIM_CONTROL_WIDTH
                && eventY > entityShape.getHeight() - RIM_CONTROL_WIDTH) {
            type = DraggingType.ENTITY_NW_DRAGGING;
        } else if (eventX > entityShape.getWidth() - RIM_CONTROL_WIDTH) {
            type = DraggingType.ENTITY_HORIZONTAL_DRAGGING;
        } else if (eventY > entityShape.getHeight() - RIM_CONTROL_WIDTH) {
            type = DraggingType.ENTITY_VERTICAL_DRAGGING;
        }

        return type;
    }
}
