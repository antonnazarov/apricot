package za.co.apricotdb.ui.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.script.GenericScriptGenerator;
import za.co.apricotdb.ui.DBScriptType;
import za.co.apricotdb.ui.ScriptGenerateController;
import za.co.apricotdb.ui.ScriptGenerateController.ScriptSource;
import za.co.apricotdb.ui.ScriptGenerateController.ScriptTarget;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The Generate Script- related business logic.
 * 
 * @author Anton Nazarov
 * @since 02/04/2019
 */
@Component
public class GenerateScriptHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    public void createGenerateScriptForm(DBScriptType scriptType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-generate-script.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        String formHeader = null;
        switch (scriptType) {
        case CREATE_SCRIPT:
            formHeader = "Generate CREATE- Script";
            break;
        case DELETE_SCRIPT:
            formHeader = "Generate DELETE- Script";
            break;
        case DROP_SCRIPT:
            formHeader = "Generate DROP- Script";
            break;
        default:
            break;
        }

        dialog.setTitle(formHeader);

        Scene generateScriptScene = new Scene(window);
        dialog.setScene(generateScriptScene);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("script-s1.JPG")));
        generateScriptScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        ScriptGenerateController controller = loader.<ScriptGenerateController>getController();
        controller.init(scriptType, getSelectedEntities().size() > 0);
        dialog.show();
    }

    @Transactional
    public boolean generateScript(ScriptSource source, ScriptTarget target, DBScriptType scriptType, Window window) {
        StringBuilder sb = new StringBuilder();

        List<ApricotTable> tables = getScriptTables(source);
        String operationName = null;
        for (ApricotTable table : tables) {
            List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTable(table);
            String sTable = null;
            switch (scriptType) {
            case CREATE_SCRIPT:
                sTable = scriptGenerator.createTableAll(table, relationships);
                operationName = "CREATE";
                break;
            default:
                break;
            }
            sb.append(sTable).append("\n");
        }

        return saveToFile(operationName, sb.toString(), window);
    }

    private boolean saveToFile(String operationName, String script, Window window) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();

        String outputDir = null;
        ApricotProjectParameter param = parameterManager.getParameterByName(projectManager.findCurrentProject(),
                ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR);
        if (param != null) {
            outputDir = param.getValue();
        } else {
            outputDir = System.getProperty("user.dir");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Generate " + operationName + " SQL Script");
        fileChooser.setInitialDirectory(new File(outputDir));
        fileChooser.setInitialFileName(operationName + "_" + snapshot.getName());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL script", "*.sql");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                try {
                    Files.write(Paths.get(file.getPath()), script.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Alert alert = alertDecorator.getAlert(operationName + " SQL Script",
                        "The SQL Script was successfully created in: " + file.getAbsolutePath(), AlertType.INFORMATION);
                alert.showAndWait();
                parameterManager.saveParameter(projectManager.findCurrentProject(),
                        ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR, file.getParent());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private List<ApricotTable> getScriptTables(ScriptSource source) {
        List<ApricotTable> ret = new ArrayList<>();
        switch (source) {
        case SELECTED:
            ret = getSelectedEntities();
            break;
        case CURRENT_VIEW:
            ret = viewHandler.getTablesForView(snapshotManager.getDefaultSnapshot(), canvasHandler.getCurrentView());
            break;
        case CURRENT_SNAPSHOT:
            ret = tableManager.getTablesForSnapshot(snapshotManager.getDefaultSnapshot());
            break;
        }

        return ret;
    }

    private List<ApricotTable> getSelectedEntities() {
        List<ApricotTable> ret = new ArrayList<>();

        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> result = canvas.getSelectedEntities();
        for (ApricotEntity ent : result) {
            ret.add(getTableForEntity(ent));
        }

        return ret;
    }

    private ApricotTable getTableForEntity(ApricotEntity entity) {
        return tableManager.getTableByName(entity.getTableName(), snapshotManager.getDefaultSnapshot());
    }
}
