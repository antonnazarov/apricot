package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafxapplication.align.AlignCommand;
import javafxapplication.align.CanvasSizeAjustor;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.entity.shape.ApricotEntityShape;

public class EntityOnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private String tableName = null;
    private ApricotCanvas canvas = null;
    private AlignCommand aligner = null;

    public EntityOnMouseReleasedEventHandler(String tableName, ApricotCanvas canvas) {
        this.tableName = tableName;
        this.canvas = canvas;
        aligner = new CanvasSizeAjustor(canvas);
    }
    
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotEntityShape) {
            ApricotEntityShape entityShape = (ApricotEntityShape) event.getSource();
            if (tableName.equals(entityShape.getId())) {
                aligner.align();
                
                event.consume();
            }
        }
    }
}
