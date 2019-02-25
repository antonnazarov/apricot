package za.co.apricotdb.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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

    private BorderPane mainPane;

    public void setParentPane(Parent parent) {
        if (parent instanceof BorderPane) {
            this.mainPane = (BorderPane) parent;
        } else {
            this.mainPane = null;
        }
    }

    public TreeView<String> getProjectTreeView() {
        TreeView<String> ret = null;

        List<Node> l = getCenterNode().getItems();

        if (l != null && l.size() == 2 && l.get(0) instanceof TreeView) {
            ret = (TreeView) l.get(0);
        }

        return ret;
    }

    public TabPane getProjectTabPane() {
        TabPane ret = null;

        List<Node> l = getCenterNode().getItems();

        if (l != null && l.size() == 2 && l.get(1) instanceof TabPane) {
            ret = (TabPane) l.get(1);
        }

        return ret;
    }

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
        SplitPane ret = null;

        if (mainPane.getCenter() instanceof SplitPane) {
            ret = (SplitPane) mainPane.getCenter();
        }

        return ret;
    }

    public HBox getTopNode() {
        HBox ret = null;

        if (mainPane.getTop() instanceof HBox) {
            ret = (HBox) mainPane.getTop();
        }

        return ret;
    }

    public void setApplicationData(ApplicationData appData) {
        mainPane.setUserData(appData);
    }

    public ApplicationData getApplicationData() {
        ApplicationData ret = null;

        if (mainPane.getUserData() != null) {
            ret = (ApplicationData) mainPane.getUserData();
        } else {
            ret = new ApplicationData();
            mainPane.setUserData(ret);
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

    /**
     * Initialize the menu items and buttons depending on the empty/non empty
     * environment.
     */
    private void initEnvironmentElements(boolean empty) {
        MenuBar mainMenu = getMenuBar("mainMenu");
        setMenuDisable(mainMenu.getMenus(), empty);
        MenuBar snapshotMenu = getMenuBar("snapshotMenu");
        setMenuDisable(snapshotMenu.getMenus(), empty);
        ButtonBar mainButtons = getButtonBar("mainButtons");
        List<Node> buttons = mainButtons.getButtons();
        setButtonDisable(buttons, empty);
        
        ComboBox<String> combo = getSnapshotCombo();
        combo.setUserData(null);
    }

    private HBox getMainBox() {
        for (Node n : mainPane.getChildren()) {
            if (n.getId() != null && n.getId().equals("mainBox") && n instanceof HBox) {
                return (HBox) n;
            }
        }

        return null;
    }

    private MenuBar getMenuBar(String menuId) {
        HBox b = getMainBox();
        for (Node n : b.getChildren()) {
            if (n.getId() != null && n.getId().equals(menuId) && n instanceof MenuBar) {
                return (MenuBar) n;
            }
        }

        return null;
    }

    private ButtonBar getButtonBar(String buttonBarId) {
        HBox b = getMainBox();
        for (Node n : b.getChildren()) {
            if (n.getId() != null && n.getId().equals(buttonBarId) && n instanceof ButtonBar) {
                return (ButtonBar) n;
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

    private void setButtonDisable(List<Node> buttons, boolean disable) {
        for (Node btn : buttons) {
            if (btn.getId() != null && Arrays.stream(EMPTY_RELATED_OBJECTS).anyMatch(btn.getId()::equals)) {
                btn.setDisable(disable);
            }
        }
    }
}
