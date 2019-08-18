package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The initializer of the islands bundle.
 *  
 * @author Anton Nazarov
 * @since 18/08/2019
 */
@Component
public class IslandBundleInitializer {
    
    public void initIslands(EntityIslandBundle bundle, ApricotCanvas canvas) {
        List<EntityIsland> islands = new ArrayList<>();
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                EntityIsland island = new EntityIsland((ApricotEntity) elm);
                islands.add(island);
            }
        }
        
        bundle.getIslands().clear();
        bundle.getIslands().addAll(islands);
        
        bundle.getStandAloneEntities().clear();
        bundle.getStandAloneIslands().clear();
    }
}
