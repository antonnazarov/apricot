package za.co.apricotdb.ui.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.map.ActiveFrameHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.canvas.ElementType;

import javax.transaction.Transactional;
import java.util.Optional;

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

    @Autowired
    NonTransactionalPort port;

    @Autowired
    ParentWindow parent;

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    /**
     * Build a new Tab and populate it with the initial data.
     */
    public Tab buildTab(ApricotSnapshot snapshot, ApricotView view, ApricotCanvas canvas) {
        Tab tab = new Tab();
        tab.setText(view.getName());

        ScrollPane scroll = buildScrollPane();
        scroll.setContent((Pane) canvas);
        tab.setContent(scroll);
        canvas.setScrollPane(scroll);

        TabInfoObject o = new TabInfoObject();
        o.setCanvas(canvas);
        o.setView(view);
        o.setSnapshot(snapshot);
        o.setScroll(scroll);
        tab.setUserData(o);

        createTabContextMenu(tab, o);

        return tab;
    }

    @Transactional
    public void saveCanvasAllocationMap(TabInfoObject tabInfo) {
        CanvasAllocationMap allocationMap = tabInfo.getCanvas().getAllocationMap();
        ApricotView view = saveCanvasAllocationMap(allocationMap, tabInfo.getView());
        tabInfo.setView(view);
    }

    @Transactional
    public ApricotView saveCanvasAllocationMap(CanvasAllocationMap map, ApricotView v) {
        ApricotView view = viewManager.findViewById(v.getId());
        for (CanvasAllocationItem alloc : map.getAllocations()) {
            ApricotObjectLayout layout = layoutManager.findLayoutByName(view, alloc.getName());

            if (layout != null) {
                layout.setObjectLayout(alloc.getPropertiesAsString());
                layoutManager.saveObjectLayout(layout);
            } else {
                // a new layout needs to be added
                LayoutObjectType objectType = LayoutObjectType.TABLE;
                if (alloc.getType() == ElementType.RELATIONSHIP) {
                    objectType = LayoutObjectType.RELATIONSHIP;
                }

                layout = new ApricotObjectLayout(objectType, alloc.getName(), alloc.getPropertiesAsString(), view);
                view.getObjectLayouts().add(layout);
            }
        }

        return viewManager.saveView(view);
    }

    public CanvasAllocationMap readCanvasAllocationMap(ApricotView view) {
        CanvasAllocationMap map = new CanvasAllocationMap();
        for (ApricotObjectLayout layout : view.getObjectLayouts()) {
            ElementType type;
            if (layout.getObjectType() == LayoutObjectType.TABLE) {
                type = ElementType.ENTITY;
            } else {
                type = ElementType.RELATIONSHIP;
            }
            map.addCanvasAllocationItem(buildAllocationItem(layout.getObjectName(), type, layout.getObjectLayout()));
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
            map.addCanvasAllocationItem(
                    buildAllocationItem(table.getName(), ElementType.ENTITY, layout.getObjectLayout()));
            for (ApricotRelationship r : relationshipManager.getRelationshipsForTable(table)) {
                layout = layoutManager.findLayoutByName(view, r.getName());
                if (layout != null) {
                    map.addCanvasAllocationItem(
                            buildAllocationItem(r.getName(), ElementType.RELATIONSHIP, layout.getObjectLayout()));
                }
            }
        }

        return map;
    }

    private CanvasAllocationItem buildAllocationItem(String name, ElementType type, String layout) {
        CanvasAllocationItem alloc = new CanvasAllocationItem();
        alloc.setName(name);
        alloc.setType(type);
        alloc.setPropertiesFromString(layout);
        return alloc;
    }

    private ScrollPane buildScrollPane() {
        ScrollPane scroll = new ScrollPane();

        //  move the Active Frame accordingly
        scroll.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (!activeFrameHandler.isDragging()) {
                activeFrameHandler.positionActiveFrame(scroll);
            }
        });
        scroll.hvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (!activeFrameHandler.isDragging()) {
                activeFrameHandler.positionActiveFrame(scroll);
            }
        });

        return scroll;
    }

    private void createTabContextMenu(Tab tab, TabInfoObject tabInfo) {
        if (tab.getText().equals(ApricotView.MAIN_VIEW)) {
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
                alert.initOwner(parent.getWindow());
                alertDecorator.decorateAlert(alert);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(no) == yes) {
                    port.removeView(tabInfo.getView());
                    tab.getTabPane().getTabs().remove(tab);
                }
            }
        });

        contextMenu.setAutoHide(true);
        tab.setContextMenu(contextMenu);
    }
}
