package za.co.apricotdb.viewport.canvas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The allocation of all objects in the current Canvas.
 *  
 * @author Anton Nazarov
 * @since 17/01/2019
 */
public class CanvasAllocationMap implements Serializable {
    
    private static final long serialVersionUID = -8123393340945466942L;
    
    private Map<String, CanvasAllocationItem> allocations = new HashMap<>();
    
    public void addCanvasAllocationItem(CanvasAllocationItem item) {
        allocations.put(item.getName(), item);
    }
    
    public CanvasAllocationItem getAllocation(String name) {
        return allocations.get(name);
    }
    
    public List<CanvasAllocationItem> getAllocations() {
        return new ArrayList(allocations.values());
    }

    public boolean isEmpty() {
        return allocations.isEmpty();
    }
    
    @Override
    public String toString() {
        return allocations.toString();
    }
}
