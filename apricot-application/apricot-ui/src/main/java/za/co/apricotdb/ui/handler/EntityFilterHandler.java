package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.sqlserver.jdbc.StringUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component supports all filter- related operations.
 * 
 * @author Anton Nazarov
 * @since 21/12/2019
 */
@Component
public class EntityFilterHandler {

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TableManager tableManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ObjectAllocationHandler allocationHandler;

    /**
     * Set up the entity filter, using the case insensitive search string.
     */
    @ApricotErrorLogger(title = "Unable to set the Entity Filter")
    public void setupEntityFilter(String search) {
        if (search.equals("*") || StringUtils.isEmpty(search)) {
            resetEntityFilter();
            return;
        }

        String s = convert(search);

        List<ApricotTable> filterResult = tableManager.findTablesByName(s);

        if (filterResult.size() == 0) {
            Alert alert = alertDecorator.getAlert("Entity Filter",
                    "There was nothing found. Please clarify the search criteria", AlertType.WARNING);
            alert.showAndWait();
            if (parentWindow.getFilterTables().size() > 0) {
                resetEntityFilter();
            }
        } else {
            // the filter is not empty
            parentWindow.getFilterTables().clear();
            parentWindow.getFilterTables().addAll(filterResult);
            snapshotHandler.syncronizeSnapshot(true);

            positionOnCanvas(getEntitiesAsString(filterResult));
        }
    }

    /**
     * Add results of the subsequent search to the existing filter.
     */
    @ApricotErrorLogger(title = "Unable to add to the Entity Filter")
    public void addToEntityFilter(String search) {
        // if the filter is empty, start the creation of the filter from scratch
        if (parentWindow.getFilterTables().isEmpty()) {
            setupEntityFilter(search);
            return;
        }

        String s = convert(search);
        List<ApricotTable> filterResult = tableManager.findTablesByName(s);
        if (!filterResult.isEmpty()) {
            parentWindow.getFilterTables().addAll(filterResult);
            snapshotHandler.syncronizeSnapshot(true);
            positionOnCanvas(getEntitiesAsString(filterResult));
        }
    }

    /**
     * Reset the Entity Filter.
     */
    @ApricotErrorLogger(title = "Unable to reset the Entity Filter")
    public void resetEntityFilter() {
        parentWindow.getFilterTables().clear();
        snapshotHandler.syncronizeSnapshot(true);
    }

    public boolean isFilterOn() {
        return !parentWindow.getFilterTables().isEmpty();
    }

    public List<ApricotTable> getFilterTables() {
        return parentWindow.getFilterTables();
    }

    private String convert(String search) {
        String ret = null;

        ret = search.trim();
        if (ret.startsWith("*")) {
            ret = ret.replaceFirst("\\*", "%");
        }

        if (ret.endsWith("*")) {
            ret = ret.substring(0, ret.length() - 1);
            ret += "%";
        }

        return ret;
    }

    private void positionOnCanvas(List<String> entities) {
        canvasHandler.makeEntitiesSelected(entities, true);
        allocationHandler.scrollToSelected(canvasHandler.getCurrentViewTabInfo());
    }

    private List<String> getEntitiesAsString(List<ApricotTable> tables) {
        List<String> ret = new ArrayList<>();
        tables.forEach(table -> {
            ret.add(table.getName());
        });

        return ret;
    }
}
