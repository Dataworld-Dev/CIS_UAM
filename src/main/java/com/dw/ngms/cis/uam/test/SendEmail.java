package com.dw.ngms.cis.uam.test;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.uam.config.SendBlueMailService;
import com.dw.ngms.cis.uam.configuration.MailConfiguration;
import com.dw.ngms.cis.uam.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swaroop on 2019/04/13.
 */
public class SendEmail {

    @Autowired
    private static MailConfiguration mailConfiguration;

    public static void main(String args[]){
        MailDTO mailDTO = new MailDTO();
        mailDTO.setId(1);
        mailDTO.setBody1("test");
        mailDTO.setBody2("test1");
        mailDTO.setBody3("test2");
        mailDTO.setBody4("test3");
        mailDTO.setHeader("hello");
        mailDTO.setFooter("Bye");
        mailDTO.setToAddress("swaroopeswara@gmail.com");
        String response = sendEmail(mailDTO);
    }
    protected static String sendEmail(MailDTO mailDTO) {
        SendBlueMailService http = new SendBlueMailService("https://api.sendinblue.com/v2.0", "MGzZOdpQ9wLBfnb3");
        Map<String, String> attr = new HashMap<>();
        attr.put("HEADER",mailDTO.getHeader());
        attr.put("SUBJECT",mailDTO.getSubject());
        attr.put("BODY1", mailDTO.getBody1());
        attr.put("BODY2", mailDTO.getBody2());
        attr.put("BODY3", mailDTO.getBody3());
        attr.put("BODY4", mailDTO.getBody4());
        attr.put("FOOTER", mailDTO.getFooter());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html; charset=iso-8859-1");
        Map<String, Object> data = new HashMap<>();
        data.put("id", ExceptionConstants.mailTemplateID);
        data.put("to", mailDTO.getToAddress());
        data.put("attr", attr);
        data.put("headers", headers);
        String mailResponse = http.send_transactional_template(data);

        return mailResponse;
    }//sendMail
}
