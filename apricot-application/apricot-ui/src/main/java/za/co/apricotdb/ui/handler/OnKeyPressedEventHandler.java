package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This is a main entry point into the key press handling in UI.
 * 
 * @author Anton Nazarov
 * @since 20/03/2019
 */
@Component
public class OnKeyPressedEventHandler implements EventHandler<KeyEvent> {

    @Autowired
    DeleteSelectedHandler deleteSelectedHandler;
    
    @Autowired
    ApricotCanvasHandler canvasHandler;
    
    @Autowired
    ApricotEntityHandler entityHandler;

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
        case DELETE:
            deleteSelectedHandler.deleteSelected();
            break;
        case ENTER:
            ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
            List<ApricotEntity> ent = canvas.getSelectedEntities();
            if (ent.size() == 1) {
                try {
                    entityHandler.openEntityEditorForm(false, ent.get(0).getTableName());
                } catch (IOException e) {
                    e.printStackTrace();
                }                
            }
            break;
        default:
            break;
        }
        
        event.consume();
    }
}
