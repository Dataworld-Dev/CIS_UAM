package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.Roles;
import com.dw.ngms.cis.uam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/03/24.
 */

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Roles> findByRoleType(String type) {
        return this.roleRepository.findByRoleType(type);
    }

    public Roles getRoleByRoleCode(String roleCode) {
    	return roleRepository.findByRoleCode(roleCode);
    }

    public String getMenuByRoleCode(String roleCode) {
        return roleRepository.getMenuByRoleCode(roleCode);
    }


}
