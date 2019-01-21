package za.co.apricotdb.viewport.canvas;

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
public class CanvasAllocationMap {
    
    private Map<String, CanvasAllocationItem> allocations = new HashMap<>();
    
    public void addCanvasAllocationItem(CanvasAllocationItem item) {
        allocations.put(item.getName(), item);
    }
    
    public CanvasAllocationItem getAllocation(String name) {
        return allocations.get(name);
    }
    
    public List<CanvasAllocationItem> getAllocations() {
        return new ArrayList<CanvasAllocationItem>(allocations.values());
    }
    
    @Override
    public String toString() {
        return allocations.toString();
    }
}
