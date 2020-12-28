package za.co.apricotdb.ui.handler;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ScriptGenerateController.ScriptSource;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The handler of the OS Clipboard- operations.
 * 
 * @author Anton Nazarov
 * @since 27/06/2019
 */
@Component
public class ApricotClipboardHandler {

    public static final String CLIPBOARD_HEADER = "CLIPBOARD_HEADER";
    public static final String CLIPBOARD_BODY = "CLIPBOARD_BODY";

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    GenerateScriptHandler scriptHandler;

    @Autowired
    TableManager tableManager;

    @Autowired
    DuplicateHandler duplicateHandler;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    MainAppController appController;

    @Autowired
    ClipboardMarkerHandler markerHandler;

    @Transactional
    public void copySelectedToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        Properties props = new Properties();
        if (createClipboardContent(props)) {
            content.putUrl(props.getProperty(CLIPBOARD_HEADER));
            content.putString(props.getProperty(CLIPBOARD_BODY));
            clipboard.setContent(content);
        }
    }

    public void pasteSelectedFromClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (containsInfoToPaste(clipboard)) {
            String header = (String) clipboard.getContent(DataFormat.URL);
            ClipboardMarker marker = markerHandler.parseMarker(header);
            List<String> sEntities = marker.getTables();
            String sourceSnapshotName = marker.getSnapshotName();
            String sourceViewName = marker.getViewName(); // the name of the original view
            List<String> sDuplicated = duplicateHandler.duplicate(sEntities, sourceViewName, sourceSnapshotName);

            appController.save(null);
            snapshotHandler.synchronizeSnapshot(true);
            canvasHandler.makeEntitiesSelected(sDuplicated, true);
            selectInMainView(sDuplicated);
            treeViewHandler.selectEntities(sDuplicated);
        }
    }

    public boolean containsInfoToPaste(Clipboard clipboard) {
        String header = (String) clipboard.getContent(DataFormat.URL);
        if (header != null && header.startsWith(ClipboardMarker.CLIPBOARD_MARKER)) {
            return true;
        }

        return false;
    }

    public boolean containsInfoToPaste() {
        return containsInfoToPaste(Clipboard.getSystemClipboard());
    }

    private boolean createClipboardContent(Properties props) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> entities = canvas.getSelectedEntities();
        if (entities.isEmpty()) {
            return false;
        }

        String marker = markerHandler.buildMarker(entities);

        String script = scriptHandler.generateCreateScript(ScriptSource.SELECTED, null);
        props.put(CLIPBOARD_HEADER, marker);
        props.put(CLIPBOARD_BODY, script);

        return true;
    }

    private void selectInMainView(List<String> sDuplicated) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotView view = viewManager.getCurrentView(project);
        if (!view.getName().equals(ApricotView.MAIN_VIEW)) {
            ApricotView mainView = viewManager.getViewByName(project, ApricotView.MAIN_VIEW).get(0);
            TabInfoObject info = canvasHandler.getTabInfoOnView(mainView);
            if (info != null) {
                canvasHandler.makeEntitiesSelected(info.getCanvas(), sDuplicated, true);
            }
        }
    }
}
