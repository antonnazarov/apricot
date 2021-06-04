package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This handler implements some functionality related to the MetaData type.
 *
 * @author Anton Nazarov
 * @since 20/03/2021
 */
@Component
public class MetaDataHandler {

    @Autowired
    ConsistencyHandler consistencyHandler;

    public ApricotSnapshot createSnapshot(MetaData metaData, String[] blackList) {
        List<ApricotTable> included = new ArrayList<>();
        ApricotSnapshot ret = new ApricotSnapshot();
        ret.setName("Reversed Snapshot");

        if (blackList.length > 0) {
            //  there is some tables in the Black List.
            //  Implement the consistent exclude from the reversed list of tables
            List<String> bList = Arrays.asList(blackList);
            List<ApricotTable> excluded = new ArrayList<>();
            for (ApricotTable tbl : metaData.getTables()) {
                if (!bList.contains(tbl.getName())) {
                    included.add(tbl);
                } else {
                    excluded.add(tbl);
                }
            }

            Map<ApricotTable, ApricotTable> extraExclude = consistencyHandler.getFullConsistentExclude(excluded, metaData.getRelationships());
            if (!extraExclude.isEmpty()) {
                for (ApricotTable t : extraExclude.keySet()) {
                    included.remove(t);
                    excluded.add(t);
                }
            }
        } else {
            included = metaData.getTables();
        }

        ret.setTables(included);

        //  mark Relationships for backward search from the Constraint- side
        for (ApricotRelationship relationship : metaData.getRelationships()) {
            relationship.getChild().setRelationship(relationship);
        }

        return ret;
    }
}
