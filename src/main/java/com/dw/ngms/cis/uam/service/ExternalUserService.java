package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.dto.ExternalUserDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.ExternalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/28.
 */
@Service
public class ExternalUserService {

    @Autowired
    ExternalUserRepository externalUserRepository;


    public ExternalUser updateSecurityQuestions(ExternalUser externalUser) {

        return this.externalUserRepository.save(externalUser);
    }

    public ExternalUser findByUserCode(UserDTO userDTO) {
        return this.externalUserRepository.findByUserCode(userDTO.getUsercode());
    }


    public Long getRoleId() {
        return this.externalUserRepository.getRoleId();
    } //FindUserByEmail




    public void deleteAssistant(ExternalUser externalUser) {
    this.externalUserRepository.delete(externalUser);
    }









}
