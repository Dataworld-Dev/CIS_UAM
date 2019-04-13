package com.dw.ngms.cis.uam.service;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeGeneratorService {

	@Autowired
	private EntityManager em; 
	
	private Long getNextValueOfSequence(String sequenceName) {
		Long idValue = null;
		try {
			String queryString = "SELECT "+sequenceName+ ".nextval FROM dual";
			Query query = em.createNativeQuery(queryString);
			BigDecimal value = (BigDecimal) query.getSingleResult();
			if(value == null) 
				throw new RuntimeException("Failed to generate next value for "+sequenceName);	
			else 
				idValue = value.longValue();
		}catch(Exception e) {
			log.error("Failed to generate next value"+e);
			throw new RuntimeException("Failed to generate next value for "+sequenceName, e);
		}
		return idValue;
	}//getNextValueOfSequence

	public String getSectorNextCode() {
		return "SEC"+ getNextValueOfSequence("SECTOR_ID_SEQ");
	}//getSectorNextCode
	
	public String getOrganisationTypeNextCode() {
		return "ORGTY"+ getNextValueOfSequence("ORGANISATION_TYPE_ID_SEQ");
	}

	public String getOrganisationNextCode() {
		return "ORG"+ getNextValueOfSequence("ORGANISATION_ID_SEQ");
	}
	
	public String getCommunicationTypeNextCode() {
		return "COMM"+ getNextValueOfSequence("COMMUNICATION_TYPE_ID_SEQ");
	}

	public String getPlsUserNextCode() {
		return "PLS"+ getNextValueOfSequence("PLS_USER_ID_SEQ");
	}
}
