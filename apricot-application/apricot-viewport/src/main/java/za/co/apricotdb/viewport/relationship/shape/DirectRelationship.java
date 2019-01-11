package za.co.apricotdb.viewport.relationship.shape;

import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

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
    
    @Override
    public void setDefault() {
        super.setDefault();
        ruler.setVisible(false);
    }

    @Override
    public void setSelected() {
        super.setSelected();
        ruler.setVisible(true);
    }

    @Override
    public void translateRelationshipRulers(double translateX, double translateY) {
        if (this.getUserData() == null) {
            this.setUserData(rulerX);
            rulerX += translateX;
        } else {
            double initRulerX = (double)this.getUserData();
            rulerX = initRulerX + translateX;
        }
    }

    @Override
    public void resetRelationshipRulers() {
        this.setUserData(null);
    }
}
