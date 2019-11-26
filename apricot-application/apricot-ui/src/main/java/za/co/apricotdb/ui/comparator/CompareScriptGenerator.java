package za.co.apricotdb.ui.comparator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * The main (top level) component, which generates the differences alignment
 * script.
 * 
 * @author Anton Nazarov
 * @since 26/11/2019
 */
@Component
public class CompareScriptGenerator {

    @Autowired
    AddTableScript addTableScript;

    @Autowired
    RemoveTableScript removeTableScript;

    @Autowired
    AddColumnScript addColumnScript;

    @Autowired
    RemoveColumnScript removeColumnScript;

    @Autowired
    AlterColumnScript alterColumnScript;

    @Autowired
    RemoveConstraintScript removeConstraintScript;

    @Autowired
    AlterConstraintScript alterConstraintScript;

    @Autowired
    AddConstraintScript addConstraintScript;

    @Autowired
    RelatedConstraintsHandler relConstrHandler;

    /**
     * Generate the differences alignment script using the collection of the
     * differences and the schema name (if any).
     */
    @Transactional
    public String generate(List<CompareSnapshotRow> differences, String schema) {
        StringBuilder sb = new StringBuilder();

        // add tables, remove tables, add columns
        sb.append(addTableScript.generate(differences, schema));
        sb.append(removeTableScript.generate(differences, schema));
        sb.append(addColumnScript.generate(differences, schema));

        // collect the constraints eligible for removal and for removal/recover with the
        // updated columns
        List<ApricotConstraint> removeCnstrRel = removeColumnScript.getRelatedConstraints(differences);
        List<ApricotConstraint> removeRestoreCnstrRel = alterColumnScript.getRelatedConstraints(differences);
        List<ApricotConstraint> removeCnstr = removeConstraintScript.getRelatedConstraints(differences);
        List<ApricotConstraint> alterCnstr = alterConstraintScript.getRelatedConstraints(differences);
        Set<ApricotConstraint> removalCnstr = new HashSet<>(removeCnstrRel);
        removalCnstr.addAll(removeRestoreCnstrRel);
        removalCnstr.addAll(removeCnstr);
        removalCnstr.addAll(alterCnstr);
        sb.append(relConstrHandler.removeRelatedConstraints(removalCnstr, schema));

        // the columns removals, alterations
        sb.append(removeColumnScript.generate(differences, schema));
        sb.append(alterColumnScript.generate(differences, schema));

        // prepare the constraints for the creation/redo
        List<ApricotConstraint> addCnstr = addConstraintScript.getRelatedConstraints(differences);
        Set<ApricotConstraint> redoCnstr = new HashSet<>(removeRestoreCnstrRel);
        redoCnstr.addAll(alterCnstr);
        redoCnstr.addAll(addCnstr);
        sb.append(relConstrHandler.addRelatedConstraints(redoCnstr, schema));

        return sb.toString();
    }
}
