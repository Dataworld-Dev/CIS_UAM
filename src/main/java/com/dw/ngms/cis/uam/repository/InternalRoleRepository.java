package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swaroop on 2019/04/03.
 */

@Repository
public interface InternalRoleRepository extends JpaRepository<InternalRole, Long> {



    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole> updateAccessRight(
             @Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole> findByRoleCode(@Param("roleCode") String roleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.roleCode = :roleCode")
    List<InternalRole>  updateDashBoardAccessRight(
           @Param("roleCode") String roleCode);

    @Query(value = "DELETE  FROM INTERNALROLES  u WHERE u.INTERNALROLECODE = ?1 ", nativeQuery = true)
    void deleteByInternalRoleCode(
            @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT u FROM InternalRole u WHERE u.internalRoleCode = :internalRoleCode")
    InternalRole findByInternalRoleCode(
            @Param("internalRoleCode") String internalRoleCode);



    @Query("SELECT accessRightJson FROM InternalRole u WHERE u.internalRoleCode = :internalRoleCode")
    String getAccessRightJson ( @Param("internalRoleCode") String internalRoleCode);

    @Query("SELECT dashBoardRightJson FROM InternalRole u WHERE u.internalRoleCode = :internalRoleCode")
    String getdashBoardRightJson ( @Param("internalRoleCode") String internalRoleCode);


    @Query("SELECT u FROM InternalRole  u WHERE u.provinceCode = :provinceCode")
    List<InternalRole> getSectionsByProvinceCode (@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode = :sectionCode and u.provinceCode = :provinceCode")
    List<InternalRole> getRolesBySectionsAndProvince (@Param("sectionCode") String sectionCode,@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode IS NULL and u.provinceCode = :provinceCode")
    List<InternalRole> getRolesBySectionsAndProvinceBySectionCodeNull (@Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM InternalRole  u WHERE  u.sectionCode = :sectionCode and u.provinceCode IS NULL")
    List<InternalRole> getRolesBySectionsAndProvinceByProvinceCodeNull (@Param("sectionCode") String sectionCode);




    @Query(value = "SELECT U.*  FROM INTERNALROLES  u WHERE u.sectionCode is null and  u.provinceCode is null ",
            nativeQuery = true)
    List<InternalRole> getNationalRoles ();


}
