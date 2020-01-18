package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.event.EntityOnMouseDraggedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseEnteredEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseMovedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.EntityOnMouseReleasedEventHandler;
import za.co.apricotdb.viewport.event.EventOnMouseExitedEventHandler;
import za.co.apricotdb.viewport.event.GroupOperationHandler;
import za.co.apricotdb.viewport.relationship.RelationshipBatchBuilder;

/**
 * Initialize entity events.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class SetEntityEventsModifier implements ElementVisualModifier {

    private ApricotCanvas canvas = null;
    private GroupOperationHandler groupHandler = null;
    private AlignCommand aligner = null;
    private RelationshipBatchBuilder relationshipBuilder = null;

    public SetEntityEventsModifier(ApricotCanvas canvas, GroupOperationHandler groupHandler, 
            AlignCommand aligner, RelationshipBatchBuilder relationshipBuilder) {
        this.canvas = canvas;
        this.groupHandler = groupHandler;
        this.aligner = aligner;
        this.relationshipBuilder = relationshipBuilder;
    }

    @Override
    public void modify(ApricotShape shape) {
        if (shape instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) shape;
            ApricotElement element = entityShape.getElement();
            String tableName = ((ApricotEntity) element).getTableName();

            entityShape.setOnMousePressed(new EntityOnMousePressedEventHandler(tableName, canvas));
            entityShape.setOnMouseReleased(new EntityOnMouseReleasedEventHandler(tableName, canvas, aligner, groupHandler));
            entityShape.setOnMouseDragged(new EntityOnMouseDraggedEventHandler(tableName, canvas, groupHandler, relationshipBuilder));
            entityShape.setOnMouseMoved(new EntityOnMouseMovedEventHandler(tableName, canvas));
            entityShape.setOnMouseEntered(new EntityOnMouseEnteredEventHandler(tableName, canvas));
            entityShape.setOnMouseExited(new EventOnMouseExitedEventHandler(tableName, canvas));
        }
    }
}
