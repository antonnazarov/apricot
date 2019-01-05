package za.co.apricotdb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.apricotdb.persistence.entity.ApricotView;

public interface ApricotViewRepository extends JpaRepository<ApricotView, Long> {

}
