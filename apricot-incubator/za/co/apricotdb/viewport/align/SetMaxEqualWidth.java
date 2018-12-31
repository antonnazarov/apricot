package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

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
                ApricotEntity entity = (ApricotEntity) element;
                ApricotEntityShape entityShape = entity.getEntityShape(); 
                
                if (maxWidth < entityShape.getWidth()) {
                    maxWidth = entityShape.getWidth();
                }
                
                entities.add(entityShape);
            }
        }
        
        for (VBox entity : entities) {
            entity.setPrefWidth(maxWidth);
        }
        
        PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
        delay.setOnFinished(event -> canvas.buildRelationships());
        delay.play();
    }
}
