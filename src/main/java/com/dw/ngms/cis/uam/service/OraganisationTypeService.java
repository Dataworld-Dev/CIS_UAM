package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.OrganisationType;
import com.dw.ngms.cis.uam.repository.OrganisationTypeRepository;

@Service
public class OraganisationTypeService {
	
	@Autowired
	private OrganisationTypeRepository organisationTypeRepository;
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	
	public List<OrganisationType> getAllOrganisationTypes() {
		return organisationTypeRepository.findAll();
	}//getAllOrganisatioTypes



	public OrganisationType addOrganisationType(OrganisationType organisationType) {
		if(organisationType == null|| organisationType.getOrganizationTypeCode() != null) return null;
		organisationType.setOrganizationTypeCode(getOrganisationTypeCode());
		return organisationTypeRepository.save(organisationType);
	}//getAllOrganisatioTypes

	private String getOrganisationTypeCode() {
		return codeGeneratorService.getOrganisationTypeNextCode();
	}//getOrganisationTypeCode
}
