package com.dw.ngms.cis.uam.report;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.dto.UserLogReportDto;
import com.dw.ngms.cis.uam.dto.UserSummaryReportDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ReportController extends MessageController {

	@Autowired
	private ReportGenerator reportGenerator;
	
	@PostMapping("/userSummaryReport")
	public ResponseEntity<?> generateUserSummaryReport(HttpServletRequest request, @RequestBody @Valid UserSummaryReportDto userSummaryReportDto) {
		String reportJrxml = "userSummary.jrxml";
		String reportName = "UserSummaryReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = getResourcePath();
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userSummaryReportDto.getFromDate());
			parameters.put("toDate", (userSummaryReportDto.getToDate() == null) ? new Date() : 
				userSummaryReportDto.getToDate());
			parameters.put("organisation", userSummaryReportDto.getOrganisation());
			parameters.put("section", userSummaryReportDto.getSection());
			parameters.put("sector", userSummaryReportDto.getSector());
			parameters.put("userType", userSummaryReportDto.getUserType());
			parameters.put("province", userSummaryReportDto.getProvince());		
			parameters.put("resourcePath", resourcePath);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "User summary report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "User summary report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateUserSummaryReport

	@PostMapping("/userLogReport")
	public ResponseEntity<?> generateUserLogReport(HttpServletRequest request, @RequestBody @Valid UserLogReportDto userLogReportDto) {
		String reportJrxml = "userLog.jrxml";
		String reportName = "UserLogReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = getResourcePath();
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userLogReportDto.getFromDate());
			parameters.put("toDate", (userLogReportDto.getToDate() == null) ? new Date() : 
				userLogReportDto.getToDate());
			parameters.put("userType", userLogReportDto.getUserType());
			parameters.put("resourcePath", resourcePath);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "User log report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "User log report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateUserLogReport
	
	@GetMapping("/quarterlyUpdatedUserReport")
	public ResponseEntity<?> generateQuarterlyUpdatedUserReport(HttpServletRequest request) {
		String reportJrxml = "quarterlyUpdatedUser.jrxml";
		String reportName = "QuarterlyUpdatedUserReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = getResourcePath();
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", DateUtils.parseDate("2019-03-01", new String[] {"yyyy-MM-dd"}));
			parameters.put("toDate", new Date());
			parameters.put("resourcePath", resourcePath);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Quarterly updated user report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Quarterly updated user report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateQuarterlyUpdatedUserReport
	
	@GetMapping("/quarterlyDeletedUserReport")
	public ResponseEntity<?> generateQuarterlyDeletedUserReport(HttpServletRequest request) {
		String reportJrxml = "quarterlyDeletedUser.jrxml";
		String reportName = "QuarterlyDeletedUserReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = getResourcePath();
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", DateUtils.parseDate("2019-03-01", new String[] {"yyyy-MM-dd"}));
			parameters.put("toDate", new Date());
			parameters.put("resourcePath", resourcePath);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Quarterly deleted user report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Quarterly deleted user report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateQuarterlyDeletedUserReport
	
	public void cleanupUserSummary(String fileJrxml, String reportName) {
		this.cleanupFileOnExists(fileJrxml.replace(".jrxml", ".jasper"));
		this.cleanupFileOnExists(reportName);
	}//cleanupUserSummary
	
	public void cleanupFileOnExists(String fileName) {
		try {
			log.info("cleanup of file: {}", fileName);
			File reportFile = new File(fileName);
			if (reportFile.exists()) {
				reportFile.delete();
				log.info("{} has been deleted", fileName);
			}
		}catch(Exception e) {
			log.error("{} cleanup filed {}", fileName, e.getMessage());
		}
	}//cleanupFileOnExists
	
}