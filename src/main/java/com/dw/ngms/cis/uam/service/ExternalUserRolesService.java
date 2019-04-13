package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.ExternalUserRoles;
import com.dw.ngms.cis.uam.repository.ExternalUserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/09.
 */
@Service
public class ExternalUserRolesService {

    @Autowired
    private ExternalUserRolesRepository externalUserRolesRepository;


    public ExternalUserRoles save(ExternalUserRoles externalUserRoles){

        return this.externalUserRolesRepository.save(externalUserRoles);
    }

}
