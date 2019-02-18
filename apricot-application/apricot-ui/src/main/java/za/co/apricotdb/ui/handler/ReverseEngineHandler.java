package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
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
    
    public void openScanResultForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-re-tables-list.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();
        ReversedTablesController controller = loader.<ReversedTablesController>getController();
        
        // @TODO fake 
        List<ApricotTable> ftab = new ArrayList<>();
        ApricotTable t = new ApricotTable("test_table_one", null, null, null);
        ftab.add(t);
        t = new ApricotTable("test_table_two", null, null, null);
        ftab.add(t);
        t = new ApricotTable("test_table_three", null, null, null);
        ftab.add(t);
        t = new ApricotTable("test_table_four", null, null, null);
        ftab.add(t);
        t = new ApricotTable("test_table_five", null, null, null);
        ftab.add(t);
        
        String[] fbl = new String[] {"test_table_three", "test_table_five"};
        controller.init(ftab, fbl);
        
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Result of the scan");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("bw-reverse-s1.jpg")));
        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        dialog.show();
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
