package com.dw.ngms.cis.im.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.im.entity.EmailTemplate;

@Repository
public interface EmailTemplateRepositoty extends JpaRepository<EmailTemplate, UUID>{
	/*
	 * @Query(value = "SELECT COSTCATEGORY_SEQ.nextval FROM dual", nativeQuery =
	 * true) Long getCategoryId();
	 */
	
	 @Query("select u from EmailTemplate u ")
	 List<EmailTemplate> getEmailTemplateDetails();


}
