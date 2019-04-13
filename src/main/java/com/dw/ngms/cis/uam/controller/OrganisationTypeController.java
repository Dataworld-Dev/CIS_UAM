package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.dw.ngms.cis.uam.entity.OrganisationType;
import com.dw.ngms.cis.uam.service.OraganisationTypeService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class OrganisationTypeController extends MessageController {

	@Autowired
	private OraganisationTypeService oraganisationTypeService;
	
	@GetMapping("/getOrgTypes")
	public ResponseEntity<?> getAllOrgTypes(HttpServletRequest request) {
		try{
			List<OrganisationType> organisationTypeList = this.oraganisationTypeService.getAllOrganisationTypes();
			return (CollectionUtils.isEmpty(organisationTypeList)) ? generateEmptyResponse(request, "OrgTypes not found")
					: ResponseEntity.status(HttpStatus.OK).body(organisationTypeList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//getAllOrganisationTypes
	
	@PostMapping("/createOrgType")
	public ResponseEntity<?> createOrganisationType(HttpServletRequest request, @RequestBody @Valid OrganisationType organisationType) {
		try{
			organisationType = oraganisationTypeService.addOrganisationType(organisationType);
			return (organisationType == null) ? generateEmptyResponse(request, "Failed to add organisation type") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}//createOrganisationType

}