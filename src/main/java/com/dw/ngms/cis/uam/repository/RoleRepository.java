package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {

    @Query("SELECT u FROM Roles u WHERE u.roletype=:roletype order by u.rolename")
    List<Roles> findByRoleType(@Param("roletype") String roletype);

    @Query("SELECT u FROM Roles u WHERE u.rolecode=?1")
	Roles findByRoleCode(String roleCode);

    @Query("SELECT menuJson FROM Roles u WHERE u.rolecode=?1")
    String getMenuByRoleCode(String roleCode);


}
