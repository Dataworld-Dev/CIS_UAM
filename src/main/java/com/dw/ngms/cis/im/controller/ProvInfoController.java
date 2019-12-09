package com.dw.ngms.cis.im.controller;

import javax.annotation.Generated;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.ProvinceInformation;
import com.dw.ngms.cis.uam.service.ProivnceInformationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class ProvInfoController extends MessageController{

	
	@Autowired
	ProivnceInformationService service;
	
	
	
		 @GetMapping("/getProvInfo")
	public ResponseEntity<?> getProvinceInfo(HttpServletRequest request,  @RequestParam String provCode ){
		
		ProvinceInformation provInfo = service.getProvinceInformationByCode(provCode);
		
		 return (StringUtils.isEmpty(provInfo)) ?  generateEmptyResponse(request, "Target not found") :
             ResponseEntity.status(HttpStatus.OK).body(provInfo);
			 
	}
}
