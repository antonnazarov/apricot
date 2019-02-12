package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.model.SnapshotFormModel;

@Component
public class EditSnapshotController {
    
    @FXML
    TextField snapshotName;

    @FXML
    TextArea snapshotDescription;
    
    @FXML
    RadioButton initFromSnapOption;

    @FXML
    RadioButton initEmptyOption;
    
    @FXML
    ComboBox<String> initFromSnapCombo;
    
    @FXML
    Pane mainPane;
   
    public void init(SnapshotFormModel model) {
        if (model.isNewSnapshot()) {
            final ToggleGroup group = new ToggleGroup();
            initFromSnapOption.setToggleGroup(group);
            initEmptyOption.setToggleGroup(group);
            group.selectToggle(initEmptyOption);
        } else {
            initFromSnapOption.setDisable(true);
            initEmptyOption.setDisable(true);
            initFromSnapCombo.setDisable(true);
        }

        
        if (model.isNewView()) {
            final ToggleGroup group = new ToggleGroup();
            initEmptyOption.setToggleGroup(group);
            initSelectedOption.setToggleGroup(group);
        }

        
    }
    
    @FXML
    public void initFromSnapOptionSelected(ActionEvent event) {
        
    }
    
    @FXML
    public void initEmptyOptionSelected(ActionEvent event) {
        
    }
    
    @FXML
    public void initFromSnapList() {
        
    }
    
    @FXML
    public void reverseDatabase(ActionEvent event) {
        
    }
    
    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }
    
    @FXML
    public void save(ActionEvent event) {
        
    }
    
    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
    
    private void applyModel(SnapshotFormModel model) {
        snapshotName.setText(model.getSnapshotName());
        snapshotDescription.setText(model.getSnapshotDescription());
    }
}
