package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.persistence.entity.ViewDetailLevel;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.ObjectAllocationHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

@Component
public class ApricotViewSerializer {
    
    private static final double BIAS_X = 40;
    private static final double BIAS_Y = 40;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    AlertMessageDecorator alertDecorator;
    
    @Autowired
    ObjectAllocationHandler allocHandler;
    
    public boolean validate(ViewFormModel model) {
        if (!validateName(model)) {
            Alert alert = getAlert("Please enter a name of the view");
            alert.showAndWait();

            return false;
        }

        if (!validateViewTables(model)) {
            Alert alert = getAlert("Please choose some tables to be included into the view");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    @Transactional
    public ApricotView serializeView(ViewFormModel model) {
        ApricotView ret = null;
        if (model.isNewView()) {
            ret = serializeNewView(model);
        } else {
            ret = serializeEditedView(model);
        }

        return ret;
    }

    /**
     * Serialize the new view.
     */
    private ApricotView serializeNewView(ViewFormModel model) {
        ApricotProject project = model.getSnapshot().getProject();
        ApricotView view = new ApricotView();
        view.setName(model.getViewName());
        view.setComment(model.getComment());
        view.setCreated(new java.util.Date());
        view.setProject(project);
        view.setGeneral(false);
        view.setOrdinalPosition(viewManager.getMaxOrdinalPosition(project) + 1);
        view.setDetailLevel(ViewDetailLevel.DEFAULT);

        ApricotView sourceView = null;
        if (model.getSourceView() != null) {
            sourceView = viewHandler.getViewByName(project, model.getSourceView());
        } else {
            sourceView = viewManager.getGeneralView(project);
        }

        if (sourceView != null) {
            List<ApricotObjectLayout> layouts = viewHandler.getObjectLayoutsFromReferenceView(model.getViewTables(),
                    sourceView, model.getSnapshot());

            List<ApricotObjectLayout> targetLayouts = new ArrayList<>();
            for (ApricotObjectLayout l : layouts) {
                ApricotObjectLayout layout = new ApricotObjectLayout(l.getObjectType(), l.getObjectName(),
                        l.getObjectLayout(), view);
                targetLayouts.add(layout);
            }
            view.setObjectLayouts(targetLayouts);
        }
        viewManager.saveView(view);

        return viewManager.setCurrentView(view);
    }

    /**
     * Serialize the existing view.
     */
    private ApricotView serializeEditedView(ViewFormModel model) {
        ApricotView ret = null;

        if (model.getTabInfo() != null) {
            TabInfoObject tabInfo = model.getTabInfo();
            ApricotView view = viewHandler.readApricotView(tabInfo.getView());
            view.setName(model.getViewName());
            view.setComment(model.getComment());
            view.setUpdated(new java.util.Date());
            tabInfo.setView(viewManager.saveView(view));

            List<String> original = getOriginalViewTables(tabInfo);
            handleDeletedTables(original, model.getViewTables(), tabInfo);
            handleNewTables(original, model.getViewTables(), tabInfo);
            
            ret = tabInfo.getView();
        }

        return ret;
    }

    @Transactional
    public void addEntitiesToView(List<String> entities, TabInfoObject tabInfo) {
        double biasX = 0;
        double biasY = 0;
        for (String tableName : entities) {
            allocHandler.centerEntityOnView(tabInfo, tableName, biasX, biasY);
            biasX += BIAS_X;
            biasY += BIAS_Y;
        }
    }

    @Transactional
    public void deleteEntitiesFromView(List<String> entities, TabInfoObject tabInfo) {
        List<String> original = getOriginalViewTables(tabInfo);
        List<String> renewed = new ArrayList<>(original);
        for (String ent : entities) {
            if (original.contains(ent)) {
                renewed.remove(ent);
            }
        }

        handleDeletedTables(original, renewed, tabInfo);
    }

    private List<String> getOriginalViewTables(TabInfoObject tabInfo) {
        List<String> ret = new ArrayList<>();

        List<ApricotTable> origViewTables = viewHandler.getTablesForView(tabInfo.getSnapshot(), tabInfo.getView());
        for (ApricotTable t : origViewTables) {
            ret.add(t.getName());
        }

        return ret;
    }

    /**
     * Handle those tables, which were removed from the right side- list.
     */
    private void handleDeletedTables(List<String> origTables, List<String> renewedTables, TabInfoObject tabInfo) {
        ApricotView v = viewHandler.readApricotView(tabInfo.getView());
        boolean save = false;
        for (String orig : origTables) {
            if (!renewedTables.contains(orig)) {
                // delete all layout artifacts, related to the original table
                for (ApricotObjectLayout layout : v.getObjectLayouts()) {
                    if (layout.getObjectName().equals(orig)) {
                        v.getObjectLayouts().remove(layout);
                        layoutManager.deleteObjectLayout(layout);
                        save = true;
                        break;
                    }
                }
            }
        }

        if (save) {
            viewManager.saveView(v);
            tabInfo.setView(v);
        }
    }

    /**
     * Handle tables and relationships, which were added in the editing session.
     */
    private void handleNewTables(List<String> origTables, List<String> renewedTables, TabInfoObject tabInfo) {
        ApricotView v = viewHandler.readApricotView(tabInfo.getView());

        ApricotView generalView = viewManager.getGeneralView(tabInfo.getSnapshot().getProject());
        List<ApricotObjectLayout> layouts = viewHandler.getObjectLayoutsFromReferenceView(renewedTables, generalView,
                tabInfo.getSnapshot());

        boolean save = false;
        for (String nt : renewedTables) {
            if (!origTables.contains(nt)) {
                for (ApricotObjectLayout l : layouts) {
                    if (l.getObjectType() == LayoutObjectType.TABLE && l.getObjectName().equals(nt)) {
                        ApricotObjectLayout layout = new ApricotObjectLayout(l.getObjectType(), l.getObjectName(),
                                l.getObjectLayout(), v);
                        v.getObjectLayouts().add(layout);
                        save = true;
                    }
                }
            }
        }

        if (save) {
            viewManager.saveView(v);
            tabInfo.setView(v);
        }
    }

    /**
     * Check if the view- name is unique.
     */
    private boolean validateName(ViewFormModel model) {

        if (model.getViewName() == null || model.getViewName().equals("") || model.getViewName().equals("<New View>")) {
            return false;
        }

        if (model.isNewView()) {
            ApricotSnapshot snapshot = model.getSnapshot();
            List<ApricotView> sameViews = viewManager.getViewByName(snapshot.getProject(), model.getViewName());
            if (sameViews != null && sameViews.size() > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean validateViewTables(ViewFormModel model) {
        if (model.getViewTables() == null || model.getViewTables().size() == 0) {
            return false;
        }

        return true;
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Save View");
        alert.setHeaderText(text);
        alertDecorator.decorateAlert(alert);

        return alert;
    }
}
