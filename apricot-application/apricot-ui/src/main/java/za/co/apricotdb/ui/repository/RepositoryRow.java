package za.co.apricotdb.ui.repository;

import java.io.Serializable;

/**
 * This bean represents a row of the left-buttons-right panel of Apricot
 * Repository comparison form.
 *
 * @author Anton Nazarov
 * @since 16/04/2020
 */
public class RepositoryRow implements Serializable {

    private static final long serialVersionUID = -5780639025015648766L;

    private RepositoryCell localObject;
    private RepositoryControl control;
    private RepositoryCell remoteObject;
    private boolean equal;
    private RowType rowType;
    private boolean includesSnapshots;
    private ModelRow modelRow;

    public RepositoryRow(RowType rowType, boolean equal, boolean includesSnapshots, ModelRow modelRow) {
        this.rowType = rowType;
        this.equal = equal;
        this.includesSnapshots = includesSnapshots;
        this.modelRow = modelRow;
    }

    public void setLocalObject(RepositoryCell localObject) {
        this.localObject = localObject;
    }

    public RepositoryControl getControl() {
        return control;
    }

    public void setControl(RepositoryControl control) {
        this.control = control;
    }

    public void setRemoteObject(RepositoryCell remoteObject) {
        this.remoteObject = remoteObject;
    }

    public boolean isEqual() {
        return equal;
    }

    public RowType getRowType() {
        return rowType;
    }

    public String getObjectName() {
        if (localObject != null && localObject.getText() != null && localObject.getText().getText() != null) {
            return localObject.getText().getText();
        }

        if (remoteObject != null) {
            return remoteObject.getText().getText();
        }

        return null;
    }

    public boolean hasBothSides() {
        if (localObject != null && remoteObject != null) {
            return !localObject.isEmpty() && !remoteObject.isEmpty();
        }
        return false;
    }

    public boolean includesSnapshots() {
        return includesSnapshots;
    }

    public ModelRow getModelRow() {
        return modelRow;
    }

    public RepositoryCell getLocalObject() {
        return localObject;
    }

    public RepositoryCell getRemoteObject() {
        return remoteObject;
    }
}
