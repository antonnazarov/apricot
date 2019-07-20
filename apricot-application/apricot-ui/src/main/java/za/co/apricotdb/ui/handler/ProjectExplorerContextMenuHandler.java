package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import za.co.apricotdb.ui.ParentWindow;

/**
 * The handler of the context menu of the Project Explorer tree.
 * 
 * @author Anton Nazarov
 * @since 30/05/2019
 */
@Component
public class ProjectExplorerContextMenuHandler {

    @Autowired
    EntityContextMenuHandler entityMenuHandler;

    @Autowired
    ApricotProjectHandler projectHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ApplicationInitializer applicationInitializer;

    public ContextMenu buildContextMenu(TreeView<ProjectExplorerItem> projectsTreeView, boolean isMainView) {
        ContextMenu menu = new ContextMenu();

        TreeItem<ProjectExplorerItem> root = projectsTreeView.getRoot();
        List<TreeItem<ProjectExplorerItem>> selectedItems = projectsTreeView.getSelectionModel().getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) == root) {
            // the Project item was selected
            return buildProjectContextMenu(selectedItems.get(0).getValue());
        } else {
            List<ProjectExplorerItem> itm = getNonRootItems(selectedItems, root);
            boolean isInView = isInView(itm, isMainView);
            List<String> items = getTables(itm);
            if (isInView) {
                if (items.size() == 1) {
                    String name = items.get(0);
                    if (isMainView) {
                        menu.getItems().addAll(entityMenuHandler.buildEditEntityItem(name),
                                entityMenuHandler.buildDeleteEntityItem(items),
                                entityMenuHandler.buildSelectOnCanvasItem(items),
                                entityMenuHandler.buildSelectRelatedEntitiesItem(name));
                    } else {
                        menu.getItems().addAll(entityMenuHandler.buildEditEntityItem(name),
                                entityMenuHandler.buildDeleteEntityItem(items),
                                entityMenuHandler.buildRemoveFromViewItem(items),
                                entityMenuHandler.buildSelectOnCanvasItem(items),
                                entityMenuHandler.buildSelectRelatedEntitiesItem(name));
                    }
                } else {
                    if (isMainView) {
                        menu.getItems().addAll(entityMenuHandler.buildDeleteEntityItem(items),
                                entityMenuHandler.buildSelectOnCanvasItem(items));
                    } else {
                        menu.getItems().addAll(entityMenuHandler.buildDeleteEntityItem(items),
                                entityMenuHandler.buildRemoveFromViewItem(items),
                                entityMenuHandler.buildSelectOnCanvasItem(items));
                    }
                }
            } else {
                menu.getItems().addAll(entityMenuHandler.buildAddToViewItem(items));
            }
        }

        return menu;
    }

    private boolean isInView(List<ProjectExplorerItem> items, boolean isMainView) {
        if (isMainView) {
            return true;
        }

        for (ProjectExplorerItem t : items) {
            if (t.isIncluded()) {
                return true;
            }
        }

        return false;
    }

    private ContextMenu buildProjectContextMenu(ProjectExplorerItem projectItem) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(buildEditProjectItem(), buildDeleteProjectItem());

        return menu;
    }

    private List<ProjectExplorerItem> getNonRootItems(List<TreeItem<ProjectExplorerItem>> selectedItems,
            TreeItem<ProjectExplorerItem> root) {
        List<ProjectExplorerItem> ret = new ArrayList<>();

        for (TreeItem<ProjectExplorerItem> i : selectedItems) {
            if (i != root) {
                ret.add(i.getValue());
            }
        }

        return ret;
    }

    private List<String> getTables(List<ProjectExplorerItem> entities) {
        List<String> ret = new ArrayList<>();
        for (ProjectExplorerItem item : entities) {
            ret.add(item.getItemName());
        }

        return ret;
    }

    public MenuItem buildEditProjectItem() {
        MenuItem item = new MenuItem("Edit Project");
        item.setOnAction(e -> {
            try {
                projectHandler.createEditProjectForm(false, parentWindow.getMainAppPane());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return item;
    }

    public MenuItem buildDeleteProjectItem() {
        MenuItem item = new MenuItem("Delete Project");
        item.setOnAction(e -> {
            if (projectHandler.deleteCurrentProject()) {
                applicationInitializer.initializeDefault();
            }
        });

        return item;
    }
}
