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
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;

@Component
public class ApricotViewSerializer {

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ObjectLayoutManager layoutManager;

    public boolean validate(ViewFormModel model) {
        if (!validateName(model)) {
            Alert alert = getAlert("Please enter a unique name of the view");
            alert.showAndWait();

            return false;
        }

        if (!validateViewTables(model)) {
            Alert alert = getAlert("Please choose some tables for the view");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, text, ButtonType.OK);
        alert.setTitle("Save View");
        alert.setHeaderText("Unable to save the view");

        return alert;
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

        ApricotView generalView = viewManager.getGeneralView(project);
        List<ApricotObjectLayout> layouts = viewHandler.getObjectLayoutsFromPatternView(model.getViewTables(),
                generalView, model.getSnapshot());

        List<ApricotObjectLayout> targetLayouts = new ArrayList<>();
        for (ApricotObjectLayout l : layouts) {
            ApricotObjectLayout layout = new ApricotObjectLayout(l.getObjectType(), l.getObjectName(),
                    l.getObjectLayout(), view);
            targetLayouts.add(layout);
        }

        view.setObjectLayouts(targetLayouts);

        return viewManager.saveView(view);
    }

    /**
     * Serialize the existing view.
     */
    private ApricotView serializeEditedView(ViewFormModel model) {
        ApricotView ret = null;

        if (model.getTabInfo() != null) {
            TabInfoObject tabInfo = model.getTabInfo();
            ApricotView view = tabInfo.getView();
            view.setName(model.getViewName());
            view.setComment(model.getComment());
            view.setUpdated(new java.util.Date());

            List<String> original = getOriginalViewTables(tabInfo.getSnapshot(), tabInfo.getView());
            handleDeletedTables(original, model.getViewTables(), tabInfo.getView());
            handleNewTables(original, model.getViewTables(), tabInfo.getSnapshot().getProject(), model,
                    tabInfo.getView());

            ret = viewManager.saveView(view);
        }

        return ret;
    }

    private List<String> getOriginalViewTables(ApricotSnapshot snapshot, ApricotView view) {
        List<String> ret = new ArrayList<>();

        List<ApricotTable> origViewTables = viewHandler.getTablesForView(snapshot, view);
        for (ApricotTable t : origViewTables) {
            ret.add(t.getName());
        }

        return ret;
    }

    /**
     * Handle those tables, which were removed from the right side- list.
     */
    private void handleDeletedTables(List<String> origTables, List<String> newTables, ApricotView view) {
        List<ApricotObjectLayout> targetLayouts = new ArrayList<>(view.getObjectLayouts());
        for (String orig : origTables) {
            if (!newTables.contains(orig)) {
                // delete all layout artifacts, related to the original table
                for (ApricotObjectLayout layout : view.getObjectLayouts()) {
                    if (layout.getObjectName().equals(orig)) {
                        targetLayouts.remove(layout);
                        layoutManager.deleteObjectLayout(layout);
                    }
                }
            }
        }

        view.setObjectLayouts(targetLayouts);
    }

    /**
     * Handle tables and relationships, which were added in the editing session.
     */
    private void handleNewTables(List<String> origTables, List<String> newTables, ApricotProject project,
            ViewFormModel model, ApricotView view) {
        ApricotView generalView = viewManager.getGeneralView(project);
        List<ApricotObjectLayout> layouts = viewHandler.getObjectLayoutsFromPatternView(model.getViewTables(),
                generalView, model.getSnapshot());

        for (String nt : newTables) {
            if (!origTables.contains(nt)) {
                for (ApricotObjectLayout l : layouts) {
                    if (l.getObjectType() == LayoutObjectType.TABLE && l.getObjectName().equals(nt)) {
                        ApricotObjectLayout layout = new ApricotObjectLayout(l.getObjectType(), l.getObjectName(),
                                l.getObjectLayout(), view);
                        view.getObjectLayouts().add(layout);
                    }
                }
            }
        }
    }

    /**
     * Check if the view- name is unique.
     */
    private boolean validateName(ViewFormModel model) {

        if (model.getViewName() == null || model.getViewName().equals("") || model.getViewName().equals("<New View>")) {
            return false;
        }

        ApricotSnapshot snapshot = model.getSnapshot();
        List<ApricotView> sameViews = viewManager.getViewByName(snapshot.getProject(), model.getViewName());
        if (sameViews != null && sameViews.size() > 0) {
            return false;
        }

        return true;
    }

    private boolean validateViewTables(ViewFormModel model) {
        if (model.getViewTables() == null || model.getViewTables().size() == 0) {
            return false;
        }

        return true;
    }
}
