package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component implements the logic of the diff alignment script generator.
 * 
 * @author Anton Nazarov
 * @since 05/11/2019
 */
@Component
public class CompareScriptHandler {
    
    @Autowired
    AlertMessageDecorator alertDecorator;

    public void generateScript(TreeItem<CompareSnapshotRow> root) {
        if (!hasDifference(root)) {
            Alert alert = alertDecorator.getAlert("Compare Snapshot", "The selected Snapshots are equal", AlertType.WARNING);
            alert.showAndWait();
            
            return;
        }
        
        List<TreeItem<CompareSnapshotRow>> diffItems = retrieveAlignItems(root, false);
    }

    /**
     * Get all items, the diff alignment script has to be generated for.
     */
    private List<TreeItem<CompareSnapshotRow>> retrieveAlignItems(TreeItem<CompareSnapshotRow> item, boolean isParentSelected) {
        List<TreeItem<CompareSnapshotRow>> ret = new ArrayList<>();

        if (!isParentSelected) {
            isParentSelected = isItemSelected(item);
        }
        
        for (TreeItem<CompareSnapshotRow> itm : item.getChildren()) {
            if ((isParentSelected || isItemSelected(itm)) && hasDifference(itm)) {
                ret.add(itm);
            }
            
            if (hasDifference(itm)) {
                ret.addAll(retrieveAlignItems(itm, isParentSelected));
            }
        }

        return ret;
    }

    private boolean isItemSelected(TreeItem<CompareSnapshotRow> item) {
        CompareSnapshotRow row = item.getValue();

        return row.getEqualize().getValue();
    }

    private boolean hasDifference(TreeItem<CompareSnapshotRow> item) {
        CompareSnapshotRow row = item.getValue();

        return row.isDifferent();
    }
    
    private List<CompareSnapshotRow> getDifferences(List<TreeItem<CompareSnapshotRow>> items) {
        List<CompareSnapshotRow> ret = new ArrayList<>();
        
        for (TreeItem<CompareSnapshotRow> item : items) {
            CompareSnapshotRow row = item.getValue();
            
            row.getState()
        }
        
        
        return ret;
    }
}
