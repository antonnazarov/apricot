package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyEvent;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
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
    MainAppController appController;

    @Autowired
    EntityAlignHandler alignHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApricotClipboardHandler clipboardHandler;

    @Autowired
    ResetViewHandler resetViewHandler;
    
    @Autowired
    NonTransactionalPort port;

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
                port.openEntityEditorForm(false, ent.get(0).getTableName());
            }
            break;
        case A:
            if (event.isControlDown()) {
                // select all
                canvas.changeAllElementsStatus(ElementStatus.SELECTED, false);
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
            break;
        case F5:
            snapshotHandler.syncronizeSnapshot(false);
            break;
        case C:
            if (event.isControlDown()) {
                clipboardHandler.copySelectedToClipboard();
            }
            break;
        case INSERT:
            if (event.isControlDown()) {
                clipboardHandler.copySelectedToClipboard();
            } else if (event.isShiftDown()) {
                clipboardHandler.pasteSelectedFromClipboard();
            }
            break;
        case V:
            if (event.isControlDown()) {
                clipboardHandler.pasteSelectedFromClipboard();
            }
            break;
        case R:
            if (event.isControlDown()) {
                resetViewHandler.resetView(true);
            }
            break;
        default:
            break;
        }

        event.consume();
    }
}
