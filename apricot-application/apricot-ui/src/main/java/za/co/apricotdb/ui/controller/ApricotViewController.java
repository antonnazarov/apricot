package za.co.apricotdb.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.persistence.repository.ApricotViewRepository;

/**
 * The view- related operations.
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotViewController {
    
    @Resource
    ApricotViewRepository viewRepository;
    
    @Autowired
    ViewManager viewManager;
    
    @Autowired
    TableManager tableManager;
    
    @Autowired
    ObjectLayoutManager objectLayoutManager;

    public List<ApricotView> getAllViews(ApricotProject project) {
        checkGeneralView(project);

        return viewManager.getAllViews(project);
    }
    
    /**
     * Find all tables of the given snapshot, which have associated Layout Object.
     */
    public List<ApricotTable> getTablesForView(ApricotSnapshot snapshot, ApricotView view) {
        List<ApricotTable> ret = new ArrayList<>();
        
        //  get all tables first
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        
        List<ApricotObjectLayout> layouts = objectLayoutManager.getObjectLayoutsByType(view, LayoutObjectType.TABLE);
        for (ApricotObjectLayout l : layouts) {
            ApricotTable t = tables.stream()
                    .filter(table -> l.getObjectName().equals(table.getName()))
                    .findFirst()
                    .orElse(null);
            if (t != null) {
                ret.add(t);
            }
        }
        
        return ret;
    }
    
    private ApricotView createGeneralView(ApricotProject project) {
        ApricotView generalView = new ApricotView("Main View", "The main (general) view of the project " + project.getName(), 
                new java.util.Date(), null, true, 0, project, null);
        return viewRepository.save(generalView);
    }
    
    private void checkGeneralView(ApricotProject project) {
        try {
            viewManager.getGeneralView(project);
        } catch (Exception e) {
            //  if exception of any type takes place, try to re-create the General View
            viewManager.removeGeneralView(project);
            createGeneralView(project);
        }
    }
}
