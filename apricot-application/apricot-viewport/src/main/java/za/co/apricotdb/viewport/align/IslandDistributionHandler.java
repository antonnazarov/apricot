package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This component is responsible for the distribution of the ready islands (the
 * islands, which objects already have been allocated).
 * 
 * @author Anton Nazarov
 * @since 27/06/2019
 */
@Component
public class IslandDistributionHandler {

    private final static double VERTICAL_ISLANDS_DISTANCE = 100;
    private final static double HORIZONTAL_ISLANDS_DISTANCE = 100;

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

        System.out.println(getAllocMapPrintContent(allocMap, bundle));
        apply(allocMap, bundle);
    }

    private void apply(List<List<EntityIsland>> allocMap, EntityIslandBundle bundle) {
        for (List<EntityIsland> column : allocMap) {
            for (EntityIsland isl : column) {
                for (EntityAllocation alloc : isl.getAllEntities()) {
                    ApricotEntityShape shape = alloc.getEntityShape();
                    shape.setLayoutX(alloc.getLayout().getX());
                    shape.setLayoutY(alloc.getLayout().getY());
                    shape.setPrefWidth(alloc.getWidth());
                }
            }
        }

        // apply stand alone's
        for (EntityIsland isl : bundle.getStandAlone()) {
            EntityAllocation alloc = isl.getCore();
            alloc.getEntityShape().setLayoutX(alloc.getLayout().getX());
            alloc.getEntityShape().setLayoutY(alloc.getLayout().getY());
        }
    }

    /**
     * Allocate the stand alone islands/entities.
     */
    private void allocateStandAlones(EntityIslandBundle bundle, List<List<EntityIsland>> allocMap) {
        Point2D islandField = getIslandFieldCoordinates(allocMap);

        double biasX = 0;
        double biasY = islandField.getY() + VERTICAL_ISLANDS_DISTANCE;
        double maxHeight = 0;
        for (EntityIsland isl : bundle.getStandAlone()) {
            if (biasX > islandField.getX()) {
                // start the next row
                biasX = 0;
                biasY += maxHeight + VERTICAL_ISLANDS_DISTANCE;
                maxHeight = 0;
            } else {
                // continue the current row
            }

            bias(isl, biasX, biasY);

            biasX += isl.getCore().getEntityShape().getWidth() + HORIZONTAL_ISLANDS_DISTANCE / 2;
            double islandHeight = isl.getCore().getHeight();
            if (islandHeight > maxHeight) {
                maxHeight = islandHeight;
            }
        }
    }

    /**
     * Calculate the right/bottom point of the islands set in the island bundle.
     */
    private Point2D getIslandFieldCoordinates(List<List<EntityIsland>> allocMap) {
        double maxX = 0;
        double maxY = 0;

        for (List<EntityIsland> column : allocMap) {
            double colHeight = getColumnHeight(column);
            if (colHeight > maxY) {
                maxY = colHeight;
            }
        }

        List<EntityIsland> lastCol = allocMap.get(allocMap.size() - 1);
        double colWidth = getColumnWidth(lastCol);
        Point2D coords = getIslandCoords(lastCol.get(0));
        maxX = coords.getX() + colWidth;

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
        EntityIsland isl = column.get(column.size() - 1); // the last island in the column
        Point2D coord = getIslandCoords(isl);
        double height = getIslandHeight(isl);

        return coord.getY() + height;
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
        } else {
            col = column;
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
        double startX = 0;
        double endX = 0;
        if (island.getParents().size() > 0) {
            startX = island.getParents().get(0).getLayout().getX();
        } else {
            startX = island.getCore().getLayout().getX();
        }

        if (island.getChildren().size() > 0) {
            endX = island.getChildren().get(0).getLayout().getX() + island.getChildren().get(0).getWidth();
        } else {
            endX = island.getCore().getLayout().getX() + island.getCore().getWidth();
        }

        return endX - startX;
    }

    private String getAllocMapPrintContent(List<List<EntityIsland>> allocMap, EntityIslandBundle bundle) {
        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (List<EntityIsland> column : allocMap) {
            sb.append("column ").append(i).append("\n");
            for (EntityIsland isl : column) {
                sb.append("parents: ");
                for (EntityAllocation alloc : isl.getParents()) {
                    printAlloc(alloc, sb);
                }
                sb.append("\n");
                sb.append("core: ");
                printAlloc(isl.getCore(), sb);
                sb.append("\n");
                sb.append("children: ");
                for (EntityAllocation alloc : isl.getChildren()) {
                    printAlloc(alloc, sb);
                }
                sb.append("\n");
            }
            sb.append("\n");
            i++;
        }

        sb.append("Stanalone: ");
        for (EntityIsland isl : bundle.getStandAlone()) {
            printAlloc(isl.getCore(), sb);
        }

        return sb.toString();
    }

    private void printAlloc(EntityAllocation alloc, StringBuilder sb) {
        sb.append(alloc.getTableName()).append(", ").append(alloc.getLayout()).append(", ").append(alloc.getWidth())
                .append(" ");
    }

    private Point2D getIslandCoords(EntityIsland island) {
        double x = 0;
        double y = 0;

        if (island.getParents().size() > 0) {
            x = island.getParents().get(0).getLayout().getX();
            y = island.getParents().get(0).getLayout().getY();
        } else {
            x = island.getCore().getLayout().getX();
        }

        double coreY = island.getCore().getLayout().getY();
        if (coreY < y) {
            y = coreY;
        }

        if (island.getChildren().size() > 0) {
            double childY = island.getChildren().get(0).getLayout().getY();
            if (childY < y) {
                y = childY;
            }
        }

        return new Point2D(x, y);
    }
}
