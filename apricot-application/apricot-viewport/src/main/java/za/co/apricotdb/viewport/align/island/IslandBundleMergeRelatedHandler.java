package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class IslandBundleMergeRelatedHandler {

    public void mergeRelatedIslands(EntityIslandBundle bundle) {
        Collections.reverse(bundle.getIslands());
        List<EntityIsland> tIslands = new ArrayList<>(bundle.getIslands());

        List<EntityIsland> removals = new ArrayList<>();
        for (EntityIsland slave : bundle.getIslands()) {
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

        bundle.getIslands().removeAll(removals);
    }
}
