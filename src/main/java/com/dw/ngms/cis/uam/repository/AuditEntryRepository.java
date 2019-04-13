package com.dw.ngms.cis.uam.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.AuditEntry;

@Repository
public interface AuditEntryRepository extends JpaRepository<AuditEntry, UUID>{

}
