package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.notification.RelationshipContextMenuEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;

public class RelationshipOnMousePressedEventHandler implements EventHandler<MouseEvent> {

    private ApricotCanvas canvas;

    public RelationshipOnMousePressedEventHandler(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() instanceof ApricotRelationshipShape) {
            ApricotRelationshipShape shape = (ApricotRelationshipShape) event.getSource();
            ApricotElement relationship = shape.getElement();
            if (relationship.getElementStatus() != ElementStatus.GRAYED) {
                relationship.setElementStatus(ElementStatus.SELECTED);
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                RelationshipContextMenuEvent cmEvent = new RelationshipContextMenuEvent((ApricotRelationship) relationship,
                        event.getScreenX(), event.getScreenY());
                canvas.publishEvent(cmEvent);
            }

            event.consume();
        }
    }
}
