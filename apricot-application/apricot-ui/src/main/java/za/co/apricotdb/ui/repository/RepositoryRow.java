package za.co.apricotdb.ui.repository;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This bean represents a row of the left-buttons-right panel of Apricot
 * Repository comparison form.
 * 
 * @author Anton Nazarov
 * @since 16/04/2020
 */
public class RepositoryRow {

    private SimpleObjectProperty<RepositoryCell> localObject;
    private SimpleObjectProperty<RepositoryControl> control;
    private SimpleObjectProperty<RepositoryCell> remoteObject;
    private boolean equal;
    private RowType rowType;

    public RepositoryRow(RowType rowType, boolean equal) {
        this.rowType = rowType;
        this.equal = equal;
    }

    public SimpleObjectProperty<RepositoryCell> getLocalObject() {
        return localObject;
    }

    public void setLocalObject(SimpleObjectProperty<RepositoryCell> localObject) {
        this.localObject = localObject;
    }

    public SimpleObjectProperty<RepositoryControl> getControl() {
        return control;
    }

    public void setControl(SimpleObjectProperty<RepositoryControl> control) {
        this.control = control;
    }

    public SimpleObjectProperty<RepositoryCell> getRemoteObject() {
        return remoteObject;
    }

    public void setRemoteObject(SimpleObjectProperty<RepositoryCell> remoteObject) {
        this.remoteObject = remoteObject;
    }

    public boolean isEqual() {
        return equal;
    }

    public void setEqual(boolean equal) {
        this.equal = equal;
    }

    public RowType getRowType() {
        return rowType;
    }

    public void setRowType(RowType rowType) {
        this.rowType = rowType;
    }
}
