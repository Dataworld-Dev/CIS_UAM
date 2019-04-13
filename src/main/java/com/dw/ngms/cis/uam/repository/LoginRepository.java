package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/03/24.
 */
@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName = :username")
    User findByLoginName(@Param("username") String username);
}
