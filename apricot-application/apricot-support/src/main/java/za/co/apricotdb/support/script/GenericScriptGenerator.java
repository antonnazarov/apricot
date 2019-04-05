package za.co.apricotdb.support.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

@Component
public class GenericScriptGenerator implements ScriptGenerator {

    public static final String INDENT = "   ";
    public static final int COMMENT_LENGTH = 45;

    @Override
    public String createTableAll(ApricotTable table, List<ApricotRelationship> relationships) {
        StringBuilder sb = new StringBuilder();

        sb.append(StringUtils.rightPad("-- ", COMMENT_LENGTH, "*")).append("\n");
        sb.append("-- * ").append(StringUtils.center(table.getName(), COMMENT_LENGTH - 6)).append("*\n");
        sb.append(StringUtils.rightPad("-- ", COMMENT_LENGTH, "*")).append("\n");

        sb.append(createTable(table));
        sb.append(createConstraints(table));
        for (ApricotRelationship r : relationships) {
            if (table.equals(r.getChild().getTable())) {
                sb.append(createForeignKeyConstraint(r));
            }
        }

        return sb.toString();
    }

    @Override
    public String createTable(ApricotTable table) {
        StringBuilder sb = new StringBuilder();

        int maxLength = getMaxFieldName(table);
        boolean first = true;
        sb.append("create table ").append(table.getName()).append(" (\n");
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
    public String createConstraints(ApricotTable table) {
        StringBuilder sb = new StringBuilder();

        for (ApricotConstraint constr : table.getConstraints()) {
            if (constr.getType() != ConstraintType.PRIMARY_KEY && constr.getType() != ConstraintType.FOREIGN_KEY) {
                switch (constr.getType()) {
                case UNIQUE_INDEX:
                    sb.append(createIndex(constr, true)).append("\n\n");
                    break;
                case NON_UNIQUE_INDEX:
                    sb.append(createIndex(constr, false)).append("\n\n");
                    break;
                case UNIQUE:
                    sb.append(createUniqueConstraint(constr)).append("\n\n");
                    break;
                default:
                    break;
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String createForeignKeyConstraint(ApricotRelationship relationship) {
        StringBuilder sb = new StringBuilder();
        ApricotTable parent = relationship.getParent().getTable();
        ApricotTable child = relationship.getChild().getTable();

        sb.append("alter table ").append(child.getName()).append("\n").append("add constraint ")
                .append(relationship.getChild().getName()).append("\n").append("foreign key (")
                .append(getConstraintColumnsAsString(relationship.getChild())).append(") ").append("references ")
                .append(parent.getName()).append(" (").append(getConstraintColumnsAsString(relationship.getParent()))
                .append(");\n\n");

        return sb.toString();
    }

    @Override
    public String dropAllTables(List<ApricotTable> tables) {
        StringBuilder sb = new StringBuilder();

        for (ApricotTable table : tables) {
            sb.append("drop table ").append(table.getName()).append(";\n");
        }

        return sb.toString();
    }

    @Override
    public String dropSelectedTables(List<ApricotTable> tables, List<ApricotRelationship> relationships) {

        return null;
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

    private String createUniqueConstraint(ApricotConstraint constraint) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(constraint.getTable().getName()).append("\n").append("add constraint ")
                .append(constraint.getName()).append(" unique (").append(getConstraintColumnsAsString(constraint))
                .append(");");

        return sb.toString();
    }

    private String createIndex(ApricotConstraint constraint, boolean unique) {
        StringBuilder sb = new StringBuilder();

        if (unique) {
            sb.append("create unique index ");
        } else {
            sb.append("create index ");
        }
        sb.append(constraint.getName()).append(" on ").append(constraint.getTable().getName()).append("(")
                .append(getConstraintColumnsAsString(constraint)).append(");");

        return sb.toString();
    }
}
