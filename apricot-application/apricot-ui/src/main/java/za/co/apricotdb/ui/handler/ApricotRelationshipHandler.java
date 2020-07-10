package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import za.co.apricotdb.ui.model.ApricotRelationshipSerializer;
import za.co.apricotdb.ui.model.ApricotRelationshipValidator;
import za.co.apricotdb.ui.model.EditRelationshipModel;
import za.co.apricotdb.ui.model.EditRelationshipModelBuilder;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.ImageHelper;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * This component contains all Relationship high level business logic.
 *
 * @author Anton Nazarov
 * @since 14/03/2019
 */
@Component
public class ApricotRelationshipHandler {

    @Resource
    ApplicationContext context;

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

    @Transactional
    public void openRelationshipEditorForm(TabPane viewsTabPane) throws IOException {

        // check if one or two entities were selected on the current canvas
        ApricotTable[] selectedTables = getRelatedTables(viewsTabPane);
        if (selectedTables == null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "Please select one or two Entites which you want to build a new Relationship between");
            alert.showAndWait();

            return;
        }

        EditRelationshipModel model = modelBuilder.buildModel(selectedTables);
        if (model == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-relationship-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create a new Relationship");
        dialog.getIcons().add(ImageHelper.getImage("table-1-s1.jpg", getClass()));

        Scene newRelationshipScene = new Scene(window);
        dialog.setScene(newRelationshipScene);
        newRelationshipScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        EditRelationshipController controller = loader.<EditRelationshipController>getController();
        controller.init(model);
        modelBuilder.populateKeys(model);

        dialog.show();
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
        refreshTables(model);
        if (!relationshipValidator.validateRelationshipModel(model)) {
            return false;
        }
        appController.save(null);  //  save the current layout
        relationshipSerializer.serializeRelationship(model);
        snapshotHandler.syncronizeSnapshot(true);

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
