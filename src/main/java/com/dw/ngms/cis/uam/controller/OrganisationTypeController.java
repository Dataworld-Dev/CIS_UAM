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
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.OrganisationType;
import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.service.OraganisationTypeService;
import com.dw.ngms.cis.uam.service.UserService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class OrganisationTypeController extends MessageController {

	@Autowired
	private OraganisationTypeService oraganisationTypeService;
	
	 @Autowired
	    private UserService userService;

	
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
	
	
	@DeleteMapping(value = "/deleteOrgnization")
	public ResponseEntity<?> deleteSector(HttpServletRequest request, @RequestParam String orgName) {

		if (StringUtils.isEmpty(orgName)) {
			return generateEmptyWithOKResponse(request, "Sector Name shold not be empty");
		}

		List<ExternalUser> extUser = this.userService.existOrganization(orgName);
		if (extUser != null && extUser.size() > 0) {
			return generateEmptyWithOKResponse(request, "Sector Name alredy used..");
		}

		OrganisationType organisationType = this.oraganisationTypeService.getAllOrganisationTypes().stream()
				.filter(x -> orgName.equals(x.getOrganizationTypeName())).findAny().orElse(null);

		System.out.println("Test ::" + organisationType);

		if (organisationType == null) {
			return generateEmptyWithOKResponse(request, "Sector Name not found in the DB");
		}

		this.oraganisationTypeService.deleteOrganization(organisationType);

		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}
    

}