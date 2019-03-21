package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import za.co.apricotdb.ui.model.ApricotEntitySerializer;
import za.co.apricotdb.ui.model.EditEntityModel;
import za.co.apricotdb.ui.model.EditEntityModelBuilder;

/**
 * The handled of Apricot Entity (Table).
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class ApricotEntityHandler {

    @Resource
    ApplicationContext context;

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
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    ConstraintManager constraintManager;

    @Transactional
    public void openEntityEditorForm(boolean newEntity, String tableName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-entity-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        if (newEntity) {
            dialog.setTitle("Create a new Entity");
        } else {
            dialog.setTitle("Edit Entity");
        }
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("table-1-s1.jpg")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        EditEntityController controller = loader.<EditEntityController>getController();

        EditEntityModel model = modelBuilder.buildModel(newEntity, tableName);
        controller.init(model);

        dialog.show();
    }

    public void saveEntity(EditEntityModel model, String entityName) {
        model.setEntityName(entityName);
        entitySerializer.serialize(model);

        // handle when the entity name was changed
        if (!model.isNewEntity() && !model.getEntityName().equals(model.getEntityOriginalName())) {
            objectLayoutHandler.duplicateObjectLayoutsForNewEntityName(model.getEntityOriginalName(),
                    model.getEntityName());
            canvasHandler.renameEntityOnCanvas(model.getEntityOriginalName(), model.getEntityName());
        }
        canvasHandler.updateEntity(model.getTable(), model.isNewEntity());
        treeViewHandler.populate(projectManager.findCurrentProject(), snapshotManager.getDefaultSnapshot());
        treeViewHandler.selectEntity(entityName);
    }

    /**
     * Delete the entity.
     */
    @Transactional
    public void deleteEntity(String entityName) {
        ApricotTable table = tableManager.getTableByName(entityName, snapshotManager.getDefaultSnapshot());
        if (table != null) {
            List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTable(table);
            for (ApricotRelationship r : relationships) {
                relationshipHandler.deleteRelationship(r);
            }

            for (ApricotConstraint constr : table.getConstraints()) {
                if (constr.getType() != ConstraintType.FOREIGN_KEY) {
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
