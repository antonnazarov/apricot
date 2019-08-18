package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.align.island.EntityIsland;

/**
 * A representation of the column of the islands either main, or the merged one.
 * 
 * @author Anton Nazarov
 * @since 08/08/2019
 */
public class AllocMapColumn {
    private List<EntityIsland> islands = new ArrayList<>();
    private boolean merged;
    private Point2D columnAlloc;

    public AllocMapColumn(boolean merged) {
        this.merged = merged;
    }

    public List<EntityIsland> getIslands() {
        return islands;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public Point2D getColumnAlloc() {
        return columnAlloc;
    }

    public void setColumnAlloc(double x, double y) {
        columnAlloc = new Point2D(x, y);
    }
}
