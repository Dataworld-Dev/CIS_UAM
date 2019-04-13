package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.DashBoard;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/12.
 */
@Repository
public interface DashBoardRepository extends JpaRepository<DashBoard, Long> {



    @Query("SELECT dashBoardJson FROM DashBoard u WHERE u.dashBoardID = :dashBoardID")
    String findDashBoardJson(@Param("dashBoardID") Long dashBoardID);

}
