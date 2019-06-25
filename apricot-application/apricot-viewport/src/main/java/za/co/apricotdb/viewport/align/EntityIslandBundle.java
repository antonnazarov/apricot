package za.co.apricotdb.viewport.align;

import java.util.List;
import java.util.TreeSet;

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
    private TreeSet<EntityIsland> islands = new TreeSet<>();

    public EntityIslandBundle(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    public void initialize() {
        TreeSet<EntityIsland> tIslands = new TreeSet<>();
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                EntityIsland island = new EntityIsland((ApricotEntity) elm);
                tIslands.add(island);
            }
        }

        // scan islands and remove duplicate relationships

        while (tIslands.size() > 0) {
            EntityIsland current = tIslands.first();
            islands.add(current);
            tIslands.remove(current);
            for (EntityIsland island : tIslands) {
                if (island != current) {
                    List<ApricotEntity> related = current.getRelatedEntities();
                    for (ApricotEntity ent : related) {
                        island.removeEntity(ent);
                    }
                }
            }
        }
        
        System.out.println(islands);
    }
}
