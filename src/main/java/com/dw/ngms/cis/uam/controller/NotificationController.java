package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.EmailTemplate;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import com.dw.ngms.cis.im.service.EmailTemplateService;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import com.dw.ngms.cis.uam.dto.FilePathsDTO;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.NotificationSubTypes;
import com.dw.ngms.cis.uam.entity.Notifications;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.NotificationService;
import com.dw.ngms.cis.uam.service.NotificationSubTypesService;
import com.dw.ngms.cis.uam.service.UserService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/12.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class NotificationController extends MessageController {

    @Autowired
    private StorageService testService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationSubTypesService notificationSubTypes;

    @Autowired
    private EmailTemplateService email;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationPropertiesService appPropertiesService;

    /* @PostMapping("/saveNotification")
     public ResponseEntity<?> saveNotification(HttpServletRequest request, @RequestBody @Valid Notifications notification) {
         try {
             //Long notificationId = this.notificationService.getNotificationId();
             //System.out.println("notificationId is "+notificationId);
             //costCategories.setCategoryCode("COST" + Long.toString(categoryId));
             Notifications notificationSave = this.notificationService.saveNotification(notification);
             MailDTO mailDTO = new MailDTO();
             sendMailToNotification(mailDTO, notification);
             sendMailToNotification1(mailDTO,notification);
             sendMailToNotification2(mailDTO,notification);

             return ResponseEntity.status(HttpStatus.OK).body(notificationSave);
         } catch (Exception exception) {
             return generateFailureResponse(request, exception);
         }
     }//saveNotification

 */
    @PostMapping("/saveNotification")
    public ResponseEntity<?> saveNotification(HttpServletRequest request, @RequestBody @Valid Notifications notification) {
        try {
            Notifications notificationSave = this.notificationService.saveNotification(notification);
            MailDTO mailDTO = new MailDTO();

            EmailTemplate template = this.email.getEmailTemplateById(14);
            mailDTO.setBody1(template.getBody());
            mailDTO.setSubject(template.getSubject());
            mailDTO.setFooter(template.getFooter());
            mailDTO.setHeader(template.getHeader());

            sendMailToCreateNotificationUser(notificationSave, mailDTO);
            return ResponseEntity.status(HttpStatus.OK).body(notificationSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//saveNotification


    private void sendMailToCreateNotificationUser(@RequestBody @Valid Notifications notifications, MailDTO mailDTO) throws Exception {
        String userName = null;
        if(notifications.getCreatedByUserName()!= null){
            User user  = this.userService.findByEmail(notifications.getCreatedByUserName().trim().toLowerCase());
            if(user!=null){
                userName = user.getFirstName() +" "+ user.getSurname();
            }
        }
        if(StringUtils.isEmpty(userName)){
            userName = "Notification User";
        }
        Map<String, Object> model = new HashMap<String, Object>();
       /* model.put("firstName", userName);*/
        model.put("firstName",  mailDTO.getHeader()+" " +userName);
       /* model.put("body1", "Your Notification is created successfully");*/
        model.put("body1", mailDTO.getBody1());

        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        /*mailDTO.setMailSubject("Create Notification");
        model.put("FOOTER", "CIS ADMIN");*/
        mailDTO.setMailSubject(mailDTO.getSubject());
        model.put("FOOTER", mailDTO.getFooter());
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(notifications.getCreatedByUserName());
        mailDTO.setModel(model);
        sendEmail(mailDTO);


    }


    @GetMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {
        try {
            List<Notifications> notificationsList = notificationService.getAllNotifications();
            return (CollectionUtils.isEmpty(notificationsList)) ? generateEmptyResponse(request, "Notification(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(notificationsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNotifications

    @GetMapping("/getNotificationSubTypes")
    public ResponseEntity<?> getNotificationSubTypes(HttpServletRequest request) {
        try {
            List<NotificationSubTypes> notificationsList = notificationSubTypes.getNotificationSubTypes();
            return (CollectionUtils.isEmpty(notificationsList)) ? generateEmptyResponse(request, "Notification SubType(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(notificationsList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNotifications

    private void sendEmailTOAllUsers(MailDTO mailDTO, Notifications notifications, User user) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", user.getFirstName() + " " +user.getSurname());
        model.put("body1", notifications.getBody());
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailSubject(notifications.getSubject());
        mailDTO.setMailTo(user.getEmail());
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }



    @PostMapping("/uploadNotificationDocument")
    public ResponseEntity<?> uploadNotificationDocument(HttpServletRequest request, @RequestParam MultipartFile[] multipleFiles,
                                                        @RequestParam("notificationId") Long notificationId
    ) {
        String message = "";
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            Notifications notifications = this.notificationService.getNotificationByID(notificationId);
            if (notifications != null && !isEmpty(notifications)) {
                if (notifications.getNotificationDocs() != null) {
                    List<String> filesExist = new ArrayList<>();
                    String pathFromDB = notifications.getNotificationDocs();
                    FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
                    System.out.println("filePath for notification docs is " + filePath.getFiles().toString());
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
                        notifications.setNotificationDocs(json);
                        this.notificationService.saveNotification(notifications);
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
                        notifications.setNotificationDocs(json);
                        this.notificationService.saveNotification(notifications);
                    }
                }

            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//uploadNotificationDocument

    @RequestMapping(value = "/deleteNotificationDocument", method = RequestMethod.POST)
    public ResponseEntity<?> deleteNotificationDocument(HttpServletRequest request,
                                                        @RequestBody @Valid Notifications notificationBody,
                                                        @RequestParam String documentPath) throws IOException {
        try {
            String json = null;
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Notifications notifications = this.notificationService.getNotificationByID(notificationBody.getNotificationId());
            if (isEmpty(notifications)) {
                return generateEmptyResponse(request, "Requests are  not found");
            }
            if (!isEmpty(notifications)) {
                String pathFromDB = notifications.getNotificationDocs();

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
                        System.out.println("Size of file is " + fileDeleted);
                        notifications.setNotificationDocs("");
                        this.notificationService.saveNotification(notifications);
                        return ResponseEntity.status(HttpStatus.OK).body("Notification Document Deleted Successfully");
                    }

                }
                userControllerResponse.setFiles(fileDeleted);
                json = gson.toJson(userControllerResponse);
                notifications.setNotificationDocs(json);
                this.notificationService.saveNotification(notifications);
                System.out.println("size is after " + fileDeleted.size());
                return ResponseEntity.status(HttpStatus.OK).body("Notification Document Deleted Successfully");
            }
            return generateEmptyResponse(request, "Notification Document are  not found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteNotificationDocument

    @RequestMapping(value = "/getNotificationDocsList", method = RequestMethod.GET)
    public ResponseEntity<?> getNotificationDocsList(HttpServletRequest request,
                                                     @RequestParam Long notificationId) throws IOException {
        try {
            String json = null;
            List<String> test = new ArrayList<>();
            Notifications notifications = this.notificationService.getNotificationByID(notificationId);
            if (!isEmpty(notifications)) {
                String pathFromDB = notifications.getNotificationDocs();
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

    @RequestMapping(value = "/downloadNotificationDocuments", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> downloadNotificationDocuments(HttpServletRequest request,
                                                                             @RequestBody @Valid Notifications notificationBody) throws IOException {
        Notifications notifications = this.notificationService.getNotificationByID(notificationBody.getNotificationId());
        System.out.println("notifications documents are one " + notifications.getNotificationDocs());
        String pathFromDB = notifications.getNotificationDocs();

        Gson gson = new Gson();
        FilePathsDTO filePath = gson.fromJson(pathFromDB, FilePathsDTO.class);
        System.out.println("filePath is " + filePath.getFiles().toString());
        List<String> files = new ArrayList<String>();
        for (String str1 : filePath.getFiles()) {
            System.out.println(str1);
            files.add(str1);
            zipFiles(files);
        }
        File file = new File(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "NotificationDocumentsDownloadFiles.zip");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename= NotificationDocumentsDownloadFiles.zip")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length()) //
                .body(resource);
    }//downloadNotificationDocuments

    public void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(applicationPropertiesConfiguration.getDownloadDirectoryPath() + "NotificationDocumentsDownloadFiles.zip");
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


    @Component
    public class EmailNotificationScheduler {

        //@Scheduled(fixedRate = 10000000 ) for two hours
        @Scheduled(fixedRate = 24000000)
        public void fixedRateSch() throws Exception {

            List<Notifications> notificationsList = notificationService.getAllNotifications();
            MailDTO mailDTO = new MailDTO();
            for (Notifications notifications : notificationsList) {
                if (notifications.getNotificationStatus().equalsIgnoreCase("OPEN") &&
                        notifications.getNotificationuserTypes().equalsIgnoreCase("All Internal Users")) {
                    System.out.println("Email Notification scheduler into internal users: " + notifications.getNotificationId());
                    //send email to all Internal users
                    List<User> userList = userService.findAllUsersByUserType("INTERNAL");
                    if (!isEmpty(userList) && userList != null) {
                        for (User userEmailList : userList) {
                            System.out.println("User test email for Internal: " + userEmailList.getEmail());
                            sendEmailTOAllUsers(mailDTO, notifications, userEmailList);
                        }
                        notifications.setNotificationStatus("CLOSE");
                        notificationService.saveNotification(notifications);
                    }
                    System.out.println("Email Notification scheduler Done for Internal:");
                } else if (notifications.getNotificationStatus().equalsIgnoreCase("OPEN") &&
                        notifications.getNotificationuserTypes().equalsIgnoreCase("All External Users")) {
                    System.out.println("Email Notification scheduler into external users: " + notifications.getNotificationId());
                    //send email to all external users
                    List<User> userList = userService.findAllUsersByUserType("EXTERNAL");

                    if (!isEmpty(userList) && userList != null) {
                        for (User userEmailList : userList) {
                            ExternalUser externalUser = userService.getChildElements(userEmailList.getUserCode());
                            userEmailList.setExternaluser(externalUser);
                            System.out.println("User test email for External: " + userEmailList.getEmail());
                            if (notifications.getNotificationType() != null && notifications.getNotificationType().contains("News") && userEmailList.getExternaluser().getSubscribenews().equalsIgnoreCase("Y")) {
                                System.out.println("This is for news");
                                sendEmailTOAllUsers(mailDTO, notifications, userEmailList);
                            } else if (notifications.getNotificationType() != null && notifications.getNotificationType().contains("Events") && userEmailList.getExternaluser().getSubscribeevents().equalsIgnoreCase("Y")) {
                                System.out.println("This is for Events");
                                sendEmailTOAllUsers(mailDTO, notifications, userEmailList);
                            } else if (notifications.getNotificationType() != null && notifications.getNotificationType().contains("Information") && userEmailList.getExternaluser().getSubscribenotifications().equalsIgnoreCase("Y")) {
                                System.out.println("This is for Information");
                                sendEmailTOAllUsers(mailDTO, notifications, userEmailList);
                            }
                            notifications.setNotificationStatus("CLOSE");
                            notificationService.saveNotification(notifications);
                        }
                        System.out.println("Email Notification scheduler Done for External:");
                    }
                }

                /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date now = new Date();
                String strDate = sdf.format(now);
                System.out.println("Fixed Rate scheduler:: " + strDate);*/
            }
        }
    }

}
