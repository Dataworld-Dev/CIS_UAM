package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.ExternalUser;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {



    @Query("SELECT u FROM ExternalUser u WHERE u.usercode = :usercode")
    ExternalUser findByUserCode(@Param("usercode") String usercode);


    @Query(value = "SELECT user_role_seq.nextval FROM dual", nativeQuery =
            true)
    Long getRoleId();
}
