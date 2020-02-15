package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.TreeItem;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.comparator.CompareScriptGenerator;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;

/**
 * This component represents a collection of he non transactional calls to the
 * transactional methods in the business logic specific components. It allows to
 * use the general mechanism of the error handling of Apricot DB.
 * 
 * @author Anton Nazarov
 * @since 08/02/2020
 */
@Component
public class NonTransactionalPort {

    @Autowired
    CompareSnapshotsHandler compareSnapshotHandler;

    @Autowired
    CompareScriptGenerator compareScriptGenerator;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    ViewManager viewManager;
    
    @Autowired
    ApricotEntityHandler entityHandler;

    @ApricotErrorLogger(title = "Unable to compare the selected Snapshots")
    public TreeItem<CompareSnapshotRow> compare(String sourceSnapshot, String targetSnapshot, boolean diffOnly) {
        return compareSnapshotHandler.compare(sourceSnapshot, targetSnapshot, diffOnly);
    }

    @ApricotErrorLogger(title = "Unable to generate the compare script")
    public String generate(List<CompareSnapshotRow> differences, String schema) {
        return compareScriptGenerator.generate(differences, schema);
    }

    @ApricotErrorLogger(title = "Unable to initialize the application")
    public void initialize(ApricotProject project, ApricotSnapshot snapshot) {
        applicationInitializer.initialize(project, snapshot);
    }

    @ApricotErrorLogger(title = "Unable to set the default Snapshot")
    public void setDefaultSnapshot(String snapshotName) {
        ApricotSnapshot snapshot = snapshotManager.getSnapshotByName(projectManager.findCurrentProject(), snapshotName);
        if (snapshot == null) {
            return;
        }
        snapshotManager.setDefaultSnapshot(snapshot);
        initialize(snapshot.getProject(), snapshot);
    }
    
    @ApricotErrorLogger(title="Unable to delete the view")
    public void removeView(ApricotView view) {
        viewManager.removeView(view);
    }
    
    @ApricotErrorLogger(title="Unable to open the Entity editor form")
    public void openEntityEditorForm(boolean newEntity, String tableName) {
        entityHandler.openEntityEditorForm(newEntity, tableName);
    }
}
