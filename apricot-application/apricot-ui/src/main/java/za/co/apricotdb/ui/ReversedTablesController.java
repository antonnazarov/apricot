package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.CanvasAlignHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;

/**
 * The controller of the apricot-re-tables-list.fxml form.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class ReversedTablesController {

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    CanvasAlignHandler alignHandler;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ViewManager viewManager;

    @FXML
    TableView<ReversedTableRow> reversedTablesList;

    @FXML
    TableColumn<ReversedTableRow, String> tableColumn;

    @FXML
    TableColumn<ReversedTableRow, Boolean> includedColumn;

    @FXML
    Label summaryInfo;

    @FXML
    Pane mainPane;

    private MetaData metaData = null;
    private String reverseEngineeringParameters = null;

    public void init(MetaData metaData, String[] blackList, String reverseEngineeringParameters) {
        this.metaData = metaData;
        this.reverseEngineeringParameters = reverseEngineeringParameters;

        reversedTablesList.getItems().clear();
        List<ApricotTable> tables = metaData.getTables();
        Collections.sort(tables, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        for (ApricotTable t : metaData.getTables()) {
            ReversedTableRow r = null;
            if (Arrays.stream(blackList).anyMatch(t.getName()::equals)) {
                r = new ReversedTableRow(t, false);
            } else {
                r = new ReversedTableRow(t, true);
            }
            reversedTablesList.getItems().add(r);
        }
        reversedTablesList.getSelectionModel().select(0);

        tableColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, String>("tableName"));
        Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>> booleanCellFactory = new Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>>() {
            @Override
            public TableCell<ReversedTableRow, Boolean> call(TableColumn<ReversedTableRow, Boolean> p) {
                return new BooleanCell();
            }
        };
        includedColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, Boolean>("included"));
        includedColumn.setCellFactory(booleanCellFactory);
        includedColumn.setStyle("-fx-alignment: CENTER;");

        summaryInfo.setText(metaData.getTables().size() + " tables were scanned; " + blackList.length
                + " tables in the black list");
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    @FXML
    public void finish() {
        List<ApricotTable> included = new ArrayList<>();
        List<ApricotTable> excluded = new ArrayList<>();
        for (ReversedTableRow r : reversedTablesList.getItems()) {
            if (r.isIncluded()) {
                included.add(r.getTable());
            } else {
                excluded.add(r.getTable());
            }
        }

        if (reverseEngineHandler.saveReversedObjects(included, excluded, metaData.getRelationships(),
                reverseEngineeringParameters)) {
            // refresh the snapshot view
            applicationInitializer.initializeDefault();
            
            //  do the automatic alignment only in case of the coverage lower than 50%
            if (getCoverageRate(included) < 0.5) {
                alignAfterDelay(0.5).play();
            }
            getStage().close();
        }
    }

    @FXML
    public void selectAll() {
        setSelectedFlag(true);
    }

    @FXML
    public void unselectAll() {
        setSelectedFlag(false);
    }

    private void setSelectedFlag(boolean flag) {
        for (ReversedTableRow r : reversedTablesList.getItems()) {
            r.setIncluded(flag);
        }
        reversedTablesList.refresh();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    public PauseTransition alignAfterDelay(double delay) {
        PauseTransition transition = new PauseTransition(Duration.seconds(delay));
        transition.setOnFinished(e -> {
            alignHandler.alignCanvasIslands();
        });

        return transition;
    }

    /**
     * Calculate the coverage rate for the selected tables in the current active
     * project.
     */
    private double getCoverageRate(List<ApricotTable> tables) {
        double rate = 0.0;

        ApricotProject project = projectManager.findCurrentProject();
        if (project != null) {
            ApricotView view = viewManager.getGeneralView(project);
            if (view != null) {
                rate = layoutManager.calcCoverageRate(tables, view);
            }
        }

        return rate;
    }
}
