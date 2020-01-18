package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.text.WordUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This is a "model"- representation of the Constraint to be reflected in
 * apricpt-entity-editor.fxml form.
 * 
 * @author Anton Nazarov
 * @since 28/02/2019
 */
public class ApricotConstraintData implements Serializable {

    private static final long serialVersionUID = 5624231358883188078L;
    private static final int COLUMNS_LIST_LENGTH = 45;

    private SimpleStringProperty constraintType = new SimpleStringProperty();
    private SimpleStringProperty constraintName = new SimpleStringProperty();
    private ObservableList<ApricotColumnData> columns = FXCollections.observableArrayList();
    private long id;
    private boolean added;
    private ApricotTable table;
    private ApricotConstraint constraint;
    private boolean edited;
    private String parentName; // use this field to store the parent entity name of the FK constraint

    public SimpleStringProperty getConstraintType() {
        return constraintType;
    }

    public String getConstraintTypeAsString() {
        return constraintType.getValue();
    }

    public void setConstraintType(SimpleStringProperty constraintType) {
        this.constraintType = constraintType;
    }

    public SimpleStringProperty getConstraintName() {
        return constraintName;
    }

    public String getConstraintNameAsString() {
        return constraintName.getValue();
    }

    public void setConstraintName(SimpleStringProperty constraintName) {
        this.constraintName = constraintName;
    }

    public String getConstraintColumns() {
        StringBuilder sb = new StringBuilder();
        for (ApricotColumnData cd : columns) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(cd.getName().getValue());
        }

        if (parentName != null) {
            sb.append(" (parent: ").append(parentName).append(")");
        }

        String res = WordUtils.wrap(sb.toString(), COLUMNS_LIST_LENGTH);

        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public List<ApricotColumnData> getColumns() {
        return columns;
    }

    public void setColumns(ObservableList<ApricotColumnData> columns) {
        this.columns = columns;
    }

    public ApricotTable getTable() {
        return table;
    }

    public void setTable(ApricotTable table) {
        this.table = table;
    }

    public ApricotConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(ApricotConstraint constraint) {
        this.constraint = constraint;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=[").append(constraintName.getValue()).append("], type=[").append(constraintType.getValue())
                .append("], added=[").append(added).append("], edited=[").append(edited).append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return constraintName.getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApricotConstraintData other = (ApricotConstraintData) obj;
        if (constraintName == null) {
            if (other.constraintName != null) {
                return false;
            }
        } else if (!constraintName.getValue().equals(other.constraintName.getValue())) {
            return false;
        }
        return true;
    }
}
