package za.co.apricotdb.viewport.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * A generic interface for event builders of different sort.
 * 
 * @author Anton Nazarov
 * @since 10/11/2018
 */
public interface EventBuilder {
    EventHandler<MouseEvent> getOnMousePressedEventHandler(String entityId);

    EventHandler<MouseEvent> getOnMouseDraggedEventHandler(Stage primaryStage, String entityId);

    EventHandler<MouseEvent> getOnMouseReleasedEventHandler(Stage primaryStage, String entityId);

    EventHandler<MouseEvent> getOnMouseMovedEventHandler(Stage primaryStage, String entityId);

    EventHandler<MouseEvent> getOnMouseExitedEventHandler(Stage primaryStage);
}
