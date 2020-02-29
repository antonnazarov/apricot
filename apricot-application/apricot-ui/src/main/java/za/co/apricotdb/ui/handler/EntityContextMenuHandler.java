package za.co.apricotdb.ui.handler;

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
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
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

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ObjectAllocationHandler allocationHandler;

    @Autowired
    ApricotClipboardHandler clipboardHandler;

    @Autowired
    MainAppController appController;

    @Autowired
    NonTransactionalPort port;

    @Autowired
    ApricotViewHandler viewHandler;

    @ApricotErrorLogger(title = "Unable to create the Entity context menu")
    public void createEntityContextMenu(ApricotEntity entity, double x, double y) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        ApricotView view = canvasHandler.getCurrentView();

        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected.size() != 0) {
            ContextMenu contextMenu = new ContextMenu();
            if (selected.size() == 1) {
                // one entity was selected
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(buildEditEntityItem(entity.getTableName()), buildCopyItem(),
                            buildDeleteEntityItem(), buildRemoveFromViewItem(getNames(selected)),
                            buildSelectInListItem(getNames(selected)),
                            buildSelectRelatedEntitiesItem(getNames(selected)), new SeparatorMenuItem(),
                            buildRelationshipItem(true));
                } else {
                    contextMenu.getItems().addAll(buildEditEntityItem(entity.getTableName()), buildCopyItem(),
                            buildDeleteEntityItem(), buildSelectInListItem(getNames(selected)),
                            buildSelectRelatedEntitiesItem(getNames(selected)), new SeparatorMenuItem(),
                            buildRelationshipItem(true));
                }
            } else if (selected.size() > 1) {
                // a group of entities was selected
                if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
                    contextMenu.getItems().addAll(buildCopyItem(), buildDeleteEntityItem(),
                            buildRemoveFromViewItem(getNames(selected)), buildSelectInListItem(getNames(selected)),
                            buildSelectRelatedEntitiesItem(getNames(selected)),
                            new SeparatorMenuItem(), buildSameSizeWidthItem(), buildMinimizeWidthItem(),
                            buildAlignLeftItem(), buildAlignRightItem(), buildAlignUpItem(), buildAlignDownItem());
                } else {
                    contextMenu.getItems().addAll(buildCopyItem(), buildDeleteEntityItem(),
                            buildSelectInListItem(getNames(selected)),
                            buildSelectRelatedEntitiesItem(getNames(selected)),
                            new SeparatorMenuItem(),
                            buildSameSizeWidthItem(), buildMinimizeWidthItem(), buildAlignLeftItem(),
                            buildAlignRightItem(), buildAlignUpItem(), buildAlignDownItem());
                }
                if (selected.size() == 2) {
                    contextMenu.getItems().addAll(new SeparatorMenuItem(), buildRelationshipItem(false));
                }
            }

            contextMenu.setAutoHide(true);
            contextMenu.show(parentWindow.getWindow(), x, y);
        }
    }

    public MenuItem buildEditEntityItem(String entity) {
        MenuItem item = new MenuItem("Edit <Enter>");
        item.setOnAction(e -> {
            port.openEntityEditorForm(false, entity);
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
            viewHandler.removeEntitiesFromView(entities);
        });

        return item;
    }

    @ApricotErrorLogger(title = "Unable to add Entity to the View")
    public MenuItem buildAddToViewItem(List<String> entities) {
        MenuItem item = new MenuItem("Add to View");
        item.setOnAction(e -> {
            viewHandler.addEntityToView(entities);
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
            port.openRelationshipEditorForm(parentWindow.getProjectTabPane());
        });

        return item;
    }

    public MenuItem buildSelectOnCanvasItem(List<String> entities) {
        MenuItem item = new MenuItem("Select on Diagram");
        item.setOnAction(e -> {
            canvasHandler.makeEntitiesSelected(entities, true);
            allocationHandler.scrollToSelected(canvasHandler.getCurrentViewTabInfo());
        });

        return item;
    }

    public MenuItem buildCopyItem() {
        MenuItem item = new MenuItem("Copy <Ctrl+C>");
        item.setOnAction(e -> {
            clipboardHandler.copySelectedToClipboard();
        });

        return item;
    }

    public MenuItem buildSelectRelatedEntitiesItem(List<String> entities) {
        MenuItem item = new MenuItem("Select related Entities");
        item.setOnAction(e -> {
            canvasHandler.makeRelatedEntitiesSelected(entities);
            allocationHandler.scrollToSelected(canvasHandler.getCurrentViewTabInfo());
        });

        return item;
    }
}
