package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.DashBoardService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by swaroop on 2019/04/12.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class DashBoardController extends MessageController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping("/getUserRegisteredCounts")
    public ResponseEntity getIssueStatusById(HttpServletRequest request, @RequestParam Long dashBoarID) {
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        Gson gson = new Gson();
        String json = null;
        String dashBoadJson = null;
        try {

            dashBoadJson = this.dashBoardService.findDashBoardJson(dashBoarID);
            if(StringUtils.isEmpty(dashBoadJson)){
                return generateEmptyWithOKResponse(request,"No Status found");
            }
            userControllerResponse.setMessage(dashBoadJson);
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }


}
