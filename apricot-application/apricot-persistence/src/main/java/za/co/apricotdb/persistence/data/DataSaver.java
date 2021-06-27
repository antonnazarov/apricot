package za.co.apricotdb.persistence.data;

import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotDatabaseView;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotDatabaseViewRepository;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotTableRepository;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Utility class to save the meta data.
 *
 * @author Anton Nazarov
 * @since 05/10/2018
 */
@Component
public class DataSaver {

    @Resource
    ApricotTableRepository tableRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Resource
    ApricotDatabaseViewRepository viewRepository;

    @Transactional
    public void saveMetaData(MetaData metaData) {
        for (ApricotTable t : metaData.getTables()) {
            tableRepository.save(t);
        }

        for (ApricotRelationship r : metaData.getRelationships()) {
            relationshipRepository.save(r);
        }

        for (ApricotDatabaseView v : metaData.getViews()) {
            viewRepository.save(v);
        }
    }
}
