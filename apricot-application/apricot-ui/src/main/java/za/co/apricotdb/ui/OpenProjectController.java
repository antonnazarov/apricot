package za.co.apricotdb.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.handler.ApplicationInitializer;

/**
 * The controller for apricot-project-open.fxml form.
 * 
 * @author Anton Nazarov
 * @since 02/02/2019
 *
 */
@Component
public class OpenProjectController {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    ParentWindow parentWindow;

    @FXML
    Pane mainPane;

    @FXML
    TableView<ApricotProject> projectsList;

    @FXML
    TextArea projectDescription;

    @FXML
    TableColumn<ApricotProject, String> projectNameColumn;

    @FXML
    TableColumn<ApricotProject, String> dbTypeColumn;

    @FXML
    TableColumn<ApricotProject, String> createdColumn;

    private Pane mainAppPane;

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void openProject(ActionEvent event) {
        parentWindow.setParentPane(mainAppPane);
        ApricotProject selectedProject = projectsList.getSelectionModel().getSelectedItem();
        projectManager.setProjectCurrent(selectedProject);
        applicationInitializer.initializeDefault();

        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    public void init(Pane mainAppPane) {
        this.mainAppPane = mainAppPane;

        ApricotProject currentProject = projectManager.findCurrentProject();
        List<ApricotProject> projects = projectManager.getAllProjects();
        for (ApricotProject p : projects) {
            if (p.getName().equals(currentProject.getName())) {
                int idx = projects.indexOf(p);
                projects.remove(p);
                projects.add(idx, currentProject);
                break;
            }
        }
        projectsList.getItems().addAll(projects);
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<ApricotProject, String>("name"));
        dbTypeColumn.setCellValueFactory(new PropertyValueFactory<ApricotProject, String>("targetDatabase"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<ApricotProject, String>("formattedCreatedDate"));

        projectsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                projectDescription.setText(newSelection.getDescription());
            }
        });

        // handle the double click on the project name in the list
        projectsList.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                openProject(new ActionEvent());
            }
        });

        projectsList.getSelectionModel().select(currentProject);
        projectDescription.setText(currentProject.getDescription());
    }
}
