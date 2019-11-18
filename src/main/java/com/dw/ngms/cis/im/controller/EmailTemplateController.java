package com.dw.ngms.cis.im.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.service.EmailTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class EmailTemplateController extends MessageController{

	@Autowired
	EmailTemplateService email;
	
	@GetMapping("/getEmailInfo")
	public ResponseEntity<?> getEmailTemplateDetails(HttpServletRequest request) {
		 return (CollectionUtils.isEmpty(this.email.getEmailTemplateInfo())) ? generateEmptyResponse(request, "No information found") :
             ResponseEntity.status(HttpStatus.OK).body(this.email.getEmailTemplateInfo());
	}

	public ResponseEntity<?> getEmailTemplateById(HttpServletRequest request,
			@RequestParam("requestCode") String requestCode) {
		return null;
	}

	public ResponseEntity<?> updateEmailTemplateById(HttpServletRequest request,
			@RequestParam(required = false) String id, @RequestParam(required = false) String code) {
		return null;
	}

	@GetMapping("/getEmail")
	public ResponseEntity<?> save(HttpServletRequest request,@RequestParam("requestCode") String requestCode) {
		System.out.println("Save Email....");
		System.out.println("Request code ::"+ requestCode);
		email.saveEmailTemplate(new EmailTemplate());
		
		return null;
	}

}
