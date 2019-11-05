package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableCloneManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This handler duplicates the given Entities of the current Canvas.
 * 
 * @author Anton Nazarov
 * @since 27/06/2019
 */
@Component
public class DuplicateHandler {

    @Autowired
    TableCloneManager tableCloneManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ApricotObjectLayoutHandler layoutHandler;

    @Transactional
    public List<String> duplicate(List<String> sTables, String sourceViewName, String sourceSnapshotName) {
        List<String> ret = new ArrayList<>();
        ApricotProject project = projectManager.findCurrentProject();
        Map<ApricotTable, ApricotTable> clonedTables = new HashMap<>();
        Map<ApricotConstraint, ApricotConstraint> clonedConstraints = new HashMap<>();

        ApricotSnapshot sourceSnapshot = snapshotManager.getSnapshotByName(project, sourceSnapshotName);
        List<ApricotTable> tables = getTables(sTables, sourceSnapshot);
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
        ApricotView currentView = canvasHandler.getCurrentView();
        ApricotView sourceView = getSourceView(sourceViewName, currentView, project);

        for (ApricotTable table : tables) {
            ApricotTable clonedTable = tableCloneManager.cloneTable(snapshotManager.getDefaultSnapshot(), table, false,
                    true);
            tableManager.saveTable(clonedTable);
            ret.add(clonedTable.getName());
            cloneNonFKConstraints(table, clonedTable, clonedConstraints);
            clonedTables.put(table, clonedTable);

            layoutHandler.cloneTableLayout(sourceView, currentView, table.getName(), clonedTable.getName());
        }

        cloneRelationships(relationships, clonedTables, clonedConstraints);

        return ret;
    }

    private ApricotView getSourceView(String sourceViewName, ApricotView currentView, ApricotProject project) {
        ApricotView sourceView = null;
        if (!currentView.getName().equals(sourceViewName)) {
            List<ApricotView> vws = viewManager.getViewByName(project, sourceViewName);
            if (vws != null && vws.size() > 0) {
                sourceView = vws.get(0);
            } else {
                List<ApricotView> gvws = viewManager.getViewByName(project, ApricotView.MAIN_VIEW);
                if (gvws != null && gvws.size() > 0) {
                    sourceView = gvws.get(0);
                }
            }
        }

        return sourceView;
    }

    private void cloneNonFKConstraints(ApricotTable table, ApricotTable clonedTable,
            Map<ApricotConstraint, ApricotConstraint> clonedConstraints) {
        for (ApricotConstraint constraint : table.getConstraints()) {
            if (constraint.getType() != ConstraintType.FOREIGN_KEY) {
                ApricotConstraint clonedConstraint = tableCloneManager.cloneConstraint(clonedTable, constraint, true);
                constraintManager.saveConstraint(clonedConstraint);
                clonedConstraints.put(constraint, clonedConstraint);
            }
        }
    }

    private void cloneRelationships(List<ApricotRelationship> relationships,
            Map<ApricotTable, ApricotTable> clonedTables, Map<ApricotConstraint, ApricotConstraint> clonedConstraints) {
        for (ApricotRelationship rel : relationships) {
            ApricotConstraint clonedParentConstraint = clonedConstraints.get(rel.getParent());
            if (clonedParentConstraint == null) {
                throw new RuntimeException(
                        "Unable to find the cloned Primary Key for [" + rel.getParent().getName() + "]");
            }

            ApricotConstraint childConstraint = rel.getChild();
            ApricotTable clonedChild = clonedTables.get(rel.getChild().getTable());
            if (clonedChild == null) {
                throw new RuntimeException(
                        "The cloned table for [" + rel.getChild().getTable().getName() + "] was not found");
            }
            ApricotConstraint clonedChildConstraint = tableCloneManager.cloneConstraint(clonedChild, childConstraint,
                    true);
            constraintManager.saveConstraint(clonedChildConstraint);

            ApricotRelationship clonedRel = new ApricotRelationship(clonedParentConstraint, clonedChildConstraint);
            relationshipManager.saveRelationship(clonedRel);
        }
    }

    private List<ApricotTable> getTables(List<String> sTables, ApricotSnapshot sourceSnapshot) {
        List<ApricotTable> ret = new ArrayList<>();

        sTables.forEach(ent -> {
            ApricotTable table = tableManager.getTableByName(ent, sourceSnapshot);
            if (table != null) {
                ret.add(table);
            }
        });

        return ret;
    }
}
