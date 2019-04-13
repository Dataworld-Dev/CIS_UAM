package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.service.CommunicationTypeService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class CommunicationTypeController extends MessageController {
	
	@Autowired
	private CommunicationTypeService communicationTypeService;
	
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

}
