package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.model.ApricotViewSerializer;
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

    @Autowired
    DeleteSelectedHandler deleteSelectedHandler;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ApricotViewSerializer viewSerializer;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    EntityAlignHandler alignHandler;

    public void createEntityContextMenu(ApricotEntity entity, double x, double y) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        ApricotView view = canvasHandler.getCurrentView();

        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected.size() != 0) {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeFromView = new MenuItem("Remove from View");
            MenuItem selectInList = new MenuItem("Select in Project Explorer");

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
                initDeleteEntityEvent(deleteEntity);

            } else if (selected.size() > 1) {
                // a group of entities was selected
                MenuItem deleteSelected = new MenuItem("Delete Selected <Del>");
                MenuItem sameWidth = new MenuItem("Make Same Width");
                MenuItem minimizeWidth = new MenuItem("Minimize Width");
                MenuItem alignLeft = new MenuItem("Align Left <Ctrl+Left>");
                MenuItem alignRight = new MenuItem("Align Right <Ctrl+Right>");
                MenuItem alignUp = new MenuItem("Align Up <Ctrl+Up>");
                MenuItem alignDown = new MenuItem("Align Down <Ctrl+Down>");
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(deleteSelected, removeFromView, selectInList, new SeparatorMenuItem(),
                            sameWidth, minimizeWidth, alignLeft, alignRight, alignUp, alignDown);
                } else {
                    contextMenu.getItems().addAll(deleteSelected, selectInList, new SeparatorMenuItem(), sameWidth,
                            minimizeWidth, alignLeft, alignRight, alignUp, alignDown);
                }

                initDeleteEntityEvent(deleteSelected);
                initSameSizeWidth(sameWidth);
                initMinimizeWidth(minimizeWidth);
                initAlignLeft(alignLeft);
                initAlignRight(alignRight);
                initAlignUp(alignUp);
                initAlignDown(alignDown);
            }
            initSelectInListEvent(selectInList, selected);
            initRemoveFromView(removeFromView, selected);

            contextMenu.show(entity.getShape(), x, y);
        }
    }

    private void initEditEntityEvent(MenuItem item, ApricotEntity entity) {
        item.setOnAction(e -> {
            try {
                entityHandler.openEntityEditorForm(false, entity.getTableName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initDeleteEntityEvent(MenuItem item) {
        item.setOnAction(e -> {
            deleteSelectedHandler.deleteSelected();
        });
    }

    private void initSelectInListEvent(MenuItem item, List<ApricotEntity> entities) {
        item.setOnAction(e -> {
            List<String> sEnts = new ArrayList<>();
            for (ApricotEntity ent : entities) {
                sEnts.add(ent.getTableName());
            }
            treeViewHandler.selectEntities(sEnts);
        });
    }

    private void initRemoveFromView(MenuItem item, List<ApricotEntity> entities) {
        item.setOnAction(e -> {
            TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
            List<String> rem = new ArrayList<>();
            for (ApricotEntity ae : entities) {
                rem.add(ae.getTableName());
            }
            viewSerializer.deleteEntitiesFromView(rem, tabInfo);
            canvasHandler.populateCanvas(snapshotManager.getDefaultSnapshot(), canvasHandler.getCurrentView(),
                    canvasHandler.getSelectedCanvas());
        });
    }

    private void initAlignLeft(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.LEFT);
        });
    }

    private void initAlignRight(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.RIGHT);
        });
    }

    private void initAlignUp(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.TOP);
        });
    }

    private void initAlignDown(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.BOTTOM);
        });
    }

    private void initMinimizeWidth(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignEntitySize(true);
        });
    }

    private void initSameSizeWidth(MenuItem item) {
        item.setOnAction(e -> {
            alignHandler.alignEntitySize(false);
        });
    }
}
