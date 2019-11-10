package com.dw.ngms.cis.uam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;

/**
 * Created by swaroop on 2019/03/28.
 */
public interface InternalUserRoleRepository  extends JpaRepository<InternalUserRoles, Long> {
	
	@Query("select iur from InternalUserRoles iur where Lower(iur.userName) = ?1")
	List<InternalUserRoles> findByEmail(String username);

	@Query("select iur from InternalUserRoles iur where Lower(iur.userName) = ?1 and isActive = ?2")
	List<InternalUserRoles> getInternalUserRoleWithActive(String username, String isActive);

	@Query("select iur from InternalUserRoles iur where iur.internalRoleCode = ?1 and iur.isActive = ?2")
	InternalUserRoles findByInternalRoleCodeAndStatus(String internalrolecode, String isActive);
	
	@Query("select iur from InternalUserRoles iur where iur.userCode = ?1 and Lower(iur.userName) = ?2 and iur.internalRoleCode = ?3")
	InternalUserRoles findByUserCodeUserNameAndInternalRoleCode(String usercode, String username, String internalrolecode);


	@Query("select u from InternalUserRoles u where u.userCode = :userCode and Lower(u.userName) = :userName and u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode and u.internalRoleCode = :internalRoleCode")
	InternalUserRoles getInternalRoleCode(@Param("userCode") String userCode,@Param("userName") String userName,@Param("provinceCode") String provinceCode,@Param("sectionCode") String sectionCode,@Param("roleCode") String roleCode, @Param("internalRoleCode") String internalRoleCode);

	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and USERROLECODE = 'IN014'")
	List<InternalUserRoles> getOfficersOfMySection(@Param("provinceCode") String provinceCode,@Param("sectionCode") String sectionCode);

	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and USERROLECODE = 'IN014'")
	List<InternalUserRoles> getOfficersOfMySectionProvinceCode(@Param("provinceCode") String provinceCode);

	@Query("select u from InternalUserRoles u where u.sectionCode = :sectionCode and USERROLECODE = 'IN014'")
	List<InternalUserRoles> getOfficersOfMySectionSectionCode(@Param("sectionCode") String sectionCode);

	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and USERROLECODE = :userRoleCode")
	List<InternalUserRoles> getUsersByProvinceCodeAndSectionCodeAndUserRoleCode(@Param("provinceCode") String provinceCode, 
			@Param("sectionCode") String sectionCode, @Param("userRoleCode") String userRoleCode);
	
	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and USERROLECODE = :userRoleCode")
	List<InternalUserRoles> getUsersByProvinceCodeAndUserRoleCode(@Param("provinceCode") String provinceCode, @Param("userRoleCode") String userRoleCode);
	
	@Query("select u from InternalUserRoles u where u.userCode = :userCode and Lower(u.userName) = :userName and u.provinceCode = :provinceCode and u.sectionCode IS NULL and u.roleCode = :roleCode and u.internalRoleCode = :internalRoleCode")
	InternalUserRoles getInternalUserRoleCodeWithEmptySectionCode(@Param("userCode") String userCode,@Param("userName") String userName,@Param("provinceCode") String provinceCode,@Param("roleCode") String roleCode, @Param("internalRoleCode") String internalRoleCode);

	@Query("select u from InternalUserRoles u where u.userCode = :userCode")
	List<InternalUserRoles> getChildElementsInternal(@Param("userCode") String userCode);

	@Query("select u from InternalUserRoles u where u.userCode = :userCode and provinceCode is not null")
	List<InternalUserRoles> getChildElementsInternalWithProvinceCodeNotNull(@Param("userCode") String userCode);

	@Query("select u from InternalUserRoles u where u.userCode = :userCode and isActive = 'Y'")
	List<InternalUserRoles> getChildElementsInternalWithActive(@Param("userCode") String userCode);

	@Query("select u from InternalUserRoles u where u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode")
	List<InternalUserRoles> getInternalUserRoles(@Param("provinceCode") String provinceCode,@Param("sectionCode") String sectionCode,@Param("roleCode") String roleCode);

	@Query("select u from InternalUserRoles u where u.roleCode = :roleCode")
	List<InternalUserRoles> getInternalUsersForDelete(@Param("roleCode") String roleCode);


}
