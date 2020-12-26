package za.co.apricotdb.ui.model;

import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.handler.NonTransactionalPort;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.util.ArrayList;
import java.util.List;

@Component
public class EditRelationshipModelBuilder {

    @Autowired
    ApricotEntityHandler entityHandler;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    NonTransactionalPort port;

    public EditRelationshipModel buildModel(ApricotTable[] selectedTables) {

        EditRelationshipModel model = new EditRelationshipModel(selectedTables);
        if (entityHandler.getPrimaryKey(model.getParentTable()) == null) {
            model.swapTables();
            if (entityHandler.getPrimaryKey(model.getParentTable()) == null) {
                Alert alert = alertDecorator.getErrorAlert("New Relationship",
                        "The selected tables " + model.getParentTable().getName() + " and "
                                + model.getChildTable().getName()
                                + " do not have the Primary Keys. The relationship between them cannot be created");
                alert.showAndWait();
                return null;
            }
        }

        return model;
    }

    /**
     * Build the model for the existing relationship.
     */
    public EditRelationshipModel buildModel(ApricotRelationship relationship) {
        EditRelationshipModel model = new EditRelationshipModel(relationship);

        return model;
    }

    public void populateKeys(EditRelationshipModel model) {
        ApricotConstraint pk = entityHandler.getPrimaryKey(model.getParentTable());
        int cnt = 1;

        List<ApricotColumnConstraint> fkColumns = null;
        if (model.getConstraint() != null) {
            fkColumns = model.getConstraint().getColumns();
        }

        for (ApricotColumnConstraint acc : pk.getColumns()) {
            String key = "childForeignKey_" + cnt;
            ApricotColumn column = acc.getColumn();
            model.setPimaryKeyField(key, column.getName());
            List<String> fields = getChildColumnsForTypeAsString(model.getChildTable(), column.getDataType());
            model.populateChildKeyDropDown(key, fields);

            //  set the child field attributes if the relationship is
            //  already exist and just is being edited
            if (fkColumns != null && fkColumns.size() >= cnt) {
                ApricotColumnConstraint col = fkColumns.get(cnt-1);
                model.setChildConstraintField(key, col);
                port.handleForeignKeyChanged(model, key, col.getColumn().getName());
            }

            cnt++;
        }

        while (cnt < 6) {
            String key = "childForeignKey_" + cnt;
            model.setVoidSlot(key);

            cnt++;
        }
    }

    private List<ApricotColumn> getChildColumnsForType(ApricotTable child, String type) {
        List<ApricotColumn> ret = new ArrayList<>();

        for (ApricotColumn c : getNonForeignKeyChildColumns(child)) {
            if (c.getDataType().equals(type)) {
                ret.add(c);
            }
        }

        return ret;
    }

    private List<String> getChildColumnsForTypeAsString(ApricotTable child, String type) {
        List<String> ret = new ArrayList<>();
        List<ApricotColumn> columns = getChildColumnsForType(child, type);
        for (ApricotColumn c : columns) {
            ret.add(c.getName());
        }

        return ret;
    }

    private List<ApricotColumn> getNonForeignKeyChildColumns(ApricotTable table) {
        List<ApricotColumn> ret = new ArrayList<>(table.getColumns());

        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.FOREIGN_KEY) {
                for (ApricotColumnConstraint acc : c.getColumns()) {
                    ret.remove(acc.getColumn());
                }
            }
        }

        return ret;
    }
}
