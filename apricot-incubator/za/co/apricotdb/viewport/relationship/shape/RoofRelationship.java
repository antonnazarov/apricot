package za.co.apricotdb.viewport.relationship.shape;

import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class RoofRelationship extends ApricotRelationshipShape {

    private double rulerY;
    private Shape ruler;

    public RoofRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.ROOF);
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
            this.setUserData(rulerY);
            rulerY += translateY;
        } else {
            double initRulerY = (double) this.getUserData();
            rulerY = initRulerY + translateY;
        }
    }

    @Override
    public void resetRelationshipRulers() {
        this.setUserData(null);
    }

    public double getRulerY() {
        return rulerY;
    }

    public void setRulerY(double rulerY) {
        this.rulerY = rulerY;
    }
}
