package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
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
    ObjectLayoutManager layoutManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    AlertMessageDecorator alertDecorator;
    
    @Autowired
    RelationshipManager relationshipManager;
    
    /**
     * Build a new Tab and populate it with the initial data.
     */
    public Tab buildTab(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        Tab tab = new Tab();
        tab.setText(view.getName());

        ScrollPane scroll = buildScrollPane();
        scroll.setContent((Pane) canvas);

        tab.setContent(scroll);

        TabInfoObject o = new TabInfoObject();
        o.setCanvas(canvas);
        o.setView(view);
        o.setSnapshot(snapshot);
        tab.setUserData(o);

        createTabContextMenu(tab, o);

        return tab;
    }

    @Transactional
    public ApricotView saveCanvasAllocationMap(CanvasAllocationMap map, ApricotView view) {
        view = viewHandler.readApricotView(view);
        for (CanvasAllocationItem alloc : map.getAllocations()) {
            ApricotObjectLayout layout = layoutManager.findLayoutByName(view, alloc.getName());

            if (layout != null) {
                layout.setObjectLayout(alloc.getPropertiesAsString());
                layoutManager.saveObjectLayout(layout);
                // the view needs to be updated artificially after the layout was saved
                updateLayoutInView(view, layout);
            } else {
                // a new layout needs to be added
                LayoutObjectType objectType = LayoutObjectType.TABLE;
                if (alloc.getType() == ElementType.RELATIONSHIP) {
                    objectType = LayoutObjectType.RELATIONSHIP;
                }

                layout = new ApricotObjectLayout(objectType, alloc.getName(), alloc.getPropertiesAsString(), view);
                if (view.getObjectLayouts() != null) {
                    view.getObjectLayouts().add(layout);
                } else {
                    List<ApricotObjectLayout> l = new ArrayList<>();
                    l.add(layout);
                    view.setObjectLayouts(l);
                }
            }
        }

        return viewManager.saveView(view);
    }

    private void updateLayoutInView(ApricotView view, ApricotObjectLayout layout) {
        for (ApricotObjectLayout l : view.getObjectLayouts()) {
            if (l.getId() == layout.getId()) {
                l.setObjectLayout(layout.getObjectLayout());
            }
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
    
    /**
     * Get allocation map for one table.
     */
    public CanvasAllocationMap readCanvasAllocationMap(ApricotView view, ApricotTable table) {
        CanvasAllocationMap map = new CanvasAllocationMap();
        
        ApricotObjectLayout layout = layoutManager.findLayoutByName(view, table.getName());
        if (layout != null) {
            CanvasAllocationItem alloc = new CanvasAllocationItem();
            alloc.setName(table.getName());
            alloc.setType(ElementType.ENTITY);
            alloc.setPropertiesFromString(layout.getObjectLayout());
            map.addCanvasAllocationItem(alloc);

            for (ApricotRelationship r : relationshipManager.getRelationshipsForTable(table)) {
                alloc = new CanvasAllocationItem();
                alloc.setName(r.getName());
                alloc.setType(ElementType.RELATIONSHIP);
                alloc.setPropertiesFromString(layout.getObjectLayout());
                map.addCanvasAllocationItem(alloc);
            }
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

    private void createTabContextMenu(Tab tab, TabInfoObject tabInfo) {
        if (tab.getText().equals("Main View")) {
            // not for the Main (General) view
            return;
        }

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit View");
        MenuItem delete = new MenuItem("Delete View          ");
        contextMenu.getItems().addAll(edit, delete);
        contextMenu.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                tab.getTabPane().getSelectionModel().select(tab);
            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    viewHandler.createViewEditor(tab.getTabPane(), tabInfo.getView(), tab);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
                ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(AlertType.WARNING, null, yes, no);
                alert.setTitle("Delete View");
                alert.setHeaderText("Do you really want to delete the view \"" + tab.getText() + "\"?");
                alertDecorator.decorateAlert(alert);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(no) == yes) {
                    viewManager.removeView(tabInfo.getView());
                    tab.getTabPane().getTabs().remove(tab);
                }
            }
        });

        tab.setContextMenu(contextMenu);
    }
}
