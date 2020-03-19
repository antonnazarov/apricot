package za.co.apricotdb.support.export;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotProject;

/**
 * This component is responsible for serialization/deserialization operations
 * around APRICOT_COLUMN_IN_CONSTRAINT table.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
@Component
public class ConstraintColumnSerializer {

    @Autowired
    ConstraintManager constraintManager;

    @Transactional
    public List<ConstraintColumnHolder> getColumnConstraints(ApricotProject project) {
        List<ConstraintColumnHolder> ret = new ArrayList<>();

        List<ApricotColumnConstraint> cnstrs = constraintManager.getColumnConstraintsByProject(project);
        for (ApricotColumnConstraint cc : cnstrs) {
            ConstraintColumnHolder h = new ConstraintColumnHolder();
            h.setConstraintId(cc.getConstraint().getId());
            h.setColumnId(cc.getColumn().getId());
            h.setOrdinalPosition(cc.getOrdinalPosition());
            ret.add(h);
        }

        return ret;
    }
}
