package za.co.apricotdb.ui.handler;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
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

    @Transactional
    public void deleteSelected() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> entities = canvas.getSelectedEntities();
        if (entities == null || entities.size() == 0) {
            List<ApricotRelationship> relationships = canvas.getSelectedRelationships();
            if (relationships != null && relationships.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("The following Relationship(s) will be deleted:\n");
                for (ApricotRelationship r : relationships) {
                    sb.append(" * ").append(r.getParent().getTableName()).append("->")
                            .append(r.getChild().getTableName()).append("\n");
                }
                if (alert.requestYesNoOption("Delete Relationship(s)", sb.toString(), "Delete")) {
                    undoManager.addSavepoint(UndoType.OBJECT_EDITED);

                    for (ApricotRelationship r : relationships) {
                        za.co.apricotdb.persistence.entity.ApricotRelationship rel = relationshipManager
                                .findRelationshipById(r.getRelationshipId());
                        relationshipHandler.deleteRelationship(rel);
                    }
                    snapshotHandler.syncronizeSnapshot();
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("The following Entity(s) will be deleted:\n");
            for (ApricotEntity e : entities) {
                sb.append("*").append(e.getTableName()).append("\n");
            }
            if (alert.requestYesNoOption("Delete Entity(s)", sb.toString(), "Delete")) {
                undoManager.addSavepoint(UndoType.OBJECT_EDITED);

                for (ApricotEntity e : entities) {
                    entityHandler.deleteEntity(e.getTableName());
                }
                snapshotHandler.syncronizeSnapshot();
            }
        }
    }
}
