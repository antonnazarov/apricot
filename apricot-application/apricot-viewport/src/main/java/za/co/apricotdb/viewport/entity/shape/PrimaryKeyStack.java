package za.co.apricotdb.viewport.entity.shape;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;

/**
 * The stack of the multiple (more than one) primary keys on one side.
 * 
 * @author Anton Nazarov
 * @since 24/12/2018
 */
public abstract class PrimaryKeyStack extends Path implements ApricotShape {

    public final static double STACK_PARTICLE_LENGTH = 17;
    public final static double STACK_ENTITY_DISTANCE = 8;

    protected final ApricotEntityShape entityShape;
    protected final List<ApricotRelationship> relationships = new ArrayList<>();
    // the "child" - relationships have been populated in case of the simplified
    // entity representation
    protected final List<ApricotRelationship> childRelationships = new ArrayList<>();

    public PrimaryKeyStack(ApricotEntityShape entityShape) {
        this.entityShape = entityShape;
    }

    @Override
    public ApricotElement getElement() {
        return entityShape.getElement();
    }

    public boolean hasRelationships() {
        return relationships.size() + childRelationships.size() > 1;
    }

    public void addRelationship(ApricotRelationship relationship) {
        relationships.add(relationship);
    }

    public void addChildRelationship(ApricotRelationship relationship) {
        childRelationships.add(relationship);
    }

    public void clear() {
        relationships.clear();
        childRelationships.clear();
    }

    public abstract void sortRelationships();

    public abstract Side getSide();

    public abstract Point2D getRelationshipStart(ApricotRelationship relationship);

    public abstract Point2D getRelationshipEnd(ApricotRelationship relationship);

    public abstract void build();

    public void translateStack(double translateX, double translateY) {
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
    }

    public void applyStackPosition() {
        this.setTranslateX(0);
        this.setTranslateY(0);
    }

    @Override
    public void setDefault() {
        this.setStrokeWidth(ApricotRelationshipShape.RELATIONSHIP_DEFAULT_STROKE_WIDTH);
        this.setStroke(Color.BLACK);
    }

    @Override
    public void setSelected() {
        this.setStrokeWidth(ApricotRelationshipShape.RELATIONSHIP_SELECTED_STROKE_WIDTH + 1);
        this.setStroke(Color.BLACK);
    }

    @Override
    public void setGrayed() {
        this.setStrokeWidth(ApricotRelationshipShape.RELATIONSHIP_DEFAULT_STROKE_WIDTH);
        this.setStroke(Color.LIGHTGRAY);
    }

    @Override
    public void setHidden() {
        // TODO Auto-generated method stub
    }

    @Override
    public CanvasAllocationItem getAllocation() {
        return null;
    }

    @Override
    public void applyAllocation(CanvasAllocationItem item) {
    }
}
