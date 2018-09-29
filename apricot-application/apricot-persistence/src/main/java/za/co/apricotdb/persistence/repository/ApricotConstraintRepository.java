package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * Automatic JPA- repository for ApricotConstraint.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotConstraintRepository extends JpaRepository<ApricotConstraint, Long> {

}
