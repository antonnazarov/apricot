package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.undo.UndoType;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This component deletes selected entities and relationships.
 * 
 * @author Anton Nazarov
 * @since 20/03/2019
 */
@Component
public class DeleteSelectedHandler {

    private static final int ELEMENTS_IN_DELETE_LIST = 15;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    AlertMessageDecorator alert;

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    ApricotEntityHandler entityHandler;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    MainAppController appController;

    @ApricotErrorLogger(title = "Unable to delete the selected Entities/Relationship")
    public void deleteSelected() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> entities = canvas.getSelectedEntities();
        if (entities == null || entities.size() == 0) {
            List<ApricotRelationship> relationships = canvas.getSelectedRelationships();
            if (relationships != null && relationships.size() > 0) {
                deleteRelationships(relationships);
            }
        } else {
            deleteEntities(getNames(entities));
        }
    }

    List<String> getNames(List<ApricotEntity> entities) {
        List<String> ret = new ArrayList<>();

        for (ApricotEntity e : entities) {
            ret.add(e.getTableName());
        }

        return ret;
    }

    private void deleteRelationships(List<ApricotRelationship> relationships) {
        StringBuilder sb = new StringBuilder();
        sb.append("The following Relationship(s) will be deleted:\n");
        int elmCount = 0;
        for (ApricotRelationship r : relationships) {
            if (elmCount == ELEMENTS_IN_DELETE_LIST) {
                sb.append("...").append(relationships.size()-ELEMENTS_IN_DELETE_LIST).append(" more ...");
                break;
            }

            sb.append(" * ").append(r.getParent().getTableName()).append("->").append(r.getChild().getTableName())
                    .append("\n");
            elmCount++;
        }
        if (alert.requestYesNoOption("Delete Relationship(s)", sb.toString(), "Delete")) {
            appController.save(null);
            undoManager.addSavepoint(UndoType.OBJECT_EDITED);

            for (ApricotRelationship r : relationships) {
                za.co.apricotdb.persistence.entity.ApricotRelationship rel = relationshipManager
                        .findRelationshipById(r.getRelationshipId());
                relationshipHandler.deleteRelationship(rel);
            }
            snapshotHandler.syncronizeSnapshot(true);
        }
    }

    @ApricotErrorLogger(title = "Unable to delete the selected Entities/Relationship")
    public void deleteEntities(List<String> entities) {
        StringBuilder sb = new StringBuilder();
        sb.append("The following Entity(s) will be deleted:\n");
        int elmCount = 0;
        for (String e : entities) {
            if (elmCount == ELEMENTS_IN_DELETE_LIST) {
                sb.append("... ").append(entities.size()-ELEMENTS_IN_DELETE_LIST).append(" more ...").append("\n");
                break;
            }

            sb.append("* ").append(e).append("\n");
            elmCount++;
        }
        if (alert.requestYesNoOption("Delete Entity(s)", sb.toString(), "Delete")) {
            appController.save(null);
            undoManager.addSavepoint(UndoType.OBJECT_EDITED);

            for (String e : entities) {
                entityHandler.deleteEntity(e);
            }

            snapshotHandler.syncronizeSnapshot(true);
        }
    }
}
