package com.dw.ngms.cis.im.controller;


import static org.springframework.util.StringUtils.isEmpty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.dto.InvoiceDTO;
import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import com.dw.ngms.cis.im.service.EmailTemplateService;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.dto.RequestsJsonDTO;
import com.dw.ngms.cis.uam.dto.SendPopInfo;
import com.dw.ngms.cis.uam.entity.DocumentStore;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.TaskLifeCycle;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.DocumentStoreService;
import com.dw.ngms.cis.uam.service.InternalUserRoleService;
import com.dw.ngms.cis.uam.service.ProvinceService;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.service.UserService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.model.Target;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/19.
 */
@Slf4j
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class RequestController extends MessageController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestItemService requestItemService;
    @Autowired
    private StorageService testService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private InternalUserRoleService internalUserRoleService;
    @Autowired
    private UserService userService;

    @Autowired
    private DocumentStoreService documentStoreService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    private ApplicationPropertiesService appPropertiesService;

    @Autowired
    private ProvinceService provinceService;
    
    @Autowired
    private EmailTemplateService email;

    @Autowired
    private JavaMailSender mailSender;

    protected static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    @GetMapping("/getTaskTargetFlows")
    public ResponseEntity<?> getTaskTargetFlows(HttpServletRequest request,
                                                @RequestParam Long taskid, String internalrolecode) {
        if (taskid == null) {
            return generateFailureResponse(request, new Exception("Invalid task details passed"));
        }
        try {
            List<Target> targets = (StringUtils.isEmpty(internalrolecode)) ? taskService.getTaskTargetFlows(taskid) :
                    taskService.getTaskTargetFlows(taskid, internalrolecode);
            return (CollectionUtils.isEmpty(targets)) ? generateEmptyResponse(request, "Target(s) not found") :
                    ResponseEntity.status(HttpStatus.OK).body(targets);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskTargetFlows

    @GetMapping("/getTaskHistory")
    public ResponseEntity<?> getTaskHistory(HttpServletRequest request, @RequestParam @Valid String requestcode) {
        if (StringUtils.isEmpty(requestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code passed"));
        }
        try {
            List<TaskLifeCycle> taskLifeCycle = taskService.getTasksLifeCycleByTaskReferenceCode(requestcode);
            return (CollectionUtils.isEmpty(taskLifeCycle)) ? generateEmptyResponse(request, "Task history not found") :
                    ResponseEntity.status(HttpStatus.OK).body(taskLifeCycle);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskHistory


    @GetMapping("/getRequestDocuments")
    public ResponseEntity<?> getRequestDocumentsJson(HttpServletRequest request, @RequestParam String requestCode) {
        if (StringUtils.isEmpty(requestCode)) {
            return generateFailureResponse(request, new Exception("Invalid request code passed"));
        }
        try {
            Requests requestElements = this.requestService.getRequestsByRequestCode(requestCode);
            RequestsJsonDTO requestsJsonDTO = new RequestsJsonDTO();

            if(requestElements!= null){
                requestElements.getDocumentStoreCode();
                DocumentStore documentStore = documentStoreService.getDocumentByID(requestElements.getDocumentStoreCode());
                if(!StringUtils.isEmpty(documentStore)) {
                    requestsJsonDTO.setRequestDocuments(new String[]{documentStore.getDocumentPath()});
                }else{
                    requestsJsonDTO.setRequestDocuments(new String[]{});
                }


                if(!StringUtils.isEmpty(requestElements.getPopFilePath())) {
                    requestsJsonDTO.setPaymentConfirmation(new String[]{requestElements.getPopFilePath()});
                }else{
                    requestsJsonDTO.setPaymentConfirmation(new String[]{});
                }


                if(!StringUtils.isEmpty(requestElements.getDispatchDocs())) {
                    requestsJsonDTO.setDispatchDocs(new String[]{requestElements.getDispatchDocs()});
                }else{
                    requestsJsonDTO.setDispatchDocs(new String[]{});
                }


                if(!StringUtils.isEmpty(requestElements.getInvoiceFilePath())) {
                    requestsJsonDTO.setInvoiceDocs(new String[]{requestElements.getInvoiceFilePath()});
                }else{
                    requestsJsonDTO.setInvoiceDocs(new String[]{});
                }
                //requestsJsonDTO.setPaymentConfirmation(new String[]{requestElements.getPopFilePath()});
                //requestsJsonDTO.setDispatchDocs(new String[]{requestElements.getDispatchDocs()});
                //requestsJsonDTO.setInvoiceDocs(new String[]{requestElements.getInvoiceFilePath()});


            }


            return (StringUtils.isEmpty(requestElements)) ? generateEmptyResponse(request, "Request status not found") :
                    ResponseEntity.status(HttpStatus.OK).body(requestsJsonDTO);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestDocumentsJson


    @GetMapping("/getRequestStatus")
    public ResponseEntity<?> getTaskCurrentStatus(HttpServletRequest request, @RequestParam String requestcode) {
        if (StringUtils.isEmpty(requestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code passed"));
        }
        try {
            String status = getTaskCurrentStatus(requestcode);
            return (StringUtils.isEmpty(status)) ? generateEmptyResponse(request, "Request status not found") :
                    ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getTaskCurrentStatus

	private String getTaskCurrentStatus(String requestcode) {
		return taskService.getTaskCurrentStatus(requestcode);
	}//getTaskCurrentStatus

	private String getRequestStatus(String requestcode) {
		return "Closed".equalsIgnoreCase(getTaskCurrentStatus(requestcode)) ? "Closed" : "Open";
	}//getRequestStatus
	
    @PostMapping("/processUserState")
    public ResponseEntity<?> processUserState(HttpServletRequest request, @RequestBody @Valid ProcessAdditionalInfo additionalInfo) {
        if (additionalInfo == null || StringUtils.isEmpty(additionalInfo.getRequestCode()) || StringUtils.isEmpty(additionalInfo.getTaskId()) ||
                StringUtils.isEmpty(additionalInfo.getTargetSequenceId())) {
            return generateFailureResponse(request, new Exception("Invalid request/task details passed"));
        }
        try {
            this.updateRequestProvinceAndSectionCodes(additionalInfo);
            Target target = taskService.processUserState(additionalInfo);
            return (target == null) ? generateEmptyResponse(request, "Target not found") :
                    ResponseEntity.status(HttpStatus.OK).body(target.getDescription() + " succesfully completed");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//processUserState

    @GetMapping("/getRequestsOfUser")
    public ResponseEntity<?> getRequestsOfUser(HttpServletRequest request,
                                               @RequestParam(required = false) String provinceCode,
                                               @RequestParam(required = false) String userCode) {
        try {
            List<Requests> requestList = new ArrayList<>();
            if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim()) && !StringUtils.isEmpty(userCode)) {
                requestList = requestService.getRequestByUserCode(userCode);
                for (Requests req : requestList) {
                	req.setCurrentStatus(getRequestStatus(req.getRequestCode()));
                    List<RequestItems> requestItemsList = new ArrayList<>();
                    System.out.println("Request code is " + req.getRequestCode());
                    List<RequestItems> requestItems = this.requestItemService.getRequestsByRequestItemCode(req.getRequestCode());
                    if (!isEmpty(requestItems) && requestItems != null) {
                        for (RequestItems in : requestItems) {
                            System.out.println("Internal user roles" + in.getGazetteType1());
                            RequestItems requestItems1 = getRequestItems(in);
                            requestItemsList.add(requestItems1);
                            req.setRequestItems(requestItemsList);
                        }
                    }
                }
            } else if (!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(provinceCode.trim())) {
                requestList = requestService.getRequestByUserCodeProvinceCode(userCode, provinceCode);
                for (Requests req : requestList) {
                	req.setCurrentStatus(getRequestStatus(req.getRequestCode()));
                    List<RequestItems> requestItemsList = new ArrayList<>();
                    List<RequestItems> requestItems = this.requestItemService.getRequestsByRequestItemCode(req.getRequestCode());
                    if (!isEmpty(requestItems) && requestItems != null) {
                        for (RequestItems in : requestItems) {
                            System.out.println("Internal user roles" + in.getGazetteType1());
                            RequestItems requestItems1 = getRequestItems(in);
                            requestItemsList.add(requestItems1);                            
                            req.setRequestItems(requestItemsList);

                        }
                    }
                }
            }
            return (CollectionUtils.isEmpty(requestList)) ? ResponseEntity.status(HttpStatus.OK).body(requestList)
                    : ResponseEntity.status(HttpStatus.OK).body(requestList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsOfUser

    private RequestItems getRequestItems(RequestItems in) {
        RequestItems requestItems1 = new RequestItems();
        requestItems1.setCreatedDate(in.getCreatedDate());
        requestItems1.setQuantity(in.getQuantity());
        requestItems1.setRequestCode(in.getRequestCode());
        requestItems1.setRequestCost(in.getRequestCost());
        requestItems1.setRequestGazette1(in.getRequestGazette1());
        requestItems1.setRequestGazette2(in.getRequestGazette2());
        requestItems1.setRequestGazetteType(in.getRequestGazetteType());
        requestItems1.setRequestHours(in.getRequestHours());
        requestItems1.setSearchText(in.getSearchText());
        requestItems1.setRequestId(in.getRequestId());
        requestItems1.setGazetteType1(in.getGazetteType1());
        requestItems1.setGazetteType2(in.getGazetteType2());
        requestItems1.setResultJson(in.getResultJson());

        return requestItems1;
    }

    @GetMapping("/getRequestsPaidInfoByProvince")
    public ResponseEntity<?> getRequestsPaidInfoByProvince(HttpServletRequest request,
                                                           @RequestParam(required = false) String provinceCode,
                                                           @RequestParam(required = false) String period) {
        try {
            Double totalSum = 0.00;
            List<Requests> requestList = new ArrayList<>();
            if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim()) && period != null) {
                if (period.equalsIgnoreCase("Week")) {
                    requestList = requestService.getAllRequestsPaidInfoByProvinceWeek();
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Week is" + totalSum);
                } else if (period.equalsIgnoreCase("Month")) {
                    requestList = requestService.getAllRequestsPaidInfoByProvinceMonth();
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Month is" + totalSum);
                }

            } else if (StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim())) {
                requestList = requestService.getAllRequestsPaidInfoByProvince();
                Requests[] itemsArray = new Requests[requestList.size()];
                itemsArray = requestList.toArray(itemsArray);
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                }
                System.out.println("Total is" + totalSum);

            } else if (!StringUtils.isEmpty(provinceCode.trim()) && period != null) {
                if (period.equalsIgnoreCase("Week")) {
                    requestList = requestService.getRequestsPaidInfoByProvinceWeek(provinceCode);
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Week is" + totalSum);
                } else if (period.equalsIgnoreCase("Month")) {
                    requestList = requestService.getRequestsPaidInfoByProvinceMonth(provinceCode);
                    Requests[] itemsArray = new Requests[requestList.size()];
                    itemsArray = requestList.toArray(itemsArray);
                    for (int i = 0; i < itemsArray.length; i++) {
                        totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                    }
                    System.out.println("Total Month is" + totalSum);
                }
            } else if (!StringUtils.isEmpty(provinceCode.trim())) {
                requestList = requestService.getRequestsPaidInfoByProvince(provinceCode);
                Requests[] itemsArray = new Requests[requestList.size()];
                itemsArray = requestList.toArray(itemsArray);
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getTotalAmount());
                }
                System.out.println("Total value  is" + totalSum);

            }
            requestList.forEach(r -> {r.setCurrentStatus(getRequestStatus(r.getRequestCode()));});
            
            return (CollectionUtils.isEmpty(requestList)) ? ResponseEntity.status(HttpStatus.OK).body(totalSum)
                    : ResponseEntity.status(HttpStatus.OK).body(totalSum);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsPaidInfoByProvince

    @GetMapping("/getRequestByRequestCode")
    public ResponseEntity<?> getRequestByRequestCode(HttpServletRequest request,
                                                     @RequestParam String requestCode) {
        try {

            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            List<RequestItems> requestItemsList = this.requestItemService.getRequestsByRequestCode(requestCode);
            if (!StringUtils.isEmpty(requestItemsList)) {
                requests.setRequestItems(requestItemsList);
            }
            if(requests != null)
            	requests.setCurrentStatus(getRequestStatus(requests.getRequestCode()));
            
            return (requests != null) ? ResponseEntity.status(HttpStatus.OK).body(requests)
                    : generateEmptyResponse(request, "Request(s) not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//RequestController

    @GetMapping("/updateRequestOnLapse")
    public ResponseEntity<?> updateRequestOnLapse(HttpServletRequest request,
                                                  @RequestParam @Valid String reuestcode, @RequestParam @Valid Integer lapsetime,
                                                  @RequestParam @Valid boolean islapsed) {
        log.info("Start processing updateRequestOnLapse");
        if (StringUtils.isEmpty(reuestcode)) {
            return generateFailureResponse(request, new Exception("Invalid request code"));
        }
        try {
            boolean isProcessed = requestService.updateRequestOnLapse(reuestcode, lapsetime, islapsed);
            return (!isProcessed) ? generateFailureResponse(request, new Exception("Failed to update request")) :
                    ResponseEntity.status(HttpStatus.OK).body("Request succesfully processed");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateRequestOnLapse

    @PostMapping("/uploadPaymentConfirmation")
    public ResponseEntity<?> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                              @RequestParam("taskId") Long taskId,
                                              @RequestParam("requestCode") String requestCode,
                                              @RequestParam("provinceCode") String provinceCode,
                                              @RequestParam("sectionCode") String sectionCode,
                                              @RequestParam("targetSequenceId") String targetSequenceId,
                                              @RequestParam("userCode") String userCode,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("url") String url
    ) {
        String message = "";
        List<String> files = new ArrayList<String>();
        try {
            if (file.isEmpty()) {
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            } else if (requestCode != null && !isEmpty(requestCode)) {
                Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
                if (requests != null) {
                    String fileName = testService.store(file);
                    files.add(file.getOriginalFilename());
                    requests.setPopFilePath(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                    message = "You successfully uploaded " + requests.getPopFilePath() + "!";
                    Requests requests1 = requestService.saveRequest(requests);
                    if (requests1 != null) {
                        requests1.setPaymentStatus("PAID");
                    }
                    requestService.saveRequest(requests1);
                    ProcessAdditionalInfo processAdditionalInfo = new ProcessAdditionalInfo();
                    processAdditionalInfo.setTaskId(taskId);
                    processAdditionalInfo.setRequestCode(requestCode);
                    processAdditionalInfo.setProvinceCode(provinceCode);
                    processAdditionalInfo.setSectionCode(sectionCode);
                    processAdditionalInfo.setTargetSequenceId(targetSequenceId);
                    processAdditionalInfo.setUserCode(userCode);
                    processAdditionalInfo.setUserName(userName);
                    processAdditionalInfo.setUrl(url);

                    //process workflow
                    processUserState(request, processAdditionalInfo);

                    //send notification
                    uploadPaymentConfirmationNotification(requests1, false);

                    return ResponseEntity.status(HttpStatus.OK).body(message);

                } else {
                    return generateEmptyResponse(request, "No Internal User Roles  found");
                }
            }
            return generateEmptyResponse(request, "No Internal User Roles  found");
        } catch (Exception exception) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return generateFailureResponse(request, exception);
        }
    }//uploadPaymentConfirmation

    @PostMapping("/createRequest")
    public ResponseEntity<?> createRequest(HttpServletRequest request, @RequestBody @Valid Requests requests) {
        try {
            Long requestId = this.requestService.getRequestId();
            log.info("requestTypeId is " + requestId);
            requests.setRequestId(requestId);
            requests.setRequestCode("REQ" + Long.toString(requests.getRequestId()));
            //requests.setReferenceNumber("REQ" + Long.toString(requests.getRequestId()));
            String provinceShortName = this.provinceService.getProvinceShortName(requests.getProvinceCode());
            requests.setReferenceNumber("IN" + Long.toString(requests.getRequestId()) + provinceShortName);
            requests.setPaymentStatus("PENDING");
            String processId = "infoRequest";
            List<RequestItems> req = new ArrayList<>();
            if (requests.getRequestItems() != null) {
                for (RequestItems requestItems : requests.getRequestItems()) {
                    Long requestItemCode = this.requestItemService.getRequestItemId();
                    requestItems.setRequestItemId(requestItemCode);
                    requestItems.setRequestId(requests.getRequestId());
                    requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItems.getRequestItemId()));
                    requestItems.setRequestCode(requests.getRequestCode());
                    req.add(requestItems);
                }
            }
            requests.setRequestItems(req);
            requests.setRequestTypeName(requests.getRequestTypeName());
            Requests requestToSave = this.requestService.saveRequest(requests);
            MailDTO mailDTO = new MailDTO();
            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
            emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMailToCreateRequestUser(requests, mailDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.
            updateSavedRequests(requests, requestToSave);
            taskService.startProcess(processId, requests);

            return ResponseEntity.status(HttpStatus.OK).body(requests);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createRequest

    @PostMapping("/cancelRequest")
    public ResponseEntity<?> cancelRequest(HttpServletRequest request, @RequestBody @Valid Requests requests) {
    	if(requests == null || StringUtils.isEmpty(requests.getRequestCode()) || 
    			StringUtils.isEmpty(requests.getDescription())) {
    		return generateEmptyResponse(request, "No Request found with requestCode and description");
    	}
        try {
        	String processId = "infoRequest";
        	String description = requests.getDescription();
        	requests = requestService.getRequestsByRequestCode(requests.getRequestCode());
        	requests.setDescription(description);
        	requests.setModifiedDate(new Date());
        	requests.setModifiedUserCode(requests.getUserCode());
        	Requests requestToSave = this.requestService.saveRequest(requests);
        	        	
            taskService.cancelProcess(processId, requests);

            this.cancellationNotification(requestToSave);
            
            return ResponseEntity.status(HttpStatus.OK).body(requests);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//cancelRequest
    
    private void updateSavedRequests(Requests requests, Requests requestToSave) {
        requestToSave.setAssigneeInfoManager(requests.getAssigneeInfoManager());
        requestToSave.setAssigneeInfoOfficer(requests.getAssigneeInfoOfficer());
        requestToSave.setCapturerCode(requests.getCapturerCode());
        requestToSave.setCapturerName(requests.getCapturerName());
        requestToSave.setCapturerFullName(requests.getCapturerFullName());
        requestToSave.setInternalCapturer(requests.isInternalCapturer());
    }//updateSavedRequests

    private void updateRequestProvinceAndSectionCodes(ProcessAdditionalInfo additionalInfo) {
        if (additionalInfo != null && additionalInfo.getRequestCode() != null) {
            Requests request = requestService.getRequestsByRequestCode(additionalInfo.getRequestCode());
            if (request != null) {
                if (!StringUtils.isEmpty(additionalInfo.getProvinceCode()))
                    request.setProvinceCode(additionalInfo.getProvinceCode());
                if (!StringUtils.isEmpty(additionalInfo.getSectionCode()))
                    request.setSectionCode(additionalInfo.getSectionCode());
            }
        }
    }//updateRequestProvinceAndSectionCodes

    @SuppressWarnings("unused")
    @PostMapping("/generateInvoice")
    public ResponseEntity<?> saveAndSendInvoiceMail(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam("requestCode") String requestCode,
                                                    @RequestBody @Valid InvoiceDTO invoiceDTO) throws IOException {
        try {
            Requests req = requestService.getRequestsByRequestCode(requestCode);
            if (req != null) {
                ClassPathResource template = new ClassPathResource(applicationPropertiesConfiguration.getPdfTemplate());
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                String filename = applicationPropertiesConfiguration.getFileNamePDF();
                filename = timeStamp + "_" + filename;
                File file = new File(applicationPropertiesConfiguration.getInvoiceDirectory() + filename);
                if (template.exists()) {
                    try {
                        PdfReader reader = new PdfReader(template.getFilename());
                        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
                        AcroFields form = stamper.getAcroFields();
                        System.out.println("");
                        form.setField("FULL_NAME", invoiceDTO.getFullName());
                        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy");
                        form.setField("DATE", fmt.format(new Date()));
                        form.setField("ORGANIZATION", invoiceDTO.getOrgnization());
                        form.setField("REQUEST_NUMBER", invoiceDTO.getRequestNumber());
                        form.setField("REQUEST_TYPE", invoiceDTO.getRequestType());
                        form.setField("TELEPHONE", invoiceDTO.getTelephone());
                        form.setField("POSTAL_ADDRESS", invoiceDTO.getPostalAddress());
                        form.setField("EMAIL", invoiceDTO.getEmail());
                        form.setField("MOBILE", invoiceDTO.getMoible());
                        form.setField("DEPOSIT", invoiceDTO.getAmount());
                        stamper.setFormFlattening(true);
                        stamper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                    if (mimeType == null) {
                        mimeType = "application/octet-stream";
                    }
                    response.setContentType(mimeType);
                    response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                    response.setContentLength((int) file.length());
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    req.setInvoiceFilePath(applicationPropertiesConfiguration.getInvoiceDirectory() + "/" + filename);
                    Requests updatedRequest = requestService.saveRequest(req);
                    return ResponseEntity.status(HttpStatus.OK).body("Generated Invoice Successfully");
                }
            } else {
                return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
    }

    @SuppressWarnings("unused")
    @PostMapping("/sendEmailWithInvoice")
    public ResponseEntity<?> sendEmailWithInvoice(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("requestCode") String requestCode,
                                                  @RequestBody @Valid InvoiceDTO invoiceDTO) throws Exception {
        try {
            Requests req = requestService.getRequestsByRequestCode(requestCode);
            if (req != null) {
                String fileName = req.getInvoiceFilePath();
                File fileLater = new File(req.getInvoiceFilePath());
                MailDTO mailDTO = new MailDTO();
               
                EmailTemplate template = this.email.getEmailTemplateById(1);
                mailDTO.setBody1(template.getBody());
                mailDTO.setSubject(template.getSubject());
                mailDTO.setFooter(template.getFooter());
                mailDTO.setHeader(template.getHeader());
              
                
                sendMailInvoiceUser(req, mailDTO, fileName, fileLater);
                
                ProcessAdditionalInfo processAdditionalInfo = getProcessAdditionalInfo(invoiceDTO);
                processUserState(request, processAdditionalInfo);
                return ResponseEntity.status(HttpStatus.OK).body("Generated Invoice Successfully");
            } else {
                return generateEmptyResponse(request, "No Request found with requestCode " + requestCode);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }

    private void sendMailInvoiceUser(Requests requests, MailDTO mailDTO, String fileName, File fileLater) throws Exception {
        System.out.println("File Name test is " + fileName);
        if (!fileName.isEmpty()) {
            int index = fileName.lastIndexOf("/");
            fileName = fileName.substring(index + 1);
            System.out.println("File Name before is " + fileName);
            fileName = fileName.replace("_NLC_MonitoringForm", "");
            fileName = "Invoice_" + fileName;
            System.out.println("File Name before testis " + fileName);

        }
        System.out.println("File Name after is " + fileName);
        Map<String, Object> model = new HashMap<String, Object>();
        String firstName = null;
        String lastName = null;
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
        model.put("firstName", mailDTO.getHeader()+"" +firstName + " " + lastName);
       // model.put("body1", "Invoice Generated Successfully");
        model.put("body1", mailDTO.getBody1());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");

        //mailDTO.setMailSubject("DRDLR:Invoice");
        mailDTO.setMailSubject(mailDTO.getSubject());
      //  model.put("FOOTER", "CIS ADMIN");
        model.put("FOOTER", mailDTO.getFooter());
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO, fileName, fileLater);

    }

    private void sendMailWithFTPPAth(Requests requests, MailDTO mailDTO, String ftpFilePath) throws Exception {
        String firstName = null;
        String lastName = null;
        Map<String, Object> model = new HashMap<String, Object>();
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
        model.put("firstName", firstName + " " + lastName);
        model.put("body1", "FTP paths send successfully");
        model.put("body2", ftpFilePath);
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailSubject("DRDLR:Delivery");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO);

    }


    private void sendMailWithAttachment(Requests requests, MailDTO mailDTO, String fileName, File file) throws Exception {
        String firstName = null;
        String lastName = null;
        Map<String, Object> model = new HashMap<String, Object>();
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
        model.put("firstName", firstName + " " + lastName);
        model.put("body1", "Request Attached");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailSubject("DRDLR:Delivery");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);

        sendEmail(mailDTO, fileName, file);
        //sendEmail(mailDTO);

    }


    private void sendMaiForCollectionReady(Requests requests, MailDTO mailDTO) throws Exception {
        String firstName = null;
        String lastName = null;
        Map<String, Object> model = new HashMap<String, Object>();
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
        model.put("firstName", firstName + " " + lastName);
        model.put("body1", "Your request is ready for collection");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailSubject("DRDLR:Delivery");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO);

    }

    private ProcessAdditionalInfo getProcessAdditionalInfo(@RequestBody @Valid InvoiceDTO invoiceDTO) {
        ProcessAdditionalInfo processAdditionalInfo = new ProcessAdditionalInfo();
        if (invoiceDTO.getProcessAdditionalInfo() != null) {
            processAdditionalInfo.setTaskId(invoiceDTO.getProcessAdditionalInfo().getTaskId());
            processAdditionalInfo.setRequestCode(invoiceDTO.getProcessAdditionalInfo().getRequestCode());
            processAdditionalInfo.setProvinceCode(invoiceDTO.getProcessAdditionalInfo().getProvinceCode());
            processAdditionalInfo.setSectionCode(invoiceDTO.getProcessAdditionalInfo().getSectionCode());
            processAdditionalInfo.setTargetSequenceId(invoiceDTO.getProcessAdditionalInfo().getTargetSequenceId());
            processAdditionalInfo.setUserCode(invoiceDTO.getProcessAdditionalInfo().getUserCode());
            processAdditionalInfo.setUserName(invoiceDTO.getProcessAdditionalInfo().getUserName());
            invoiceDTO.setProcessAdditionalInfo(processAdditionalInfo);
        }
        return processAdditionalInfo;
    }

    @RequestMapping(value = "/downloadInvoice", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> downloadInvoice(HttpServletRequest request,
                                                             @RequestBody @Valid Requests requests) throws IOException {

        Requests ir = requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("Internal User Roles one " + ir.getInvoiceFilePath());
        int index = ir.getInvoiceFilePath().lastIndexOf("/");
        String fileName = ir.getInvoiceFilePath().substring(index + 1);
        System.out.println("File Name is " + fileName);
        File file = new File(ir.getInvoiceFilePath());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(value = "/downloadPop", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> downloadPop(HttpServletRequest request,
                                                         @RequestBody @Valid Requests requests) throws IOException {

        Requests ir = requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("Internal User Roles one " + ir.getPopFilePath());
        int index = ir.getPopFilePath().lastIndexOf("/");
        String fileName = ir.getPopFilePath().substring(index + 1);
        System.out.println("File Name is " + fileName);
        File file = new File(ir.getPopFilePath());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }//downloadPop

    @PostMapping("/uploadDispatchDocument")
    public ResponseEntity<?> uploadDispatchDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                    @RequestParam("requestCode") String requestCode
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (requests != null && !isEmpty(requests)) {
                if (requests.getDispatchDocs() != null) {
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = requests.getDispatchDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    for (String str1 : filePath.getFiles()) {
                        filesExist.add(str1);
                    }
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                } else {
                    List<String> filesExist = new ArrayList<>();
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadDispatchDocument

    @PostMapping("/uploadExternalUserRequestDocument")
    public ResponseEntity<?> uploadExternalUserDispatchDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                                @RequestParam("requestCode") String requestCode
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);

            if (requests != null && !isEmpty(requests)) {
                if (requests.getExternalUserDispatchDocs() != null) {
                    System.out.println("Get External User Dispatch DOcs" + requests.getExternalUserDispatchDocs());
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = requests.getExternalUserDispatchDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    for (String str1 : filePath.getFiles()) {
                        filesExist.add(str1);
                    }
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setExternalUserDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                } else {
                    List<String> filesExist = new ArrayList<>();
                    for (MultipartFile f : multipleFiles) {
                        List<String> files = new ArrayList<String>();
                        String fileName = testService.store(f);
                        files.add(f.getOriginalFilename());
                        filesExist.add(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        requests.setExternalUserDispatchDocs(json);
                        this.requestService.saveRequest(requests);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadExternalUserRequestDocument

    // ftp server local tech live
   /* private boolean ftpLogin(FTPClient ftpClient) throws IOException {
        String server = "160.119.101.57";
        int port = 21;
        String user = "Administrator";
        String pass = "dataworld@1";

        ftpClient.connect(server, port);
        showServerReply(ftpClient);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("Operation failed. Server reply code: " + replyCode);
            return false;
        }
        boolean success = ftpClient.login(user, pass);
        showServerReply(ftpClient);
        if (!success) {
            System.out.println("Could not login to the server");
            return false;
        } else {
            System.out.println("LOGGED IN SERVER");
            return true;
        }

    }*/









    @PostMapping("/dispatchDocumentSendMail")
    public ResponseEntity<?> dispatchDocumentSendMail(HttpServletRequest request,
                                                      @RequestBody Requests requestsParam
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        FTPClient ftpClient = new FTPClient();
        try {
            Requests requests = this.requestService.getRequestsByRequestCode(requestsParam.getRequestCode());
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            if (requests.getFormatType().equalsIgnoreCase("Electronic(Email)")) {
                if (requests != null && !isEmpty(requests)) {
                    if (requests.getDispatchDocs() != null) {
                        String pathFromDB = requests.getDispatchDocs();
                        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                        System.out.println("filePath is " + filePath.getFiles().toString());
                        List<String> files = new ArrayList<String>();
                        for (String str1 : filePath.getFiles()) {
                            System.out.println(str1);
                            files.add(str1);
                            ftpZipFiles(files, timeStamp);
                        }
                        String zipFilename = "FTPFilesDownload" + "_" + timeStamp + ".zip";
                        File firstLocalFile = new File(applicationPropertiesConfiguration.getUploadDirectoryPathFTP() + zipFilename);
                        MailDTO mailDTO = new MailDTO();

                        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
                        emailExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sendMailWithAttachment(requests, mailDTO,zipFilename, firstLocalFile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.OK).body("Dispatch documents are not found");
                }
            }
                else if (requests.getFormatType().equalsIgnoreCase("Electronic(FTP)")) {
                    if (requests != null && !isEmpty(requests)) {
                        if (requests.getDispatchDocs() != null) {
                            String pathFromDB = requests.getDispatchDocs();
                            FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                            System.out.println("filePath is " + filePath.getFiles().toString());
                            List<String> files = new ArrayList<String>();
                            for (String str1 : filePath.getFiles()) {
                                System.out.println(str1);
                                files.add(str1);
                                ftpZipFiles(files, timeStamp);
                            }


                            String zipFilename = "FTPFilesDownload" + "_" + timeStamp + ".zip";

                            //String zipFilename = "FTPFilesDownload.zip";

                            boolean loginExists = ftpLogin(ftpClient);
                            try {
                                if (loginExists) {
                                    ftpClient.enterLocalPassiveMode();
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.changeWorkingDirectory("/ftpFileDownload/");
                                    File firstLocalFile = new File(applicationPropertiesConfiguration.getUploadDirectoryPathFTP() + zipFilename);
                                    System.out.println("Local File is: " + applicationPropertiesConfiguration.getUploadDirectoryPathFTP());
                                    System.out.println("zipFilename is: " + zipFilename);
                                    String firstRemoteFile = zipFilename;
                                    InputStream inputStream = new FileInputStream(firstLocalFile);
                                    System.out.println("Start uploading first file");
                                    boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                                    if (done) {
                                        System.out.println("Done");
                                    } else {
                                        System.out.println("Not Done");
                                    }
                                    inputStream.close();
                                }
                            } catch (Exception e) {
                                System.out.println("Exception is" + e.getMessage());
                                ;
                                e.printStackTrace();
                            }
                            ftpClient.logout();
                            System.out.println("Start uploading second");
                            String path = appPropertiesService.getProperty("FTP_UPLOAD_PATH").getKeyValue();
                            String server = "ftp://" + appPropertiesService.getProperty("FTP_SERVER").getKeyValue();

                            String ftpFilePath = server + path + zipFilename;
                            System.out.println("File Path is: " + ftpFilePath);

                            System.out.println("FTP Server is: " + appPropertiesService.getProperty("FTP_SERVER").getKeyValue());
                            System.out.println("FTP userName: " + appPropertiesService.getProperty("FTP_USERNAME").getKeyValue());
                            System.out.println("FTP  Password is: " + appPropertiesService.getProperty("FTP_PASSWORD").getKeyValue());
                            MailDTO mailDTO = new MailDTO();

                            // inside your getSalesUserData() method
                            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
                            emailExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        sendMailWithFTPPAth(requests, mailDTO, ftpFilePath);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.
                        } else {
                            return ResponseEntity.status(HttpStatus.OK).body("Dispatch documents are not found");
                        }
                    }
                } else {
                    System.out.println("This is manual delivery for collection");
                    MailDTO mailDTO = new MailDTO();
                    ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
                    emailExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendMaiForCollectionReady(requests, mailDTO);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.

                }

            }catch(Exception exception){
                return generateFailureResponse(request, exception);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Sent email Sucessfully");
        }//uploadFTPDispatchDocumentSendMail

    private boolean ftpLogin(FTPClient ftpClient) throws IOException {
        String server = appPropertiesService.getProperty("FTP_SERVER").getKeyValue();
        int port = Integer.valueOf(appPropertiesService.getProperty("FTP_PORT").getKeyValue());
        String user = appPropertiesService.getProperty("FTP_USERNAME").getKeyValue();
        String pass = appPropertiesService.getProperty("FTP_PASSWORD").getKeyValue();
        ftpClient.connect(server, port);
        showServerReply(ftpClient);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            System.out.println("Operation failed. Server reply code: " + replyCode);
            return false;
        }
        boolean success = ftpClient.login(user, pass);
        showServerReply(ftpClient);
        if (!success) {
            System.out.println("Could not login to the server");
            return false;
        } else {
            System.out.println("LOGGED IN SERVER");
            return true;
        }

    }

    @RequestMapping(value = "/deleteDispatchDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteDispatchDocument(HttpServletRequest request,
                                                    @RequestBody @Valid Requests requestsBody,
                                                    @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Requests requests = this.requestService.getRequestsByRequestCode(requestsBody.getRequestCode());
            if (isEmpty(requests)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getDispatchDocs();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
//                    System.out.println("Str1 is "+str1);
//                    if (te.contains(documentPath)) {
//                        Path fileToDeletePath = Paths.get(str1);
//                        Files.delete(fileToDeletePath);
//                        System.out.println("String values are "+str1);
//                        te.remove(documentPath);
//                    }
//                    System.out.println("size is" +te.size());
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);
                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of file is " + fileDeleted);
                        requests.setDispatchDocs("");
                        this.requestService.saveRequest(requests);
                        return ResponseEntity.status(HttpStatus.OK).body("Request Document Deleted Successfully");
                    }

                }


                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                requests.setDispatchDocs(json);
                this.requestService.saveRequest(requests);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Request Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Request Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteDispatchDocument

    @RequestMapping(value = "/deleteExternalUserRequestDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteExternalUserDispatchDocument(HttpServletRequest request,
                                                                @RequestBody @Valid Requests requestsBody,
                                                                @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Requests requests = this.requestService.getRequestsByRequestCode(requestsBody.getRequestCode());
            if (isEmpty(requests)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getExternalUserDispatchDocs();

                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                List<String> fileDeleted = new ArrayList<>();
                for (String str1 : filePath.getFiles()) {
                    fileDeleted.add(str1);
                }
                System.out.println("size is " + fileDeleted.size());
                if (fileDeleted.contains(documentPath)) {
                    Path fileToDeletePath = Paths.get(documentPath);
                    Files.delete(fileToDeletePath);
                    fileDeleted.remove(documentPath);

                    if (fileDeleted.size() == 0) {
                        System.out.println("Size of external document file is " + fileDeleted);
                        requests.setExternalUserDispatchDocs("");
                        this.requestService.saveRequest(requests);
                        return ResponseEntity.status(HttpStatus.OK).body("Request External Document Deleted Successfully");
                    }
                }

                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                requests.setExternalUserDispatchDocs(json);
                this.requestService.saveRequest(requests);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Request External Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Request External Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteDispatchDocument

    @RequestMapping(value = "/getDispatchDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getDispatchDocsList(HttpServletRequest request,
                                                 @RequestParam String requestCode) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getDispatchDocs();
                if (!isEmpty(pathFromDB)) {
                    Gson gson = new Gson();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    List<String> files = new ArrayList<String>();
                    for (String str1 : filePath.getFiles()) {
                        files.add(str1);
                        json = gson.toJson(files);

                    }
                    return ResponseEntity.status(HttpStatus.OK).body(json);
                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(test);
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDispatchDocsList

    @RequestMapping(value = "/getExternalUserRequestDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getExternalUserDispatchDocsList(HttpServletRequest request,
                                                             @RequestParam String requestCode) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Requests requests = this.requestService.getRequestsByRequestCode(requestCode);
            if (!isEmpty(requests)) {
                String pathFromDB = requests.getExternalUserDispatchDocs();
                Gson gson = new Gson();
                FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                System.out.println("filePath is " + filePath.getFiles().toString());
                List<String> files = new ArrayList<String>();
                for (String str1 : filePath.getFiles()) {
                    files.add(str1);
                    json = gson.toJson(files);

                }
                return ResponseEntity.status(HttpStatus.OK).body(json);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(test);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDispatchDocsList

    @RequestMapping(value = "/externalUserDownloadRequestDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> externalUserDownloadDispatchDocuments(HttpServletRequest request,
                                                                                     @RequestBody @Valid Requests requests) throws IOException {
        Requests req = this.requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("req documents are one " + req.getExternalUserDispatchDocs());
        String pathFromDB = req.getExternalUserDispatchDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            externalUserZipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "ExternalUserDispatchDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= ExternalUserDispatchDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadDispatchDocuments

    @RequestMapping(value = "/downloadDispatchDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadDocumentation(HttpServletRequest request,
                                                                     @RequestBody @Valid Requests requests) throws IOException {
        Requests req = this.requestService.getRequestsByRequestCode(requests.getRequestCode());
        System.out.println("req documents are one " + req.getDispatchDocs());
        String pathFromDB = req.getDispatchDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DispatchDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= DispatchDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadDispatchDocuments

    public void externalUserZipFiles(List<String> files) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "ExternalUserDispatchDocumentsDownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles

    @PostMapping("/uploadUserPaymentConfirmation")
    public ResponseEntity<?> uploadUserPaymentConfirmation(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                                           @RequestParam("requestCode") String requestCode,
                                                           @RequestParam("userCode") String userCode,
                                                           @RequestParam("userName") String userName
    ) {
        String message = "";
        List<String> files = new ArrayList<String>();
        try {
            if (file.isEmpty()) {
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            } else if (requestCode != null && !isEmpty(requestCode)) {
                Requests requests = this.requestService.getRequestsByRequestCodeUserCodeUserName(requestCode, userCode, userName);
                if (requests != null) {
                    String fileName = testService.store(file);
                    files.add(file.getOriginalFilename());
                    requests.setPopFilePath(applicationPropertiesConfiguration.getUploadDirectoryPath() + fileName);
                    message = "You successfully uploaded " + requests.getPopFilePath() + "!";
                    Requests requests1 = requestService.saveRequest(requests);
                    if (requests1 != null) {
                        requests1.setPaymentStatus("PAID");
                    }
                    requestService.saveRequest(requests1);
                    //send notification
                    uploadPaymentConfirmationNotification(requests1, true);

                    return ResponseEntity.status(HttpStatus.OK).body(message);

                } else {
                    return generateEmptyResponse(request, "No Internal User Roles  found");
                }
            }
            return generateEmptyResponse(request, "No Internal User Roles  found");
        } catch (Exception exception) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return generateFailureResponse(request, exception);
        }
    }//uploadUserPaymentConfirmation

    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "DispatchDocumentsDownloadFiles.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles

    public void ftpZipFiles(List<String> files, String timeStamp) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        String zipFilename = "FTPFilesDownload" + "_" + timeStamp + ".zip";
        // String zipFilename = "FTPFilesDownload.zip";
        try {

            fos = new FileOutputStream(applicationPropertiesConfiguration.getUploadDirectoryPathFTP() + zipFilename);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                System.out.println("filePath is " + filePath);
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }//zipFiles

    private void cancellationNotification(Requests requests) {
        try {              
        	User user = null;
            List<InternalUserRoles> userList = new ArrayList<>();
            
            if(!StringUtils.isEmpty(requests.getProvinceCode()) && !StringUtils.isEmpty(requests.getSectionCode())) {
            	userList = this.internalUserRoleService.getUsersByProvinceCodeAndSectionCodeAndUserRoleCode(requests.getProvinceCode(), 
            			requests.getSectionCode(), "IN013");
            } else if (!StringUtils.isEmpty(requests.getProvinceCode())) {
            	userList = this.internalUserRoleService.getUsersByProvinceCodeAndUserRoleCode(requests.getProvinceCode(), "IN013");      
            }
            if(!CollectionUtils.isEmpty(userList))
            	user = this.userService.findByUserCode(userList.get(0).getUserCode()); 
            
            String email = (user != null && user.getUserName() != null) ? user.getUserName() : user.getEmail();
            log.info("Email is: " + email);
            if (StringUtils.isEmpty(email)) {
                log.error("Could not find invoice generated user email, No payment confirmation mail sent");
                return ;
            }
            String userFullName = (user != null) ? user.getFirstName() + " " + user.getSurname() : "";
            String subject = "Cancellation notification";
            String body = "Cancellation processed successfully, reference code: " + requests.getRequestCode();            
                       
            sendMail(requests, new MailDTO(), userFullName, subject, body, email);
        } catch (Exception e) {
            log.error("Failed to send cancellation notification, " + e.getMessage());
        }
    }//cancellationNotification
    
    private void uploadPaymentConfirmationNotification(Requests requests, boolean isUser) {
        try {              
            User user = null;
            String email = getInvoiceGeneratedUserEmail(requests);
            log.info("Email is: " + email);
            if (StringUtils.isEmpty(email)) {
                log.error("Could not find invoice generated user email, No payment confirmation mail sent");
                return ;
            }
            if(isUser) {
            	user = this.userService.getUserByUserName(email);
            } else if (requests.getUserCode() != null) {
                user = this.userService.findByUserCode(requests.getUserCode());                
            }
            String userFullName = (user != null) ? user.getFirstName() + " " + user.getSurname() : "";
            String subject = "Upload user payment confirmation";
            String body = "Upload user payment confirmation processed successfully, reference code: " + requests.getRequestCode();            
                       
            sendMail(requests, new MailDTO(), userFullName, subject, body, email);
        } catch (Exception e) {
            log.error("Failed to send upload user payment confirmation notification, " + e.getMessage());
        }
    }//uploadPaymentConfirmationNotification

    private String getInvoiceGeneratedUserEmail(Requests requests) {
        String userEmail = null;
        List<TaskLifeCycle> taskLifeCycles = taskService.getTasksLifeCycleByTaskReferenceCode(requests.getRequestCode());
        if (CollectionUtils.isEmpty(taskLifeCycles)) return null;
        for (TaskLifeCycle taskLifeCycle : taskLifeCycles) {
            if ("GenerateInvoice".equals(taskLifeCycle.getTaskStatus())) {
                if (taskLifeCycle.getTaskDoneUserName() != null) {
                    userEmail = taskLifeCycle.getTaskDoneUserName();
                } else {
                    if (taskLifeCycle.getTaskDoneUserCode() != null) {
                        User user = this.userService.findByUserCode(taskLifeCycle.getTaskDoneUserCode());
                        userEmail = (user != null) ? user.getUserName() : null;
                    }
                }
            }
        }
        return userEmail;
    }//getInvoiceGeneratedUserEmail

    private User getUser(String userName) {
        return (userName == null) ? null : this.userService.getUserByUserName(userName);
    }//getInvoiceGeneratedUser
    
    private void sendMailToCreateRequestUser(@RequestBody @Valid Requests requests, MailDTO mailDTO) throws Exception {
        String userName = null;
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            userName = (user != null) ? user.getFirstName() + " " + user.getSurname() : "Test User";
        }
        String subject = "Request Created";
        String body = "Your request is created successfully with reference code: " + requests.getRequestCode();

        sendMail(requests, mailDTO, userName, subject, body, requests.getEmail());
    }//sendMailToCreateRequestUser

    private void sendMail(Requests requests, MailDTO mailDTO, String userName, String subject, String body, String email) throws Exception {
        String firstName = null;
        String lastName = null;
        Map<String, Object> model = new HashMap<String, Object>();
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
        model.put("firstName", firstName + " " + lastName);
        model.put("body1", body);
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject(subject);
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(email);
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }//sendMail

    

	/*
	 * Send email to Cashier, get document path from IMRequests table and send mail 
	 * @PostMapping
	 * @Request
	 * 		@HttpServletRequest - requestObject 
	 * 		@DTOObject - SendPopInfo (Required)
	 * @Response
	 * 		JsonObject(Requests.class)
	 */
	@PostMapping(value= "/sendPopToCashier", produces= {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> sendPopToCashier(HttpServletRequest request, @RequestBody @Valid SendPopInfo sendPopInfo) {
		try {
			System.out.println("Test 1");
			String filePath = null;
			String requestCode = sendPopInfo.getRequestCode();
			String emailAddress = sendPopInfo.getEmailAddress();
			final String fileName = "Payment Verification Document";

			if (StringUtils.isEmpty(requestCode) && requestCode.trim().length() == 0)
				return generateEmptyWithOKResponse(request, "Request Code should not be empty");
			
			if (StringUtils.isEmpty(emailAddress) && requestCode.trim().length() == 0)
				return generateEmptyWithOKResponse(request, "Email should not be empty");
			
			if (requestCode != null) 
				filePath = requestService.getFilePathByRequestItemCode(requestCode);

			
			if (filePath == null && isEmpty(filePath)) 
				return generateEmptyWithOKResponse(request, " File  not found for Request code :" + requestCode);
			
			//filePath="C:/Personal/sample.pdf";//Testing Hardcoded value
			File attachement = new File(filePath);
			if(!attachement.exists()) 
				return generateEmptyWithOKResponse(request, " File does not existing in the location :" + filePath);
			
			
			ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
			emailExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						sendMailWithPOPAttachment(emailAddress, fileName, attachement);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			emailExecutor.shutdown();
		} 
		catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Sent email Sucessfully");
	}

	
	private void sendMailWithPOPAttachment(String emailAddress, String fileName, File file) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		MailDTO mailDTO = new MailDTO();
		model.put("firstName", "Cashier,");
		model.put("body1", "Please verify the payment based on the attachment.");
		model.put("body2", "");
		model.put("body3", "");
		model.put("body4", "");

		mailDTO.setMailSubject("Payment Verification");
		model.put("FOOTER", "CIS ADMIN");
		mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
		mailDTO.setMailTo(emailAddress);
		mailDTO.setModel(model);

		sendEmail(mailDTO, fileName, file);
	}
	 
	
	/*
	 * Save/update Tracking into IMRequests table
	 * @PutMapping
	 * @Request
	 * 		@HttpServletRequest - requestObject 
	 * 		@RequestParam - requestCode (Required)
	 * 		@RequestParam - trackingNo  (Optional)
	 * @Response
	 * 		JsonObject(Requests.class)
	 */
	@PutMapping("/setRequestTrackingNo")
	public ResponseEntity<?> setRequestTrackingNo(HttpServletRequest request,
			@RequestParam(required = false) String trackingNo, @RequestParam(required = false) String requestCode) {
		try {

			if (StringUtils.isEmpty(requestCode) && requestCode.trim().length() == 0)
				return generateEmptyWithOKResponse(request, "Invalid request code");

			Requests updateRequest = this.requestService.updateRequestOnTrackingNo(requestCode, trackingNo);

			return (StringUtils.isEmpty(updateRequest))
					? generateEmptyWithOKResponse(request, "No request found, given request code:" + requestCode)
					: ResponseEntity.status(HttpStatus.OK).body(updateRequest);

		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}
	
	
	/*
	 * Get Tracking into IMRequests table
	 * @GetMapping
	 * @Request 
	 * 		@HttpServletRequest - requestObject
	 * 		@RequestParam - requestCode (Mandatory)
	 * @Response
	 * 		JsonString(String.class)
	 */
	@GetMapping(value="/getRequestTrackingNo", produces= { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getRequestTrackingNo(HttpServletRequest request, @RequestParam(required = true) String requestCode) {
		try {

			if (StringUtils.isEmpty(requestCode) && requestCode.trim().length() == 0)
				return generateEmptyWithOKResponse(request, "Invalid request code");

			Requests requests = this.requestService.getRequestsByRequestCode(requestCode);

			if(requests != null)
            	requests.setCurrentStatus(getRequestStatus(requests.getRequestCode()));
			
			return (StringUtils.isEmpty(requests))
					? generateEmptyWithOKResponse(request, "No request found, given request code:" + requestCode)
					: ResponseEntity.status(HttpStatus.OK).body(requests.getTrackingNumnber());

		} catch (Exception exception) {
			return generateFailureResponse(request, exception);
		}
	}
    
}
