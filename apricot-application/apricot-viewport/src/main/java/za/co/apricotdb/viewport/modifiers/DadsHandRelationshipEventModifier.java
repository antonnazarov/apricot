package za.co.apricotdb.viewport.modifiers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMouseDraggedHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseEnteredHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseExitedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseReleasedEventHandler;
import za.co.apricotdb.viewport.relationship.shape.DadsHandRelationship;

public class DadsHandRelationshipEventModifier implements ElementVisualModifier {

    private EventHandler<MouseEvent> mouseRulerEnteredHandler = null;
    private EventHandler<MouseEvent> mouseRulerExitedHandler = null;
    private EventHandler<MouseEvent> mouseRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mousePressedHandler = null;
    private EventHandler<MouseEvent> mouseRulerReleasedHandler = null;

    public DadsHandRelationshipEventModifier(ApricotCanvas canvas, AlignCommand aligner) {
        mouseRulerEnteredHandler = new RelationshipOnMouseEnteredHorizontalRulerEventHandler(canvas);
        mouseRulerExitedHandler = new RelationshipOnMouseExitedRulerEventHandler(canvas);
        mouseRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("RulerX");
        mouseRulerDraggedHandler = new RelationshipOnMouseDraggedHorizontalRulerEventHandler("RulerX", canvas);
        mousePressedHandler = new RelationshipOnMousePressedEventHandler(canvas);
        mouseRulerReleasedHandler = new RelationshipOnMouseReleasedEventHandler(canvas, aligner);
    }

    @Override
    public void modify(ApricotShape shape) {
        DadsHandRelationship relationship = (DadsHandRelationship) shape;
        relationship.setOnMousePressed(mousePressedHandler);
        relationship.getRuler().setOnMouseEntered(mouseRulerEnteredHandler);
        relationship.getRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getRuler().setOnMousePressed(mouseRulerPressedHandler);
        relationship.getRuler().setOnMouseDragged(mouseRulerDraggedHandler);
        relationship.getRuler().setOnMouseReleased(mouseRulerReleasedHandler);
    }
}
