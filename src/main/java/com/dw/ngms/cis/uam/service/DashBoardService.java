package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.repository.DashBoardRepository;
import com.dw.ngms.cis.uam.repository.IssueLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/12.
 */
@Service
public class DashBoardService {

    @Autowired
    private DashBoardRepository dashBoardRepository;


    public String findDashBoardJson(Long dashBoardId){
        return this.dashBoardRepository.findDashBoardJson(dashBoardId);
    }


}
