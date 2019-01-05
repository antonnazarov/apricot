package za.co.apricotdb.support.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The sorting utility.
 *
 * @author Anton Nazarov
 * @since 22/10/2018
 */
@Component
public class SortHelper {

    public List<ApricotTable> sortByWeight(List<ApricotTable> tables, List<ApricotRelationship> relationships) {
        List<ApricotTable> ret = new ArrayList<>();

        List<ApricotTable> tmpList = new ArrayList<>(tables);
        Map<String, ApricotTable> keys = getTablesMap(tables);
        String kernelTable = findKernelTable(relationships);
        ApricotTable aKeyTable = keys.get(kernelTable);
        

        return ret;
    }
    
    public Map<String, ApricotTable> getTablesMap(List<ApricotTable> tables) {
        Map<String, ApricotTable> ret = new HashMap<>();
        for (ApricotTable t : tables) {
            ret.put(t.getName(), t);
        }
        
        return ret;
    }

    private String findKernelTable(List<ApricotRelationship> relationships) {
        Map<String, Integer> cnt = new HashMap<>();
        int topCount = 0;
        String topTable = null;
        for (ApricotRelationship r : relationships) {
            String child = r.getChild().getName();
            String parent = r.getParent().getName();
            Integer c = cnt.get(child);
            if (c == null) {
                cnt.put(child, 1);
            } else {
                c++;
                cnt.put(child, c);
                if (c > topCount) {
                    topCount = c;
                    topTable = child;
                }
            }
            c = cnt.get(parent);
            if (c == null) {
                cnt.put(parent, 1);
            } else {
                c++;
                cnt.put(parent, c);
                if (c > topCount) {
                    topCount = c;
                    topTable = parent;
                }
            }
        }
        
        return topTable;
    }
}
