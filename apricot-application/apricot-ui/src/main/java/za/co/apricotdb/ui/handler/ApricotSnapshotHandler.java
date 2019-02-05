package za.co.apricotdb.ui.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class ApricotSnapshotHandler {

    public void createDefaultSnapshot(ApricotProject project) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ApricotSnapshot snapshot = new ApricotSnapshot("Initial snapshot, created " + df.format(new java.util.Date()),
                new java.util.Date(), null, "The snapshot, created by default", true, project,
                new ArrayList<ApricotTable>());
        project.getSnapshots().add(snapshot);
    }
}
