package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.relationship.shape.ApricotRelationshipShape;
import za.co.apricotdb.viewport.relationship.shape.DadsHandRelationship;
import za.co.apricotdb.viewport.relationship.shape.DirectRelationship;
import za.co.apricotdb.viewport.relationship.shape.HatRelationship;
import za.co.apricotdb.viewport.relationship.shape.RoofRelationship;

public class RelationshipOnMousePressedRulerEventHandler implements EventHandler<MouseEvent> {

    private final String rulerId;

    public RelationshipOnMousePressedRulerEventHandler(String rulerId) {
        this.rulerId = rulerId;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Shape ruler = (Shape) event.getSource();

            if (ruler.getParent() instanceof ApricotRelationshipShape) {
                double initRuler = 0;
                ApricotRelationshipShape shape = (ApricotRelationshipShape) ruler.getParent();
                switch (shape.getShapeType()) {
                case DIRECT:
                    DirectRelationship rShape = (DirectRelationship) ruler.getParent();
                    initRuler = rShape.getRulerX();
                    break;
                case HAT:
                    HatRelationship hShape = (HatRelationship) ruler.getParent();
                    if (rulerId.equals("leftRulerX")) {
                        initRuler = hShape.getLeftRulerX();
                    } else if (rulerId.equals("rightRulerX")) {
                        initRuler = hShape.getRightRulerX();
                    } else if (rulerId.equals("centerRulerY")) {
                        initRuler = hShape.getCenterRulerY();
                    }
                    break;
                case DADS_HAND:
                    DadsHandRelationship dShape = (DadsHandRelationship) ruler.getParent();
                    initRuler = dShape.getRulerX();
                    break;
                case ROOF:
                    RoofRelationship roShape = (RoofRelationship) ruler.getParent();
                    initRuler = roShape.getRulerY();
                    break;
                }
                
                DragInitPosition pos = new DragInitPosition(event.getSceneX(), event.getSceneY(), 0, 0);
                pos.setInitRuler(initRuler);
                ruler.setUserData(pos);

                event.consume();
            }
        }
    }
}
