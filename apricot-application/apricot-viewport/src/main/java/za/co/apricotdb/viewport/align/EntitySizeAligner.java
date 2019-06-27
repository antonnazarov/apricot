package za.co.apricotdb.viewport.align;

import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This component helps to automatically set size (width) of the selected
 * entities.
 * 
 * @author Anton Nazarov
 * @since 20/04/2019
 *
 */
@Component
public class EntitySizeAligner {

    public void minimizeSelectedEntitiesWidth(ApricotCanvas canvas) {
        minimizeSelectedEntitiesWidth(getSelectedEntities(canvas));
    }

    public void minimizeSelectedEntitiesWidth(List<ApricotEntity> entities) {
        for (ApricotEntity entity : entities) {
            entity.getEntityShape().setPrefWidth(10);
        }
    }

    public boolean alignEntitiesSameWidth(ApricotCanvas canvas) {
        return alignEntitiesSameWidth(getSelectedEntities(canvas));
    }

    public boolean alignEntitiesSameWidth(List<ApricotEntity> entities) {
        double maxWidth = 0;
        if (entities != null && entities.size() > 1) {
            for (ApricotEntity entity : entities) {
                double width = entity.getEntityShape().getWidth();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }

            for (ApricotEntity entity : entities) {
                entity.getEntityShape().setPrefWidth(maxWidth);
            }
        } else {
            return false;
        }

        return true;
    }

    private List<ApricotEntity> getSelectedEntities(ApricotCanvas canvas) {
        return canvas.getSelectedEntities();
    }
}
