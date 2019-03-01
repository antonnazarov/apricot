package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * The builder of the EditConstraintModel.
 * 
 * @author Anton Nazarov
 * @since 01/03/2019
 */
@Component
public class EditConstraintModelBuilder {

    public static final String DEFAULT_CONSTRAINT_NAME = "<New Constraint>";

    public EditConstraintModel buildModel(boolean newModel, ApricotConstraintData constraintData,
            EditEntityModel editEntityModel) {
        EditConstraintModel model = new EditConstraintModel();

        if (newModel) {
            constraintData = new ApricotConstraintData();
            constraintData.getConstraintName().setValue(DEFAULT_CONSTRAINT_NAME);
            constraintData.getConstraintType().setValue(ConstraintType.UNIQUE_INDEX.name());
            model.setNewConstraint(true);
        }

        model.getConstraintName().setValue(constraintData.getConstraintName().getValue());
        model.getConstraintType().setValue(constraintData.getConstraintType().getValue());
        model.getSelectedColumns().addAll(constraintData.getColumns());
        model.setConstraintData(constraintData);

        model.getAllColumns().addAll(editEntityModel.getColumns());
        subtractSelectedColumns(model);

        return model;
    }

    private void subtractSelectedColumns(EditConstraintModel model) {
        for (ApricotColumnData cd : model.getSelectedColumns()) {
            model.getAllColumns().remove(cd);
        }
    }
}
