package za.co.apricotdb.ui.map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.viewport.event.DragInitPosition;

/**
 * This handler serves the dragging event of the Active Frame.
 *
 * @author Anton Nazarov
 * @since 01/12/2020
 */
@Component
public class ActiveFrameOnMouseDraggedHandler implements EventHandler<MouseEvent> {

    @Autowired
    MapHandler mapHandler;

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Override
    public void handle(MouseEvent mouseEvent) {
        Pane activeFrame = (Pane) mouseEvent.getSource();
        if (activeFrame.getUserData() != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            DragInitPosition initPosition = (DragInitPosition) activeFrame.getUserData();

            double offsetX = (mouseEvent.getSceneX() - initPosition.getOrgSceneX());
            double offsetY = (mouseEvent.getSceneY() - initPosition.getOrgSceneY());
            double newTranslateX = initPosition.getOrgTranslateX() + offsetX;
            double newTranslateY = initPosition.getOrgTranslateY() + offsetY;

            MapHolder holder = mapHandler.getMapHolder();
            if (checkBorderLimit(activeFrame, newTranslateX, holder.getMapCanvas(), true)) {
                activeFrame.setTranslateX(newTranslateX);
                double hValue = activeFrameHandler.getScrollHvalue(holder.getMapCanvas(), activeFrame, activeFrame.getLayoutX() + newTranslateX);
                canvasHandler.getCurrentViewTabInfo().getScroll().setHvalue(hValue);
            }
            if (checkBorderLimit(activeFrame, newTranslateY, holder.getMapCanvas(), false)) {
                activeFrame.setTranslateY(newTranslateY);
                double vValue = activeFrameHandler.getScrollVvalue(holder.getMapCanvas(), activeFrame, activeFrame.getLayoutY() + newTranslateY);
                canvasHandler.getCurrentViewTabInfo().getScroll().setVvalue(vValue);
            }
        }
    }

    private boolean checkBorderLimit(Pane activeFrame, double newTranslate, Pane mapCanvas, boolean isX) {
        if (isX) {
            double newPos = activeFrame.getLayoutX() + newTranslate;
            if (newPos < 0) {
                return false;
            }
            if (newPos+activeFrame.getWidth() > mapCanvas.getWidth()) {
                return false;
            }
        } else {
            double newPos = activeFrame.getLayoutY() + newTranslate;
            if (newPos < 0) {
                return false;
            }
            if (newPos+activeFrame.getHeight() > mapCanvas.getHeight()) {
                return false;
            }
        }

        return true;
    }
}
