package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotViewSerializer;
import za.co.apricotdb.ui.model.ViewFormModel;

/**
 * All error handler surrounded functionality is presented in this component.
 * 
 * @author Anton Nazarov
 * @since 13/02/2020
 */
@Component
public class NonTransactionalViewHandler {

    @Autowired
    ApricotViewSerializer viewSerializer;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    TreeViewHandler treeViewHandler;

    /**
     * Save the added/edited view using the data of the model object.
     */
    @ApricotErrorLogger(title = "Unable to save view")
    public boolean saveView(ViewFormModel model, TabPane viewsTabPane, String viewName) {
        if (!viewSerializer.validate(model)) {
            return false;
        }

        ApricotView view = viewSerializer.serializeView(model);

        if (model.isNewView()) {
            Tab tab = viewHandler.createViewTab(model.getSnapshot(), view, viewsTabPane);
            viewsTabPane.getSelectionModel().select(tab);
        } else {
            model.getTab().setText(viewName);
            if (model.getTabInfo() != null) {
                model.getTabInfo().setView(view);
                canvasHandler.populateCanvas(model.getSnapshot(), view, model.getTabInfo().getCanvas());
                treeViewHandler.markEntitiesIncludedIntoView(view);
                treeViewHandler.sortEntitiesByView();
            }
        }
        
        return true;
    }
}
