package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.persistence.entity.ViewDetailLevel;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ViewFormController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.log.ApricotInfoLogger;
import za.co.apricotdb.ui.log.LogParam;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.ApricotViewSerializer;
import za.co.apricotdb.ui.model.EditViewModelBuilder;
import za.co.apricotdb.ui.model.NewViewModelBuilder;
import za.co.apricotdb.ui.model.ViewFormModel;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * The view- related operations.
 *
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotViewHandler {

    @Autowired
    ViewManager viewManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    ObjectLayoutManager objectLayoutManager;

    @Autowired
    NewViewModelBuilder newViewModelBuilder;

    @Autowired
    EditViewModelBuilder editViewModelBuilder;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    CanvasBuilder canvasBuilder;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    TabViewHandler tabViewHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    MainAppController appController;

    @Autowired
    ApricotViewSerializer viewSerializer;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    OnKeyPressedEventHandler keyPressedEventHandler;

    @Autowired
    MapHandler mapHandler;

    public List<ApricotView> getAllViews(ApricotProject project) {
        checkGeneralView(project);

        return viewManager.getAllViews(project);
    }

    /**
     * Find all tables of the given snapshot, which have associated Layout Object.
     */
    @ApricotErrorLogger(title = "Unable to retrieve tables for the selected view")
    public List<ApricotTable> getTablesForView(ApricotSnapshot snapshot, ApricotView view) {
        List<ApricotTable> ret = new ArrayList<>();

        // get all tables first
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);

        List<ApricotObjectLayout> layouts = objectLayoutManager.getObjectLayoutsByType(view, LayoutObjectType.TABLE);
        for (ApricotObjectLayout l : layouts) {
            ApricotTable t = tables.stream().filter(table -> l.getObjectName().equals(table.getName())).findFirst()
                    .orElse(null);
            if (t != null) {
                ret.add(t);
            }
        }

        return ret;
    }

    private void checkGeneralView(ApricotProject project) {
        try {
            viewManager.getGeneralView(project);
        } catch (Exception e) {
            // if exception of any type takes place, try to re-create the General View
            viewManager.removeGeneralView(project);
            viewManager.createGeneralView(project);
        }
    }

    @ApricotErrorLogger(title = "Unable to create the View editor form")
    public void createViewEditor(TabPane viewsTabPane, ApricotView view, Tab tab) {

        // check if this is not the "Main View"
        if (view != null && view.isGeneral()) {
            Alert alert = alertDecorator.getAlert("Edit View", "You cannot edit the Main View", AlertType.WARNING);
            alert.showAndWait();
            return;
        }

        String title = null;
        ViewFormModel model = null;
        if (view == null) {
            title = "Create view";
            model = newViewModelBuilder.buildModel(viewsTabPane);
        } else {
            title = "Edit view";
            model = editViewModelBuilder.buildModel(tab);
        }
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-view-editor.fxml",
                "view-s1.jpg", title);
        ViewFormController controller = form.getController();
        controller.init(model, viewsTabPane);

        form.show();
    }

    /**
     * Generate a list of ApricotObjectLayout based on the provided list of tables,
     * included into view and the pattern view, which ApricotObjectLayout's will be
     * re-used.
     */
    public List<ApricotObjectLayout> getObjectLayoutsFromReferenceView(List<String> viewTables,
                                                                       ApricotView referenceView,
                                                                       ApricotSnapshot snapshot) {
        List<ApricotObjectLayout> ret = new ArrayList<>();

        // scan through the view tables
        for (String t : viewTables) {
            ApricotObjectLayout layout = objectLayoutManager.findLayoutByName(referenceView, t);
            if (layout != null) {
                ret.add(layout);
            }
        }

        List<ApricotTable> tables = tableManager.getTablesByNames(viewTables, snapshot);
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
        for (ApricotRelationship r : relationships) {
            ApricotObjectLayout layout = objectLayoutManager.findLayoutByName(referenceView, r.getName());
            if (layout != null) {
                ret.add(layout);
            }
        }

        return ret;
    }

    @Transactional
    @ApricotInfoLogger
    @LogParam(name = "snapshot", paramValue = "getName")
    @LogParam(name = "view", paramValue = "getName")
    public Tab createViewTab(ApricotSnapshot snapshot, ApricotView view, TabPane tabPane) {
        ApricotCanvas canvas = canvasBuilder.buildCanvas(view.getDetailLevel().toString(),
                snapshot.getProject().getErdNotation().toString());
        Tab tab = tabViewHandler.buildTab(snapshot, view, canvas);
        tab.setOnSelectionChanged(e -> {
            ((Pane) canvas).fireEvent(e);
            ((Pane) canvas).requestFocus();

            mapHandler.drawMap();
        });
        tabPane.getTabs().add(tab);

        canvasHandler.populateCanvas(snapshot, view, canvas);

        /*
         * The advanced Canvas focusing strategy:
         * any click in the current canvas or even mouse entered, makes it focused
         */
        Pane pCanvas = (Pane) canvas;
        pCanvas.setOnMouseEntered(e -> {
            pCanvas.requestFocus();
        });
        pCanvas.setOnKeyPressed(keyPressedEventHandler);
        pCanvas.focusedProperty().addListener((oVal, bOld, bNew) -> {
            BorderStroke bs;
            if (bNew) {
                bs = new BorderStroke(Color.BLUE, BorderStrokeStyle.DOTTED, new CornerRadii(0),
                        new BorderWidths(1));
            } else {
                bs = new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DOTTED, new CornerRadii(0),
                        new BorderWidths(1));
            }
            Border border = new Border(bs);
            pCanvas.setBorder(border);

            //  redraw the map
            mapHandler.drawMap();
        });

        return tab;
    }

    @ApricotErrorLogger(title = "Unable to get View by name")
    public ApricotView getViewByName(ApricotProject project, String name) {
        ApricotView ret = null;

        List<ApricotView> views = viewManager.getViewByName(project, name);
        if (views != null && views.size() > 0) {
            ret = views.get(0);
        }

        return ret;
    }

    public ApricotView readApricotView(ApricotView view) {
        return viewManager.findViewById(view.getId());
    }

    public void createDefaultView(ApricotProject project) {
        ApricotView v = new ApricotView(ApricotView.MAIN_VIEW, "The main view of the project", new java.util.Date(),
                null, true, 0, project, new ArrayList<ApricotObjectLayout>(), true, ViewDetailLevel.DEFAULT);
        project.getViews().add(v);
    }

    @ApricotErrorLogger(title = "Unable to remove Entity(s) from the View")
    public void removeEntitiesFromView(List<String> entities) {
        appController.save(null); // save the current layout
        TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
        viewSerializer.deleteEntitiesFromView(entities, tabInfo);
        snapshotHandler.synchronizeSnapshot(false);
    }

    @ApricotErrorLogger(title = "Unable to add Entity(s) into the View")
    public void addEntityToView(List<String> entities) {
        appController.save(null); // save the current layout
        TabInfoObject tabInfo = canvasHandler.getCurrentViewTabInfo();
        viewSerializer.addEntitiesToView(entities, tabInfo);
        snapshotHandler.synchronizeSnapshot(false);
        canvasHandler.makeEntitiesSelected(entities, true);
    }
}
