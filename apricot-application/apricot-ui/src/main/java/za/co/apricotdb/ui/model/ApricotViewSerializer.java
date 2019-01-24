package za.co.apricotdb.ui.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;

@Component
public class ApricotViewSerializer {
    
    @Autowired
    ViewManager viewManager;
    
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
        ApricotView view = new ApricotView();
        view.setName(model.getViewName());
        view.setComment(model.getComment());
        
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
