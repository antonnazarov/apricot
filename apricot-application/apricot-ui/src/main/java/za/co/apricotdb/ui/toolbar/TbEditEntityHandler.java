package za.co.apricotdb.ui.toolbar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.NonTransactionalPort;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The tool bar button: Edit Entity.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbEditEntityHandler extends TbButtonHandlerState {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    NonTransactionalPort port;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    String entityName = getSelectedEntityName();
                    if (entityName != null) {
                        port.openEntityEditorForm(false, entityName);
                    }
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbEditEntityEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbEditEntityDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Edit selected Entity";
    }

    private String getSelectedEntityName() {
        List<ApricotEntity> entities = canvasHandler.getCurrentViewTabInfo().getCanvas().getSelectedEntities();
        if (entities.size() == 1) {
            return entities.get(0).getTableName();
        }

        return null;
    }
}
