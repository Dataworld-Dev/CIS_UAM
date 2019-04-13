package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.ExternalRoleDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.ExternalRoleService;
import com.dw.ngms.cis.uam.service.ExternalUserRolesService;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by swaroop on 2019/04/09.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ExternalUserRolesController extends MessageController {

    @Autowired
    private ExternalUserRolesService externalUserRolesService;


    @Autowired
    private UserService userService;

    @Autowired
    private ExternalRoleService externalRoleService;

    @PostMapping("/registerNewExternalRole")
    public ResponseEntity<?> registerNewExternalRole(HttpServletRequest request, @RequestBody @Valid ExternalRoleDTO externalRoleDTO) {
        Gson gson = new Gson();
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        String json = null;
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsercode(externalRoleDTO.getUsercode());
            User user = this.userService.findByUserCode(userDTO);
            System.out.println("user id is" +user.getUserId());
            if(user != null && user.getUserCode() != null) {
                ExternalUserRoles externalUserRoles = new ExternalUserRoles();
                externalUserRoles.setUserName(externalRoleDTO.getUsername());
                externalUserRoles.setUserId(user.getUserId());
                externalUserRoles.setUserCode(externalRoleDTO.getUsercode());
                externalUserRoles.setUserRoleCode(externalRoleDTO.getRolecode());
                externalUserRoles.setUserRoleName(externalRoleDTO.getRolename());
                externalUserRoles.setUserProvinceCode(externalRoleDTO.getProvincecode());
                externalUserRoles.setUserProvinceName(externalRoleDTO.getProvincename());
                ExternalRole externalRole = this.externalRoleService.getByRoleCodeRoleProvince(externalRoleDTO.getRolecode(), externalRoleDTO.getProvincecode());
                externalUserRoles.setExternalRoleCode(externalRole.getExternalRoleCode());
                ExternalUserRoles externalUserRolesResponse = this.externalUserRolesService.save(externalUserRoles);
                if (externalUserRolesResponse != null && externalUserRolesResponse.getUserCode() != null) {
                    user = this.userService.findByUserCode(userDTO);
                    user.setIsApproved(ApprovalStatus.PENDING);
                }
                return ResponseEntity.status(HttpStatus.OK).body(externalUserRolesResponse);
            }else {
                return ResponseEntity.status(HttpStatus.OK).body("No user found with the user code");
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//saveInternalUser

}
