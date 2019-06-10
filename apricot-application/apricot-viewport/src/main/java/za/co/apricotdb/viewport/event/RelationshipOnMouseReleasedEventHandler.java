package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.AddLayoutSavepointEvent;

/**
 * Handler of the mouse release on relationship event.
 * 
 * @author Anton Nazarov
 * @since 28/04/2019
 */
public class RelationshipOnMouseReleasedEventHandler implements EventHandler<MouseEvent> {

    private ApricotCanvas canvas = null;
    private AlignCommand aligner = null;

    public RelationshipOnMouseReleasedEventHandler(ApricotCanvas canvas, AlignCommand aligner) {
        this.canvas = canvas;
        this.aligner = aligner;
    }

    @Override
    public void handle(MouseEvent event) {
        Shape ruler = (Shape) event.getSource();
        if (ruler.getUserData() != null && ruler.getUserData() instanceof DragInitPosition) {
            DragInitPosition pos = (DragInitPosition) ruler.getUserData();

            double offsetX = event.getSceneX() - pos.getOrgSceneX();
            double newTranslateX = pos.getOrgTranslateX() + offsetX;
            double offsetY = event.getSceneY() - pos.getOrgSceneY();
            double newTranslateY = pos.getOrgTranslateY() + offsetY;

            if (newTranslateX != 0 || newTranslateY != 0) {
                AddLayoutSavepointEvent e = new AddLayoutSavepointEvent(event);
                canvas.publishEvent(e);
                aligner.align();
            }
        }

        event.consume();
    }
}
