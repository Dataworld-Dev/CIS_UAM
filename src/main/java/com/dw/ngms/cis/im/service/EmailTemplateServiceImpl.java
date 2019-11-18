package com.dw.ngms.cis.im.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.repository.EmailTemplateRepositoty;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService{
	
	@Autowired
	EmailTemplateRepositoty emailRepository;

	@Override
	public List<EmailTemplate> getEmailTemplateInfo() {
		return this.emailRepository.getEmailTemplateDetails();
	}

	@Override
	public EmailTemplate getEmailTemplateById(String emailId) {
		return null;
	}

	@Override
	public EmailTemplate updateEmailTemplate(EmailTemplate template) {
		return null;
	}
	
	public EmailTemplate saveEmailTemplate(EmailTemplate template) {
		EmailTemplate temp = new EmailTemplate();
		temp.setCategory("Test Category");
		return emailRepository.save(template);
	}

}
