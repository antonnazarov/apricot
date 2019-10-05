package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.notification.EntityStatusChangedEvent;

/**
 * This functionality is related to the selection of the View on the View Tabs.
 * 
 * @author Anton Nazarov
 * @since 05/10/2019
 */
@Component
public class SelectViewTabHandler {

    @Autowired
    ViewManager viewManager;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    CanvasScaleHandler scaleHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void initTabPane(TabPane viewsTabPane, ComboBox<String> scale) {
        viewsTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                if (t1 != null) {
                    TabInfoObject tabInfo = TabInfoObject.getTabInfo(t1);
                    viewManager.setCurrentView(tabInfo.getView());
                    treeViewHandler.markEntitiesIncludedIntoView(tabInfo.getView());
                    scaleHandler.resetScaleIndicator(scale);

                    // notify listener that the tab has been changed and there is possible change in
                    // the entities selection
                    EntityStatusChangedEvent ev = new EntityStatusChangedEvent(tabInfo.getCanvas());
                    applicationEventPublisher.publishEvent(ev);
                }

                // reset the current undo layout
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> undoManager.resetCurrentLayout());
                delay.play();
            }
        });
    }
}
