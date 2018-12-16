package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafxapplication.align.AlignCommand;
import javafxapplication.align.CanvasSizeAjustor;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.entity.shape.ApricotEntityShape;

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
