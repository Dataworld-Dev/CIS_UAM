package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalUserRepository extends JpaRepository<InternalUserRoles, Long> {




    @Query("SELECT u FROM InternalRole u WHERE u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode and u.internalRoleCode = :internalRoleCode")
    InternalRole getInternalRoleCode(@Param("provinceCode") String provinceCode, @Param("sectionCode") String sectionCode, @Param("roleCode") String roleCode, @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.provinceCode = :provinceCode and u.sectionCode = :sectionCode and u.roleCode = :roleCode")
    InternalRole createInternalRoleCode(@Param("provinceCode") String provinceCode, @Param("sectionCode") String sectionCode, @Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.provinceCode = :provinceCode and u.sectionCode IS NULL and u.roleCode = :roleCode")
    InternalRole createInternalRoleCodeWithNullSectionCode(@Param("provinceCode") String provinceCode, @Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.provinceCode IS NULL and u.sectionCode IS NULL and u.roleCode = :roleCode")
    InternalRole createInternalRoleCodeWithNullSectionCodeProvinceCode(@Param("roleCode") String roleCode);


    @Query("SELECT u FROM InternalUserRoles u WHERE u.userCode = :userCode and u.userName = :userName and u.internalRoleCode = :internalRoleCode")
    InternalUserRoles findByUserByNameAndCode(
            @Param("userCode") String userCode, @Param("userName") String userName, @Param("internalRoleCode") String internalRoleCode);
}
