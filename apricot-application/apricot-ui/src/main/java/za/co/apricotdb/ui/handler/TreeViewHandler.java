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
        TreeItem<String> root = new TreeItem<>(project.getName());
        root.getChildren().addAll(getTables(tables));
        root.setExpanded(true);
        TreeView<String> tw = parentWindow.getProjectTreeView();
        tw.setRoot(root);
        tw.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                TreeItem<String> item = tw.getSelectionModel().getSelectedItem();
                if (item != tw.getRoot()) {
                    // the table was selected by double click
                    try {
                        entityHandler.openEntityEditorForm(false, item.getValue());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        tw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void selectEntity(String entity) {
        List<String> e = new ArrayList<>();
        e.add(entity);
        selectEntities(e);
    }
    
    public void selectEntities(List<String> entities) {
        TreeView<String> tw = parentWindow.getProjectTreeView();
        tw.getSelectionModel().clearSelection();
        TreeItem<String> root = tw.getRoot();
        Map<String, TreeItem<String>> items = getTreeItemsMap(root.getChildren());
        for (String entity : entities) {
            TreeItem<String> item = items.get(entity);
            if (item != null) {
                tw.getSelectionModel().select(item);
            }
        }
    }
    
    private Map<String, TreeItem<String>> getTreeItemsMap(List<TreeItem<String>> items) {
        Map<String, TreeItem<String>> ret = new HashMap<>();

        for (TreeItem<String> item : items) {
            ret.put(item.getValue(), item);
        }

        return ret;
    }

    private List<TreeItem<String>> getTables(List<ApricotTable> tables) {
        List<TreeItem<String>> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ret.add(new TreeItem<String>(t.getName()));
        }

        return ret;
    }

}
