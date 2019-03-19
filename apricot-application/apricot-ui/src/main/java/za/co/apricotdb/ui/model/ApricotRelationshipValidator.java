package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.EditRelationshipController;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * All values validation business logic of the Relationship being created is
 * located in this component.
 * 
 * @author Anton Nazarov
 * @since 15/03/2019
 */
@Component
public class ApricotRelationshipValidator {

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    RelationshipManager relationshipManager;

    public boolean checkPrimaryKey(EditRelationshipModel model) {
        ApricotTable table = model.getParentTable();
        ApricotConstraint pk = getPrimaryKey(table);

        if (pk == null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship", "The table " + table.getName()
                    + " does not have a Primary Key. It can't be a Parent in the Relationship");
            alert.showAndWait();

            return false;
        }

        if (pk.getColumns().size() > 5) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship", "The Primary Key of the table "
                    + table.getName()
                    + " includes more than 5 fields. Maximum 5 fields in the Primary Key is a limitation of ApricotDB. Sorry");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    public ApricotConstraint getPrimaryKey(ApricotTable table) {

        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.PRIMARY_KEY) {
                return c;
            }
        }

        return null;
    }

    public boolean isColumnPrimaryKey(ApricotTable table, ApricotColumn column) {
        ApricotConstraint constraint = getPrimaryKey(table);

        if (constraint != null) {
            for (ApricotColumnConstraint acc : constraint.getColumns()) {
                if (acc.getColumn().equals(column)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isColumnForeignKey(ApricotTable table, ApricotColumn column) {
        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.FOREIGN_KEY) {
                for (ApricotColumnConstraint acc : c.getColumns()) {
                    if (acc.getColumn().equals(column)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Validate the model data after the editing is finished.
     */
    public boolean validateRelationshipModel(EditRelationshipModel model) {

        if (!validateRelationshipName(model)) {
            return false;
        }

        if (!verifyForeignKeyColumns(model)) {
            return false;
        }

        return true;
    }

    private boolean verifyForeignKeyColumns(EditRelationshipModel model) {
        List<String> columns = new ArrayList<>();
        boolean hasPK = false;
        boolean allPK = true;
        for (ParentChildKeyHolder h : model.getKeys()) {
            String columnName = h.getForeignKeyField();
            if (columnName == null || columnName.equals("")) {
                Alert alert = alertDecorator.getErrorAlert("New Relationship",
                        "Please select or fill in a non empty name of the foreign key field");
                alert.showAndWait();
                return false;
            }

            if (columns.contains(columnName)) {
                Alert alert = alertDecorator.getErrorAlert("New Relationship",
                        "The fields in the foreign key must be unique");
                alert.showAndWait();
                return false;
            }

            ApricotColumn column = model.getChildTable().getColumnByName(columnName);
            if (column != null) {
                if (isColumnForeignKey(model.getChildTable(), column)) {
                    Alert alert = alertDecorator.getErrorAlert("New Relationship",
                            "The field + " + columnName + " is already included in the foreign key");
                    alert.showAndWait();
                    return false;
                }
            } else if (h.isPrimaryKey()) {
                // for the new field and if it was marked as a Primary Key, check that the
                // existing primary
                // key of the child- table is not included into any existing relationship.
                // In this case, it is prohibited to change content of the Primary Key.
                for (ApricotRelationship r : relationshipManager.getRelationshipsForTable(model.getChildTable())) {
                    if (r.getParent().getTable().equals(model.getChildTable())) {
                        Alert alert = alertDecorator.getErrorAlert("New Relationship", "The field \"" + columnName
                                + "\" cannot be created as a part of the Primary Key of the Child table, "
                                + "because there is already a Relationship, where the this table acts as a parent.");
                        alert.showAndWait();
                        return false;
                    }
                }
            }

            if (h.isPrimaryKey()) {
                hasPK = true;
            } else {
                allPK = false;
            }

            columns.add(columnName);
        }

        if (hasPK && !allPK) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "If there is a Primary Key among the selected/added Foreign Key fields of the child table, "
                            + "all the Foreign Keys of the child table must be included in its Primary Key");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean validateRelationshipName(EditRelationshipModel model) {
        String relName = model.getRelationshipNameProperty().getValue();

        if (relName == null || relName.equals("") || relName.equals(EditRelationshipController.NEW_RELATIONSHIP)) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "Please provide a non empty name of the Relatinship");
            alert.showAndWait();

            return false;
        }

        if (constraintManager.getConstraintByName(relName) != null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "The constraint with the name " + "\"" + relName + "\" already exists");
            alert.showAndWait();

            return false;
        }

        return true;
    }
}
