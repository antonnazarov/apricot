package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This component created and parses the clipboard marker, which has been
 * transferred between the copy and past operations.
 * 
 * @author Anton Nazarov
 * @since 05/11/2019
 */
@Component
public class ClipboardMarkerHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    public String buildMarker(List<ApricotEntity> entities) {
        StringBuilder sb = new StringBuilder(ClipboardMarker.CLIPBOARD_MARKER);
        sb.append("|");
        TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
        String viewName = tabInfo.getView().getName();
        String snapshotName = tabInfo.getSnapshot().getName();

        sb.append(ClipboardMarker.CLIPBOARD_SNAPSHOT_NAME).append("=").append(snapshotName).append("|");
        sb.append(ClipboardMarker.CLIPBOARD_VIEW_NAME).append("=").append(viewName).append("|");
        sb.append(ClipboardMarker.CLIPBOARD_TABLES).append("=");
        boolean first = true;
        for (ApricotEntity ent : entities) {
            if (first) {
                first = false;
            } else {
                sb.append(";");
            }
            sb.append(ent.getTableName());
        }

        return sb.toString();
    }

    public ClipboardMarker parseMarker(String marker) {
        ClipboardMarker mrk = new ClipboardMarker();
        if (!marker.contains(ClipboardMarker.CLIPBOARD_MARKER)) {
            return null;
        }

        String[] splt = marker.split("\\|");
        for (String s : splt) {
            if (s.contains(ClipboardMarker.CLIPBOARD_SNAPSHOT_NAME)) {
                String[] p = s.split("=");
                if (p.length == 2) {
                    mrk.setSnapshotName(p[1]);
                }
            } else if (s.contains(ClipboardMarker.CLIPBOARD_VIEW_NAME)) {
                String[] p = s.split("=");
                if (p.length == 2) {
                    mrk.setViewName(p[1]);
                }
            } else if (s.contains(ClipboardMarker.CLIPBOARD_TABLES)) {
                String[] p = s.split("=");
                if (p.length == 2) {

                    String[] tbls = p[1].split(";");
                    for (String t : tbls) {
                        mrk.getTables().add(t);
                    }
                }
            }
        }

        return mrk;
    }
}
