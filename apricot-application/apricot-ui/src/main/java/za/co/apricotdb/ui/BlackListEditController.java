package za.co.apricotdb.ui;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.model.BlackListFormModel;
import za.co.apricotdb.ui.model.BlackListFormModelBuilder;

/**
 * The controller of the apricot-blacklist-editor.fxml- form.
 * 
 * @author Anton Nazarov
 * @since 22/02/2019
 */
@Component
public class BlackListEditController {

    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    BlackListHandler blackListHandler;
    
    @Autowired
    BlackListFormModelBuilder blackListFormModelBuilder;

    @FXML
    ListView<String> allTablesList;

    @FXML
    ListView<String> blackListTablesList;
    
    @FXML
    Pane mainPane;
    
    private TextArea blackList;

    public void init(TextArea blackList) {
        this.blackList = blackList;
        
        allTablesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        blackListTablesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        BlackListFormModel model = blackListFormModelBuilder.buildModel();
        applyModel(model);
    }

    @FXML
    public void include(ActionEvent event) {
        List<String> selected = allTablesList.getSelectionModel().getSelectedItems();
        if (selected != null && selected.size() > 0) {
            blackListTablesList.getItems().addAll(selected);
            Collections.sort(blackListTablesList.getItems());
            for (String t : selected) {
                blackListTablesList.getSelectionModel().select(t);
            }
            allTablesList.getItems().removeAll(selected);
        }
    }

    @FXML
    public void exclude(ActionEvent event) {
        List<String> selected = blackListTablesList.getSelectionModel().getSelectedItems();
        if (selected != null && selected.size() > 0) {
            allTablesList.getItems().addAll(selected);
            Collections.sort(allTablesList.getItems());
            for (String t : selected) {
                allTablesList.getSelectionModel().select(t);
            }
            blackListTablesList.getItems().removeAll(selected);
        }
    }

    @FXML
    public void save(ActionEvent event) {
        blackListHandler.saveStringBlackList(projectManager.findCurrentProject(), blackListTablesList.getItems());
        String bl = blackListHandler.getBlackListAsString(projectManager.findCurrentProject());
        blackList.setText(bl);

        getStage().close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    private void applyModel(BlackListFormModel model) {
        allTablesList.getItems().clear();
        blackListTablesList.getItems().clear();

        allTablesList.getItems().addAll(model.getAllTables());
        blackListTablesList.getItems().addAll(model.getBlackListTables());
    }
    
    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
