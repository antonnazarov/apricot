package za.co.apricotdb.viewport.relationship.shape;

import java.util.Properties;

import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.ElementType;
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
    
    @Override
    public CanvasAllocationItem getAllocation() {
        CanvasAllocationItem ret = new CanvasAllocationItem();
        ApricotRelationship r = (ApricotRelationship) getElement();
        ret.setName(r.getRelationshipName());
        ret.setType(ElementType.RELATIONSHIP);

        Properties props = new Properties();
        props.setProperty("relationshipType", RelationshipShapeType.DIRECT.toString());
        props.setProperty("rulerX", String.valueOf(rulerX));
        ret.setProperties(props);

        return ret;
    }
    
    @Override
    public void applyAllocation(CanvasAllocationItem item) {
        if (item.getProperties() != null) {
            String relationshipType = item.getProperties().getProperty("relationshipType");
            if (!RelationshipShapeType.DIRECT.toString().equals(relationshipType)) {
                return;
            }
        }
        
        ApricotRelationship r = (ApricotRelationship) getElement();
        if (item.getName().equals(r.getRelationshipName()) && item.getType() == ElementType.RELATIONSHIP) {
            double rulerX = Double.parseDouble(item.getProperties().getProperty("rulerX"));
            this.rulerX = rulerX;
        }
    }
}
