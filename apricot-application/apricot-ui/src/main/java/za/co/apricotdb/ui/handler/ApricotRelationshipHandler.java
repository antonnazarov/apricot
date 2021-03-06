package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.EditRelationshipController;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.ApricotRelationshipSerializer;
import za.co.apricotdb.ui.model.ApricotRelationshipValidator;
import za.co.apricotdb.ui.model.EditRelationshipModel;
import za.co.apricotdb.ui.model.EditRelationshipModelBuilder;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

import javax.transaction.Transactional;
import java.util.List;

/**
 * This component contains all Relationship high level business logic.
 *
 * @author Anton Nazarov
 * @since 14/03/2019
 */
@Component
public class ApricotRelationshipHandler {

    @Autowired
    EditRelationshipModelBuilder modelBuilder;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ApricotRelationshipValidator relationshipValidator;

    @Autowired
    ApricotRelationshipSerializer relationshipSerializer;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    MainAppController appController;

    @Autowired
    DialogFormHandler formHandler;

    @Transactional
    public void openRelationshipEditorForm(TabPane viewsTabPane) {

        // check if one or two entities were selected on the current canvas
        ApricotTable[] selectedTables = getRelatedTables(viewsTabPane);
        if (selectedTables == null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "Please select one or two Entities which you want to build a new Relationship between");
            alert.showAndWait();

            return;
        }

        EditRelationshipModel model = modelBuilder.buildModel(selectedTables);
        if (model == null) {
            return;
        }

        showForm(model, null);
    }

    @Transactional
    public void openRelationshipEditorForm(ApricotRelationship relationship) {
        ApricotRelationship r = relationshipManager.findRelationshipById(relationship.getId());
        EditRelationshipModel model = modelBuilder.buildModel(r);
        showForm(model, relationship);
    }

    private void showForm(EditRelationshipModel model, ApricotRelationship relationship) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-relationship-editor.fxml",
                "table-1-s1.jpg", "Create a new Relationship");

        EditRelationshipController controller = form.getController();
        if (relationship == null) {
            controller.init(model);
        } else {
            controller.init(model, relationship);
        }
        modelBuilder.populateKeys(model);

        form.show();
    }

    @Transactional
    public void swapEntities(EditRelationshipController controller) {
        EditRelationshipModel model = controller.getModel();
        refreshTables(model);
        model.swapTables();
        if (!relationshipValidator.checkPrimaryKey(model)) {
            return;
        }
        controller.init(model);
        modelBuilder.populateKeys(model);
    }

    @Transactional
    public boolean saveRelationship(EditRelationshipController controller) {
        EditRelationshipModel model = controller.getModel();

        //  if the constraint exists, delete the relationship first
        if (model.getRelationship() != null) {
            deleteRelationship(model.getRelationship());
        }

        refreshTables(model);
        if (!relationshipValidator.validateRelationshipModel(model)) {
            return false;
        }

        appController.save(null);  //  save the current layout
        relationshipSerializer.serializeRelationship(model);
        snapshotHandler.synchronizeSnapshot(true);

        return true;
    }

    @Transactional
    public void handleForeignKeyChanged(EditRelationshipModel model, String key, String value) {
        refreshTables(model);

        ApricotColumn column = model.getChildTable().getColumnByName(value);
        if (column != null) {
            relationshipValidator.isColumnPrimaryKey(model.getChildTable(), column);
            model.initColumnAttributes(key, relationshipValidator.isColumnPrimaryKey(model.getChildTable(), column),
                    !column.isNullable());
        } else {
            model.resetColumnAttributes(key);
        }
    }

    /**
     * Delete relationship.
     */
    public void deleteRelationship(ApricotRelationship relationship) {
        ApricotConstraint childConstraint = relationship.getChild();
        relationshipManager.deleteRelationship(relationship);
        constraintManager.deleteConstraint(childConstraint);
    }

    private void refreshTables(EditRelationshipModel model) {
        ApricotSnapshot snap = snapshotManager.getDefaultSnapshot();
        ApricotTable parentTable = tableManager.getTableByName(model.getParentTable().getName(), snap);
        ApricotTable childTable = tableManager.getTableByName(model.getChildTable().getName(), snap);
        model.setParentTable(parentTable);
        model.setChildTable(childTable);
    }

    private ApricotTable[] getRelatedTables(TabPane viewsTabPane) {
        ApricotCanvas canvas = getCurrentCanvas(viewsTabPane);
        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected != null && (selected.size() == 1 || selected.size() == 2)) {
            ApricotTable[] ret = new ApricotTable[2];
            ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
            ApricotTable table = tableManager.getTableByName(selected.get(0).getTableName(), snapshot);
            ret[0] = table;
            if (selected.size() == 2) {
                table = tableManager.getTableByName(selected.get(1).getTableName(), snapshot);
            }
            ret[1] = table;

            return ret;
        }

        return null;
    }

    private ApricotCanvas getCurrentCanvas(TabPane viewsTabPane) {
        Tab tab = viewsTabPane.getSelectionModel().getSelectedItem();

        if (tab.getUserData() instanceof TabInfoObject) {
            TabInfoObject o = (TabInfoObject) tab.getUserData();
            return o.getCanvas();
        }

        return null;
    }
}
