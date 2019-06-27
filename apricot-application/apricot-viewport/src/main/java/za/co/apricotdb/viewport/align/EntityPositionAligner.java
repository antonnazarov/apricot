package za.co.apricotdb.viewport.align;

import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * This component helps to align the selected entities to one of four sides.
 * 
 * @author Anton Nazarov
 * @since 19/04/2019
 */
@Component
public class EntityPositionAligner {

    public boolean alignSelectedEntitiesLeft(ApricotCanvas canvas) {
        List<ApricotEntity> entities = getSelectedEntities(canvas);
        if (!eligibleForAlign(entities)) {
            return false;
        }
        alignEntitiesLeft(entities);

        return true;
    }

    public boolean alignSelectedEntitiesRight(ApricotCanvas canvas) {
        List<ApricotEntity> entities = getSelectedEntities(canvas);
        if (!eligibleForAlign(entities)) {
            return false;
        }
        alignEntitiesRight(entities);

        return true;
    }

    public boolean alignSelectedEntitiesTop(ApricotCanvas canvas) {
        List<ApricotEntity> entities = getSelectedEntities(canvas);
        if (!eligibleForAlign(entities)) {
            return false;
        }
        alignEntitiesTop(entities);

        return true;
    }

    public boolean alignSelectedEntitiesBottom(ApricotCanvas canvas) {
        List<ApricotEntity> entities = getSelectedEntities(canvas);
        if (!eligibleForAlign(entities)) {
            return false;
        }
        alignEntitiesBottom(entities);

        return true;
    }

    public void alignEntitiesLeft(List<ApricotEntity> entities) {
        double leftMostX = 10000;
        for (ApricotEntity e : entities) {
            double layoutX = e.getEntityShape().getLayoutX();
            if (layoutX < leftMostX) {
                leftMostX = layoutX;
            }
        }

        for (ApricotEntity e : entities) {
            e.getEntityShape().setLayoutX(leftMostX);
        }
    }

    public void alignEntitiesRight(List<ApricotEntity> entities) {
        double rightMostX = 0;
        for (ApricotEntity e : entities) {
            double layoutX = e.getEntityShape().getLayoutX() + e.getEntityShape().getWidth();
            if (layoutX > rightMostX) {
                rightMostX = layoutX;
            }
        }

        for (ApricotEntity e : entities) {
            e.getEntityShape().setLayoutX(rightMostX - e.getEntityShape().getWidth());
        }
    }

    public void alignEntitiesTop(List<ApricotEntity> entities) {
        double topMostY = 10000;
        for (ApricotEntity e : entities) {
            double layoutY = e.getEntityShape().getLayoutY();
            if (layoutY < topMostY) {
                topMostY = layoutY;
            }
        }

        for (ApricotEntity e : entities) {
            e.getEntityShape().setLayoutY(topMostY);
        }
    }

    public void alignEntitiesBottom(List<ApricotEntity> entities) {
        double bottomMostY = 0;
        for (ApricotEntity e : entities) {
            double layoutY = e.getEntityShape().getLayoutY() + e.getEntityShape().getHeight();
            if (layoutY > bottomMostY) {
                bottomMostY = layoutY;
            }
        }

        for (ApricotEntity e : entities) {
            e.getEntityShape().setLayoutY(bottomMostY - e.getEntityShape().getHeight());
        }
    }

    private List<ApricotEntity> getSelectedEntities(ApricotCanvas canvas) {
        return canvas.getSelectedEntities();
    }

    private boolean eligibleForAlign(List<ApricotEntity> entities) {
        return (entities != null && entities.size() > 1);
    }
}
