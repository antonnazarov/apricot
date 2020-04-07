package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;

/**
 * the default implementation of the JPA repository for ApricotApplicationParameter entity.
 * 
 * @author Anton Nazarov
 * @since 07/04/2020
 */
public interface ApricotApplicationParameterRepository extends JpaRepository<ApricotApplicationParameter, Long> {
    ApricotApplicationParameter findByName(String name);
}
