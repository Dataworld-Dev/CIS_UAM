package com.dw.ngms.cis.uam.service;

import java.util.ArrayList;
import java.util.List;

import com.dw.ngms.cis.uam.entity.InternalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.repository.InternalUserRoleRepository;

/**
 * Created by swaroop on 2019/03/28.
 */

@Service
public class InternalUserRoleService {

    @Autowired
    private InternalUserRoleRepository internalUserRoleRepository;
    
    public List<InternalUserRoles> getInternalUserRole(String email) {
    	return internalUserRoleRepository.findByEmail(email);
    }//getInternalUserRole
	
    public void deleteInternalUserRole(String usercode, String username, String internalrolecode) {
    	if(usercode == null || username == null || internalrolecode == null)
    		throw new RuntimeException("Usercode and username and internalrolecode required");
    	
    	InternalUserRoles internalUserRoles = internalUserRoleRepository.findByUserCodeUserNameAndInternalRoleCode(usercode,username,internalrolecode);

    	internalUserRoleRepository.delete(internalUserRoles);
    }//deleteInternalUserRole
    
    public InternalUserRoles getInternalUserRole(String usercode, String username, String internalrolecode) {
    	return internalUserRoleRepository.findByUserCodeUserNameAndInternalRoleCode(usercode, username, internalrolecode);
    }//getInternalUserRole



    public InternalUserRoles getInternalUserRoleCode(String userCode, String userName,String provinceCode, String sectionCode, String roleCode, String internalRoleCode) {
        return this.internalUserRoleRepository.getInternalRoleCode(userCode,userName,provinceCode,sectionCode,roleCode,internalRoleCode);
    }//get Internal Role Code

    public InternalUserRoles getInternalUserRoleCodeWithEmptySectionCode(String userCode, String userName,String provinceCode, String roleCode, String internalRoleCode) {
        return this.internalUserRoleRepository.getInternalUserRoleCodeWithEmptySectionCode(userCode,userName,provinceCode,roleCode,internalRoleCode);
    }//get Internal Role Code


    public ArrayList<InternalUserRoles> getChildElementsInternal(String userCode) {
        return this.internalUserRoleRepository.getChildElementsInternal(userCode);
    } //getChildElementsInternal

    public List<InternalUserRoles>getInternalUserName(String provinceCode,String sectionCode,String roleCode){
        return this.internalUserRoleRepository.getInternalUserName(provinceCode,sectionCode,roleCode);
    }

}
