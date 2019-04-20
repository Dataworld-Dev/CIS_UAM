package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.FormatTypes;
import com.dw.ngms.cis.im.service.FormatTypeService;
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
public class FormatTypeController extends MessageController {

    @Autowired
    private FormatTypeService formatTypeService;


    @PostMapping("/createFormatType")
    public ResponseEntity<?> createFormatType(HttpServletRequest request, @RequestBody @Valid FormatTypes formatTypes) {
        try {
            Long formatTypeId = this.formatTypeService.getFormatType();
            System.out.println("formatTypeId is "+formatTypeId);
            formatTypes.setFormatTypeCode("IMF" + Long.toString(formatTypeId));
            FormatTypes formatTypesSave = this.formatTypeService.saveFormatType(formatTypes);
            return ResponseEntity.status(HttpStatus.OK).body(formatTypesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


}
