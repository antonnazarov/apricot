package za.co.apricotdb.ui.handler;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ViewDetailLevel;
import za.co.apricotdb.support.excel.TableWrapper;
import za.co.apricotdb.support.excel.TableWrapper.ReportRow;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.log.ApricotInfoLogger;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAdjustor;
import za.co.apricotdb.viewport.align.SimpleGridEntityAllocator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.DefaultEntityBuilder;
import za.co.apricotdb.viewport.entity.EntityBuilder;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.relationship.ApricotRelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipBatchBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Canvas- related top level operations.
 *
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotCanvasHandler {

    @Autowired
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    TabViewHandler tabViewHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    RelatedEntitiesHandler relatedEntitiesHandler;

    @Autowired
    CanvasAlignHandler canvasAlignHandler;

    @Autowired
    AlertMessageDecorator alertMessageDecorator;

    @Autowired
    MapHandler mapHandler;

    @Autowired
    RelationshipConsistencyValidator relationshipValidator;

    private final RelationshipBatchBuilder relationshipsBuilder = new RelationshipBatchBuilder();

    /**
     * Populate the given canvas with the information of snapshot, using the
     * provided skin.
     */
    @Transactional
    @ApricotInfoLogger
    public List<ApricotTable> populateCanvas(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        ApricotView v = viewHandler.readApricotView(view);
        // clean the canvas first
        canvas.cleanCanvas();

        List<ApricotTable> tables;
        Map<String, RelatedEntityAbsent> absenceInfo = null;
        if (v.isGeneral()) {
            tables = tableManager.getTablesForSnapshot(snapshot);
        } else {
            tables = viewHandler.getTablesForView(snapshot, v);
            List<String> tbNames = new ArrayList<>();
            tables.forEach(t -> {
                tbNames.add(t.getName());
            });
            absenceInfo = relatedEntitiesHandler.getRelatedEntitiesAbsence(tbNames);
        }

        populateCanvas(canvas, tables, v.getDetailLevel(), absenceInfo);

        // if view does not contain layout definitions, do default alignment
        if (tables.size() > 0 && (v.getObjectLayouts() == null || v.getObjectLayouts().size() == 0) && v.isGeneral()) {
            if (alertMessageDecorator.requestYesNoOption("Align Diagram", "Align the objects on the current diagram automatically?", "Align")) {
                canvasAlignHandler.alignCanvasIslands();
            } else {
                runAlignerAfterDelay(canvas, v, 0.5).play();
            }
        } else {
            runAllocation(canvas, v, ElementType.ENTITY);
            Platform.runLater(() -> {
                runAllocation(canvas, v, ElementType.RELATIONSHIP);
            });
        }

        Platform.runLater(() -> {
            AlignCommand aligner = new CanvasSizeAdjustor(canvas);
            aligner.align();
        });

        return tables;
    }

    public ApricotCanvas getSelectedCanvas() {
        ApricotCanvas ret = null;
        Tab tab = parentWindow.getProjectTabPane().getSelectionModel().getSelectedItem();
        if (tab != null) {
            if (tab.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) tab.getUserData();
                ret = o.getCanvas();
            }
        }

        return ret;
    }

    public List<ApricotCanvas> getAllCanvases() {
        List<ApricotCanvas> ret = new ArrayList<>();
        for (Tab tab : parentWindow.getProjectTabPane().getTabs()) {
            if (tab.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) tab.getUserData();
                ret.add(o.getCanvas());
            }
        }

        return ret;
    }

    /**
     * Retrieve the canvas, which presents the Main View.
     */
    public ApricotCanvas getMainCanvas() {
        ApricotCanvas ret = null;
        for (Tab tab : parentWindow.getProjectTabPane().getTabs()) {
            if (tab.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) tab.getUserData();
                if (o.getView().getName().equals(ApricotView.MAIN_VIEW)) {
                    ret = o.getCanvas();
                    break;
                }
            }
        }

        return ret;
    }

    public ApricotView getCurrentView() {
        TabInfoObject tabInfo = getCurrentViewTabInfo();
        if (tabInfo != null) {
            return tabInfo.getView();
        }

        return null;
    }

    private void populateCanvas(ApricotCanvas canvas, List<ApricotTable> tables, ViewDetailLevel detailLevel,
                                Map<String, RelatedEntityAbsent> absenceInfo) {
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);

        Map<String, List<FieldDetail>> fieldDetails = new HashMap<>();
        EntityBuilder eBuilder = new DefaultEntityBuilder(canvas, detailLevel.name());
        for (ApricotTable t : tables) {
            List<FieldDetail> fd = getFieldDetails(t, relationships);
            if (canvas.findEntityByName(t.getName()) == null) {
                boolean isParentAbsent = false;
                boolean isChildAbsent = false;
                if (absenceInfo != null) {
                    RelatedEntityAbsent absence = absenceInfo.get(t.getName());
                    if (absence != null) {
                        isParentAbsent = absence.isParent();
                        isChildAbsent = absence.isChild();
                    }
                }
                ApricotElement element = eBuilder.buildEntity(t.getName(), fd, isSlave(fd), isParentAbsent,
                        isChildAbsent);
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
     * Override the given Entity on Canvas and redraw its relationships.
     */
    @Transactional
    public void overrideEntityOnCanvas(String tableName) {
        TabPane tp = parentWindow.getProjectTabPane();
        ApricotTable table = tableManager.getTableByName(tableName);

        //  scan through the tabs, replacing the entity and related relationships
        for (Tab tab : tp.getTabs()) {
            TabInfoObject tabInfo = TabInfoObject.getTabInfo(tab);
            if (tabInfo != null) {
                ApricotCanvas canvas = tabInfo.getCanvas();

                ApricotEntity entity = canvas.findEntityByName(tableName);
                if (entity != null) {
                    canvas.removeElement(entity);

                    List<ApricotTable> tables = relatedEntitiesHandler.getRelatedTablesOnView(table, tabInfo.getView());
                    RelatedEntityAbsent absence = relatedEntitiesHandler.getAbsenceForEntity(tableName, tabInfo.getView());
                    Map<String, RelatedEntityAbsent> absMap = new HashMap<>();
                    absMap.put(tableName, absence);
                    populateCanvas(canvas, tables, tabInfo.getView().getDetailLevel(), absMap);

                    CanvasAllocationMap allocMap = tabViewHandler.readCanvasAllocationMap(tabInfo.getView(), table);
                    if (allocMap != null) {
                        canvas.applyAllocationMap(allocMap, ElementType.ENTITY);
                        Platform.runLater(() -> {
                            makeEntitySelected(tabInfo, tableName, true);
                            relationshipsBuilder.buildRelationships(canvas);
                            canvas.applyAllocationMap(allocMap, ElementType.RELATIONSHIP);

                            relationshipsBuilder.buildRelationships(canvas);
                        });
                    }
                }
            }
        }
    }

    public void renameEntityOnCanvas(String oldName, String newName) {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                o.getCanvas().renameEntity(oldName, newName);
            }
        }
    }

    public TabInfoObject getCurrentViewTabInfo() {
        Tab t = parentWindow.getProjectTabPane().getSelectionModel().getSelectedItem();
        if (t.getUserData() instanceof TabInfoObject) {
            TabInfoObject o = (TabInfoObject) t.getUserData();

            return o;
        }

        return null;
    }

    public TabInfoObject getTabInfoOnView(ApricotView view) {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            TabInfoObject o = (TabInfoObject) t.getUserData();
            if (o.getView().equals(view)) {
                return o;
            }
        }

        return null;
    }

    public ApricotView getViewOnTab(Tab tab) {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            if (t == tab) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                return o.getView();
            }
        }

        return null;
    }

    public Tab getTabOnCurrentView() {
        for (Tab t : parentWindow.getProjectTabPane().getTabs()) {
            TabInfoObject o = (TabInfoObject) t.getUserData();
            if (o.getView().isCurrent()) {
                return t;
            }
        }

        return null;
    }

    public void makeEntitySelected(TabInfoObject tabInfo, String tableName, boolean deselectOthers) {
        ApricotEntity entity = tabInfo.getCanvas().findEntityByName(tableName);
        if (entity != null) {
            if (deselectOthers) {
                tabInfo.getCanvas().changeAllElementsStatus(ElementStatus.DEFAULT, false);
            }
            entity.setElementStatus(ElementStatus.SELECTED);
        }
    }

    public void makeEntitiesSelected(List<String> tables, boolean deselectOthers) {
        ApricotCanvas canvas = getSelectedCanvas();
        makeEntitiesSelected(canvas, tables, deselectOthers);
    }

    public void makeEntitiesSelected(ApricotCanvas canvas, List<String> tables, boolean deselectOthers) {
        if (canvas != null && tables.size() > 0 && deselectOthers) {
            canvas.changeAllElementsStatus(ElementStatus.DEFAULT, false);
        }
        for (ApricotElement e : canvas.getElements()) {
            if (e.getElementType() == ElementType.ENTITY) {
                ApricotEntity ent = (ApricotEntity) e;
                if (tables.contains(ent.getTableName())) {
                    ent.setElementStatus(ElementStatus.SELECTED);
                }
            }
        }
    }

    @ApricotErrorLogger(title = "Unable to save the edited canvases")
    public void saveEditedCanvases() {
        for (Tab t : parentWindow.getViewsTabPane().getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                // save only changed canvas
                if (o.getCanvas().isCanvasChanged()) {
                    tabViewHandler.saveCanvasAllocationMap(o);
                    t.setStyle("-fx-font-weight: normal;");
                    o.getCanvas().setCanvasChanged(false);
                }
            }
        }
    }

    private za.co.apricotdb.viewport.relationship.ApricotRelationship convertRelationship(ApricotRelationship r,
                                                                                          Map<String, List<FieldDetail>> fieldDetails,
                                                                                          RelationshipBuilder rBuilder) {
        String parentTable = r.getParent().getTable().getName();
        String childTable = r.getChild().getTable().getName();
        // both sides of relationship have to have some key(s) in the constraint
        if (r.getParent().getColumns().size() == 0 || r.getChild().getColumns().size() == 0) {
            return null;
        }
        String parentColumn = r.getParent().getColumns().get(0).getColumn().getName();
        String childColumn = r.getChild().getColumns().get(0).getColumn().getName();

        boolean valid = relationshipValidator.validateRelationship(r);
        za.co.apricotdb.viewport.relationship.ApricotRelationship ret =
                rBuilder.buildRelationship(parentTable, childTable, parentColumn, childColumn, r.getId(),
                        getRelationshipType(childColumn, fieldDetails.get(childTable)), valid);
        if (!valid) {
            //  transfer the validation message into the viewport relationship object
            ret.setValidationMessage(r.getValidationMessage());
        }
        ret.setConstraintName(r.getChild().getName());

        return ret;
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

    private PauseTransition runAlignerAfterDelay(ApricotCanvas canvas, ApricotView view, double delay) {
        PauseTransition transition = new PauseTransition(Duration.seconds(delay));
        transition.setOnFinished(e -> {
            AlignCommand aligner = new SimpleGridEntityAllocator(canvas);
            aligner.align();
            canvas.buildRelationships();

            CanvasAllocationMap allocationMap = canvas.getAllocationMap();
            tabViewHandler.saveCanvasAllocationMap(allocationMap, view);
        });

        return transition;
    }

    private void runAllocation(ApricotCanvas canvas, ApricotView view,
                               ElementType elementType) {
        canvas.buildRelationships();
        CanvasAllocationMap map = tabViewHandler.readCanvasAllocationMap(view);
        canvas.applyAllocationMap(map, elementType);
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
