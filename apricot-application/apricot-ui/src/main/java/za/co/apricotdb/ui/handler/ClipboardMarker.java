package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * This bean keeps parsed elements of the current clipboard marker.
 * 
 * @author Anton Nazarov
 * @since 05/11/2019
 */
public class ClipboardMarker {
    
    public static final String CLIPBOARD_MARKER = "ApricotClipboard";
    public static final String CLIPBOARD_HEADER_DIVIDER = "|";
    public static final String CLIPBOARD_SNAPSHOT_NAME = "snapshot_name";
    public static final String CLIPBOARD_VIEW_NAME = "view_name";
    public static final String CLIPBOARD_TABLES = "clipboard_tables";

    private String snapshotName;
    private String viewName;
    private List<String> tables = new ArrayList<>();

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public List<String> getTables() {
        return tables;
    }

    public boolean isConsistent() {
        return StringUtils.isNotEmpty(snapshotName) && StringUtils.isNotEmpty(viewName) && tables != null
                && tables.size() > 0;
    }

    @Override
    public String toString() {
        return "ClipboardMarker [snapshotName=" + snapshotName + ", viewName=" + viewName + ", tables=" + tables + "]";
    }
}
