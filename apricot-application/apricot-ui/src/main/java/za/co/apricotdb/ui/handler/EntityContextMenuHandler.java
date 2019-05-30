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
import za.co.apricotdb.ui.ParentWindow;
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

    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @Autowired
    ParentWindow parentWindow;

    public void createEntityContextMenu(ApricotEntity entity, double x, double y) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        ApricotView view = canvasHandler.getCurrentView();

        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected.size() != 0) {
            ContextMenu contextMenu = new ContextMenu();
            if (selected.size() == 1) {
                // one entity was selected
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(buildEditEntityItem(entity.getTableName()), buildDeleteEntityItem(),
                            buildRemoveFromViewItem(getNames(selected)), buildSelectInListItem(getNames(selected)),
                            buildRelationshipItem(true));
                } else {
                    contextMenu.getItems().addAll(buildEditEntityItem(entity.getTableName()), buildDeleteEntityItem(),
                            buildSelectInListItem(getNames(selected)), buildRelationshipItem(true));
                }
            } else if (selected.size() > 1) {
                // a group of entities was selected
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(buildDeleteEntityItem(), buildRemoveFromViewItem(getNames(selected)),
                            buildSelectInListItem(getNames(selected)), new SeparatorMenuItem(),
                            buildSameSizeWidthItem(), buildMinimizeWidthItem(), buildAlignLeftItem(),
                            buildAlignRightItem(), buildAlignUpItem(), buildAlignDownItem(), new SeparatorMenuItem(),
                            buildRelationshipItem(false));
                } else {
                    contextMenu.getItems().addAll(buildDeleteEntityItem(), buildSelectInListItem(getNames(selected)),
                            new SeparatorMenuItem(), buildSameSizeWidthItem(), buildMinimizeWidthItem(),
                            buildAlignLeftItem(), buildAlignRightItem(), buildAlignUpItem(), buildAlignDownItem(),
                            new SeparatorMenuItem(), buildRelationshipItem(false));
                }
            }

            contextMenu.setAutoHide(true);
            contextMenu.show(entity.getShape(), x, y);
        }
    }

    public MenuItem buildEditEntityItem(String entity) {
        MenuItem item = new MenuItem("Edit <Enter>");
        item.setOnAction(e -> {
            try {
                entityHandler.openEntityEditorForm(false, entity);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return item;
    }

    public MenuItem buildDeleteEntityItem() {
        MenuItem item = new MenuItem("Delete <Del>");
        item.setOnAction(e -> {
            deleteSelectedHandler.deleteSelected();
        });

        return item;
    }

    public MenuItem buildDeleteEntityItem(List<String> entities) {
        MenuItem item = new MenuItem("Delete <Del>");
        item.setOnAction(e -> {
            deleteSelectedHandler.deleteEntities(entities);
        });

        return item;
    }

    public MenuItem buildSelectInListItem(List<String> entities) {
        MenuItem item = new MenuItem("Select in Project Explorer");
        item.setOnAction(e -> {
            treeViewHandler.selectEntities(entities);
        });

        return item;
    }

    private List<String> getNames(List<ApricotEntity> entities) {
        List<String> ret = new ArrayList<>();
        for (ApricotEntity ent : entities) {
            ret.add(ent.getTableName());
        }

        return ret;
    }

    public MenuItem buildRemoveFromViewItem(List<String> entities) {
        MenuItem item = new MenuItem("Remove from View");
        item.setOnAction(e -> {
            TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
            viewSerializer.deleteEntitiesFromView(entities, tabInfo);
            canvasHandler.populateCanvas(snapshotManager.getDefaultSnapshot(), canvasHandler.getCurrentView(),
                    canvasHandler.getSelectedCanvas());
        });

        return item;
    }

    public MenuItem buildAlignLeftItem() {
        MenuItem item = new MenuItem("Align Left <Ctrl+Left>");
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.LEFT);
        });

        return item;
    }

    public MenuItem buildAlignRightItem() {
        MenuItem item = new MenuItem("Align Right <Ctrl+Right>");
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.RIGHT);
        });

        return item;
    }

    public MenuItem buildAlignUpItem() {
        MenuItem item = new MenuItem("Align Up <Ctrl+Up>");
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.TOP);
        });

        return item;
    }

    public MenuItem buildAlignDownItem() {
        MenuItem item = new MenuItem("Align Down <Ctrl+Down>");
        item.setOnAction(e -> {
            alignHandler.alignSelectedEntities(Side.BOTTOM);
        });

        return item;
    }

    public MenuItem buildMinimizeWidthItem() {
        MenuItem item = new MenuItem("Minimize Width");
        item.setOnAction(e -> {
            alignHandler.alignEntitySize(true);
        });

        return item;
    }

    public MenuItem buildSameSizeWidthItem() {
        MenuItem item = new MenuItem("Make Same Width");
        item.setOnAction(e -> {
            alignHandler.alignEntitySize(false);
        });

        return item;
    }

    public MenuItem buildRelationshipItem(boolean autorelationship) {
        MenuItem item = null;
        if (autorelationship) {
            item = new MenuItem("New auto- relationship");
        } else {
            item = new MenuItem("New relationship");
        }
        item.setOnAction(e -> {
            try {
                relationshipHandler.openRelationshipEditorForm(parentWindow.getProjectTabPane());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return item;
    }
    
    public MenuItem buildSelectOnCanvasItem(List<String> entities) {
        MenuItem item = new MenuItem("Select on Diagram");
        item.setOnAction(e -> {

        });

        return item;
    }
}
