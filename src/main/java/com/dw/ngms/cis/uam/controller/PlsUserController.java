package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.PlsUser;
import com.dw.ngms.cis.uam.service.PlsUserService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class PlsUserController extends MessageController {

	@Autowired
    private PlsUserService plsUserService;

    @GetMapping("/getAllPlsUsers")
    public ResponseEntity<?> getAllPlsUsers(HttpServletRequest request) {
        try {
        	List<PlsUser> plsUserList = plsUserService.getAllPlsUsers();
        	return (CollectionUtils.isEmpty(plsUserList)) ? generateEmptyResponse(request, "PlsUser(s) not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(plsUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllPlsUsers
        
    @GetMapping("/validatePlsUser")
    public ResponseEntity<?> validatePlsUser(HttpServletRequest request, @RequestParam String plscode) {
        try {
        	PlsUser plsUser = plsUserService.findByCode(plscode);
        	return (plsUser == null) ? generateEmptyWithOKResponse(request, "PlsUser does not exist") 
            		: ResponseEntity.status(HttpStatus.OK).body(plsUser);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//validatePlsUser
    
    @PostMapping("/registerPlsUser")
    public ResponseEntity<?> registerPlsUser(HttpServletRequest request, @RequestBody @Valid PlsUser plsuser) {
    	try{
    		plsuser = plsUserService.addPlsUser(plsuser);
        	return (plsuser == null) ? generateEmptyResponse(request, "Failed to add pls user") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
    }//registerPlsUser
    
    @PostMapping("/updatePlsUser")
    public ResponseEntity<?> updatePlsUser(HttpServletRequest request, @RequestBody @Valid PlsUser plsuser) {
    	try{
    		plsuser = plsUserService.updatePlsUser(plsuser);
        	return (plsuser == null) ? generateEmptyResponse(request, "Failed to update pls user") :
				ResponseEntity.status(HttpStatus.OK).body("Successful");
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
    }//registerPlsUser
}
