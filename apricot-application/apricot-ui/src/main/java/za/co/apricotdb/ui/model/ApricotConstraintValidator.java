package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * The Validator component of the data entered into the New/Edit Constraint
 * form.
 * 
 * @author Anton Nazarov
 * @since 25/03/2019
 */
@Component
public class ApricotConstraintValidator {

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ConstraintManager constraintManager;

    public boolean validate(EditConstraintModel constraintModel, EditEntityModel editEntityModel) {

        if (!checkConstraintName(constraintModel, editEntityModel)) {
            return false;
        }

        if (!checkConstraintColumns(constraintModel)) {
            return false;
        }

        return true;
    }

    private boolean checkConstraintName(EditConstraintModel constraintModel, EditEntityModel editEntityModel) {
        String constraintName = constraintModel.getConstraintName().getValue();
        if (constraintName == null || constraintName.equals("") || constraintName.equals("<New Constraint>")) {
            Alert alert = alertDecorator.getErrorAlert("Edit Constraint",
                    "Please provide a not empty name of the Constraint");
            alert.showAndWait();
            return false;
        }

        // check the "soft" unique
        boolean isUnique = true;
        for (ApricotConstraintData acd : editEntityModel.getConstraints()) {
            if (acd.getConstraintName().getValue().equals(constraintModel.getConstraintName().getValue())
                    && acd != constraintModel.getConstraintData()) {
                isUnique = false;
                break;
            }
        }

        // check the "hard" unique
        ApricotConstraint cn = constraintManager.getConstraintByName(constraintName);
        if (!isUnique || (cn != null
                && (constraintModel.isNewConstraint() || cn.getId() != constraintModel.getConstraintData().getId()))) {
            Alert alert = alertDecorator.getErrorAlert("Edit Constraint",
                    "Constraint \"" + constraintName + "\" already exist");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private boolean checkConstraintColumns(EditConstraintModel constraintModel) {
        if (constraintModel.getSelectedColumns().size() == 0) {
            Alert alert = alertDecorator.getErrorAlert("Edit Constraint",
                    "Please include some columns into the Constraint");
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
