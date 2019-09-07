package za.co.apricotdb.ui.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ViewDetailLevel;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This component draws and handles the Canvas context menu.
 * 
 * @author Anton Nazarov
 * @since 06/05/2019
 */
@Component
public class CanvasContextMenuHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotEntityHandler entityHandler;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    CanvasAlignHandler aligner;

    @Autowired
    ApricotClipboardHandler clipboardHandler;

    @Autowired
    MainAppController appController;

    public void createCanvasContextMenu(ApricotCanvas canvas, double x, double y) {
        ApricotView view = canvasHandler.getCurrentView();

        TabPane tabPane = parentWindow.getProjectTabPane();

        MenuItem editViewEntity = new MenuItem("Edit View");
        editViewEntity.setOnAction(e -> {
            try {
                viewHandler.createViewEditor(tabPane, view, tabPane.getSelectionModel().getSelectedItem());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        MenuItem newEntity = new MenuItem("New Entity");
        newEntity.setOnAction(e -> {
            try {
                entityHandler.openEntityEditorForm(true, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        MenuItem refreshCanvas = new MenuItem("Refresh <F5>");
        refreshCanvas.setOnAction(e -> {
            snapshotHandler.syncronizeSnapshot(false);
        });

        MenuItem paste = new MenuItem("Paste <Ctrl+V>");
        paste.setOnAction(e -> {
            clipboardHandler.pasteSelectedFromClipboard();
        });

        RadioMenuItem rSimple = new RadioMenuItem("Simple View");
        rSimple.setOnAction(e -> {
            changeViewDetailLevel(ViewDetailLevel.SIMPLE, view);
        });
        RadioMenuItem rDefault = new RadioMenuItem("Default View");
        rDefault.setOnAction(e -> {
            changeViewDetailLevel(ViewDetailLevel.DEFAULT, view);
        });
        RadioMenuItem rExtended = new RadioMenuItem("Extended View");
        rExtended.setOnAction(e -> {
            changeViewDetailLevel(ViewDetailLevel.EXTENDED, view);
        });
        ToggleGroup toggleGroup = new ToggleGroup();
        rSimple.setToggleGroup(toggleGroup);
        rDefault.setToggleGroup(toggleGroup);
        rExtended.setToggleGroup(toggleGroup);

        switch (view.getDetailLevel()) {
        case DEFAULT:
            toggleGroup.selectToggle(rDefault);
            break;
        case SIMPLE:
            toggleGroup.selectToggle(rSimple);
            break;
        case EXTENDED:
            toggleGroup.selectToggle(rExtended);
            break;
        }

        MenuItem alignEntities = new MenuItem("Align Entities");
        alignEntities.setOnAction(e -> {
            aligner.alignCanvasIslands();
        });

        ContextMenu contextMenu = new ContextMenu();
        if (clipboardHandler.containsInfoToPaste()) {
            contextMenu.getItems().addAll(paste, refreshCanvas, editViewEntity, new SeparatorMenuItem(), newEntity,
                    new SeparatorMenuItem(), rSimple, rDefault, rExtended, new SeparatorMenuItem(), alignEntities);
        } else {
            contextMenu.getItems().addAll(refreshCanvas, editViewEntity, new SeparatorMenuItem(), newEntity,
                    new SeparatorMenuItem(), rSimple, rDefault, rExtended, new SeparatorMenuItem(), alignEntities);
        }
        contextMenu.setAutoHide(true);
        contextMenu.show(parentWindow.getWindow(), x, y);
    }

    private void changeViewDetailLevel(ViewDetailLevel detailLevel, ApricotView view) {
        view.setDetailLevel(detailLevel);
        viewManager.saveView(view);

        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        canvas.setDetailLevel(view.getDetailLevel().toString());

        appController.save(null);
        snapshotHandler.syncronizeSnapshot(false);
    }
}
