package za.co.apricotdb.support.script;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

public interface ScriptGenerator {

    String createTable(ApricotTable table);

    String createConstraints(ApricotTable table);

    String createForeignKeyConstraint(ApricotRelationship relationship);
    
    String createTableAll(ApricotTable table, List<ApricotRelationship> relationships);
    
    String dropAllTables(List<ApricotTable> tables);
    
    String dropSelectedTables(List<ApricotTable> tables, List<ApricotRelationship> relationships);
}
