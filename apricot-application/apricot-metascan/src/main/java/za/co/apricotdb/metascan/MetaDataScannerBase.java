package za.co.apricotdb.metascan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;

import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

public abstract class MetaDataScannerBase implements MetaDataScanner {

    @Override
    public MetaData scan(String driverClassName, String url, String userName, String password,
            ApricotSnapshot snapshot) {
        JdbcOperations jdbc = MetaDataScanner.getTargetJdbcOperations(driverClassName, url, userName, password);

        Map<String, ApricotTable> tables = getTables(jdbc, snapshot);
        getColumns(jdbc, tables);
        Map<String, ApricotConstraint> constraints = getConstraints(jdbc, tables);
        List<ApricotRelationship> relationships = getRelationships(jdbc, constraints);

        MetaData ret = new MetaData();
        ret.setTables(new ArrayList<ApricotTable>(tables.values()));
        ret.setRelationships(relationships);

        return ret;
    }
}
