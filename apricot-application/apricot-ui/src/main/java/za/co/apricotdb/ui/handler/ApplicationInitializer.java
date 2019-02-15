package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

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

    @Autowired
    TabViewHandler tabViewHandler;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    CanvasBuilder canvasBuilder;

    @Autowired
    ApricotCanvasHandler canvasHandler;
    
    @Autowired
    ParentWindow parentWindow;

    @Transactional
    public void initializeDefault(PropertyChangeListener canvasChangeListener) {
        ApricotProject currentProject = projectManager.findCurrentProject();
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(currentProject);

        initialize(currentProject, defaultSnapshot, canvasChangeListener);
    }

    @Transactional
    public void initializeForProject(ApricotProject project,
            PropertyChangeListener canvasChangeListener) {
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(project);
        initialize(project, defaultSnapshot, canvasChangeListener);
    }

    @Transactional
    public void initialize(ApricotProject project, ApricotSnapshot snapshot,
            PropertyChangeListener canvasChangeListener) {
        //  remember the current project
        parentWindow.getApplicationData().setCurrentProject(project);

        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        TreeItem<String> root = new TreeItem<>(project.getName());
        root.getChildren().addAll(getTables(tables));
        root.setExpanded(true);
        parentWindow.getProjectTreeView().setRoot(root);

        ComboBox<String> combo = parentWindow.getSnapshotCombo();
        if (combo.getUserData() != null && 
                combo.getUserData().equals("snapshotCombo.selectSnapshot")) {
            combo.setUserData("reset");
        } else {
            combo.setUserData("AppInitialize");
            initCombo(project, snapshot, combo);
        }

        TabPane tabPane = parentWindow.getProjectTabPane();
        tabPane.getTabs().clear();
        for (ApricotView view : viewHandler.getAllViews(project)) {
            //  create Tabs for General view and for other views with the Layout Objects
            if (view.isGeneral() || view.getObjectLayouts().size() != 0) {
                viewHandler.createViewTab(snapshot, view, tabPane, canvasChangeListener);
            }
        }
    }
    
    private void initCombo(ApricotProject project, ApricotSnapshot snapshot, ComboBox<String> combo) {
        combo.getItems().clear();
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
        for (ApricotSnapshot s : snapshots) {
            combo.getItems().add(s.getName());
        }
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
