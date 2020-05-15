package za.co.apricotdb.ui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.handler.ProjectExplorerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The operations related with the parent (main) window of the application.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class ParentWindow {

    public static final String[] EMPTY_RELATED_OBJECTS = new String[] { "menuOpen", "menuEdit", "menuDelete",
            "menuOperations", "menuSnapshot", "buttonSave", "buttonSnapshot", "buttonView" };

    private Pane mainAppPane;
    private Application application;
    private MainAppController controller;
    private List<ApricotTable> filterTables = new ArrayList<>();
    private TabPane viewsTabPane;
    private ProgressBarModel progressBarModel;

    public void init(MainAppController controller) {
        this.controller = controller;
        progressBarModel = new ProgressBarModel(controller.mainProgressBar);
        controller.mainProgressBar.visibleProperty().bind(progressBarModel.getVisibleProperty());
        controller.mainProgressBar.progressProperty().bind(progressBarModel.getProgressProperty());
    }

    public void setParentPane(Pane mainAppPane) {
        this.mainAppPane = mainAppPane;
    }

    public TreeView<ProjectExplorerItem> getProjectTreeView() {
        return controller.projectsTreeView;
    }

    public TabPane getProjectTabPane() {
        TabPane ret = null;

        List<Node> l = getCenterNode().getItems();

        if (l != null && l.size() == 2 && l.get(1) instanceof TabPane) {
            ret = (TabPane) l.get(1);
        }

        return ret;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ComboBox<String> getSnapshotCombo() {
        ComboBox<String> ret = null;

        HBox h = getTopNode();
        for (Node n : h.getChildren()) {
            if ("snapshot_dropdown".equals(n.getId())) {
                ret = (ComboBox) n;
                break;
            }
        }

        return ret;
    }

    public SplitPane getCenterNode() {
        return (SplitPane) getMainVBox().getChildren().get(2);
    }

    public VBox getMainVBox() {
        return (VBox) mainAppPane.getChildren().get(0);
    }

    public HBox getTopNode() {
        return (HBox) getMainVBox().getChildren().get(0);
    }

    public void setApplicationData(ApplicationData appData) {
        mainAppPane.setUserData(appData);
    }

    public ApplicationData getApplicationData() {
        ApplicationData ret = null;

        if (mainAppPane.getUserData() != null) {
            ret = (ApplicationData) mainAppPane.getUserData();
        } else {
            ret = new ApplicationData();
            mainAppPane.setUserData(ret);
        }

        return ret;
    }

    public void setEmptyEnv(boolean empty) {
        ApplicationData ad = getApplicationData();
        ad.setEmptyEnv(empty);
        initEnvironmentElements(empty);
    }

    public boolean isEmptyEnv() {
        ApplicationData ad = getApplicationData();
        return ad.isEmptyEnv();
    }

    public Pane getMainAppPane() {
        return mainAppPane;
    }

    public Window getWindow() {
        return mainAppPane.getScene().getWindow();
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    
    public List<ApricotTable> getFilterTables() {
        return filterTables;
    }
    
    public TextField getFilterField() {
        return controller.getFilterField();
    }

    public TabPane getViewsTabPane() {
        return viewsTabPane;
    }

    public void setViewsTabPane(TabPane viewsTabPane) {
        this.viewsTabPane = viewsTabPane;
    }

    public ProgressBarModel getProgressBarModel() {
        return progressBarModel;
    }

    /**
     * Initialize the menu items and buttons depending on the empty/non empty
     * environment.
     */
    private void initEnvironmentElements(boolean empty) {
        MenuBar mainMenu = getMenuBar("mainMenu");
        setMenuDisable(mainMenu.getMenus(), empty);

        ComboBox<String> combo = getSnapshotCombo();
        combo.setUserData(null);
    }

    private MenuBar getMenuBar(String menuId) {
        HBox b = getTopNode();
        for (Node n : b.getChildren()) {
            if (n.getId() != null && n.getId().equals(menuId) && n instanceof MenuBar) {
                return (MenuBar) n;
            }
        }

        return null;
    }

    private void setMenuDisable(List<Menu> menus, boolean disable) {
        for (Menu m : menus) {
            if (m.getId() != null && Arrays.stream(EMPTY_RELATED_OBJECTS).anyMatch(m.getId()::equals)) {
                m.setDisable(disable);
            }
            for (MenuItem mi : m.getItems()) {
                if (mi.getId() != null && Arrays.stream(EMPTY_RELATED_OBJECTS).anyMatch(mi.getId()::equals)) {
                    mi.setDisable(disable);
                }
            }
        }
    }
}
