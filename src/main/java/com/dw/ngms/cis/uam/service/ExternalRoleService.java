package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.repository.ExternalRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/03/30.
 */
@Service
public class ExternalRoleService {


    @Autowired
    ExternalRoleRepository externalRoleRepository;

    public ExternalRole getByRoleCodeRoleProvince(String rolesCode, String provinceCode) {
        return this.externalRoleRepository.getByRoleCodeRoleProvince(rolesCode, provinceCode);
    }

    public List<ExternalRole> findByRoleCode(String roleCode) {
        return this.externalRoleRepository.findByRoleCode(roleCode);
    }

    public ExternalRole findByExternalRoleCode(String externalRoleCode) {
        return this.externalRoleRepository.findByExternalRoleCode(externalRoleCode);
    }

    public void deleteByInternalRoleCode(ExternalRole externalRole) {
          this.externalRoleRepository.delete(externalRole);
    }






    public List<ExternalRole> updateAccessRight(String roleCode) {
        return this.externalRoleRepository.updateAccessRight(roleCode);
    }


    public ExternalRole updateExternalRole(ExternalRole externalRole) {
        return this.externalRoleRepository.save(externalRole);
    }

    public String getAccessRightJson(String internalRoleCode) {
        return this.externalRoleRepository.getAccessRightJson(internalRoleCode);
    }



}
