package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApricotViewHandler;

@Component
public class ApricotViewSerializer {

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

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
    public void serializeView(ViewFormModel model) {
        if (model.isNewView()) {
            serializeNewView(model);
        } else {
            serializeEditedView(model);
        }
    }

    /**
     * Serialize the new view.
     */
    private void serializeNewView(ViewFormModel model) {
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
            ApricotObjectLayout layout = new ApricotObjectLayout(l.getObjectType(), l.getObjectName(), l.getObjectLayout(), view);
            targetLayouts.add(layout);
        }
        
        view.setObjectLayouts(targetLayouts);
        viewManager.saveView(view);
    }

    private void serializeEditedView(ViewFormModel model) {

    }

    /**
     * Check if the view- name is unique.
     */
    private boolean validateName(ViewFormModel model) {
        if (model.getViewName().equals("<New View>")) {
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
