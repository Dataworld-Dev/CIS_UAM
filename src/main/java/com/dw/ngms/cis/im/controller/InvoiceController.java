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
			@RequestParam(required = false) String provienceCode) throws ParseException {

		String taskStatus = "Closed";
		
		Date fromdate = null;//local variable
		Date todate =null; //local variable

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yy HH:mm:ss");
		
		
		if(fromDate!=null) {
		fromdate = formatter.parse(fromDate + " 00:00:00");
		System.out.println("test is" + formatter.format(fromdate));
		}
		
		
		if(toDate!=null) {
		todate = formatter.parse(toDate + " 23:59:59");
		System.out.println("todate is" + formatter.format(todate));
		}

		if (StringUtils.isEmpty(provienceCode)) {
			return generateFailureResponse(request, new Exception("Invalid request code passed"));
		}

		try {
		
			List<Task> taskList = invoiceStatusService.findByCriteria(provienceCode, fromdate, todate, taskStatus);

			System.out.println("****************************************");
			taskList.forEach(x -> System.out.println(x.toString()));

			System.out.println("***********End***************************");
			return (CollectionUtils.isEmpty(taskList)) ? generateEmptyResponse(request, "Invoice history not found")
					: ResponseEntity.status(HttpStatus.OK).body(taskList);
		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}

	}

}
