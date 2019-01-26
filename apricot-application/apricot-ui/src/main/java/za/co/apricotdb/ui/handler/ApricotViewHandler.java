package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import za.co.apricotdb.persistence.repository.ApricotViewRepository;
import za.co.apricotdb.ui.ViewFormController;
import za.co.apricotdb.ui.model.EditViewModelBuilder;
import za.co.apricotdb.ui.model.NewViewModelBuilder;
import za.co.apricotdb.ui.model.ViewFormModel;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

/**
 * The view- related operations.
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotViewHandler {

    @Resource
    ApplicationContext context;

    @Resource
    ApricotViewRepository viewRepository;

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

    public List<ApricotView> getAllViews(ApricotProject project) {
        checkGeneralView(project);

        return viewManager.getAllViews(project);
    }

    /**
     * Find all tables of the given snapshot, which have associated Layout Object.
     */
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

    private ApricotView createGeneralView(ApricotProject project) {
        ApricotView generalView = new ApricotView("Main View",
                "The main (general) view of the project " + project.getName(), new java.util.Date(), null, true, 0,
                project, null);
        return viewRepository.save(generalView);
    }

    private void checkGeneralView(ApricotProject project) {
        try {
            viewManager.getGeneralView(project);
        } catch (Exception e) {
            // if exception of any type takes place, try to re-create the General View
            viewManager.removeGeneralView(project);
            createGeneralView(project);
        }
    }

    public void createViewEditor(Stage primaryStage, TabPane viewsTabPane, ApricotView view,
            PropertyChangeListener canvasChangeListener) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-view-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        ViewFormModel model = null;
        if (view == null) {
            dialog.setTitle("Create view");
            model = newViewModelBuilder.buildModel(viewsTabPane);
        } else {
            model = editViewModelBuilder.buildModel(viewsTabPane);
        }

        Scene addViewScene = new Scene(window);
        dialog.setScene(addViewScene);

        ViewFormController controller = loader.<ViewFormController>getController();
        controller.init(model, viewsTabPane, canvasChangeListener);

        dialog.show();
    }

    /**
     * Generate a list of ApricotObjectLayout based on the provided list of tables,
     * included into view and the pattern view, which ApricotObjectLayout's will be
     * re-used.
     */
    public List<ApricotObjectLayout> getObjectLayoutsFromPatternView(List<String> viewTables, ApricotView patternView,
            ApricotSnapshot snapshot) {
        List<ApricotObjectLayout> ret = new ArrayList<>();

        // scan through the view tables
        for (String t : viewTables) {
            ApricotObjectLayout layout = objectLayoutManager.findLayoutByName(patternView, t);
            if (layout != null) {
                ret.add(layout);
            }
        }

        List<ApricotTable> tables = tableManager.getTablesByNames(viewTables, snapshot);
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
        for (ApricotRelationship r : relationships) {
            ApricotObjectLayout layout = objectLayoutManager.findLayoutByName(patternView, r.getName());
            if (layout != null) {
                ret.add(layout);
            }
        }

        return ret;
    }

    @Transactional
    public Tab createViewTab(ApricotSnapshot snapshot, ApricotView view, TabPane tabPane,
            PropertyChangeListener canvasChangeListener) {
        ApricotCanvas canvas = canvasBuilder.buildCanvas(canvasChangeListener);
        Tab tab = tabViewHandler.buildTab(snapshot, view, canvas);
        tab.setText(view.getName());
        tabPane.getTabs().add(tab);

        canvasHandler.populateCanvas(snapshot, view, canvas);
        
        return tab;
    }
}
