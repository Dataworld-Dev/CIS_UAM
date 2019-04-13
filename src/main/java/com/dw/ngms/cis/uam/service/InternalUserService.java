package com.dw.ngms.cis.uam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.repository.InternalUserRepository;

/**
 * Created by swaroop on 2019/03/26.
 */

@Service
public class InternalUserService {

    @Autowired
    InternalUserRepository internalUserRepository;



    public InternalUserRoles saveInternalUserRole(InternalUserRoles internalUserRoles) {

        return this.internalUserRepository.save(internalUserRoles);
    }//saveExternalUser

    public InternalRole getInternalRoleCode(String provinceCode, String sectionCode, String roleCode, String internalRoleCode) {
        return this.internalUserRepository.getInternalRoleCode(provinceCode,sectionCode,roleCode,internalRoleCode);
    }//get Internal Role Code

    public InternalRole createInternalRoleCode(String provinceCode, String sectionCode, String roleCode) {
        return this.internalUserRepository.createInternalRoleCode(provinceCode,sectionCode,roleCode);
    }//get Internal Role Code

    public InternalRole createInternalRoleCodeWithNullSectionCode(String provinceCode, String roleCode) {
        return this.internalUserRepository.createInternalRoleCodeWithNullSectionCode(provinceCode,roleCode);
    }//get Internal Role Code

    public InternalRole createInternalRoleCodeWithNullSectionCodeProvinceCode(String roleCode) {
        return this.internalUserRepository.createInternalRoleCodeWithNullSectionCodeProvinceCode(roleCode);
    }//get Internal Role Code



    public InternalUserRoles findByUserByNameAndCode(String userCode, String userName,String internalRoleCode) {
        return this.internalUserRepository.findByUserByNameAndCode(userCode,userName,internalRoleCode);
    }//get Internal Role Code




}
