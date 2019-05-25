package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.relationship.RelationshipType;

/**
 * Builder of the graphical primitives used in the entity link representation.
 *
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public interface PrimitivesBuilder {

    Group getStart(RelationshipType type);

    Shape getRuler();

    Group getEnd(RelationshipType type);

    void addStartElement(RelationshipType type, Point2D parentStart, Side parentSide, ApricotRelationshipShape shape);

    void addEndElement(RelationshipType type, Point2D childEnd, Side childSide, ApricotRelationshipShape shape);

    public static PrimitivesBuilder instantiateBuilder(ApricotCanvas canvas) {
        PrimitivesBuilder builder = null;
        switch (canvas.getErdNotation()) {
        case "IDEF1x":
            builder = new Idef1xPrimitivesBuilder();
            break;
        case "CROWS_FOOT":
            builder = new CrowsFootPrimitivesBuilder();
            break;
        }

        return builder;
    }
}
