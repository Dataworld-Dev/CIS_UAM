package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/05.
 */

@Data
@Getter
@Setter
@ToString
public class ExternalUserDTO implements Serializable {


    private String organizationtypecode;

    private String organizationtypename;

    private String ppno;

    private String usercode;

    private String practicename;

    private String postaladdressline1;

    private String postaladdressline2;

    private String postaladdressline3;

    private String postalcode;

    private String communicationmodetypecode;

    private String communicationmodetypename;

    private String securityquestiontypecode1;

    private String securityquestion1;

    private String securityanswer1;

    private String securityquestiontypecode2;

    private String securityquestion2;

    private String securityanswer2;

    private String securityquestiontypecode3;

    private String securityquestion3;

    private String securityanswer3;;

    private Date createdDate;

    private String subscribenews;

    private String subscribenotifications;

    private String subscribeevents;

    private String sectorCode;

    private String sectorName;

}
