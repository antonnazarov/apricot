package za.co.apricotdb.ui.handler;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ProjectExplorerItem.ItemType;
import za.co.apricotdb.ui.log.ApricotInfoLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component serves the TreeView on the left side of the main form.
 * 
 * @author Anton Nazarov
 * @since 20/03/2019
 */
@Component
public class TreeViewHandler {

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    EntityFilterHandler filterHandler;

    @Autowired
    NonTransactionalPort port;

    @ApricotInfoLogger
    public void populate(ApricotProject project, ApricotSnapshot snapshot) {
        List<ApricotTable> tables = new ArrayList<>();
        if (filterHandler.isFilterOn()) {
            tables = filterHandler.getFilterTables();
        } else {
            tables = tableManager.getTablesForSnapshot(snapshot);
        }

        StringBuilder projectItemText = new StringBuilder(project.getName()).append(" (")
                .append(project.getTargetDatabase()).append(")");
        TreeItem<ProjectExplorerItem> root = new TreeItem<>(
                buildItemNode(projectItemText.toString(), ItemType.PROJECT, true));
        root.getChildren().addAll(getTables(tables));
        root.setExpanded(true);
        TreeView<ProjectExplorerItem> tw = getTreeView();
        tw.setRoot(root);
        tw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void selectEntity(String entity) {
        List<String> e = new ArrayList<>();
        e.add(entity);
        selectEntities(e);
    }

    public void selectEntities(List<String> entities) {
        TreeView<ProjectExplorerItem> tw = getTreeView();
        tw.getSelectionModel().clearSelection();
        TreeItem<ProjectExplorerItem> root = tw.getRoot();
        Map<String, TreeItem<ProjectExplorerItem>> items = getTreeItemsMap(root.getChildren());
        for (String entity : entities) {
            TreeItem<ProjectExplorerItem> item = items.get(entity);
            if (item != null) {
                tw.getSelectionModel().select(item);
            }
        }
    }

    /**
     * Scan the InternetExplorer list and "select" entities included into the
     * current view.
     */
    public void markEntitiesIncludedIntoView(ApricotView view) {
        deselectProjectExplorerItems();

        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();

        TreeView<ProjectExplorerItem> tw = getTreeView();
        TreeItem<ProjectExplorerItem> root = tw.getRoot();
        Map<String, TreeItem<ProjectExplorerItem>> items = getTreeItemsMap(root.getChildren());
        for (ApricotTable t : viewHandler.getTablesForView(snapshot, view)) {
            TreeItem<ProjectExplorerItem> item = items.get(t.getName());
            if (item != null) {
                item.getValue().setIncluded(true);
            }
        }
    }

    /**
     * Sort entities in the Project Explorer list to show the entities, included
     * into the current view first, and then other entities, not included into the
     * view.
     */
    public void sortEntitiesByView() {
        TreeView<ProjectExplorerItem> tw = getTreeView();
        TreeItem<ProjectExplorerItem> root = tw.getRoot();
        List<TreeItem<ProjectExplorerItem>> entities = root.getChildren();

        entities.sort((TreeItem<ProjectExplorerItem> e1, TreeItem<ProjectExplorerItem> e2) -> {
            if (e1.getValue().isIncluded() && !e2.getValue().isIncluded()) {
                return -1;
            } else if (!e1.getValue().isIncluded() && e2.getValue().isIncluded()) {
                return 1;
            }

            return e1.getValue().getItemName().toLowerCase().compareTo(e2.getValue().getItemName().toLowerCase());
        });
    }

    public void deselectProjectExplorerItems() {
        TreeView<ProjectExplorerItem> tw = getTreeView();
        TreeItem<ProjectExplorerItem> root = tw.getRoot();
        for (TreeItem<ProjectExplorerItem> item : root.getChildren()) {
            item.getValue().setIncluded(false);
        }
    }

    private Map<String, TreeItem<ProjectExplorerItem>> getTreeItemsMap(List<TreeItem<ProjectExplorerItem>> items) {
        Map<String, TreeItem<ProjectExplorerItem>> ret = new HashMap<>();

        for (TreeItem<ProjectExplorerItem> item : items) {
            ret.put(item.getValue().getItemName(), item);
        }

        return ret;
    }

    private List<TreeItem<ProjectExplorerItem>> getTables(List<ApricotTable> tables) {
        List<TreeItem<ProjectExplorerItem>> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ProjectExplorerItem pei = buildItemNode(t.getName(), ItemType.ENTITY, false);
            pei.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    port.openEntityEditorForm(false, t.getName());
                }
            });
            TreeItem<ProjectExplorerItem> item = new TreeItem<>(pei);
            ret.add(item);
        }

        return ret;
    }

    private ProjectExplorerItem buildItemNode(String itemName, ProjectExplorerItem.ItemType type, boolean included) {
        return new ProjectExplorerItem(itemName, type, included);
    }

    private TreeView<ProjectExplorerItem> getTreeView() {
        return parentWindow.getProjectTreeView();
    }
}
