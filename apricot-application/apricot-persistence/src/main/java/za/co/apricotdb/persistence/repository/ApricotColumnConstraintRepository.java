package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ColumnConstraintId;

/**
 * Automatic JPA- repository for ApricotConstraint.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotColumnConstraintRepository extends JpaRepository<ApricotColumnConstraint, ColumnConstraintId> {

}
