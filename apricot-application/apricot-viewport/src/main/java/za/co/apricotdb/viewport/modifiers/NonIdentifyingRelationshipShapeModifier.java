package za.co.apricotdb.viewport.modifiers;

import javafx.scene.shape.Path;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;

/**
 * Decorator of the non identifying relationship shape.
 *  
 * @author Anton Nazarov
 * @since 29/12/2018
 */
public class NonIdentifyingRelationshipShapeModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotShape shape) {
        if (shape instanceof ApricotRelationshipShape) {
            ApricotRelationshipShape relationshipShape = (ApricotRelationshipShape) shape;
            ApricotRelationship r = (ApricotRelationship) relationshipShape.getElement();
            if (r.getRelationshipType() != RelationshipType.IDENTIFYING) {
                Path path = relationshipShape.getPath();
                path.getStrokeDashArray().addAll(5d, 5d);
            }
        }
    }
}
