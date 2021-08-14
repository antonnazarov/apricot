package za.co.apricotdb.ui.handler;

import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;

/**
 * This component is responsible for validation of the Apricot Relationships.
 */
@Component
public class RelationshipConsistencyValidator {

    /**
     * Validate the given relationship.
     */
    public boolean validateRelationship(ApricotRelationship relationship) {
        if (!checkConstraintsPresence(relationship)) {
            return false;
        }

        if (!checkConstraintsContent(relationship)) {
            return false;
        }

        // if (!checkChildMandatoryFields(relationship)) {
        //    return false;
        // }

        return true;
    }

    private boolean checkConstraintsPresence(ApricotRelationship relationship) {

        ApricotConstraint parent = relationship.getParent();
        if (parent == null) {
            relationship.setValidationMessage("The parent constraint was hot found");
            return false;
        }

        ApricotConstraint child = relationship.getChild();
        if (child == null) {
            relationship.setValidationMessage("The child constraint was hot found");
            return false;
        }

        if (parent.getColumns() == null || parent.getColumns().isEmpty()) {
            relationship.setValidationMessage("The parent constraint does not contain any fields");
            return false;
        }

        if (child.getColumns() == null || child.getColumns().isEmpty()) {
            relationship.setValidationMessage("The child constraint does not contain any fields");
            return false;
        }

        return true;
    }

    private boolean checkConstraintsContent(ApricotRelationship relationship) {
        ApricotConstraint parent = relationship.getParent();
        ApricotConstraint child = relationship.getChild();

        if (parent.getColumns().size() != child.getColumns().size()) {
            relationship.setValidationMessage("The parent and child constraints contain different number of fields");
            return false;
        }

        int cnt = 0;
        for (ApricotColumnConstraint parentCc : parent.getColumns()) {
            ApricotColumnConstraint childCc = child.getColumns().get(cnt);
            ApricotColumn parentColumn = parentCc.getColumn();
            ApricotColumn childColumn = childCc.getColumn();

            if (!parentColumn.getDataType().equals(childColumn.getDataType())) {
                relationship.setValidationMessage("The types of the linked columns " +
                        parentColumn.getName() + "/" + childColumn.getName() +
                        " have not been equal: " + parentColumn.getDataType() + "/" + childColumn.getDataType());
                return false;
            }

            if (parentColumn.getValueLength() != null && childColumn.getValueLength() != null &&
                    !parentColumn.getValueLength().equals(childColumn.getValueLength())) {
                relationship.setValidationMessage("The lengths of the linked columns " +
                        parentColumn.getName() + "/" + childColumn.getName() +
                        " have not been equal: " + parentColumn.getValueLength() + "/" + childColumn.getValueLength());
                return false;
            }

            cnt++;
        }

        return true;
    }

    private boolean checkChildMandatoryFields(ApricotRelationship relationship) {
        boolean nullable = relationship.getChild().getColumns().get(0).getColumn().isNullable();
        for (ApricotColumnConstraint acc : relationship.getChild().getColumns()) {
            if (nullable != acc.getColumn().isNullable()) {
                relationship.setValidationMessage("The child fields of the composite FK have to be either ALL mandatory or ALL nullable");
                return false;
            }
        }

        return true;
    }
}
