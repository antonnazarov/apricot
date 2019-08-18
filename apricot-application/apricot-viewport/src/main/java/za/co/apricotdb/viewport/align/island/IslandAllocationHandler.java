package za.co.apricotdb.viewport.align.island;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.align.EntityAllocation;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The business logic of allocation of the objects included into one island.
 * 
 * @author Anton Nazarov
 * @since 27/06/2019
 */
@Component
public class IslandAllocationHandler {

    private final static double VERTICAL_DISTANCE = 20;
    private final static double HORIZONTAL_BIAS = 20;

    /**
     * Perform the relative (0,0 based) allocation of the island.
     */
    public void allocateIsland(EntityIsland island) {
        sortParents(island);
        alignEntityWidth(island);
        alignVertically(island);
        alignHorizontally(island);

        // allocate all merged islands
        for (EntityIsland isl : island.getMergedIslands()) {
            allocateIsland(isl);
        }
    }

    public double getOverallHeight(List<EntityAllocation> entities) {
        double ret = 0;
        if (entities.size() > 0) {
            if (entities.size() == 1) {
                ret = entities.get(0).getHeight();
            } else {
                double top = entities.get(0).getLayout().getY();
                EntityAllocation alloc = entities.get(entities.size() - 1);
                double bottom = alloc.getLayout().getY() + alloc.getHeight();
                ret = bottom - top;
            }
        }

        return ret;
    }

    public void bias(List<EntityAllocation> allocs, double biasX, double biasY) {
        for (EntityAllocation alloc : allocs) {
            bias(alloc, biasX, biasY);
        }
    }

    public void bias(EntityAllocation alloc, double biasX, double biasY) {
        Point2D layout = alloc.getLayout();
        alloc.setLayout(layout.getX() + biasX, layout.getY() + biasY);
    }

    /**
     * The parents included into the island have to be ordered according to the
     * sequence of the fields in the core entity.
     */
    private void sortParents(EntityIsland island) {
        Set<EntityAllocation> sortedParents = new HashSet<>();
        List<EntityAllocation> parents = island.getParents();

        EntityAllocation core = island.getCore();
        List<ApricotRelationship> links = core.getForeignLinks();

        List<FieldDetail> details = core.getDetails();
        for (FieldDetail det : details) {
            if (det.getConstraints() != null && det.getConstraints().contains("FK")) {
                for (ApricotRelationship r : links) {
                    if (r.getForeignKeyName().equals(det.getName())) {
                        EntityAllocation alloc = parents.stream()
                                .filter(a -> r.getParent().getTableName().equals(a.getTableName())).findAny()
                                .orElse(null);
                        if (alloc != null) {
                            sortedParents.add(alloc);
                        }
                    }
                }
            }
        }

        if (sortedParents.size() != parents.size()) {
            throw new RuntimeException("The original and sorted parents lists have different sizes for the Island=["
                    + island.getCore().getTableName() + "]");
        }

        parents.clear();
        parents.addAll(sortedParents);
    }

    private void alignEntityWidth(EntityIsland island) {
        if (island.getParents().size() > 0) {
            double maxParentWidth = getEntitiesMaxWidth(island.getParents());
            for (EntityAllocation alloc : island.getParents()) {
                alloc.setWidth(maxParentWidth);
            }
        }
        if (island.getChildren().size() > 0) {
            double maxChildWidth = getEntitiesMaxWidth(island.getChildren());
            for (EntityAllocation alloc : island.getChildren()) {
                alloc.setWidth(maxChildWidth);
            }
        }
        island.getCore().setWidth(island.getCore().getEntityShape().getWidth());
    }

    private double getEntitiesMaxWidth(List<EntityAllocation> entities) {
        double maxWidth = 0;
        if (entities != null && entities.size() > 0) {
            for (EntityAllocation entity : entities) {
                double width = entity.getEntityShape().getWidth();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }

        return maxWidth;
    }

    /**
     * Do all vertical alignments.
     */
    private void alignVertically(EntityIsland island) {
        alignVertically(island.getParents());
        alignVertically(island.getChildren());

        double parentHeight = getOverallHeight(island.getParents());
        double childHeight = getOverallHeight(island.getChildren());
        double coreHeight = island.getCore().getHeight();

        double max = Math.max(parentHeight, childHeight);
        max = Math.max(max, coreHeight);

        if (parentHeight < max) {
            bias(island.getParents(), 0, max / 2 - parentHeight / 2);
        }
        if (childHeight < max) {
            bias(island.getChildren(), 0, max / 2 - childHeight / 2);
        }
        if (coreHeight < max) {
            bias(island.getCore(), 0, max / 2 - coreHeight / 2);
        }
    }

    private void alignHorizontally(EntityIsland island) {
        double biasX = 0;
        if (island.getParents().size() > 0) {
            double dist = (island.getParents().size() + 2) * HORIZONTAL_BIAS;
            biasX = island.getParents().get(0).getWidth() + dist;
        }

        bias(island.getCore(), biasX, 0);

        biasX += island.getCore().getWidth() + (island.getChildren().size() + 2) * HORIZONTAL_BIAS;
        bias(island.getChildren(), biasX, 0);
    }

    private void alignVertically(List<EntityAllocation> allocs) {
        if (allocs.size() > 0) {
            double layoutY = allocs.get(0).getLayout().getY();
            for (EntityAllocation alloc : allocs) {
                Point2D l = alloc.getLayout();
                alloc.setLayout(l.getX(), layoutY);

                layoutY += alloc.getHeight() + VERTICAL_DISTANCE;
            }
        }
    }
}
