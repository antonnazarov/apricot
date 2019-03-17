package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
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
    
    public boolean validateRelationshipModel(EditRelationshipModel model) {
        
        if (!validateRelationshipName(model)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateRelationshipName(EditRelationshipModel model) {
        String relName = model.getRelationshipNameProperty().getValue();
        
        if (relName == null || relName.equals("") || relName.equals(EditRelationshipController.NEW_RELATIONSHIP)) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship", "Please provide a non empty name of the Relatinship");
            alert.showAndWait();

            return false;
        }
        
        if (constraintManager.getConstraintByName(relName) != null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship", "The constraint with the name " + "\""+ relName + "\" already exists");
            alert.showAndWait();

            return false;
        }
        
        return true;
    }
}
