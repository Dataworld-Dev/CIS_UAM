package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.ExternalUserAssistantDTO;
import com.dw.ngms.cis.uam.entity.ExternalUserAssistant;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.ExternalUserAssistantService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/03/26.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ExternalUserAssistantController extends MessageController {


    @Autowired
    private ExternalUserAssistantService externalUserAssistantService;


    @RequestMapping(value = "/approveRejectAssitant", method = RequestMethod.POST)
    public ResponseEntity<?> approveRejectAssitant(HttpServletRequest request, @RequestBody @Valid ExternalUserAssistantDTO externalUserAssistantDTO) throws IOException {
        try {
            ExternalUserAssistant externalUserAssistant = this.externalUserAssistantService.findByUserCodeName(externalUserAssistantDTO);
            if (isEmpty(externalUserAssistant)){
                return generateEmptyResponse(request, "External User Assistant not found");
            }
            if (!isEmpty(externalUserAssistant)) {
                externalUserAssistant.setIsApproved(externalUserAssistantDTO.getIsapproved());
                externalUserAssistant.setRejectionreason(externalUserAssistantDTO.getRejectionreason());
                externalUserAssistant.setIsapprejdate(new Date());
            }
             this.externalUserAssistantService.updateExternalAssistant(externalUserAssistant);
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("External User Assistant Updated Sucessfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @RequestMapping(value = "/deleteAssistant", method = RequestMethod.POST)
    public ResponseEntity<?> deleteAssistant(HttpServletRequest request, @RequestBody @Valid ExternalUserAssistantDTO externalUserAssistantDTO) throws IOException {
        try {
            ExternalUserAssistant externalUserAssistant = this.externalUserAssistantService.findByUserCodeName(externalUserAssistantDTO);
            if (isEmpty(externalUserAssistant)){
                return generateEmptyResponse(request, "External User Assistant not found");
            }
            if (!isEmpty(externalUserAssistant)) {
                this.externalUserAssistantService.deleteAssistant(externalUserAssistant);
            }
            //todo Send Email to User
            return ResponseEntity.status(HttpStatus.OK).body("Assistant Deleted Successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @RequestMapping(value = "/deactivateAssistant", method = RequestMethod.POST)
    public ResponseEntity<?> deactivateAssistant(HttpServletRequest request, @RequestBody @Valid ExternalUserAssistantDTO externalUserAssistantDTO) throws IOException {
        try {
            Gson gson = new Gson();
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            String json = null;
            ExternalUserAssistant externalUserAssistant = this.externalUserAssistantService.findByUserCodeName(externalUserAssistantDTO);
            if (isEmpty(externalUserAssistant)){
                userControllerResponse.setMessage("External User Assistant not found");
                json = gson.toJson(userControllerResponse);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }
            if (!isEmpty(externalUserAssistant)) {
                externalUserAssistant.setIsActive("N");
                this.externalUserAssistantService.saveExternalAssistant(externalUserAssistant);
            }
            //todo Send Email to User
            userControllerResponse.setMessage("Assistant de-activated Successfully");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }




}
