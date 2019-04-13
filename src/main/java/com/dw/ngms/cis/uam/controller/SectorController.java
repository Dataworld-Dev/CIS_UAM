package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.service.SectorService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class SectorController extends MessageController {
	
    @Autowired
    private SectorService sectorService;

    @GetMapping("/getSectors")
    public ResponseEntity<?> getAllSectors(HttpServletRequest request) {
        try {
        	List<Sector> sectorList = this.sectorService.getAllSectors();
        	return (CollectionUtils.isEmpty(sectorList)) ? generateEmptyResponse(request, "Sectors not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(sectorList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllSectors
    
    @PostMapping("/createSector")
    public ResponseEntity<?> createSector(HttpServletRequest request, @RequestBody @Valid Sector sector) {
        try{
        	sector = sectorService.addSector(sector);
        	return (sector == null) ? generateEmptyResponse(request, "Failed to add sector") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
    }//createSector

}
