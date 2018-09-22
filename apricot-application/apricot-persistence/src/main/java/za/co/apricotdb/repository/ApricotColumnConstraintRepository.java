package za.co.apricotdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.apricotdb.entity.ApricotColumnConstraint;
import za.co.apricotdb.entity.ColumnConstraintId;

/**
 * Automatic JPA- repository for ApricotConstraint.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotColumnConstraintRepository extends JpaRepository<ApricotColumnConstraint, ColumnConstraintId> {

}
