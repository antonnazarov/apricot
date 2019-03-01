package za.co.apricotdb.ui.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This model serves the following editor form: apricot-constraint-editor.fxml.
 * 
 * @author Anton Nazarov
 * @since 01/03/2019
 */
public class EditConstraintModel implements Serializable {

    private static final long serialVersionUID = 6953493854835934099L;

    private boolean newConstraint;
    private SimpleStringProperty constraintName = new SimpleStringProperty();
    private SimpleStringProperty constraintType = new SimpleStringProperty();
    private ObservableList<ApricotColumnData> allColumns = FXCollections.observableArrayList();
    private ObservableList<ApricotColumnData> selectedColumns = FXCollections.observableArrayList();
    private ApricotConstraintData constraintData;

    public boolean isNewConstraint() {
        return newConstraint;
    }

    public void setNewConstraint(boolean newConstraint) {
        this.newConstraint = newConstraint;
    }

    public SimpleStringProperty getConstraintName() {
        return constraintName;
    }

    public SimpleStringProperty getConstraintType() {
        return constraintType;
    }

    public ObservableList<ApricotColumnData> getAllColumns() {
        return allColumns;
    }

    public ObservableList<ApricotColumnData> getSelectedColumns() {
        return selectedColumns;
    }

    public ApricotConstraintData getConstraintData() {
        return constraintData;
    }

    public void setConstraintData(ApricotConstraintData constraintData) {
        this.constraintData = constraintData;
    }
}
