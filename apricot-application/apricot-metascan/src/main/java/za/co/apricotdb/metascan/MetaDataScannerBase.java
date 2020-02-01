package za.co.apricotdb.metascan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import com.microsoft.sqlserver.jdbc.StringUtils;

import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

public abstract class MetaDataScannerBase implements MetaDataScanner {

    public static final int DATA_TYPE_LENGTH = 25;

    @Autowired
    MetaDataScannerFactory scannerFactory;

    @Override
    public MetaData scan(ApricotTargetDatabase targetDb, String driverClassName, String url, String schema,
            String userName, String password, ApricotSnapshot snapshot) {
        if (StringUtils.isEmpty(schema)) {
            String defSchema = scannerFactory.getDefaultSchema(url, userName, targetDb);
            if (StringUtils.isEmpty(defSchema)) {
                schema = null;
            } else {
                schema = defSchema;
            }
        }

        JdbcOperations jdbc = MetaDataScanner.getTargetJdbcOperations(driverClassName, url, userName, password);

        Map<String, ApricotTable> tables = getTables(jdbc, snapshot, schema);
        getColumns(jdbc, tables, schema);
        Map<String, ApricotConstraint> constraints = getConstraints(jdbc, tables, schema);
        List<ApricotRelationship> relationships = getRelationships(jdbc, constraints, schema);

        MetaData ret = new MetaData();
        ret.setTables(new ArrayList<ApricotTable>(tables.values()));
        ret.setRelationships(relationships);

        return ret;
    }

    protected String getDataType(String dataType) {
        String ret = dataType;
        if (dataType.length() > DATA_TYPE_LENGTH) {
            String[] split = dataType.split(" ");
            if (split != null && split.length > 0) {
                if (split[0].length() < DATA_TYPE_LENGTH) {
                    ret = split[0];
                } else {
                    ret = dataType.substring(0, DATA_TYPE_LENGTH);
                }
            }
        }

        return ret;
    }
}
