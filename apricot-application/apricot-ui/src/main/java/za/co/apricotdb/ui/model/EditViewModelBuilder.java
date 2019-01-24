package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import javafx.scene.control.TabPane;

@Component
public class EditViewModelBuilder {
    
    public ViewFormModel buildModel(TabPane viewsTabPane) {
        ViewFormModel model = new ViewFormModel();
        
        return model;
    }
}
