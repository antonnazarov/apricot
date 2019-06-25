package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        List<EntityIsland> tmpIslands = new ArrayList<>();
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                EntityIsland island = new EntityIsland((ApricotEntity) elm);
                tmpIslands.add(island);
            }
        }
        sortIslands(tmpIslands);

        // scan islands and remove duplicate relationships
        while (tmpIslands.size() > 0) {
            EntityIsland current = tmpIslands.get(0);
            islands.add(current);
            tmpIslands.remove(current);
            for (EntityIsland island : tmpIslands) {
                if (island != current) {
                    List<ApricotEntity> related = current.getRelatedEntities();
                    for (ApricotEntity ent : related) {
                        island.removeEntity(ent);
                    }
                }
            }
            sortIslands(tmpIslands);
        }

        removeDuplicates(islands);
        handleStandAlone(islands, standAlone);
        mergeIslands(islands);

        System.out.println(islands);
    }

    private void removeDuplicates(List<EntityIsland> islands) {
        List<EntityIsland> tmpIslands = new ArrayList<>(islands);
        List<EntityIsland> notLinkedIslands = getUnlinkedIslands(islands);
        for (EntityIsland lnIsland : notLinkedIslands) {
            for (EntityIsland island : tmpIslands) {
                if (island.getIslandRank() > 0 && island.isLinkedTo(lnIsland.getCore())) {
                    islands.remove(lnIsland);
                }
            }
        }
    }

    private List<EntityIsland> getUnlinkedIslands(List<EntityIsland> islands) {
        List<EntityIsland> ret = new ArrayList<>();
        for (EntityIsland island : islands) {
            if (island.getIslandRank() == 0) {
                ret.add(island);
            }
        }

        return ret;
    }

    private void handleStandAlone(List<EntityIsland> islands, List<EntityIsland> standAlone) {
        standAlone.clear();
        standAlone.addAll(getUnlinkedIslands(islands));
        for (EntityIsland island : standAlone) {
            islands.remove(island);
        }
    }

    private void mergeIslands(List<EntityIsland> islands) {
        List<EntityIsland> delIslands = new ArrayList<>();
        for (EntityIsland cIsl : islands) {
            for (EntityIsland isl : islands) {
                if (!isl.equals(cIsl) && !delIslands.contains(cIsl) && !delIslands.contains(isl)
                        && isl.isLinkedTo(cIsl.getCore())) {
                    cIsl.merge(isl);
                    delIslands.add(isl);
                }
            }
        }

        islands.removeAll(delIslands);
        List<ApricotEntity> cores = new ArrayList<>();
        for (EntityIsland isl : islands) {
            cores.add(isl.getCore());
        }
        for (EntityIsland isl : islands) {
            for (EntityIsland mIsl : isl.getMergedIslands()) {
                for (ApricotEntity ent : mIsl.getRelatedEntities()) {
                    if (cores.contains(ent)) {
                        mIsl.removeEntity(ent);
                    }
                }
            }
        }
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
