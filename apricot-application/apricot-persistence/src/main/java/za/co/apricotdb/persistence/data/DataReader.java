package za.co.apricotdb.persistence.data;

import java.util.List;
import javax.annotation.Resource;
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

    public MetaData readMetaData() {
        MetaData ret = new MetaData();
        List<ApricotTable> tables = tableRepository.findAll();
        List<ApricotRelationship> relationships = relationshipRepository.findAll();
        
        ret.setTables(tables);
        ret.setRelationships(relationships);
        
        return ret;
    }
}
