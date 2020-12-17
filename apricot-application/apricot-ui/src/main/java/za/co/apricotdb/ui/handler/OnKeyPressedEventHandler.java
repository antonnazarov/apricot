package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;

import java.util.List;
import java.util.Map;

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

    @Autowired
    QuickViewHandler quickViewHandler;

    @Autowired
    HtmlViewHandler htmlViewHandler;

    @Autowired
    HtmlEntityInfoHandler htmlEntityInfoHandler;

    @Autowired
    ObjectAllocationHandler allocationHandler;

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
                snapshotHandler.synchronizeSnapshot(false);
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
            case Q:
                if (event.isControlDown()) {
                    quickViewHandler.createQuickView();
                }
                break;
            case SPACE:
                handleSpace();
            case TAB:
                //  the <TAB> button moves to the next selected entity on the right side
                //  the <CTRL>+<TAB> moves the visible area to the next right selected entity
                TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
                if (event.isControlDown()) {
                    allocationHandler.scrollToNextEntity(tabInfo, false);
                } else {
                    allocationHandler.scrollToNextEntity(tabInfo, true);
                }
            default:
                break;
        }

        event.consume();
    }

    public void handleSpace() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> entities = canvas.getSelectedEntities();
        if (entities.size() == 1) {
            Map<String, String> values = htmlEntityInfoHandler.getEntityValueMap(entities.get(0).getTableName());
            htmlViewHandler.showHtmlViewForm(values, "table-info.html", "Entity Info");
        }
    }
}
