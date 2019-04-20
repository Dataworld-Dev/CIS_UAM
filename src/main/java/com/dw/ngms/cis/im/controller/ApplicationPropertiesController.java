package com.dw.ngms.cis.im.controller;

import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ApplicationPropertiesController extends MessageController {

    @Autowired
    private ApplicationPropertiesService applicationPropertiesService;


    @GetMapping("/getPropertyValueByName")
    public ResponseEntity<?> getPropertyValueByName(HttpServletRequest request,
                                                    @RequestParam String name) {
        try {
            List<ApplicationProperties> applicationPropertiesList = applicationPropertiesService.getPropertyValueByName(name);
            return (CollectionUtils.isEmpty(applicationPropertiesList)) ? ResponseEntity.status(HttpStatus.OK).body(applicationPropertiesList)
                    : ResponseEntity.status(HttpStatus.OK).body(applicationPropertiesList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getPropertyValueByName


    @PostMapping("/setPropertyValueByName")
    public ResponseEntity<?> setPropertyValueByName(HttpServletRequest request, @RequestBody @Valid ApplicationProperties applicationProperties) {
        try {
            ApplicationProperties applicationPropertiesSave = this.applicationPropertiesService.saveProperties(applicationProperties);
            return ResponseEntity.status(HttpStatus.OK).body(applicationPropertiesSave);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createTask

}
