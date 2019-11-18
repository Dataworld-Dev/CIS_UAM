package com.dw.ngms.cis.im.service;

import java.util.Date;
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
	public EmailTemplate getEmailTemplateById(long id) {
		return this.emailRepository.getEmailTemplateById(id);
	}

	@Override
	public EmailTemplate updateEmailTemplate(EmailTemplate updatetemplate) {
		if (updatetemplate == null || updatetemplate.getEmailTemplateId() == null)
			return null;
		EmailTemplate existingTemplate = this.getEmailTemplateById(updatetemplate.getEmailTemplateId());
		
		if(existingTemplate== null) 
			return null;
		
		return emailRepository.save(updateValue(updatetemplate, existingTemplate));
	}
	
	public EmailTemplate saveEmailTemplate(EmailTemplate template) {
		return emailRepository.save(template);
	}

	
	private EmailTemplate updateValue(EmailTemplate updateInfo, EmailTemplate existingInfo) {

		if (updateInfo.getFrom() != null) {
			existingInfo.setFrom(updateInfo.getFrom());
		}

		if (updateInfo.getSubject() != null) {
			existingInfo.setSubject(updateInfo.getSubject());
		}

		if (updateInfo.getHeader() != null) {
			existingInfo.setHeader(updateInfo.getHeader());
		}

		if (updateInfo.getBody() != null) {
			existingInfo.setBody(updateInfo.getBody());
		}

		if (updateInfo.getFooter() != null) {
			existingInfo.setFooter(updateInfo.getFooter());
		}

		existingInfo.setModifiedDate(new Date());

		return existingInfo;
	}
}
