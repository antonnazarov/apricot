package javafxapplication.relationship;

import javafxapplication.entity.ApricotBBBEntity;

/**
 * Implementation of the Ordinary Link.
 *
 * @author Anton Nazarov
 * @since 22/11/2018
 */
public class ApricotOrdinaryLink extends ApricotBasicEntityLink {

    private double middleStepLayoutX;

    public ApricotOrdinaryLink(ApricotBBBEntity parent, ApricotBBBEntity child, double primaryFieldLayoutY,
            double foreignFieldLayoutY, RelationshipType type) {
        super(parent, child, primaryFieldLayoutY, foreignFieldLayoutY, type);
    }

    @Override
    public EntityLinkBuilder getLinkBuilder() {
        return new OrdinaryLinkBuilder();
    }

    public double getMiddleStepLayoutX() {
        return middleStepLayoutX;
    }

    public void setMiddleStepLayoutX(double middleStepLayoutX) {
        this.middleStepLayoutX = middleStepLayoutX;
    }

    @Override
    public RelationshipType getRelationshipType() {
        return type;
    }
}
