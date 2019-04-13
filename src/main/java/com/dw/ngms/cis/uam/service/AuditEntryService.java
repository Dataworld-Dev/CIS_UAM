package com.dw.ngms.cis.uam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.AuditEntry;
import com.dw.ngms.cis.uam.repository.AuditEntryRepository;

@Service
public class AuditEntryService {

	@Autowired
	private AuditEntryRepository auditEntryRepository;
	
	public AuditEntry logAuditEntry(AuditEntry aditEntry) {
		return auditEntryRepository.save(aditEntry);
	}//logAuditEntry
}
