package za.co.apricotdb.ui;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
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
}
