package za.co.apricotdb.persistence.comparator;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The comparator of the tables.
 * 
 * @author Anton Nazarov
 * @since 06/10/2019
 */
@Component
public class TableComparator implements ApricotObjectComparator<ApricotTable, TableDifference> {

    @Override
    public TableDifference compare(ApricotTable source, ApricotTable target) {
        // TODO Auto-generated method stub
        return null;
    }

}
