package com.dw.ngms.cis.im.service;

import java.util.List;

import com.dw.ngms.cis.im.entity.EmailTemplate;

public interface EmailTemplateService {

	public List<EmailTemplate> getEmailTemplateInfo() ;
	
	public EmailTemplate getEmailTemplateById(String emailId);
	
	public EmailTemplate updateEmailTemplate(EmailTemplate template);
	
	public EmailTemplate saveEmailTemplate(EmailTemplate template);
	
}
