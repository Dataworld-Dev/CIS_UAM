package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.service.SectorService;
import com.dw.ngms.cis.uam.service.UserService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class SectorController extends MessageController {
	
    @Autowired
    private SectorService sectorService;
    
    @Autowired
    private UserService userService;

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
    }
    
    
	@DeleteMapping(value = "/deleteSector")
	public ResponseEntity<?> deleteSector(HttpServletRequest request, @RequestParam String sectorName) {
		
		if (StringUtils.isEmpty(sectorName)) {
			return generateEmptyWithOKResponse(request, "Sector Name shold not be empty");
		}
		
		List<ExternalUser> extUser = this.userService.existSector(sectorName);
		if(extUser!=null && extUser.size()>0) {
			return generateEmptyWithOKResponse(request, "Sector Name alredy used..");
		}
		
		Sector sector = this.sectorService.getAllSectors().stream()
				.filter(x -> sectorName.equals(x.getName()))
				.findAny().orElse(null);

		  System.out.println("Test ::" + sector);
		  
		if (sector == null) {
			return generateEmptyWithOKResponse(request, "Sector Name not found in the DB");
		}
		  
		this.sectorService.deleteSector(sector);
		 
		
		return ResponseEntity.status(HttpStatus.OK).body("Success");

	}
    

}
