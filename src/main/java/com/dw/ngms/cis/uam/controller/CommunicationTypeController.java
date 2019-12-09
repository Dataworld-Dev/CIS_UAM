package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.service.CommunicationTypeService;
import com.dw.ngms.cis.uam.service.UserService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class CommunicationTypeController extends MessageController {
	
	@Autowired
	private CommunicationTypeService communicationTypeService;
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/getCommunicationTypes")
	public ResponseEntity<?> getAllCommTypes(HttpServletRequest request) {
		try{
			List<CommunicationType> communicationTypeList = communicationTypeService.getAllCommunicationTypes();
			return (CollectionUtils.isEmpty(communicationTypeList)) ? generateEmptyResponse(request, "CommTypes not found")
					: ResponseEntity.status(HttpStatus.OK).body(communicationTypeList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}		
	}//getAllCommunicationTypes
	
	@PostMapping("/createCommType")
	public ResponseEntity<?> createCommType(HttpServletRequest request, @RequestBody @Valid CommunicationType communicationType){
		try{
			communicationType = communicationTypeService.addCommunicationType(communicationType);
			return (communicationType == null) ? generateEmptyResponse(request, "Failed to add communication type") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//createCommType
	
	@DeleteMapping(value = "/deleteCommunicationType")
	public ResponseEntity<?> deleteCommunicationType(HttpServletRequest request, @RequestParam String commTypeName) {
		
		if (StringUtils.isEmpty(commTypeName)) {
			return generateEmptyWithOKResponse(request, "Sector Name shold not be empty");
		}
		
		List<ExternalUser> extUser = this.userService.existCommunication(commTypeName);
		if(extUser!=null && extUser.size()>0) {
			return generateEmptyResponse(request, "Reference exists");
		}
		
		CommunicationType cmmunicationType = this.communicationTypeService.getAllCommunicationTypes().stream()
				.filter(x -> commTypeName.equals(x.getCommunicationTypeName()))
				.findAny().orElse(null);

		  System.out.println("Test ::" + cmmunicationType);
		  
		if (cmmunicationType == null) {
			return generateEmptyWithOKResponse(request, "Sector Name not found in the DB");
		}
		  
		this.communicationTypeService.deleteCommunicationType(cmmunicationType);
		 
		
		return ResponseEntity.status(HttpStatus.OK).body("Success");

	}

}
