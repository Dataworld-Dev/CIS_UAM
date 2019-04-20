package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.MediaTypes;
import com.dw.ngms.cis.im.service.MediaTypeService;
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
public class MediaTypeController extends MessageController {

    @Autowired
    private MediaTypeService mediaTypeService;


    @PostMapping("/createMediaType")
    public ResponseEntity<?> createMediaType(HttpServletRequest request, @RequestBody @Valid MediaTypes mediaTypes) {
        try {
            Long mediaTypeId = this.mediaTypeService.getMediaType();
            System.out.println("mediaTypeId is "+mediaTypeId);
            mediaTypes.setMediaTypeCode("MEDIA" + Long.toString(mediaTypeId));
            MediaTypes mediaTypesSave = this.mediaTypeService.saveMediaType(mediaTypes);
            return ResponseEntity.status(HttpStatus.OK).body(mediaTypesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createCategory


}
