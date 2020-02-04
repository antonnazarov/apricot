package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.comparator.SnapshotComparator;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.CompareSnapshotsController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * The compare snapshots functionality.
 * 
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    SnapshotComparator snapshotComparator;

    @ApricotErrorLogger(title = "Unable to open the Compare Snapshots form")
    public void openCompareSnapshotsForm() throws IOException {
        if (!validate()) {
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-compare-snapshots.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Compare Snapshots");
        dialog.getIcons().add(
                new Image(getClass().getResourceAsStream("/za/co/apricotdb/ui/toolbar/tbCompareSnapshotEnabled.png")));
        Scene scene = new Scene(window);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        dialog.setScene(scene);

        CompareSnapshotsController controller = loader.<CompareSnapshotsController>getController();
        controller.init();

        dialog.show();
    }
    
    @Transactional
    public SnapshotDifference compare(String sourceSnapshot, String targetSnapshot) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotSnapshot source = snapshotManager.getSnapshotByName(project, sourceSnapshot);
        ApricotSnapshot target = snapshotManager.getSnapshotByName(project, targetSnapshot);
        
        
        return snapshotComparator.compare(source, target);
    }
    
    private boolean validate() {
        ApricotProject project = projectManager.findCurrentProject();
        List<ApricotSnapshot> snaps = new ArrayList<>();
        if (project != null) {
            snaps = snapshotManager.getAllSnapshots(project);
        } else {
            Alert alert = alertDecorator.getErrorAlert("Compare Snapshots", "The current Project does not have Snapshots");
            alert.showAndWait();
            
            return false;
        }

        if (snaps.size() <= 1) {
            Alert alert = alertDecorator.getErrorAlert("Compare Snapshots", "There have to be at least two Snapshots in the Project to compare them");
            alert.showAndWait();
        } else {
            return true;
        }
        
        return false;
    }
}
