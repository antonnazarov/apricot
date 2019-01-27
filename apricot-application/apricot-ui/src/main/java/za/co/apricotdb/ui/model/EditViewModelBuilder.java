package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;

@Component
public class EditViewModelBuilder {
    
    @Autowired
    ApricotViewHandler viewHandler;
    
    @Autowired
    TableManager tableManager;
    
    @Autowired    
    NewViewModelBuilder viewModelBuilder;
    
    public ViewFormModel buildModel(Tab tab) {
        ViewFormModel model = new ViewFormModel();
        model.setNewView(false);
        model.setTab(tab);
        
        if (tab.getUserData() instanceof TabInfoObject) {
            TabInfoObject tabInfo = (TabInfoObject) tab.getUserData();
            ApricotView view = tabInfo.getView();
            
            model.setViewName(view.getName());
            model.setComment(view.getComment());
            model.setSnapshot(tabInfo.getSnapshot());
            
            List<ApricotTable> viewTables = viewHandler.getTablesForView(tabInfo.getSnapshot(), tabInfo.getView());
            List<String> viewTableNames = getTableNames(viewTables);
            Collections.sort(viewTableNames);
            List<ApricotTable> snapTables = tableManager.getTablesForSnapshot(tabInfo.getSnapshot());
            List<String> snapTableNames = getTableNames(snapTables);
            
            List<String> availableTables = viewModelBuilder.getAvailableTablesFromSnapshotAndSelected(snapTableNames, viewTableNames);
            Collections.sort(availableTables);
            
            model.addAvailableTables(availableTables);
            model.addViewTables(viewTableNames);
        }
        
        return model;
    }
    
    private List<String> getTableNames(List<ApricotTable> tables) {
        List<String> ret = new ArrayList<>();
        for (ApricotTable t : tables) {
            ret.add(t.getName());
        }
        
        return ret;
    }
}
