package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import za.co.apricotdb.persistence.data.TableManager;
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

    /**
     * Set up the entity filter, using the case insensitive search string.
     */
    public void setupEntityFilter(String search) {
        List<ApricotTable> filterTables = tableManager.findTablesByName(search);

        if (filterTables.size() == 0) {
            Alert alert = alertDecorator.getAlert("Entity Filter",
                    "There was nothing found as a result of the search. Please clarify the search criteria",
                    AlertType.WARNING);
            alert.showAndWait();
            
            return;
        }
    }
}
