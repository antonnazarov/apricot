package za.co.apricotdb.ui.repository;

import org.springframework.stereotype.Component;

/**
 * This is the convenient factory to build the RepositoryRow holder.
 * 
 * @author Anton Nazarov
 * @since 19/04/2020
 */
@Component
public class RepositoryRowFactory {

    /**
     * Construct a new row, using the given minimal parameters.
     */
    public RepositoryRow buildRow(RowType type, boolean equal, String localName, String remoteName) {

        if (localName == null && remoteName == null) {
            throw new IllegalArgumentException("the localName and remoteName can't be both null");
        }

        RepositoryRow row = new RepositoryRow(type, equal);
        RepositoryCell oLocal = new RepositoryCell(localName, false, row);
        RepositoryCell oRemote = new RepositoryCell(remoteName, true, row);
        RepositoryControl cntrl = new RepositoryControl();

        row.setLocalObject(oLocal);
        row.setRemoteObject(oRemote);
        row.setControl(cntrl);

        return row;
    }
}
