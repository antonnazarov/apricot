package za.co.apricotdb.ui.handler;

import org.apache.commons.lang3.StringUtils;
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
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.AdvancedSearchController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    ApricotViewHandler viewHandler;

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
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);

        List<String> sTables = new ArrayList<>();
        for (ApricotTable t : tables) {
            sTables.add(t.getName());
        }
        List<String> ret = fullWordSearch(text, sTables);
        ret.sort(Comparator.comparing(String::toLowerCase));

        return ret;
    }

    private List<String> fullWordSearch(String text, List<String> tables) {
        String patternString = "\\b(" + StringUtils.join(tables, "|") + ")\\b";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        List<String> ret = new ArrayList<>();
        while (matcher.find()) {
            ret.add(matcher.group(1).toUpperCase());
        }

        return ret;
    }

    public void selectEntitiesOnAllCanvas(List<String> entities) {
        List<ApricotCanvas> canvases = canvasHandler.getAllCanvases();
        for (ApricotCanvas canvas : canvases) {
            canvasHandler.makeEntitiesSelected(canvas, entities, true);
        }
    }

    /**
     * Add the selected entities to the current view.
     * Add only those entities, which not included into the diagram yet.
     */
    public void addToCurrentView(List<String> entities) {
        ApricotView view = canvasHandler.getCurrentView();
        List<ApricotTable> tables =
                viewHandler.getTablesForView(snapshotManager.getDefaultSnapshot(), view);
        List<String> toInsert = new ArrayList<>(entities);
        for (ApricotTable table : tables) {
            if (toInsert.contains(table.getName())) {
                toInsert.remove(table.getName());
            }
        }
        viewHandler.addEntityToView(toInsert);
    }
}
