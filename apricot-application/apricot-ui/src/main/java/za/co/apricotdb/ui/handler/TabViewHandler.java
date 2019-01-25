package za.co.apricotdb.ui.handler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;
import za.co.apricotdb.viewport.canvas.ElementType;

/**
 * The controller of Tab- related functions.
 * 
 * @author Anton Nazarov
 * @since 12/01/2019
 */
@Component
public class TabViewHandler {

    @Autowired
    CanvasBuilder canvasBuilder;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    ViewManager viewManager;

    /**
     * Build a new Tab and populate it with the initial data.
     */
    public Tab buildTab(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        Tab tab = new Tab();

        ScrollPane scroll = buildScrollPane();
        scroll.setContent((Pane) canvas);

        tab.setContent(scroll);

        TabInfoObject o = new TabInfoObject();
        o.setCanvas(canvas);
        o.setView(view);
        o.setSnapshot(snapshot);
        tab.setUserData(o);
        
        //  TODO use where needed and remove 
        // tab.setStyle("-fx-font-weight: bold;"); normal

        return tab;
    }

    public void saveCanvasAllocationMap(CanvasAllocationMap map, ApricotView view) {
        for (CanvasAllocationItem alloc : map.getAllocations()) {
            ApricotObjectLayout layout = layoutManager.findLayoutByName(view, alloc.getName());

            if (layout != null) {
                layout.setObjectLayout(alloc.getPropertiesAsString());
            } else {
                // a new layout needs to be added
                LayoutObjectType objectType = LayoutObjectType.TABLE;
                if (alloc.getType() == ElementType.RELATIONSHIP) {
                    objectType = LayoutObjectType.RELATIONSHIP;
                }

                layout = new ApricotObjectLayout(objectType, alloc.getName(), alloc.getPropertiesAsString(), view);
            }

            layoutManager.saveObjectLayout(layout);
        }
    }
    
    public CanvasAllocationMap readCanvasAllocationMap(ApricotView view) {
        CanvasAllocationMap map = new CanvasAllocationMap();
        for (ApricotObjectLayout layout : view.getObjectLayouts()) {
            CanvasAllocationItem alloc = new CanvasAllocationItem();
            alloc.setName(layout.getObjectName());
            ElementType type = null;
            if (layout.getObjectType() == LayoutObjectType.TABLE) {
                type = ElementType.ENTITY;
            } else {
                type = ElementType.RELATIONSHIP;
            }
            alloc.setType(type);
            alloc.setPropertiesFromString(layout.getObjectLayout());
            
            map.addCanvasAllocationItem(alloc);
        }
        
        return map;
    }

    private ScrollPane buildScrollPane() {
        ScrollPane scroll = new ScrollPane();

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
