package za.co.apricotdb.viewport.entity.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class IdentifyingStack extends PrimaryKeyStack {

    public IdentifyingStack(ApricotEntityShape entityShape) {
        super(entityShape);
    }

    @Override
    public Side getSide() {
        return Side.TOP;
    }

    @Override
    public void sortRelationships() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Point2D getRelationshipStart(ApricotRelationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void build() {
        // TODO Auto-generated method stub
        
    }
}
