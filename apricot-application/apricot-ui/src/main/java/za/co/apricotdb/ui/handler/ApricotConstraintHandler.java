package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.EditConstraintController;
import za.co.apricotdb.ui.model.ApricotConstraintData;
import za.co.apricotdb.ui.model.EditConstraintModel;
import za.co.apricotdb.ui.model.EditConstraintModelBuilder;
import za.co.apricotdb.ui.model.EditEntityModel;

@Component
public class ApricotConstraintHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    EditConstraintModelBuilder modelBuilder;

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    RelationshipManager relationshipManager;

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
            sortConstraints(ret);
        }

        return ret;
    }

    public void sortConstraints(List<ApricotConstraint> constraints) {
        constraints.sort((ApricotConstraint c1, ApricotConstraint c2) -> {
            if (c1.getType().getOrder() == c2.getType().getOrder()) {
                // use ordinal position of the constraint fields
                if (c1.getColumns() != null && c1.getColumns().size() > 0 && c2.getColumns() != null
                        && c2.getColumns().size() > 0) {
                    return c1.getColumns().get(0).getColumn().getOrdinalPosition()
                            - c2.getColumns().get(0).getColumn().getOrdinalPosition();
                }
            }
            return c1.getType().getOrder() - c2.getType().getOrder();
        });
    }

    @Transactional
    public void openConstraintEditorForm(boolean newConstraint, ApricotConstraintData constraintData,
            EditEntityModel editEntityModel, TableView<ApricotConstraintData> constraintsTable) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-constraint-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        if (newConstraint) {
            dialog.setTitle("Create a new Constraint");
        } else {
            dialog.setTitle("Edit Constraint");
        }
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("table-1-s1.jpg")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        EditConstraintController controller = loader.<EditConstraintController>getController();

        EditConstraintModel model = modelBuilder.buildModel(newConstraint, constraintData, editEntityModel);
        controller.init(model, constraintsTable, editEntityModel);

        dialog.show();
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
