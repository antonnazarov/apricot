package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;

/**
 * This component is responsible for the distribution of the ready islands (the
 * islands, which objects already have been allocated).
 * 
 * @author Anton Nazarov
 * @since 27/06/2019
 */
@Component
public class IslandDistributionHandler {

    private final static double VERTICAL_ISLANDS_DISTANCE = 40;
    private final static double HORIZONTAL_ISLANDS_DISTANCE = 40;

    @Autowired
    IslandAllocationHandler allocationHandler;

    public void distributeIslands(EntityIslandBundle bundle) {
        List<List<EntityIsland>> allocMap = initAllocationMap(bundle);

        double biasX = 0;
        double biasY = 0;
        // the "horizontal" cycle
        for (List<EntityIsland> column : allocMap) {
            biasY = 0;
            // the "vertical" cycle
            for (EntityIsland isl : column) {
                bias(isl, biasX, biasY);
                biasY += getIslandHeight(isl) + VERTICAL_ISLANDS_DISTANCE;
            }

            biasX += getColumnWidth(column) + HORIZONTAL_ISLANDS_DISTANCE;
        }

        allocateStandAlones(bundle, allocMap);
    }

    private void allocateStandAlones(EntityIslandBundle bundle, List<List<EntityIsland>> allocMap) {
        Point2D islandField = getIslandFieldCoordinates(allocMap);

        double currentX = 0;
        double currentY = 0;
        double maxY = 0;
        for (EntityIsland isl : bundle.getStandAlone()) {
            if (currentX > islandField.getX()) {
                ??
            } else {
                ??
            }
            
        }
    }

    /**
     * Calculate the right/bottom point of the islands set in the island bundle.
     */
    private Point2D getIslandFieldCoordinates(List<List<EntityIsland>> allocMap) {
        double maxX = 0;
        double maxY = 0;

        // the "horizontal" cycle
        for (List<EntityIsland> column : allocMap) {
            double colHeight = getColumnHeight(column);
            if (colHeight > maxY) {
                maxY = colHeight;
            }

            double colWidth = getColumnWidth(column);
            if (colWidth > maxX) {
                maxX = colWidth;
            }
        }

        return new Point2D(maxX, maxY);
    }

    private double getColumnWidth(List<EntityIsland> column) {
        double ret = 0;

        for (EntityIsland isl : column) {
            double wdth = getIslandWidth(isl);
            if (wdth > ret) {
                ret = wdth;
            }
        }

        return ret;
    }

    private double getColumnHeight(List<EntityIsland> column) {
        double ret = 0;

        EntityIsland isl = column.get(column.size() - 1);
        ret = isl.getCore().getLayout().getY();
        if (isl.getParents().size() > 0) {
            double parentY = isl.getParents().get(0).getLayout().getY();
            if (parentY < ret) {
                ret = parentY;
            }
        }

        if (isl.getChildren().size() > 0) {
            double childY = isl.getChildren().get(0).getLayout().getY();
            if (childY < ret) {
                ret = childY;
            }
        }

        return ret;
    }

    private List<List<EntityIsland>> initAllocationMap(EntityIslandBundle bundle) {
        List<List<EntityIsland>> allocMap = new ArrayList<>();

        for (EntityIsland isl : bundle.getIslands()) {
            allocateIsland(isl, allocMap, null);
        }

        return allocMap;
    }

    private void allocateIsland(EntityIsland island, List<List<EntityIsland>> allocMap, List<EntityIsland> column) {
        List<EntityIsland> col = null;
        if (column == null) {
            col = new ArrayList<>();
            allocMap.add(col);
        }
        col.add(island);

        if (island.getMergedIslands().size() > 0) {
            col = new ArrayList<>();
            allocMap.add(col);
            for (EntityIsland mIsl : island.getMergedIslands()) {
                allocateIsland(mIsl, allocMap, col);
            }
        }
    }

    private void bias(EntityIsland island, double biasX, double biasY) {
        allocationHandler.bias(island.getParents(), biasX, biasY);
        allocationHandler.bias(island.getChildren(), biasX, biasY);
        allocationHandler.bias(island.getCore(), biasX, biasY);
    }

    private double getIslandHeight(EntityIsland island) {
        double parentHeight = allocationHandler.getOverallHeight(island.getParents());
        double childHeight = allocationHandler.getOverallHeight(island.getChildren());
        double coreHeight = island.getCore().getHeight();

        double max = Math.max(parentHeight, childHeight);
        max = Math.max(max, coreHeight);

        return max;
    }

    private double getIslandWidth(EntityIsland island) {
        EntityAllocation alloc = null;
        if (island.getChildren().size() > 0) {
            alloc = island.getChildren().get(0);
        } else {
            alloc = island.getCore();
        }

        return alloc.getLayout().getX() + alloc.getWidth();
    }
}
