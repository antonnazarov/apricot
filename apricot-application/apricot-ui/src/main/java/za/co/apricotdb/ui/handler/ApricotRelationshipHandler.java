package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.EditRelationshipController;
import za.co.apricotdb.ui.model.EditRelationshipModel;
import za.co.apricotdb.ui.model.EditRelationshipModelBuilder;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This component contains all Relationship high level business logic.
 * 
 * @author Anton Nazarov
 * @since 14/03/2019
 */
@Component
public class ApricotRelationshipHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    EditRelationshipModelBuilder modelBuilder;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Transactional
    public void openRelationshipEditorForm(TabPane viewsTabPane) throws IOException {

        // check if one or two entities were selected on the current canvas
        ApricotTable[] selectedTables = getRelatedTables(viewsTabPane);
        if (selectedTables == null) {
            Alert alert = alertDecorator.getErrorAlert("New Relationship",
                    "Please select one or two Entites you want to build a new Relationship in between");
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-relationship-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create a new Relationship");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("table-1-s1.jpg")));

        Scene newRelationshipcene = new Scene(window);
        dialog.setScene(newRelationshipcene);

        EditRelationshipController controller = loader.<EditRelationshipController>getController();

        EditRelationshipModel model = modelBuilder.buildModel(selectedTables);
        controller.init(model);

        dialog.show();
    }

    private ApricotTable[] getRelatedTables(TabPane viewsTabPane) {
        ApricotCanvas canvas = getCurrentCanvas(viewsTabPane);
        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected != null && (selected.size() == 1 || selected.size() == 2)) {
            ApricotTable[] ret = new ApricotTable[2];
            ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
            ApricotTable table = tableManager.getTableByName(selected.get(0).getTableName(), snapshot);
            ret[0] = table;
            if (selected.size() == 2) {
                table = tableManager.getTableByName(selected.get(1).getTableName(), snapshot);
            }
            ret[1] = table;

            return ret;
        }

        return null;
    }

    private ApricotCanvas getCurrentCanvas(TabPane viewsTabPane) {
        Tab tab = viewsTabPane.getSelectionModel().getSelectedItem();

        if (tab.getUserData() instanceof TabInfoObject) {
            TabInfoObject o = (TabInfoObject) tab.getUserData();
            return o.getCanvas();
        }

        return null;
    }
}