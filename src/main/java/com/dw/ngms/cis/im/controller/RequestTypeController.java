package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.service.RequestTypeService;
import com.dw.ngms.cis.uam.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by swaroop on 2019/04/19.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
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


}
