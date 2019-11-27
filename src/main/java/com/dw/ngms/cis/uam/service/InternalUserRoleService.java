package com.dw.ngms.cis.uam.service;

import java.util.List;

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

    public List<InternalUserRoles> getInternalUserRoleWithActive(String email, String isActive) {
        return internalUserRoleRepository.getInternalUserRoleWithActive(email,isActive);
    }

    public void deleteInternalUserRole(String usercode, String username, String internalrolecode) {
    	if(usercode == null || username == null || internalrolecode == null)
    		throw new RuntimeException("Usercode and username and internalrolecode required");
    	
    	InternalUserRoles internalUserRoles = internalUserRoleRepository.findByUserCodeUserNameAndInternalRoleCode(usercode,username.trim().toLowerCase(),internalrolecode);

    	internalUserRoleRepository.delete(internalUserRoles);
    }//deleteInternalUserRole
    
    public InternalUserRoles getInternalUserRoleWithInternalRoleCodeAndStatus(String internalRoleCode, String isActive) {
        return internalUserRoleRepository.findByInternalRoleCodeAndStatus(internalRoleCode, isActive);
    }
    
   /* public InternalUserRoles getInternalUserRole(String usercode, String username, String internalrolecode) {
    	return internalUserRoleRepository.findByUserCodeUserNameAndInternalRoleCode(usercode, username, internalrolecode);
    }//getInternalUserRole
*/

    public InternalUserRoles getInternalUserRoleCode(String userCode, String userName,String provinceCode, String sectionCode, String roleCode, String internalRoleCode) {
        return this.internalUserRoleRepository.getInternalRoleCode(userCode,userName,provinceCode,sectionCode,roleCode,internalRoleCode);
    }//get Internal Role Code

    public List<InternalUserRoles> getInternalUserDataWithProvinceCodeAndSectionCode(String provinceCode, String sectionCode) {
        return this.internalUserRoleRepository.getInternalUserDataWithProvinceCodeAndSectionCode(provinceCode,sectionCode);
    }//getInternalUserDataWithProvinceCodeAndSectionCode

    public List<InternalUserRoles> getInternalUserDataWithProvinceCode(String provinceCode) {
        return this.internalUserRoleRepository.getInternalUserDataWithProvinceCode(provinceCode);
    }//getInternalUserDataWithProvinceCode


    public List<InternalUserRoles> getInternalUserDataWithSectionCode(String sectionCode) {
        return this.internalUserRoleRepository.getInternalUserDataWithSectionCode(sectionCode);
    }//getInternalUserDataWithSectionCode


    public List<InternalUserRoles> getOfficersOfMySection(String provinceCode, String sectionCode) {
        return this.internalUserRoleRepository.getOfficersOfMySection(provinceCode,sectionCode);
    }//get Internal Role Code

    public List<InternalUserRoles> getOfficersOfMySectionProvinceCode(String provinceCode) {
        return this.internalUserRoleRepository.getOfficersOfMySectionProvinceCode(provinceCode);
    }//get Internal Role Code


    public List<InternalUserRoles> getOfficersOfMySectionSectionCode(String sectionCode) {
        return this.internalUserRoleRepository.getOfficersOfMySectionSectionCode(sectionCode);
    }//get Internal Role Code

    public List<InternalUserRoles> getUsersByProvinceCodeAndSectionCodeAndUserRoleCode(String provinceCode, String sectionCode, String userRoleCode) {
        return this.internalUserRoleRepository.getUsersByProvinceCodeAndSectionCodeAndUserRoleCode(provinceCode, sectionCode, userRoleCode);
    }//getUsersByProvinceCodeAndSectionCodeAndUserRoleCode

    public List<InternalUserRoles> getUsersByProvinceCodeAndUserRoleCode(String provinceCode, String userRoleCode) {
        return this.internalUserRoleRepository.getUsersByProvinceCodeAndUserRoleCode(provinceCode, userRoleCode);
    }//getUsersByProvinceCodeAndUserRoleCode
    
    public InternalUserRoles getInternalUserRoleCodeWithEmptySectionCode(String userCode, String userName,String provinceCode, String roleCode, String internalRoleCode) {
        return this.internalUserRoleRepository.getInternalUserRoleCodeWithEmptySectionCode(userCode,userName,provinceCode,roleCode,internalRoleCode);
    }//get Internal Role Code


    public List<InternalUserRoles> getChildElementsInternal(String userCode) {
        return this.internalUserRoleRepository.getChildElementsInternal(userCode);
    } //getChildElementsInternal

    public List<InternalUserRoles> getChildElementsInternalWithProvinceCodeNotNull(String userCode) {
        return this.internalUserRoleRepository.getChildElementsInternalWithProvinceCodeNotNull(userCode);
    } //getChildElementsInternal


    public List<InternalUserRoles> getChildElementsInternalWithActive(String userCode) {
        return this.internalUserRoleRepository.getChildElementsInternalWithActive(userCode);
    } //getChildElementsInternalWithActive




    public List<InternalUserRoles> getInternalUserRoles(String provinceCode,String sectionCode,String roleCode){
        return this.internalUserRoleRepository.getInternalUserRoles(provinceCode,sectionCode,roleCode);
    }


    public List<InternalUserRoles> getInternalUsersForDelete(String roleCode) {
        return internalUserRoleRepository.getInternalUsersForDelete(roleCode);
    }

}
