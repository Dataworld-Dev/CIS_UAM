package com.dw.ngms.cis.im.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.service.EmailTemplateService;

@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class EmailTemplateController extends MessageController {

	@Autowired
	EmailTemplateService email;

	@GetMapping("/getEmailInfo")
	public ResponseEntity<?> getEmailTemplateDetails(HttpServletRequest request) {

		List<EmailTemplate> emailTemplateList = this.email.getEmailTemplateInfo();
		return (CollectionUtils.isEmpty(emailTemplateList)) ? generateEmptyResponse(request, "No information found")
				: ResponseEntity.status(HttpStatus.OK).body(emailTemplateList);
	}

	@GetMapping("/getEmailInfoById")
	public ResponseEntity<?> getEmailTemplateById(HttpServletRequest request, @RequestParam("id") String id) {
		if(StringUtils.isEmpty(id)) {
			return generateEmptyResponse(request, "Email Template ID should not be empty");
		}
		
		Long templateId = Long.parseLong(id);
		EmailTemplate template = this.email.getEmailTemplateById(templateId);
		return (StringUtils.isEmpty(template)) ? generateEmptyResponse(request, "No information found")
				: ResponseEntity.status(HttpStatus.OK).body(template);
	}

	@PutMapping("/updateEmailInfoById")
	public ResponseEntity<?> updateEmailTemplateById(HttpServletRequest request,
			@RequestBody @Valid EmailTemplate emailTemplate) {
		
		if(StringUtils.isEmpty(emailTemplate)) {
			return generateEmptyResponse(request, "Email Template information should not be empty");
		}
		
		if(StringUtils.isEmpty(emailTemplate.getSubject())) {
			return generateEmptyResponse(request, "Email Subject should not be empty");
		}
		
		if(StringUtils.isEmpty(emailTemplate.getBody())) {
			return generateEmptyResponse(request, "Email Content should not be empty");
		}
		EmailTemplate template = this.email.updateEmailTemplate(emailTemplate);

		return (StringUtils.isEmpty(template)) ? generateEmptyResponse(request, "No information found")
				: ResponseEntity.status(HttpStatus.OK).body(template);

	}

}
