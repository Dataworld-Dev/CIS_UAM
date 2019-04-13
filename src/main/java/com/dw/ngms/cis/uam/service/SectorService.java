package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.repository.SectorRepository;

@Service
public class SectorService {
	
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
	private CodeGeneratorService codeGeneratorService;
    
    public List<Sector> getAllSectors() {
        return this.sectorRepository.findAll();
    }

    public Sector addSector(Sector sector) {
    	if(sector == null || sector.getCode() != null) return null;
    	sector.setCode(getSectorCode());
        return this.sectorRepository.save(sector);
    }//addSector
    
    private String getSectorCode() {
    	return codeGeneratorService.getSectorNextCode();
	}//getSectorCode

}
