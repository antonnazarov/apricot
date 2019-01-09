package za.co.apricotdb.ui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * The operations related with the parent (main) window of the application.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
public class ParentWindow {
    
    private final BorderPane mainPane;
    
    public ParentWindow(Parent parent) {
        if (parent instanceof BorderPane) {
            this.mainPane = (BorderPane) parent;
        } else {
            this.mainPane = null;
        }
    }
    
    public TreeView<String> getProjectTreeView() {
        TreeView<String> ret = null;
        
        List<Node> l = getCenterNode().getItems();
        
        if (l != null && l.size() == 2) {
            if (l.get(0) instanceof ScrollPane) {
                ScrollPane scroll = (ScrollPane) l.get(0); 
                if (scroll.getContent() instanceof TreeView) {
                    ret = (TreeView) scroll.getContent();
                }
            }
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
    
    private SplitPane getCenterNode() {
        SplitPane ret = null;
        
        if (mainPane.getCenter() instanceof SplitPane) {
            ret = (SplitPane) mainPane.getCenter();
        }
        
        return ret;
    }

    private HBox getTopNode() {
        HBox ret = null;
        
        if (mainPane.getTop() instanceof HBox) {
            ret = (HBox) mainPane.getTop();
        }
        
        return ret;
    }
    
    
}
