package za.co.apricotdb.persistence.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The utility- class that reads the current Meta Data in the Apricot-
 * repository.
 *
 * @author Anton Nazarov
 * @since 05/10/2018
 */
@Component
public class DataReader {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;
    
    @Autowired
    RelationshipManager relationshipManager;

    public MetaData readTablesByList(List<String> tableNames, long snapshotId) {
        ApricotSnapshot snapshot = snapshotManager.getSnapshotById(snapshotId);
        MetaData ret = new MetaData();
        List<ApricotTable> tables = null;
        if (tableNames.contains("*")) {
            tables = tableManager.getTablesForSnapshot(snapshot);
        } else {
            tables = tableManager.getTablesByNames(tableNames, snapshot);
        }
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
        ret.setTables(tables);
        ret.setRelationships(relationships);

        return ret;
    }
}
