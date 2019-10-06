package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The class - holder of the differences found for two snapshots.
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
        StringBuilder sb = new StringBuilder("SnapshotDifference: ");
        sb.append(source.getName()).append("->").append(target.getName());

        return sb.toString();
    }
    
    @Override
    public boolean isDifferent() {
        return source != null && target != null;
    }
}
