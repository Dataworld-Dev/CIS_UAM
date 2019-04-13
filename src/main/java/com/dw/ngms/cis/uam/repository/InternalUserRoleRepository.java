package com.dw.ngms.cis.uam.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import org.springframework.data.repository.query.Param;

/**
 * Created by swaroop on 2019/03/28.
 */
public interface InternalUserRoleRepository  extends JpaRepository<InternalUserRoles, Long> {
	
	@Query("select iur from InternalUserRoles iur where iur.userName = ?1")
	List<InternalUserRoles> findByEmail(String username);
	
	@Query("select iur from InternalUserRoles iur where iur.userCode = ?1 and iur.userName = ?2 and iur.internalRoleCode = ?3")
	InternalUserRoles findByUserCodeUserNameAndInternalRoleCode(String usercode, String username, String internalrolecode);


	@Query("select u from InternalUserRoles u where u.userCode = :userCode and u.userName = :userName and u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode and u.internalRoleCode = :internalRoleCode")
	InternalUserRoles getInternalRoleCode(@Param("userCode") String userCode,@Param("userName") String userName,@Param("provinceCode") String provinceCode,@Param("sectionCode") String sectionCode,@Param("roleCode") String roleCode, @Param("internalRoleCode") String internalRoleCode);

	@Query("select u from InternalUserRoles u where u.userCode = :userCode and u.userName = :userName and u.provinceCode = :provinceCode and u.sectionCode IS NULL and u.roleCode = :roleCode and u.internalRoleCode = :internalRoleCode")
	InternalUserRoles getInternalUserRoleCodeWithEmptySectionCode(@Param("userCode") String userCode,@Param("userName") String userName,@Param("provinceCode") String provinceCode,@Param("roleCode") String roleCode, @Param("internalRoleCode") String internalRoleCode);


	@Query("select u from InternalUserRoles u where u.userCode = :userCode")
	ArrayList<InternalUserRoles> getChildElementsInternal(@Param("userCode") String userCode);


	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode")
	ArrayList<InternalUserRoles> getInternalUserName(@Param("provinceCode") String provinceCode,@Param("sectionCode") String sectionCode,@Param("roleCode") String roleCode);




}
