package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.shape.DirectRelationship;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeType;
import za.co.apricotdb.viewport.relationship.shape.TopologyHelper;

/**
 * This class allocates the core relationships: relationships between parents
 * and core of the island as well as core-to-children relationships.
 * 
 * @author Anton Nazarov
 * @since 08/09/2019
 */
@Component
public class CoreRelationshipsAllocator {
    
    Logger logger = LoggerFactory.getLogger(CoreRelationshipsAllocator.class);

    public void allocateCoreRelationships(EntityIslandBundle bundle) {
        for (EntityIsland island : bundle.getAllIslands()) {
            allocateIslandRelationships(island);
            for (EntityIsland isl : getMergedIslands(island)) {
                allocateIslandRelationships(isl);
            }
        }
    }

    private List<EntityIsland> getMergedIslands(EntityIsland island) {
        List<EntityIsland> merged = new ArrayList<>(island.getMergedIslands());
        for (EntityIsland isl : island.getMergedIslands()) {
            merged.addAll(getMergedIslands(isl));
        }

        return merged;
    }

    private void allocateIslandRelationships(EntityIsland island) {
        logger.info("Allocating core relationships of the island: " + island.getCore());
        allocateParentRelationships(island);
        allocateChildRelationships(island);
    }

    private void allocateParentRelationships(EntityIsland island) {
        List<Relationship> parentRels = new ArrayList<>();
        for (EntityAllocation alloc : island.getParents()) {
            for (ApricotRelationship r : alloc.getPrimaryLinks()) {
                if (r.getRelationshipShapeType() == RelationshipShapeType.DIRECT
                        && island.getCore().getTableName().equals(r.getChild().getTableName())) {
                    // this is a relationship between the parent and core
                    double y = TopologyHelper.getFieldY(r.getParent(), r.getPrimaryKeyName());
                    parentRels.add(new Relationship(y, r));
                }
            }
        }

        Collections.sort(parentRels);
        allocateRelationshipRulers(parentRels);
    }

    private void allocateChildRelationships(EntityIsland island) {
        List<Relationship> childRelsTop = new ArrayList<>();
        List<Relationship> childRelsBottom = new ArrayList<>();
        for (EntityAllocation alloc : island.getChildren()) {
            for (ApricotRelationship r : alloc.getForeignLinks()) {
                if (r.getRelationshipShapeType() == RelationshipShapeType.DIRECT
                        && island.getCore().getTableName().equals(r.getParent().getTableName())) {
                    // this is a relationship between the core and child
                    double coreY = TopologyHelper.getFieldY(r.getParent(), r.getPrimaryKeyName());
                    double childY = TopologyHelper.getFieldY(r.getChild(), r.getForeignKeyName());
                    if (coreY < childY) {
                        childRelsTop.add(new Relationship(coreY, r));
                    } else {
                        childRelsBottom.add(new Relationship(coreY, r));
                    }
                }
            }
        }
        
        Collections.sort(childRelsTop);
        allocateRelationshipRulers(childRelsTop);
        Collections.sort(childRelsBottom);
        Collections.reverse(childRelsBottom);
        allocateRelationshipRulers(childRelsBottom);

    }

    private void allocateRelationshipRulers(List<Relationship> relationships) {
        if (!relationships.isEmpty()) {
            ApricotEntityShape eshape = (ApricotEntityShape) relationships.get(0).relationship.getParent().getEntityShape();
            double rulerX = eshape.getLayoutX() + eshape.getWidth() + IslandAllocationHandler.HORIZONTAL_BIAS * 2;
            for (Relationship r : relationships) {
                DirectRelationship dr = (DirectRelationship) r.relationship.getShape();
                logger.info("Relationship=[" + r.relationship.getRelationshipName() + "], rulerX=[" + rulerX + "]");
                dr.setRulerX(rulerX);
                
                rulerX += IslandAllocationHandler.HORIZONTAL_BIAS;
            }
        }
    }

    /**
     * The lightweight representation of the relationship with the staring Y-
     * coordinate.
     */
    class Relationship implements Comparable<Relationship> {
        double y;
        ApricotRelationship relationship;

        Relationship(double y, ApricotRelationship relationship) {
            this.y = y;
            this.relationship = relationship;
        }

        @Override
        public int compareTo(Relationship r) {
            return new Double(y - r.y).intValue();
        }
    }
}
