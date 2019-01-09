package za.co.apricotdb.ui.controller;

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
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
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

    public void initialize(ParentWindow pw) {
        ApricotProject currentProject = projectManager.findCurrentProject();

        System.out.println("The application was started. The current project: " + currentProject.getName());

        pw.getProjectTreeView().setRoot(new TreeItem<String>(currentProject.getName()));
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(currentProject);
        System.out.println("The default snapshot: " + defaultSnapshot.getName());

        ComboBox<String> combo = pw.getSnapshotCombo();
        combo.getItems().addAll(defaultSnapshot.getName());
        combo.setValue(defaultSnapshot.getName());
    }
}
