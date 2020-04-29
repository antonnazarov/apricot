package za.co.apricotdb.ui.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The holder of the repository data comparison.
 * 
 * @author Anton Nazarov
 * @since 19/04/2020
 */
public class RepositoryModel implements Serializable {

    private static final long serialVersionUID = -9214962765948616446L;

    private List<ModelRow> rows = new ArrayList<>();

    public List<ModelRow> getRows() {
        return rows;
    }

    /**
     * Sort the model content properly:
     * 1) all import projects
     * 2) all export projects
     * 3) all unequal projects
     * 4) all equal projects
     * Inside the group the sorting is alphabetical
     * For the unequal projects the snapshots inside
     * have the same sequence as projects one level up
     * (import, export, unequal, equal)
     */
    public void sort() {
        sort(rows);
        for (ModelRow r : rows) {
            sort(r.getIncludedItems());
        }
    }

    private void sort(List<ModelRow> rows) {
        rows.sort((ModelRow r1, ModelRow r2) -> {
            if (r1.getLocalName() == null && r2.getLocalName() != null) {
                return -1;
            } else  if (r1.getLocalName() != null && r2.getLocalName() == null) {
                return 1;
            }
            if (r1.getRemoteName() == null && r2.getRemoteName() != null) {
                return -1;
            } else if (r1.getRemoteName() != null && r2.getRemoteName() == null) {
                return 1;
            }
            if (r1.getLocalName() == null && r2.getLocalName() == null) {
                return r1.getRemoteName().compareTo(r2.getRemoteName());
            }
            if (r1.getRemoteName() == null && r2.getRemoteName() == null) {
                return r1.getLocalName().compareTo(r2.getLocalName());
            }
            if (!r1.isEqual() && r2.isEqual()) {
                return -1;
            }
            if (!r1.isEqual() && !r2.isEqual() || r1.isEqual() && r2.isEqual()) {
                return r1.getLocalName().compareTo(r2.getLocalName());
            }

            return 0;
        });
    }
}
