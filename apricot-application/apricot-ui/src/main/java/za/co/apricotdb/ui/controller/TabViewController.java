package za.co.apricotdb.ui.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

/**
 * The controller of Tab- related functions. 
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class TabViewController {
    
    @Autowired
    CanvasBuilder canvasBuilder;
    
    /**
     * Build a new Tab and populate it with the initial data.
     */
    public Tab buildTab(ApricotSnapshot snapshot, ApricotView view) {
        Tab tab = new Tab();
        
        ApricotCanvas canvas = canvasBuilder.buildCanvas();
        setCanvas(canvas);
        
        ScrollPane scroll = buildScrollPane();
        scroll.setContent((Pane)canvas);
        
        tab.setContent(scroll);
        
        return tab;
    }
    
    private void setCanvas(ApricotCanvas canvas) {
        ((Pane)canvas).setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(1))));
    }
    
    private ScrollPane buildScrollPane() {
        ScrollPane scroll = new ScrollPane();
        
        scroll.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
        
        Set<Node> nodes = scroll.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == Orientation.VERTICAL) { // HORIZONTAL is another option.
                    sb.setPrefWidth(18); // You can define your preferred width here.
                } else {
                    sb.setPrefHeight(18);
                }
            }
        }
        
        return scroll;
    }
}
