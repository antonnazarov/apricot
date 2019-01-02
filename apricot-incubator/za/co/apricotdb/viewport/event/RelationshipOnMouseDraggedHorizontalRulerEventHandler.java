package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
import za.co.apricotdb.viewport.relationship.shape.DirectRelationship;
import za.co.apricotdb.viewport.relationship.shape.HatRelationship;

public class RelationshipOnMouseDraggedHorizontalRulerEventHandler implements EventHandler<MouseEvent> {

    private final String rulerId;
    private final ApricotCanvas canvas;

    public RelationshipOnMouseDraggedHorizontalRulerEventHandler(String rulerId, ApricotCanvas canvas) {
        this.rulerId = rulerId;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent event) {
        Shape ruler = (Shape) event.getSource();

        if (ruler.getUserData() != null && ruler.getUserData() instanceof DragInitPosition) {
            DragInitPosition pos = (DragInitPosition) ruler.getUserData();
            double offsetX = event.getSceneX() - pos.getOrgSceneX();
            double newTranslateX = pos.getOrgTranslateX() + offsetX;
            if (ruler.getParent() instanceof ApricotRelationshipShape) {
                ApricotRelationshipShape shape = (ApricotRelationshipShape) ruler.getParent();
                switch (shape.getShapeType()) {
                case DIRECT:
                    DirectRelationship ds = (DirectRelationship) shape;
                    ds.setRulerX(pos.getInitRuler() + newTranslateX);
                    break;
                case HAT:
                    HatRelationship hs = (HatRelationship) shape;
                    if (rulerId.equals("leftRulerX")) {
                        hs.setLeftRulerX(pos.getInitRuler() + newTranslateX);
                    } else if (rulerId.equals("rightRulerX")) {
                        hs.setRightRulerX(pos.getInitRuler() + newTranslateX);
                    }
                    break;
                }

                ApricotElement element = shape.getElement();
                element.buildShape();
                
                Pane pane = (Pane) canvas;
                pane.getScene().setCursor(Cursor.E_RESIZE);                

                event.consume();
            }
        }
    }
}
