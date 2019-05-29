package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ProjectExplorerItem.ItemType;

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
    TableManager tableManager;

    @Autowired
    ApricotEntityHandler entityHandler;

    public void populate(ApricotProject project, ApricotSnapshot snapshot) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        TreeItem<ProjectExplorerItem> root = new TreeItem<>(buildItemNode(project.getName(), ItemType.PROJECT, true));
        root.getChildren().addAll(getTables(tables));
        root.setExpanded(true);
        TreeView<ProjectExplorerItem> tw = parentWindow.getProjectTreeView();
        tw.setRoot(root);
        tw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void selectEntity(String entity) {
        List<String> e = new ArrayList<>();
        e.add(entity);
        selectEntities(e);
    }

    public void selectEntities(List<String> entities) {
        TreeView<ProjectExplorerItem> tw = parentWindow.getProjectTreeView();
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
                    try {
                        entityHandler.openEntityEditorForm(false, t.getName());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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
}
