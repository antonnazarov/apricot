package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

public interface ApricotSnapshotRepository extends JpaRepository<ApricotSnapshot, Long> {

}
