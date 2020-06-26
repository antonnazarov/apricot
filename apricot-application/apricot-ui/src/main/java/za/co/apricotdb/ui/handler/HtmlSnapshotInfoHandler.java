package za.co.apricotdb.ui.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component produces HTML representation of the Snapshot information.
 *
 * @author Anton Nazarov
 * @since 26/06/2020
 */
@Component
public class HtmlSnapshotInfoHandler {

    public Map<String, String> getSnapshotValueMap(ApricotSnapshot snapshot) {
        Map<String, String> ret = new HashMap<>();

        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ret.put("snapshot_name", fixEmpty(snapshot.getName()));
        ret.put("snapshot_comment", fixEmpty(snapshot.getComment()));
        ret.put("snapshot_created", fixEmpty(df.format(snapshot.getCreated())));
        if (snapshot.getUpdated() != null) {
            ret.put("snapshot_updated", df.format(snapshot.getUpdated()));
        } else {
            ret.put("snapshot_updated", "NONE");
        }
        ret.put("table_list", fixEmpty(getTableList(snapshot, 300)));

        return ret;
    }

    public String getTableList(ApricotSnapshot snapshot, int lengthLimit) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String table : getTablesAsStrings(snapshot.getTables())) {
            if (lengthLimit > 0 && sb.length() >= lengthLimit) {
                sb.append("...");
                break;
            }

            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(table);
        }
        sb.append(" (").append(snapshot.getTables().size()).append(" tables)");

        return sb.toString();
    }

    private List<String> getTablesAsStrings(List<ApricotTable> tables) {
        List<String> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ret.add(t.getName());
        }
        Collections.sort(ret, String.CASE_INSENSITIVE_ORDER);

        return ret;
    }

    private String fixEmpty(String s) {
        if (StringUtils.isEmpty(s)) {
            return "NONE";
        }

        return s;
    }
}
