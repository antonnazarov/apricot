package za.co.apricotdb.ui.notification;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * The event listener, "listens" by the View Port events.
 * 
 * @author Anton Nazarov
 * @since 25/01/2019
 */
public class CanvasChangeListener implements PropertyChangeListener {

    private TabPane viewsTabPane;
    private Button saveButton;

    public CanvasChangeListener(TabPane viewsTabPane, Button saveButton) {
        this.viewsTabPane = viewsTabPane;
        this.saveButton = saveButton;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        boolean changed = (boolean) event.getNewValue();

        Tab selectedTab = viewsTabPane.getSelectionModel().getSelectedItem();
        if (changed) {
            selectedTab.setStyle("-fx-font-weight: bold;");
            saveButton.setStyle("-fx-font-weight: bold;");
        } else {
            // reset all tabs
            saveButton.setStyle("-fx-font-weight: normal;");
            for (Tab tab : viewsTabPane.getTabs()) {
                tab.setStyle("-fx-font-weight: normal;");
            }
        }
    }
}
