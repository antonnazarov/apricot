package za.co.apricotdb.ui.map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.event.DragInitPosition;

/**
 * The handler of the mouse pressed event anywhere on the Active Frame.
 *
 * @author Anton Nazarov
 * @since 01/12/2020
 */
@Component
public class ActiveFrameOnMousePressedHandler implements EventHandler<MouseEvent> {

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    @Override
    public void handle(MouseEvent mouseEvent) {

        Pane activeFrame = (Pane) mouseEvent.getSource();
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            //  register the initial position as UserData in the activeFrame object
            DragInitPosition initPosition = new DragInitPosition(mouseEvent.getSceneX(), mouseEvent.getSceneY(), activeFrame.getTranslateX(),
                    activeFrame.getTranslateY());
            activeFrame.setUserData(initPosition);
            activeFrameHandler.setDragging(true);
        }
    }
}
