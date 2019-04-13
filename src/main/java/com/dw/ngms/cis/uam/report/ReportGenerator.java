package com.dw.ngms.cis.uam.report;

import java.io.InputStream;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

@Slf4j
@Service
public class ReportGenerator {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private ReportExporter reportExporter;
	
	public boolean generateAndExportReport(String reportJrxml, String reportName, Map<String, Object> parameters) {
		JasperReport jasperReport = preparReport(reportJrxml);
		if(jasperReport == null) return false;
		JasperPrint jasperPrint = fillReport(jasperReport, parameters);
		if(jasperPrint == null) return false;
		reportExporter.exportToPdf(jasperPrint, reportName, "CIS");
		return true;
	}//generateReport
	
	private JasperReport preparReport(String reportJrxml) {
		if(reportJrxml == null) return null;
		try {
            InputStream reportStream = this.getClass().getResourceAsStream("/rptfiles/".concat(reportJrxml));
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JRSaver.saveObject(jasperReport, reportJrxml.replace(".jrxml", ".jasper"));
			return jasperReport;
		} catch (Exception ex) {
			log.error("Error while preparing the report {}", ex.getMessage());
		}
		return null;
	}//preparReport
	
	private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters) {
		if(jasperReport == null || CollectionUtils.isEmpty(parameters)) return null;
		
		try {
			return JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
		} catch (Exception ex) {
			log.error("Error while filling the report {}", ex.getMessage());
		}
		return null;
	}//fillReport
}
