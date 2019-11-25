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
import za.co.apricotdb.support.script.SqlScriptGenerator;

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
    SqlScriptGenerator scriptGenerator;

    @Transactional
    public List<ApricotConstraint> getConstraintsRelatedToColumn(List<CompareSnapshotRow> differences,
            boolean useSource) {
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

    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> differences, boolean useSource) {
        List<ApricotConstraint> ret = new ArrayList<>();

        for (CompareSnapshotRow r : differences) {
            ApricotConstraint constraint = null;
            if (useSource) {
                constraint = (ApricotConstraint) r.getDifference().getSourceObject();
            } else {
                constraint = (ApricotConstraint) r.getDifference().getTargetObject();
            }

            constraint = constraintManager.getConstraintById(constraint.getId());
            if (constraint != null) {
                ret.add(constraint);
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
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public String addRelatedConstraints(Set<ApricotConstraint> constraints, String schema) {
        StringBuilder sb = new StringBuilder();

        if (!constraints.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--             ADD CONSTRAINTS              \n");
            sb.append("--******************************************\n");

            sb.append(scriptGenerator.createConstraints(new ArrayList<>(constraints), schema, true));
            sb.append("\n\n");
        }

        return sb.toString();
    }

    public void init() {
        scriptGenerator.init();
    }
}
