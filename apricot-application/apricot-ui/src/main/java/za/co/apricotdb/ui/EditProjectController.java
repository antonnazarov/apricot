package za.co.apricotdb.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ERDNotation;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.model.ApricotProjectSerializer;
import za.co.apricotdb.ui.model.ProjectFormModel;

/**
 * This controller serves the form apricot-project-editor.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/02/2019
 */
@Component
public class EditProjectController {

    @Autowired
    ApricotProjectSerializer projectSerializer;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @FXML
    Pane mainPane;

    @FXML
    TextField projectName;

    @FXML
    TextArea projectDescription;

    @FXML
    ChoiceBox<String> projectDatabase;

    @FXML
    ChoiceBox<String> erdNotation;

    @FXML
    TextArea blackList;

    private boolean isCreateNew = false;
    private ProjectFormModel model = null;
    private Pane mainAppPane = null;

    @FXML
    public void save(ActionEvent event) {
        parentWindow.setParentPane(mainAppPane);

        model.setProjectName(projectName.getText());
        model.setProjectDescription(projectDescription.getText());
        model.setProjectDatabase(projectDatabase.getSelectionModel().getSelectedItem());
        model.setErdNotation(ERDNotation.parseNotation(erdNotation.getSelectionModel().getSelectedItem()));

        if (projectSerializer.serializeProject(model, isCreateNew)) {
            snapshotHandler.syncronizeSnapshot(true);
            getStage().close();
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    public void init(boolean isCreateNew, ProjectFormModel model, Pane mainAppPane) {
        this.isCreateNew = isCreateNew;
        this.model = model;
        this.mainAppPane = mainAppPane;

        // initialize the project target database
        for (ApricotTargetDatabase d : ApricotTargetDatabase.values()) {
            if (d.isSupported()) {
                projectDatabase.getItems().add(d.getDatabaseName());
            }
        }

        for (ERDNotation n : ERDNotation.values()) {
            erdNotation.getItems().add(n.getDefinition());
        }

        projectName.setText(model.getProjectName());
        projectDescription.setText(model.getProjectDescription());
        projectDatabase.getSelectionModel().select(model.getProjectDatabase());
        erdNotation.getSelectionModel().select(model.getErdNotation().getDefinition());
        if (isCreateNew) {
            blackList.setDisable(true);
        } else {
            blackList.setText(model.getBlackList());
        }
    }

    @FXML
    public void editBlackList(ActionEvent event) {
        try {
            blackListHandler.openEditBlackListForm(blackList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
