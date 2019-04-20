package com.dw.ngms.cis.uam.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.enums.Status;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;

import com.dw.ngms.cis.uam.dto.InternalUserRoleDTO;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.service.InternalUserRoleService;
import com.dw.ngms.cis.uam.service.InternalUserService;
import com.dw.ngms.cis.uam.storage.StorageService;
import com.dw.ngms.cis.uam.utilities.Constants;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/03/28.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class InternalUserRoleController extends MessageController {

    @Autowired
    private InternalUserService internalUserService;
    @Autowired
    private InternalUserRoleService internalUserRoleService;
    @Autowired
    private StorageService testService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/registerInternalUserRole")
    public ResponseEntity<?> createInternalUserRole(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRoleDTO) {
        try {

            TaskController taskController = new TaskController();
            Task task = new Task();
            InternalRole internalRole = null;
            System.out.println("Internal Role Code " + internalUserRoleDTO.getProvinceCode());
            InternalUserRoles internalUserRoles = new InternalUserRoles();
            internalUserRoles.setUserCode(internalUserRoleDTO.getUserCode());
            internalUserRoles.setUserName(internalUserRoleDTO.getUserName());
            internalUserRoles.setProvinceCode(internalUserRoleDTO.getProvinceCode());
            internalUserRoles.setProvinceName(internalUserRoleDTO.getProvinceName());
            internalUserRoles.setSectionCode(internalUserRoleDTO.getSectionCode());
            internalUserRoles.setSectionName(internalUserRoleDTO.getSectionName());
            internalUserRoles.setRoleCode(internalUserRoleDTO.getRoleCode());
            internalUserRoles.setRoleName(internalUserRoleDTO.getRoleName());
            internalUserRoles.setIsActive(internalUserRoleDTO.getIsActive());
            internalUserRoles.setCreateddate(new Date());

            if(!StringUtils.isEmpty(internalUserRoles.getProvinceCode()) && !StringUtils.isEmpty(internalUserRoles.getSectionCode())) {
                 internalRole = this.internalUserService.createInternalRoleCode(internalUserRoles.getProvinceCode(), internalUserRoles.getSectionCode(), internalUserRoles.getRoleCode());
            }
            if(StringUtils.isEmpty(internalUserRoles.getProvinceCode()) && StringUtils.isEmpty(internalUserRoles.getSectionCode())){
                internalRole = this.internalUserService.createInternalRoleCodeWithNullSectionCodeProvinceCode( internalUserRoles.getRoleCode());
            }
            if(StringUtils.isEmpty(internalUserRoles.getSectionCode()) && !StringUtils.isEmpty(internalUserRoles.getProvinceCode())){
                 internalRole = this.internalUserService.createInternalRoleCodeWithNullSectionCode(internalUserRoles.getProvinceCode(), internalUserRoles.getRoleCode());
            }

            if(isEmpty(internalRole)){
                  return generateEmptyResponse(request, "Internal Role is empty with province code "+internalUserRoles.getProvinceCode() + "and Role code "+internalUserRoles.getRoleCode());
            }
            internalUserRoles.setInternalRoleCode(internalRole.getInternalRoleCode());
            InternalUserRoles savedResponse = internalUserService.saveInternalUserRole(internalUserRoles);
            //createTask(task);
            //taskController.createTask(request, task);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsercode(internalUserRoleDTO.getUserCode());
            System.out.println("user code in Internal user roles saved "+userDTO.getUsercode());
            User user = this.userService.findByUserCode(userDTO);



            if (!isEmpty(user) && user != null) {
               // user.setIsApproved(ApprovalStatus.PENDING);
                this.userService.saveInternalUser(user);
            }
            return ResponseEntity.status(HttpStatus.OK).body(savedResponse);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }


    }//createInternalUserRole



    @RequestMapping(value = "/deactivateUserRole", method = RequestMethod.POST)
    public ResponseEntity<?> deactivateUserRole(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRoleDTO) throws IOException {
        try {
            InternalUserRoles internalUserRoles = this.internalUserService.findByUserByNameAndCode(internalUserRoleDTO.getUserCode(), internalUserRoleDTO.getUserName(),internalUserRoleDTO.getInternalRoleCode());
            if (isEmpty(internalUserRoles)) {
                return generateEmptyResponse(request, "Internal User Roles not found");
            }
            if (!isEmpty(internalUserRoles)) {
                internalUserRoles.setIsActive(internalUserRoleDTO.getIsActive());
            }
            this.internalUserService.saveInternalUserRole(internalUserRoles);
            return ResponseEntity.status(HttpStatus.OK).body("Internal Roles DeActivated Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deactivateUser

    @PostMapping("/uploadSignedUserAccess")
    public ResponseEntity<?> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                              @RequestParam("userCode") String userCode,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("provinceCode") String provinceCode,
                                              @RequestParam("provinceName") String provinceName,
                                              @RequestParam("sectionCode") String sectionCode,
                                              @RequestParam("sectionName") String sectionName,
                                              @RequestParam("roleCode") String roleCode,
                                              @RequestParam("roleName") String roleName,
                                              @RequestParam("internalRoleCode") String internalRoleCode,
                                              @RequestParam("isActive") String isActive

    ) {
        String message = "";
        List<String> files = new ArrayList<String>();
        try {
            if (file.isEmpty()) {
                return generateEmptyResponse(request, "File Not Found to upload, Please upload a file");
            } else {
                InternalUserRoles internalUserRoles;
                if(sectionCode!= null && !isEmpty(sectionCode) ) {
                     internalUserRoles = this.internalUserRoleService.getInternalUserRoleCode(userCode, userName, provinceCode, sectionCode, roleCode, internalRoleCode);
                }else{
                    internalUserRoles = this.internalUserRoleService.getInternalUserRoleCodeWithEmptySectionCode(userCode, userName, provinceCode, roleCode, internalRoleCode);
                }
                if (internalUserRoles != null && internalUserRoles.getUserRoleId() != null) {
                    String fileName = testService.store(file);
                    files.add(file.getOriginalFilename());
                    internalUserRoles.setSignedAccessDocPath(Constants.uploadDirectoryPath + fileName);
                    message = "You successfully uploaded " + internalUserRoles.getSignedAccessDocPath() + "!";
                    internalUserService.saveInternalUserRole(internalUserRoles);
                    return ResponseEntity.status(HttpStatus.OK).body(message);
                } else {
                    return generateEmptyResponse(request, "No Internal User Roles  found");
                }

            }

        } catch (Exception exception) {
            message = "FAIL to upload " + file.getOriginalFilename() + "!";
            return generateFailureResponse(request, exception);
        }
    }//handleFileUpload

   /* @PostMapping("/downloadSignedUserAccess")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRoles) throws IOException {
        // Load file from database
       // if (internalUserRoles.getUserName() != null && internalUserRoles.getUserCode() != null) {
            InternalUserRoles ir = this.internalUserService.findByUserByNameAndCode(internalUserRoles.getUserCode(), internalUserRoles.getUserName(),internalUserRoles.getInternalRoleCode());
            System.out.println("Internal User Roles one " + ir.getSignedAccessDocPath());
            int index = ir.getSignedAccessDocPath().lastIndexOf("/");
            String fileName = ir.getSignedAccessDocPath().substring(index + 1);
            System.out.println("File Name is " + fileName);
            File file = new File(ir.getSignedAccessDocPath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String exportedContent = resource.getInputStream().toString();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
            headers.set("Content-Disposition", "attachment; filename=" + fileName);
            headers.set("Content-Type", "application/pdf");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment;filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                    .body(resource);
            //return new ResponseEntity<String>(exportedContent, headers, HttpStatus.OK);

       // }


    }//downloadFile

*/




    @RequestMapping(value = "/downloadSignedUserAccess", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> downloadFile1(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRoles) throws IOException {

        InternalUserRoles ir = this.internalUserService.findByUserByNameAndCode(internalUserRoles.getUserCode(), internalUserRoles.getUserName(),internalUserRoles.getInternalRoleCode());
        System.out.println("Internal User Roles one " + ir.getSignedAccessDocPath());
        int index = ir.getSignedAccessDocPath().lastIndexOf("/");
        String fileName = ir.getSignedAccessDocPath().substring(index + 1);
        System.out.println("File Name is " + fileName);
        File file = new File(ir.getSignedAccessDocPath());

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

    @GetMapping("/getInternalUserRolesByEmail")
    public ResponseEntity<?> getInternalUserRolesByEmail(HttpServletRequest request, @RequestParam String email) {
        try {
            List<InternalUserRoles> internalUserRoles = internalUserRoleService.getInternalUserRole(email);
            return (CollectionUtils.isEmpty(internalUserRoles)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserRoles)
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserRoles);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getInternalUserRolesByEmail

    @PostMapping("/deleteInternalUserRole")
    public ResponseEntity<?> deleteInternalUserRoles(HttpServletRequest request, @RequestBody @Valid InternalUserRoleDTO internalUserRole) {
        try {
            internalUserRoleService.deleteInternalUserRole(internalUserRole.getUserCode(),
                    internalUserRole.getUserName(), internalUserRole.getInternalRoleCode());
            return ResponseEntity.status(HttpStatus.OK).body("Successful");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteInternalUserRoles



    /*private void createTask(Task task) throws IOException {
        Long taskId = this.taskService.getTaskID();
        System.out.println("task id is" +taskId);
        task.setTaskCode("TASK000" + Long.toString(taskId));
        Task taskService = this.taskService.saveTask(task);
        //MailDTO mailDTO = getMailDTO(taskService);
        //sendMailToTaskUser(taskService, mailDTO);
    }
*/

    private MailDTO getMailDTO(@RequestBody @Valid Task task) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setHeader(ExceptionConstants.header);
        mailDTO.setFooter(ExceptionConstants.footer);
        mailDTO.setSubject(ExceptionConstants.subject);
        return mailDTO;
    }


    private void sendMailToTaskUser(@RequestBody @Valid Task task, MailDTO mailDTO) throws IOException {
        String mailResponse = null;
        String userCode = null;

        System.out.println("Province Code "+task.getTaskAllProvinceCode() +"section Code " +task.getTaskAllOCSectionCode() +"taskCode "+task.getTaskAllOCRoleCode());
        List<InternalUserRoles> userRolesList = this.internalUserRoleService.getInternalUserName(task.getTaskAllProvinceCode(),task.getTaskAllOCSectionCode(),task.getTaskAllOCRoleCode());
        System.out.println("user code is " +userRolesList.get(0).getUserCode());
        System.out.println("user name is " +userRolesList.get(0).getUserName());
        for(InternalUserRoles user: userRolesList){
            System.out.println("user code are " +user.getUserCode());
        }
        String userName = this.userService.getUserName(userRolesList.get(0).getUserCode());
        mailDTO.setHeader(ExceptionConstants.header + " " + userName + ",");
        mailDTO.setSubject("New Task Created");
        mailDTO.setBody1("New Task have been created for you.");
        mailDTO.setBody2("Task type is " +task.getTaskReferenceType());
        mailDTO.setBody3("");
        mailDTO.setBody4("");;
        mailDTO.setToAddress(userRolesList.get(0).getUserName());//admin user for later
        mailResponse = sendMail(mailDTO);
        System.out.println("mailResponse is "+mailResponse);
    }

}
