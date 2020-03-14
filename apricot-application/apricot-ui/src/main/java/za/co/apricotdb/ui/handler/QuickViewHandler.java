package za.co.apricotdb.ui.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.ReversedTablesController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ViewFormModel;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This handler serves the "Quick View" functionality.
 * 
 * @author Anton Nazarov
 * @since 13/03/2020
 */
@Component
public class QuickViewHandler {

    private final static String QUICK_VIEW_NAME = "Quick View";

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    NonTransactionalViewHandler ntaViewHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    NonTransactionalPort port;
    
    @Autowired
    CanvasAlignHandler aligner;
    
    @Autowired
    ReversedTablesController revTabController;

    /**
     * Create a new Quick View using the selected entities in the current view.
     */
    @ApricotErrorLogger(title = "Unable to build the Quick View")
    public void createQuickView() {
        List<ApricotEntity> selected = getSelected();

        // if there is no selected entities in the current view,
        // just return with no action
        if (selected.size() == 0) {
            return;
        }

        removeExistingQuickView();
        ntaViewHandler.saveView(createViewModel(selected, QUICK_VIEW_NAME), parentWindow.getProjectTabPane(),
                QUICK_VIEW_NAME);
        // aligner.alignCanvasIslands();
        
        revTabController.alignAfterDelay(3).play();
    }

    private List<ApricotEntity> getSelected() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        return canvas.getSelectedEntities();
    }

    /**
     * Create a model of the new view.
     */
    private ViewFormModel createViewModel(List<ApricotEntity> selected, String viewName) {
        ViewFormModel model = new ViewFormModel();

        List<String> tables = getTableNames(selected);
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        model.setNewView(true);
        model.setViewName(viewName);
        model.setSnapshot(snapshot);
        model.getViewTables().addAll(tables);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        model.setComment("The Quick View was created " + df.format(new java.util.Date()) + "\n"
                + "The entities included into the Quick View: " + getTablesAsString(tables));

        return model;
    }

    private String getTablesAsString(List<String> tables) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (String table : tables) {
            if (first) {
                first = false;
            } else {
                sb.append("; ");
            }
            sb.append(table);
        }

        return sb.toString();
    }

    List<String> getTableNames(List<ApricotEntity> selected) {
        List<String> tables = new ArrayList<>();

        for (ApricotEntity ent : selected) {
            tables.add(ent.getTableName());
        }

        return tables;
    }

    private void removeExistingQuickView() {
        TabPane tpane = parentWindow.getViewsTabPane();
        Tab removeTab = null;
        for (Tab tab : tpane.getTabs()) {
            TabInfoObject tObj = TabInfoObject.getTabInfo(tab);
            if (tObj.getView().getName().equals(QUICK_VIEW_NAME)) {
                port.removeView(tObj.getView());
                removeTab = tab;
                break;
            }
        }
        
        if (removeTab != null) {
            tpane.getTabs().remove(removeTab);
        }
    }
}
