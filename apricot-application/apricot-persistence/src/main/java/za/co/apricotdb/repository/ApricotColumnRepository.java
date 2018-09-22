package za.co.apricotdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.apricotdb.entity.ApricotColumn;

/**
 * Automatic JPA- repository for ApricotColumn.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotColumnRepository extends JpaRepository<ApricotColumn, Long> {

}
