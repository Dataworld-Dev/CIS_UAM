package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by swaroop on 2019/04/11.
 */

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Long> {



    @Query("SELECT issueStatus FROM IssueLog u WHERE u.issueId = :issueId")
    String findIssueStatus(@Param("issueId") Long issueId);


     @Query("SELECT u  FROM IssueLog u WHERE u.issueId = :issueId")
     IssueLog findIssueById(@Param("issueId") Long issueId);

}
