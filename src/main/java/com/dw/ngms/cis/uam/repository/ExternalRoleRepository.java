package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import com.dw.ngms.cis.uam.entity.InternalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swaroop on 2019/03/30.
 */

@Repository
public interface ExternalRoleRepository extends JpaRepository<ExternalRole, Long> {


    @Query("SELECT u FROM ExternalRole u WHERE u.roleCode = :roleCode and u.provinceCode = :provinceCode")
    ExternalRole getByRoleCodeRoleProvince(@Param("roleCode") String roleCode, @Param("provinceCode") String provinceCode);

    @Query("SELECT u FROM ExternalRole u WHERE u.roleCode = :roleCode")
    List<ExternalRole> findByRoleCode( @Param("roleCode") String roleCode);

    @Query("SELECT u FROM ExternalRole u WHERE u.externalRoleCode = :externalRoleCode")
    ExternalRole findByExternalRoleCode( @Param("externalRoleCode") String externalRoleCode);


    @Query("SELECT u FROM ExternalRole u WHERE u.roleCode = :roleCode")
    List<ExternalRole> updateAccessRight(
           @Param("roleCode") String roleCode);


    @Query("SELECT accessRightJson FROM ExternalRole u WHERE u.externalRoleCode = :externalRoleCode")
    String getAccessRightJson ( @Param("externalRoleCode") String externalRoleCode);


}
