package za.co.apricotdb.ui.handler;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
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

    private static final Logger logger = LoggerFactory.getLogger(SelectViewTabHandler.class);

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
                long start = System.currentTimeMillis();
                if (t1 != null && t != null) {
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() {
                            TabInfoObject tabInfo = TabInfoObject.getTabInfo(t1);
                            viewManager.setCurrentView(tabInfo.getView());

                            Platform.runLater(() -> {
                                treeViewHandler.markEntitiesIncludedIntoView(tabInfo.getView());
                                treeViewHandler.sortEntitiesByView();
                                scaleHandler.resetScaleIndicator(scale);

                                // notify listener that the tab has been changed and there is possible change in
                                // the entities selection
                                EntityStatusChangedEvent ev = new EntityStatusChangedEvent(tabInfo.getCanvas());
                                applicationEventPublisher.publishEvent(ev);
                            });

                            return null;
                        }
                    };
                    new Thread(task).start();
                }

                // reset the current undo layout
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> undoManager.resetCurrentLayout());
                delay.play();

                long end = System.currentTimeMillis();
                logger.info("The current tab was changed in " + (end - start) + " ms");
            }
        });
    }
}
