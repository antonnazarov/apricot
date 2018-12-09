package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafxapplication.canvas.ApricotEntityRelationshipCanvas;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.shape.ApricotEntityShape;

/**
 * The mouse is pressed.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class EntityOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    public static final double RIM_CONTROL_WIDTH = 10;

    private String tableName = null;
    ApricotEntityRelationshipCanvas canvas = null;

    public EntityOnMousePressedEventHandler(String tableName, ApricotEntityRelationshipCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        // event.isControlDown() !!!!!!!
        if (event.getSource() instanceof ApricotEntityShape && event.getButton() == MouseButton.PRIMARY) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                DraggingType type = getDraggingType(entityShape, event.getX(), event.getY());
                registerEntityOriginalPosition(entityShape, event.getSceneX(), event.getSceneY(), type);
                
                ApricotEntity entity = entityShape.getEntity();
                entity.setElementStatus(ElementStatus.SELECTED);
                


                event.consume();
            }
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
    private DraggingType getDraggingType(ApricotEntityShape entityShape, double eventX, double eventY) {
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
