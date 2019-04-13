package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.SecurityQuestionDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.SecurityQuestionService;
import com.dw.ngms.cis.uam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by swaroop on 2019/03/24.
 */
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class SecurityQuestionController extends MessageController {

    @Autowired
    private SecurityQuestionService securityQuestionService;

    @Autowired
    private UserService userService;

    @GetMapping("/getSecurityQuestions")
    public ResponseEntity<?> getAllSecurityQuestions(HttpServletRequest request) {
        try {
            List<SecurityQuestion> questionList = this.securityQuestionService.getAllSecurityQuestions();
            return (CollectionUtils.isEmpty(questionList)) ? generateEmptyResponse(request, "Security Questions not found")
                    : ResponseEntity.status(HttpStatus.OK).body(questionList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSecurityQuestions






    @RequestMapping(value = "/getUserSecurityQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> getUserSecurityQuestions(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO) {
        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        try {


            User user = this.userService.findByEmail(userDTO.getEmail());


            List<SecurityQuestionDTO> securityQuestionsTestList = new ArrayList<>();
            if (!isEmpty(user) && user.getUserTypeName().equalsIgnoreCase("EXTERNAL")) {
                System.out.println("User code get "+user.getUserCode());

                ExternalUser externalUser = this.userService.getChildElements(user.getUserCode());

                SecurityQuestionDTO securityQuestionDTO = new SecurityQuestionDTO();
                SecurityQuestionDTO securityQuestionDTO1 = new SecurityQuestionDTO();
                SecurityQuestionDTO securityQuestionDTO2 = new SecurityQuestionDTO();

                securityQuestionDTO.setCode(externalUser.getSecurityquestiontypecode1());
                securityQuestionDTO.setQue(externalUser.getSecurityquestion1());
                securityQuestionDTO.setAns(externalUser.getSecurityanswer1());
                securityQuestionsTestList.add(securityQuestionDTO);

                securityQuestionDTO1.setCode(externalUser.getSecurityquestiontypecode2());
                securityQuestionDTO1.setQue(externalUser.getSecurityquestion2());
                securityQuestionDTO1.setAns(externalUser.getSecurityanswer2());
                securityQuestionsTestList.add(securityQuestionDTO1);

                securityQuestionDTO2.setCode(externalUser.getSecurityquestiontypecode3());
                securityQuestionDTO2.setQue(externalUser.getSecurityquestion3());
                securityQuestionDTO2.setAns(externalUser.getSecurityanswer3());
                securityQuestionsTestList.add(securityQuestionDTO2);

                userDTO.setQuestion(securityQuestionsTestList);
                userDTO.setUsercode(user.getUserCode());

            }

            return (isEmpty(userDTO)) ? generateEmptyResponse(request, "Security Questions not found")
                    : ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getSecurityQuestions



}
