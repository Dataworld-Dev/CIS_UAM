package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.IssueLogService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by swaroop on 2019/04/11.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class IssueLogController extends MessageController {

    @Autowired
    private IssueLogService issueLogService;

    @RequestMapping(value = "/saveIssueLog", method = RequestMethod.POST)
    public ResponseEntity<?> saveIssueLog(HttpServletRequest request, @RequestBody @Valid IssueLog issueLog) {
        try {
            issueLog = issueLogService.saveIssueLog(issueLog);
            if (issueLog != null) {
                return ResponseEntity.status(HttpStatus.OK).body(issueLog);
            }

            return generateEmptyWithOKResponse(request, "Empty");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }

    }


    @GetMapping("/getAllIssueLogs")
    public ResponseEntity<?> getAllIssueLogs(HttpServletRequest request) {
        try {
            List<IssueLog> issueLogList = null;
            issueLogList = issueLogService.findAll();
            return (CollectionUtils.isEmpty(issueLogList)) ? ResponseEntity.status(HttpStatus.OK).body(issueLogList)
                    : ResponseEntity.status(HttpStatus.OK).body(issueLogList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllIssueLogs


    @GetMapping("/getIssueLogStatus")
    public ResponseEntity getIssueStatusById(HttpServletRequest request,@RequestParam Long issueID) {
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        Gson gson = new Gson();
        String json = null;
        try {
            String status = null;
            status = issueLogService.findIssueStatus(issueID);
            if(StringUtils.isEmpty(status)){
                return generateEmptyWithOKResponse(request,"No Status found");
            }
            userControllerResponse.setMessage(status);
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


    @RequestMapping(value = "/issueLogUpdateStatus", method = RequestMethod.POST)
    public ResponseEntity updateStatus(HttpServletRequest request,@RequestParam Long issueID, @Valid @RequestBody IssueLog issueLogDetails) {

        IssueLog issueLog = issueLogService.findById(issueID);
        if (!StringUtils.isEmpty(issueLog)) {
            issueLog.setIssueStatus(issueLogDetails.getIssueStatus());
            IssueLog updateIssueLog = this.issueLogService.saveIssueLog(issueLog);
            return ResponseEntity.status(HttpStatus.OK).body(updateIssueLog);
        }else{
            return generateEmptyWithOKResponse(request, "No Issue log found");
        }

    }


}

