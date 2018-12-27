package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

public class TopologyHelper {
    
    /**
     * Detect, which side is the parent entity.
     */
    public static boolean isParentLeft(ApricotEntity parent, ApricotEntity child) {
        return parent.getShape().getLayoutX() + parent.getShape().getTranslateX() < child.getShape().getLayoutX() + child.getShape().getTranslateX();
    }

    /**
     * Get horizontal distance between entities.
     */
    public static double getHorizontalDistance(ApricotEntity parent, ApricotEntity child) {

        double ret = 0;

        Node np = parent.getShape();
        Node nc = child.getShape();

        if (np instanceof VBox && nc instanceof VBox) {
            VBox parentNode = (VBox) np;
            VBox childNode = (VBox) nc;
            Point2D parentTopLeft = new Point2D(parentNode.getLayoutX()+parentNode.getTranslateX(), 
                    parentNode.getLayoutY()+parentNode.getTranslateY());
            Point2D parentBottomRight = new Point2D(parentNode.getLayoutX()+parentNode.getTranslateX() + parentNode.getWidth(),
                    parentNode.getLayoutY()+parentNode.getLayoutY() + parentNode.getHeight());

            Point2D childTopLeft = new Point2D(childNode.getLayoutX()+childNode.getTranslateX(), childNode.getLayoutY()+childNode.getTranslateY());
            Point2D childBottomRight = new Point2D(childNode.getLayoutX()+childNode.getTranslateX() + childNode.getWidth(),
                    childNode.getLayoutY()+childNode.getTranslateY() + childNode.getHeight());

            Point2D parentMidpoint = parentTopLeft.midpoint(parentBottomRight);
            Point2D childMidpoint = childTopLeft.midpoint(childBottomRight);

            ret = Math.abs(parentMidpoint.getX() - childMidpoint.getX())
                    - (parentNode.getWidth() + childNode.getWidth()) / 2;
        }

        return ret;
    }
    
    public static double getFieldY(ApricotEntity entity, String fieldName) {
        ApricotEntityShape shape = entity.getEntityShape();
        
        double localY = shape.getFieldLocalY(fieldName);
        
        return localY + shape.getLayoutY() + shape.getTranslateY();
    }
}
