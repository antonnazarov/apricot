package za.co.apricotdb.viewport.relationship.shape;

import java.util.Properties;

import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.ElementType;
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
    
    @Override
    public CanvasAllocationItem getAllocation() {
        CanvasAllocationItem ret = new CanvasAllocationItem();
        ApricotRelationship r = (ApricotRelationship) getElement();
        ret.setName(r.getRelationshipName());
        ret.setType(ElementType.RELATIONSHIP);

        Properties props = new Properties();
        props.setProperty("relationshipType", RelationshipShapeType.ROOF.toString());
        props.setProperty("rulerY", String.valueOf(rulerY));
        ret.setProperties(props);

        return ret;
    }

    @Override
    public void applyAllocation(CanvasAllocationItem item) {
        if (item.getProperties() != null) {
            String relationshipType = item.getProperties().getProperty("relationshipType");
            if (!RelationshipShapeType.ROOF.toString().equals(relationshipType)) {
                return;
            }
        }
        
        ApricotRelationship r = (ApricotRelationship) getElement();
        if (item.getName().equals(r.getRelationshipName()) && item.getType() == ElementType.RELATIONSHIP) {
            double rulerY = Double.parseDouble(item.getProperties().getProperty("rulerY"));
            this.rulerY = rulerY;
        }
    }
}
