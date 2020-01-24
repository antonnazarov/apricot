package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.BlackListEditController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;

/**
 * All operations required for Black List have been supported by this component.
 *  
 * @author Anton Nazarov
 * @since 19/02/2019
 */
@Component
public class BlackListHandler {
    
    @Resource
    ApplicationContext context;
    
    @Autowired
    ProjectParameterManager projectParameterManager;
    
    public String getBlackListAsString(ApricotProject project) {
        String ret = null;
        ApricotProjectParameter p = projectParameterManager.getParameterByName(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM);
        if (p != null) {
            ret = p.getValue();
        }
        
        return ret;
    }
    
    public String[] getBlackListTables(ApricotProject project) {
        String[] ret = new String[] {};
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-blacklist-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Black List");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("project-2-s1.JPG")));

        Scene scene = new Scene(window);
        dialog.setScene(scene);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        BlackListEditController controller = loader.<BlackListEditController>getController();
        controller.init(blackList);

        dialog.show();
    }
}
