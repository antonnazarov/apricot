package za.co.apricotdb.support.script;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

public interface ScriptGenerator {

    String createTable(ApricotTable table, String schema);

    String createConstraints(ApricotTable table, String schema);

    String createForeignKeyConstraint(ApricotRelationship relationship, String schema);
    
    String createTableAll(ApricotTable table, List<ApricotRelationship> relationships, String schema);
    
    String dropAllTables(List<ApricotTable> tables, String schema);
    
    String dropSelectedTables(List<ApricotTable> tables, String schema);
    
    String dropConstraint(ApricotConstraint constraint, String schema);
    
    String deleteInAllTables(List<ApricotTable> tables, String schema);
    
    String deleteInSelectedTables(List<ApricotTable> tables, String schema);
}
