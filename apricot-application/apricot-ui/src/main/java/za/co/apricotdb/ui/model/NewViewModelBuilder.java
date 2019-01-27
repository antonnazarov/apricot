package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

@Component
public class NewViewModelBuilder {

    @Autowired
    TableManager tableManager;

    @Autowired
    ApricotViewHandler viewHandler;

    public ViewFormModel buildModel(TabPane viewsTabPane) {
        ViewFormModel model = new ViewFormModel();

        model.setNewView(true);

        model.setViewName("<New View>");

        int selectedIdx = viewsTabPane.getSelectionModel().getSelectedIndex();
        Tab tab = viewsTabPane.getTabs().get(selectedIdx);
        Object o = tab.getUserData();
        if (o != null && o instanceof TabInfoObject) {
            TabInfoObject tabInfo = (TabInfoObject) o;
            ApricotCanvas canvas = tabInfo.getCanvas();
            ApricotSnapshot snapshot = tabInfo.getSnapshot();
            model.setSnapshot(snapshot);

            List<ApricotTable> snapTables = tableManager.getTablesForSnapshot(snapshot);
            model.setSnapshotTables(getTableNames(snapTables));

            List<ApricotEntity> selectedEntites = canvas.getSelectedEntities();
            List<String> availableTables = null;
            if (selectedEntites != null && selectedEntites.size() > 0) {
                // there are selected entities in the current view
                availableTables = getAvailableTables(snapTables, selectedEntites);
                List<String> viewTables = getSelectedTables(selectedEntites);
                model.setSelectedInCanvas(new ArrayList<String>(viewTables));
                model.addViewTables(viewTables);
            } else {
                availableTables = getAvailableTables(snapTables, null);
            }
            model.addAvailableTables(availableTables);
            model.addFromViews(getViews(tabInfo));
        }

        return model;
    }

    private List<String> getViews(TabInfoObject tabInfo) {
        List<String> ret = new ArrayList<>();
        List<ApricotView> views = viewHandler.getAllViews(tabInfo.getSnapshot().getProject());
        for (ApricotView v : views) {
            ret.add(v.getName());
        }

        return ret;
    }

    public List<String> getAvailableTablesFromSnapshotAndSelected(List<String> snapTables,
            List<String> selectedTables) {
        List<String> ret = new ArrayList<>(snapTables);

        if (selectedTables != null && selectedTables.size() > 0) {
            for (String t : selectedTables) {
                ret.remove(t);
            }
        }

        Collections.sort(ret);

        return ret;
    }

    private List<String> getAvailableTables(List<ApricotTable> snapTables, List<ApricotEntity> selectedEntites) {
        List<String> sTables = getTableNames(snapTables);
        List<String> selectedTables = getSelectedTables(selectedEntites);

        return getAvailableTablesFromSnapshotAndSelected(sTables, selectedTables);
    }

    private List<String> getTableNames(List<ApricotTable> tables) {
        List<String> ret = new ArrayList<>();
        for (ApricotTable table : tables) {
            ret.add(table.getName());
        }
        Collections.sort(ret);

        return ret;
    }

    private List<String> getSelectedTables(List<ApricotEntity> selectedEntites) {
        List<String> ret = new ArrayList<>();

        if (selectedEntites != null && selectedEntites.size() > 0) {
            for (ApricotEntity entity : selectedEntites) {
                ret.add(entity.getTableName());
            }

            Collections.sort(ret);
        }

        return ret;
    }
}
