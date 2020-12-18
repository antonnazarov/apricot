package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
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
            handle(canvas, relationship, (event.getButton() == MouseButton.SECONDARY),
                    event.getScreenX(), event.getScreenY());

            event.consume();
        }
    }

    public void handle(ApricotCanvas canvas, ApricotElement relationship, boolean secondary, double x, double y) {
        if (relationship.getElementStatus() != ElementStatus.GRAYED) {
            relationship.setElementStatus(ElementStatus.SELECTED);

            ApricotRelationship rl = (ApricotRelationship) relationship;
            ApricotEntity parent = rl.getParent();
            parent.setSelectPrimaryRelationshipsFlag(false);
            parent.setElementStatus(ElementStatus.SELECTED);
            parent.setSelectPrimaryRelationshipsFlag(true);

            ApricotEntity child = rl.getChild();
            if (!child.equals(parent)) {
                child.setSelectPrimaryRelationshipsFlag(false);
                child.setElementStatus(ElementStatus.SELECTED);
                child.setSelectPrimaryRelationshipsFlag(true);
            }
        }

        if (secondary) {
            RelationshipContextMenuEvent cmEvent = new RelationshipContextMenuEvent((ApricotRelationship) relationship,
                    x, y);
            canvas.publishEvent(cmEvent);
        }
    }
}
