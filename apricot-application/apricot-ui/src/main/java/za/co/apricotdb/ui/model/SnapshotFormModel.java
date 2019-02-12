package za.co.apricotdb.ui.model;

import java.io.Serializable;

/**
 * The model of teh snapshot form.
 * 
 * @author Anton Nazarov
 * @since 11/02/2019
 */
public class SnapshotFormModel implements Serializable {

    private static final long serialVersionUID = 1475341422293997020L;
    
    private String snapshotName;
    private String snapshotDescription;
    private boolean initializedFromSnapshot;
    private boolean newSnapshot;
    
    public String getSnapshotName() {
        return snapshotName;
    }
    
    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }
    
    public String getSnapshotDescription() {
        return snapshotDescription;
    }
    
    public void setSnapshotDescription(String snapshotDescription) {
        this.snapshotDescription = snapshotDescription;
    }

    public boolean isInitializedFromSnapshot() {
        return initializedFromSnapshot;
    }

    public void setInitializedFromSnapshot(boolean initializedFromSnapshot) {
        this.initializedFromSnapshot = initializedFromSnapshot;
    }

    public boolean isNewSnapshot() {
        return newSnapshot;
    }

    public void setNewSnapshot(boolean newSnapshot) {
        this.newSnapshot = newSnapshot;
    }
}
