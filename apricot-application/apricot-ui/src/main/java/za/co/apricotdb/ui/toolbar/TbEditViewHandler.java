package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;

/**
 * The tool bar button: Edit View.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbEditViewHandler extends TbButtonHandlerState {

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    try {
                        ApricotView view = canvasHandler.getCurrentView();
                        TabPane tabPane = parentWindow.getProjectTabPane();
                        viewHandler.createViewEditor(tabPane, view, tabPane.getSelectionModel().getSelectedItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbEditViewEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbEditViewDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Edit current View";
    }
}
