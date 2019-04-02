package za.co.apricotdb.support.script;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

public interface ScriptGenerator {

    String createTable(ApricotTable table);

    String createConstraint(ApricotConstraint constraint);

    String createIndex(ApricotConstraint constraint);

    String createUniqueIndex(ApricotConstraint constraint);

    String createUniqueConstraint(ApricotConstraint constraint);

    String createForeignKeyConstraint(ApricotTable table, List<ApricotRelationship> relationships);
}
