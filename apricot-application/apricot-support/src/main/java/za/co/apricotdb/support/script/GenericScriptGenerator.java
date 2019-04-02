package za.co.apricotdb.support.script;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

public abstract class GenericScriptGenerator implements ScriptGenerator {

    
    public String createTable(ApricotTable table) {
        
        return null;
    }

    public String createConstraint(ApricotConstraint constraint) {
        
        return null;
    }

    public String createIndex(ApricotConstraint constraint) {
        
        return null;
    }

    public String createUniqueIndex(ApricotConstraint constraint) {
        
        return null;
    }

    public String createUniqueConstraint(ApricotConstraint constraint) {
        
        return null;
    }

    public String createForeignKeyConstraint(ApricotTable table, List<ApricotRelationship> relationships) {
        
        return null;
    }
}
