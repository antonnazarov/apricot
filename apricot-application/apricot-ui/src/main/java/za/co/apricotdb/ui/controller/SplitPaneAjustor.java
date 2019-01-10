package za.co.apricotdb.ui.controller;

import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;

@Component
public class SplitPaneAjustor {

    public void adjustSplitPaneWidth(SplitPane pane, double stageWidth) {
        if (pane.getItems().size() > 1 && pane.getItems().get(0) instanceof ScrollPane) {
            ScrollPane p = (ScrollPane) pane.getItems().get(0);
            Node n = p.getContent();
            if (n instanceof TreeView) {
                double w = ((TreeView) n).getWidth();
                double dividerPosition = w / stageWidth;
                pane.setDividerPosition(0, dividerPosition);
            }
        }
    }
}
