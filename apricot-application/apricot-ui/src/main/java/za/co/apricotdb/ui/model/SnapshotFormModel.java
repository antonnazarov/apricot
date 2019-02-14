package za.co.apricotdb.ui.model;

import java.io.Serializable;

/**
 * The model of the snapshot form.
 * 
 * @author Anton Nazarov
 * @since 11/02/2019
 */
public class SnapshotFormModel implements Serializable {

    private static final long serialVersionUID = 1475341422293997020L;
    
    private String snapshotName;
    private String snapshotDescription;
    private String initSourceSnapshot;
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

    public boolean isNewSnapshot() {
        return newSnapshot;
    }

    public void setNewSnapshot(boolean newSnapshot) {
        this.newSnapshot = newSnapshot;
    }

    public String getInitSourceSnapshot() {
        return initSourceSnapshot;
    }

    public void setInitSourceSnapshot(String initSourceSnapshot) {
        this.initSourceSnapshot = initSourceSnapshot;
    }
}
