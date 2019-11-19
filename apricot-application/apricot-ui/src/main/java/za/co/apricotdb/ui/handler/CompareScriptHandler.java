package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.CompareScriptController;
import za.co.apricotdb.ui.comparator.AddColumnScript;
import za.co.apricotdb.ui.comparator.AddTableScript;
import za.co.apricotdb.ui.comparator.CompareRowType;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.comparator.RemoveTableScript;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component implements the logic of the diff alignment script generator.
 * 
 * @author Anton Nazarov
 * @since 05/11/2019
 */
@Component
public class CompareScriptHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    AddTableScript addTableScript;

    @Autowired
    RemoveTableScript removeTableScript;
    
    @Autowired
    AddColumnScript addColumnScript;

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
            ex.printStackTrace();
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
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-generate-diff-script.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Generate Alignment Script");
        Scene generateScriptScene = new Scene(window);
        dialog.setScene(generateScriptScene);
        dialog.getIcons().add(
                new Image(getClass().getResourceAsStream("/za/co/apricotdb/ui/toolbar/tbInsertScriptEnabled.png")));
        generateScriptScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        CompareScriptController controller = loader.<CompareScriptController>getController();
        controller.init(differences);
        dialog.show();
    }

    /**
     * Generate the differences alignment script using the collection of the
     * differences and the schema name (if any).
     */
    public String generate(List<CompareSnapshotRow> differences, String schema) {
        StringBuilder sb = new StringBuilder();

        sb.append(addTableScript.generate(differences, schema));
        sb.append(removeTableScript.generate(differences, schema));
        sb.append("\n");
        sb.append(addColumnScript.generate(differences, schema));

        return sb.toString();
    }
}
