package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import za.co.apricotdb.ui.RelatedEntitiesController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
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

    @Resource
    ApplicationContext context;

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

    /**
     * Populate the given canvas with the information of snapshot, using the
     * provided skin.
     */
    @Transactional
    public List<ApricotTable> populateCanvas(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        ApricotView v = viewHandler.readApricotView(view);
        // clean the canvas first
        canvas.cleanCanvas();

        List<ApricotTable> tables = null;
        if (v.isGeneral()) {
            tables = tableManager.getTablesForSnapshot(snapshot);
        } else {
            tables = viewHandler.getTablesForView(snapshot, v);
        }
        populateCanvas(canvas, tables, v.getDetailLevel());

        // if view does not contain layout definitions, do default alignment
        if ((v.getObjectLayouts() == null || v.getObjectLayouts().size() == 0) && v.isGeneral()) {
            runAlignerAfterDelay(canvas, v, 0.1).play();
        } else {
            runAllocationAfterDelay(canvas, v, 0, ElementType.ENTITY).play();
            runAllocationAfterDelay(canvas, v, 2.0, ElementType.RELATIONSHIP).play();
        }

        return tables;
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

    public ApricotView getCurrentView() {
        TabInfoObject tabInfo = getCurrentViewTabInfo();
        if (tabInfo != null) {
            return tabInfo.getView();
        }

        return null;
    }

    private void populateCanvas(ApricotCanvas canvas, List<ApricotTable> tables, ViewDetailLevel detailLevel) {
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);

        Map<String, List<FieldDetail>> fieldDetails = new HashMap<>();
        EntityBuilder eBuilder = new DefaultEntityBuilder(canvas, detailLevel.name());
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

    /**
     * Select tables, related to the given list.
     */
    @ApricotErrorLogger(title = "Unable to select the related Entities")
    public void makeRelatedEntitiesSelected(List<String> tables) {
        Set<String> selectTbl = new HashSet<>();
        Map<String, RelatedEntityAbsent> absentTbl = new HashMap<>(); // the tables, which have not been presented on the current view
        selectTbl.addAll(tables);
        ApricotCanvas canvas = getSelectedCanvas();
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

        makeEntitiesSelected(canvas, new ArrayList<>(selectTbl), false);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-related-entities.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Related Entities not in the View");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("table-1-s1.jpg")));

        Scene relatedEntitiesScene = new Scene(window);
        dialog.setScene(relatedEntitiesScene);
        relatedEntitiesScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        RelatedEntitiesController controller = loader.<RelatedEntitiesController>getController();
        controller.init(relatedEntities);

        dialog.show();
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
            Map<String, List<FieldDetail>> fieldDetails, RelationshipBuilder rBuilder) {
        String parentTable = r.getParent().getTable().getName();
        String childTable = r.getChild().getTable().getName();
        // both sides of relationship have to have some key(s) in the constraint
        if (r.getParent().getColumns().size() == 0 || r.getChild().getColumns().size() == 0) {
            return null;
        }
        String parentColumn = r.getParent().getColumns().get(0).getColumn().getName();
        String childColumn = r.getChild().getColumns().get(0).getColumn().getName();

        return rBuilder.buildRelationship(parentTable, childTable, parentColumn, childColumn, r.getId(),
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

    private PauseTransition runAllocationAfterDelay(ApricotCanvas canvas, ApricotView view, double delay,
            ElementType elementType) {
        PauseTransition transition = new PauseTransition(Duration.seconds(delay));
        transition.setOnFinished(e -> {
            CanvasAllocationMap map = tabViewHandler.readCanvasAllocationMap(view);
            canvas.applyAllocationMap(map, elementType);

            AlignCommand aligner = new CanvasSizeAdjustor(canvas);
            aligner.align();
        });

        return transition;
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
