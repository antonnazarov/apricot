package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
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

    @Resource
    ConstraintManager constraintManager;

    @Autowired
    PrimaryKeySerializer primaryKeySerializer;

    public void serialize(EditEntityModel model) {
        if (!model.isNewEntity()) {
            deleteRemovedConstraints(model);
        }
        
        updateConstraints(model);
        primaryKeySerializer.serializePrimaryKey(model);
    }

    private void updateConstraints(EditEntityModel model) {
        for (ApricotConstraintData cd : model.getConstraints()) {
            if (cd.getConstraintTypeAsString().equals(ConstraintType.PRIMARY_KEY.name())
                    || cd.getConstraintTypeAsString().equals(ConstraintType.FOREIGN_KEY.name()) || !cd.isEdited()) {
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

            serializeConstraintColumns(constraint, cd, model);
        }
    }

    private void deleteRemovedConstraints(EditEntityModel model) {
        for (ApricotConstraint constraint : getRemovedConstraints(model)) {
            for (ApricotColumnConstraint acc : constraint.getColumns()) {
                constraintManager.deleteConstraintColumn(acc);
            }
            constraintManager.deleteConstraint(constraint);
        }
    }

    private List<ApricotConstraint> getRemovedConstraints(EditEntityModel model) {
        List<ApricotConstraint> deletedConstraints = new ArrayList<>();

        for (ApricotConstraintData cd : model.getDeletedConstraints()) {
            if (!cd.isAdded()) {
                deletedConstraints.add(cd.getConstraint());
            }
        }

        return deletedConstraints;
    }

    private void serializeConstraintColumns(ApricotConstraint constraint, ApricotConstraintData constraintData,
            EditEntityModel model) {
        for (ApricotColumnConstraint constrCol : constraint.getColumns()) {
            constraintManager.deleteConstraintColumn(constrCol);
        }
        constraint.getColumns().clear();

        int ordinalPosition = 0;
        for (ApricotColumnData columnData : constraintData.getColumns()) {
            ApricotColumnConstraint acc = new ApricotColumnConstraint(constraint, columnData.getColumn());
            acc.setOrdinalPosition(ordinalPosition);
            constraint.getColumns().add(acc);

            ordinalPosition++;
        }
    }
}
