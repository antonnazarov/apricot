package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This component checks if the content of the current snapshot and the reversed schema content have been match enough.
 *
 * @author Anton Nazarov
 * @since 22/03/2021
 */
@Component
public class SnapshotMatchChecker {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    AlertMessageDecorator alert;

    public boolean checkMatch(MetaData metaData, String[] blackList, int requestedMatch) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        if (!isMatch(metaData, blackList, requestedMatch)) {
            return alert.requestYesNoOption("Entities Match Test", "The Entities in the current Snapshot '" + snapshot.getName()
                            + "' have been very different from just scanned database schema. You might need to switch to another Project. Do you want to continue?",
                    "Continue", Alert.AlertType.WARNING);
        } else {
            return true;
        }
    }

    /**
     * Check if the requested match has been achieved.
     */
    @ApricotErrorLogger(title = "Unable to check the match status of the current snapshot and the target database schema")
    public boolean isMatch(MetaData metaData, String[] blackList, int requestedMatch) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        List<ApricotTable> snapshotTables = tableManager.getTablesForSnapshot(snapshot);
        Set<String> reversedTables = getTableNames(metaData.getTables());
        List<String> blList = Arrays.asList(blackList);

        int matchCnt = 0;
        for (ApricotTable table : snapshotTables) {
            //  count the matches excluding the black list content
            if (reversedTables.contains(table.getName()) && !blList.contains(table.getName())) {
                matchCnt++;
            }
        }

        long currentRatio = Math.round((double)matchCnt / (double)snapshotTables.size() * 100);
        if (currentRatio >= requestedMatch) {
            return true;
        }

        return false;
    }

    /**
     * Recalculate the given table names into the proper names of entities in the system.
     */
    private Set<String> getTableNames(List<ApricotTable> tables) {
        Set<String> ret = new HashSet<>();

        for (ApricotTable table : tables) {
            ret.add(table.getName());
        }

        return ret;
    }
}
