package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafxapplication.relationship.ApricotRelationship;

/**
 * The main implementation of the RelationshipShapeBuilder. It mainly detects
 * which type of the link must be created.
 * 
 * @author Anton Nazarov
 * @since 18/12/2018
 */
public abstract class RelationshipShapeBuilderImpl implements RelationshipShapeBuilder {

    protected RelationshipPrimitivesBuilder primitivesBuilder = null;

    public RelationshipShapeBuilderImpl(RelationshipPrimitivesBuilder primitivesBuilder) {
        this.primitivesBuilder = primitivesBuilder;
    }
    
    protected abstract Point2D getParentStart(ApricotRelationship relationship);
    
    protected abstract Point2D getChildEnd(ApricotRelationship relationship);
}
