package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.RolesDTO;
import com.dw.ngms.cis.uam.dto.UpdateAccessRightsDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.InternalRoleService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/08.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class InternalRoleController extends MessageController {

    @Autowired
    private InternalRoleService internalRoleService;


  @GetMapping("/getSectionsByProvince")
    public ResponseEntity<?> getSectionsByProvince(HttpServletRequest request, @RequestParam String provinceCode
                                                   ) {
        try {
            List<InternalRole> internalUserList = this.internalRoleService.getSectionsByProvinceCode(provinceCode);
            return (CollectionUtils.isEmpty(internalUserList)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserList)
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSectionsByProvince


    @GetMapping("/getNationalRoles")
    public ResponseEntity<?> getNationalRoles(HttpServletRequest request

    ) {
        try {
            List<InternalRole> internalUserList = this.internalRoleService.getNationalRoles();
            return (CollectionUtils.isEmpty(internalUserList)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserList)
                    : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getNationalRoles


    @GetMapping("/getRolesBySectionsAndProvince")
    public ResponseEntity<?> getRolesBySections(HttpServletRequest request,
                                                @RequestParam(required = false, defaultValue = "") String sectionCode,
                                                @RequestParam(required = false, defaultValue = "") String provinceCode

    ) {
        List<InternalRole> internalUserList;
        try {
            if(sectionCode.equalsIgnoreCase("") ){
               internalUserList = this.internalRoleService.getRolesBySectionsAndProvinceBySectionCodeNull(provinceCode);
                return (CollectionUtils.isEmpty(internalUserList)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserList)
                        : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
            }else if (sectionCode.equalsIgnoreCase("")){
              internalUserList = this.internalRoleService.getRolesBySectionsAndProvinceByProvinceCodeNull(sectionCode);
                return (CollectionUtils.isEmpty(internalUserList)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserList)
                        : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
            }else {
              internalUserList = this.internalRoleService.getRolesBySectionsAndProvince(sectionCode, provinceCode);
                return (CollectionUtils.isEmpty(internalUserList)) ? ResponseEntity.status(HttpStatus.OK).body(internalUserList)
                        : ResponseEntity.status(HttpStatus.OK).body(internalUserList);
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRolesBySections

    @GetMapping("/getDashboardRights")
    public ResponseEntity<?> getDashboardRights(HttpServletRequest request,
                                                @RequestParam String userType,
                                                @RequestParam String roleCode

    ) {
        try {
            if(userType.equalsIgnoreCase("Internal")){
                String dashBoarRightJson = this.internalRoleService.getdashBoardRightJson(roleCode);
                return (dashBoarRightJson == null) ? generateEmptyResponse(request, "DashBoard Right Json not found")
                        : ResponseEntity.status(HttpStatus.OK).body(dashBoarRightJson);
            }else{
                return  ResponseEntity.status(HttpStatus.OK).body("Access Right Json not found");
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getDashboardRights

    @PostMapping("/setDashboardRights")
    public ResponseEntity<?> setDashboardRights(HttpServletRequest request,@RequestBody @Valid UpdateAccessRightsDTO updateAccessRightsDTO


    ) {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            Gson gson = new Gson();
            String json = null;
            System.out.println("User Type is " + updateAccessRightsDTO.getUsertype());
            if (updateAccessRightsDTO.getUsertype().equalsIgnoreCase("Internal")) {
                for (RolesDTO roles : updateAccessRightsDTO.getRoles()) {
                    if (!isEmpty(roles)) {
                        List<InternalRole> internalRoleList = this.internalRoleService.updateDashBoardAccessRight(roles.getRolecode());
                        if (!isEmpty(internalRoleList)) {
                            for(InternalRole internalRole:internalRoleList) {
                                internalRole.setDashBoardRightJson(updateAccessRightsDTO.getDashboardrightjson());
                                this.internalRoleService.updateInternalRole(internalRole);
                            }
                                userControllerResponse.setMessage("Dash Board right added successfully");
                                json = gson.toJson(userControllerResponse);
                                return ResponseEntity.status(HttpStatus.OK).body(json);
                        } else {
                            return generateEmptyResponse(request, "No roles found");
                        }

                    } else {
                        return generateEmptyResponse(request, "No roles found");
                    }
                }
            }
            return generateEmptyResponse(request, "No roles found");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//setDashboardRights




    @GetMapping(value = "/deleteInternalRole")
    public ResponseEntity<?> deleteInternalRole(HttpServletRequest request, @RequestParam String internalRoleCode) throws IOException {
        try {
            if(internalRoleCode!= null && !isEmpty(internalRoleCode)) {
                InternalRole internalRole = this.internalRoleService.findByInternalRoleCode(internalRoleCode);
                System.out.println("Intenal user code is " +internalRole.getInternalRoleCode());
                if(!isEmpty(internalRole)){
                    this.internalRoleService.deleteByInternalRoleCode(internalRole);
                    return ResponseEntity.status(HttpStatus.OK).body("Internal Role deleted successfully");
                }
            }
                return ResponseEntity.status(HttpStatus.OK).body("No Internal Role found for given Internal code");

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }



    @GetMapping("/getInternalRolesByRoleCode")
    public ResponseEntity<?> getInternalRolesByRoleCode(HttpServletRequest request,
                                                        @RequestParam String roleCode
    ) {
        try {
            List<InternalRole> internalRoleList = this.internalRoleService.findByRoleCode(roleCode);
            return (CollectionUtils.isEmpty(internalRoleList)) ? ResponseEntity.status(HttpStatus.OK).body(internalRoleList)
                    : ResponseEntity.status(HttpStatus.OK).body(internalRoleList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getInternalRolesByRoleCode








}
