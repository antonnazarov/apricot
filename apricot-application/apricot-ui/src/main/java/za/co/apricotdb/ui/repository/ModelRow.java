package za.co.apricotdb.ui.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The holder of the row of the model.
 * 
 * @author Anton Nazarov
 * @since 20/04/2020
 */
public class ModelRow implements Serializable {
    private static final long serialVersionUID = 7662841946637151164L;

    private RowType type;
    private boolean equal;
    private String localName;
    private String remoteName;
    private List<ModelRow> includedItems = new ArrayList<>();

    public ModelRow(RowType type, boolean equal, String localName, String remoteName) {
        if (localName == null && remoteName == null) {
            throw new IllegalArgumentException("The local and remote names can't be both null");
        }
        this.type = type;
        this.equal = equal;
        this.localName = localName;
        this.remoteName = remoteName;
    }

    public RowType getType() {
        return type;
    }

    public boolean isEqual() {
        return equal;
    }

    public void setEqual(boolean equalFlag) {
        this.equal = equalFlag;
    }

    public String getLocalName() {
        return localName;
    }

    public String getRemoteName() {
        return remoteName;
    }
    
    public  List<ModelRow> getIncludedItems() {
        return includedItems;
    }

    public String getName() {
        if (localName != null) {
            return localName;
        }
        if (remoteName != null) {
            return remoteName;
        }

        return null;
    }
}
