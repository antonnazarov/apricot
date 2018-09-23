package za.co.apricotdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.apricotdb.entity.ApricotTable;

/**
 * Automatic JPA- repository for ApricotTable.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotTableRepository extends JpaRepository<ApricotTable, Long> {
    ApricotTable findByName(String name);
}
