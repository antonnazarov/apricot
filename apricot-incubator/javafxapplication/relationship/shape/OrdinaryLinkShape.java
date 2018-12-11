package javafxapplication.relationship.shape;

import javafx.scene.shape.Shape;
import javafxapplication.relationship.ApricotEntityLink;
import javafxapplication.relationship.RelationshipType;

/**
 * This is an extension of the Group class with the parameters, specific for the
 * Ordinary Link.
 * 
 * @author Anton Nazarov
 */
public class OrdinaryLinkShape extends ApricotLinkShape {

    private Shape rulerX;

    public OrdinaryLinkShape(Shape polyline, Shape startSign, Shape endSign, Shape rulerX, ApricotEntityLink link) {
        super(polyline, startSign, endSign, rulerX);

        this.polyline = polyline;
        this.startSign = startSign;
        this.endSign = endSign;
        this.rulerX = rulerX;

        this.type = link.getRelationshipType();
    }

    @Override
    public void setSelected(boolean selected) {

    }

    @Override
    public ApricotEntityLink getEntityLink() {
        return link;
    }

    public Shape getPolyline() {
        return polyline;
    }

    public Shape getStartSign() {
        return startSign;
    }

    public Shape getEndSign() {
        return endSign;
    }

    public Shape getRulerX() {
        return rulerX;
    }
}
