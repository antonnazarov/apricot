package za.co.apricotdb.ui.repository;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * This is the convenient factory to build the RepositoryRow holder.
 * 
 * @author Anton Nazarov
 * @since 19/04/2020
 */
@Component
public class RepositoryRowFactory {

    @Resource
    ApplicationContext applicationContext;

    /**
     * Construct a new row, using the given minimal parameters.
     */
    public RepositoryRow buildRow(RowType type, boolean equal, String localName, String remoteName, ModelRow modelRow) {

        if (localName == null && remoteName == null) {
            throw new IllegalArgumentException("the localName and remoteName can't be both null");
        }

        RepositoryRow row = new RepositoryRow(type, equal, includesSnapshots(type, equal, localName, remoteName),
                modelRow);
        RepositoryCell cell = new RepositoryCell(localName, false, row);
        cell.init(applicationContext);
        row.setLocalObject(cell);
        cell = new RepositoryCell(remoteName, true, row);
        cell.init(applicationContext);
        row.setRemoteObject(cell);
        row.setControl(new RepositoryControl(row));

        return row;
    }

    private boolean includesSnapshots(RowType type, boolean equal, String localName, String remoteName) {
        return (type == RowType.PROJECT && !equal && localName != null && remoteName != null);
    }
}
