package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.CompareScriptController;
import za.co.apricotdb.ui.comparator.CompareRowType;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    DialogFormHandler formHandler;

    @ApricotErrorLogger(title = "Unable to generate the align script")
    public void generateScript(TreeItem<CompareSnapshotRow> root) {
        if (!hasDifference(root)) {
            Alert alert = alertDecorator.getAlert("Compare Snapshot", "The selected Snapshots are equal",
                    AlertType.WARNING);
            alert.showAndWait();

            return;
        }

        List<TreeItem<CompareSnapshotRow>> diffItems = retrieveAlignItems(root, false);
        if (diffItems.isEmpty()) {
            Alert alert = alertDecorator.getAlert("Compare Snapshot",
                    "You need to checkmark the items with differences, which you'd like to generate the script for",
                    AlertType.WARNING);
            alert.showAndWait();

            return;
        }

        List<CompareSnapshotRow> differences = getDifferences(diffItems);

        try {
            createGenerateScriptForm(differences);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Get all items, the diff alignment script has to be generated for.
     */
    private List<TreeItem<CompareSnapshotRow>> retrieveAlignItems(TreeItem<CompareSnapshotRow> item,
                                                                  boolean isParentSelected) {
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

            String state = row.getState().toString();
            if (state.equals("ADD") || state.equals("REMOVE") || (state.equals("DIFF")
                    && (row.getType() == CompareRowType.COLUMN || row.getType() == CompareRowType.CONSTRAINT))) {
                ret.add(row);
            }
        }

        return ret;
    }

    public void createGenerateScriptForm(List<CompareSnapshotRow> differences) throws IOException {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-generate-diff-script.fxml",
                "/za/co/apricotdb/ui/toolbar/tbInsertScriptEnabled.png", "Generate Alignment Script");
        CompareScriptController controller = form.getController();
        controller.init(differences);
        form.show();
    }
}
