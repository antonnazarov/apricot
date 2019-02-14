package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.model.ApricotSnapshotSerializer;
import za.co.apricotdb.ui.model.SnapshotFormModel;

@Component
public class EditSnapshotController {
    
    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    ParentWindow parentWindow;
    
    @Autowired
    ApricotSnapshotSerializer snapshotSerializer;
    
    @Autowired
    ApplicationInitializer applicationInitializer;
    
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
    
    private boolean isNewSnapshot;
    private final ToggleGroup group = new ToggleGroup();
    private PropertyChangeListener canvasChangeListener = null;
   
    public void init(SnapshotFormModel model, PropertyChangeListener canvasChangeListener) {
        if (model.isNewSnapshot()) {
            initFromSnapOption.setToggleGroup(group);
            initEmptyOption.setToggleGroup(group);
            group.selectToggle(initEmptyOption);
            
            List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(parentWindow.getApplicationData().getCurrentProject());
            List<String> snps = getSnapshotNames(snapshots);
            initFromSnapCombo.getItems().add("<None>");
            initFromSnapCombo.getItems().addAll(snps);
            initFromSnapCombo.getSelectionModel().select("<None>");
            initFromSnapCombo.setDisable(true);
        } else {
            initFromSnapOption.setDisable(true);
            initEmptyOption.setDisable(true);
            initFromSnapCombo.setDisable(true);
        }
        
        applyModel(model);
        isNewSnapshot = model.isNewSnapshot();
        this.canvasChangeListener = canvasChangeListener;
    }
    
    @FXML
    public void initFromSnapOptionSelected(ActionEvent event) {
        initFromSnapCombo.setDisable(false);
    }
    
    @FXML
    public void initEmptyOptionSelected(ActionEvent event) {
        initFromSnapCombo.setDisable(true);
        initFromSnapCombo.getSelectionModel().select("<None>");
    }
    
    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }
    
    @FXML
    public void save(ActionEvent event) {
        SnapshotFormModel model = new SnapshotFormModel();
        model.setNewSnapshot(isNewSnapshot);
        model.setSnapshotName(snapshotName.getText());
        model.setSnapshotDescription(snapshotDescription.getText());
        
        if (isNewSnapshot) {
            if (group.getSelectedToggle() == initFromSnapOption) {
                String selected = initFromSnapCombo.getSelectionModel().getSelectedItem();
                if (!"<None>".equals(selected)) {
                    model.setInitSourceSnapshot(selected);
                }
            }
        }
        
        ApricotSnapshot snapshot = snapshotSerializer.serializeSnapshot(model);
        applicationInitializer.initializeForProject(snapshot.getProject(), parentWindow, canvasChangeListener);
        
        getStage().close();
    }
    
    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
    
    private void applyModel(SnapshotFormModel model) {
        snapshotName.setText(model.getSnapshotName());
        snapshotDescription.setText(model.getSnapshotDescription());
    }
    
    private List<String> getSnapshotNames(List<ApricotSnapshot> snps) {
        List<String> ret = new ArrayList<>();
        
        for (ApricotSnapshot s : snps) {
            ret.add(s.getName());
        }
        
        return ret;
    }
}
