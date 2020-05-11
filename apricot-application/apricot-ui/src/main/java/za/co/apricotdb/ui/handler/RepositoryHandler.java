package za.co.apricotdb.ui.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.repository.*;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * The Repository related functionality is supported by this component.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryHandler {

    private final Logger logger = LoggerFactory.getLogger(RepositoryHandler.class);

    @Resource
    ApplicationContext context;
    
    @Autowired
    RepositoryRowFactory rowFactory;

    @Autowired
    RepositoryConsistencyService consistencyService;

    @Autowired
    RepositoryConfigHandler configHandler;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepoCompareService compareService;

    @ApricotErrorLogger(title = "Unable to create the Apricot Repository forms")
    @Transactional
    public void showRepositoryForm() {
        if (!checkIfUrlConfigured()) {
            return;
        }
        if (!checkRemoteRepository()) {
            return;
        }

        try {
            localRepoService.initLocalRepo();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to initialize the local repository", ex);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-repository.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Apricot Repository Import/Export");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("repository-small-s.png")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);
        openProjectScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        RepositoryController controller = loader.<RepositoryController>getController();
        // controller.init(generateTestModel());
        RepositoryModel model = compareService.generateModel();
        if (model != null) {
            controller.init(model);
        }

        dialog.show();
    }

    /**
     * Check if the Remote Repository if properly configured.
     */
    private boolean checkIfUrlConfigured() {
        boolean first = true;
        if (!consistencyService.isRemoteRepositoryUrlConfigured()) {
            while (true) {
                if (first) {
                    first = false;
                } else {
                    if (!alertDec.requestYesNoOption("Configure Remote Git Repository",
                            "The URL of the remote Git repository is not configured",
                            "Configure", Alert.AlertType.WARNING)) {
                        return false;
                    }
                }
                configHandler.showRepositoryConfigForm(true);
                if (consistencyService.isRemoteRepositoryUrlConfigured()) {
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkRemoteRepository() {
        while (true) {
            try {
                remoteRepositoryService.checkRemoteRepository(configHandler.getRepositoryConfiguration());
                return true;
            } catch (ApricotRepositoryException ex) {
                if (!alertDec.requestYesNoOption("The Remote Repository Check",
                        "Unable to access the Remote Repository: " + ex.getMessage(),
                        "Configure", Alert.AlertType.WARNING)) {
                    return false;
                } else {
                    configHandler.showRepositoryConfigForm(true);
                }
            }
        }
    }

    private RepositoryModel generateTestModel() {
        RepositoryModel model = new RepositoryModel();

        model.getRows().add(new ModelRow(RowType.PROJECT, true, "Equal Project", "Equal Project"));
        model.getRows().add(new ModelRow(RowType.PROJECT, false, null, "Test Project A"));
        model.getRows().add(new ModelRow(RowType.PROJECT, false, null, "Test Project Imp"));

        model.getRows().add(new ModelRow(RowType.PROJECT, false, "Export Test Project X", null));
        model.getRows().add(new ModelRow(RowType.PROJECT, false, "Export Test Project Y", null));

        ModelRow prj = new ModelRow(RowType.PROJECT, false, "IE Account PRJ", "IE Account PRJ");
        model.getRows().add(prj);
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, null, "IE Snapshot 2"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 1", "Snapshot with changes 1"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Second Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Third Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 2", "Snapshot with changes 2"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 3", "Snapshot with changes 3"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 4", "Snapshot with changes 4"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, true, "Equal Snapshot", "Equal Snapshot"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, null, "IE Snapshot 1"));

        model.getRows().add(new ModelRow(RowType.PROJECT, false, "Test Project Exp W", null));
        model.getRows().add(new ModelRow(RowType.PROJECT, true, "Equal Project", "Equal Project"));

        prj = new ModelRow(RowType.PROJECT, false, "IE Account PRJ2 ", "IE Account PRJ 2");
        model.getRows().add(prj);
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, true, "Equal Snapshot", "Equal Snapshot"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, null, "IE Snapshot 2"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Second Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "My Third Snapshot to export", null));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 2", "Snapshot with changes 2"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 3", "Snapshot with changes 3"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, null, "IE Snapshot 1"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 4", "Snapshot with changes 4"));
        prj.getIncludedItems().add(new ModelRow(RowType.SNAPSHOT, false, "Snapshot with changes 1", "Snapshot with changes 1"));
        
        model.getRows().add(new ModelRow(RowType.PROJECT, true, "This is second equal project", "This is second equal project"));
        model.getRows().add(new ModelRow(RowType.PROJECT, false, null, "Test Project Imp 2"));
        model.getRows().add(new ModelRow(RowType.PROJECT, false, "Test Project Exp Z", null));

        model.sort();
        
        return model;
    }
}
