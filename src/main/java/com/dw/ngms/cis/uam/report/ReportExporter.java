package com.dw.ngms.cis.uam.report;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Slf4j
@Service
public class ReportExporter {

	public void exportToPdf(JasperPrint jasperPrint, String fileName, String author) {

        // print report to file
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileName));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(author);
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
        try {
            exporter.exportReport();
        } catch (JRException ex) {
        	log.error("Error while preparing the report {}", ex.getMessage());
        }
    }//exportToPdf

    public void exportToXlsx(JasperPrint jasperPrint, String resourcePath, String fileName, String sheetName) {
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(resourcePath.concat("/rptfiles/generated/".concat(fileName))));

        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
        reportConfig.setSheetNames(new String[] { sheetName });

        exporter.setConfiguration(reportConfig);

        try {
            exporter.exportReport();
        } catch (JRException ex) {
        	log.error("Error while preparing the report {}", ex.getMessage());
        }
    }//exportToXlsx

    public void exportToCsv(JasperPrint jasperPrint, String resourcePath, String fileName) {
        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(resourcePath.concat("/rptfiles/generated/".concat(fileName))));

        try {
            exporter.exportReport();
        } catch (JRException ex) {
        	log.error("Error while preparing the report {}", ex.getMessage());
        }
    }//exportToCsv

    public void exportToHtml(JasperPrint jasperPrint, String resourcePath, String fileName) {
        HtmlExporter exporter = new HtmlExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(resourcePath.concat("/rptfiles/generated/".concat(fileName))));

        try {
            exporter.exportReport();
        } catch (JRException ex) {
        	log.error("Error while preparing the report {}", ex.getMessage());
        }
    }//exportToHtml
}
