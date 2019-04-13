package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.SecurityQuestion;
import com.dw.ngms.cis.uam.repository.SecurityQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityQuestionService {
    @Autowired
    SecurityQuestionRepository securityQuestionRepository;

    public List<SecurityQuestion> getAllSecurityQuestions() {
        return this.securityQuestionRepository.findAll();
    }


}
