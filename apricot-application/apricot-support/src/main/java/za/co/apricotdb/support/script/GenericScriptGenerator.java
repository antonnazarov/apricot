package za.co.apricotdb.support.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

@Component
public class GenericScriptGenerator implements ScriptGenerator {

    public static final String INDENT = "   ";
    public static final int COMMENT_LENGTH = 55;

    @Autowired
    RelationshipManager relationshipManager;

    @Override
    public String createTableAll(ApricotTable table, List<ApricotRelationship> relationships, String schema) {
        StringBuilder sb = new StringBuilder();

        sb.append(StringUtils.rightPad("-- ", COMMENT_LENGTH, "*")).append("\n");
        sb.append("-- * ").append(StringUtils.center(table.getName(), COMMENT_LENGTH - 6)).append("*\n");
        sb.append(StringUtils.rightPad("-- ", COMMENT_LENGTH, "*")).append("\n");

        sb.append(createTable(table, schema));
        sb.append(createConstraints(table, schema));
        for (ApricotRelationship r : relationships) {
            if (table.equals(r.getChild().getTable())) {
                sb.append(createForeignKeyConstraint(r, schema));
            }
        }

        return sb.toString();
    }

    @Override
    public String createTable(ApricotTable table, String schema) {
        StringBuilder sb = new StringBuilder();

        String tableName = table.getName();
        if (schema != null) {
            tableName = schema + "." + tableName;
        }

        int maxLength = getMaxFieldName(table);
        boolean first = true;
        sb.append("create table ").append(tableName).append(" (\n");
        for (ApricotColumn col : table.getColumns()) {
            if (!first) {
                sb.append(",\n");
            } else {
                first = false;
            }
            sb.append(INDENT).append(StringUtils.rightPad(col.getName(), maxLength)).append(" ")
                    .append(col.getDataType());
            if (col.getValueLength() != null) {
                sb.append("(").append(col.getValueLength()).append(")");
            }
            if (!col.isNullable()) {
                sb.append(" not null");
            }
        }

        ApricotConstraint pk = getPrimaryKey(table);
        if (pk != null) {
            String pkCols = getConstraintColumnsAsString(pk);
            sb.append(",\n");
            sb.append(INDENT).append("constraint ").append(pk.getName()).append(" primary key (").append(pkCols)
                    .append(")\n");
        }

        sb.append(");\n\n");

        return sb.toString();
    }

    private int getMaxFieldName(ApricotTable table) {
        int ret = 0;

        for (ApricotColumn c : table.getColumns()) {
            if (ret < c.getName().length()) {
                ret = c.getName().length();
            }
        }

        return ret;
    }

    @Override
    public String createConstraints(ApricotTable table, String schema) {
        StringBuilder sb = new StringBuilder();

        for (ApricotConstraint constr : table.getConstraints()) {
            if (constr.getType() != ConstraintType.PRIMARY_KEY && constr.getType() != ConstraintType.FOREIGN_KEY) {
                switch (constr.getType()) {
                case UNIQUE_INDEX:
                    sb.append(createIndex(constr, true, schema)).append("\n\n");
                    break;
                case NON_UNIQUE_INDEX:
                    sb.append(createIndex(constr, false, schema)).append("\n\n");
                    break;
                case UNIQUE:
                    sb.append(createUniqueConstraint(constr, schema)).append("\n\n");
                    break;
                default:
                    break;
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String createForeignKeyConstraint(ApricotRelationship relationship, String schema) {
        StringBuilder sb = new StringBuilder();
        ApricotTable parent = relationship.getParent().getTable();
        ApricotTable child = relationship.getChild().getTable();

        String parentTableName = parent.getName();
        String childTableName = child.getName();
        if (schema != null) {
            parentTableName = schema + "." + parentTableName;
            childTableName = schema + "." + childTableName;
        }

        sb.append("alter table ").append(childTableName).append("\n").append("add constraint ")
                .append(relationship.getChild().getName()).append("\n").append("foreign key (")
                .append(getConstraintColumnsAsString(relationship.getChild())).append(") ").append("references ")
                .append(parentTableName).append(" (").append(getConstraintColumnsAsString(relationship.getParent()))
                .append(");\n\n");

        return sb.toString();
    }

    @Override
    public String dropAllTables(List<ApricotTable> tables, String schema) {
        StringBuilder sb = new StringBuilder();

        for (ApricotTable table : tables) {
            String tableName = table.getName();
            if (schema != null) {
                tableName = schema + "." + tableName;
            }

            sb.append("drop table ").append(tableName).append(";\n");
        }

        return sb.toString();
    }

    @Override
    public String dropSelectedTables(List<ApricotTable> tables, String schema) {
        StringBuilder sb = new StringBuilder();

        // first drop the outgoing/external relationships
        List<ApricotRelationship> externalRelationships = relationshipManager.findExernalRelationships(tables, true);
        for (ApricotRelationship r : externalRelationships) {
            sb.append(dropConstraint(r.getChild(), schema));
        }
        if (sb.length() > 0) {
            sb.append("\n");
        }
        sb.append(dropAllTables(tables, schema));

        return sb.toString();
    }

    @Override
    public String dropConstraint(ApricotConstraint constraint, String schema) {
        StringBuilder sb = new StringBuilder();

        String tableName = constraint.getTable().getName();
        if (schema != null) {
            tableName = schema + "." + tableName;
        }

        sb.append("alter table ").append(tableName).append("\n").append("drop constraint ").append(constraint.getName())
                .append(";\n");

        return sb.toString();
    }

    private ApricotConstraint getPrimaryKey(ApricotTable table) {
        for (ApricotConstraint constr : table.getConstraints()) {
            if (constr.getType() == ConstraintType.PRIMARY_KEY) {
                return constr;
            }
        }

        return null;
    }

    private List<ApricotColumn> getConstraintColumns(ApricotConstraint constraint) {
        List<ApricotColumn> ret = new ArrayList<>();

        for (ApricotColumnConstraint cc : constraint.getColumns()) {
            ret.add(cc.getColumn());
        }

        return ret;
    }

    private String getConstraintColumnsAsString(ApricotConstraint constraint) {
        StringBuilder sb = new StringBuilder();

        List<ApricotColumn> columns = getConstraintColumns(constraint);
        boolean first = true;
        for (ApricotColumn col : columns) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }

            sb.append(col.getName());
        }

        return sb.toString();
    }

    private String createUniqueConstraint(ApricotConstraint constraint, String schema) {
        StringBuilder sb = new StringBuilder();
        String tableName = constraint.getTable().getName();
        if (schema != null) {
            tableName = schema + "." + tableName;
        }

        sb.append("alter table ").append(tableName).append("\n").append("add constraint ").append(constraint.getName())
                .append(" unique (").append(getConstraintColumnsAsString(constraint)).append(");");

        return sb.toString();
    }

    private String createIndex(ApricotConstraint constraint, boolean unique, String schema) {
        StringBuilder sb = new StringBuilder();
        String tableName = constraint.getTable().getName();
        if (schema != null) {
            tableName = schema + "." + tableName;
        }

        if (unique) {
            sb.append("create unique index ");
        } else {
            sb.append("create index ");
        }
        sb.append(constraint.getName()).append(" on ").append(tableName).append("(")
                .append(getConstraintColumnsAsString(constraint)).append(");");

        return sb.toString();
    }
}
