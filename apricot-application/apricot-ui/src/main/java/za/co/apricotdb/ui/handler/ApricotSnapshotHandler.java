package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.EditSnapshotController;
import za.co.apricotdb.ui.model.EditSnapshotModelBuilder;
import za.co.apricotdb.ui.model.NewSnapshotModelBuilder;
import za.co.apricotdb.ui.model.SnapshotFormModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

@Component
public class ApricotSnapshotHandler {
    
    @Resource
    ApplicationContext context;
    
    @Autowired
    NewSnapshotModelBuilder newSnapshotModelBuilder;
    
    @Autowired
    EditSnapshotModelBuilder editSnapshotModelBuilder;
    
    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    AlertMessageDecorator alertDecorator;
    
    public void createDefaultSnapshot(ApricotProject project) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ApricotSnapshot snapshot = new ApricotSnapshot("Initial snapshot, created " + df.format(new java.util.Date()),
                new java.util.Date(), null, "The snapshot, created by default", true, project,
                new ArrayList<ApricotTable>());
        project.getSnapshots().add(snapshot);
    }
    
    @Transactional
    public void setDefaultSnapshot(ApricotSnapshot snapshot) {
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(snapshot.getProject());
        for (ApricotSnapshot s : snapshots) {
            s.setDefaultSnapshot(false);
            snapshotManager.saveSnapshot(s);
        }
        
        snapshot.setDefaultSnapshot(true);
        snapshotManager.saveSnapshot(snapshot);
    }

    /**
     * Create the snapshot editing form for a new or existing snapshot.
     */
    public void createEditSnapshotForm(boolean isCreateNew, BorderPane mainBorderPane,
            PropertyChangeListener canvasChangeListener) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-snapshot-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        SnapshotFormModel model = null;
        if (isCreateNew) {
            dialog.setTitle("Create Snapshot");
            model = newSnapshotModelBuilder.buildModel();
        } else {
            dialog.setTitle("Edit Snapshot");
            model = editSnapshotModelBuilder.buildModel();
        }

        dialog.getIcons().add(new Image(getClass().getResourceAsStream("snapshot-s1.JPG")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        EditSnapshotController controller = loader.<EditSnapshotController>getController();
        controller.init(model, canvasChangeListener);

        dialog.show();
    }
    
    public boolean deleteSnapshot() {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        
        ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, null, yes, no);
        alert.setTitle("Delete Snapshot");
        alert.setHeaderText("Do you really want to delete the snapshot \"" + snapshot.getName() + "\"?");
        alertDecorator.decorateAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            snapshotManager.deleteSnapshot(snapshot);
            ApricotProject project = projectManager.findCurrentProject();
            List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
            ApricotSnapshot defSnapshot = snapshots.get(snapshots.size()-1);
            setDefaultSnapshot(defSnapshot);
            
            return true;
        }
        
        return false;
    }
}
