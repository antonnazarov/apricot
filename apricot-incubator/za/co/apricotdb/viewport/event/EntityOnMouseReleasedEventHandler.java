package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAjustor;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

public class EntityOnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;
    private AlignCommand aligner = null;
    private GroupOperationHandler groupHander = null;

    public EntityOnMouseReleasedEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
        aligner = new CanvasSizeAjustor(canvas);
        groupHander = new GroupOperationHandler();
    }
    
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                groupHander.applyCurrentPosition(canvas, ElementStatus.SELECTED);
                aligner.align();
                
                event.consume();
            }
        }
    }
}
