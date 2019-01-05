package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotProject;

public interface ApricotProjectRepository extends JpaRepository<ApricotProject, Long> {

}
