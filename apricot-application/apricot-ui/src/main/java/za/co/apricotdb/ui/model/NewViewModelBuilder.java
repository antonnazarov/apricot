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
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

@Component
public class NewViewModelBuilder {

    @Autowired
    TableManager tableManager;

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
            List<ApricotEntity> selectedEntites = canvas.getSelectedEntities();
            List<String> availableTables = null;
            if (selectedEntites != null && selectedEntites.size() > 0) {
                // there are selected entities in the current view
                availableTables = getAvailableTables(snapTables, selectedEntites);
                List<String> viewTables = getSelectedTables(selectedEntites);
                model.addViewTables(viewTables);
            } else {
                availableTables = getAvailableTables(snapTables, null);
            }
            model.addAvailableTables(availableTables);
        }

        return model;
    }

    private List<String> getAvailableTables(List<ApricotTable> snapTables, List<ApricotEntity> selectedEntites) {
        List<String> ret = new ArrayList<>();

        for (ApricotTable table : snapTables) {
            ret.add(table.getName());
        }

        if (selectedEntites != null && selectedEntites.size() > 0) {
            List<String> selectedTables = getSelectedTables(selectedEntites);
            for (String t : selectedTables) {
                ret.remove(t);
            }
        }

        Collections.sort(ret);

        return ret;
    }

    private List<String> getSelectedTables(List<ApricotEntity> selectedEntites) {
        List<String> ret = new ArrayList<>();

        for (ApricotEntity entity : selectedEntites) {
            ret.add(entity.getTableName());
        }

        Collections.sort(ret);

        return ret;
    }
}
