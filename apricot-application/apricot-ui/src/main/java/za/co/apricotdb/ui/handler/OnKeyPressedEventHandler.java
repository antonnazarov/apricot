package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyEvent;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This is a main entry point into the key press handling in UI.
 * 
 * @author Anton Nazarov
 * @since 20/03/2019
 */
@Component
public class OnKeyPressedEventHandler implements EventHandler<KeyEvent> {

    @Autowired
    DeleteSelectedHandler deleteSelectedHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotEntityHandler entityHandler;

    @Autowired
    MainAppController appController;

    @Autowired
    EntityAlignHandler alignHandler;
    
    @Autowired
    ApricotUndoManager undoManager;

    @Override
    public void handle(KeyEvent event) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        switch (event.getCode()) {
        case DELETE:
            deleteSelectedHandler.deleteSelected();
            break;
        case ENTER:
            List<ApricotEntity> ent = canvas.getSelectedEntities();
            if (ent.size() == 1) {
                try {
                    entityHandler.openEntityEditorForm(false, ent.get(0).getTableName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        case A:
            if (event.isControlDown()) {
                // select all
                selectAllEntities(canvas);
            }
            break;
        case S:
            if (event.isControlDown()) {
                // save the unsaved changes in canvas
                appController.save(null);
            }
            break;
        case LEFT:
            if (event.isControlDown()) {
                alignHandler.alignSelectedEntities(Side.LEFT);
            }
            break;
        case RIGHT:
            if (event.isControlDown()) {
                alignHandler.alignSelectedEntities(Side.RIGHT);
            }
            break;
        case UP:
            if (event.isControlDown()) {
                alignHandler.alignSelectedEntities(Side.TOP);
            }
            break;
        case DOWN:
            if (event.isControlDown()) {
                alignHandler.alignSelectedEntities(Side.BOTTOM);
            }
            break;
        case Z:
            if (event.isControlDown()) {
                undoManager.undo();
            }
        default:
            break;
        }

        event.consume();
    }

    private void selectAllEntities(ApricotCanvas canvas) {
        List<ApricotElement> elements = canvas.getElements();
        for (ApricotElement elm : elements) {
            elm.setElementStatus(ElementStatus.SELECTED);
        }
    }
}
