package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.AdvancedSearchController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A helper service of the Advanced Search Form.
 *
 * @author Anton Nazarov
 * @since 12/08/2020
 */
@Component
public class AdvancedSearchHandler {

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @ApricotErrorLogger(title = "Unable to open the Advanced Search screen")
    public void openSearchForm() {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-search.fxml",
                "search.png", "Advanced Search");
        AdvancedSearchController controller = form.getController();
        controller.init(retrievePreviousSearchCondition());

        form.show();
    }

    private String retrievePreviousSearchCondition() {
        String ret = null;
        ApricotProject project = projectManager.findCurrentProject();
        ApricotProjectParameter param =
                parameterManager.getParameterByName(project, ProjectParameterManager.PREVIOUS_ADVANCED_SEARCH);
        if (param != null) {
            ret = param.getValue();
        }

        return ret;
    }

    public void savePreviousSearchCondition(String condText) {
        ApricotProject project = projectManager.findCurrentProject();
        parameterManager.saveParameter(project, ProjectParameterManager.PREVIOUS_ADVANCED_SEARCH, condText);
    }

    public List<String> findEntitiesInFreeText(String text) {
        List<String> ret = new ArrayList<>();

        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);

        for (ApricotTable t : tables) {
            if (text.toLowerCase().contains(t.getName().toLowerCase())) {
                ret.add(t.getName());
            }
        }

        ret.sort(Comparator.comparing(String::toLowerCase));

        return ret;
    }

    public void selectEntitiesOnAllCanvas(List<String> entities) {
        List<ApricotCanvas> canvases = canvasHandler.getAllCanvases();
        for (ApricotCanvas canvas : canvases) {
            canvasHandler.makeEntitiesSelected(canvas, entities, true);
        }
    }
}
