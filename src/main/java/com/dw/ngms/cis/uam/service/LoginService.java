package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/24.
 */
@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;

    public User findByLoginName(String userName) {
        return this.loginRepository.findByLoginName(userName);
    }
}
