package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.repository.CommunicationTypeRepository;

@Service
public class CommunicationTypeService {
	
	@Autowired
	private CommunicationTypeRepository communicationTypeRepository;
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	
	public List<CommunicationType> getAllCommunicationTypes(){
		return communicationTypeRepository.findAll();
	}//getAllCommunicationTypes
	
	public CommunicationType addCommunicationType(CommunicationType communicationType) throws Exception {
		/*if(communicationType == null || communicationType.getId() != null) return null;*/
		if(communicationType == null || communicationType.getCommunicationTypeCode() != null) return null;
		communicationType.setCommunicationTypeCode(generateCommTypeCode());
		return communicationTypeRepository.save(communicationType);
	}//createAndPersistCommunicationType

	private String generateCommTypeCode() {
		return codeGeneratorService.getCommunicationTypeNextCode();
	}//generateCommTypeCode

}
