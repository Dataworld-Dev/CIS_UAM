package com.dw.ngms.cis.uam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

	@Query("SELECT s FROM Sector s WHERE s.name = :sectorName")
	public Sector getSectorByName(@Param("sectorName") String sectorName);

	Long deleteByCode(String code);
}

