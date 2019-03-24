package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.EditEntityController;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * Implementation of the validations required for the "Create Entity"-
 * operation.
 * 
 * @author Anton Nazarov
 * @since 24/03/2019
 */
@Component
public class ApricotEntityValidator {

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    public boolean validate(EditEntityModel model, EditEntityController controller) {
        if (!checkEntityName(model)) {
            return false;
        }
        
        if (!checkColumns(model, controller)) {
            return false;
        }

        return true;
    }

    private boolean checkEntityName(EditEntityModel model) {
        String entityName = model.getEntityName();

        if (entityName == null || entityName.equals("") || entityName.equals("<New Entity>")) {
            Alert alert = alertDecorator.getErrorAlert("Edit Entity", "Please provide a not empty name of the Entity");
            alert.showAndWait();
            return false;
        }

        ApricotTable table = tableManager.getTableByName(entityName, snapshotManager.getDefaultSnapshot());
        if (table != null && (model.isNewEntity() || !model.getTable().equals(table))) {
            Alert alert = alertDecorator.getErrorAlert("Edit Entity", "The Entity \"" + entityName + "\" already exists");
            alert.showAndWait();
            return false;
        }

        return true;
    }
    
    private boolean checkColumns(EditEntityModel model, EditEntityController controller) {
        int pos = 0;
        List<String> names = new ArrayList<>();
        for (ApricotColumnData row : model.getColumns()) {
            String name = row.getName().getValue();
            if (name == null || name.equals("") || name.equals("<New Column>")) {
                Alert alert = alertDecorator.getErrorAlert("Edit Entity", "The column name must be a unique not empty string");
                controller.selectColumn(pos, "columnName");
                alert.showAndWait();
                return false;
            }
            
            if (names.contains(name)) {
                Alert alert = alertDecorator.getErrorAlert("Edit Entity", "The column \"" + name + "\" is not unique");
                controller.selectColumn(pos, "columnName");
                alert.showAndWait();
                return false;
            }
            
            String dataType = row.getDataType().getValue();
            if (dataType == null || dataType.equals("")) {
                Alert alert = alertDecorator.getErrorAlert("Edit Entity", "The data type of the column has to be provided");
                controller.selectColumn(pos, "dataType");
                alert.showAndWait();
                return false;
            }
            
            names.add(name);
            pos++;
        }
        
        return true;
    }
}
