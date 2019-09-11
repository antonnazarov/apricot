package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

/**
 * The bundle of the Entity Island holder.
 * 
 * @author Anton Nazarov
 * @since 24/06/2019
 */
public class EntityIslandBundle {

    private List<EntityIsland> islands = new ArrayList<>();
    private List<EntityIsland> standAloneEntities = new ArrayList<>(); // the islands, include one entity and not
                                                                       // related to other islands
    private List<EntityIsland> standAloneIslands = new ArrayList<>(); // the islands with no relationships with other
                                                                      // ones

    public List<EntityIsland> getIslands() {
        return islands;
    }

    public List<EntityIsland> getStandAloneEntities() {
        return standAloneEntities;
    }

    public List<EntityIsland> getStandAloneIslands() {
        return standAloneIslands;
    }

    /**
     * Return all islands related and standalone.
     */
    public List<EntityIsland> getAllIslands() {
        List<EntityIsland> ret = new ArrayList<>();

        ret.addAll(islands);
        ret.addAll(standAloneIslands);

        return ret;

    }

    /**
     * Retrieve all entities in the bundle (core and merged) as a flat list.
     */
    public List<EntityIsland> getEntityIslandsFlat() {
        List<EntityIsland> ret = new ArrayList<>();

        List<EntityIsland> all = getAllIslands();
        for (EntityIsland isl : all) {
            ret.addAll(getMergedIslands(isl));
        }

        return ret;
    }

    private List<EntityIsland> getMergedIslands(EntityIsland island) {
        List<EntityIsland> ret = new ArrayList<>();
        for (EntityIsland isl : island.getMergedIslands()) {
            ret.addAll(getMergedIslands(isl));
        }
        ret.add(island);

        return ret;
    }

    public String getMergedIslandsAsString() {
        StringBuilder sb = new StringBuilder();

        for (EntityIsland isl : islands) {
            if (isl.getMergedIslands().size() > 0) {
                sb.append("The master Island: ").append(isl.getCore().getTableName()).append("\n");
                for (EntityIsland misl : isl.getMergedIslands()) {
                    sb.append("---->").append(misl);
                }
            }
        }

        return sb.toString();
    }
}
