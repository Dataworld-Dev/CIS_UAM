package com.dw.ngms.cis.uam.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.service.EmailTemplateService;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.IssueLogService;
import com.google.gson.Gson;

/**
 * Created by swaroop on 2019/04/11.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class IssueLogController extends MessageController {

    @Autowired
    private IssueLogService issueLogService;

    @Autowired
    private EmailTemplateService email;


    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @RequestMapping(value = "/saveIssueLog", method = RequestMethod.POST)
    public ResponseEntity<?> saveIssueLog(HttpServletRequest request, @RequestBody @Valid IssueLog issueLog) {
        try {
            issueLog = issueLogService.saveIssueLog(issueLog);
            
			if (issueLog != null) {
				MailDTO mailDTO = new MailDTO();
				
				EmailTemplate template = this.email.getEmailTemplateById(12);
				  
				mailDTO.setBody1(template.getBody());
				mailDTO.setSubject(template.getSubject());
				mailDTO.setFooter(template.getFooter());
				mailDTO.setHeader(template.getHeader());

				sendMailCreateLog(issueLog, mailDTO); 
			}
            
            if (issueLog != null) {
                return ResponseEntity.status(HttpStatus.OK).body(issueLog);
            }

            return generateEmptyWithOKResponse(request, "Empty");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }

    }


    @GetMapping("/getAllIssueLogs")
    public ResponseEntity<?> getAllIssueLogs(HttpServletRequest request) {
        try {
            List<IssueLog> issueLogList = null;
            issueLogList = issueLogService.findAll();
            return (CollectionUtils.isEmpty(issueLogList)) ? ResponseEntity.status(HttpStatus.OK).body(issueLogList)
                    : ResponseEntity.status(HttpStatus.OK).body(issueLogList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllIssueLogs


    @GetMapping("/getIssueLogStatus")
    public ResponseEntity getIssueStatusById(HttpServletRequest request,@RequestParam Long issueID) {
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        Gson gson = new Gson();
        String json = null;
        try {
            String status = null;
            status = issueLogService.findIssueStatus(issueID);
            if(StringUtils.isEmpty(status)){
                return generateEmptyWithOKResponse(request,"No Status found");
            }
            userControllerResponse.setMessage(status);
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @GetMapping("/getMyIssues")
    public ResponseEntity getMyIssues(HttpServletRequest request,@RequestParam String email) {
        try {
            String status = null;
            List<IssueLog> issueLog = issueLogService.findIssueWithEmail(email);
            if(StringUtils.isEmpty(issueLog)){
                return generateEmptyWithOKResponse(request,"No Issue Log found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(issueLog);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @RequestMapping(value = "/issueLogUpdateStatus", method = RequestMethod.POST)
    public ResponseEntity updateStatus(HttpServletRequest request,@RequestParam Long issueID, @Valid @RequestBody IssueLog issueLogDetails) throws IOException {

        IssueLog issueLog = issueLogService.findById(issueID);
        if (!StringUtils.isEmpty(issueLog)) {
            issueLog.setIssueStatus(issueLogDetails.getIssueStatus());
            if(issueLogDetails.getResolvedComments() != null){
                issueLog.setResolvedComments(issueLogDetails.getResolvedComments());
            }
            IssueLog updateIssueLog = this.issueLogService.saveIssueLog(issueLog);

            MailDTO mailDTO = new MailDTO();
            EmailTemplate template = this.email.getEmailTemplateById(12);
            mailDTO.setBody1(template.getBody());
            mailDTO.setSubject(template.getSubject());
            mailDTO.setFooter(template.getFooter());
            mailDTO.setHeader(template.getHeader());

            MailDTO mailDTO1 = new MailDTO();
            EmailTemplate template1 = this.email.getEmailTemplateById(13);
            mailDTO1.setBody1(template1.getBody());
            mailDTO1.setSubject(template1.getSubject());
            mailDTO1.setFooter(template1.getFooter());
            mailDTO1.setHeader(template1.getHeader());

            sendMailToUser(updateIssueLog, mailDTO, mailDTO1);
            return ResponseEntity.status(HttpStatus.OK).body(updateIssueLog);
        }else{
            return generateEmptyWithOKResponse(request, "No Issue log found");
        }

    }


    private void sendMailToUser(@RequestBody @Valid IssueLog issueLog, MailDTO mailDTO,MailDTO mailDTO1 ) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        if (issueLog.getIssueStatus().equalsIgnoreCase("OPEN")) {
        	
    	   String body = mailDTO.getBody1();
           java.util.Map<String, String> m1 = new java.util.HashMap<String, String>();
           m1.put("data", body);
           String bodyText = MessageFormat.format(m1.get("data"),issueLog.getIssueId());

            model.put("body1", bodyText);
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");

        } else  if (issueLog.getIssueStatus().equalsIgnoreCase("CLOSED"))  {
            model.put("body1", mailDTO1.getBody1());
            model.put("body2", issueLog.getResolvedComments());
            model.put("body3", "");
            model.put("body4", "");

        }
        mailDTO.setMailSubject(mailDTO.getSubject());
        /*model.put("firstName", issueLog.getFullName());*/
        model.put("firstName", mailDTO.getHeader()+" " +issueLog.getFullName());
      /*  model.put("FOOTER", "CIS ADMIN");*/
        model.put("FOOTER", mailDTO.getFooter());
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(issueLog.getEmail());
        mailDTO.setModel(model);
        try {
            sendEmail(mailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void sendMailCreateLog(@RequestBody @Valid IssueLog issueLog, MailDTO mailDTO ) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
       
        	
    	   String body = mailDTO.getBody1();
           java.util.Map<String, String> m1 = new java.util.HashMap<String, String>();
           m1.put("data", body);
           String bodyText = MessageFormat.format(m1.get("data"),issueLog.getIssueId());

            model.put("body1", bodyText);
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");

        mailDTO.setMailSubject(mailDTO.getSubject());
        model.put("firstName", mailDTO.getHeader()+" " +issueLog.getFullName());
        model.put("FOOTER", mailDTO.getFooter());
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(issueLog.getEmail());
        mailDTO.setModel(model);
        try {
            sendEmail(mailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

