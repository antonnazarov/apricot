package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This is a serialize of constraints after the entity added/edited.
 * 
 * @author Anton Nazarov
 * @since 03/03/2019
 */
@Component
public class ApricotConstraintSerializer {

    public void serialize(EditEntityModel model) {
        if (model.isNewEntity()) {

        } else {

        }

    }

    private void updateConstraints(EditEntityModel model) {
        for (ApricotConstraintData cd : model.getConstraints()) {
            if (cd.getConstraintTypeAsString().equals(ConstraintType.PRIMARY_KEY.name())
                    || cd.getConstraintTypeAsString().equals(ConstraintType.FOREIGN_KEY.name())) {
                continue;
            }
            ApricotConstraint constraint = null;
            if (cd.isAdded()) {
                constraint = model.getTable().getConstraintByName(cd.getConstraintName().getValue());
                if (constraint == null) {
                    constraint = new ApricotConstraint();
                    model.getTable().getConstraints().add(constraint);
                }
            } else {
                constraint = model.getTable().getConstraintById(cd.getId());
            }

            constraint.setName(cd.getConstraintNameAsString());
            constraint.setType(ConstraintType.valueOf(cd.getConstraintTypeAsString()));
            constraint.setTable(model.getTable());
        }
    }

    public List<ApricotConstraint> getDeletedConstraints(EditEntityModel model) {
        List<ApricotConstraint> deletedConstraints = new ArrayList<>();

        for (ApricotConstraintData cd : model.getDeletedConstraints()) {
            if (!cd.isAdded()) {
                deletedConstraints.add(cd.getConstraint());
            }
        }

        return deletedConstraints;
    }
}
