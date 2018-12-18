package javafxapplication.relationship.shape;

import javafx.scene.shape.Shape;
import javafxapplication.relationship.ApricotRelationship;

/**
 * This is a simple holder of data specific for the Direct Relationsip type.
 * 
 * @author Anton Nazarov
 * @since 18/12/2018
 */
public class DirectRelationship extends ApricotRelationshipShape {

    private double rulerX;
    private Shape ruler;

    public DirectRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.DIRECT);
    }
    
    public double getRulerX() {
        return this.rulerX;
    }
    
    public void setRulerX(double rulerX) {
        this.rulerX = rulerX;
    }

    public Shape getRuler() {
        return ruler;
    }

    public void setRuler(Shape ruler) {
        if (this.ruler != null) {
            this.getChildren().remove(this.ruler);
        }
        this.getChildren().add(ruler);
        this.ruler = ruler;
    }
}
