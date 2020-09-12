package za.co.apricotdb.ui.handler;

import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.EditConstraintController;
import za.co.apricotdb.ui.model.ApricotConstraintData;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.EditConstraintModel;
import za.co.apricotdb.ui.model.EditConstraintModelBuilder;
import za.co.apricotdb.ui.model.EditEntityModel;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApricotConstraintHandler {

    @Autowired
    EditConstraintModelBuilder modelBuilder;

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    DialogFormHandler formHandler;

    public List<ApricotConstraint> getConstraintsForColumn(ApricotColumn column) {
        List<ApricotConstraint> ret = new ArrayList<>();

        ApricotTable table = column.getTable();
        for (ApricotConstraint constr : table.getConstraints()) {
            for (ApricotColumnConstraint cc : constr.getColumns()) {
                if (cc.getColumn().equals(column)) {
                    ret.add(constr);
                }
            }
        }

        if (ret.size() > 1) {
            constraintManager.sortConstraints(ret);
        }

        return ret;
    }

    @Transactional
    public void openConstraintEditorForm(boolean newConstraint, ApricotConstraintData constraintData,
                                         EditEntityModel editEntityModel,
                                         TableView<ApricotConstraintData> constraintsTable, boolean editableFields) {
        String title;
        if (newConstraint) {
            title = "Create a new Constraint";
        } else {
            title = "Edit Constraint";
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-constraint-editor.fxml",
                "table-1-s1.jpg", title);
        EditConstraintController controller = form.getController();

        EditConstraintModel model = modelBuilder.buildModel(newConstraint, constraintData, editEntityModel);
        controller.init(model, constraintsTable, editEntityModel, editableFields);

        form.show();
    }

    /**
     * Delete the constraint as well as the related relationships.
     */
    public void deleteRelatedRelationships(ApricotConstraint constraint) {
        List<ApricotRelationship> relationships = relationshipManager.findRelationshipsByConstraint(constraint);
        if (relationships != null && relationships.size() > 0) {
            if (constraint.getType() == ConstraintType.PRIMARY_KEY) {
                deleteRelationships(relationships);
            } else if (constraint.getType() == ConstraintType.FOREIGN_KEY) {
                relationshipManager.deleteRelationship(relationships.get(0));
            }
        }
    }

    private void deleteRelationships(List<ApricotRelationship> relationships) {
        for (ApricotRelationship r : relationships) {
            relationshipHandler.deleteRelationship(r);
        }
    }
}
