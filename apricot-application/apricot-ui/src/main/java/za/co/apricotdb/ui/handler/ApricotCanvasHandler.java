package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.support.excel.TableWrapper;
import za.co.apricotdb.support.excel.TableWrapper.ReportRow;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAjustor;
import za.co.apricotdb.viewport.align.SimpleGridEntityAllocator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.ApricotEntityBuilder;
import za.co.apricotdb.viewport.entity.EntityBuilder;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.relationship.ApricotRelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipType;

/**
 * The Canvas- related top level operations.
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotCanvasHandler {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    TabViewHandler tabViewHandler;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ObjectLayoutManager objectLayoutManager;

    /**
     * Populate the given canvas with the information of snapshot, using the
     * provided skin.
     */
    @Transactional
    public void populateCanvas(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        // clean the canvas first
        canvas.cleanCanvas();

        List<ApricotTable> tables = null;
        if (view.isGeneral()) {
            tables = tableManager.getTablesForSnapshot(snapshot);
        } else {
            tables = viewHandler.getTablesForView(snapshot, view);
        }
        populateCanvas(canvas, tables);

        // if view does not contain layout definitions, do default alignment
        if ((view.getObjectLayouts() == null || view.getObjectLayouts().size() == 0) && view.isGeneral()) {
            runAlignerAfterDelay(canvas, view);
        } else {
            runAllocationAfterDelay(canvas, view);
        }
    }

    /**
     * Update or add entity on view port.
     */
    @Transactional
    public void updateEntity(ApricotTable table, boolean newEntity) {
        if (newEntity) {
            addEntityOnViewPort(table);
        } else {
            updateEntityOnViewPort(table);
        }
    }

    /**
     * Remove entity and all related relationships from the canvas.
     */
    public void removeEntityFromCanvas(ApricotTable table, ApricotCanvas canvas) {
        ApricotEntity entity = canvas.findEntityByName(table.getName());
        if (entity != null) {
            canvas.removeElement(entity);
        }
    }

    /**
     * Populate the given canvas for the edited table in the given view.
     */
    private void populateCanvas(ApricotCanvas canvas, ApricotTable table, ApricotView view) {
        List<ApricotTable> tables = getRelatedTables(table, view);
        populateCanvas(canvas, tables);
    }

    private void populateCanvas(ApricotCanvas canvas, List<ApricotTable> tables) {
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);

        Map<String, List<FieldDetail>> fieldDetails = new HashMap<>();
        EntityBuilder eBuilder = new ApricotEntityBuilder(canvas);
        for (ApricotTable t : tables) {
            List<FieldDetail> fd = getFieldDetails(t, relationships);
            if (canvas.findEntityByName(t.getName()) == null) {
                ApricotElement element = eBuilder.buildEntity(t.getName(), fd, isSlave(fd));
                canvas.addElement(element);
            }
            fieldDetails.put(t.getName(), fd);
        }

        RelationshipBuilder rBuilder = new ApricotRelationshipBuilder(canvas);
        for (ApricotRelationship ar : relationships) {
            if (canvas.findRelationshipByName(ar.getName()) == null) {
                za.co.apricotdb.viewport.relationship.ApricotRelationship wpar = convertRelationship(ar, fieldDetails,
                        rBuilder);
                if (wpar != null) {
                    canvas.addElement(wpar);
                }
            }
        }
    }

    /**
     * Find all tables, related to the given one on the given view.
     */
    private List<ApricotTable> getRelatedTables(ApricotTable table, ApricotView view) {
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTable(table);
        List<ApricotTable> ret = new ArrayList<>();
        ret.add(table);
        for (ApricotRelationship r : relationships) {
            ApricotTable parent = r.getParent().getTable();
            ApricotTable child = r.getChild().getTable();
            if (objectLayoutManager.findLayoutByName(view, parent.getName()) != null && !ret.contains(parent)) {
                ret.add(parent);
            }
            if (objectLayoutManager.findLayoutByName(view, child.getName()) != null && !ret.contains(child)) {
                ret.add(child);
            }
        }

        return ret;
    }

    public void renameEntityOnCanvas(String oldName, String newName) {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                o.getCanvas().renameEntity(oldName, newName);
            }
        }
    }

    /**
     * Update an existing entity on the current view port.
     */
    private void updateEntityOnViewPort(ApricotTable table) {
        List<ApricotView> views = viewManager.getViewsByObjectName(projectManager.findCurrentProject(),
                LayoutObjectType.TABLE, table.getName());
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                if (views.contains(o.getView())) {
                    drawEntityOnCanvas(table, o.getCanvas(), o.getView(), false);
                    CanvasAllocationMap map = tabViewHandler.readCanvasAllocationMap(o.getView(), table);
                    runAllocationAfterDelay(o.getCanvas(), map);
                }
            }
        }
    }

    private void addEntityOnViewPort(ApricotTable table) {
        TabInfoObject c = getGeneralViewTabInfo();
        drawEntityOnCanvas(table, c.getCanvas(), c.getView(), true);
        if (!isCurrentViewGeneral()) {
            c = getCurrentViewTabInfo();
            drawEntityOnCanvas(table, c.getCanvas(), c.getView(), true);
        }
    }

    private boolean isCurrentViewGeneral() {
        Tab t = parentWindow.getProjectTabPane().getSelectionModel().getSelectedItem();
        if (t.getUserData() instanceof TabInfoObject) {
            TabInfoObject o = (TabInfoObject) t.getUserData();
            if (o.getView().getName().equals("Main View")) {
                return true;
            }
        }

        return false;
    }

    private TabInfoObject getGeneralViewTabInfo() {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                if (o.getView().getName().equals("Main View")) {
                    return o;
                }
            }
        }

        return null;
    }

    private TabInfoObject getCurrentViewTabInfo() {
        Tab t = parentWindow.getProjectTabPane().getSelectionModel().getSelectedItem();
        if (t.getUserData() instanceof TabInfoObject) {
            TabInfoObject o = (TabInfoObject) t.getUserData();

            return o;
        }

        return null;
    }

    /**
     * Update (or add from scratch) the given entity on the canvas.
     */
    private void drawEntityOnCanvas(ApricotTable table, ApricotCanvas canvas, ApricotView view, boolean newEntity) {
        if (!newEntity) {
            removeEntityFromCanvas(table, canvas);
        }
        populateCanvas(canvas, table, view);
    }

    private za.co.apricotdb.viewport.relationship.ApricotRelationship convertRelationship(ApricotRelationship r,
            Map<String, List<FieldDetail>> fieldDetails, RelationshipBuilder rBuilder) {
        String parentTable = r.getParent().getTable().getName();
        String childTable = r.getChild().getTable().getName();
        // both sides of relationship have to have some key(s) in the constraint
        if (r.getParent().getColumns().size() == 0 || r.getChild().getColumns().size() == 0) {
            return null;
        }
        String parentColumn = r.getParent().getColumns().get(0).getColumn().getName();
        String childColumn = r.getChild().getColumns().get(0).getColumn().getName();

        return rBuilder.buildRelationship(parentTable, childTable, parentColumn, childColumn,
                getRelationshipType(childColumn, fieldDetails.get(childTable)));
    }

    private RelationshipType getRelationshipType(String childColumn, List<FieldDetail> childFields) {
        RelationshipType ret = RelationshipType.OPTIONAL_NON_IDENTIFYING;

        for (FieldDetail fd : childFields) {
            if (childColumn.equals(fd.getName())) {
                if (fd.getConstraints() != null && fd.getConstraints().contains("PK")) {
                    ret = RelationshipType.IDENTIFYING;
                    break;
                } else if (fd.isMandatory()) {
                    ret = RelationshipType.MANDATORY_NON_IDENTIFYING;
                    break;
                }
            }
        }

        return ret;
    }

    private void runAlignerAfterDelay(ApricotCanvas canvas, ApricotView view) {
        Task<Void> sleeper = getSleeper(100);
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                AlignCommand aligner = new SimpleGridEntityAllocator(canvas);
                aligner.align();
                canvas.buildRelationships();

                CanvasAllocationMap allocationMap = canvas.getAllocationMap();
                tabViewHandler.saveCanvasAllocationMap(allocationMap, view);
            }
        });
        new Thread(sleeper).start();
    }

    private void runAllocationAfterDelay(ApricotCanvas canvas, ApricotView view) {
        Task<Void> sleeper = getSleeper(100);
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                CanvasAllocationMap map = tabViewHandler.readCanvasAllocationMap(view);
                canvas.applyAllocationMap(map, ElementType.ENTITY);
                canvas.buildRelationships();
                canvas.applyAllocationMap(map, ElementType.RELATIONSHIP);
                AlignCommand aligner = new CanvasSizeAjustor(canvas);
                aligner.align();
                canvas.buildRelationships();
            }
        });
        new Thread(sleeper).start();
    }

    private void runAllocationAfterDelay(ApricotCanvas canvas, CanvasAllocationMap map) {
        Task<Void> sleeper = getSleeper(100);
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                canvas.applyAllocationMap(map, ElementType.ENTITY);
                canvas.buildRelationships();
                canvas.applyAllocationMap(map, ElementType.RELATIONSHIP);
                AlignCommand aligner = new CanvasSizeAjustor(canvas);
                aligner.align();
                canvas.buildRelationships();
            }
        });
        new Thread(sleeper).start();
    }

    private Task<Void> getSleeper(int delay) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };

        return sleeper;
    }

    private List<FieldDetail> getFieldDetails(ApricotTable table, List<ApricotRelationship> relationships) {
        List<FieldDetail> ret = new ArrayList<>();
        TableWrapper wrapper = new TableWrapper(table, relationships);
        Map<String, ReportRow> rows = wrapper.getRowMap();
        List<ApricotColumn> cols = table.getColumns();

        for (ApricotColumn c : cols) {
            ReportRow rr = rows.get(c.getName());
            boolean isPK = false;
            if (rr.getConstraints() != null && rr.getConstraints().contains("PK")) {
                isPK = true;
            }
            FieldDetail fd = new FieldDetail(c.getName(), !c.isNullable(), rr.getColumnType(), isPK,
                    rr.getConstraints());
            ret.add(fd);
        }

        return ret;
    }

    private boolean isSlave(List<FieldDetail> details) {
        for (FieldDetail fd : details) {
            if (fd.getConstraints() != null && fd.getConstraints().contains("PK")
                    && fd.getConstraints().contains("FK")) {
                return true;
            }
        }

        return false;
    }
}
