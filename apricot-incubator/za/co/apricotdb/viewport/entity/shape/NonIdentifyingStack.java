package za.co.apricotdb.viewport.entity.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class NonIdentifyingStack extends PrimaryKeyStack {

    private final Side side;

    public NonIdentifyingStack(ApricotEntityShape entityShape, Side side) {
        super(entityShape);
        this.side = side;
    }

    @Override
    public void sortRelationships() {
        // TODO Auto-generated method stub

    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public Point2D getRelationshipStart(ApricotRelationship relationship) {
        Point2D ret = null;
        int idx = relationships.indexOf(relationship);
        if (idx >= 0) {
            Point2D stackStart = getStackStartingPoint();
            double y = (idx + 1) * STACK_PARTICLE_LENGTH;
            double x = 0;
            if (side == side.LEFT) {
                x = stackStart.getX() - STACK_PARTICLE_LENGTH;
            } else {
                x = stackStart.getX() + STACK_PARTICLE_LENGTH;
            }
            
            ret = new Point2D(x, y);
        }

        return ret;
    }

    private Point2D getStackStartingPoint() {
        double X = 0;
        if (side == side.LEFT) {
            X = entityShape.getLayoutX();
        } else {
            X = entityShape.getLayoutX() + entityShape.getWidth();
        }
        Point2D ret = new Point2D(X, getPrimaryKeyY());

        return ret;
    }

    private double getPrimaryKeyY() {
        double ret = 0;
        if (!relationships.isEmpty()) {
            ApricotRelationship r = relationships.get(0);

            String pKey = r.getPrimaryKeyName();
            ret = entityShape.getLayoutY() + entityShape.getFieldLocalY(pKey);
        }

        return ret;
    }

    @Override
    public void build() {
        this.getElements().clear();
        
        Point2D start = getStackStartingPoint(); 
        this.getElements().add(new MoveTo(start.getX(), start.getY()));
        if (side == side.LEFT) {
            this.getElements().add(new HLineTo(start.getX()-STACK_PARTICLE_LENGTH));
        } else {
            this.getElements().add(new HLineTo(start.getX()+STACK_PARTICLE_LENGTH));
        }
        
        this.getElements().add(new VLineTo(start.getY() + relationships.size() * STACK_PARTICLE_LENGTH));
    }
}
