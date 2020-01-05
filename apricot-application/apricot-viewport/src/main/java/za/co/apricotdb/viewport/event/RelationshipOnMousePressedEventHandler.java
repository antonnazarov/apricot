package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;

public class RelationshipOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotRelationshipShape && event.getButton() == MouseButton.PRIMARY) {
            ApricotRelationshipShape shape = (ApricotRelationshipShape) event.getSource();
            ApricotElement relationship = shape.getElement();
            if (relationship.getElementStatus() != ElementStatus.GRAYED) {
                relationship.setElementStatus(ElementStatus.SELECTED);
            }

            event.consume();
        }
    }
}
