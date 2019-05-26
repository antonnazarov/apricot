package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.notification.AddLayoutSavepointEvent;

public class EntityOnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;
    private AlignCommand aligner = null;
    private GroupOperationHandler groupHandler = null;

    public EntityOnMouseReleasedEventHandler(String tableName, ApricotCanvas canvas, AlignCommand aligner, 
            GroupOperationHandler groupHandler) {
        this.tableName = tableName;
        this.canvas = canvas;
        this.aligner = aligner;
        this.groupHandler = groupHandler;
    }
    
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                AddLayoutSavepointEvent evt = new AddLayoutSavepointEvent(event);
                if (groupHandler.applyCurrentPosition(canvas, ElementStatus.SELECTED)) {
                    canvas.publishEvent(evt);
                } else {
                    if (entityShape.getUserData() != null && entityShape.getUserData() instanceof DragInitPosition) {
                        DragInitPosition pos = (DragInitPosition) entityShape.getUserData();
                        if (pos.getOrigWidth() != entityShape.getWidth()) {
                            canvas.publishEvent(evt);
                        }
                    }
                }
                aligner.align();
                
                event.consume();
            }
        }
    }
}
