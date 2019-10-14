package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The class - holder of the differences found between two Apricot Snapshots.
 * 
 * @author Anton Nazarov
 * @since 06/10/2019
 */
public class SnapshotDifference implements ApricotObjectDifference<ApricotSnapshot> {

    private ApricotSnapshot source;
    private ApricotSnapshot target;
    private List<TableDifference> tableDiffs;
    private List<RelationshipDifference> relationshipDiffs;

    public SnapshotDifference(ApricotSnapshot source, ApricotSnapshot target) {
        this.source = source;
        this.target = target;
        tableDiffs = new ArrayList<>();
        relationshipDiffs = new ArrayList<>();
    }

    @Override
    public ApricotSnapshot getSourceObject() {
        return source;
    }

    @Override
    public ApricotSnapshot getTargetObject() {
        return target;
    }

    public List<TableDifference> getTableDiffs() {
        return tableDiffs;
    }

    public List<RelationshipDifference> getRelationshipDiffs() {
        return relationshipDiffs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Snapshot: ").append(source != null ? source.getName() : EMPTY).append("->")
                .append(target != null ? target.getName() : EMPTY);
        getDiffFlag(sb);

        for (TableDifference td : tableDiffs) {
            sb.append(td.toString());
        }

        for (RelationshipDifference rd : relationshipDiffs) {
            sb.append(rd.toString());
        }

        return sb.toString();
    }

    @Override
    public boolean isDifferent() {
        return source == null || target == null || isTablesDif() || isRelationshipsDif();
    }

    private boolean isTablesDif() {
        for (TableDifference td : tableDiffs) {
            if (td.isDifferent()) {
                return true;
            }
        }

        return false;
    }

    private boolean isRelationshipsDif() {
        for (RelationshipDifference rd : relationshipDiffs) {
            if (rd.isDifferent()) {
                return true;
            }
        }

        return false;
    }
}
