package za.co.apricotdb.viewport.modifiers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMouseDraggedHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseEnteredHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseExitedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedRulerEventHandler;
import za.co.apricotdb.viewport.relationship.shape.DirectRelationship;

public class DirectRelationshipEventModifier implements ElementVisualModifier {

    private EventHandler<MouseEvent> mouseRulerEnteredHandler = null;
    private EventHandler<MouseEvent> mouseRulerExitedHandler = null;
    private EventHandler<MouseEvent> mouseRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mousePressedHandler = null;

    public DirectRelationshipEventModifier(ApricotCanvas canvas) {
        mouseRulerEnteredHandler = new RelationshipOnMouseEnteredHorizontalRulerEventHandler(canvas);
        mouseRulerExitedHandler = new RelationshipOnMouseExitedRulerEventHandler(canvas);
        mouseRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("RulerX");
        mouseRulerDraggedHandler = new RelationshipOnMouseDraggedHorizontalRulerEventHandler("RulerX", canvas);
        mousePressedHandler = new RelationshipOnMousePressedEventHandler();
    }

    @Override
    public void modify(ApricotShape shape) {
        DirectRelationship relationship = (DirectRelationship) shape;
        relationship.setOnMousePressed(mousePressedHandler);
        relationship.getRuler().setOnMouseEntered(mouseRulerEnteredHandler);
        relationship.getRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getRuler().setOnMousePressed(mouseRulerPressedHandler);
        relationship.getRuler().setOnMouseDragged(mouseRulerDraggedHandler);
    }
}
