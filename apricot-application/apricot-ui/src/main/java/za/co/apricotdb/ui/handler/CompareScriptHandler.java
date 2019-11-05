package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.control.TreeItem;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;

/**
 * This component implements the logic of the diff alignment script generator.
 * 
 * @author Anton Nazarov
 * @since 05/11/2019
 */
@Component
public class CompareScriptHandler {

    public void generateScript(TreeItem<CompareSnapshotRow> root) {

    }

    List<TreeItem<CompareSnapshotRow>> retrieveAlignItems(TreeItem<CompareSnapshotRow> item) {
        List<TreeItem<CompareSnapshotRow>> ret = new ArrayList<>();
        for (TreeItem<CompareSnapshotRow> itm : item.getChildren()) {

        }

        return ret;
    }
}
