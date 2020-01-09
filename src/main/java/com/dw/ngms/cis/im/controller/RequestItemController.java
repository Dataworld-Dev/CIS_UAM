package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.dto.RequestItemsDTO;
import com.dw.ngms.cis.im.entity.*;
import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import com.dw.ngms.cis.im.service.EmailTemplateService;
import com.dw.ngms.cis.im.service.RequestItemService;
import com.dw.ngms.cis.im.service.RequestService;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class RequestItemController extends MessageController {

    @Autowired
    private RequestItemService requestItemService;

    @Autowired
    private RequestService requestsService;

    @Autowired
    private ApplicationPropertiesService appPropertiesService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    private EmailTemplateService email;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;
    
    @Autowired
    private TaskService taskService;

   /*@GetMapping("/getRequestsOfUser")
    public ResponseEntity<?> getRequestsOfUser(HttpServletRequest request,
                                               @RequestParam(required=false) String provinceCode,
                                               @RequestParam(required=false) String userCode) {
        try {
            List<Requests> requestList = new ArrayList<>();
            if(StringUtils.isEmpty(provinceCode) || "all".equalsIgnoreCase(provinceCode.trim())){
                requestList = requestService.getAllRequests();
            }else if(!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(provinceCode.trim()) ){
                requestList = requestService.getRequestByUserCodeProvinceCode(userCode,provinceCode);
            }
            return (CollectionUtils.isEmpty(requestList)) ? generateEmptyResponse(request, "Request(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(requestList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestsOfUser
*/

    @PostMapping("/createRequestItem")
    public ResponseEntity<?> createRequestType(HttpServletRequest request, @RequestBody @Valid RequestItems requestItems) {
        Gson gson = new Gson();
        try {
            Long requestItemCode = this.requestItemService.getRequestItemId();
            System.out.println("requestItemCode is " + requestItemCode);
            requestItems.setRequestItemCode("REQITEM" + Long.toString(requestItemCode));
            RequestItemsDTO requestItemsDTO = new RequestItemsDTO();
            requestItemsDTO.setGazetteType1(requestItems.getGazetteType1());
            requestItemsDTO.setGazetteType2(requestItems.getGazetteType2());
            requestItemsDTO.setRequestCost(requestItems.getRequestCost());
            requestItemsDTO.setRequestHours(requestItems.getRequestHours());
            String requestJson = gson.toJson(requestItemsDTO);
            requestItems.setResultJson(requestJson);
            RequestItems requestItemsSave = this.requestItemService.saveRequestItem(requestItems);

            List<RequestItems> getAllRequestItems = this.requestItemService.getRequestsByRequestItemCode(requestItemsSave.getRequestCode());

            RequestItems[] itemsArray = new RequestItems[getAllRequestItems.size()];
            itemsArray = getAllRequestItems.toArray(itemsArray);

            Double totalSum = 0.00;
            for (int i = 0; i < itemsArray.length; i++) {
                totalSum = totalSum + Double.parseDouble(itemsArray[i].getRequestCost());
            }
            System.out.println("Total is" + totalSum);

            Requests requests = this.requestsService.getRequestsByRequestCode(requestItemsSave.getRequestCode());
            requests.setTotalAmount(String.valueOf(totalSum));
            this.requestsService.saveRequest(requests);

            return ResponseEntity.status(HttpStatus.OK).body(requestItemsSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createRequestItem


    @PostMapping("/getRequestItemsFilesSendFTP")
    public ResponseEntity<?> getRequestItemsFilesSendFTP(HttpServletRequest request,
                                                           @RequestBody RequestItems requestItems
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        FTPClient ftpClient = new FTPClient();
        String str = null;
        List<String> filesExist = new ArrayList<>();
        double totalFileSize = 0.0;
        try {
            List<RequestItems> requestItemsList = requestItemService.getRequestsByRequestCode(requestItems.getRequestCode());
            System.out.println("size is "+requestItemsList.size());
            if (requestItemsList.size()>0) {
                for (RequestItems items : requestItemsList) {
                    String fileName = null;

                 if (items.getFtpSiteUrl() != null && !items.getFtpSiteUrl().isEmpty()) {
                        int index = items.getFtpSiteUrl().lastIndexOf("/");
                         fileName = items.getFtpSiteUrl().substring(index + 1);
                        System.out.println("File Name is " + fileName);
                    }
                 
                 
                 if (items.getRequestGazetteType() != null
							&& "REQUEST".equalsIgnoreCase(items.getRequestGazetteType())) {
                 	System.out.println("items ::"+ items.toString());
                 	JSONObject obj = new JSONObject(items.getResultJson());
                     String imageSize = obj.getString("mbSize");
                     System.out.println("*************** pageName **********:"+ imageSize);
                     totalFileSize = totalFileSize + Double.parseDouble(imageSize);

					}

                    if (items.getFtpSiteUrl() != null && !items.getFtpSiteUrl().isEmpty()) {
                        InputStream inputStream = new URL(items.getFtpSiteUrl()).openStream();
                        Files.copy(inputStream, Paths.get(applicationPropertiesConfiguration.getRequestDirectoryLocalPath() + fileName), StandardCopyOption.REPLACE_EXISTING);
                        filesExist.add(applicationPropertiesConfiguration.getRequestDirectoryLocalPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        items.setFtpSiteUrlJson(json);

                        str = items.getFtpSiteUrlJson();

                        //System.out.println("Items are "+new Gson().toJson(items.getFtpSiteUrlJson()));
                    }
                }

				Task t1 = this.updateTaskStatus(totalFileSize, requestItems.getRequestCode());
				
				if (t1 == null) {
					return generateEmptyWithOKResponse(request, " ");
				}
                
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                if (str != null && !str.isEmpty()) {
                    String pathFromDB = str;
                    System.out.println("str is " + str);
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    List<String> files = new ArrayList<String>();
                    for (String str1 : filePath.getFiles()) {
                        System.out.println(str1);
                        files.add(str1);
                        ftpZipFiles(files, ftpClient, timeStamp);
                    }


                    String zipFilename = "RequestItemsFtpFilesDownload" + "_" + timeStamp + ".zip";

                    boolean loginExists = ftpLogin(ftpClient);
                    if (loginExists) {
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.changeWorkingDirectory("/ftpFileDownload/");
                        File firstLocalFile = new File(applicationPropertiesConfiguration.getRequestDirectoryFtpPath() + zipFilename);
                        String firstRemoteFile = zipFilename;
                        InputStream inputStream = new FileInputStream(firstLocalFile);
                        System.out.println("Start uploading first file");
                        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                        inputStream.close();
                    }
                    ftpClient.logout();
                    String path = appPropertiesService.getProperty("FTP_UPLOAD_PATH").getKeyValue();
                    String server = "ftp://" + appPropertiesService.getProperty("FTP_SERVER").getKeyValue();

                    String ftpFilePath = server + path + zipFilename;
                    System.out.println("File Path is: " + ftpFilePath);

                    MailDTO mailDTO = new MailDTO();
                    EmailTemplate template = this.email.getEmailTemplateById(10);
                    mailDTO.setBody1(template.getBody());
                    mailDTO.setSubject(template.getSubject());
                    mailDTO.setFooter(template.getFooter());
                    mailDTO.setHeader(template.getHeader());


                    // inside your getSalesUserData() method
                    Requests requests = this.requestsService.getRequestsByRequestCode(requestItems.getRequestCode());
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
                }else{
                    return generateEmptyResponse(request, "RequestItems files are  not found");
                }
            }else{
                return generateEmptyResponse(request, "RequestItems files are  not found");
            }


        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Sent email Sucessfully");
    }



    @PostMapping("/getRequestItemsFilesSendEmail")
    public ResponseEntity<?> getRequestItemsFilesSendEmail(HttpServletRequest request,
                                                           @RequestBody RequestItems requestItems
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        FTPClient ftpClient = new FTPClient();
        String str = null;
        List<String> filesExist = new ArrayList<>();
        try {
            List<RequestItems> requestItemsList = requestItemService.getRequestsByRequestCode(requestItems.getRequestCode());
            System.out.println("size is "+requestItemsList.size());
            double totalFileSize = 0.00;
            if (requestItemsList.size()>0) {
                for (RequestItems items : requestItemsList) {
                    String fileName = null;

                    if (items.getFtpSiteUrl() != null && !items.getFtpSiteUrl().isEmpty()) {
                        int index = items.getFtpSiteUrl().lastIndexOf("/");
                        fileName = items.getFtpSiteUrl().substring(index + 1);
                        System.out.println("File Name is " + fileName);
                    }
                    
                    
                    
                    if (items.getRequestGazetteType() != null
							&& "REQUEST".equalsIgnoreCase(items.getRequestGazetteType())) {
                    	System.out.println("items ::"+ items.toString());
                    	JSONObject obj = new JSONObject(items.getResultJson());
                        String imageSize = obj.getString("mbSize");
                        System.out.println("*************** pageName **********:"+ imageSize);
                        totalFileSize = totalFileSize + Double.parseDouble(imageSize);

					}

                    if (items.getFtpSiteUrl() != null && !items.getFtpSiteUrl().isEmpty()) {
                        System.out.println("items url is " + items.getFtpSiteUrl());
                        InputStream inputStream = new URL(items.getFtpSiteUrl()).openStream();
                        Files.copy(inputStream, Paths.get(applicationPropertiesConfiguration.getRequestDirectoryLocalPath() + fileName), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("items open");

                        filesExist.add(applicationPropertiesConfiguration.getRequestDirectoryLocalPath() + fileName);
                        userControllerResponse.setFiles(filesExist);
                        json = gson.toJson(userControllerResponse);
                        items.setFtpSiteUrlJson(json);

                        str = items.getFtpSiteUrlJson();

                        //System.out.println("Items are "+new Gson().toJson(items.getFtpSiteUrlJson()));
                    }
                }

                Task t1 = this.updateTaskStatus(totalFileSize, requestItems.getRequestCode());
                
                if(t1 == null) {
                	return generateEmptyWithOKResponse(request, " ");
                }
                
                
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                if (str != null && !str.isEmpty()) {
                    String pathFromDB = str;
                    System.out.println("str is " + str);
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath is " + filePath.getFiles().toString());
                    List<String> files = new ArrayList<String>();
                    for (String str1 : filePath.getFiles()) {
                        System.out.println(str1);
                        files.add(str1);
                        ftpZipFilesEmail(files, ftpClient, timeStamp);
                    }


                    String zipFilename = "RequestDocument" + "_" + timeStamp + ".zip";
                    File firstLocalFile = new File(applicationPropertiesConfiguration.getRequestDirectoryFtpPath() + zipFilename);

                    MailDTO mailDTO = new MailDTO();
                    EmailTemplate template = this.email.getEmailTemplateById(9);
                    mailDTO.setBody1(template.getBody());
                    mailDTO.setSubject(template.getSubject());
                    mailDTO.setFooter(template.getFooter());
                    mailDTO.setHeader(template.getHeader());

                    ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
                    emailExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendMailWithAttachment(requestItems, mailDTO,zipFilename, firstLocalFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.
                }else{
                    return generateEmptyResponse(request, "RequestItems files are  not found");
                }
            }else{
                return generateEmptyResponse(request, "RequestItems files are  not found");
            }


        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Sent email Successfully");
    }


    private Task updateTaskStatus(double totalFileSize, String requestCode) {
    	 System.out.println("----------------------"+appPropertiesService.getProperty("SINGLE_REQUEST_SIZE").getKeyValue());
         String configValue = appPropertiesService.getProperty("SINGLE_REQUEST_SIZE").getKeyValue();
         double configSize = (!isEmpty(configValue)) ? (Double.valueOf(configValue)/1024d) : 0.0 ;
         System.out.println("Test conf size :"+ configSize);
         System.out.println("******* Total File Size ******* :"+ totalFileSize);
         Task updatedTask = null;
         if(totalFileSize <= configSize) {
         	System.out.println("Sample");
         	updatedTask = taskService.getTask(requestCode);
         	
         	updatedTask.setTaskStatus("Closed");
         	updatedTask.setUpdatedDate(new Date());
         	
         	updatedTask = taskService.saveTask(updatedTask);
         	
         }else {
        	 return null;
         }
         return updatedTask;
    }
    

    private void sendMailWithAttachment(RequestItems requestItems, MailDTO mailDTO, String fileName, File file) throws Exception {
        String firstName = null;
        String lastName = null;
        Map<String, Object> model = new HashMap<String, Object>();
        Requests requests = this.requestsService.getRequestsByRequestCode(requestItems.getRequestCode());
        if (requests.getUserCode() != null) {
            User user = this.userService.findByUserCode(requests.getUserCode());
            firstName = user.getFirstName();
            lastName = user.getSurname();
        }
       /* model.put("firstName", firstName + " " + lastName);*/
        model.put("firstName", mailDTO.getHeader()+" " +firstName + " " + lastName);

  /*      model.put("body1", "Request Attached");*/
        model.put("body1", mailDTO.getBody1());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");

        /*mailDTO.setMailSubject("DRDLR:Delivery");*/
       /* model.put("FOOTER", "CIS ADMIN");*/
        mailDTO.setMailSubject(mailDTO.getSubject());
        model.put("FOOTER", mailDTO.getFooter());
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getUserName());
        mailDTO.setModel(model);

        sendEmail(mailDTO, fileName, file);
        //sendEmail(mailDTO);

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
       /* model.put("firstName", firstName + " " + lastName);*/
        model.put("firstName", mailDTO.getHeader()+" " +firstName + " " + lastName);
       /* model.put("body1", "FTP paths send successfully");*/
        model.put("body1", mailDTO.getBody1());
        model.put("body2", ftpFilePath);
        model.put("body3", "");
        model.put("body4", "");

       /* mailDTO.setMailSubject("DRDLR:Delivery");
        model.put("FOOTER", "CIS ADMIN");*/

        mailDTO.setMailSubject(mailDTO.getSubject());
        model.put("FOOTER", mailDTO.getFooter());

        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(requests.getEmail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);

    }


    public void ftpZipFilesEmail(List<String> files, FTPClient ftpClient,String timeStamp) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        String zipFilename = "RequestDocument"+"_"+timeStamp+".zip";
        try {

            fos = new FileOutputStream(applicationPropertiesConfiguration.getRequestDirectoryFtpPath() + zipFilename);
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




    public void ftpZipFiles(List<String> files, FTPClient ftpClient,String timeStamp) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        String zipFilename = "RequestItemsFtpFilesDownload"+"_"+timeStamp+".zip";
        try {

            fos = new FileOutputStream(applicationPropertiesConfiguration.getRequestDirectoryFtpPath() + zipFilename);
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



    @GetMapping("/getRequestItemsOfRequest")
    public ResponseEntity<?> getRequestItemsOfRequest(HttpServletRequest request,
                                                      @RequestParam String requestCode) {
        try {
            List<RequestItems> requestItemsList = requestItemService.getRequestsByRequestCode(requestCode);
            return (org.springframework.util.CollectionUtils.isEmpty(requestItemsList)) ? generateEmptyResponse(request, "RequestKind(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(requestItemsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestItemsOfRequest


    @RequestMapping(value = "/deleteRequestItem", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRequestItem(HttpServletRequest request,
                                               @RequestBody @Valid RequestItems items) throws IOException {
        try {
            RequestItems requestItems = this.requestItemService.getRequestsByRequestCodeAndItemCode(items.getRequestCode(), items.getRequestItemCode());
            if (isEmpty(requestItems)) {
                return generateEmptyResponse(request, "RequestItems are  not found");
            }
            if (!isEmpty(requestItems)) {
                this.requestItemService.deleteRequestItem(requestItems);
                List<RequestItems> getAllRequestItems = this.requestItemService.getRequestsByRequestItemCode(requestItems.getRequestCode());

                RequestItems[] itemsArray = new RequestItems[getAllRequestItems.size()];
                itemsArray = getAllRequestItems.toArray(itemsArray);

                Double totalSum = 0.00;
                for (int i = 0; i < itemsArray.length; i++) {
                    totalSum = totalSum + Double.parseDouble(itemsArray[i].getRequestCost());
                }
                System.out.println("Total sum after delete is" + totalSum);


                Requests requests = this.requestsService.getRequestsByRequestCode(requestItems.getRequestCode());
                requests.setTotalAmount(String.valueOf(totalSum));
                this.requestsService.saveRequest(requests);

                return ResponseEntity.status(HttpStatus.OK).body("Request Item Deleted Successfully");
            }

            return generateEmptyResponse(request, "RequestItems are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


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


    protected static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

}
