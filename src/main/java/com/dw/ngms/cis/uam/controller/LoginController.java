package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.LoggedUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.LoginService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by swaroop on 2019/03/24.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class LoginController extends MessageController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateLoginUser(HttpServletRequest request, @RequestBody @Valid LoggedUser loggedUser) {
        Gson gson = new Gson();
        User user = null;
        String json = null;
        String typeName = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        try {
            user = this.loginService.findByLoginName(loggedUser.getUsername());
            System.out.println("loggedUser.getUsername()" +loggedUser.getUsername());
            if (user == null) {
                return generateEmptyResponse(request, "User not found");
            }
            if (loggedUser.getInternal().equalsIgnoreCase("YES")) {
                typeName = "Internal";
            } else {
                typeName = "External";
            }
            //String decodeLoggedUserPassword = decodeUserPassword(user);
            if (user.getPassword().equalsIgnoreCase(loggedUser.getPassword()) && user.getUserTypeName().equalsIgnoreCase(typeName)) {
                userControllerResponse.setValid("true");
                userControllerResponse.setIsApproved(user.getIsApproved().getDisplayString());
                userControllerResponse.setActive(user.getIsActive().getStatus());
                userControllerResponse.setFirstLogin(user.getFirstLogin());
                json = gson.toJson(userControllerResponse);
            } else {
                userControllerResponse.setValid("false");
                json = gson.toJson(userControllerResponse);
            }
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    private String decodeUserPassword(User user) {
        String authString = user.getPassword();
        String decodedAuth = "";
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authString);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        return decodedAuth;
    }
}
