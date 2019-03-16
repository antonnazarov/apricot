package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
import za.co.apricotdb.viewport.relationship.shape.HatRelationship;
import za.co.apricotdb.viewport.relationship.shape.RoofRelationship;

public class RelationshipOnMouseDraggedVerticalRulerEventHandler implements EventHandler<MouseEvent> {

    private final String rulerId;
    private final ApricotCanvas canvas;

    public RelationshipOnMouseDraggedVerticalRulerEventHandler(String rulerId, ApricotCanvas canvas) {
        this.rulerId = rulerId;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        Shape ruler = (Shape) event.getSource();

        if (ruler.getUserData() != null && ruler.getUserData() instanceof DragInitPosition) {
            DragInitPosition pos = (DragInitPosition) ruler.getUserData();
            double offsetY = event.getSceneY() - pos.getOrgSceneY();
            double newTranslateY = pos.getOrgTranslateY() + offsetY;
            if (ruler.getParent() instanceof ApricotRelationshipShape) {
                ApricotRelationshipShape shape = (ApricotRelationshipShape) ruler.getParent();
                switch (shape.getShapeType()) {
                case HAT:
                    HatRelationship hs = (HatRelationship) shape;
                    if (rulerId.equals("centerRulerY")) {
                        hs.setCenterRulerY(pos.getInitRuler() + newTranslateY);
                    }
                    break;
                case ROOF:
                    RoofRelationship rs = (RoofRelationship) shape;
                    rs.setRulerY(pos.getInitRuler() + newTranslateY);
                    break;
                default:
                    break;
                }

                ApricotElement element = shape.getElement();
                element.buildShape();

                Pane pane = (Pane) canvas;
                pane.getScene().setCursor(Cursor.N_RESIZE);

                canvas.publishEvent(new CanvasChangedEvent(canvas));
                
                event.consume();
            }
        }
    }
}
