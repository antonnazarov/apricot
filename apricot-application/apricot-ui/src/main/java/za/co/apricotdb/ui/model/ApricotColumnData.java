package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * The wrapper and extender of the ApricotColumn entity object.
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
public class ApricotColumnData implements Serializable {

    private static final long serialVersionUID = 7477076271961219617L;

    private long id;
    private SimpleBooleanProperty primaryKey;
    private SimpleStringProperty name;
    private SimpleStringProperty dataType;
    private SimpleBooleanProperty nullable;
    private SimpleStringProperty valueLength;
    private String comment;
    private boolean added;
    private boolean deleted;
    private ApricotColumn column;

    public ApricotColumnData() {
        primaryKey = new SimpleBooleanProperty();
        name = new SimpleStringProperty();
        dataType = new SimpleStringProperty();
        nullable = new SimpleBooleanProperty();
        valueLength = new SimpleStringProperty();
    }

    public ApricotColumnData(ApricotColumn col) {
        this.column = col;
        this.id = col.getId();
        primaryKey = new SimpleBooleanProperty(isPrimaryKey(col));
        name = new SimpleStringProperty(col.getName());
        dataType = new SimpleStringProperty(col.getDataType());
        nullable = new SimpleBooleanProperty(col.isNullable());
        valueLength = new SimpleStringProperty(col.getValueLength());
    }

    private boolean isPrimaryKey(ApricotColumn col) {
        ApricotTable t = col.getTable();
        List<ApricotConstraint> cnsts = t.getConstraints();
        for (ApricotConstraint c : cnsts) {
            if (c.getType() == ConstraintType.PRIMARY_KEY) {
                List<ApricotColumnConstraint> cc = c.getColumns();
                for (ApricotColumnConstraint clm : cc) {
                    if (clm.getColumn().equals(col)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public long getId() {
        return id;
    }

    public SimpleBooleanProperty getPrimaryKey() {
        return primaryKey;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public SimpleStringProperty getDataType() {
        return dataType;
    }

    public SimpleBooleanProperty getNullable() {
        return nullable;
    }

    public SimpleStringProperty getValueLength() {
        return valueLength;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ApricotColumn getColumn() {
        return column;
    }

    public void setColumn(ApricotColumn column) {
        this.column = column;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApricotColumnData other = (ApricotColumnData) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ApricotColumnData: ").append("Id=[").append(id).append("], name=[").append(name.getValue())
                .append("], primaryKey=[").append(primaryKey.getValue()).append("], isNullable")
                .append(nullable.getValue()).append("]");

        return sb.toString();
    }
}
