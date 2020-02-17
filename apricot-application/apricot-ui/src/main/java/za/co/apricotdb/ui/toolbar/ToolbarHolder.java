package za.co.apricotdb.ui.toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;

/**
 * This Java bean holds the references to all tool bar buttons.
 * 
 * @author Anton Nazarov
 * @since 20/09/2019
 *
 */
@Component
public class ToolbarHolder {

    @Autowired
    TbNewProjectHandler tbNewProjectHandler;
    @Autowired
    TbOpenProjectHandler tbOpenProjectHandler;
    @Autowired
    TbEditProjectHandler tbEditProjectHandler;
    @Autowired
    TbSaveHandler tbSaveHandler;
    @Autowired
    TbAlignBottomHandler tbAlignBottomHandler;
    @Autowired
    TbAlignLeftHandler tbAlignLeftHandler;
    @Autowired
    TbAlignRightHandler tbAlignRightHandler;
    @Autowired
    TbAlignTopHandler tbAlignTopHandler;
    @Autowired
    TbAllocateEntitiesHandler tbAllocateEntitiesHandler;
    @Autowired
    TbCompareSnapshotHandler tbCompareSnapshotHandler;
    @Autowired
    TbDeleteScriptHandler tbDeleteScriptHandler;
    @Autowired
    TbDropScriptHandler tbDropScriptHandler;
    @Autowired
    TbEditEntityHandler tbEditEntityHandler;
    @Autowired
    TbEditSnapshotHandler tbEditSnapshotHandler;
    @Autowired
    TbEditViewHandler tbEditViewHandler;
    @Autowired
    TbExcelReportHandler tbExcelReportHandler;
    @Autowired
    TbCreateScriptHandler tbInsertScriptHandler;
    @Autowired
    TbMinimizeWidthHandler tbMinimizeWidthHandler;
    @Autowired
    TbNewEntityHandler tbNewEntityHandler;
    @Autowired
    TbNewRelationshipHandler tbNewRelationshipHandler;
    @Autowired
    TbNewSnapshotHandler tbNewSnapshotHandler;
    @Autowired
    TbNewViewHandler tbNewViewHandler;
    @Autowired
    TbRefreshHandler tbRefreshHandler;
    @Autowired
    TbResetAllocationHandler tbResetAllocationHandler;
    @Autowired
    TbSameWidthHandler tbSameWidthHandler;
    @Autowired
    TbSearchHandler tbSearchHandler;
    @Autowired
    TbUndoHandler tbUndoHandler;
    @Autowired
    TbReverseEngineeringHandler tbReverseEngineeringHandler;

    Map<TbButton, TbButtonHandler> buttons = new HashMap<>();

    public void init(Button tbNewProject, Button tbOpenProject, Button tbEditProject, Button tbSave, Button tbUndo,
            Button tbRefresh, Button tbNewSnapshot, Button tbEditSnapshot, Button tbCompareSnapshot, Button tbNewView,
            Button tbEditView, Button tbNewEntity, Button tbEditEntity, Button tbNewRelationship, Button tbSearch,
            Button tbAlignLeft, Button tbAlignRight, Button tbAlignTop, Button tbAlignBottom, Button tbSameWidth,
            Button tbMinimizeWidth, Button tbAllocateEntities, Button tbResetAllocation, Button tbExcelReport,
            Button tbInsertScript, Button tbDeleteScript, Button tbDropScript, Button tbReverseEngineering) {

        tbNewProjectHandler.initButton(tbNewProject);
        buttons.put(TbButton.tbNewProject, tbNewProjectHandler);

        tbOpenProjectHandler.initButton(tbOpenProject);
        buttons.put(TbButton.tbOpenProject, tbOpenProjectHandler);

        tbEditProjectHandler.initButton(tbEditProject);
        buttons.put(TbButton.tbEditProject, tbEditProjectHandler);

        tbSaveHandler.initButton(tbSave);
        buttons.put(TbButton.tbSave, tbSaveHandler);

        tbAlignBottomHandler.initButton(tbAlignBottom);
        buttons.put(TbButton.tbAlignBottom, tbAlignBottomHandler);

        tbAlignLeftHandler.initButton(tbAlignLeft);
        buttons.put(TbButton.tbAlignLeft, tbAlignLeftHandler);

        tbAlignRightHandler.initButton(tbAlignRight);
        buttons.put(TbButton.tbAlignRight, tbAlignRightHandler);

        tbAllocateEntitiesHandler.initButton(tbAllocateEntities);
        buttons.put(TbButton.tbAllocateEntities, tbAllocateEntitiesHandler);

        tbAlignTopHandler.initButton(tbAlignTop);
        buttons.put(TbButton.tbAlignTop, tbAlignTopHandler);

        tbCompareSnapshotHandler.initButton(tbCompareSnapshot);
        buttons.put(TbButton.tbCompareSnapshot, tbCompareSnapshotHandler);

        tbDeleteScriptHandler.initButton(tbDeleteScript);
        buttons.put(TbButton.tbDeleteScript, tbDeleteScriptHandler);

        tbDropScriptHandler.initButton(tbDropScript);
        buttons.put(TbButton.tbDropScript, tbDropScriptHandler);

        tbEditEntityHandler.initButton(tbEditEntity);
        buttons.put(TbButton.tbEditEntity, tbEditEntityHandler);

        tbEditSnapshotHandler.initButton(tbEditSnapshot);
        buttons.put(TbButton.tbEditSnapshot, tbEditSnapshotHandler);

        tbEditViewHandler.initButton(tbEditView);
        buttons.put(TbButton.tbEditView, tbEditViewHandler);

        tbExcelReportHandler.initButton(tbExcelReport);
        buttons.put(TbButton.tbExcelReport, tbExcelReportHandler);

        tbInsertScriptHandler.initButton(tbInsertScript);
        buttons.put(TbButton.tbInsertScript, tbInsertScriptHandler);

        tbMinimizeWidthHandler.initButton(tbMinimizeWidth);
        buttons.put(TbButton.tbMinimizeWidth, tbMinimizeWidthHandler);

        tbNewEntityHandler.initButton(tbNewEntity);
        buttons.put(TbButton.tbNewEntity, tbNewEntityHandler);

        tbNewRelationshipHandler.initButton(tbNewRelationship);
        buttons.put(TbButton.tbNewRelationship, tbNewRelationshipHandler);

        tbNewSnapshotHandler.initButton(tbNewSnapshot);
        buttons.put(TbButton.tbNewSnapshot, tbNewSnapshotHandler);

        tbNewViewHandler.initButton(tbNewView);
        buttons.put(TbButton.tbNewView, tbNewViewHandler);

        tbRefreshHandler.initButton(tbRefresh);
        buttons.put(TbButton.tbRefresh, tbRefreshHandler);

        tbResetAllocationHandler.initButton(tbResetAllocation);
        buttons.put(TbButton.tbResetAllocation, tbResetAllocationHandler);

        tbSameWidthHandler.initButton(tbSameWidth);
        buttons.put(TbButton.tbSameWidth, tbSameWidthHandler);

        tbSearchHandler.initButton(tbSearch);
        buttons.put(TbButton.tbSearch, tbSearchHandler);

        tbUndoHandler.initButton(tbUndo);
        buttons.put(TbButton.tbUndo, tbUndoHandler);

        tbUndoHandler.initButton(tbUndo);
        buttons.put(TbButton.tbUndo, tbUndoHandler);

        tbReverseEngineeringHandler.initButton(tbReverseEngineering);
        buttons.put(TbButton.tbReverseEngineering, tbReverseEngineeringHandler);
    }

    public void enableAll() {
        for (TbButtonHandler h : buttons.values()) {
            h.enable();
        }
    }

    public void disableAll() {
        for (TbButtonHandler h : buttons.values()) {
            h.disable();
        }
    }

    public void enable(List<TbButton> btns) {
        for (TbButton b : btns) {
            enable(b);
        }
    }

    public void disable(List<TbButton> btns) {
        for (TbButton b : btns) {
            disable(b);
        }
    }

    public void enable(TbButton btn) {
        TbButtonHandler h = buttons.get(btn);
        if (h != null) {
            h.enable();
        }
    }

    public void disable(TbButton btn) {
        TbButtonHandler h = buttons.get(btn);
        if (h != null) {
            h.disable();
        }
    }
}
