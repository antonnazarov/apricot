package javafxapplication.event;

import javafx.scene.Node;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ElementStatus;
import javafxapplication.canvas.ElementType;

public class GroupOperationHandler {

    public void setEntityTranslatePosition(ApricotCanvas canvas, double translateX, double translateY, ElementStatus elementStatus) {
        for (ApricotElement element : canvas.getElements()) {
            if (element.getElementType() == ElementType.ENTITY && element.getElementStatus() == elementStatus) {
                Node shape = element.getShape();
                shape.setTranslateX(translateX);
                shape.setTranslateY(translateY);
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
}
