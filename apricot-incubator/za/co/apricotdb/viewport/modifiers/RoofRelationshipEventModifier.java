package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.relationship.shape.RoofRelationship;

public class RoofRelationshipEventModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotShape shape) {
        RoofRelationship relationship = (RoofRelationship) shape; 
        relationship.setOnMousePressed(new RelationshipOnMousePressedEventHandler());
    }
}
