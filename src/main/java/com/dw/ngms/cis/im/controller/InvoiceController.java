package com.dw.ngms.cis.im.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.service.InvoiceStatusService;
import com.dw.ngms.cis.uam.entity.Task;

@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class InvoiceController extends MessageController {

	@Autowired
	InvoiceStatusService invoiceStatusService;

	@GetMapping("/getInvoiceAmountDetails")
	public ResponseEntity<?> getInvoiceAmountStatus(HttpServletRequest request,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false) String provienceCode, @RequestParam(required = false) String taskStatus)
			throws ParseException {

		Date fromdate = null;
		Date todate = null; 

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yy HH:mm:ss");

		if (fromDate != null && fromDate.trim().length() > 0) {
			fromdate = formatter.parse(fromDate + " 00:00:00");
		}

		if (toDate != null && toDate.trim().length() > 0) {
			todate = formatter.parse(toDate + " 23:59:59");
		}

		if (StringUtils.isEmpty(provienceCode)) {
			return generateFailureResponse(request, new Exception("Invalid request code passed"));
		}

		try {

			List<Task> taskList = invoiceStatusService.findByCriteria(provienceCode, fromdate, todate, taskStatus);
			return (CollectionUtils.isEmpty(taskList)) ? generateEmptyResponse(request, "Invoice history not found")
					: ResponseEntity.status(HttpStatus.OK).body(taskList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}

	}

}
