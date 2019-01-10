package za.co.apricotdb.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ParentWindow;

/**
 * This controller is responsible for initialisation of the application on
 * startup.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class ApplicationInitializer {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    TableManager tableManager;
    
    public void initializeDefault(ParentWindow pw) {
        ApricotProject currentProject = projectManager.findCurrentProject();
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(currentProject);
        
        initialize(pw, currentProject, defaultSnapshot);
    }

    public void initialize(ParentWindow pw, ApricotProject project, ApricotSnapshot snapshot) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        TreeItem<String> root = new TreeItem<>(project.getName());
        root.getChildren().addAll(getTables(tables));
        root.setExpanded(true);
        pw.getProjectTreeView().setRoot(root);
        
        ComboBox<String> combo = pw.getSnapshotCombo();        
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
        List<String> snapNames = new ArrayList<>();
        for (ApricotSnapshot s : snapshots) {
            snapNames.add(s.getName());
        }
        combo.getItems().addAll(snapNames);
        combo.setValue(snapshot.getName());
    }
    
    private List<TreeItem<String>> getTables(List<ApricotTable> tables) {
        List<TreeItem<String>> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ret.add(new TreeItem<String>(t.getName()));
        }
        
        return ret;
    }
}
