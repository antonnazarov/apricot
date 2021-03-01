package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.comparator.ApricotObjectDifference;
import za.co.apricotdb.persistence.comparator.ColumnDifference;
import za.co.apricotdb.persistence.comparator.ConstraintDifference;
import za.co.apricotdb.persistence.comparator.RelationshipDifference;
import za.co.apricotdb.persistence.comparator.SnapshotComparator;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.comparator.TableDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.support.util.FieldAttributeHelper;
import za.co.apricotdb.ui.CompareSnapshotsController;
import za.co.apricotdb.ui.comparator.CompareRowType;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.comparator.CompareState;
import za.co.apricotdb.ui.comparator.CompareStateAdd;
import za.co.apricotdb.ui.comparator.CompareStateDiff;
import za.co.apricotdb.ui.comparator.CompareStateEqual;
import za.co.apricotdb.ui.comparator.CompareStateRemove;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.repository.ModelRow;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.UiConstants;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * The compare snapshots functionality.
 *
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsHandler {

    Logger logger = LoggerFactory.getLogger(CompareSnapshotsHandler.class);

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotComparator snapshotComparator;

    @Autowired
    DialogFormHandler formHandler;

    /**
     * Open the Snapshot comparator form.
     *
     * @param compareRemote if true, the form is used for the comparison between local and remote snapshots
     * @param row           if compareRemote=true, then this parameter should be not null - the Repository model row
     */
    @ApricotErrorLogger(title = "Unable to open the Compare Snapshots form")
    public void openCompareSnapshotsForm(boolean compareRemote, ModelRow row) {
        if (!compareRemote && !validate()) {
            return;
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-compare-snapshots.fxml",
                "/za/co/apricotdb/ui/toolbar/tbCompareSnapshotEnabled.png", "Compare Snapshots");
        CompareSnapshotsController controller = form.getController();
        if (!compareRemote) {
            controller.init();
        } else {
            if (row != null) {
                controller.initCompareRemote(row.getLocalSnapshot(), row.getRemoteSnapshot());
            }
        }

        form.show();
    }

    /**
     * Compare two unmanaged snapshots
     */
    @Transactional
    public TreeItem<CompareSnapshotRow> compare(ApricotSnapshot sourceSnapshot, ApricotSnapshot targetSnapshot,
                                                boolean diffOnly) {
        SnapshotDifference diff = snapshotComparator.compare(sourceSnapshot, targetSnapshot);

        CompareState state = null;
        if (diff.isDifferent()) {
            state = new CompareStateDiff();
        } else {
            state = new CompareStateEqual();
        }

        CompareSnapshotRow compareRow = new CompareSnapshotRow(diff.getSourceObject().getName(),
                diff.isDifferent(),
                diff.getTargetObject().getName(), CompareRowType.SNAPSHOT, state, "snapshot", diff);
        TreeItem<CompareSnapshotRow> root = new TreeItem<>(compareRow);

        String sourceName = null;
        String targetName = null;
        String objectName = null;

        // the cycle through the tables
        for (TableDifference td : diff.getTableDiffs()) {
            if (td.getSourceObject() != null) {
                sourceName = td.getSourceObject().getName();
                objectName = sourceName;
            } else {
                sourceName = UiConstants.ELLIPSIS;
            }

            if (td.getTargetObject() != null) {
                targetName = td.getTargetObject().getName();
                objectName = targetName;
            } else {
                targetName = UiConstants.ELLIPSIS;
            }

            if (!diffOnly || td.isDifferent()) {
                TreeItem<CompareSnapshotRow> tableRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, td.isDifferent(), targetName, CompareRowType.TABLE,
                                getCompareState(td, sourceName, targetName), objectName, td));
                root.getChildren().add(tableRow);

                // the cycle through the columns
                for (ColumnDifference cd : td.getColumnDiffs()) {
                    sourceName = formatColumn(cd.getSourceObject());
                    targetName = formatColumn(cd.getTargetObject());
                    if (!sourceName.equals(UiConstants.ELLIPSIS)) {
                        objectName = cd.getSourceObject().getName();
                    } else {
                        objectName = cd.getTargetObject().getName();
                    }
                    if (!diffOnly || cd.isDifferent()) {
                        TreeItem<CompareSnapshotRow> columnRow = new TreeItem<>(
                                new CompareSnapshotRow(sourceName, cd.isDifferent(), targetName, CompareRowType.COLUMN,
                                        getCompareState(cd, sourceName, targetName), objectName, cd));
                        tableRow.getChildren().add(columnRow);
                    }
                }

                // the cycle through constraints
                for (ConstraintDifference cnstrd : td.getConstraintDiffs()) {
                    populateConstraint(cnstrd, tableRow, diffOnly);
                }
            }
        }

        // scan through relationships
        for (RelationshipDifference rd : diff.getRelationshipDiffs()) {
            sourceName = formatRelationship(rd.getSourceObject());
            targetName = formatRelationship(rd.getTargetObject());
            if (!sourceName.equals(UiConstants.ELLIPSIS)) {

                objectName = " between " + rd.getSourceObject().getParent().getTable().getName() + " and "
                        + rd.getSourceObject().getChild().getTable().getName() + " tables";
            } else {
                objectName = " between " + rd.getTargetObject().getParent().getTable().getName() + " and "
                        + rd.getTargetObject().getChild().getTable().getName() + " tables";

            }
            if (!diffOnly || rd.isDifferent()) {
                TreeItem<CompareSnapshotRow> relationshipRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, rd.isDifferent(), targetName, CompareRowType.RELATIONSHIP,
                                getCompareState(rd, sourceName, targetName), objectName, rd));
                root.getChildren().add(relationshipRow);

                populateConstraint(rd.getPkDiff(), relationshipRow, diffOnly);
                populateConstraint(rd.getFkDiff(), relationshipRow, diffOnly);
            }
        }

        return root;
    }

    /**
     * Compare two snapshots: the source and the target ones.
     */
    @Transactional
    public TreeItem<CompareSnapshotRow> compare(String sourceSnapshot, String targetSnapshot, boolean diffOnly) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotSnapshot source = snapshotManager.getSnapshotByName(project, sourceSnapshot);
        ApricotSnapshot target = snapshotManager.getSnapshotByName(project, targetSnapshot);

        return compare(source, target, diffOnly);
    }

    private boolean validate() {
        ApricotProject project = projectManager.findCurrentProject();
        List<ApricotSnapshot> snaps = new ArrayList<>();
        if (project != null) {
            snaps = snapshotManager.getAllSnapshots(project);
        } else {
            Alert alert = alertDecorator.getErrorAlert("Compare Snapshots",
                    "The current Project does not have Snapshots");
            alert.showAndWait();

            return false;
        }

        if (snaps.size() <= 1) {
            Alert alert = alertDecorator.getErrorAlert("Compare Snapshots",
                    "There have to be at least two Snapshots in the Project to compare them");
            alert.showAndWait();
        } else {
            return true;
        }

        return false;
    }

    private CompareState getCompareState(ApricotObjectDifference<?> diff, String source, String target) {
        CompareState state = new CompareStateEqual();
        if (diff.isDifferent()) {
            if (!source.equals(UiConstants.ELLIPSIS) && !target.equals(UiConstants.ELLIPSIS)) {
                state = new CompareStateDiff();
            } else if (source.equals(UiConstants.ELLIPSIS) && !target.equals(UiConstants.ELLIPSIS)) {
                state = new CompareStateAdd();
            } else if (!source.equals(UiConstants.ELLIPSIS) && target.equals(UiConstants.ELLIPSIS)) {
                state = new CompareStateRemove();
            }
        }

        return state;
    }

    /**
     * Format the column info uniformly.
     */
    private String formatColumn(ApricotColumn column) {
        if (column == null) {
            return UiConstants.ELLIPSIS;
        }

        StringBuilder sb = new StringBuilder(column.getName());

        if (!column.isNullable()) {
            sb.append(" *");
        }
        sb.append(" ").append(column.getDataType());
        sb.append(FieldAttributeHelper.formFieldLength(column.getValueLength()));

        return sb.toString();
    }

    private void populateConstraint(ConstraintDifference cnstrd, TreeItem<CompareSnapshotRow> parentRow,
                                    boolean diffOnly) {
        if (cnstrd == null) {
            return;
        }

        String sourceName = formatConstraint(cnstrd.getSourceObject());
        String targetName = formatConstraint(cnstrd.getTargetObject());
        String objectName = null;
        CompareState state = null;
        if (!sourceName.equals(UiConstants.ELLIPSIS)) {
            objectName = cnstrd.getSourceObject().getName();
        } else {
            objectName = cnstrd.getTargetObject().getName();
        }
        if (!diffOnly || cnstrd.isDifferent()) {
            TreeItem<CompareSnapshotRow> constrRow = new TreeItem<>(
                    new CompareSnapshotRow(sourceName, cnstrd.isDifferent(), targetName, CompareRowType.CONSTRAINT,
                            getCompareState(cnstrd, sourceName, targetName), objectName, cnstrd));
            parentRow.getChildren().add(constrRow);

            sourceName = getConstraintFields(cnstrd.getSourceObject());
            targetName = getConstraintFields(cnstrd.getTargetObject());
            if (sourceName != null && targetName != null) {
                boolean different = (sourceName.length() != targetName.length());
                if (!different) {
                    state = new CompareStateEqual();
                } else {
                    state = new CompareStateDiff();
                }
                if (!diffOnly || different) {
                    TreeItem<CompareSnapshotRow> constrColRow = new TreeItem<>(new CompareSnapshotRow(sourceName,
                            different, targetName, CompareRowType.CONSTRAINT_COLUMNS, state, "list of fields", null));
                    constrRow.getChildren().add(constrColRow);
                }
            }
        }
    }

    /**
     * Format the constraint name uniformly.
     */
    private String formatConstraint(ApricotConstraint constraint) {
        if (constraint == null) {
            return UiConstants.ELLIPSIS;
        }

        StringBuilder sb = new StringBuilder(constraint.getName());
        sb.append(" (").append(constraint.getType().getAbbreviation()).append(")");

        return sb.toString();
    }

    private String getConstraintFields(ApricotConstraint constraint) {
        if (constraint == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (ApricotColumnConstraint acc : constraint.getColumns()) {
            if (sb.length() != 0) {
                sb.append(",\n");
            }
            sb.append(acc.getColumn().getName());
        }

        return sb.toString();
    }

    private String formatRelationship(ApricotRelationship relationship) {
        if (relationship == null) {
            return UiConstants.ELLIPSIS;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(relationship.getParent().getTable().getName()).append("->")
                .append(relationship.getChild().getTable().getName());

        return sb.toString();
    }
}
