package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This component produces HTML representation of the Entity information.
 *
 * @author Anton Nazarov
 * @since 26/06/2020
 */
@Component
public class HtmlEntityInfoHandler {

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    TableManager tableManager;

    @Transactional
    public Map<String, String> getEntityValueMap(String entity) {
        ApricotTable table = tableManager.getTableByName(entity);

        return getEntityValueMap(table);
    }

    @Transactional
    public Map<String, String> getEntityValueMap(ApricotTable entity) {
        Map<String, String> ret = new HashMap<>();

        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTable(entity);
        ret.put("table_name", entity.getName());
        ret.put("table_columns", getColumns(entity));
        ret.put("table_constraints", getConstraints(entity));
        ret.put("table_relationships", getHtmlRow(new String[]{getRelationshipTables(entity, relationships, true),
                getRelationshipTables(entity, relationships, false)}));

        return ret;
    }

    private String getColumns(ApricotTable entity) {
        StringBuilder sb = new StringBuilder();
        List<ApricotColumn> columns =
                entity.getColumns().stream().sorted(Comparator.comparingInt(ApricotColumn::getOrdinalPosition)).collect(Collectors.toList());
        for (ApricotColumn column : columns) {
            String type = column.getDataType() + (column.getValueLength() != null ?
                    " (" + column.getValueLength() + ")" : "");
            String nullable = column.isNullable() ? "nullable" : "";
            sb.append(getHtmlRow(new String[]{column.getName(), type, nullable}));
        }

        return sb.toString();
    }

    private String getConstraints(ApricotTable entity) {
        StringBuilder sb = new StringBuilder();

        List<ApricotConstraint> constraints = entity.getConstraints().stream().sorted(Comparator.comparingInt(c -> c.getType().getOrder())).collect(Collectors.toList());

        for (ApricotConstraint constraint : constraints) {
            sb.append(getHtmlRow(new String[]{constraint.getName(), constraint.getType().name(),
                    getColumnsInConstraint(constraint)}));
        }

        return sb.toString();
    }

    private String getHtmlRow(String[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>\n");
        for (String s : values) {
            sb.append("<td>").append(s).append("</td>").append("\n");
        }
        sb.append("</tr>\n");

        return sb.toString();
    }

    private String getColumnsInConstraint(ApricotConstraint constraint) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ApricotColumnConstraint acc : constraint.getColumns()) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(acc.getColumn().getName());
        }

        return sb.toString();
    }

    private String getRelationshipTables(ApricotTable table, List<ApricotRelationship> relationships, boolean master) {
        StringBuilder sb = new StringBuilder();

        List<String> tables = new ArrayList<>();
        for (ApricotRelationship r : relationships) {
            if (master) {
                if (r.getChild().getTable().equals(table)) {
                    tables.add(r.getParent().getTable().getName());
                }
            } else {
                if (r.getParent().getTable().equals(table)) {
                    tables.add(r.getChild().getTable().getName());
                }
            }
        }

        tables.sort(String.CASE_INSENSITIVE_ORDER);

        boolean first = true;
        for (String tbl : tables) {
            if (!first) {
                sb.append("<br/>");
            } else {
                first = false;
            }
            sb.append(tbl);
        }

        return sb.toString();
    }
}
