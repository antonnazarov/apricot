package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * A model builder for the EditEntityModel.
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class EditEntityModelBuilder {

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    public EditEntityModel buildModel(boolean newEntity, String tableName) {
        EditEntityModel model = new EditEntityModel(newEntity);

        if (!newEntity && tableName != null) {
            readData(tableName, model);
        }

        return model;
    }

    /**
     * Read current data for the given table and populate the model.
     */
    public void readData(String tableName, EditEntityModel model) {
        ApricotTable table = tableManager.getTableByName(tableName, snapshotManager.getDefaultSnapshot());
        if (table != null) {
            model.setEntityName(table.getName());
        }

        ObservableList<ApricotColumnData> columns = FXCollections.observableArrayList();
        for (ApricotColumn col : table.getColumns()) {
            ApricotColumnData cd = new ApricotColumnData(col);
            columns.add(cd);
        }
        model.setColumns(columns);
    }
}
