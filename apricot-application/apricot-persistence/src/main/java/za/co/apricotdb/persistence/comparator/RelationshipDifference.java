package za.co.apricotdb.persistence.comparator;

import za.co.apricotdb.persistence.entity.ApricotRelationship;

/**
 * The difference between the Apricot Relationships.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public class RelationshipDifference implements ApricotObjectDifference<ApricotRelationship> {

    private ApricotRelationship source;
    private ApricotRelationship target;
    private ConstraintDifference pkDiff;
    private ConstraintDifference fkDiff;

    public RelationshipDifference(ApricotRelationship source, ApricotRelationship target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public ApricotRelationship getSourceObject() {
        return source;
    }

    @Override
    public ApricotRelationship getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        return source == null || target == null || pkDiff.isDifferent() || fkDiff.isDifferent();
    }

    public ConstraintDifference getPkDiff() {
        return pkDiff;
    }

    public void setPkDiff(ConstraintDifference pkDiff) {
        this.pkDiff = pkDiff;
    }

    public ConstraintDifference getFkDiff() {
        return fkDiff;
    }

    public void setFkDiff(ConstraintDifference fkDiff) {
        this.fkDiff = fkDiff;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("* ");
        if (source != null) {
            sb.append(source.toString());
        } else if (target != null) {
            sb.append(target.toString());
        }
        getDiffFlag(sb);

        if (isDifferent()) {
            if (source == null && target != null) {
                sb.append(EMPTY).append("->Relationship\n");
            } else if (source != null && target == null) {
                sb.append("Relationship->").append(EMPTY).append("\n");
            }
        }
        sb.append("primary key->").append(pkDiff.toString());
        sb.append("foreign key->").append(fkDiff.toString());

        return sb.toString();
    }
}
