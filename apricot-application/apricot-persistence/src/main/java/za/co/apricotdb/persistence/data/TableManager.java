package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotTableRepository;

@Component
public class TableManager {

    @Resource
    EntityManager em;
    
    @Resource
    ApricotTableRepository tableRep;

    public List<ApricotTable> getTablesForSnapshot(ApricotSnapshot snapshot) {
        List<ApricotTable> ret = new ArrayList<>();

        TypedQuery<ApricotTable> query = em.createNamedQuery("ApricotTable.getTablesBySnapshot", ApricotTable.class);
        query.setParameter("snapshot", snapshot);
        ret = query.getResultList();

        return ret;
    }

    public List<ApricotTable> getTablesByNames(List<String> tableNames, ApricotSnapshot snapshot) {
        TypedQuery<ApricotTable> query = em.createNamedQuery("ApricotTable.getTablesByName", ApricotTable.class);
        query.setParameter("tables", tableNames);
        query.setParameter("snapshot", snapshot);
        return query.getResultList();
    }
    
    public ApricotTable saveTable(ApricotTable table) {
        return tableRep.save(table);
    }
    
    public ApricotTable getTableByName(String name, ApricotSnapshot snapshot) {
        ApricotTable ret = null;
        
        TypedQuery<ApricotTable> query = em.createNamedQuery("ApricotTable.getTableByName", ApricotTable.class);
        query.setParameter("name", name);
        query.setParameter("snapshot", snapshot);
        List<ApricotTable> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res.get(0);
        }
        
        return ret;
    }
}
