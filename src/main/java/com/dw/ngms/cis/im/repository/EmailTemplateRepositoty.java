package com.dw.ngms.cis.im.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.im.entity.EmailTemplate;




@Repository
public interface EmailTemplateRepositoty extends JpaRepository<EmailTemplate, UUID>{
	
	 @Query("select e from EmailTemplate e order by e.category")
	 List<EmailTemplate> getEmailTemplateDetails();
	 
	 
	 @Query("Select e from EmailTemplate e where e.emailTemplateId=:id")
	 EmailTemplate getEmailTemplateById(@Param("id") long id);
	 
}
