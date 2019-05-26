package za.co.apricotdb.ui.undo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The manager of the undo operations on the layout undo type.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 */
@Component
public class LayoutUndoManager {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ParentWindow parent;

    @Autowired
    MainAppController appController;
    
    @Autowired
    ApplicationEventPublisher eventPublisher;

    /**
     * Perform the Layout- specific undo operation.
     */
    public void undo(UndoChunk chunk) {
        LayoutSavepoint lsp = (LayoutSavepoint) chunk;
        setCurrentLayout(lsp);
        ApricotCanvas canvas = selectUndoTab(lsp);
        if (canvas != null) {
            setScreenPosition(lsp.getScreenPosition());
            selectCanvasElements(canvas, lsp.getInvolvedElements());
            canvas.applyAllocationMap(lsp.getCurrentAllocationMap(), ElementType.ENTITY);
            canvas.applyAllocationMap(lsp.getCurrentAllocationMap(), ElementType.RELATIONSHIP);

            PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
            delay.setOnFinished(e -> canvas.buildRelationships());
            delay.play();
            
            CanvasChangedEvent event = new CanvasChangedEvent(canvas);
            eventPublisher.publishEvent(event);
        }
    }

    public UndoChunk buildChunk() {
        CanvasAllocationMap map = getAllocationMap();
        if (map != null) {
            return new LayoutSavepoint(getScreenPosition(), getElenments(), getCurrentTabName(), map);
        }

        return null;
    }

    /**
     * Read the current allocation map.
     */
    private CanvasAllocationMap getAllocationMap() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        if (canvas != null) {
            return canvas.getAllocationMap();
        }

        return null;
    }

    private ApricotCanvas selectUndoTab(LayoutSavepoint chunk) {

        TabPane tp = parent.getProjectTabPane();
        for (Tab tab : tp.getTabs()) {
            if (chunk.getCurrentTabName().equals(tab.getText())) {
                tp.getSelectionModel().select(tab);
                TabInfoObject tabInfo = TabInfoObject.getTabInfo(tab);
                return tabInfo.getCanvas();
            }
        }

        return null;
    }

    /**
     * Retrieve the horizontal and vertical positions of the ScrollPane of the
     * current Canvas.
     */
    private Point2D getScreenPosition() {
        Point2D ret = null;
        TabInfoObject o = canvasHandler.getCurrentViewTabInfo();
        if (o != null) {
            ScrollPane scroll = o.getScroll();
            ret = new Point2D(scroll.getHvalue(), scroll.getVvalue());
        }

        return ret;
    }

    private void setScreenPosition(Point2D pos) {
        TabInfoObject o = canvasHandler.getCurrentViewTabInfo();
        if (o != null) {
            ScrollPane scroll = o.getScroll();
            scroll.setHvalue(pos.getX());
            scroll.setVvalue(pos.getY());
        }
    }

    private List<String> getElenments() {
        List<String> ret = new ArrayList<>();

        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        for (ApricotEntity e : canvas.getSelectedEntities()) {
            ret.add("T->" + e.getTableName());
        }
        for (ApricotRelationship r : canvas.getSelectedRelationships()) {
            ret.add("R->" + r.getRelationshipName());
        }

        return ret;
    }

    private String getCurrentTabName() {
        String ret = null;

        Tab selectedTab = appController.getViewsTabPane().getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            ret = selectedTab.getText();
        }

        return ret;
    }

    private void selectCanvasElements(ApricotCanvas canvas, List<String> elementNames) {
        canvas.changeAllElementsStatus(ElementStatus.DEFAULT);

        for (String s : elementNames) {
            String[] splt = s.split("->");
            if (splt.length == 2) {
                switch (splt[0]) {
                case "T":
                    ApricotEntity entity = canvas.findEntityByName(splt[1]);
                    if (entity != null) {
                        entity.setElementStatus(ElementStatus.SELECTED);
                    }
                    break;

                case "R":
                    ApricotRelationship relationship = canvas.findRelationshipByName(splt[1]);
                    if (relationship != null) {
                        relationship.setElementStatus(ElementStatus.SELECTED);
                    }
                }
            }
        }
    }

    private void setCurrentLayout(LayoutSavepoint currentLayout) {
        parent.getApplicationData().setCurrentLayout(currentLayout);
    }
}
