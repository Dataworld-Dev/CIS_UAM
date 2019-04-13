package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.CommunicationType;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Long>{

	
//	@Modifying
//	@Query("insert into CommunicationModeTypes c (c.communicationModeTypeName, c.description) values (:communicationModeTypeName, :description)")
//	public void insertCommunicationType(@Param("communicationModeTypeName") String communicationModeTypeName,@Param("description") String description);
}
