package za.co.apricotdb.viewport.relationship.shape;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class HatRelationship extends ApricotRelationshipShape {

    private double leftRulerX;
    private Shape leftRuler;
    private double rightRulerX;
    private Shape rightRuler;
    private double centerRulerY;
    private Shape centerRuler;

    public HatRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.HAT);
    }

    @Override
    public void translateRelationshipRulers(double translateX, double translateY) {
        if (this.getUserData() == null) {
            Map<String, Double> rulers = new HashMap<>();
            rulers.put("leftRulerX", leftRulerX);
            rulers.put("rightRulerX", rightRulerX);
            rulers.put("centerRulerY", centerRulerY);
            this.setUserData(rulers);
            leftRulerX += translateX;
            rightRulerX += translateX;
            centerRulerY += translateY;
        } else {
            if (this.getUserData() instanceof Map) {
                Map<String, Double> rulers = (Map) this.getUserData();
                leftRulerX = rulers.get("leftRulerX") + translateX;
                rightRulerX = rulers.get("rightRulerX") + translateX;
                centerRulerY = rulers.get("centerRulerY") + translateY;
            }
        }
    }

    @Override
    public void resetRelationshipRulers() {
        this.setUserData(null);
    }

    @Override
    public void setDefault() {
        super.setDefault();
        if (leftRuler != null) {
            leftRuler.setVisible(false);
        }
        if (rightRuler != null) {
            rightRuler.setVisible(false);
        }
        if (centerRuler != null) {
            centerRuler.setVisible(false);
        }
    }

    @Override
    public void setSelected() {
        super.setSelected();
        if (leftRuler != null) {
            leftRuler.setVisible(true);
        }
        if (rightRuler != null) {
            rightRuler.setVisible(true);
        }
        if (centerRuler != null) {
            centerRuler.setVisible(true);
        }
    }

    public void setLeftRuler(Shape ruler) {
        if (this.leftRuler != null) {
            this.getChildren().remove(this.leftRuler);
        }
        this.getChildren().add(ruler);
        this.leftRuler = ruler;
    }

    public void setRightRuler(Shape ruler) {
        if (this.rightRuler != null) {
            this.getChildren().remove(this.rightRuler);
        }
        this.getChildren().add(ruler);
        this.rightRuler = ruler;
    }

    public void setCenterRuler(Shape ruler) {
        if (this.centerRuler != null) {
            this.getChildren().remove(this.centerRuler);
        }
        this.getChildren().add(ruler);
        this.centerRuler = ruler;
    }

    public double getLeftRulerX() {
        return leftRulerX;
    }

    public void setLeftRulerX(double leftRulerX) {
        this.leftRulerX = leftRulerX;
    }

    public double getRightRulerX() {
        return rightRulerX;
    }

    public void setRightRulerX(double rightRulerX) {
        this.rightRulerX = rightRulerX;
    }

    public double getCenterRulerY() {
        return centerRulerY;
    }

    public void setCenterRulerY(double centerRulerY) {
        this.centerRulerY = centerRulerY;
    }

    public Shape getLeftRuler() {
        return leftRuler;
    }

    public Shape getRightRuler() {
        return rightRuler;
    }

    public Shape getCenterRuler() {
        return centerRuler;
    }
}
