package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
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
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
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

    @Transactional
    public void initializeDefault(ParentWindow pw, PropertyChangeListener canvasChangeListener) {
        ApricotProject currentProject = projectManager.findCurrentProject();
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(currentProject);

        initialize(pw, currentProject, defaultSnapshot, canvasChangeListener);
    }

    @Transactional
    public void initialize(ParentWindow pw, ApricotProject project, ApricotSnapshot snapshot,
            PropertyChangeListener canvasChangeListener) {
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

        TabPane tabPane = pw.getProjectTabPane();
        tabPane.getTabs().clear();
        for (ApricotView view : viewHandler.getAllViews(project)) {
            viewHandler.createViewTab(snapshot, view, tabPane, canvasChangeListener);
        }
    }

    private List<TreeItem<String>> getTables(List<ApricotTable> tables) {
        List<TreeItem<String>> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ret.add(new TreeItem<String>(t.getName()));
        }

        return ret;
    }
}
