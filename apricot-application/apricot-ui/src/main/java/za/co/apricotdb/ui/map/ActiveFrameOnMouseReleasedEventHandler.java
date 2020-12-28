package za.co.apricotdb.ui.map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The handler of the Mouse Release Event on the Active Frame.
 *
 * @author Anton Nazarov
 * @since 03/12/2020
 */
@Component
public class ActiveFrameOnMouseReleasedEventHandler implements EventHandler<MouseEvent>  {

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    @Override
    public void handle(MouseEvent mouseEvent) {
        Pane activeFrame = (Pane) mouseEvent.getSource();
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            activeFrame.setLayoutX(activeFrame.getLayoutX() + activeFrame.getTranslateX());
            activeFrame.setTranslateX(0);

            activeFrame.setLayoutY(activeFrame.getLayoutY() + activeFrame.getTranslateY());
            activeFrame.setTranslateY(0);

            activeFrameHandler.setDragging(false);
        }
    }
}
