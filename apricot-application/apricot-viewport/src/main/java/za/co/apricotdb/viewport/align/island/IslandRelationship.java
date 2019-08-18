package za.co.apricotdb.viewport.align.island;

import java.util.Comparator;
import java.util.List;

/**
 * The relationship between the current island and other one.
 * 
 * @author Anton Nazarov
 * @since 18/08/2019
 */
public class IslandRelationship {

    private EntityIsland island;
    private EntityIsland relatedIsland;
    private boolean coreRelationship;

    public IslandRelationship(EntityIsland island, EntityIsland relatedIsland, boolean coreRelationship) {
        this.island = island;
        this.relatedIsland = relatedIsland;
        this.coreRelationship = coreRelationship;
    }

    public EntityIsland getIsland() {
        return island;
    }

    public EntityIsland getRelatedIsland() {
        return relatedIsland;
    }

    public boolean isCoreRelationship() {
        return coreRelationship;
    }

    /**
     * Get the relationship the most eligible for pairing the islands.
     */
    public static IslandRelationship getTopRelationship(List<IslandRelationship> relationships) {
        if (relationships.isEmpty()) {
            return null;
        }

        relationships.sort(new Comparator<IslandRelationship>() {
            @Override
            public int compare(IslandRelationship r1, IslandRelationship r2) {

                if (r1.isCoreRelationship() && !r2.isCoreRelationship()) {
                    return -1;
                }

                if (!r1.isCoreRelationship() && r2.isCoreRelationship()) {
                    return 1;
                }

                if ((r1.isCoreRelationship() && r2.isCoreRelationship())
                        || (!r1.isCoreRelationship() && !r2.isCoreRelationship())) {
                    return r1.getRelatedIsland().getIslandRank() - r2.getRelatedIsland().getIslandRank();
                }

                return 0;
            }
        });

        return relationships.get(0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (coreRelationship ? 1231 : 1237);
        result = prime * result + ((island == null) ? 0 : island.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IslandRelationship other = (IslandRelationship) obj;
        if (coreRelationship != other.coreRelationship) {
            return false;
        }
        if (island == null) {
            if (other.island != null) {
                return false;
            }
        } else if (!island.equals(other.island)) {
            return false;
        }
        return true;
    }
}
