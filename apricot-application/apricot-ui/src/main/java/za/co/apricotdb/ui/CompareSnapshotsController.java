package za.co.apricotdb.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.comparator.ApricotObjectDifference;
import za.co.apricotdb.persistence.comparator.ColumnDifference;
import za.co.apricotdb.persistence.comparator.ConstraintDifference;
import za.co.apricotdb.persistence.comparator.RelationshipDifference;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.comparator.TableDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareDiffColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareRowType;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.comparator.CompareSourceColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareState;
import za.co.apricotdb.ui.comparator.CompareStateAdd;
import za.co.apricotdb.ui.comparator.CompareStateDiff;
import za.co.apricotdb.ui.comparator.CompareStateEqual;
import za.co.apricotdb.ui.comparator.CompareStateRemove;
import za.co.apricotdb.ui.comparator.CompareTargetColumnConstructor;
import za.co.apricotdb.ui.handler.CompareScriptHandler;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;
import za.co.apricotdb.ui.util.UiConstants;

/**
 * This is a controller, which serves the apricot-compare-snapshots.fxml form.
 * 
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsController {

    Logger logger = LoggerFactory.getLogger(CompareSnapshotsController.class);

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @Autowired
    CompareSourceColumnConstructor sourceColumnConstructor;

    @Autowired
    CompareTargetColumnConstructor targetColumnConstructor;

    @Autowired
    CompareDiffColumnConstructor diffColumnConstructor;

    @Autowired
    CompareScriptHandler compareScriptHandler;

    @FXML
    ChoiceBox<String> sourceSnapshot;

    @FXML
    ChoiceBox<String> targetSnapshot;

    @FXML
    TreeTableView<CompareSnapshotRow> compareTree;

    @FXML
    TreeTableColumn<CompareSnapshotRow, String> sourceColumn;

    @FXML
    TreeTableColumn<CompareSnapshotRow, Boolean> diffColumn;

    @FXML
    TreeTableColumn<CompareSnapshotRow, String> targetColumn;

    @FXML
    AnchorPane mainPane;

    @FXML
    CheckBox diffOnlyFlag;

    @FXML
    Button generateScriptButton;

    private TreeItem<CompareSnapshotRow> root;
    private boolean compared = false;

    @FXML
    public void swapSnapshots(ActionEvent event) {
        String source = sourceSnapshot.getSelectionModel().getSelectedItem();
        String target = targetSnapshot.getSelectionModel().getSelectedItem();
        sourceSnapshot.getSelectionModel().select(target);
        targetSnapshot.getSelectionModel().select(source);

        if (compared) {
            compare(null);
        }
    }

    /**
     * Perform the comparison of the selected snapshots.
     */
    @FXML
    public void compare(ActionEvent event) {
        SnapshotDifference diff = compareSnapshotsHandler.compare(sourceSnapshot.getSelectionModel().getSelectedItem(),
                targetSnapshot.getSelectionModel().getSelectedItem());

        logger.info(diff.toString());

        CompareState state = null;
        if (diff.isDifferent()) {
            state = new CompareStateDiff();
        } else {
            state = new CompareStateEqual();
        }

        CompareSnapshotRow snapshots = new CompareSnapshotRow(diff.getSourceObject().getName(), diff.isDifferent(),
                diff.getTargetObject().getName(), CompareRowType.SNAPSHOT, state, "snapshot");
        root = new TreeItem<>(snapshots);
        root.setExpanded(true);
        compareTree.setRoot(root);

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

            if (!diffOnlyFlag.isSelected() || td.isDifferent()) {
                TreeItem<CompareSnapshotRow> tableRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, td.isDifferent(), targetName, CompareRowType.TABLE,
                                getCompareState(td, sourceName, targetName), objectName));
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
                    if (!diffOnlyFlag.isSelected() || cd.isDifferent()) {
                        TreeItem<CompareSnapshotRow> columnRow = new TreeItem<>(
                                new CompareSnapshotRow(sourceName, cd.isDifferent(), targetName, CompareRowType.COLUMN,
                                        getCompareState(cd, sourceName, targetName), objectName));
                        tableRow.getChildren().add(columnRow);
                    }
                }

                // the cycle through constraints
                for (ConstraintDifference cnstrd : td.getConstraintDiffs()) {
                    populateConstraint(cnstrd, tableRow);
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
            if (!diffOnlyFlag.isSelected() || rd.isDifferent()) {
                TreeItem<CompareSnapshotRow> relationshipRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, rd.isDifferent(), targetName, CompareRowType.RELATIONSHIP,
                                getCompareState(rd, sourceName, targetName), objectName));
                root.getChildren().add(relationshipRow);

                populateConstraint(rd.getPkDiff(), relationshipRow);
                populateConstraint(rd.getFkDiff(), relationshipRow);
            }
        }

        compareTree.refresh();
        compared = true;
        generateScriptButton.setDisable(false);
    }

    private void populateConstraint(ConstraintDifference cnstrd, TreeItem<CompareSnapshotRow> parentRow) {
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
        if (!diffOnlyFlag.isSelected() || cnstrd.isDifferent()) {
            TreeItem<CompareSnapshotRow> constrRow = new TreeItem<>(
                    new CompareSnapshotRow(sourceName, cnstrd.isDifferent(), targetName, CompareRowType.CONSTRAINT,
                            getCompareState(cnstrd, sourceName, targetName), objectName));
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
                if (!diffOnlyFlag.isSelected() || different) {
                    TreeItem<CompareSnapshotRow> constrColRow = new TreeItem<>(new CompareSnapshotRow(sourceName,
                            different, targetName, CompareRowType.CONSTRAINT_COLUMNS, state, "list of fields"));
                    constrRow.getChildren().add(constrColRow);
                }
            }
        }
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
        if (column.getValueLength() != null && column.getValueLength().length() > 0) {
            sb.append(" (").append(column.getValueLength()).append(")");
        }

        return sb.toString();
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

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    /**
     * Generate the script for the selected differences alignment.
     */
    @FXML
    public void generateScript(ActionEvent event) {
        if (compared && root != null) {
            compareScriptHandler.generateScript(root);
        }
    }

    public void init() {
        ApricotSnapshot defSnapshot = snapshotManager.getDefaultSnapshot();

        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(projectManager.findCurrentProject());
        ApricotSnapshot srcSnap = findSourceSnapshot(snaps, defSnapshot);

        sourceSnapshot.getItems().clear();
        sourceSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        sourceSnapshot.getSelectionModel().select(srcSnap.getName());

        targetSnapshot.getItems().clear();
        targetSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        targetSnapshot.getSelectionModel().select(defSnapshot.getName());

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(sourceColumn);
        targetColumnConstructor.construct(targetColumn);
        diffColumnConstructor.construct(diffColumn);

        sourceSnapshot.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });

        targetSnapshot.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });

        diffOnlyFlag.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    /**
     * Find the snapshot to initialize the source snapshot slop.
     */
    private ApricotSnapshot findSourceSnapshot(List<ApricotSnapshot> snaps, ApricotSnapshot defSnapshot) {
        ApricotSnapshot ret = null;
        int idx = snaps.indexOf(defSnapshot);
        if (idx == 0) {
            ret = snaps.get(idx + 1);
        } else {
            ret = snaps.get(idx - 1);
        }

        return ret;
    }
}
