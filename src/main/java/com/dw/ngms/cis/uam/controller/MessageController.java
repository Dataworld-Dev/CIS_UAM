package com.dw.ngms.cis.uam.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dw.ngms.cis.uam.configuration.MailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.exception.ResponseBuilderAgent;
import com.dw.ngms.cis.exception.RestResponse;
import com.dw.ngms.cis.uam.config.SendBlueMailService;
import com.dw.ngms.cis.uam.dto.MailDTO;


@CrossOrigin(origins = "*")
public class MessageController implements ExceptionConstants {

	@Autowired
	private ResponseBuilderAgent responseBuilderAgent;

	@Autowired
	private MailConfiguration mailConfiguration;
	
	/**
	 * This is to generate failure response 
	 * 
	 * @param request
	 * @param exception
	 * @return ResponseEntity
	 */
	protected ResponseEntity<?> generateFailureResponse(HttpServletRequest request, Exception exception) {
		RestResponse errorResponse = responseBuilderAgent.createFailureResponse(exception, request.getRequestURI(), RESPONSE_ERROR_CODE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}//generateFailureResponse

	/**
	 * This is to generate empty response 
	 * 
	 * @param request
	 * @param responseMessage
	 * @return ResponseEntity
	 */
	protected ResponseEntity<?> generateEmptyResponse(HttpServletRequest request, String responseMessage) {
		RestResponse emptyResponse = responseBuilderAgent.createErrorResponse(request.getRequestURI(), responseMessage, RESPONSE_ERROR_CODE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyResponse);
	}//generateEmptyResponse


	protected ResponseEntity<?> generateEmptyWithOKResponse(HttpServletRequest request, String responseMessage) {
		RestResponse emptyResponse = responseBuilderAgent.createErrorResponse(request.getRequestURI(), responseMessage, RESPONSE_SUCCESS_CODE);
		return ResponseEntity.status(HttpStatus.OK).body(emptyResponse);
	}//generateEmptyResponse

	protected ResponseEntity<?> getResponseEntityStream(File reportFile, String reportName) throws Exception {		
		HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+reportName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(reportFile.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        String mimeType = URLConnection.guessContentTypeFromName(reportFile.getName());
        if(mimeType == null)
        	mimeType = "application/octet-stream";
        	
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(reportFile.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);		
	}//getResponseEntityStream
	
	protected String getResourcePath() throws IOException {
		URL url = this.getClass().getResource("/images/Logo_App.jpg");
		return (url != null && url.getPath() != null) ? 
				url.getPath().replace("/images/Logo_App.jpg", "") : null;
	}//getResourcePath

	protected String sendMail(MailDTO mailDTO) {
		SendBlueMailService http = new SendBlueMailService(mailConfiguration.getSendBlueMailLinkURL(), mailConfiguration.getSendBlueMailPassword());
		//SendBlueMailService http = new SendBlueMailService("https://api.sendinblue.com/v2.0", "MGzZOdpQ9wLBfnb3");
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
		System.out.println(http);
		String mailResponse = http.send_transactional_template(data);

		return mailResponse;
	}//sendMail


	protected String sendSMS(String cellNumber, String message) throws IOException {


		String myURI = "https://api.bulksms.com/v1/messages";
		String replyText = null;
		// change these values to match your own account
		String myUsername = "dataworldproject";
		String myPassword = "dataworld";
		System.out.println("cellNumber "+cellNumber);
		System.out.println("message "+message);
		// the details of the message we want to send
		/*String myData = "{to: \"+27849410645\", body: \"test\"}";
		                  // {to:+27849410645,body:Hello welcome message}
		                  {"to":"+27849410645","body":"Hello welcome message"}
		                     {to: "+27849410645", body: "test"}*/

		String myData = "{to:\""+cellNumber+"\"," +
				"body:\""+message+"\"}";
		/*String myData = "{to:"  + cellNumber +
				"," +
				"body:" + message +
				"}";*/

		System.out.println(myData);

		// if your message does not contain unicode, the "encoding" is not required:
		// String myData = "{to: \"1111111\", body: \"Hello Mr. Smith!\"}";

		// build the request based on the supplied settings
		URL url = new URL(myURI);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);

		// supply the credentials
		String authStr = myUsername + ":" + myPassword;
		String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
		request.setRequestProperty("Authorization", "Basic " + authEncoded);

		// we want to use HTTP POST
		request.setRequestMethod("POST");
		request.setRequestProperty( "Content-Type", "application/json");

		// write the data to the request
		OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
		out.write(myData);
		out.close();

		// try ... catch to handle errors nicely
		try {
			// make the call to the API
			InputStream response = request.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(response));
			
			while ((replyText = in.readLine()) != null) {
				System.out.println(replyText);
			}
			in.close();
		} catch (IOException ex) {
			System.out.println("An error occurred:" + ex.getMessage());
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
			// print the detail that comes with the error
			while ((replyText = in.readLine()) != null) {
				System.out.println(replyText);
			}
			in.close();
		}
		request.disconnect();
		
		return  replyText;
	}//sendSMS
	
}

