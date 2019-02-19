package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.text.WordUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ConnectionSqlServerController;
import za.co.apricotdb.ui.ReversedTablesController;
import za.co.apricotdb.ui.model.DatabaseConnectionModel;
import za.co.apricotdb.ui.model.DatabaseConnectionModelBuilder;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component is responsible for reverse engineering operation.
 */
@Component
public class ReverseEngineHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    DatabaseConnectionModelBuilder databaseConnectionModelBuilder;

    @Autowired
    ConsistencyHandler consistencyHandler;

    public boolean startReverseEngineering() {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        if (tables != null && tables.size() > 0) {
            // the reverse eng operation cannot be performed of the non empty snapshot
            Alert alert = getAlert("The snapshot \"" + snapshot.getName() + "\" contains entities.\n"
                    + "You only can perform the database reverse engineering into the EMPTY snapshot.");
            alert.showAndWait();

            return false;
        }

        try {
            openDatabaseConnectionForm(snapshot.getProject());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void openScanResultForm(MetaData metaData, String[] blackList) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-re-tables-list.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();
        ReversedTablesController controller = loader.<ReversedTablesController>getController();
        controller.init(metaData, blackList);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Result of the database scan");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("bw-reverse-s1.jpg")));
        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        dialog.show();
    }

    public boolean saveReversedObjects(List<ApricotTable> included, List<ApricotTable> excluded,
            List<ApricotRelationship> relationships) {
        boolean ret = true;
        Map<ApricotTable, ApricotTable> extraExclude = consistencyHandler.getFullConsistentExclude(excluded,
                relationships);
        if (!extraExclude.isEmpty()) {
            ButtonType yes = new ButtonType("Ok, exclude", ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(AlertType.WARNING, null, no, yes);
            alert.setTitle("Delete Snapshot");
            alert.setHeaderText(WordUtils.wrap(
                    "Some Parent tables were excluded from the resulting list. "
                            + "In order to maintain consistency of the scanned database structure, "
                            + "the corresponding Child tables will be excluded from the result:\n\n", 60)
                            + getMessageForExtraExclude(extraExclude));
            alertDecorator.decorateAlert(alert);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {

            } else {
                ret = false;
            }
        }

        return ret;
    }

    private String getMessageForExtraExclude(Map<ApricotTable, ApricotTable> extraExclude) {
        StringBuilder sb = new StringBuilder();

        Set<ApricotTable> keys = extraExclude.keySet();
        List<ApricotTable> children = keys.stream().collect(Collectors.toList());
        Collections.sort(children, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        for (ApricotTable t : children) {
            sb.append(t.getName()).append(" [").append(extraExclude.get(t).getName()).append("]\n");
        }

        return sb.toString();
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Start Reverse Engineering process");
        alert.setHeaderText(text);
        alertDecorator.decorateAlert(alert);

        return alert;
    }

    private void openDatabaseConnectionForm(ApricotProject project) throws IOException {
        ApricotTargetDatabase targetDatabase = ApricotTargetDatabase.valueOf(project.getTargetDatabase());
        DatabaseConnectionModel model = databaseConnectionModelBuilder.buildModel(project);

        Pane window = null;
        FXMLLoader loader = null;
        String title = null;
        switch (targetDatabase) {
        case MSSQLServer:
            loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-re-sqlserver.fxml"));
            loader.setControllerFactory(context::getBean);
            window = loader.load();

            title = "Connect to SQLServer database";
            ConnectionSqlServerController controller = loader.<ConnectionSqlServerController>getController();
            controller.init(model);
            break;
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("bw-reverse-s1.jpg")));
        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        dialog.show();
    }
}
