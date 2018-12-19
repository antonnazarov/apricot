package za.co.apricotdb.viewport.event;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class GroupOperationHandler {

    public void setEntityTranslatePosition(ApricotCanvas canvas, double translateX, double translateY, ElementStatus elementStatus) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY && element.getElementStatus() == elementStatus) {
                Node shape = element.getShape();
                shape.setTranslateX(translateX);
                shape.setTranslateY(translateY);
                
                rebuildRelationships((ApricotEntity) element);
            }
        }
    }
    
    public void applyCurrentPosition(ApricotCanvas canvas, ElementStatus elementStatus) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY && element.getElementStatus() == elementStatus) {
                Node shape = element.getShape();
                shape.setLayoutX(shape.getLayoutX() + shape.getTranslateX());
                shape.setLayoutY(shape.getLayoutY() + shape.getTranslateY());
                shape.setTranslateX(0);
                shape.setTranslateY(0);
            }
        }
    }
    
    /**
     * Rebuild all relationships related to the given entity.
     */
    public void rebuildRelationships(ApricotEntity entity) {
        List<ApricotRelationship> relationships = new ArrayList<>();
        relationships.addAll(entity.getPrimaryLinks());
        relationships.addAll(entity.getForeignLinks());
        
        for (ApricotRelationship r : relationships) {
            r.buildShape();
        }
    }
}
