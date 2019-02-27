package za.co.apricotdb.ui.model;

import java.io.Serializable;

import javafx.collections.ObservableList;

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
    private ObservableList<ApricotColumnData> columns;

    public EditEntityModel(boolean newEntity) {
        this.newEntity = newEntity;
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
}