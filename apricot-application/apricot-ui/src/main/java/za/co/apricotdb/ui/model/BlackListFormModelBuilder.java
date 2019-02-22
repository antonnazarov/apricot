package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.handler.BlackListHandler;

/**
 * This component build initial model for the Black List editing form.
 * 
 * @author Anton Nazarov
 * @since 22/02/2019
 */
@Component
public class BlackListFormModelBuilder {

    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    TableManager tableManager;
    
    @Autowired
    BlackListHandler blackListHandler;
    
    
    public BlackListFormModel buildModel() {
        BlackListFormModel model = new BlackListFormModel();
        ApricotProject project = projectManager.findCurrentProject();
        
        String[] sbl = blackListHandler.getBlackListTables(project);
        List<String> bl = Arrays.asList(sbl);
        List<String> blackList = new ArrayList<>(); 
        List<String> allTables = getAllUniqueTablesInProject(project);
        for (String t : bl) {
            if (allTables.contains(t)) {
                blackList.add(t);
                allTables.remove(t);
            }
        }
        
        Collections.sort(allTables);
        Collections.sort(blackList);
        model.setAllTables(allTables);
        model.setBlackListTables(blackList);
        
        return model;
    }
    
    private List<String> getAllUniqueTablesInProject(ApricotProject project) {
        List<String> uniqueTables = new ArrayList<>();
        for (ApricotSnapshot snapshot : snapshotManager.getAllSnapshots(project)) {
            for (ApricotTable table : tableManager.getTablesForSnapshot(snapshot)) {
                if (!uniqueTables.contains(table.getName())) {
                    uniqueTables.add(table.getName());
                }
            }
        }
        
        return uniqueTables;
    }
}
