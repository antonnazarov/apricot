package za.co.apricotdb.viewport.modifiers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.event.RelationshipOnMouseDraggedHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseDraggedVerticalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseEnteredHorizontalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseEnteredVerticalRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseExitedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMousePressedRulerEventHandler;
import za.co.apricotdb.viewport.event.RelationshipOnMouseReleasedEventHandler;
import za.co.apricotdb.viewport.relationship.shape.HatRelationship;

public class HatRelationshipEventModifier implements ElementVisualModifier {

    private EventHandler<MouseEvent> mousePressedHandler = null;

    private EventHandler<MouseEvent> mouseHRulerEnteredHandler = null;
    private EventHandler<MouseEvent> mouseVRulerEnteredHandler = null;
    private EventHandler<MouseEvent> mouseRulerExitedHandler = null;
    private EventHandler<MouseEvent> mouseLeftRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseRightRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseCenterRulerPressedHandler = null;
    private EventHandler<MouseEvent> mouseLeftRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mouseRightRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mouseVRulerDraggedHandler = null;
    private EventHandler<MouseEvent> mouseRulerReleasedHandler = null;

    public HatRelationshipEventModifier(ApricotCanvas canvas, AlignCommand aligner) {
        mousePressedHandler = new RelationshipOnMousePressedEventHandler();

        mouseHRulerEnteredHandler = new RelationshipOnMouseEnteredHorizontalRulerEventHandler(canvas);
        mouseVRulerEnteredHandler = new RelationshipOnMouseEnteredVerticalRulerEventHandler(canvas);
        mouseRulerExitedHandler = new RelationshipOnMouseExitedRulerEventHandler(canvas);
        mouseLeftRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("leftRulerX");
        mouseRightRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("rightRulerX");
        mouseCenterRulerPressedHandler = new RelationshipOnMousePressedRulerEventHandler("centerRulerY");
        mouseLeftRulerDraggedHandler = new RelationshipOnMouseDraggedHorizontalRulerEventHandler("leftRulerX", canvas);
        mouseRightRulerDraggedHandler = new RelationshipOnMouseDraggedHorizontalRulerEventHandler("rightRulerX",
                canvas);
        mouseVRulerDraggedHandler = new RelationshipOnMouseDraggedVerticalRulerEventHandler("centerRulerY", canvas);
        mouseRulerReleasedHandler = new RelationshipOnMouseReleasedEventHandler(canvas, aligner);
    }

    @Override
    public void modify(ApricotShape shape) {
        HatRelationship relationship = (HatRelationship) shape;
        relationship.setOnMousePressed(mousePressedHandler);

        relationship.getLeftRuler().setOnMouseEntered(mouseHRulerEnteredHandler);
        relationship.getLeftRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getLeftRuler().setOnMousePressed(mouseLeftRulerPressedHandler);
        relationship.getLeftRuler().setOnMouseDragged(mouseLeftRulerDraggedHandler);
        relationship.getLeftRuler().setOnMouseReleased(mouseRulerReleasedHandler);

        relationship.getRightRuler().setOnMouseEntered(mouseHRulerEnteredHandler);
        relationship.getRightRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getRightRuler().setOnMousePressed(mouseRightRulerPressedHandler);
        relationship.getRightRuler().setOnMouseDragged(mouseRightRulerDraggedHandler);
        relationship.getRightRuler().setOnMouseReleased(mouseRulerReleasedHandler);

        relationship.getCenterRuler().setOnMouseEntered(mouseVRulerEnteredHandler);
        relationship.getCenterRuler().setOnMouseExited(mouseRulerExitedHandler);
        relationship.getCenterRuler().setOnMousePressed(mouseCenterRulerPressedHandler);
        relationship.getCenterRuler().setOnMouseDragged(mouseVRulerDraggedHandler);
        relationship.getCenterRuler().setOnMouseReleased(mouseRulerReleasedHandler);
    }
}
