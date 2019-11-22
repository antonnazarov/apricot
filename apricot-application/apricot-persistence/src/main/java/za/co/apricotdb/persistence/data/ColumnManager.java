package za.co.apricotdb.persistence.data;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.repository.ApricotColumnRepository;

/**
 * The implementation of the business logic, related to the ApricotColumn.
 * 
 * @author Anton Nazarov
 * @since 07/03/2019
 */
@Component
public class ColumnManager {

    @Resource
    ApricotColumnRepository columnRepository;

    public void deleteColumn(ApricotColumn column) {
        columnRepository.delete(column);
    }

    public ApricotColumn findColumnById(long id) {
        Optional<ApricotColumn> c = columnRepository.findById(id);
        if (c.isEmpty()) {
            return null;
        }

        return c.get();
    }
}
