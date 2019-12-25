package za.co.apricotdb.ui.handler;

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

    /**
     * Set up the entity filter, using the case insensitive search string.
     */
    public void setupEntityFilter(String search) {
        if (search.equals("*") || StringUtils.isEmpty(search)) {
            resetEntityFilter();
            return;
        }

        List<ApricotTable> filterResult = tableManager.findTablesByName(search);

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
        }
    }

    /**
     * Reset the Entity Filter.
     */
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
}
