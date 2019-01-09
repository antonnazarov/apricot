package za.co.apricotdb.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotProject;

public interface ApricotProjectRepository extends JpaRepository<ApricotProject, Long> {
    
    List<ApricotProject> findByCurrent(boolean current);
}
