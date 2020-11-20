package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.EditEntityController;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.model.ApricotEntitySerializer;
import za.co.apricotdb.ui.model.ApricotEntityValidator;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.EditEntityModel;
import za.co.apricotdb.ui.model.EditEntityModelBuilder;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.undo.UndoType;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

/**
 * The handled of Apricot Entity (Table).
 *
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class ApricotEntityHandler {

    @Autowired
    EditEntityModelBuilder modelBuilder;

    @Autowired
    ApricotConstraintHandler constraintHandler;

    @Autowired
    ApricotEntitySerializer entitySerializer;

    @Autowired
    ApricotObjectLayoutHandler objectLayoutHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    ApricotEntityValidator entityValidator;

    @Autowired
    EditEntityKeyHandler keyHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    MainAppController appController;

    @Autowired
    ObjectAllocationHandler allocationHandler;

    @Autowired
    DialogFormHandler formHandler;

    @Transactional
    public void openEntityEditorForm(boolean newEntity, String tableName) {
        String title;
        if (newEntity) {
            title = "Create a new Entity";
        } else {
            title = "Edit Entity";
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-entity-editor.fxml",
                "table-1-s1.jpg", title, keyHandler);
        EditEntityController controller = form.getController();
        keyHandler.setEditEntityController(controller);
        form.getDialog().setOnCloseRequest(e -> {
            controller.cancel(null);
            e.consume();
        });

        EditEntityModel model = modelBuilder.buildModel(newEntity, tableName, form.getDialog());
        controller.init(model);

        form.show();
    }

    @Transactional
    public boolean saveEntity(EditEntityModel model, String entityName, EditEntityController controller) {
        model.setEntityName(entityName);

        if (!entityValidator.validate(model, controller)) {
            return false;
        }

        appController.save(null);
        undoManager.addSavepoint(UndoType.OBJECT_EDITED);

        entitySerializer.serialize(model);

        // handle when the entity name was changed
        if (!model.isNewEntity() && !model.getEntityName().equals(model.getEntityOriginalName())) {
            objectLayoutHandler.duplicateObjectLayoutsForNewEntityName(model.getEntityOriginalName(),
                    model.getEntityName());
            canvasHandler.renameEntityOnCanvas(model.getEntityOriginalName(), model.getEntityName());
        }

        TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
        if (model.isNewEntity()) {
            allocationHandler.centerEntityOnView(tabInfo, entityName, 0, 0);
        }
        snapshotHandler.synchronizeSnapshot(true);
        treeViewHandler.selectEntity(entityName);
        canvasHandler.makeEntitySelected(tabInfo, entityName, true);

        return true;
    }

    /**
     * Delete the entity.
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void deleteEntity(String entityName) {
        ApricotTable table = tableManager.getTableByName(entityName, snapshotManager.getDefaultSnapshot());
        if (table != null) {
            List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTable(table);
            for (ApricotRelationship r : relationships) {
                relationshipHandler.deleteRelationship(r);
            }

            for (ApricotConstraint constr : table.getConstraints()) {
                if (relationships.isEmpty() || constr.getType() != ConstraintType.FOREIGN_KEY) {
                    constraintManager.deleteConstraint(constr);
                }
            }

            tableManager.deleteTable(table);
        }
    }

    public ApricotConstraint getPrimaryKey(ApricotTable table) {
        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.PRIMARY_KEY) {
                return c;
            }
        }

        return null;
    }
}
