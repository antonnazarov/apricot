package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This component serves the Entity Context Menu needs.
 * 
 * @author Anton Nazarov
 * @since 17/04/2019
 */
@Component
public class EntityContextMenuHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotEntityHandler entityHandler;

    public void createEntityContextMenu(ApricotEntity entity, double x, double y) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        ApricotView view = canvasHandler.getCurrentView();

        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected.size() != 0) {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeFromView = new MenuItem("Remove from View");
            MenuItem selectInList = new MenuItem("Select in List");
            if (selected.size() == 1) {
                // one entity was selected
                MenuItem editEntity = new MenuItem("Edit <Enter>");
                MenuItem deleteEntity = new MenuItem("Delete <Del>");
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(editEntity, deleteEntity, removeFromView, selectInList);
                } else {
                    contextMenu.getItems().addAll(editEntity, deleteEntity, selectInList);
                }
                
                initEditEntityEvent(editEntity, entity);
                
            } else if (selected.size() > 1) {
                // a group of entities was selected
                MenuItem deleteSelected = new MenuItem("Delete selected <Del>");
                MenuItem sameWidth = new MenuItem("Make same width");
                MenuItem alignLeft = new MenuItem("Align left <Ctrl+Left>");
                MenuItem alignRight = new MenuItem("Align right <Ctrl+Right>");
                MenuItem alignUp = new MenuItem("Align up <Ctrl+Up>");
                MenuItem alignDown = new MenuItem("Align down <Ctrl+Down>");
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(deleteSelected, removeFromView, selectInList, new SeparatorMenuItem(),
                            sameWidth, alignLeft, alignRight, alignUp, alignDown);
                } else {
                    contextMenu.getItems().addAll(deleteSelected, selectInList, new SeparatorMenuItem(), sameWidth,
                            alignLeft, alignRight, alignUp, alignDown);
                }
            }
            
            contextMenu.show(entity.getShape(), x, y);
        }
    }

    private void initEditEntityEvent(MenuItem editEntity, ApricotEntity entity) {
        editEntity.setOnAction(e -> {
            try {
                entityHandler.openEntityEditorForm(false, entity.getTableName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
