package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotRelationship;

/**
 * Automatic JPA- repository for ApricotRelationship.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
public interface ApricotRelationshipRepository extends JpaRepository<ApricotRelationship, Long> {

}
