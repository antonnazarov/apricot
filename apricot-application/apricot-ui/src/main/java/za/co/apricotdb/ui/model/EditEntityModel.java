package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The data model for the form apricot-entity-editor.fxml.
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
public class EditEntityModel implements Serializable {

    private static final long serialVersionUID = -5278550898908852806L;

    private String entityName;
    private boolean newEntity;
    private ApricotTable table;
    private ObservableList<ApricotColumnData> columns = FXCollections.observableArrayList();
    private ObservableList<ApricotConstraintData> constraints = FXCollections.observableArrayList();
    private String entityOriginalName;
    private boolean edited;
    private Stage dialog;
    
    private List<ApricotColumnData> deletedColumns = new ArrayList<>();
    private List<ApricotConstraintData> deletedConstraints = new ArrayList<>();

    public EditEntityModel(boolean newEntity, Stage dialog) {
        this.newEntity = newEntity;
        this.dialog = dialog;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public boolean isNewEntity() {
        return newEntity;
    }

    public void setNewEntity(boolean newEntity) {
        this.newEntity = newEntity;
    }

    public ObservableList<ApricotColumnData> getColumns() {
        return columns;
    }

    public void setColumns(ObservableList<ApricotColumnData> columns) {
        this.columns = columns;
    }

    public ObservableList<ApricotConstraintData> getConstraints() {
        return constraints;
    }

    public void setConstraints(ObservableList<ApricotConstraintData> constraints) {
        this.constraints = constraints;
    }

    public List<ApricotColumnData> getDeletedColumns() {
        return deletedColumns;
    }

    public List<ApricotConstraintData> getDeletedConstraints() {
        return deletedConstraints;
    }

    public ApricotTable getTable() {
        return table;
    }

    public void setTable(ApricotTable table) {
        this.table = table;
    }
    
    public ApricotColumnData getColumnByName(String name) {
        for (ApricotColumnData cd : columns) {
            if (cd.getName().getValue().equals(name)) {
                return cd;
            }
        }
        
        return null;
    }

    public String getEntityOriginalName() {
        return entityOriginalName;
    }

    public void setEntityOriginalName(String entityOriginalName) {
        this.entityOriginalName = entityOriginalName;
    }
    
    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        if (!newEntity) {
            if (edited) {
                dialog.setTitle("Edit Entity *");
            } else {
                dialog.setTitle("Edit Entity");
            }
        }
        this.edited = edited;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("entityName=[").append(entityName)
            .append("], newEntity=[").append(newEntity)
            .append("], entityOriginalName=[").append(entityOriginalName)
            .append("]");
        
        return sb.toString();
    }
}
