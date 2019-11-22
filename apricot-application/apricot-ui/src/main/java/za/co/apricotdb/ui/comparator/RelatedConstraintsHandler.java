package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ColumnManager;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * This component handles the related constraints business logic.
 * 
 * @author Anton Nazarov
 * @since 22/11/2019
 */
@Component
public class RelatedConstraintsHandler {

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    ColumnManager columnManager;

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> differences, boolean useSource) {
        List<ApricotConstraint> ret = new ArrayList<>();

        for (CompareSnapshotRow r : differences) {
            if (r.getDifference().getSourceObject() instanceof ApricotColumn) {
                ApricotColumn column = null;
                if (useSource) {
                    column = (ApricotColumn) r.getDifference().getSourceObject();
                } else {
                    column = (ApricotColumn) r.getDifference().getTargetObject();
                }
                column = columnManager.findColumnById(column.getId());
                if (column != null) {
                    List<ApricotConstraint> constraints = constraintManager.getConstraintsByColumn(column);
                    if (constraints != null && !constraints.isEmpty()) {
                        ret.addAll(constraints);
                    }
                }
            }
        }

        return ret;
    }

    public String removeRelatedConstraints(Set<ApricotConstraint> constraints, String schema) {
        StringBuilder sb = new StringBuilder();

        if (!constraints.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--             DROP CONSTRAINTS             \n");
            sb.append("--(+THOSE WHICH INCLUDE THE CHANGED COLUMNS)\n");
            sb.append("--******************************************\n");
            for (ApricotConstraint cnstr : constraints) {
                sb.append(scriptGenerator.dropConstraint(cnstr, schema));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }
}
