package za.co.apricotdb.ui.undo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The essential UNDO logic is represented by this component.
 * 
 * @author Anton Nazarov
 * @since 23/04/2019
 *
 */
@Component
public class ApricotUndoManager {

    @Autowired
    ParentWindow parent;

    @Autowired
    LayoutUndoManager layoutUndoManager;

    @Autowired
    ObjectUndoManager objectUndoManager;

    @Autowired
    ApricotCanvasHandler canvasHandler;
    
    @Autowired
    MainAppController appController;    

    public static final int UNDO_STACK_SIZE = 10;

    /**
     * Perform the UNDO operation.
     */
    public void undo() {
        UndoChunk chunk = getUndoBuffer().removeFirst();
        if (chunk != null) {
            switch (chunk.getUndoType()) {
            case LAYOUT_CHANGED:
                layoutUndoManager.undo(chunk);
                break;
            case OBJECT_EDITED:
                objectUndoManager.undo(chunk);
                break;
            }

            undo(chunk);
        } else {
            // signal the main interface, that there is
            // no items in the undo buffer
        }
    }

    /**
     * Add the savepoint to the undo buffer.
     */
    public void addSavepoint(UndoType type) {
        UndoChunk chunk = null;
        switch (type) {
        case LAYOUT_CHANGED:
            chunk = layoutUndoManager.buildChunk(getScreenPosition(), getElenments(), getCurrentTabName());
            parent.getApplicationData().saveCurrentLayout((LayoutSavepoint) chunk);
            break;
        case OBJECT_EDITED:
            chunk = objectUndoManager.buildChunk(getScreenPosition(), getElenments(), getCurrentTabName());
            getUndoBuffer().addFirst(chunk);
            break;
        }

        // @TODO inform app that there is an undo chunk in the undo buffer
    }

    public void resetUndoBuffer() {
        getUndoBuffer().clear();
    }

    private ArrayDeque<UndoChunk> getUndoBuffer() {
        return parent.getApplicationData().getUndoBuffer();
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

    private List<String> getElenments() {
        List<String> ret = new ArrayList<>();

        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        for (ApricotEntity e : canvas.getSelectedEntities()) {
            ret.add("T->" + e.getTableName());
        }
        for (ApricotRelationship e : canvas.getSelectedRelationships()) {
            ret.add("R->" + e.getRelationshipName());
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

    /**
     * Perform the generic (common for all types) undo operation. This would
     * include: undo current Tab; undo the screen position; undo selected elements
     */
    private void undo(UndoChunk chunk) {

    }
}
