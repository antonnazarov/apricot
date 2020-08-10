package za.co.apricotdb.ui.handler;

import javafx.scene.control.TextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.BlackListEditController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;

import java.io.IOException;
import java.util.List;

/**
 * All operations required for Black List have been supported by this component.
 *
 * @author Anton Nazarov
 * @since 19/02/2019
 */
@Component
public class BlackListHandler {

    @Autowired
    ProjectParameterManager projectParameterManager;

    @Autowired
    DialogFormHandler formHandler;

    public String getBlackListAsString(ApricotProject project) {
        String ret = null;
        ApricotProjectParameter p = projectParameterManager.getParameterByName(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM);
        if (p != null) {
            ret = p.getValue();
        }

        return ret;
    }

    @ApricotErrorLogger(title = "Unable to retrieve the black list tables")
    public String[] getBlackListTables(ApricotProject project) {
        String[] ret = new String[]{};
        String blackList = getBlackListAsString(project);
        if (blackList != null) {
            ret = blackList.split("; ");
        }

        return ret;
    }

    public void saveBlackList(ApricotProject project, List<ApricotTable> tables) {
        StringBuilder sb = new StringBuilder();
        for (ApricotTable t : tables) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(t.getName());
        }

        projectParameterManager.saveParameter(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM, sb.toString());
    }

    public void saveStringBlackList(ApricotProject project, List<String> tables) {
        StringBuilder sb = new StringBuilder();
        for (String t : tables) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(t);
        }

        projectParameterManager.saveParameter(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM, sb.toString());
    }

    /**
     * Open the form of editing of the black list.
     */
    @ApricotErrorLogger(title = "Unable to open the Black List editor")
    public void openEditBlackListForm(TextArea blackList) throws IOException {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-blacklist-editor.fxml",
                "project-2-s1.JPG", "Edit Black List");
        BlackListEditController controller = form.getController();
        controller.init(blackList);

        form.show();
    }
}
