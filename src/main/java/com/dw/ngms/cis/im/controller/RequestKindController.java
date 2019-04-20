package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.RequestKinds;
import com.dw.ngms.cis.im.service.CostCategoryService;
import com.dw.ngms.cis.im.service.RequestKindService;
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
public class RequestKindController extends MessageController {

    @Autowired
    private RequestKindService requestKindService;


    @PostMapping("/createRequestKind")
    public ResponseEntity<?> createRequestKind(HttpServletRequest request, @RequestBody @Valid RequestKinds requestKinds) {
        try {
            Long requestKindId = this.requestKindService.getRequestKind();
            System.out.println("requestKindId is "+requestKindId);
            requestKinds.setRequestKindCode("REQK" + Long.toString(requestKindId));
            RequestKinds requestKindsSave = this.requestKindService.saveRequestKind(requestKinds);
            return ResponseEntity.status(HttpStatus.OK).body(requestKindsSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


}
