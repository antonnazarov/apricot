package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotTableRepository;

/**
 * The utility- class that reads the current Meta Data in the Apricot-
 * repository.
 *
 * @author Anton Nazarov
 * @since 05/10/2018
 */
@Component
public class DataReader {

    @Resource
    ApricotTableRepository tableRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Resource
    EntityManager em;

    public MetaData readMetaData() {
        MetaData ret = new MetaData();
        List<ApricotTable> tables = tableRepository.findAll();
        List<ApricotRelationship> relationships = relationshipRepository.findAll();

        ret.setTables(tables);
        ret.setRelationships(relationships);

        return ret;
    }

    public MetaData readTablesByList(List<String> tableNames) {
        MetaData ret = new MetaData();
        if (tableNames.contains("*")) {
            ret = readMetaData();
        } else {
            TypedQuery<ApricotTable> queryTbls = em.createNamedQuery("ApricotTable.getTablesByName", ApricotTable.class);
            queryTbls.setParameter("tables", tableNames);
            List<ApricotTable> tables = queryTbls.getResultList();

            TypedQuery<ApricotRelationship> queryRel = em.createNamedQuery("ApricotRelationship.getRelationshipsForTables", ApricotRelationship.class);
            queryRel.setParameter("tables", tableNames);
            List<ApricotRelationship> relationships = queryRel.getResultList();
            
            ret.setTables(tables);
            ret.setRelationships(relationships);
        }

        return ret;
    }
}
