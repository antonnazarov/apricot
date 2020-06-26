package za.co.apricotdb.ui.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This component produces HTML representation of the Project information.
 *
 * @author Anton Nazarov
 * @since 26/06/2020
 */
@Component
public class HtmlProjectInfoHandler {

    @Autowired
    HtmlSnapshotInfoHandler htmlSnapshotInfoHandler;

    /**
     * Compose the map of the give project values.
     */
    public Map<String, String> getProjectValueMap(ApricotProject project) {
        Map<String, String> ret = new HashMap<>();

        ret.put("project_name", fixEmpty(project.getName()));
        ret.put("database_type", fixEmpty(project.getTargetDatabase()));
        ret.put("erd_notation", fixEmpty(project.getErdNotation().getDefinition()));
        ret.put("description", fixEmpty(project.getDescription()));
        ret.put("snapshot_list", fixEmpty(getSnapshotList(project)));
        ret.put("table_list", fixEmpty(getTableList(project)));
        ret.put("black_list", fixEmpty(getBlackList(project)));

        return ret;
    }

    private String fixEmpty(String s) {
        if (StringUtils.isEmpty(s)) {
            return "NONE";
        }

        return s;
    }

    public String getSnapshotList(ApricotProject project) {
        StringBuilder sb = new StringBuilder();
        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        boolean first = true;
        List<ApricotSnapshot> sortedSnapshots = project.getSnapshots().stream()
                .sorted((s1, s2) -> s2.getCreated().compareTo(s1.getCreated()))
                .collect(Collectors.toList());
        for (ApricotSnapshot s: sortedSnapshots) {
            if (!first) {
                sb.append(";<br/>");
            } else {
                first = false;
            }
            sb.append(s.getName()).append(" (created: ").append(df.format(s.getCreated())).append(")");
        }

        return sb.toString();
    }

    public String getTableList(ApricotProject project) {
        ApricotSnapshot snapshot = project.getSnapshots().get(project.getSnapshots().size()-1);

        return htmlSnapshotInfoHandler.getTableList(snapshot, 300);
    }

    private String getBlackList(ApricotProject project) {
        for (ApricotProjectParameter pp : project.getParameters()) {
            if (pp.getName().equals(ProjectParameterManager.PROJECT_BLACKLIST_PARAM)) {
                return pp.getValue();
            }
        }

        return null;
    }
}
