package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotProjectParameter;

public interface ApricotProjectParameterRepository extends JpaRepository<ApricotProjectParameter, Long> {

}
