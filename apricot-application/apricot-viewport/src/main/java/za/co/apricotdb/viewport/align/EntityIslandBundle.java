package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The bundle of the Entity Island holder.
 * 
 * @author Anton Nazarov
 * @since 24/06/2019
 */
public class EntityIslandBundle {

    private ApricotCanvas canvas;
    private List<EntityIsland> islands = new ArrayList<>();
    private List<EntityIsland> standAlone = new ArrayList<>();

    public EntityIslandBundle(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    public List<EntityIsland> getIslands() {
        return islands;
    }

    public List<EntityIsland> getStandAlone() {
        return standAlone;
    }

    public void initialize() {

        initIslands(islands, canvas);

        System.out.println("----------------------------------------");
        System.out.println("                 ORIGINAL");
        System.out.println("----------------------------------------");
        System.out.println(islands);

        handleStandAlone(islands, standAlone);

        System.out.println("----------------------------------------");
        System.out.println("                 NO STAND ALONES");
        System.out.println("----------------------------------------");
        System.out.println(islands);

        removeDuplicates(islands);

        System.out.println("----------------------------------------");
        System.out.println("                 REMOVED DUPLICATES");
        System.out.println("----------------------------------------");
        System.out.println(islands);

        islands.removeAll(getUnlinkedIslands(islands));

        System.out.println("----------------------------------------");
        System.out.println("                 REMOVED UNLINKED");
        System.out.println("----------------------------------------");
        System.out.println(islands);

        removeFullyIncluded(islands);

        System.out.println("----------------------------------------");
        System.out.println("                 REMOVED FULLY INCLUDED");
        System.out.println("----------------------------------------");
        System.out.println(islands);

        mergeRelatedIslands(islands);

        System.out.println("----------------------------------------");
        System.out.println("                 MERGED RELATED");
        System.out.println("----------------------------------------");
        System.out.println(islands);

    }

    private void initIslands(List<EntityIsland> islands, ApricotCanvas canvas) {
        islands.clear();
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                EntityIsland island = new EntityIsland((ApricotEntity) elm);
                islands.add(island);
            }
        }
        sortIslands(islands);
    }

    /**
     * Scan islands and remove duplicate relationships.
     */
    private void removeDuplicates(List<EntityIsland> islands) {
        List<EntityIsland> tmpIslands = new ArrayList<>(islands);
        islands.clear();
        while (tmpIslands.size() > 0) {
            EntityIsland current = tmpIslands.get(0);
            islands.add(current);
            tmpIslands.remove(current);
            for (EntityIsland island : tmpIslands) {
                for (EntityAllocation ent : current.getRelatedEntities()) {
                    island.removeEntity(ent);
                }
                island.removeEntity(current.getCore());
            }
            sortIslands(tmpIslands);
        }
    }

    private List<EntityIsland> getUnlinkedIslands(List<EntityIsland> islands) {
        List<EntityIsland> ret = new ArrayList<>();
        for (EntityIsland island : islands) {
            if (island.getIslandRank() == 1) {
                ret.add(island);
            }
        }

        return ret;
    }

    private void handleStandAlone(List<EntityIsland> islands, List<EntityIsland> standAlone) {
        standAlone.clear();
        List<EntityIsland> unlinked = getUnlinkedIslands(islands);
        standAlone.addAll(unlinked);
        islands.removeAll(unlinked);
    }

    /**
     * Remove the islands which entities have been fully included into the high rank
     * islands.
     */
    private void removeFullyIncluded(List<EntityIsland> islands) {
        List<EntityIsland> delete = new ArrayList<>();
        Set<EntityAllocation> allocs = new HashSet<>();

        for (EntityIsland isl : islands) {
            if (allocs.containsAll(isl.getAllEntities())) {
                delete.add(isl);
            } else {
                allocs.addAll(isl.getAllEntities());
            }
        }

        islands.removeAll(delete);
    }

    private void mergeRelatedIslands(List<EntityIsland> islands) {
        sortIslands(islands);
        Collections.reverse(islands);
        List<EntityIsland> tIslands = new ArrayList<>(islands);

        List<EntityIsland> removals = new ArrayList<>();
        for (EntityIsland slave : islands) {
            if (tIslands.remove(slave)) {
                for (EntityIsland master : tIslands) {
                    boolean remove = false;
                    if (slave.getAllEntities().contains(master.getCore())) {
                        slave.removeEntity(master.getCore());
                        master.merge(slave, true);
                        remove = true;
                    } else if (master.getAllEntities().contains(slave.getCore())) {
                        master.removeEntity(slave.getCore());
                        master.merge(slave, false);
                        remove = true;
                    }

                    if (remove) {
                        removals.add(slave);
                        break;
                    }
                }
            }
        }

        islands.removeAll(removals);
        sortIslands(islands);
    }

    private void sortIslands(List<EntityIsland> islands) {
        islands.sort(new Comparator<EntityIsland>() {
            @Override
            public int compare(EntityIsland i1, EntityIsland i2) {
                if (i1.getIslandRank() == i2.getIslandRank()) {
                    return i2.getChildren().size() - i1.getChildren().size();
                }

                return i2.getIslandRank() - i1.getIslandRank();
            }
        });
    }
}
