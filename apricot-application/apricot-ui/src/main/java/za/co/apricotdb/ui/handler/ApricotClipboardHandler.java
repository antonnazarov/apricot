package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import za.co.apricotdb.persistence.data.TableManager;
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
    public static final String CLIPBOARD_MARKER = "-- ApricotClipboardBuffer:";

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
            List<String> sEntities = getEntitesToPaste(header);
            duplicateHandler.duplicate(sEntities);

            snapshotHandler.syncronizeSnapshot(true);
        }
    }

    public boolean containsInfoToPaste(Clipboard clipboard) {
        String header = (String) clipboard.getContent(DataFormat.URL);
        if (header != null && header.startsWith(CLIPBOARD_MARKER)) {
            return true;
        }

        return false;
    }

    private List<String> getEntitesToPaste(String header) {
        String l = header.substring(CLIPBOARD_MARKER.length());
        String[] ents = l.split(";");
        List<String> ret = new ArrayList<>();
        for (String s : ents) {
            ret.add(s);
        }
        return ret;
    }

    private boolean createClipboardContent(Properties props) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        List<ApricotEntity> entities = canvas.getSelectedEntities();
        if (entities.isEmpty()) {
            return false;
        }

        boolean first = true;
        StringBuilder sb = new StringBuilder(CLIPBOARD_MARKER);
        for (ApricotEntity ent : entities) {
            if (first) {
                first = false;
            } else {
                sb.append(";");
            }
            sb.append(ent.getTableName());
        }

        String script = scriptHandler.generateCreateScript(ScriptSource.SELECTED, null);
        props.put(CLIPBOARD_HEADER, sb.toString());
        props.put(CLIPBOARD_BODY, script);

        return true;
    }
}
