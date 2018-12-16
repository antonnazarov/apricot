package javafxapplication.align;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.VBox;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ElementType;

public class SetMaxEqualWidth implements AlignCommand {

    private ApricotCanvas canvas = null;
    
    public SetMaxEqualWidth(ApricotCanvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void align() {
        List<VBox> entities = new ArrayList<>();
        double maxWidth = 0;
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY) {
                VBox shape = (VBox) element.getShape();
                
                if (maxWidth < shape.getWidth()) {
                    maxWidth = shape.getWidth();
                }
                
                entities.add(shape);
            }
        }
        
        for (VBox entity : entities) {
            entity.setPrefWidth(maxWidth);
        }
    }
}
