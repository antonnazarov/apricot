package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.RelatedEntitiesController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This component contains all "Related Entities" business logic.
 *
 * @author Anton Nazarov
 * @since 13/04/2020
 */
@Component
public class RelatedEntitiesHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

    /**
     * Select tables, related to the given list.
     */
    @ApricotErrorLogger(title = "Unable to select the related Entities")
    public void makeRelatedEntitiesSelected(List<String> tables) {
        Set<String> selectTbl = new HashSet<>();
        Map<String, RelatedEntityAbsent> absentTbl = new HashMap<>(); // the tables, which have not been presented on
        // the current view
        selectTbl.addAll(tables);
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        for (String tableName : tables) {
            ApricotTable table = tableManager.getTableByName(tableName);
            for (ApricotRelationship r : relationshipManager.getRelationshipsForTable(table)) {
                String relTable = null;
                boolean isParent = false;
                if (r.getChild().getTable().equals(table)) {
                    relTable = r.getParent().getTable().getName();
                    isParent = true;
                } else {
                    relTable = r.getChild().getTable().getName();
                }

                if (canvas.findEntityByName(relTable) != null) {
                    selectTbl.add(relTable);
                } else {
                    RelatedEntityAbsent absent = absentTbl.get(relTable);
                    if (absent == null) {
                        absent = new RelatedEntityAbsent(relTable);
                        absentTbl.put(relTable, absent);
                    }
                    if (isParent) {
                        absent.setParent(true);
                    } else {
                        absent.setChild(true);
                    }
                }
            }
        }

        canvasHandler.makeEntitiesSelected(canvas, new ArrayList<>(selectTbl), false);

        // if there are related tables, not presented on the current view, show the form
        // with the list of such tables
        if (!absentTbl.isEmpty()) {
            createRelatedEntitiesForm(new ArrayList<>(absentTbl.values()));
        }
    }

    /**
     * Show the pop-up window with the Related Entities.
     */
    @ApricotErrorLogger(title = "Unable to open the list of related Entities")
    public void createRelatedEntitiesForm(List<RelatedEntityAbsent> relatedEntities) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-related-entities.fxml",
                "table-1-s1.jpg", "Related Entities not in the View");
        RelatedEntitiesController controller = form.getController();
        controller.init(relatedEntities);

        form.show();
    }

    /**
     * For each entity in the list, calculate the absence information for related
     * and absent parent and child entities.
     */
    public Map<String, RelatedEntityAbsent> getRelatedEntitiesAbsence(List<String> tables) {
        Map<String, RelatedEntityAbsent> ret = new HashMap<>();

        for (String tableName : tables) {
            ApricotTable table = tableManager.getTableByName(tableName);
            RelatedEntityAbsent absence = getAbsenceForTable(table, tables);
            ret.put(tableName, absence);
        }

        return ret;
    }

    @Transactional
    public RelatedEntityAbsent getAbsenceForEntity(String tableName, ApricotView view) {
        RelatedEntityAbsent absence = new RelatedEntityAbsent(tableName);

        ApricotTable table = tableManager.getTableByName(tableName);
        ApricotView v = viewManager.getViewById(view.getId());
        if (v != null) {
            List<ApricotTable> viewTables = viewHandler.getTablesForView(snapshotManager.getDefaultSnapshot(), v);
            if (viewTables != null) {
                List<String> tables = new ArrayList<>();
                for (ApricotTable t : viewTables) {
                    tables.add(t.getName());
                }

                absence = getAbsenceForTable(table, tables);
            }
        }

        return absence;
    }

    private RelatedEntityAbsent getAbsenceForTable(ApricotTable table, List<String> tables) {
        RelatedEntityAbsent absence = new RelatedEntityAbsent(table.getName());
        for (ApricotRelationship r : relationshipManager.getRelationshipsForTable(table)) {
            boolean isChild = r.getChild().getTable().getName().equals(table.getName()); // the side of the relationship
            if (isChild && !tables.contains(r.getParent().getTable().getName())) {
                absence.setParent(true);
            }
            if (!isChild && !tables.contains(r.getChild().getTable().getName())) {
                absence.setChild(true);
            }
        }

        return absence;
    }

    /**
     * Retrieve all related entities for the given one, which have been presented on the current view.
     */
    @Transactional
    public List<ApricotTable> getRelatedTablesOnView(ApricotTable table, ApricotView view) {
        List<ApricotTable> ret = new ArrayList<>();

        List<ApricotTable> viewTables = viewHandler.getTablesForView(snapshotManager.getDefaultSnapshot(), view);
        for (ApricotRelationship rel : relationshipManager.getRelationshipsForTable(table)) {
            ApricotTable relatedTable = rel.getChild().getTable();
            if (table.equals(rel.getChild().getTable())) {
                relatedTable = rel.getParent().getTable();
            }

            if (viewTables.contains(relatedTable) &&
                    !ret.contains(relatedTable)) {
                ret.add(relatedTable);
            }

            if (!ret.contains(table)) {
                ret.add(table);
            }
        }

        return ret;
    }
}
