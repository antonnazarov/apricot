package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.ApricotTargetDatabases;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
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

    @FXML
    Pane mainPane;

    @FXML
    TextField projectName;

    @FXML
    TextArea projectDescription;

    @FXML
    ChoiceBox<String> projectDatabase;

    private boolean isCreateNew = false;
    private ProjectFormModel model = null;
    private BorderPane mainBorderPane = null;
    private PropertyChangeListener canvasChangeListener = null;

    @FXML
    public void save(ActionEvent event) {
        parentWindow.setParentPane(mainBorderPane);

        model.setProjectName(projectName.getText());
        model.setProjectDescription(projectDescription.getText());
        model.setProjectDatabase(projectDatabase.getSelectionModel().getSelectedItem());

        if (!projectSerializer.validate(model)) {
            return;
        }

        ApricotProject project = null;
        if (isCreateNew) {
            project = projectSerializer.serializeNewProject(model);
        } else {
            project = projectSerializer.serializeEditedProject(model);
        }

        applicationInitializer.initializeForProject(project, parentWindow, canvasChangeListener);
        if (isCreateNew) {
            projectManager.setProjectCurrent(project);
        }

        getStage().close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    public void init(boolean isCreateNew, ProjectFormModel model, BorderPane mainBorderPane,
            PropertyChangeListener canvasChangeListener) {
        this.isCreateNew = isCreateNew;
        this.model = model;
        this.mainBorderPane = mainBorderPane;
        this.canvasChangeListener = canvasChangeListener;

        // initialize the project target database
        for (ApricotTargetDatabases d : ApricotTargetDatabases.values()) {
            projectDatabase.getItems().add(d.toString());
        }

        projectName.setText(model.getProjectName());
        projectDescription.setText(model.getProjectDescription());
        projectDatabase.getSelectionModel().select(model.getProjectDatabase());
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
