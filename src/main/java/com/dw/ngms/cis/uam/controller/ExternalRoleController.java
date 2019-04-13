package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.service.ExternalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/04/08.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ExternalRoleController extends MessageController {

    @Autowired
    ExternalRoleService externalRoleService;



    @GetMapping("/getExternalRolesByRoleCode")
    public ResponseEntity<?> getExternalRolesByRoleCode(HttpServletRequest request,
                                                        @RequestParam String roleCode
    ) {
        try {
            List<ExternalRole> externalRoleList = this.externalRoleService.findByRoleCode(roleCode);
            return (CollectionUtils.isEmpty(externalRoleList)) ? ResponseEntity.status(HttpStatus.OK).body(externalRoleList)
                    : ResponseEntity.status(HttpStatus.OK).body(externalRoleList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getExternalRolesByRoleCode


    @GetMapping(value = "/deleteExternalRole")
    public ResponseEntity<?> deleteExternalRole(HttpServletRequest request, @RequestParam String externalRoleCode) throws IOException {
        try {
            if(externalRoleCode!= null && !isEmpty(externalRoleCode)) {
                ExternalRole externalRole = this.externalRoleService.findByExternalRoleCode(externalRoleCode);
                if(!isEmpty(externalRole)){
                    this.externalRoleService.deleteByInternalRoleCode(externalRole);
                    return ResponseEntity.status(HttpStatus.OK).body("Internal Role deleted successfully");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("No Internal Role found for given Internal code");

        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//deleteExternalRole




}
