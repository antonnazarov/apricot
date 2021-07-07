package za.co.apricotdb.ui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotDatabaseView;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.CanvasAlignHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.viewport.canvas.ElementType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    TableColumn<ReversedTableRow, HBox> tableColumn;

    @FXML
    TableColumn<ReversedTableRow, HBox> includedColumn;

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
        Collections.sort(tables, Comparator.comparing(ApricotTable::getName));
        for (ApricotTable t : metaData.getTables()) {
            ReversedTableRow r;
            if (Arrays.stream(blackList).anyMatch(t.getName()::equals)) {
                r = new ReversedTableRow(t, false, ElementType.ENTITY);
            } else {
                r = new ReversedTableRow(t, true, ElementType.ENTITY);
            }
            reversedTablesList.getItems().add(r);
        }

        List<ApricotDatabaseView> views = metaData.getViews();
        Collections.sort(views, Comparator.comparing(ApricotDatabaseView::getDbViewName));
        for (ApricotDatabaseView v : views) {
            if (Arrays.stream(blackList).anyMatch(v.getDbViewName()::equals)) {
                reversedTablesList.getItems().add(new ReversedTableRow(new ApricotTable(v.getDbViewName(), null, null, null), false, ElementType.DBVIEW));
            } else {
                reversedTablesList.getItems().add(new ReversedTableRow(new ApricotTable(v.getDbViewName(), null, null, null), true, ElementType.DBVIEW));
            }
        }
        reversedTablesList.getSelectionModel().select(0);

        tableColumn.setCellValueFactory(new PropertyValueFactory<>("element"));
        // Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>> booleanCellFactory = p -> new BooleanCell();
        includedColumn.setCellValueFactory(new PropertyValueFactory<>("included"));
        // includedColumn.setCellFactory(booleanCellFactory);

        summaryInfo.setText(metaData.getTables().size() + " tables and " + views.size() + " views have been scanned; " + blackList.length
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
                reverseEngineeringParameters, metaData.getViews())) {
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
        transition.play();

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
