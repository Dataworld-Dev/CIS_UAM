package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.service.RequestTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.im/api/v1")
@CrossOrigin(origins = "*")
public class RequestTypeController extends MessageController {

    @Autowired
    private RequestTypeService requestTypeService;


    @PostMapping("/createRequestType")
    public ResponseEntity<?> createRequestType(HttpServletRequest request, @RequestBody @Valid RequestTypes requestTypes) {
        try {
            Long requestTypeId = this.requestTypeService.getRequestTypeID();
            System.out.println("requestTypeId is "+requestTypeId);
            requestTypes.setRequestTypeCode("REQT" + Long.toString(requestTypeId));
            RequestTypes requestTypeSave = this.requestTypeService.saveRequestType(requestTypes);
            return ResponseEntity.status(HttpStatus.OK).body(requestTypeSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory



    @GetMapping("/getRequestTypes")
    public ResponseEntity<?> getRequestTypes(HttpServletRequest request) {
        try {
            List<RequestTypes> requestTypesList = requestTypeService.getAllRequestTypes();
            return (CollectionUtils.isEmpty(requestTypesList)) ? generateEmptyResponse(request, "RequestType(s) not found")
                    : ResponseEntity.status(HttpStatus.OK).body(requestTypesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getRequestTypes


}
