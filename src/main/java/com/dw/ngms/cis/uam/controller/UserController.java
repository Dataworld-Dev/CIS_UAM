package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;

import com.dw.ngms.cis.uam.dto.*;
import com.dw.ngms.cis.uam.entity.*;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.enums.Status;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.ldap.UserCredentials;
import com.dw.ngms.cis.uam.service.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class UserController extends MessageController {

    private static final String INTERNAL_USER_TYPE_NAME = "INTERNAL";
    private static final String EXTERNAL_USER_TYPE_NAME = "EXTERNAL";

    @Autowired
    private UserService userService;

    @Autowired
    private ExternalRoleService externalRoleService;


    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private InternalRoleService internalRoleService;

    @Autowired
    private ExternalUserAssistantService externalUserAssistantService;

    @Autowired
    private InternalUserRoleService internalUserRoleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @PostMapping("/checkADUserExists")
    public ResponseEntity<?> isADUserExists(HttpServletRequest request, @RequestBody @Valid UserCredentials userCredentials) {
        if (userCredentials == null || StringUtils.isEmpty(userCredentials.getUsername()) ||
                StringUtils.isEmpty(userCredentials.getPassword())) {
            return generateFailureResponse(request, new Exception("Invalid credentials passed"));
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.isADUserExists(userCredentials.getUsername(), userCredentials.getPassword()));
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//isADUserExists

    @PostMapping("/submitInternalUserForApproval")
    public ResponseEntity<?> submitInternalUserForApproval(HttpServletRequest request, @RequestBody @Valid UserDTO userDto) {
        if (userDto == null || userDto.getUsercode() == null ||
                userDto.getUsername() == null || userDto.getIsapproved() == null) {
            return generateFailureResponse(request, new Exception("Required params missing"));
        }

        try {
            User user = userService.submitInternalUserForApproval(userDto.getUsercode(), userDto.getUsername(),
                    INTERNAL_USER_TYPE_NAME, ApprovalStatus.valueOf(userDto.getIsapproved()));
            return (user == null) ? generateEmptyResponse(request, "User(s) not found") :
                    ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//submitInternalUserForApproval

    @GetMapping("/getAllInternalUsers")
    public ResponseEntity<?> getAllInternalUsers(HttpServletRequest request, @RequestParam String provincecode) {

        try {
            List<User> userList = new ArrayList<>();

            if (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) {
                userList = userService.getAllUsersByUserTypeName(INTERNAL_USER_TYPE_NAME);

                for (User userInfo : userList) {
                    List<InternalUserRoles> interUserRolesList = new ArrayList<>();
                    List<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternalWithProvinceCodeNotNull(userInfo.getUserCode());
                    if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                        for (InternalUserRoles in : internalUserRoles) {
                            System.out.println("Internal user roles" + in.getInternalRoleCode());
                            InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                            internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                            internalUserRoles1.setRoleCode(in.getRoleCode());
                            internalUserRoles1.setRoleName(in.getRoleName());
                            internalUserRoles1.setProvinceCode(in.getProvinceCode());
                            internalUserRoles1.setProvinceName(in.getProvinceName());
                            internalUserRoles1.setSectionCode(in.getSectionCode());
                            internalUserRoles1.setSectionName(in.getSectionName());
                            internalUserRoles1.setUserRoleId(in.getUserRoleId());
                            internalUserRoles1.setIsActive(in.getIsActive());
                            internalUserRoles1.setUserName(in.getUserName());
                            internalUserRoles1.setCreateddate(in.getCreateddate());
                            internalUserRoles1.setSignedAccessDocPath(in.getSignedAccessDocPath());
                            interUserRolesList.add(internalUserRoles1);
                            userInfo.setInternalUserRoles(interUserRolesList);
                        }
                    } else {
                        userInfo.setInternalUserRoles(new ArrayList<InternalUserRoles>());
                    }
                }
            } else {
                userList = userService.getAllInternalUsersByProvinceCode(provincecode);

                for (User userInfo : userList) {
                    ArrayList<InternalUserRoles> interUserRolesList = new ArrayList<>();
                    List<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternal(userInfo.getUserCode());
                    if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                        for (InternalUserRoles in : internalUserRoles) {
                            System.out.println("Internal user roles" + in.getInternalRoleCode());
                            InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                            internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                            internalUserRoles1.setRoleCode(in.getRoleCode());
                            internalUserRoles1.setRoleName(in.getRoleName());
                            internalUserRoles1.setProvinceCode(in.getProvinceCode());
                            internalUserRoles1.setProvinceName(in.getProvinceName());
                            internalUserRoles1.setSectionCode(in.getSectionCode());
                            internalUserRoles1.setSectionName(in.getSectionName());
                            internalUserRoles1.setUserRoleId(in.getUserRoleId());
                            internalUserRoles1.setIsActive(in.getIsActive());
                            internalUserRoles1.setUserName(in.getUserName());
                            internalUserRoles1.setCreateddate(in.getCreateddate());
                            internalUserRoles1.setSignedAccessDocPath(in.getSignedAccessDocPath());
                            interUserRolesList.add(internalUserRoles1);
                            userInfo.setInternalUserRoles(interUserRolesList);
                        }
                    } else {
                        userInfo.setInternalUserRoles(new ArrayList<InternalUserRoles>());
                    }
                }
            }

            return (CollectionUtils.isEmpty(userList)) ? ResponseEntity.status(HttpStatus.OK).body(userList)
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllInternalUsers


    @GetMapping("/getAllExternalUsers")
    public ResponseEntity<?> getAllExternalUsers(HttpServletRequest request, @RequestParam String provincecode) {
        try {
            List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ?
                    userService.getAllUsersByUserTypeName(EXTERNAL_USER_TYPE_NAME) :
                    userService.getAllExternalUsersByProvinceCode(provincecode);
            return (CollectionUtils.isEmpty(userList)) ? ResponseEntity.status(HttpStatus.OK).body(userList)
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers





    @PostMapping("/updateExternalUser")
    public ResponseEntity<?> updateExternalUser(HttpServletRequest request, @RequestBody UserUpdateDTO externalUserDTO) {
        try {
            if (externalUserDTO.getUserTypeName().equalsIgnoreCase("EXTERNAL")) {
                User externalUser = this.userService.updateUser(externalUserDTO);
                return (externalUser == null) ? generateEmptyResponse(request, "Failed to update external user") :
                        ResponseEntity.status(HttpStatus.OK).body("Update Successful");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Not a valid External User");
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateExternalUser

    @PostMapping("/updateInternalUser")
    public ResponseEntity<?> updateInternalUser(HttpServletRequest request, @RequestBody UserUpdateDTO externalUserDTO) {
        try {
            if (externalUserDTO.getUserTypeName().equalsIgnoreCase("INTERNAL")) {
                System.out.println("User Code " + externalUserDTO.getUserCode());
                User internalUser = this.userService.updateInternalUser(externalUserDTO);
                return (internalUser == null) ? generateEmptyResponse(request, "Failed to update internal user") :
                        ResponseEntity.status(HttpStatus.OK).body("Update Successful");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Not a valid Internal User");
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateInternalUser


    @GetMapping("/getAccessRightsByCode")
    public ResponseEntity<?> getAccessRightsByCode(HttpServletRequest request, @RequestParam String userType,
                                                   @RequestParam String code) {
        try {
            if (userType.equalsIgnoreCase("Internal")) {
                String accessRightJson = this.internalRoleService.getAccessRightJson(code);
                return (accessRightJson == null) ? generateEmptyResponse(request, "Access Right Json not found")
                        : ResponseEntity.status(HttpStatus.OK).body(accessRightJson);
            } else if (userType.equalsIgnoreCase("External")) {

                String accessRightJson = this.externalRoleService.getAccessRightJson(code);
                return (accessRightJson == null) ? generateEmptyResponse(request, "Access Right Json not found")
                        : ResponseEntity.status(HttpStatus.OK).body(accessRightJson);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Access Right Json not found");
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers


    @PostMapping("/updateAccessRights")
    public ResponseEntity<?> updateAccessRights(HttpServletRequest request, @RequestBody @Valid UpdateAccessRightsDTO updateAccessRightsDTO) {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String json = null;
            System.out.println("User Type is " + updateAccessRightsDTO.getUsertype());
            if (updateAccessRightsDTO.getUsertype().equalsIgnoreCase("Internal")) {
                for (RolesDTO roles : updateAccessRightsDTO.getRoles()) {
                    if (!isEmpty(roles)) {
                        List<InternalRole> internalRoleList = this.internalRoleService.updateAccessRight(roles.getRolecode());
                        if (!isEmpty(internalRoleList)) {
                            for (InternalRole internalRole : internalRoleList) {
                                internalRole.setAccessRightJson(updateAccessRightsDTO.getAccessrightjson());
                                this.internalRoleService.updateInternalRole(internalRole);
                            }
                            userControllerResponse.setMessage("updated Rights access successfully");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(json);
                        } else {
                            return generateEmptyResponse(request, "No roles found");
                        }

                    } else {
                        return generateEmptyResponse(request, "No roles found");
                    }
                }
            } else {
                for (RolesDTO roles : updateAccessRightsDTO.getRoles()) {
                    if (!isEmpty(roles)) {
                        List<ExternalRole> externalRoleList = this.externalRoleService.updateAccessRight(roles.getRolecode());
                        if (!isEmpty(externalRoleList)) {
                            for (ExternalRole externalRole : externalRoleList) {
                                externalRole.setAccessRightJson(updateAccessRightsDTO.getAccessrightjson());
                                this.externalRoleService.updateExternalRole(externalRole);
                            }
                            userControllerResponse.setMessage("updated Rights access successfully");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(json);
                        } else {
                            return generateEmptyResponse(request, "No roles found");
                        }
                    }
                }
            }
            return generateEmptyResponse(request, "No roles found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }
    //updateAccessRights

    //

    @GetMapping("/getUsersForPendingApproval")
    public ResponseEntity<?> getUsersForPendingApproval(HttpServletRequest request, @RequestParam String provincecode) {
        try {
            List<User> userList = (StringUtils.isEmpty(provincecode) || "all".equalsIgnoreCase(provincecode.trim())) ?
                    userService.getAllExternalApprovalPendingUsers(ApprovalStatus.PENDING.name(), ApprovalStatus.YES.name()) :
                    userService.getAllExternalApprovalPendingUsersByProvinceCode(ApprovalStatus.PENDING.name(), ApprovalStatus.YES.name(), provincecode);
            return (CollectionUtils.isEmpty(userList)) ? generateEmptyResponse(request, "User(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUsersForPendingApproval


    @GetMapping("/getMyAssistants")
    public ResponseEntity<?> getSurveyorAssistants(HttpServletRequest request, @RequestParam String surveyorusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(surveyorusercode)) {
                userList = userService.getAllAssistantsBySurveyorUserCode(surveyorusercode);
            }
            return (CollectionUtils.isEmpty(userList)) ? ResponseEntity.status(HttpStatus.OK).body(userList)
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSurveyorAssistants

    @GetMapping("/getMySurveyors")
    public ResponseEntity<?> getAssistantsSurveyor(HttpServletRequest request, @RequestParam String assistantusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(assistantusercode)) {
                userList = userService.getAllSurveyorsByAssistantsUserCode(assistantusercode);
            }
            return (CollectionUtils.isEmpty(userList)) ? ResponseEntity.status(HttpStatus.OK).body(userList)
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAssistantsSurveyor


    @GetMapping("/getAssistantsForPendingApproval")
    public ResponseEntity<?> getAssistantsForPendingApproval(HttpServletRequest request, @RequestParam String surveyorusercode) {
        try {
            List<User> userList = null;
            if (!StringUtils.isEmpty(surveyorusercode)) {
                userList = userService.getAllAssistantsForPendingApprovalBySurveyorUserCode(ApprovalStatus.WAITING.name(), ApprovalStatus.PENDING.name(), surveyorusercode);
            }
            return (CollectionUtils.isEmpty(userList)) ? ResponseEntity.status(HttpStatus.OK).body(userList)
                    : ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAssistantsForPendingApproval

    @GetMapping("/checkUserExist")
    public ResponseEntity<?> checkUserExistsInDB(HttpServletRequest request, @RequestParam String email) {
        User userExists = null;
        String exists = null;
        String json = null;
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            userExists = this.userService.findByEmail(email.trim().toLowerCase());
            if (userExists == null) {
                userControllerResponse.setExists("false");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }

            if (userExists.getUserName() != null) {
                userControllerResponse.setExists("true");
                json = gson.toJson(userControllerResponse);
            } else {
                userControllerResponse.setExists("false");
                json = gson.toJson(userControllerResponse);
            }

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }//checkUserExistsInDB


    @GetMapping("/getUserInfoByEmail")
    public ResponseEntity<?> getUserInfoByMail(HttpServletRequest request, @RequestParam String email) {

        System.out.println("email is " + email);
        try {

            User userInfo = this.userService.findByEmail(email.trim().toLowerCase());
            if (!isEmpty(userInfo) && userInfo != null && userInfo.getUserTypeName().equalsIgnoreCase("EXTERNAL")) {
                ExternalUser externalUser = this.userService.getChildElements(userInfo.getUserCode());
                List<ExternalUserRoles> externalUserRolesList = this.userService.getExternalUserRolesChildElements(userInfo.getUserCode());
                userInfo.setExternaluser(externalUser);
                userInfo.setExternalUserRoles(externalUserRolesList);
                if (!CollectionUtils.isEmpty(externalUserRolesList) && externalUserRolesList != null) {
                    userInfo.setMainRoleCode(externalUserRolesList.get(0).getUserRoleCode());
                    userInfo.setMainRoleName(externalUserRolesList.get(0).getUserRoleName());
                }
            } else if (!isEmpty(userInfo) && userInfo != null && userInfo.getUserTypeName().equalsIgnoreCase("INTERNAL")) {
                System.out.println("User code is: " + userInfo.getUserCode());
                List<InternalUserRoles> interUserRolesList = new ArrayList<>();
                List<InternalUserRoles> internalUserRoles = this.internalUserRoleService.getChildElementsInternalWithActive(userInfo.getUserCode());
                if (!isEmpty(internalUserRoles) && internalUserRoles != null) {
                    for (InternalUserRoles in : internalUserRoles) {
                        System.out.println("Internal user roles" + in.getInternalRoleCode());
                        InternalUserRoles internalUserRoles1 = new InternalUserRoles();
                        internalUserRoles1.setInternalRoleCode(in.getInternalRoleCode());
                        internalUserRoles1.setUserCode(in.getUserCode());
                        internalUserRoles1.setRoleCode(in.getRoleCode());
                        internalUserRoles1.setRoleName(in.getRoleName());
                        internalUserRoles1.setProvinceCode(in.getProvinceCode());
                        internalUserRoles1.setProvinceName(in.getProvinceName());
                        internalUserRoles1.setSectionCode(in.getSectionCode());
                        internalUserRoles1.setSectionName(in.getSectionName());
                        internalUserRoles1.setUserRoleId(in.getUserRoleId());
                        internalUserRoles1.setIsActive(in.getIsActive());
                        internalUserRoles1.setUserName(in.getUserName());
                        internalUserRoles1.setSignedAccessDocPath(in.getSignedAccessDocPath());
                        internalUserRoles1.setCreateddate(in.getCreateddate());
                        interUserRolesList.add(internalUserRoles1);
                        userInfo.setInternalUserRoles(interUserRolesList);
                    }
                }
            }
            return (isEmpty(userInfo)) ? generateEmptyWithOKResponse(request, "Users not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUserInfoByMail


    @RequestMapping(value = "/deleteExternalUser", method = RequestMethod.POST)
    public ResponseEntity<?> deleteAssistant(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
        try {
            User user = this.userService.findByUserCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users are  not found");
            }
            if (!isEmpty(user)) {
                ExternalUser externalUser = this.userService.getChildElements(userDTO.getUsercode());
                user.setExternaluser(externalUser);
                this.userService.deleteUserAndChild(user);
            }
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }

    @PostMapping(value = "/registerExternalUser", produces = "application/json")
    public ResponseEntity<?> createExternalUser(HttpServletRequest request, @Valid @RequestBody User user) {
        Gson gson = new Gson();
        Task task = new Task();
        String json = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            System.out.println("Email is " + user.getEmail());
            User userExists = this.userService.findByEmail(user.getEmail().trim().toLowerCase());
            if (userExists != null && userExists.getEmail() != null) {
                userControllerResponse.setMessage("User Already Exist with this email ID");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            Long userID = this.userService.getUserId();
            System.out.println("UserId is " + userID);
            mapUserDetails(user, userID);
            if (!user.getExternalUserRoles().isEmpty()) {
                ArrayList<ExternalUserRoles> externalRoleCode = new ArrayList<>();
                for (ExternalUserRoles externalUserRoles : user.getExternalUserRoles()) {
                    if (user.getMainRoleCode().equalsIgnoreCase("EX011")) {
                        String response = saveExternalUserAssistant(user, externalRoleCode, externalUserRoles);
                        if (response.equals("failed")) {
                            userControllerResponse.setMessage("Surveyor is not registered with CIS");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(json);
                        }
                    } else if (user.getMainRoleCode().equalsIgnoreCase("EX010") || user.getMainRoleCode().equalsIgnoreCase("EX002")) {
                        //String response = saveExternalUserAssistantForPLSExUser(user, externalRoleCode, externalUserRoles);
                        String ppNumber = this.userService.getpPNumber(user.getExternaluser().getPpno());
                        if (ppNumber == null || ppNumber.length() == 0) {
                            ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(user.getMainRoleCode(), externalUserRoles.getUserProvinceCode());
                            externalUserRolesMapping(user, externalUserRoles, externalRole);
                            externalUserRoles.setUserRoleName(user.getMainRoleName());
                            externalUserRoles.setUserRoleCode(user.getMainRoleCode());
                            externalRoleCode.add(externalUserRoles);
                        } else if (ppNumber != null || ppNumber.length() > 0) {
                            userControllerResponse.setMessage("PLS user already Registered with this PPN NO");
                            json = gson.toJson(userControllerResponse);
                            return ResponseEntity.status(HttpStatus.OK).body(json);
                        }
                    } else {
                        System.out.println("Main Role Code " + user.getMainRoleCode());
                        ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(user.getMainRoleCode(), externalUserRoles.getUserProvinceCode());
                        externalUserRolesMapping(user, externalUserRoles, externalRole);
                        externalUserRoles.setUserRoleName(user.getMainRoleName());
                        externalUserRoles.setUserRoleCode(user.getMainRoleCode());
                        externalRoleCode.add(externalUserRoles);
                    }
                }
            }
            User response = userService.saveExternalUser(user);

            MailDTO mailDTO = new MailDTO();

            // inside your getSalesUserData() method
            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
            emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMailToUser(user, mailDTO);
                        sendMailToAdmin(user, mailDTO);
                        sendMailToProvinceAdmin(user, mailDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.




            //sendSMS(user.getMobileNo(),message);
            //sendSMS(user.getMobileNo(),message); //todo for provincial administrator
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createExternalUser


    @GetMapping(value = "/mailTest", produces = "application/json")
    public ResponseEntity<?> mailTest(HttpServletRequest request) throws Exception {


        MailDTO mail = new MailDTO();
        mail.setMailFrom("javabycod@gmail.com");
        mail.setMailTo("swaroopeswara@gmail.com");
        mail.setMailSubject("User Registered");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", "swaroop");
        model.put("lastName", "eswara");
        model.put("body1", "Thank you for registering with us. Your account is");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        model.put("FOOTER", "Ragava");

        mail.setModel(model);

        sendEmail(mail);
        //sendMailToAdmin(user, mailDTO);
        //sendMailToProvinceAdmin(user, mailDTO);

        return ResponseEntity.status(HttpStatus.OK).body("mail sent sucessfully");

    }//createExternalUser


    @GetMapping("/getUserInfoLite")
    public ResponseEntity<?> getUserInfoLite(HttpServletRequest request, @RequestParam String email) {
        System.out.println("email is " + email);
        try {

            User userInfo = this.userService.findByEmail(email.trim().toLowerCase());
            if (!isEmpty(userInfo) && userInfo != null) {
                UserLiteDTO userLiteDTO = new UserLiteDTO();
                userLiteDTO.setFirstName(userInfo.getFirstName());
                userLiteDTO.setUserName(userInfo.getUserName());
                userLiteDTO.setSurName(userInfo.getSurname());
                userLiteDTO.setMobileNumber(userInfo.getMobileNo());
                userLiteDTO.setUserCode(userInfo.getUserCode());
                userLiteDTO.setEmail(userInfo.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(userLiteDTO);
            }
            return (isEmpty(userInfo)) ? generateEmptyWithOKResponse(request, "Users not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getUserInfoLite


    @PostMapping("/registerInternalUser")
    public ResponseEntity<?> createInternalUser(HttpServletRequest request, @RequestBody @Valid User internalUser) {
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        String json = null;
        try {
            System.out.println("internalUser.getEmail() " + internalUser.getEmail());
            User userExists = this.userService.findByEmail(internalUser.getEmail().trim().toLowerCase());
            if (userExists != null && userExists.getEmail() != null) {
                userControllerResponse.setMessage("User Already Exist with this email ID");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            Long userID = this.userService.getUserId();
            internalUser.setUserId(userID);
            internalUser.setUserCode("USR000" + Long.toString(internalUser.getUserId()));
            internalUser.setEmail(internalUser.getEmail().trim());
            User response = userService.saveInternalUser(internalUser);

            MailDTO mailDTO = new MailDTO();

            // inside your getSalesUserData() method
            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
            emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMailToInternalUser(internalUser, mailDTO);
                        sendMailToInternalAdmin(internalUser, mailDTO);
                        sendMailToProvinceInternalAdmin(internalUser, mailDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            emailExecutor.shutdown(); // it is very important to shutdown your non-singleton ExecutorService.





            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//saveInternalUserm



    @GetMapping("/sendEmailTest")
    public ResponseEntity<?> sendEmailTest(HttpServletRequest request) {
        try {
            System.out.println("Inside the send email test: ");
            MailDTO mailDTO = new MailDTO();
            sendTestEmail(mailDTO);
            System.out.println("send email test done: ");
            return ResponseEntity.status(HttpStatus.OK).body("Mail Sent Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers


    @RequestMapping(value = "/approveRejectUser", method = RequestMethod.POST)
    public ResponseEntity<?> approveRejectAssitant(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
        try {
            User user = this.userService.findByUserByNameAndCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(user)) {
                user.setIsApproved(ApprovalStatus.valueOf(userDTO.getIsapproved()));
                user.setRejectionReason(userDTO.getRejectionreason());
                user.setIsApprejDate(new Date());
            }
            this.userService.updateUserApproval(user);
            MailDTO mailDTO = new MailDTO();
            sendMailToUser(user, mailDTO);
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("User Approval Updated Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//approveRejectAssitant


    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseEntity<?> updatePassword(HttpServletRequest request, @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) throws IOException {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String json = null;
            UserDTO userDTO = new UserDTO();
            userDTO.setUsercode(updatePasswordDTO.getUsercode());
            userDTO.setUsername(updatePasswordDTO.getUsername());
            User user = this.userService.findByUserByNameAndCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(user)) {
                if (updatePasswordDTO.getType().equalsIgnoreCase("change")) {
                    if (user.getPassword().equalsIgnoreCase(updatePasswordDTO.getOldpassword())) {
                        user.setPassword(updatePasswordDTO.getNewpassword());
                        user.setFirstLogin(updatePasswordDTO.getFirstlogin());
                        sendPasswordChangeMailToUser(user);
                    } else {
                        userControllerResponse.setMessage("Old Password and new password do not match");
                        json = gson.toJson(userControllerResponse);
                        return ResponseEntity.status(HttpStatus.OK).body(json);
                    }
                } else if (updatePasswordDTO.getType().equalsIgnoreCase("reset")) {
                    user.setPassword(updatePasswordDTO.getNewpassword());
                    user.setFirstLogin(updatePasswordDTO.getFirstlogin());
                    sendPasswordResetMailToUser(user);
                }
            }
            this.userService.updatePassword(user);
            userControllerResponse.setMessage("User Password Updated Successfully");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updatePassword*/

    private void sendPasswordResetMailToUser(User user) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            MailDTO mailDTO = new MailDTO();
            mailDTO.setMailSubject("User password reset");

            model.put("body1", "Your password is reset Successfully");
            model.put("body2", "Your password is " + user.getPassword());
            model.put("body3", "");
            model.put("body4", "");
            model.put("firstName", user.getFirstName() + ",");
            model.put("FOOTER", "CIS ADMIN");
            mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
            mailDTO.setMailTo(user.getEmail());
            mailDTO.setModel(model);
            sendEmail(mailDTO);
        } catch (Exception e) {
            log.error("Error while sending mail {}", e.getMessage());
        }
    }//sendPasswordChangeMailToUser


    private void sendPasswordChangeMailToUser(User user) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            MailDTO mailDTO = new MailDTO();
            mailDTO.setMailSubject("User password updated");

            model.put("body1", "Your password is updated Successfully");
            model.put("body2", "Your password is " + user.getPassword());
            model.put("body3", "");
            model.put("body4", "");
            model.put("firstName", user.getFirstName() + ",");
            model.put("FOOTER", "CIS ADMIN");
            mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
            mailDTO.setMailTo(user.getEmail());
            mailDTO.setModel(model);
            sendEmail(mailDTO);
        } catch (Exception e) {
            log.error("Error while sending mail {}", e.getMessage());
        }
    }//sendPasswordChangeMailToUser


    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String json = null;
            User user = this.userService.findByUserCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(user)) {
                final int SHORT_ID_LENGTH = 8;
                user.setPassword(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
                user.setFirstLogin("Y");
                this.userService.updatePassword(user);
                userControllerResponse.setMessage("User Password Reset Successfully");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            userControllerResponse.setMessage("Reset Password is UnSuccessful");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//resetPassword*/


    @RequestMapping(value = "/deactivateUser", method = RequestMethod.POST)
    public ResponseEntity<?> deactivateUser(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {
        try {
            User user = this.userService.findByUserByNameAndCode(userDTO);
            if (isEmpty(user)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(user)) {
                user.setIsActive(Status.valueOf(userDTO.getIsactive())); //todo test later with srinivas garu
            }
            this.userService.updateUserApproval(user);
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("User DeActivated Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deactivateUser


    private void mapUserDetails(@RequestBody @Valid User user, Long userID) {
        user.setUserId(userID);
        user.setUserCode("USR000" + Long.toString(user.getUserId()));
        user.getExternaluser().setUserId(user.getUserId());
        user.getExternaluser().setUsercode(user.getUserCode());
        user.getExternaluser().setCreatedDate(new Date());
        user.setEmail(user.getEmail().trim());
        user.setUserName(user.getUserName().trim());
        final int SHORT_ID_LENGTH = 8;
        user.setPassword(RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH));
        user.setFirstLogin("Y");
    }


    private void sendMailToUser(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        if (user.getIsApproved().getDisplayString().equalsIgnoreCase("YES")) {
            model.put("firstName", user.getFirstName() + " " +user.getSurname());
            model.put("body1", "Thank you for registering with us. Your account is approved.");
            model.put("body2", "Your password is " + user.getPassword());
            model.put("body3", "");
            model.put("body4", "");

        } else {

            model.put("firstName", user.getFirstName() + " " +user.getSurname());
            model.put("body1", "Thank you for registering with us. Your account is pending approval.");
            model.put("body2", "Your password is " + user.getPassword());
            model.put("body3", "");
            model.put("body4", "");

        }

        mailDTO.setMailSubject("Welcome to CIS");
        model.put("FOOTER", "CIS ADMIN");
        //mailDTO.setFooter("CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(user.getEmail());

        mailDTO.setModel(model);
        sendEmail(mailDTO);


    }

    private void sendTestEmail(MailDTO mailDTO) throws Exception {
        System.out.println("send email test with body: ");
        Map<String, Object> model = new HashMap<String, Object>();
            model.put("body1", "This is test mail to test the SMPTP. Plz ignore the mail");
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");
        mailDTO.setMailSubject("Test Email");
        model.put("firstName",  "Test User ,");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo("swaroopeswara@gmail.com");
        mailDTO.setModel(model);
        System.out.println("send email test with body with to : "+mailDTO.getMailTo());
        sendEmail(mailDTO);
    }


    private void sendMailToInternalUser(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        if (user.getIsApproved().getDisplayString().equalsIgnoreCase("YES")) {
            model.put("body1", "Thank you for registering with us. Your account is approved.");
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");
        } else {
            model.put("body1", "Thank you for registering with us. Your account is pending approval.");
            model.put("body2", "");
            model.put("body3", "");
            model.put("body4", "");
        }



        mailDTO.setMailSubject("Welcome to CIS");
        model.put("firstName", " " + user.getFirstName() + " " +user.getSurname() + ",");
        model.put("FOOTER", "CIS ADMIN");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(user.getEmail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }


    private void sendMailToAdmin(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        mailDTO.setMailSubject("New " + user.getUserTypeName().toLowerCase() + " User Registration");
        model.put("firstName", " " + "Admin Name" + ",");
        model.put("FOOTER", "CIS ADMIN");
        model.put("body1", "New user registered with email " + user.getEmail() + " in province " + user.getExternalUserRoles().get(0).getUserProvinceName());
        model.put("body2", "New task created for approval by provincial administrator");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(applicationPropertiesConfiguration.getAdminUserMail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }


    private void sendMailToInternalAdmin(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        mailDTO.setMailSubject("New " + user.getUserTypeName().toLowerCase() + " User Registration");
        model.put("firstName", " " + "Admin Name" + ",");
        model.put("FOOTER", "CIS ADMIN");
        // mailDTO.setBody1("New user registered with email " +user.getEmail()+  " in province "+user.getInternalUserRoles().get(0).getProvinceName());
        model.put("body1", "New user registered with email " + user.getEmail());
        model.put("body2", "New task created for approval by provincial administrator");
        model.put("body3", "");
        model.put("body4", "");
        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(applicationPropertiesConfiguration.getAdminUserMail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }


    private void sendMailToProvinceAdmin(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        mailDTO.setMailSubject("New " + user.getUserTypeName().toLowerCase() + " User Registration");
        model.put("firstName", " " + "Province Administrator" + ",");
        model.put("FOOTER", "CIS ADMIN");

        model.put("body1", "New user registered with email " + user.getEmail() + " in province " + user.getExternalUserRoles().get(0).getUserProvinceName());
        model.put("body2", "New task created for approval by you");
        model.put("body3", "");
        model.put("body4", "");

        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(applicationPropertiesConfiguration.getProvinceAdminMail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }

    private void sendMailToProvinceInternalAdmin(@RequestBody @Valid User user, MailDTO mailDTO) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        mailDTO.setMailSubject("New " + user.getUserTypeName().toLowerCase() + " User Registration");
        model.put("firstName", " " + "Province Administrator" + ",");
        model.put("FOOTER", "CIS ADMIN");
        //mailDTO.setBody1("New user registered with email " +user.getEmail()+  " in province " +user.getInternalUserRoles().get(0).getProvinceName());

        model.put("body1", "New user registered with email " + user.getEmail());
        model.put("body2", "New task created for approval by you");
        model.put("body3", "");
        model.put("body4", "");


        mailDTO.setMailFrom(applicationPropertiesConfiguration.getMailFrom());
        mailDTO.setMailTo(applicationPropertiesConfiguration.getProvinceAdminMail());
        mailDTO.setModel(model);
        sendEmail(mailDTO);
    }


    private void externalUserRolesMapping(@RequestBody @Valid User user, ExternalUserRoles externalUserRoles, ExternalRole externalRole) {
        externalUserRoles.setExternalRoleCode(externalRole.getExternalRoleCode());
        Long userRoleID = this.externalUserService.getRoleId();
        System.out.println("userRoleID " + userRoleID);
        externalUserRoles.setUserRoleID(userRoleID);
        externalUserRoles.setUserCode(user.getUserCode());
        externalUserRoles.setUserId(user.getUserId());
        externalUserRoles.setUserName(user.getUserName());
        externalUserRoles.setUserRoleCode(user.getMainRoleCode());
        externalUserRoles.setUserRoleName(user.getMainRoleName());
    }


    private String saveExternalUserAssistant(@RequestBody @Valid User user, ArrayList<ExternalUserRoles> externalRoleCode, ExternalUserRoles externalUserRoles) {
        String response = "Success";
        ExternalUserAssistant externalUserAssistant = new ExternalUserAssistant();
        response = externalUserAssistantMapping(user, externalUserAssistant);
        if (response.equals("failed")) {
            return response = "failed";
        }
        ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(user.getMainRoleCode(), externalUserRoles.getUserProvinceCode());
        externalUserRolesMapping(user, externalUserRoles, externalRole);
        externalRoleCode.add(externalUserRoles);
        ExternalUserAssistant externalUserAssistant1 = this.externalUserAssistantService.saveExternalAssistant(externalUserAssistant);
        //// TODO: 2019/04/01 Send email to surveyor

        return response;
    }


    private String saveExternalUserAssistantForPLSExUser(@RequestBody @Valid User user, ArrayList<ExternalUserRoles> externalRoleCode, ExternalUserRoles externalUserRoles) {
        String response = "Success";
        ExternalUserAssistant externalUserAssistant = new ExternalUserAssistant();
        response = externalUserAssistantMappingPlsExUser(user, externalUserAssistant);
        if (response.equals("failed")) {
            return response = "failed";
        }
        ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(user.getMainRoleCode(), externalUserRoles.getUserProvinceCode());
        externalUserRolesMapping(user, externalUserRoles, externalRole);
        externalRoleCode.add(externalUserRoles);
        ExternalUserAssistant externalUserAssistant1 = this.externalUserAssistantService.saveExternalAssistant(externalUserAssistant);
        //// TODO: 2019/04/01 Send email to surveyor

        return response;
    }

    private String externalUserAssistantMappingPlsExUser(@RequestBody @Valid User user, ExternalUserAssistant externalUserAssistant) {
        String message = "Success";
        System.out.println("PP number is " + user.getExternaluser().getPpno());
        String ppNumber = this.userService.getpPNumber(user.getExternaluser().getPpno());
        System.out.println("PP number is " + ppNumber);
        if (ppNumber == null || ppNumber.length() == 0) {
            String UserCode = this.userService.getUserCode(ppNumber);
            externalUserAssistant.setSurveyorusercode(user.getUserCode());
            externalUserAssistant.setSurveyorusername(user.getUserName());
            externalUserAssistant.setAssistantusercode(UserCode);
            externalUserAssistant.setAssistantusername(user.getEmail());
            externalUserAssistant.setIsApproved("Pending");
            externalUserAssistant.setIsActive("Y");
            externalUserAssistant.setCreateddate(new Date());
            externalUserAssistant.setUserId(user.getUserId());
            message = "Success";
            return message;
        } else if (ppNumber != null || ppNumber.length() > 0) {
            message = "failed";
            return message;
        }
        return message;
    }

    private String externalUserAssistantMapping(@RequestBody @Valid User user, ExternalUserAssistant externalUserAssistant) {
        String message = "Success";
        if (user.getMainRoleName().equalsIgnoreCase("Land Survey Assistant") && user.getMainRoleCode().equalsIgnoreCase("EX011")) {
            String pPnumber = this.userService.getpPNumberForAssistant(user.getExternaluser().getPpno());
            System.out.println("pPnumber is " + pPnumber);
            if (pPnumber == null || pPnumber.length() == 0) {
                message = "failed";
                return message;
            }
            String userCode = this.userService.getUserCodeForAssistant(pPnumber);
            String userName = this.userService.getUserName(userCode);
            externalUserAssistant.setSurveyorusercode(userCode);
            externalUserAssistant.setSurveyorusername(userName);
            externalUserAssistant.setAssistantusercode(user.getUserCode());
            externalUserAssistant.setAssistantusername(user.getEmail());
            externalUserAssistant.setIsApproved("Pending");
            externalUserAssistant.setIsActive("Y");
            externalUserAssistant.setCreateddate(new Date());
            externalUserAssistant.setUserId(user.getUserId());
        }
        return message;
    }


}
