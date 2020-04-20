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
}
