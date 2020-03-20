package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity for apricot_constraint table.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_constraint")
@NamedQuery(name = "ApricotConstraint.getConstraintsByTable", query = "SELECT cnst FROM ApricotConstraint cnst WHERE cnst.table = :table")
@NamedQuery(name = "ApricotConstraint.getConstraintsByName", query = "SELECT DISTINCT cnst FROM ApricotSnapshot snap JOIN snap.tables tbl JOIN tbl.constraints cnst WHERE snap = :snapshot AND cnst.name = :name")
@NamedQuery(name = "ApricotConstraint.getConstraintsByColumn", query = "SELECT DISTINCT cnst FROM ApricotConstraint cnst JOIN cnst.columns acc WHERE acc.column = :column")
public class ApricotConstraint implements Serializable {

    private static final long serialVersionUID = 3862015345366091286L;

    @NoExport
    @Transient
    Logger logger = LoggerFactory.getLogger(ApricotConstraint.class);

    public ApricotConstraint() {
    }

    public ApricotConstraint(String name, ConstraintType type, ApricotTable table, String columns) {
        this.name = name;
        this.type = type;
        this.table = table;

        addColumns(columns);
    }

    public ApricotConstraint(String name, ConstraintType type, ApricotTable table) {
        this.name = name;
        this.type = type;
        this.table = table;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constraint_id")
    private long id;

    @Column(name = "constraint_name")
    private String name;

    @Column(name = "constraint_type")
    @Enumerated(EnumType.STRING)
    private ConstraintType type;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "table_id")
    private ApricotTable table;

    @NoExport
    @OneToMany(mappedBy = "constraint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotColumnConstraint> columns = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstraintType getType() {
        return type;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    public ApricotTable getTable() {
        return table;
    }

    public void setTable(ApricotTable table) {
        this.table = table;
    }

    public List<ApricotColumnConstraint> getColumns() {
        return columns;
    }

    public void setColumns(List<ApricotColumnConstraint> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApricotConstraint constraint = (ApricotConstraint) o;

        return name.equalsIgnoreCase(constraint.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nApricotConstraint: ");
        sb.append("id=[").append(id).append("], ");
        sb.append("name=[").append(name).append("], ");
        sb.append("type=[").append(type).append("], ");
        sb.append("table=[").append(table.getName()).append("], ");
        sb.append("columns=[").append(columns).append("]");

        return sb.toString();
    }

    /**
     * Add fields into the constraint (fields to be provided as a semicolon-
     * separated list).
     */
    public void addColumns(String sColumns) {
        if (sColumns == null) {
            return;
        }

        List<ApricotColumn> cls = table.getColumnListByNames(sColumns);
        for (int i = 0; i < cls.size(); i++) {
            ApricotColumn c = cls.get(i);
            ApricotColumnConstraint acc = new ApricotColumnConstraint(this, c);
            acc.setOrdinalPosition(i + 1);
            columns.add(acc);
        }
    }

    /**
     * Add an individual column.
     */
    public void addColumn(String column) {
        // check if the column is already included into the constraint
        if (findConstraintColumn(column) != null) {
            return;
        }

        ApricotColumn c = table.getColumnByName(column);
        if (c != null) {
            ApricotColumnConstraint acc = new ApricotColumnConstraint(this, c);
            acc.setOrdinalPosition(columns.size() + 1);
            columns.add(acc);
        } else {
            logger.info("The column [" + column + "] was not found in the table [" + table.getName()
                    + "]. This table contains the following columns: " + table.getColumns());
        }
    }

    private ApricotColumnConstraint findConstraintColumn(String column) {
        for (ApricotColumnConstraint cc : columns) {
            if (cc.getColumn().getName().equals(column)) {
                return cc;
            }
        }

        return null;
    }
}
