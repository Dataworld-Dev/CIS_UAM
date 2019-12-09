package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.im.entity.ProvinceInformation;

@Repository
public interface ProvinceInformationRespository extends JpaRepository<ProvinceInformation, Long> {

	@Query("SELECT p FROM ProvinceInformation p WHERE p.provCode = :provcode")
	ProvinceInformation getProvinceInformationByCode(@Param("provcode") String provcode);
	
}
