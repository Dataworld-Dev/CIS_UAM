package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.SecurityQuestionDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.ExternalUserService;
import com.dw.ngms.cis.uam.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/03/28.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ExternalController extends MessageController {

    @Autowired
    private ExternalUserService externalUserService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/updateSecurityQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> updateSecurityQuestions(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) throws IOException {

        try {
            ExternalUser externalUser = this.externalUserService.findByUserCode(userDTO);
            if (isEmpty(externalUser)) {
                return generateEmptyResponse(request, "Users not found");
            }
            if (!isEmpty(externalUser)) {
                externalUser.setSecurityquestiontypecode1(userDTO.getQuestion().get(0).getCode());
                externalUser.setSecurityquestion1(userDTO.getQuestion().get(0).getQue());
                externalUser.setSecurityanswer1(userDTO.getQuestion().get(0).getAns());;
                externalUser.setSecurityquestiontypecode2(userDTO.getQuestion().get(1).getCode());
                externalUser.setSecurityquestion2(userDTO.getQuestion().get(1).getQue());
                externalUser.setSecurityanswer2(userDTO.getQuestion().get(1).getAns());
                externalUser.setSecurityquestiontypecode3(userDTO.getQuestion().get(2).getCode());
                externalUser.setSecurityquestion3(userDTO.getQuestion().get(2).getQue());
                externalUser.setSecurityanswer3(userDTO.getQuestion().get(2).getAns());
                System.out.println(" externalUser sec ans 3: " + externalUser.getSecurityanswer3());
                this.externalUserService.updateSecurityQuestions(externalUser);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Security Questions updated successfully");
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//updateSecurityQuestions



    @RequestMapping(value = "/checkUserSecurityQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> checkUserSecurityQuestions(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) {
        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        Gson gson = new Gson();
        String json = null;
        UserControllerResponse userControllerResponse = new UserControllerResponse();
        userControllerResponse.setMessage("false");
        json = gson.toJson(userControllerResponse);
        try {
            User user = this.userService.findByEmail(userDTO.getEmail());
            if(isEmpty(user)){
                return generateEmptyResponse(request, "Users not found");
            }
            ExternalUser externalUser = this.userService.getChildElements(user.getUserCode());
            if (!isEmpty(externalUser)) {
                System.out.println("externalUser.getSecurityquestion3() "+externalUser.getSecurityquestion3());
                System.out.println("userDTO.getQuestion().get(2).getAns() "+userDTO.getQuestion().get(2).getAns());
                if(externalUser.getSecurityquestiontypecode1().equalsIgnoreCase(userDTO.getQuestion().get(0).getCode())
                        && externalUser.getSecurityquestion1().equalsIgnoreCase(userDTO.getQuestion().get(0).getQue())
                        && externalUser.getSecurityanswer1().equalsIgnoreCase(userDTO.getQuestion().get(0).getAns())
                        && externalUser.getSecurityquestiontypecode2().equalsIgnoreCase(userDTO.getQuestion().get(1).getCode())
                        && externalUser.getSecurityquestion2().equalsIgnoreCase(userDTO.getQuestion().get(1).getQue())
                        && externalUser.getSecurityanswer2().equalsIgnoreCase(userDTO.getQuestion().get(1).getAns())
                        && externalUser.getSecurityquestiontypecode3().equalsIgnoreCase(userDTO.getQuestion().get(2).getCode())
                        && externalUser.getSecurityquestion3().equalsIgnoreCase(userDTO.getQuestion().get(2).getQue())
                        && externalUser.getSecurityanswer3().equalsIgnoreCase(userDTO.getQuestion().get(2).getAns())

                        ){
                    userControllerResponse.setMessage("true");
                    json = gson.toJson(userControllerResponse);
                    return ResponseEntity.status(HttpStatus.OK).body(json);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//checkUserSecurityQuestions





}
