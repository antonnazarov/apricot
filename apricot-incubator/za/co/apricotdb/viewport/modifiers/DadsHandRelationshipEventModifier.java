package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.relationship.shape.DadsHandRelationship;

public class DadsHandRelationshipEventModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotShape shape) {
        DadsHandRelationship relationship = (DadsHandRelationship) shape; 
        relationship.setOnMousePressed(new RelationshipOnMousePressedEventHandler());

        
    }

}
