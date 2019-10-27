package za.co.apricotdb.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.comparator.ApricotObjectDifference;
import za.co.apricotdb.persistence.comparator.ColumnDifference;
import za.co.apricotdb.persistence.comparator.ConstraintDifference;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.comparator.TableDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
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
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;

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
    public void swapSnapshots(ActionEvent event) {

    }

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
                diff.getTargetObject().getName(), CompareRowType.SNAPSHOT, state);
        TreeItem<CompareSnapshotRow> root = new TreeItem<>(snapshots);
        root.setExpanded(true);
        compareTree.setRoot(root);

        // the cycle through the tables
        for (TableDifference td : diff.getTableDiffs()) {
            String sourceName = null;
            if (td.getSourceObject() != null) {
                sourceName = td.getSourceObject().getName();
            }
            String targetName = null;
            if (td.getTargetObject() != null) {
                targetName = td.getTargetObject().getName();
            }

            TreeItem<CompareSnapshotRow> tableRow = new TreeItem<>(new CompareSnapshotRow(sourceName, td.isDifferent(),
                    targetName, CompareRowType.TABLE, getCompareState(td, sourceName, targetName)));
            root.getChildren().add(tableRow);

            // the cycle through the columns
            for (ColumnDifference cd : td.getColumnDiffs()) {
                sourceName = formatColumn(cd.getSourceObject());
                targetName = formatColumn(cd.getTargetObject());
                TreeItem<CompareSnapshotRow> columnRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, cd.isDifferent(), targetName, CompareRowType.COLUMN,
                                getCompareState(cd, sourceName, targetName)));
                tableRow.getChildren().add(columnRow);
            }

            // the cycle through constraints
            for (ConstraintDifference cnstrd : td.getConstraintDiffs()) {
                sourceName = formatConstraint(cnstrd.getSourceObject());
                targetName = formatConstraint(cnstrd.getTargetObject());
                TreeItem<CompareSnapshotRow> constrRow = new TreeItem<>(
                        new CompareSnapshotRow(sourceName, cnstrd.isDifferent(), targetName, CompareRowType.CONSTRAINT,
                                getCompareState(cnstrd, sourceName, targetName)));
                tableRow.getChildren().add(constrRow);

                sourceName = getConstraintFields(cnstrd.getSourceObject());
                targetName = getConstraintFields(cnstrd.getTargetObject());
                if (sourceName != null && targetName != null) {
                    boolean different = (sourceName.length() != targetName.length());
                    if (!different) {
                        state = new CompareStateEqual();
                    } else {
                        state = new CompareStateDiff();
                    }
                    TreeItem<CompareSnapshotRow> constrColRow = new TreeItem<>(new CompareSnapshotRow(sourceName,
                            different, targetName, CompareRowType.CONSTRAINT_COLUMNS, state));
                    constrRow.getChildren().add(constrColRow);
                }
            }
        }
    }

    private CompareState getCompareState(ApricotObjectDifference<?> diff, String source, String target) {
        CompareState state = new CompareStateEqual();
        if (diff.isDifferent()) {
            if (source != null && target != null) {
                state = new CompareStateDiff();
            } else if (source == null && target != null) {
                state = new CompareStateAdd();
            } else if (source != null && target == null) {
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
            return null;
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
            return null;
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

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void generateScript(ActionEvent event) {

    }

    public void init() {
        ApricotSnapshot defSnapshot = snapshotManager.getDefaultSnapshot();

        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(projectManager.findCurrentProject());

        sourceSnapshot.getItems().clear();
        sourceSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));

        targetSnapshot.getItems().clear();
        targetSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        targetSnapshot.getSelectionModel().select(defSnapshot.getName());

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(sourceColumn);
        targetColumnConstructor.construct(targetColumn);
        diffColumnConstructor.construct(diffColumn);
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
