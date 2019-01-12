package za.co.apricotdb.ui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The Canvas- related top level operations. 
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class ApricotCanvasController {
    
    @Autowired
    TableManager tableManager;
    
    @Autowired
    RelationshipManager relationshipManager;
    
    /**
     * Populate the given canvas with the information of snapshot, using the provided skin.
     */
    public void populateCanvas(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
    }
}
