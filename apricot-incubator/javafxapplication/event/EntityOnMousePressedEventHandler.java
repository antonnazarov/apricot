package javafxapplication.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The handler of OnMousePressed
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class EntityOnMousePressedEventHandler implements EventHandler<MouseEvent> {
    
    private String tableName = null;
    
    public EntityOnMousePressedEventHandler(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void handle(MouseEvent event) {
        Node sourceNode = (Node) event.getSource();
        event.isControlDown() !!!!!!!
        if (event.getButton() == MouseButton.PRIMARY && tableName.equals(sourceNode.getId())) {
            
        }
    }
}
