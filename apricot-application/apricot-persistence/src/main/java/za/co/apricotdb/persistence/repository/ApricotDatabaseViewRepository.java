package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.apricotdb.persistence.entity.ApricotDatabaseView;

/**
 * The JPA repository to access the basic operations on ApricotDatabaseView.
 *
 * @author Anton Nazarov
 * @since 26/06/2021
 */
public interface ApricotDatabaseViewRepository extends JpaRepository<ApricotDatabaseView, Long> {

    ApricotDatabaseView findByDbViewName(String name);
}
