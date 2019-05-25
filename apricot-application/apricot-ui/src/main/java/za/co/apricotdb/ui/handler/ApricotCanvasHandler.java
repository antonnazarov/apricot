package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
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
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAjustor;
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
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
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
    public void populateCanvas(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        // clean the canvas first
        canvas.cleanCanvas();

        List<ApricotTable> tables = null;
        if (view.isGeneral()) {
            tables = tableManager.getTablesForSnapshot(snapshot);
        } else {
            tables = viewHandler.getTablesForView(snapshot, view);
        }
        populateCanvas(canvas, tables, view.getDetailLevel());

        // if view does not contain layout definitions, do default alignment
        if ((view.getObjectLayouts() == null || view.getObjectLayouts().size() == 0) && view.isGeneral()) {
            runAlignerAfterDelay(canvas, view, 0.1).play();
        } else {
            runAllocationAfterDelay(canvas, view, 0, ElementType.ENTITY).play();
            runAllocationAfterDelay(canvas, view, 0.3, ElementType.RELATIONSHIP).play();
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

    /**
     * Center the Entity in the visible part of the screen.
     */
    public void centerEntityOnView(TabInfoObject tabInfo, String tableName) {
        ApricotCanvas canvas = tabInfo.getCanvas();
        ApricotEntity entity = canvas.findEntityByName(tableName);
        if (entity != null) {
            ApricotEntityShape shape = entity.getEntityShape();
            if (shape != null) {
                double canvasWidth = ((Pane) canvas).getWidth();
                double canvasHeight = ((Pane) canvas).getHeight();
                ScrollPane scroll = tabInfo.getScroll();
                double visibleWidth = scroll.getWidth();
                double visibleHeight = scroll.getHeight();
                double horizontalBias = scroll.getHvalue();
                double verticalBias = scroll.getVvalue();

                double layoutX = 0;
                if (canvasWidth > visibleWidth) {
                    layoutX = (canvasWidth - visibleWidth) * horizontalBias + visibleWidth / 2;
                } else {
                    layoutX = visibleWidth / 2;
                }
                double layoutY = 0;
                if (canvasHeight > visibleHeight) {
                    layoutY = (canvasHeight - visibleHeight) * verticalBias + visibleHeight / 2;
                } else {
                    layoutY = visibleHeight / 2;
                }
                shape.setLayoutX(layoutX);
                shape.setLayoutY(layoutY);
            }
            tabViewHandler.saveCanvasAllocationMap(tabInfo);
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

    public void makeEntitySelected(TabInfoObject tabInfo, String tableName, boolean deselectOthers) {
        ApricotEntity entity = tabInfo.getCanvas().findEntityByName(tableName);
        if (entity != null) {
            if (deselectOthers) {
                tabInfo.getCanvas().changeAllElementsStatus(ElementStatus.DEFAULT);
            }
            entity.setElementStatus(ElementStatus.SELECTED);
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

            AlignCommand aligner = new CanvasSizeAjustor(canvas);
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
