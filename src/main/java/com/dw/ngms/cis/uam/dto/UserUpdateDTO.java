package com.dw.ngms.cis.uam.dto;

import com.dw.ngms.cis.uam.enums.ApprovalStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/05.
 */


@Getter
@Setter
public class UserUpdateDTO implements Serializable {

    private String firstName;
    private String userCode;
    private String userTypeCode;
    private String userTypeName;
    private String title;
    private String userName;
    private String firstLogin;
    private String surname;
    private String isApprejuserCode;
    private String telephoneNo;
    private String email;
    private ApprovalStatus isApproved;
    private String mobileNo;
    private String rejectionReason;
    private String isApprejuserName;
    private Date isApprejDate;
    private ExternalUserDTO externaluser;

}
