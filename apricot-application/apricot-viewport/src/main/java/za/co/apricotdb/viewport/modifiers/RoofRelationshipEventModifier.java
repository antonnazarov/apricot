package za.co.apricotdb.viewport.modifiers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMouseDraggedVerticalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseEnteredVerticalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseExitedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedRulerEventHandler;
import za.co.apricotdb.viewport.relationship.shape.RoofRelationship;

public class RoofRelationshipEventModifier implements ElementVisualModifier {

    private EventHandler<MouseEvent> mouseRulerEnteredHandler = null;
    private EventHandler<MouseEvent> mouseRulerExitedHandler = null;
    private EventHandler<MouseEvent> mouseRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mousePressedHandler = null;

    public RoofRelationshipEventModifier(ApricotCanvas canvas) {
        mouseRulerEnteredHandler = new RelationshipOnMouseEnteredVerticalRulerEventHandler(canvas);
        mouseRulerExitedHandler = new RelationshipOnMouseExitedRulerEventHandler(canvas);
        mouseRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("RulerY");
        mouseRulerDraggedHandler = new RelationshipOnMouseDraggedVerticalRulerEventHandler("RulerY", canvas);
        mousePressedHandler = new RelationshipOnMousePressedEventHandler();
    }

    @Override
    public void modify(ApricotShape shape) {
        RoofRelationship relationship = (RoofRelationship) shape;
        relationship.setOnMousePressed(mousePressedHandler);
        relationship.getRuler().setOnMouseEntered(mouseRulerEnteredHandler);
        relationship.getRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getRuler().setOnMousePressed(mouseRulerPressedHandler);
        relationship.getRuler().setOnMouseDragged(mouseRulerDraggedHandler);
    }
}
