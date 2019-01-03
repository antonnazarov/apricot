package za.co.apricotdb.viewport.entity.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This implementation of the PrimaryKeyStack is for presentation of the identifying relationships,
 * which always been driven via the stack.  
 * 
 * @author Anton Nazarov
 * @since 31/12/2018
 */
public class IdentifyingStack extends PrimaryKeyStack {
    
    public static final double STARTING_POINT_BIAS = 28;
    public static final double STACK_VERTICAL_BIAS = 12;

    public IdentifyingStack(ApricotEntityShape entityShape) {
        super(entityShape);
    }

    @Override
    public Side getSide() {
        return Side.TOP;
    }

    @Override
    public void sortRelationships() {
        //  Collections.sort(relationships, new RelationshipInStackComparator());
    }

    @Override
    public Point2D getRelationshipStart(ApricotRelationship relationship) {
        Point2D ret = null;
        int idx = relationships.indexOf(relationship);
        if (idx >= 0) {
            double X = entityShape.getLayoutX() + entityShape.getTranslateX() + entityShape.getWidth() - STACK_PARTICLE_LENGTH*(idx+1);
            double Y = entityShape.getLayoutY() + entityShape.getTranslateY() + STACK_VERTICAL_BIAS;
            ret = new Point2D(X, Y);
        }
        
        return ret;
    }
    
    @Override
    public void build() {
        this.getElements().clear();
        
        double startX = entityShape.getLayoutX() + entityShape.getTranslateX() + entityShape.getWidth();
        double startY = entityShape.getLayoutY() + entityShape.getTranslateY() + STARTING_POINT_BIAS;
        
        this.getElements().add(new MoveTo(startX, startY));
        this.getElements().add(new VLineTo(entityShape.getLayoutY() + entityShape.getTranslateY() + STACK_VERTICAL_BIAS));
        this.getElements().add(new HLineTo(startX - STACK_PARTICLE_LENGTH * (relationships.size()+1)));
    }

    @Override
    public boolean hasRelationships() {
        return relationships.size() >= 1;
    }
}
